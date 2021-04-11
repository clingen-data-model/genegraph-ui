(ns curator.pages.home.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.common.views :as common-views]
            [curator.pages.home.subs :as subs]
            [curator.common.subs :as common-subs]
            [curator.pages.home.events]
            [reitit.frontend.easy :as rfe]))


(defn search []
  (let [topic @(subscribe [::subs/search-topic])
        suggested-genes @(subscribe [::subs/suggested-genes])
        search-results @(subscribe [::subs/search-results])]
    [:section.section.home-search-main
     [:div.columns
      [:div.column
       [:div.columns
        ; two fifths, left fifth empty, right fifth dropdown menu
        [:div.column.is-one-fifth.is-offset-one-fifth
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
                                                 ])}]]]]
        ; one fifth submit button
        [:div.column.is-one-fifth
         [:button.button {:type "submit"
                          :on-click #(dispatch [:home/request-search
                                                topic
                                                (-> (.getElementById js/document "search-box") .-value)]
                                               )}
          "Search"]]
        ;;;
        ]
       [:p @(subscribe [::subs/errors])]
       [:div.container.is-widescreen
        (case topic
          :gene (when (seq suggested-genes)
                  [:div.box
                   (for [gene suggested-genes]
                     ^{:key gene} [:div.block
                                   [:a {:href (rfe/href :genes gene)}
                                    (:text gene)]])])
          :variation [:div.block.table-container            ;.is-widescreen
                      {:style {;:width :100%
                               }}
                      (.log js/console "Search results:")
                      (.log js/console (clj->js search-results))
                      (for [aggregate-assertion (:aggregate_assertions search-results)]
                        (let [id (:id aggregate-assertion)]
                          ^{:key (str "div" id)}
                          [:div
                           [:table [:tbody
                                    [:tr
                                     [:td "Assertion Subject: "]
                                     [:td (get-in aggregate-assertion [:subject :name])]]
                                    [:tr
                                     [:td "Aggregate Assertion ID: "]
                                     [:td (:id aggregate-assertion)]]
                                    [:tr
                                     [:td "Assertion Predicate: "]
                                     [:td (:predicate aggregate-assertion)]]
                                    [:tr
                                     [:td "Review Status: "]
                                     [:td (:review_status aggregate-assertion)]]
                                    ]]
                           (let [fields [[:id :id]
                                         [:subject #(get-in % [:subject :name])]
                                         [:predicate :predicate]
                                         ;:object
                                         [:version :version]
                                         [:review_status :review_status]
                                         [:date_updated :date_updated]
                                         [:release_date :release_date]
                                         [:collection_methods :collection_methods]
                                         [:allele_origins :allele_origins]]]
                             ^{:key (str "table" id)}
                             [:table.table
                              {:style {:display :block
                                       :height :600px
                                       :overflow-y :scroll
                                       ;:border-collapse :collapse
                                       }}
                              [:thead
                               [:tr
                                (for [field fields]
                                  ^{:key field} [:th
                                                 {:style {:background-color :white
                                                          :border-width :1px
                                                          :border-color :white
                                                          :position :sticky
                                                          :top :0px}}
                                                 (first field)])]]
                              [:tbody
                               (for [single-assertion (:members aggregate-assertion)]
                                 ^{:key single-assertion} [:tr
                                                           (for [field fields]
                                                             ^{:key (str single-assertion field)}
                                                             [:td (clj->js
                                                                    ((second field) single-assertion)
                                                                    )])])
                               ]
                              ])])

                        )

                      ]
          [:div.notification "Unknown search topic"]
          )]
       ]]]))

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
     [:section.section (common-views/navbar)]
     ;(case authorization
     ;  :authorized (search)
     ;  ;:unauthorized (unauthorized-user)
     ;  :unauthorized (search)
     ;  (login-invitation))
     (search)]))
