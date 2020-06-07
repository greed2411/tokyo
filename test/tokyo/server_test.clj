(ns tokyo.server-test
  (:require [clojure.test :refer :all]
            [tokyo.server :refer :all]
            [ring.mock.request :as mock]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [cheshire.core :refer :all]
            [ring.util.response :refer [response]]))


(deftest app-handler-test
  (is (= (app (mock/request :get "/"))
         {:status  200
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :body    "Hello World"})))


(deftest app-handler-json-test
  (is (= (app (mock/request :get "/ping"))
         {:status  200
          :headers {"Content-Type" "application/json; charset=utf-8"}
          :body    (generate-string {:msg "pong"})})))


;; https://alcamech.com/file-upload-in-clojure-endpoint-test/
(defn create-temp-file
  "Simulates the output from ring's wrap-multipart-params."
  [file-path]
  (let [filename-start (inc (string/last-index-of file-path "/"))
        file-extension-start (string/last-index-of file-path ".")
        file-name (subs file-path filename-start file-extension-start)
        file-extension (subs file-path file-extension-start)
        temp-file (java.io.File/createTempFile file-name file-extension)]
    (io/copy (io/file file-path) temp-file)
    temp-file))


(deftest upload-test
  (testing "Testing POST request to /file"
    (let [filecontent {:tempfile (create-temp-file "resources/sample.txt")
                       :content-type "text/plain"
                       :filename     "sample.txt"}
          response (app (-> (assoc
                             (mock/request :post "/file")
                             :params {:file filecontent}
                             :multipart-params {"file" filecontent})))]
      (is (= (:body response)
             (generate-string {:mime-type "text/plain"
                               :ext ".txt"
                               :text "Hello.\n\nI'm a simple plain text file used of testing the api.\n\nThanks!\n"}))))))
