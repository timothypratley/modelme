# Model Me

Chart how your schedule affects your body
http://timothypratley.github.io/modelme


## Overview

Model me is all about you!
What's the impact of drinking a smoothie?
Model me presents a holistic view of how your body is expected to respond.
Record actual responses and it will tune in even closer to you.


## Development

### Setup

To get an interactive development environment run:

    lein dev

and open your browser at [localhost:9500](http://localhost:9500/dev.html).
This will auto compile and send all changes to the browser without the need to reload.

Devcards are built that you can view at

    http://localhost:9500/devcards.html


To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`.


### Tests

Logic tests can be run as Clojure

    lein test

or

    lein test-refresh

or in ClojureScript

    lein doo nashorn


### Deploying

To push to Github Pages:

    ./deploy.sh


## License

Copyright © 2018 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
