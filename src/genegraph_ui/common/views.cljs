(ns genegraph-ui.common.views
  (:require [genegraph-ui.common.events :as common-events]
            [genegraph-ui.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
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

(defn brand []
  [:div.navbar-brand
   [:a.navbar-item {:href (rfe/href :home)}
    [:img {:src "/images/logo.svg"}]]
   [:a.navbar-item {:href (rfe/href :home)}
    [:h5.title.is-6 "Genegraph"]]])

(defn menu []
  [:div.navbar-menu
   [:div.navbar-start
    [:a.navbar-item {:href "#"}
     ]]])

(defn navbar []
  [:nav.navbar.is-fixed-top {:role "navigation"}
   (brand)])

(defn panel-tabs []
  (let [search-option @(subscribe [::common-subs/current-search-option])
        options [:gene :disease :affiliation]]
    [:p.panel-tabs
     (for [option options]
       (if (= search-option option)
         ^{:key option}
         [:a.is-active
          {:on-click #(dispatch [:common/select-search-option option])}
          option]
         ^{:key option}
         [:a
          {:on-click #(dispatch [:common/select-search-option option])}
          option]))]))

(defn panel-search []
  (let [search-option @(subscribe [::common-subs/current-search-option])]
    [:div.panel-block
     [:form.control.has-icons-left
      {:on-submit
       #(do (dispatch [:common/search
                       (.-value (.getElementById js/document "search"))])
            false)}
      [:input.input
       {:id "search"
        :type :search
        :placeholder search-option}]
      [:span.icon.is-left
       [:i.fas.fa-search {:aria-hidden :true}]]]]))

(defn gene-results []
  (let [genes @(subscribe [::common-subs/gene-list])]
    [:div
     (for [gene genes]
       ^{:key gene}
       [:div
        [:a.panel-block
         {:href (rfe/href :gene gene)}
         (:label gene)]
        (for [assertion (:gene_validity_assertions gene)]
          ^{:key assertion} [:div.panel-block
                             [:a.icon
                              {:href (rfe/href :gene-validity assertion)}
                              [:i.fas.fa-file]]
                             [:a {:href (rfe/href :disease (:disease assertion))}
                              (get-in assertion [:disease :label])]])])]))
(defn disease-results []
  [:div])

(defn affiliation-results []
  [:div
   (for [affiliation @(subscribe [::common-subs/affiliation-list])]
     ^{:key affiliation}
     [:div
      [:a.panel-block
       (:label affiliation)]])])

(defn panel-results []
  (case @(subscribe [::common-subs/current-search-option])
    :gene (gene-results)
    :disease (disease-results)
    :affiliation (affiliation-results)))

(defn side-panel []
  [:nav.panel.box
   [:p.panel-heading
    "Genegraph"]
   (panel-tabs)
   (panel-search)
   (panel-results)])
