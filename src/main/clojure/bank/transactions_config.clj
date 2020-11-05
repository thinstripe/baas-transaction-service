(ns bank.transactions-config
  (:require [clojure.java.data :as d]
            [java-time :as jt]
            [taoensso.timbre :as log :only [info]]
            [environ.core :refer [env]]
            [bank.builder :as b]
            [bank.tm.transactions :as tm :only [balances transactions]]
            [bank.logging :as l])
  (:import [com.ow.banking.bank BankTransactions BankDeposit BankTransfer]
           [com.ow.banking.bank.model.transaction GetTransactionsResponse TxtArray]
           [com.ow.banking.bank.model.transaction PostDepositResponse]
           [com.ow.banking.bank.model.transaction TransferResponse]))

(gen-class :name ^{org.springframework.boot.autoconfigure.condition.ConditionalOnProperty {:name "cbs.provider" :havingValue "tm"}
                   org.springframework.context.annotation.Configuration                   {}} com.ow.banking.bank.tm.BankTransactionsConfiguration
           :methods [[^{org.springframework.context.annotation.Bean {}}
                      tmBankTransactions [] com.ow.banking.bank.BankTransactions]
                     [^{org.springframework.context.annotation.Bean {}}
                      tmBankDeposit [] com.ow.banking.bank.BankDeposit]
                     [^{org.springframework.context.annotation.Bean {}}
                      tmBankTransfer [] com.ow.banking.bank.BankTransfer]])

(defn -tmBankTransactions [_]
  (letfn [(from-transactions-request [req]
            (-> (bean req)
                (select-keys [:orderBy :rowSize :custAccountNo :startDate :endDate :period])
                (clojure.set/rename-keys [:orderBy :order_by
                                          :rowSize :page_size
                                          :custAccountNo :account_id
                                          :startDate :time_range.from_value_time
                                          :endDate :time_range.to_value_time])
                (update :order_by #(->> % clojure.string/upper-case (str "ORDER_BY_VALUE_TIME_" )))))
          (to-transactions-response [{:keys [customer account balances]}]
            (->> {:hasMore      ""
                  :totalRecords (int 0)
                  :txtArray     (some->> balances
                                         (map (fn [balance]
                                                {:txnAmount                (-> balance :amount Integer/parseInt)
                                                 :txnDate                  (jt/local-date 2020 10 29) ;TODO
                                                 :category                 (:asset balance)
                                                 :destinationAccountNumber (:account_id balance)
                                                 :destinationName          ""}))
                                         (map #(d/to-java TxtArray %))
                                         (into []))}
                 (b/to-builder GetTransactionsResponse)))]
    (reify BankTransactions
      (getTransactions [_ req]
        (->> (from-transactions-request req)
             (tm/transactions env)
             to-transactions-response)))))

(defn -tmBankDeposit [_]
  (letfn [(from-deposit-request [req]
            (some-> (bean req)
                    (clojure.set/rename-keys {:custAccountNo :account-id :txnAmount :amount :txnCurrency :denomination})
                    (select-keys [:account-id :amount :denomination])))
          (to-deposit-response [balance-amount]
            (->> (Float/parseFloat balance-amount)
                 (hash-map :inAccountBalance)
                 (b/to-builder PostDepositResponse)))]
    (reify BankDeposit
      (deposit [_ req]
        (some->> (from-deposit-request req)
                 (tm/deposit env)
                 to-deposit-response)))))

(defn -tmBankTransfer [_]
  (letfn [(from-transfer-request [req]
            (some-> (bean req)
                    (clojure.set/rename-keys {:payerAccountNo :payer-account-id :payeeAccountNo :payee-account-id :txnAmount :amount :txnCurrency :denomination})
                    (select-keys [:payer-account-id :payee-account-id :amount :denomination])))
          (to-transfer-response [{:keys [payer payee] :as all}]
            (l/log-pretty all)
            ;TODO
            (->> {:cardNumber           ""
                  :custAccountNo        (get-in payer [:account :id])
                  :custAccountType      ""
                  :custName             ""
                  :currency             (-> (get-in payer [:account :permitted_denominations]) first)
                  :productId            ""
                  :txnDate              ""
                  :txnAmount            (int 0)
                  :inAmount             (int 0)
                  :outAccountBalance    (int 0)
                  :holdAmount           (int 0)
                  :outSuspendBillNumber ""
                  :payeeCardNo          ""
                  :payeeCustAccountNo   ""
                  :payeeCustAccountType ""
                  :payeeCustName        ""
                  :payeeCurrency        ""
                  :payeeAccountProduct  ""
                  :inAccountBalance     (int 0)
                  :newHoldId            ""
                  :inSuspendBillNumber  ""}
                 (b/to-builder TransferResponse)))]
    (reify BankTransfer
      (transfer [_ req]
        (some->> (from-transfer-request req)
                 (tm/transfer env)
                 to-transfer-response)))))