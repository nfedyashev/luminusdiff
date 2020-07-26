(ns luminusdiff.config)

(def opts
  {:servers
   ["aleph"
    "http-kit"
    "immutant"
    "jetty"
    "undertow"]

   :databases
   ["h2"
    "postgres"
    "mysql"
    "mongodb"
    "datomic"
    "sqlite"]

   :service-api
   ["graphql"
    "swagger"
    "service"]

   :clojure-script
   ["cljs"
    "reagent"
    "re-frame"
    "kee-frame"
    "shadow-cljs"]

   :misc
   ["boot"
    "auth"
    "auth-jwe"
    "oauth"
    "hoplon"
    "cucumber"
    "sassc"
    "war"
    "site"
    "kibit"
    "servlet"
    "basic"]})
