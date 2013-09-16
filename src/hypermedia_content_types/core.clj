(ns hypermedia-content-types.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [hiccup.core :refer [html]]
            [liberator.core :refer  [resource defresource]]
            [liberator.representation :refer [render-map-generic]]
            [compojure.route :as route] 
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes ANY GET POST]]))

(defrecord Product [name category price])

(def baseball-cap
  (Product. "Baseball Cap" "Headwear" "$20"))

(def nike-air
  (Product. "Nike Air" "Shoe" "$120"))

(def products [baseball-cap nike-air])

(defn store-data [ctx]
  products)

(def default-media-types
  ["application/json"
   "application/edn"
   "application/clojure"
   "application/xhtml+xml"
   "text/csv"
   "text/plain"
   "text/tab-separated-values" 
   "text/html"])

(defmethod render-map-generic "text/html" [data context]
  (html [:div
         [:h1 (-> data :class first)]
         [:h2 "Properties"]
         [:dl
          (mapcat (fn [[key value]] `([:dt ~key] [:dd ~value]))
               (:properties data))]]))

(defmethod render-map-generic "application/siren+edn" [data context]
  (binding [*print-dup* false]
    (with-out-str (pr data))))

(defresource product
  :allowed-methods [:get]
  :available-media-types ["application/siren+edn" "text/html"]
  :handle-ok (fn [ctx]
               {:class ["product"]
                :properties (into {} baseball-cap)
                :actions [{:name "delete-item"
                           :title "Delete Item"
                           :method "DELETE"
                           :href "/products/1"
                           :type "application/x-www-form-urlencoded"}]
                :links [{:rel ["self"] :href "/products/1"}
                        {:rel ["listing"] :href "/products"}]}))

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
  (ANY "/products/:id" [] product)
  (route/not-found "<p>Page not found.</p>"))

(def reloading-handler
  (reload/wrap-reload (site #'all-routes)))

(defn -main [& args]
  (run-server #'reloading-handler {:port 8080}))

