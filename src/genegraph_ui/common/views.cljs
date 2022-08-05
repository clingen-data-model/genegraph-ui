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

(defn navbar []
  (let [navbar-burger-active (when @(subscribe [::common-subs/navbar-burger-active])
                               "is-active")
        search-option @(subscribe [::common-subs/current-search-option])]
    [:nav.navbar
     {:role "navigation" :aria-label "main navigation"}
     [:div.navbar-brand
      [:a.navbar-item.has-text-link.logo.is-size-4
       {:href "/"}
       [:img {:src "images/clingen-logo-vp.svg"
              :width "30%"
              :height "auto"}]
       
       "genegraph"]
      [:div.navbar-item.field
       [:form.control.has-icons-right
        {:on-submit
         #(do (dispatch [:find/search
                         (.-value (.getElementById js/document "find-text-navbar"))])
              (rfe/push-state :find)
              (.preventDefault %)
              false)}
        [:input.input.is-rounded
         {:id "find-text-navbar"
          :name "find-text"
          :placeholder "search"}]
        [:span.icon.is-right [:ion-icon {:name "search-outline"}]]]]
      [:a.navbar-burger {:role "button"
                         :aria-label "menu"
                         :aria-expanded "false"
                         :class navbar-burger-active
                         :on-click #(dispatch [:common/toggle-navbar-burger])}
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]]]
     [:div.navbar-menu
      {:class navbar-burger-active}
      [:div.navbar-start
]
      [:div.navbar-end
       [:a.navbar-item
        {:href (rfe/href :documentation)}
        "documentation"]]]]))

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
     (panel-results)
     [:div.panel-block
      {:on-click #(dispatch [:common/toggle-menu])}
      "hide menu"
      [:span.icon.is-pulled-right
       [:i.fas.fa-angle-double-left]]]]))
