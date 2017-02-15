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

(defrecord ^:no-doc OrpheusCanvas
  [view]
  app/ICanvas
  (init-canvas! [this element]
    ;; try to lift the current dom structure as vdom properties (gives
    ;; smooth first update if the html already matches the initial vdom)
    (lift/lift-properties element))
  (update-canvas! [this state element model msg-callback]
    (patch/patch-properties! element state (->props (view model))
                             {:dispatch! msg-callback}))
  (finish-canvas! [this state element]
    (patch/patch-properties! element state {}
                             {})))

(defn ^{:doc "Returns an implementation of [[calliope.core/ICanvas]],
  to be passed to [[calliope.core/app]], enabling the view of the
  application to use the orpheus virtual dom library. The `view`
  function passed must take the application model, and return new
  properties for the master dom element, or one or more virtual dom
  elements to be used as the children of that."}  canvas [view]
  (OrpheusCanvas. view))

;; (defn element [^js/Element element]
;;   element)

;; (defn element-by-id [^js/DOMDocument document id]
;;   (.getElementById document id))

;; (defn page [^js/DOMDocument document]
;;   (.-body document))

(defn app "Defines a [[calliope/app]], whose view can use the orpheus
  virtual dom library. The `view` function passed must take the
  application model, and return new properties for the master dom
  element, or one or more virtual dom elements to be used as the
  children of that."
  [init view update subscriptions]
  (app/app (canvas view) init update subscriptions))
