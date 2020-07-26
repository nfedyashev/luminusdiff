
(ns luminusdiff.user
  (:refer-clojure :exclude [==])
  (:require
   [luminusdiff.config :as config]
   [clj-http.client :as client]
   [clojure.string]
   [clojure.java.shell]
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(defn retrieve-lein-template-versions []
  (let [response (client/get "https://clojars.org/api/artifacts/luminus/lein-template" {:accept :edn})
        body (edn/read-string (:body response))
        ;;latest-version (:latest_version body) ;; "3.82"
        ;;latest-release (:latest_release body) ;; "3.82"
        ]

    (->> (:recent_versions body)
         ;; drops download stats
         (map #(:version %)))))

(defn sh-exec [& rest]
  (let [result (apply clojure.java.shell/sh rest)]
    (println result)
    (assert (= 0 (:exit result)) result)))

(defn lein-generate-from-template [version option]
  "Generate luminus from template and save it in the current dir for clean diffs"
  (if option
    (sh-exec "lein"
             "new"
             "luminus"
             "luminusdiff"
             "--template-version"
             version
             "--to-dir"
             "."
             (str "+" option))

    (sh-exec "lein"
             "new"
             "luminus"
             "luminusdiff"
             "--template-version"
             version
             "--to-dir"
             ".")))

(defn generate-template-and-git-push [version option]
  "Generate a new luminus template based on completely clean repo/state.
  The goal here is to have the cleanest before/after state possible for proper diffs"
  (let [branch-name (str version (when option (str "+" option)))]
    (sh-exec "git" "checkout" "blank")
    ;; TODO ensure it is blank indeed?
    (sh-exec "git" "checkout" "-b" branch-name)

    (lein-generate-from-template version option)

    ;; Git Add everything as is to avoid touching this part in the future
    (sh-exec "git" "add" ".")

    (sh-exec "git" "commit" "-m" (clojure.string/trim (str "lein new luminus luminusdiff --template-version " version " " option)))

    (sh-exec "git" "push" "origin" branch-name)))

(defn retrieve-branches []
  "Retrieve remote git branches from github"
  (let [res (clojure.java.shell/sh "bash" "-c" "git ls-remote --heads | awk '{print $2}' | grep 'refs/heads/' | sed 's:refs/heads/::'")]
    (assert (= 0 (:exit res)) res)

    (clojure.string/split-lines (-> res :out))))

(defn all-combinations [versions]
  "Return all combinations of template install options for the given versions
  including one without an explicit +option"
  (let [version-options (cons nil (-> config/opts vals flatten))]
    (flatten
     (map (fn [ver] (map (fn [o] (if o (str ver "+" o) ver)) version-options))
          versions))))

(defn foo []
  (let [;; versions recent-versions
        ;; versions '("3.82" ;; "3.81" "3.80"
        ;;            )
        versions (retrieve-lein-template-versions)
        _all-combinations (all-combinations versions)
        missing-version-options (remove
                              (set (retrieve-branches))
                              _all-combinations)
        ;;FIXME exclude develop/master/clean for proper counting

        __ (println "missing count: " (count missing-version-options) "/" (count _all-combinations))]
    (doseq [version-option missing-version-options]
      (println "Saving " version-option)

      (let [[version option] (clojure.string/split version-option #"\+")]
        ;; NOTE: option could be blank
        ;; (println version option)
        (generate-template-and-git-push version option)))))
