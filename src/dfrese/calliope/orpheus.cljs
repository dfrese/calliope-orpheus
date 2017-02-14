(ns dfrese.calliope.orpheus
  (:require [dfrese.calliope.app :as app]
            [dfrese.orpheus.core :as orpheus]
            [dfrese.orpheus.lift :as lift]
            [dfrese.orpheus.patch :as patch]))

(defn- ->props [html]
  ;; conveniently be a little relaxed about the return value of 'view'
  (cond
    (vector? html) {"childNodes" html}
    (orpheus/velement? html) {"childNodes" [html]}
    :else html ;; (map? html)
    ))

#_(defn ^:no-doc clear-element! [element]
  (doseq [c (vec (array-seq (.-childNodes element)))]
    (.removeChild element c)))

(def ^{:doc "Implementation of [[calliope.core/ICanvas]], to be passed
  to [[calliope.core/app]], enabling the view of the application to
  use the orpheus virtual dom library."}
  canvas
  (reify app/ICanvas
    (normalize-view [this v] (->props v))
    (init-canvas! [this element]
      ;; try to lift the current dom structure as vdom properties (gives
      ;; smooth first update if the html already matches the initial vdom)
      (lift/lift-properties element))
    (update-canvas! [this state element v msg-callback]
      (patch/patch-properties! element state v
                               {:dispatch! msg-callback}))
    (finish-canvas! [this state element]
      (patch/patch-properties! element state {}
                               {}))))

;; (defn element [^js/Element element]
;;   element)

;; (defn element-by-id [^js/DOMDocument document id]
;;   (.getElementById document id))

;; (defn page [^js/DOMDocument document]
;;   (.-body document))

(defn app "Defines a [[calliope/app]], whose view can use the orpheus
  virtual dom library."
  [init view update subscriptions]
  (app/app canvas init view update subscriptions))
