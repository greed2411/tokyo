(ns tokyo.guess-test
  (:require [clojure.test :refer :all]
            [tokyo.guess :refer :all]))


(deftest extraction-test
  (is (= (extract-ext-mimetype-text "resources/sample.txt")
         {:mime-type "text/plain"
          :ext ".txt"
          :text "Hello.\n\nI'm a simple plain text file used of testing the api.\n\nThanks!\n"})))

