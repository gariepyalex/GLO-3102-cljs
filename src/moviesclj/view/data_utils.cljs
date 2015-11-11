(ns moviesclj.view.data-utils
  (:require [moviesclj.utils :as utils]
            [moviesclj.authentication.authentication :as auth]
            [ajax.core :as ajax]))

(defn update-single-data!
  [data-atom path]
  (ajax/GET path
            (-> {:handler #(reset! data-atom (first (:results %)))}
                auth/with-auth
                utils/json-str->keyword)))
