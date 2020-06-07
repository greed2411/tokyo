(defproject tokyo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [ring/ring-devel "1.8.1"]
                 [ring/ring-json "0.5.0" :exclusions [joda-time]]
                 [ring-logger "1.0.1"]
                 [ring/ring-mock "0.4.0"]
                 [cheshire "5.10.0"]
                 [compojure "1.6.1" :exclusions [joda-time]]
                 [com.novemberain/pantomime "2.11.0"]
                 [org.apache.pdfbox/pdfbox "2.0.19"]
                 [org.xerial/sqlite-jdbc "3.31.1"]
                 [org.slf4j/slf4j-simple "1.6.2"]]
  :main ^:skip-aot tokyo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
