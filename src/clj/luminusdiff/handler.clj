(ns luminusdiff.handler
  (:require 
            [luminusdiff.layout :refer [error-page]]
            [luminusdiff.routes.home :refer [home-routes]]
            [luminusdiff.routes.oauth :refer [oauth-routes]]
            [compojure.core :refer [routes wrap-routes]]
            [ring.util.http-response :as response]
            [luminusdiff.middleware :as middleware]
            [compojure.route :as route]
            [luminusdiff.env :refer [defaults]]
            [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(mount/defstate app
  :start
  (middleware/wrap-base
    (routes
      (-> #'home-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats))
          #'oauth-routes
          (route/not-found
             (:body
               (error-page {:status 404
                            :title "page not found"}))))))

