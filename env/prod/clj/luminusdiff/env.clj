(ns luminusdiff.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[luminusdiff started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[luminusdiff has shut down successfully]=-"))
   :middleware identity})
