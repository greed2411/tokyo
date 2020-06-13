# tokyo

[![greed2411](https://circleci.com/gh/greed2411/tokyo.svg?style=svg)](https://app.circleci.com/pipelines/github/greed2411/tokyo?branch=master)


> When you hit rock-bottom, you still have a way to go until the abyss.- Tokyo, Netflix's "Money Heist" (La Casa De Papel)

<p align="center">
  <br>
  <img src="https://res.cloudinary.com/teepublic/image/private/s--RcVSHez1--/t_Preview/b_rgb:36538b,c_limit,f_jpg,h_630,q_90,w_630/v1569759975/production/designs/6137078_0.jpg"/>
  <br>
  <em>image belongs to teepublic</em>
  <br>
  <br>
</p>



When one is limited by the technology of the time, One resorts to Java APIs using Clojure.

This is my first attempt on Clojure to have a REST API which when uploaded a file, identifies it's `mime-type`, `extension` and `text` if present inside the file and returns information as JSON.
This works for several type of files. Including the ones which require OCR, thanks to Tesseract. Complete [list](https://tika.apache.org/0.9/formats.html) of supported file formats by Tika.

Uses [ring](https://github.com/ring-clojure/ring) for Clojure HTTP server abstraction, [jetty](https://www.eclipse.org/jetty/) for actual HTTP server, [pantomime](https://github.com/michaelklishin/pantomime) for a clojure abstraction over [Apache Tika](https://tika.apache.org/) and also optionally served using [traefik](https://containo.us/traefik/) acting as reverse-proxy.


## Installation

Two options:
1. Download [openjdk-11](https://openjdk.java.net/) and install [lein](https://leiningen.org/). Followed by `lein uberjar`
2. Use the `Dockerfile` (Recommended)

## Building

1. You can obtain the `.jar` file from releases (if it's available).
2. Else build the docker image using `Dockerfile`.

```
docker build ./ -t tokyo
docker run tokyo:latest
```

Note: the server defaults to running on port 80, because it has been exposed in the docker image.
You can change the port number by setting an enviornment variable `TOKYO_PORT` inside the `Dockerfile`, or in your shell prompt to whichever port number you'd like when running the `.jar` file.

I've also added a `docker-compose.yml` which uses [traefik](https://containo.us/traefik/) as reverse proxy. use `docker-compose up`.

## Usage

1. the `/file` route. make a `POST` request by uploading a file.
    * the command line approach using [curl](https://curl.haxx.se/)


    ```bash
    curl -XPOST  "http://localhost:80/file" -F file=@/path/to/file/sample.doc

    {"mime-type":"application/msword","ext":".bin","text":"Lorem ipsum \nLorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ac faucibus odio."}
    ```

    * The Python Way using [requests](https://requests.readthedocs.io/en/master/)

    ```python
    >>> import requests
    >>> import json

    >>> url = "http://localhost:80/file"
    >>> files = {"file": open("/path/to/file/sample.doc")}
    >>> response = requests.post(url, files=files)
    >>> json.loads(response.content)

    {'mime-type': 'application/msword', 'ext': '.bin', 'text': 'Lorem ipsum \nLorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ac faucibus odio.'}
    ```

    the general API response,json-schema is of the form:
    ```
    :mime-type (string) - the mime-type of the file. eg: application/msword, text/plain etc.
    :ext       (string) - the extension of the file. eg: .txt, .jpg etc.
    :text      (string) - the text content of the file.
    ```

Note: The files being uploaded are stored as temp files, in `/tmp` and removed after an hour later. (assuming the jvm is still running for that hour or so).

2. just a `/`, `GET` request returns `Hello World` as plain text. to act as ping.

If going down the path of using `docker-compose`. The request gets altered to

```bash
curl -XPOST  -H Host:tokyo.localhost http://localhost/file -F file=@/path/to/file/sample.doc

{"mime-type":"application/msword","ext":".bin","text":"Lorem ipsum \nLorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ac faucibus odio."}
```

and

```python
>>> response = requests.post(url, files=files, headers={"Host": "tokyo.localhost"})
```

where `tokyo.localhost` has been mentioned in `docker-compose.yml`

### Why?

I had to do this because neither Python's [filetype](https://github.com/h2non/filetype.py) (doesn't identify .doc, .docx, plain text), [textract](https://github.com/deanmalmgren/textract) (hacky way of extracting text, and one needs to know the extension before extracting) are as good as Tika. The Go version, [filetype](https://github.com/h2non/filetype) didn't support a way to extract text. So I resorted to spiraling down the path of using Java's [Apache Tika](https://tika.apache.org/) using the Clojure [pantomime](https://github.com/michaelklishin/pantomime) library.


## License

Copyright © 2020 greed2411/tokyo

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
