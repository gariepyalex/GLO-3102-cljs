(ns moviesclj.utils
  (:require [ajax.core :as ajax]))

(defn input-value
  [input]
  (-> input .-target .-value))

(defn json-str->keyword
  [ajax-request]
  (assoc ajax-request :response-format (ajax/json-response-format {:keywords? true})))

