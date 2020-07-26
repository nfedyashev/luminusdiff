(ns luminusdiff.routes.home
  (:require [luminusdiff.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page [request]
  (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page [request]
  (layout/render request "about.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (GET "/graphiql" request (layout/render request "graphiql.html"))
  (GET "/about" request (about-page request)))

