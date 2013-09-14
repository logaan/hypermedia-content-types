(ns user
  (:require [hypermedia-content-types.core :as c]))

(def application (atom nil))

(defn start []
  (reset! application {:http-kit (c/-main)}))

(defn stop []
  ((:http-kit @application)))

