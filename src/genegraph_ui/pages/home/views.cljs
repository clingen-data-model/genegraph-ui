(ns genegraph-ui.pages.home.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.home.subs :as subs]
            [genegraph-ui.common.subs :as common-subs]
            [genegraph-ui.pages.home.events]
            [reitit.frontend.easy :as rfe]))


(defn search []
  (let [topic @(subscribe [::subs/search-topic])
        suggested-genes @(subscribe [::subs/suggested-genes])
        search-results @(subscribe [::subs/search-results])]
    [:div.columns
     [:div.column
      [:div.columns
                                        ; two fifths, left fifth empty, right fifth dropdown menu
       [:div.column.is-narrow.is-offset-one-fifth
        [:div.dropdown.is-hoverable
         [:div.dropdown-trigger
          [:button.button {:aria-haspopup "true" :aria-controls "dropdown-menu"}
           [:span topic]
           [:span.icon.is-small
            [:i.fas.fa-angle-down {:aria-hidden "true"}]]]]
         [:div.dropdown-menu {:id "dropdown-menu" :role "menu"}
          [:div.dropdown-content.has-text-left
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :gene])}
            "gene"]
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :disease])}
            "disease"]
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :affiliation])}
            "affiliation"]
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :variation])}
            "variation"]
           ]]]]
                                        ; two fifths for search bar
       [:div.column.is-two-fifths
        [:div.field
         [:div.control
          [:input.input {:id "search-box"
                         :type "text"
                         :placeholder topic
                         :on-change #(dispatch [:home/request-suggestions
                                        ;(fn [event] (println event) (-> event .-target .-value))
                                                (-> % .-target .-value) ; passed the Javascript event object
                                                ])}]]]
        (case topic
          :gene (when (seq suggested-genes)
                  [:div.box
                   (for [gene suggested-genes]
                     ^{:key gene} [:div.block
                                   [:a {:href (rfe/href :gene gene)}
                                    (:text gene)]])]))]
       [:div.column.is-one-fifth
        [:button.button {:type "submit"
                         :on-click #(dispatch [:home/request-search
                                               topic
                                               (-> (.getElementById js/document "search-box") .-value)])}
         "Search"]]]
      [:p @(subscribe [::subs/errors])]]]))

(defn unauthorized-user []
  [:section.hero
   [:div.hero-body
    [:div.container
     [:h1.title "You are not authorized to access this resource"]
     [:h2.subtitle "Please contact an administrator to gain access if you believe this message is in error."]]]])

(defn login-invitation []
  [:section.hero
   [:div.hero-body
    [:div.container
     [:h1.title "ClinGen admin interface"]
     [:h2.subtitle "Please login to continue"]]]])

(defn home []
  (let [authorization @(subscribe [::common-subs/user-authorization])]
    [:div
     [:section.section (common-views/navbar)
      (search)]]))
