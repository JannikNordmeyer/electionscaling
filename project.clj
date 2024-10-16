(defproject electionscaling "0.1.0"
  :min-lein-version "2.0.0"
  :description "Script for Downloading and Scaling Election Results from:"
  :url "https://github.com/gockelhahn/qual-o-mat-data"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure   "1.11.1"]
                 [org.clojure/tools.cli "1.0.219"]
                 [conexp-clj            "2.6.0"]]
  :main electionscaling.core
  :source-paths ["scr"]
  :target-path "builds/%s"
  :aot [electionscaling.core]
  :profiles {:uberjar {:aot [electionscaling.core]
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
	     :jar {:aot [electionscaling.core]
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]})
