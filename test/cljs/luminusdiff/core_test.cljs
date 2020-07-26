(ns luminusdiff.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [pjstadig.humane-test-output]
            [luminusdiff.core :as rc]))

(deftest test-home
  (is (= true true)))

