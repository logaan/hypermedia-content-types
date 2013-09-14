(ns hypermedia-content-types.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [liberator.core :refer  [resource defresource]]
            [compojure.route :as route] 
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes ANY GET POST]]))

(defresource index
  :available-media-types ["text/html"]
  :handle-ok (fn [ctx]
               (format "<html>It's %d milliseconds since the beginning of the
                       epoch." (System/currentTimeMillis))))

(defresource secret
  :available-media-types ["text/html"]
  :exists? (fn [ctx]
             (= "tiger" (get-in ctx [:request :params :word])))
  :handle-ok "You found the secret word!"
  :handle-not-found "Uh, that's the wrong word. Guess again!" )

(defroutes all-routes
  (ANY "/" [] index)
  (ANY "/secret" [] secret)
  (route/not-found "<p>Page not found.</p>"))

(def reloading-handler
  (reload/wrap-reload (site #'all-routes)))

(defn -main [& args]
  (run-server reloading-handler {:port 8080}))

