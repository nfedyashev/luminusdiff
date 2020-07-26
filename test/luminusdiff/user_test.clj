(ns luminusdiff.user-test
  (:require [luminusdiff.user :as sut]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.test :refer [deftest is testing]])
  (:import [java.io File])
  )


(deftest test-retrieve-lein-template-versions
  (let [expected (-> "lein-template-versions.edn"
            io/resource
            slurp
            edn/read-string)]
    (is (= expected (sut/retrieve-lein-template-versions)))))

(deftest test-find-branches
  (let [expected (-> "remote-branches.edn"
                     io/resource
                     slurp
                     edn/read-string)]
    (is (= expected (sut/retrieve-branches)))))

;; (clojure.java.shell/sh "test" "-f" "/tmp/hey/project.clj")
;; (clojure.java.shell/sh "grep" "cljs" "/tmp/hey/project.clj")

(deftest test-lein-generate-from-template
  (let [tmp-dir "/tmp/hey"]
    (clojure.java.shell/sh "rm" "-rf" tmp-dir)
    (clojure.java.shell/sh "mkdir" "-p" tmp-dir)

    (binding [clojure.java.shell/*sh-dir* tmp-dir]
      (let [res (sut/lein-generate-from-template {:option "+cljs" :version "3.80"})
            _ (println res)]
        (is (= 0 (:exit res)))
        (is (= 0 (-> (clojure.java.shell/sh "test" "-f" (str tmp-dir "/project.clj")) :exit)))
        (is (= 0 (-> (clojure.java.shell/sh "grep" "cljs" (str tmp-dir "/project.clj")) :exit)))
        (is (= 0 (-> (clojure.java.shell/sh "grep" "3.80" (str tmp-dir "/README.md")) :exit)))))))


(deftest test-all-combinations
  (let [expected ["3.82"
                  "3.82+aleph"
                  "3.82+http-kit"
                  "3.82+immutant"
                  "3.82+jetty"
                  "3.82+undertow"
                  "3.82+h2"
                  "3.82+postgres"
                  "3.82+mysql"
                  "3.82+mongodb"
                  "3.82+datomic"
                  "3.82+sqlite"
                  "3.82+graphql"
                  "3.82+swagger"
                  "3.82+service"
                  "3.82+cljs"
                  "3.82+reagent"
                  "3.82+re-frame"
                  "3.82+kee-frame"
                  "3.82+shadow-cljs"
                  "3.82+boot"
                  "3.82+auth"
                  "3.82+auth-jwe"
                  "3.82+oauth"
                  "3.82+hoplon"
                  "3.82+cucumber"
                  "3.82+sassc"
                  "3.82+war"
                  "3.82+site"
                  "3.82+kibit"
                  "3.82+servlet"
                  "3.82+basic"
                  "3.81"
                  "3.81+aleph"
                  "3.81+http-kit"
                  "3.81+immutant"
                  "3.81+jetty"
                  "3.81+undertow"
                  "3.81+h2"
                  "3.81+postgres"
                  "3.81+mysql"
                  "3.81+mongodb"
                  "3.81+datomic"
                  "3.81+sqlite"
                  "3.81+graphql"
                  "3.81+swagger"
                  "3.81+service"
                  "3.81+cljs"
                  "3.81+reagent"
                  "3.81+re-frame"
                  "3.81+kee-frame"
                  "3.81+shadow-cljs"
                  "3.81+boot"
                  "3.81+auth"
                  "3.81+auth-jwe"
                  "3.81+oauth"
                  "3.81+hoplon"
                  "3.81+cucumber"
                  "3.81+sassc"
                  "3.81+war"
                  "3.81+site"
                  "3.81+kibit"
                  "3.81+servlet"
                  "3.81+basic"]]
    (is (= expected (sut/all-combinations '("3.82" "3.81"))))))
