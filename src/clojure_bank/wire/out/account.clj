(ns clojure-bank.wire.out.account
  (:require [schema.core :as s]))

(s/defschema Account {:account  s/Uuid
                      :agency   s/Str
                      :balance  s/Num})