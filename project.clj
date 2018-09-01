(defproject algopop/modelme "0.1.0-SNAPSHOT"

  :description "Model me crafts weight projections"

  :url "http://github.com/timothypratley/modelme"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]
            "dev" ["trampoline" "run" "-m" "figwheel.main" "--" "-b" "dev" "-r"]}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojure/core.async  "0.4.474"]
                 [reagent "0.8.1"]
                 [soda-ash "0.82.2"]
                 [cljsjs/vega "3.3.1-0"]
                 [cljsjs/vega-lite "2.6.0-0"]
                 [cljsjs/vega-embed "3.16.1-0"]
                 [cljsjs/vega-tooltip "0.12.0-0"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :cljsbuild {:builds
              {"min"
               {:source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/modelme.js"
                           :main modelme.app
                           :optimizations :advanced
                           :infer-externs true
                           :pretty-print false}}

               ;; TODO: I would like to remove this build in favour of multiple mains in dev.cljs.edn
               "test"
               {:source-paths ["test" "src"]
                :compiler {:main modelme.test-runner
                           :optimizations :advanced
                           :output-to "resources/public/js/compiled/test.js"
                           :output-dir "resources/public/js/compiled/out/test"
                           :asset-path "js/compiled/out/test"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}}}

  :clean-targets ^{:protect false} ["resources/public/js/compiled" :target-path]

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.1.8"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]
                                  [binaryage/devtools "0.9.10"]
                                  [devcards "0.2.5"]
                                  [lein-doo "0.1.10"]]

                   :plugins [[lein-doo "0.1.10"]]

                   :doo {:build "test"}

                   :figwheel {:css-dirs ["resources/public/css"]}

                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}})
