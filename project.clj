(defproject algopop/modelme "0.1.0-SNAPSHOT"

  :description "Model me crafts weight projections"

  :url "http://github.com/timothypratley/modelme"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

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
                           :pretty-print false}}}}

  :clean-targets ^{:protect false} ["resources/public/js/compiled" :target-path]

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.16"]
                                  [cider/piggieback "0.3.8"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [devcards "0.2.5"]
                                  [lein-doo "0.1.10"]]

                   :plugins [[lein-figwheel "0.5.16"]
                             [lein-doo "0.1.10"]]

                   :doo {:build "test"}

                   :figwheel {:css-dirs ["resources/public/css"]}

                   :cljsbuild
                   {:builds
                    {"dev"
                     {:source-paths ["dev" "src"]
                      :figwheel {}
                      :compiler {:main modelme.app
                                 :optimizations :none
                                 :output-to "resources/public/js/compiled/dev.js"
                                 :output-dir "resources/public/js/compiled/out/dev"
                                 :asset-path "js/compiled/out/dev"
                                 :source-map-timestamp true
                                 :preloads [devtools.preload]}}

                     "devcards"
                     {:source-paths ["dev" "src" "test"]
                      :figwheel {:devcards true}
                      :compiler {:main modelme.devcards
                                 :optimizations :none
                                 :output-to "resources/public/js/compiled/devcards.js"
                                 :output-dir "resources/public/js/compiled/out/devcards"
                                 :asset-path "js/compiled/out/devcards"
                                 :source-map-timestamp true
                                 :preloads [devtools.preload]}}

                     "test"
                     {:source-paths ["src" "test"]
                      :compiler {:main modelme.test-runner
                                 :output-to "resources/public/js/compiled/tests.js"
                                 :output-dir "target/out/test"
                                 :optimizations :whitespace}}}}

                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}})
