(ns genegraph-ui.routes
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reagent.core :as reagent]
            [reitit.core :as r]
            [reitit.coercion.spec :as rss]
            [reitit.coercion.schema :as rsc]
            [schema.core :as s]
            [reitit.frontend :as rf]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [genegraph-ui.views.default]
            [genegraph-ui.pages.find.views :as find]
            [genegraph-ui.pages.find.events :as find-events]
            [genegraph-ui.pages.resource.views :as resource]
            [genegraph-ui.pages.resource.events :as resource-events]
            [genegraph-ui.pages.home.views :as home]
            [genegraph-ui.pages.gene.views :as gene]
            [genegraph-ui.pages.documentation.views :as documentation]
            [genegraph-ui.pages.disease.views :as disease]
            [genegraph-ui.pages.gene-validity.views :as gene-validity]
            [genegraph-ui.pages.gene.events :as gene-events]
            [genegraph-ui.pages.gene-validity.events :as gene-validity-events]))

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
   ["find"
    {:name :find
     :view find/find-page
     :link-text "find"
     :controllers
     [{:parameters {:query [:find-text]}
       :start (fn [params]
                (js/console.log (str params)))
       :stop  (fn [& params]
                (js/console.log "leaving find page"))}]}]
   ["documentation"
    {:name :documentation
     :view documentation/home
     :link-text "documentation"}]
   ["documentation/sepio-overview"
    {:name :sepio-overview
     :view documentation/sepio-overview
     :link-text "SEPIO overview"}]
   ["documentation/graphql-mapping"
    {:name :graphql-mapping
     :view documentation/graphql-mapping
     :link-text "GraphQL mapping"}]
   ["documentation/examples"
    {:name :examples
     :view documentation/examples
     :link-text "examples"}]
   ["documentation/gene-validity"
    {:name :gene-validity
     :view documentation/gene-validity
     :link-text "gene validity"}]
   ["resource/:curie"
    {:name :resource
     :view resource/resource
     :link-text "resource"
     :controllers
     [{:parameters {:path [:curie]}
       :start (fn [params]
                (re-frame/dispatch
                 [:resource/select-resource
                  (get-in params [:path :curie])]))
       :stop  (fn [& params] (js/console.log "Leaving resource page"))}]}]
;;    ["gene/:curie"
;;     {:name      :gene
;;      :view      gene/gene
;;      :link-text "genes"
;;      :controllers
;;      [{;; Do whatever initialization needed for home page
;;        ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
;;        :parameters {:path [:curie]}
;;        :start (fn [params]
;;                 (re-frame/dispatch
;;                  [:gene/request-gene (get-in params [:path :curie])]))
;;        ;; Teardown can be done here.
;;        :stop  (fn [& params] (js/console.log "Leaving genes page"))}]}]
;;    ["disease/:curie"
;;     {:name      :disease
;;      :view      disease/disease
;;      :link-text "disease"
;;      :controllers
;;      [{;; Do whatever initialization needed for home page
;;        ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
;;        :parameters {:path [:curie]}
;;        :start (fn [params]
;;                 (re-frame/dispatch
;;                  [:disease/request-disease (get-in params [:path :curie])]))
;;        ;; Teardown can be done here.
;;        :stop  (fn [& params] (js/console.log "Leaving disease page"))}]}]
;;    ["gene_validity"
;;     {:name :gene-validity-list
;;      :view gene-validity/gene-validity-assertions
;;      :link-text "gene validity"
;;      :controllers
;;      [{ :start (fn [params]
;;                  (js/console.log "entering gene validity list page")
;;                  (re-frame/dispatch
;;                   [:gene-validity/request-assertion-list]))
;;        :stop  (fn [& params] (js/console.log "Leaving gene validity list page"))}]}]
;;    ["gene_validity/:curie"
;;     {:name      :gene-validity
;;      :view      gene-validity/gene-validity-assertion
;;      :link-text "gene validity"
;;      :controllers
;;      [{:parameters {:path [:curie]}
;;        :start (fn [params]
;;                 (js/console.log "entering gene validity page")
;;                 (re-frame/dispatch
;;                  [:gene-validity/request-assertion (get-in params [:path :curie])]))
;;        :stop  (fn [& params] (js/console.log "Leaving gene validity page"))}]}]
   ])

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



