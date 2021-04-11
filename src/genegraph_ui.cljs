(ns genegraph-ui
  (:require ["firebase/app" :as firebase-app]
            ["firebase/auth"] ; must be loaded for firebase-app.auth to work
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [re-graph.core :as re-graph]
            [genegraph-ui.pages.home.views :as home]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.routes :as routes]
            [genegraph-ui.config :refer [firebase-config]])) 

(enable-console-print!)

(goog-define BACKEND_WS "ws://localhost:8888/ws")
(goog-define BACKEND_HTTP "http://localhost:8888/api")
(goog-define FIREBASE_CONFIG_NAME "clingen-dev")

(defn ^:dev/after-load mount-root []
  (println "[main] reloaded lib:")
  (routes/init-routes!)
  (rdom/render [routes/router-component {:router routes/router}]
               (.getElementById js/document "app")))

(re-frame/reg-event-db
  ::initialize-db
  (fn [db _]
    (if db
      db
      {:current-route nil
       :home/search-topic :gene})))

(defn ^:export init []
  (re-frame/dispatch-sync [::initialize-db])
  (let [app-config (get firebase-config (keyword FIREBASE_CONFIG_NAME))]
    (js/console.log "app-config:" (clj->js app-config))
    (.initializeApp firebase-app (clj->js app-config))
    (js/console.log "firebase initialized"))
  (.log js/console "backend-ws: " BACKEND_WS ", backend-http: " BACKEND_HTTP)
  (re-frame/dispatch [::re-graph/init
                      {:ws {:url BACKEND_WS}
                       :http {:url BACKEND_HTTP}}])

  (-> (.auth firebase-app) (.onAuthStateChanged #(dispatch [:common/auth-state-change])))
  (mount-root))
 
