(ns user
  (:require [luminusdiff.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [luminusdiff.core :refer [start-app]]
            [luminusdiff.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'luminusdiff.core/repl-server))

(defn stop []
  (mount/stop-except #'luminusdiff.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'luminusdiff.db.core/*db*)
  (mount/start #'luminusdiff.db.core/*db*)
  (binding [*ns* 'luminusdiff.db.core]
    (conman/bind-connection luminusdiff.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


