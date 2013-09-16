(ns hypermedia-content-types.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [liberator.core :refer  [resource defresource]]
            [compojure.route :as route] 
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes ANY GET POST]]))

(defrecord Product [name category price])

(defn store-data [ctx]
  [(Product. "Baseball Cap" "Headwear" "$20")
   (Product. "Nike Air" "Shoe" "$120")])

(def default-media-types
  ["application/json"
   "application/edn"
   "application/clojure"
   "application/xhtml+xml"
   "text/csv"
   "text/plain"
   "text/tab-separated-values" 
   "text/html"])

(defresource index
  :allowed-methods [:options :head :get :post :put :delete]
  :available-media-types default-media-types
  :handle-ok store-data
  :handle-created store-data)

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

