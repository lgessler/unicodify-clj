# unicodify-clj

This is a program specifically designed to adapt `wget -r` dumps of old websites with the legacy Devanagari encoding XDVNG so that they use unicode instead. It has only been tested on the contents of [Malhar](http://www.hindi-urdu-malhar.org).

## Usage

**First**, modify the variables in unicodify-clj.core to suit your machine. `INPUT_PATH` should point to the root dir of where your `wget -r http://www.hindi-urdu-malhar.org` output was put.

Then, `lein run`

I haven't tested this on Windows. It'll probably break.

## License

Copyright © 2017 Luke Gessler

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
