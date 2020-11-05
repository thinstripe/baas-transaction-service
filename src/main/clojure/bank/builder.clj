(ns bank.builder
  (:require [clojure.java.data.builder :as builder]
            [taoensso.timbre :as log :only [debug]])
  (:use [clojure.reflect]))

(defonce ^:private no-args (into-array []))

(defonce ^:private no-class-args (into-array Class []))

(defn to-builder [clazz props]
  (log/debug clazz props)
  (let [builder-clazz (-> (clojure.reflect/typename clazz)
                          (str "$Builder")
                          Class/forName)
        builder-method (.getMethod clazz "builder" no-class-args)
        instance (.newInstance clazz)
        builder (.invoke builder-method instance no-args)]
    (builder/to-java clazz builder-clazz builder props {})))
