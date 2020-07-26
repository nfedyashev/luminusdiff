(ns luminusdiff.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [luminusdiff.core-test]))

(doo-tests 'luminusdiff.core-test)

