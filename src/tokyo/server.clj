(ns tokyo.server
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [ring.logger :as logger]
            [tokyo.guess :as guess]))


(defn upload-extract
  "takes the form uploaded file and runs tika parser"
  [request]
  (-> request
      :params
      :file
      :tempfile
      str
      guess/extract-ext-mimetype-text
      response))


(defroutes app-routes
  "all the API URI routes"
  (GET "/" [] "Hello World")
  (GET "/ping" [] (response {:msg "pong"}))
  (POST "/file" request (upload-extract request))
  (route/not-found "You must be new here!"))


(def app-handler
  (handler/site app-routes))


(def app
  (-> app-handler
      wrap-params
      wrap-multipart-params
      wrap-json-response
      logger/wrap-with-logger))


(def port-number
  (try
    (Integer/valueOf
     (System/getenv "TOKYO_PORT"))
    (catch Exception e 3000)))