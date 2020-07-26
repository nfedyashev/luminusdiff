(ns luminusdiff.core
  (:gen-class)
  (:require
   [clojure.java.shell]))

(defn start-app [args]
  ;;(clojure.java.shell/sh "bash" "-c" "date > generated.txt")
  ;;(clojure.java.shell/sh "git" "add" "generated.txt")
  ;;(clojure.java.shell/sh "git" "commit" "-m" "from clj")
  ;;(clojure.java.shell/sh "git" "push")

  (System/exit 0))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (start-app args))
