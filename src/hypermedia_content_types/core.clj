(ns hypermedia-content-types.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [compojure.route :as route] 
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST]]))

(defroutes all-routes
  (GET "/" [] "handling-page")
  (route/not-found "<p>Page not found.</p>"))

(def reloading-handler
  (reload/wrap-reload (site #'all-routes)))

(defn -main [& args]
  (run-server reloading-handler {:port 8080}))

