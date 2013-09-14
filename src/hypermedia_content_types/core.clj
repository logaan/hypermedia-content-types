(ns hypermedia-content-types.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [liberator.core :refer  [defresource]]
            [compojure.route :as route] 
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST]]))

(defresource index
  :available-media-types ["text/html"]
  :handle-ok "<html>Hello, Internet.</html>")

(defroutes all-routes
  (GET "/" [] index)
  (route/not-found "<p>Page not found.</p>"))

(def reloading-handler
  (reload/wrap-reload (site #'all-routes)))

(defn -main [& args]
  (run-server reloading-handler {:port 8080}))

