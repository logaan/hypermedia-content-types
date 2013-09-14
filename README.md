# hypermedia-content-types

An hypermedia web application. It aims to expose resources via a number of
content types. My original idea was to use custom written ring middleware that
would take response data in a Siren like format and serialise it to json, edn
or xhtml depending on the client's desire.

I have looked into using a REST library to assist. [bishop]
(https://github.com/cmiles74/bishop) appeared to be complecting routing with
response types and expected the application layer to do the rendering.
[Clothesline] (https://github.com/banjiewen/Clothesline) also appear to be
doing routing.  [plugboard] (https://github.com/malcolmsparks/plugboard) is no
longer maintained, the author is now consentrating on liberator. [Liberator]
(http://clojure-liberator.github.io/liberator/) looks good.

## Usage

FIXME

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
