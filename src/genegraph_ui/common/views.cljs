(ns genegraph-ui.common.views
  (:require [genegraph-ui.common.events :as common-events]
            [genegraph-ui.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [clojure.string :as s]
            [reitit.frontend.easy :as rfe]))

(defn signin-widget []
  (let [user @(subscribe [::common-subs/user])]
    (if user
      [:div.navbar-end
       [:div.navbar-item (:email user)]
       [:div.navbar-item [:figure.image.is-32x32 
                          [:img.is-rounded {:src (:avatar user)}]]]
       [:div.navbar-item
        [:div.buttons
         [:button.button
          {:on-click #(dispatch [:common/sign-out])}
          [:strong "log out"]]]]]
      [:div.navbar-end
       [:div.navbar-item
        [:div.buttons
         [:button.button
          {:on-click #(dispatch [:common/authenticate])}
          [:strong "log in"]]]]])))


;; (defn panel-tabs []
;;   (let [search-option @(subscribe [::common-subs/current-search-option])
;;         options [:GENE :DISEASE :AFFILIATION]]
;;     [:p.panel-tabs
;;      (for [option options]
;;        (if (= search-option option)
;;          ^{:key option}
;;          [:a.is-active
;;           {:on-click #(dispatch [:common/select-search-option option])}
;;           (s/lower-case (name option))]
;;          ^{:key option}
;;          [:a
;;           {:on-click #(dispatch [:common/select-search-option option])}
;;           (s/lower-case (name option))]))]))

(defn panel-search []
  (let [search-option @(subscribe [::common-subs/current-search-option])]
    [:div.panel-block
     [:form.control.has-icons-left
      {:on-submit
       #(do (dispatch [:common/search
                       (.-value (.getElementById js/document "search"))])
            (.preventDefault %)
            false)}
      [:input.input
       {:id "search"
        :type :search
        :placeholder (s/lower-case (name search-option))}]
      [:span.icon.is-left
       [:i.fas.fa-search {:aria-hidden :true}]]]]))

(defn panel-results []
  (let [result @(subscribe [::common-subs/search-result])]
    [:div 
     (for [i (:results result)]
       ^{:key i}
       [:a.panel-block
        {:href (rfe/href :resource {:curie (:curie i)})}
        (:label i)])]))

(defn side-panel []
  (let [result @(subscribe [::common-subs/current-value-object])]
    [:nav.panel
     (panel-search)
     (panel-results)]))
