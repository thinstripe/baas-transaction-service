(ns bank.tm.transactions
  (:require [clj-http.client :as client]
            [cheshire.core :as ch]
            [taoensso.timbre :as log :only [info error warn]]
            [bank.core :as b]
            [bank.params :as p]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [bank.logging :as l])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn get-customer [env customer-id]
  (log/info "tm/get-customer" customer-id)
  (some->
    (client/get
      (str (:core-api-base env) "/v1/customers/" customer-id)
      {:accept  :json
       :headers {:X-Auth-Token (:sa-token env)}})
    :body
    (ch/parse-string true)))

(defn get-account [env account_id]
  (log/info "tm/get-account" account_id)
  (some->
    (client/get
      (str (:core-api-base env) "/v1/accounts/" account_id "?view=ACCOUNT_VIEW_INCLUDE_BALANCES")
      {:accept  :json
       :headers {:X-Auth-Token (:sa-token env)}})
    :body
    (ch/parse-string true)))

(defn get-balances [env opts]
  (log/info "tm/get-balances" opts)
  (some->
    (client/get
      (str (:core-api-base env) "/v1/balances?"
           (some-> {:page_size 100}
                   (merge opts)
                   (select-keys [:order_by :page_size :account_id :time_range.from_value_time :time_range.to_value_time])
                   (p/to-param-string)))
      {:accept  :json
       :headers {:X-Auth-Token (:sa-token env)}})
    :body
    (ch/parse-string true)))

(defn balances [env {:keys [account_id] :as opts}]
  (log/info "tm/balances" opts)
  (let [account (when account_id
                  (get-account env account_id))
        customer-id (some-> account :stakeholder_ids first)
        customer-future (future (get-customer env customer-id))
        balances-future (future (get-balances env opts))]
    {:account account :customer @customer-future :balances @balances-future}))

(defn transactions [env {:keys [account-id]}]
  (log/info "tm/transactions" account-id)
  (balances env {:account_id account-id}))

(defn- posting-instruction-batch [env command-kw batch-id instructions]
  (let [uri (case command-kw
              :asyncCreate "posting-instruction-batches"
              :validate "create-posting-instruction-batch")
        command (name command-kw)
        client-id (str (clojure.string/capitalize command) "PostingInstructionBatch")]
    (some->
      (client/post
        (str (:core-api-base env) "/v1/" uri ":" command)
        {:content-type :json
         :accept       :json
         :headers      {:X-Auth-Token (:sa-token env)}
         :body         (-> {:request_id                (b/generate-timestamp-id)
                            :posting_instruction_batch {:client_id            client-id
                                                        :client_batch_id      batch-id
                                                        :posting_instructions instructions}}
                           ch/generate-string)})
      :body
      (ch/parse-string true))))

(defn- validate-posting-instruction-batch [env batch-id instructions]
  (posting-instruction-batch env :validate batch-id instructions))

(defn- async-create-posting-instruction-batch [env batch-id instructions]
  (posting-instruction-batch env :asyncCreate batch-id instructions))

(defn- get-posting-instruction-batch [env batch-id]
  (log/info "tm/get-posting-instruction-batch" batch-id)
  (some->
    (client/get
      (str (:core-api-base env) "/v1/posting-instruction-batches/" batch-id)
      {:accept :json :headers {:X-Auth-Token (:sa-token env)}})
    :body
    (ch/parse-string true)))

(defn- list-posting-instruction-batch [env batch-id]
  (log/info "tm/list-posting-instruction-batch" batch-id)
  (some->
    (client/get
      (str (:core-api-base env) "/v1/posting-instruction-batches?" (p/to-param-string {:page_size 100 :client_batch_ids batch-id}))
      {:accept :json :headers {:X-Auth-Token (:sa-token env)}})
    :body
    (ch/parse-string true)))

(defn- find-batch [response batch-id]
  (some->> (:posting_instruction_batches response)
           (filter (fn [b] (= (:client_batch_id b) (str batch-id))))
           first))

(defn- batch-status [response batch-id]
  (some-> (find-batch response batch-id) :status))

(defn- posting-accepted? [response batch-id]
  (let [status (batch-status response batch-id)]
    (log/info batch-id status)
    (= status "POSTING_INSTRUCTION_BATCH_STATUS_ACCEPTED")))

(defn- get-posting-error [response batch-id]
  (->> (find-batch response batch-id)
       :posting_instructions
       first
       :posting_violations
       first
       :type))

(defn deposit [env {:keys [account-id amount denomination] :as details}]
  (log/info "tm/deposit" details)
  (let [batch-id (b/generate-uuid)
        instructions [{:client_transaction_id   batch-id
                       :inbound_hard_settlement {:amount              (str amount)
                                                 :denomination        denomination
                                                 :target_account      {:account_id account-id}
                                                 :internal_account_id "1"
                                                 :advice              false}}]
        {:keys [id done] :as posting-response} (async-create-posting-instruction-batch env batch-id instructions)
        balance-amount (some-> (get-account env account-id) :account_balance :live_balances last :amount)]
    (log/info "posting-response" posting-response)
    (log/info "balances-amount" balance-amount)
    (let [list-response (list-posting-instruction-batch env batch-id)]
      (log/info "list-response")
      (l/log-pretty list-response)
      (when-not (posting-accepted? list-response batch-id)
        (log/warn (get-posting-error list-response batch-id))))
    balance-amount))

(defn transfer [env {:keys [payer-account-id payee-account-id amount denomination] :as details}]
  (log/info "tm/transfer" details)
  (log/info "payer balances")
  (l/log-pretty (balances env {:account_id payer-account-id}))
  (log/info "payee balances")
  (l/log-pretty (balances env {:account_id payee-account-id}))
  (let [batch-id (b/generate-uuid)
        instructions [{:client_transaction_id batch-id
                       :transfer              {:amount                  (str amount)
                                               :denomination            denomination
                                               :debtor_target_account   {:account_id payer-account-id}
                                               :creditor_target_account {:account_id payer-account-id}
                                               :advice                  false}}]
        {:keys [id done] :as posting-response} (async-create-posting-instruction-batch env batch-id instructions)]
    (log/info "posting-response")
    (l/log-pretty posting-response)
    (let [list-response (list-posting-instruction-batch env batch-id)]
      (log/info "outbound-list-response")
      (l/log-pretty list-response)
      (when-not (posting-accepted? list-response batch-id)
        (log/warn (get-posting-error list-response batch-id)))
      (let [payer-balances-future (future (balances env {:account_id payer-account-id}))
            payee-balances-future (future (balances env {:account_id payee-account-id}))]
        {:payer @payer-balances-future :payee @payee-balances-future}))))