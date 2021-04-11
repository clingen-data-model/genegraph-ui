(ns curator.routes
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reagent.core :as reagent]
            [reitit.core :as r]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as rf]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [curator.pages.home.views :as home]
            [curator.pages.admin.views :as admin]
            [curator.pages.genes.views :as genes]
            [curator.pages.gene-validity.views :as gene-validity]
            [curator.pages.genes.events :as gene-events]
            [curator.pages.gene-validity.events :as gene-validity-events]))

;;; Events ;;;

(re-frame/reg-event-db ::navigated
  (fn [db [_ new-match]]
    (let [old-match   (:current-route db)
          controllers (rfc/apply-controllers (:controllers old-match) new-match)]
      (assoc db :current-route (assoc new-match :controllers controllers)))))

;;; Subscriptions ;;;

(re-frame/reg-sub ::current-route
  (fn [db]
    (:current-route db)))

;;; Routes ;;;

(defn href
  "Return relative url for given route. Url can be used in HTML links."
  ([k]
   (href k nil nil))
  ([k params]
   (href k params nil))
  ([k params query]
   (rfe/href k params query)))

(def routes
  ["/"
   [""
    {:name      :home
     :view      home/home
     :link-text "home"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params](js/console.log "Entering home page"))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving home page"))}]}]
   ["admin"
    {:name      :admin
     :view      admin/admin
     :link-text "admin"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (js/console.log "Entering admin page"))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving admin page"))}]}]
   ["genes/:id"
    {:name      :genes
     :view      genes/genes
     :link-text "genes"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :parameters {:path [:id]}
       :start (fn [params]
                (re-frame/dispatch
                 [:gene/request-gene (get-in params [:path :id])]))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving genes page"))}]}]
   ["gene_validity/:id"
    {:name      :gene-validity
     :view      gene-validity/gene-validity-assertion
     :link-text "gene validity"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :parameters {:path [:id]}
       :start (fn [params]
                (js/console.log "entering gene validity page")
                (re-frame/dispatch
                 [:gene-validity/request-assertion (get-in params [:path :id])]))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving gene validity page"))}]}]   ])

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::navigated new-match])))

(def router
  (rf/router
    routes
    {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start!
    router
    on-navigate
    {:use-fragment true}))

(defn router-component [{:keys [router]}]
  (let [current-route @(re-frame/subscribe [::current-route])]
    [:div
     (when current-route
       [(-> current-route :data :view)])]))



