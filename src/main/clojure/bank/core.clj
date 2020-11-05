(ns bank.core
  (:require [clj-uuid :as uuid]))

(defn generate-uuid [] (uuid/v1))

(defn generate-timestamp-id []
  (-> (generate-uuid) uuid/get-timestamp str))