(ns tokyo.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [tokyo.server :refer :all]))


(defn -main []
  (jetty/run-jetty app {:port port-number}))
