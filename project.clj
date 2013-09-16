(defproject hypermedia-content-types "0.1.0-SNAPSHOT"
  :description "A hypermedia web application. It aims to expose resources via a
               number of content types. My original idea was to use custom
               written ring middleware that would take response data in a Siren
               like format and serialise it to json, edn or xhtml depending on
               the client's desire."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [liberator "0.9.0"]
                 [org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [ring/ring-devel "1.1.8"]
                 [ring/ring-core "1.1.8"]
                 [http-kit "2.0.0"]]
  :profiles {:dev {:source-paths ["dev"]}})
