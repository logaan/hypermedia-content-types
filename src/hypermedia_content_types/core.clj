(ns hypermedia-content-types.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [liberator.core :refer  [resource defresource]]
            [compojure.route :as route] 
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes ANY GET POST]]))

(defn seconds-since-epoch [ctx]
  (format "<html>It's %d milliseconds since the beginning of the
          epoch." (System/currentTimeMillis)))

(defresource index
  :allowed-methods [:options :head :get :post :put :delete]
  :available-media-types ["text/plain"]
  :handle-ok seconds-since-epoch
  :handle-created seconds-since-epoch)

(defresource secret
  :available-media-types ["text/html"]
  :exists? (fn [ctx]
             (= "tiger" (get-in ctx [:request :params :word])))
  :handle-ok "You found the secret word!"
  :handle-not-found "Uh, that's the wrong word. Guess again!" )

(defresource babel
  :available-media-types ["text/plain" "text/html" "application/json"
                          "application/clojure;q=0.9"]
  :handle-ok 
  (fn [ctx]
    (let [media-type (get-in ctx [:representation :media-type])]
      (condp = media-type
        "text/plain" "You requested plain text"
        "text/html" "<html><h1>You requested HTML</h1></html>"
        {:message "You requested a media type" :media-type media-type})))
  :handle-not-acceptable "Uh, Oh, I cannot speak those languages!")

(defroutes all-routes
  (ANY "/" [] index)
  (ANY "/secret" [] secret)
  (ANY "/babel" []  babel)
  (route/not-found "<p>Page not found.</p>"))

(def reloading-handler
  (reload/wrap-reload (site #'all-routes)))

(defn -main [& args]
  (run-server #'reloading-handler {:port 8080}))

