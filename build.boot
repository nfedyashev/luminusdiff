(set-env!
 :dependencies '[[adzerk/boot-test "1.2.0" :scope "test"]
                 [clojure.java-time "0.3.2"]
                 [com.fasterxml.jackson.core/jackson-core "2.9.7"]
                 [com.fasterxml.jackson.datatype/jackson-datatype-jdk8 "2.9.7"]
                 [compojure "1.6.1"]
                 [cprop "0.1.13"]
                 [funcool/struct "1.3.0"]
                 [luminus-immutant "0.2.4"]
                 [luminus-transit "0.1.1"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.5"]
                 [metosin/muuntaja "0.6.1"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.14"]
                 [nrepl "0.4.5"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.4.1"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.webjars.bower/tether "1.4.4"]
                 [org.webjars/bootstrap "4.1.3"]
                 [org.webjars/font-awesome "5.5.0"]
                 [org.webjars/jquery "3.3.1-1"]
                 [org.webjars/webjars-locator "0.34"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.4"]]
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
                              [expound "0.7.1"]
                              [pjstadig/humane-test-output "0.9.0"]
                              [prone "1.6.1"]
                              [ring/ring-devel "1.7.1"]
                              [ring/ring-mock "0.3.2"]]))
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
