(ns bank.logging
  (:require [taoensso.timbre :as log]
            [clojure.pprint :as pp]))

(defn log-pretty [object]
  (log/info (with-out-str (pp/pprint object))))
