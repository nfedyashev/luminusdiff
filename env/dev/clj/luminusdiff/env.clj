(ns luminusdiff.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [luminusdiff.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[luminusdiff started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[luminusdiff has shut down successfully]=-"))
   :middleware wrap-dev})
