(ns bank.params)

(defn to-param-string [m]
  (->> (map (fn [[k v]] (str (name k) "=" v)) m)
       (clojure.string/join "&")))