(ns tokyo.guess
  (:gen-class)
  (:require [pantomime.mime :as pm]
            [pantomime.extract :as pe]))


(defn extract-mime-type
  "when given a file path it suggests a mime-type to the file"
  [path]
  (pm/mime-type-of (java.io.File. path)))


(defn extract-extension
  "when given a file path it suggests an extension to the file"
  [path]
  (pm/extension-for-name (pm/mime-type-of path)))


(defn extract-text
  "when given a file path it extracts the text present inside the file"
  [path]
  (get (pe/parse path) :text))


(defn extract-ext-mimetype-text
  "calls all the above mentioned three functions
   which are dependent on the apache tika parser
   
   JSON Response:
   :mime-type (string) - the mime-type of the file. eg: application/msword, text/plain etc 
   :ext       (string) - the extension of the file. eg: .txt, .jpg etc
   :text      (string) - the text content of the file. "
  [path]
  {:mime-type (extract-mime-type path)
   :ext (extract-extension path)
   :text (extract-text path)})
