(ns user
  (:require [luminusdiff.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [luminusdiff.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'luminusdiff.core/repl-server))

(defn stop []
  (mount/stop-except #'luminusdiff.core/repl-server))

(defn restart []
  (stop)
  (start))


