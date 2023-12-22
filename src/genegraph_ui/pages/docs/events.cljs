(ns genegraph-ui.pages.docs.events
  (:require [re-frame.core :as re-frame])
  (:import [goog.net XhrIo]))

(re-frame/reg-fx
 ::get-document
 (fn [[doc event-handler]]
   (js/console.log "Getting document " event-handler)
   (XhrIo/send (str "/markdown/" doc ".md") 
               (fn [^js response]
                 (re-frame/dispatch [event-handler
                                     (.getResponseText (.-target response))])))))



(re-frame/reg-event-db
 ::recieve-markdown
 (fn [db [_ result]]
   (js/console.log "successful request")
   (assoc db :docs/page result)))

(re-frame/reg-event-db
 ::failed-request
 (fn [db [_ result]]
   (js/console.log "failed request")
   (assoc db :docs/page result)))

(re-frame/reg-event-fx
 ::fetch-markdown
 (fn [_ [_ val]]
   (js/console.log "fetch markdown " val )
   {:fx [[::get-document [val ::recieve-markdown]]]}))
