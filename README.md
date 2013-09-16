# hypermedia-content-types

A hypermedia web application. It aims to expose resources via a number of
content types. My original idea was to use custom written ring middleware that
would take response data in a Siren like format and serialise it to json, edn
or xhtml depending on the client's desire.

## Possible libraries

I have looked into using a REST library to assist. [bishop] [bishop] appeared
to be complecting routing with response types and expected the application
layer to do the rendering.  [Clothesline] [clothesline] also appear to be doing
routing. [plugboard] [plugboard] is no longer maintained, the author is now
consentrating on liberator.

[Liberator] [liberator] does not take responsability for routing. It also seems
popular and well documented. The [babel example] [babel] demonstrates default
action for `application/json`. If that could be extended to work with
`text/html` and `application/edn` as well then that would pretty much take care
of my requirements.

### Liberator

Liberator takes the "Don't call me. I'll call you." approach. The idea that I'd
have a bug and have to reach for that huge decision graph to figure out why my
code isn't being called is pretty intimidating.

When used with compojure the method part of the dispatch seems irrelevant.
You'll find yourself using ANY all the time. Furthermore the request
destructuring between the two isn't seamless.

`resource` does not take a map but instead takes vararsg which it then treats
as a map. It returns a function that can act as a ring handler. This somewhat
impedes composability. This is somewhat helped by using run-resource. _How
would I introducer some new default behavior?_.

## Usage

    lein repl
    (start)

Then open your browser to <http://localhost:8080/>.

## License

Copyright © 2013 Logan Campbell

Distributed under the Eclipse Public License, the same as Clojure.

[babel]: http://clojure-liberator.github.io/liberator/tutorial/conneg.html
[clothesline]: https://github.com/banjiewen/Clothesline
[plugboard]: https://github.com/malcolmsparks/plugboard
[library]: http://clojure-liberator.github.io/liberator/
[bishop]: https://github.com/cmiles74/bishop
