(set-env!
 :dependencies '[[adzerk/boot-test "1.2.0" :scope "test"]
                 [cheshire "5.8.1"]
                 [clojure.java-time "0.3.2"]
                 [cprop "0.1.13"]
                 [funcool/struct "1.3.0"]
                 [luminus-immutant "0.2.5"]
                 [luminus-transit "0.1.1"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.10.0"]
                 [metosin/muuntaja "0.6.4"]
                 [metosin/reitit "0.3.5"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.webjars.npm/bulma "0.7.4"]
                 [org.webjars.npm/material-icons "0.3.0"]
                 [org.webjars/webjars-locator "0.36"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.12"]]
 :source-paths #{"src/clj"}
 :resource-paths #{"resources"})
(require '[clojure.java.io :as io]
         '[clojure.edn :as edn]
         '[adzerk.boot-test :refer [test]])


(deftask dev
  "Enables configuration for a development setup."
  []
  (set-env!
   :source-paths #(conj % "env/dev/clj")
   :resource-paths #(conj % "env/dev/resources")
   :dependencies #(concat % '[[prone "1.1.4"]
                              [ring/ring-mock "0.3.0"]
                              [ring/ring-devel "1.6.1"]
                              [pjstadig/humane-test-output "0.8.2"]
                              [expound "0.7.2"]
                              [pjstadig/humane-test-output "0.9.0"]
                              [prone "1.6.3"]
                              [ring/ring-devel "1.7.1"]
                              [ring/ring-mock "0.4.0"]]))
  (require 'pjstadig.humane-test-output)
  (let [pja (resolve 'pjstadig.humane-test-output/activate!)]
    (pja))
  (.. System (getProperties) (setProperty "conf" "dev-config.edn"))
  identity)

(deftask testing
  "Enables configuration for testing."
  []
  (dev)
  (set-env! :resource-paths #(conj % "env/test/resources"))
  (.. System (getProperties) (setProperty "conf" "test-config.edn"))
  identity)

(deftask prod
  "Enables configuration for production building."
  []
  (merge-env! :source-paths #{"env/prod/clj"}
              :resource-paths #{"env/prod/resources"})
  identity)

(deftask start-server
  "Runs the project without building class files.

  This does not pause execution. Combine with a wait task or use the \"run\"
  task."
  []
  (require 'luminusdiff.core)
  (let [-main (resolve 'luminusdiff.core/-main)]
    (with-pass-thru _
      (apply -main *args*))))

(deftask run
  "Starts the server and causes it to wait."
  []
  (comp
   (apply start-server "--" *args*)
   (wait)))

(deftask uberjar
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (prod)
   (aot :namespace #{'luminusdiff.core})
   (uber)
   (jar :file "luminusdiff.jar" :main 'luminusdiff.core)
   (sift :include #{#"luminusdiff.jar"})
   (target)))

