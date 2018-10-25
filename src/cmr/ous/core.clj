(ns cmr.ous.core
  (:require
    [clojusc.twig :as logger]
    [cmr.ous.impl.v1 :as v1]
    [cmr.ous.impl.v2-1 :as v2-1]
    [cmr.ous.components.core :as components]
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as log]
    [trifl.java :as trifl])
  (:gen-class))

(defn get-opendap-urls
  [component api-version user-token raw-params]
  (log/trace "Got API version:" api-version)
  (case (keyword api-version)
    :v1 (v1/get-opendap-urls component user-token raw-params)
    :v2.1 (v2-1/get-opendap-urls component user-token raw-params)
    ;; XXX I believe EDSC is using 2.1 by default now, so we can change the
    ;;     default option to be that ...
    (v1/get-opendap-urls component user-token raw-params)))

(defn -main
  [& args]
  (logger/set-level! '[cmr] :info logger/no-color-log-formatter)
  (let [system (components/init)]
    (component/start system)
    (trifl/add-shutdown-handler #(component/stop system))))
