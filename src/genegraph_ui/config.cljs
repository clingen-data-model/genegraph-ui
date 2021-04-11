(ns genegraph-ui.config)

(def firebase-config
  {:clingen-dev {:apiKey "AIzaSyDj-cOdeUUNoaiA1DiUu9uLfRkrZ32TWJo",
                 :authDomain "clingen-dev.firebaseapp.com",
                 :projectId "clingen-dev",
                 :storageBucket "clingen-dev.appspot.com",
                 :messagingSenderId "522856288592",
                 :appId "1:522856288592:web:d36446a53f898c80579c55"}
   :clingen-stage {:apiKey "AIzaSyCbBq1o5ZiVB5UflVNN-zCULAIRGgkHtrk"
                   :authDomain "clingen-stage.firebaseapp.com"
                   :projectId "clingen-stage"
                   :storageBucket "clingen-stage.appspot.com"
                   :messagingSenderId "583560269534"
                   :appId "1:583560269534:web:b41fb5f1d09a6ef6fcbc4f"}})
