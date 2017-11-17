ZeKKe REST API v0.1
===================

[![Coverage](https://codecov.io/gh/dan-zx/zekke-api/branch/develop/graph/badge.svg)](https://codecov.io/gh/dan-zx/zekke-api)
[![Build Status](https://travis-ci.org/dan-zx/zekke-api.svg?branch=develop)](https://travis-ci.org/dan-zx/zekke-api)
[![License](https://img.shields.io/badge/licence-Apache_Licence_2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

ZeKKe provides geographic web services for directions and POIs (Points of interest) in private spaces. where no roads exist in systems like Google Maps or Bing Maps. Also, provides additional information about the POIs like events, adds, etc.

Getting started
---------------

1. Install MongoDB and initialize it.

    a. Create the collections sequences, users and waypoints

    b. Create the stored procedure functions located at [fuctions directory](mongo/functions) in your MongoDB with the command.
    ```javascript
    db.system.js.save( { _id: "<functionName>", value: <function> } );
    ```

    c. Create [indexes](mongo/indexes.js) for the ZeKKe collections.

2. Set your Mongo DB connection settings depending on the configuration you will use `spring.profiles.active` in [application.properties](src/main/resources/application.properties):

    * For **dev** profile: Set in [application.properties](src/main/resources/application.properties) file the connection values for your Mongo DB instance:
        + `dev.mongodb.host`
        + `dev.mongodb.port`
        + `dev.mongodb.db`
        + `dev.mongodb.db.user`
        + `dev.mongodb.db.password`

    * For **staging** profile: Set the `MONGODB_URI` system property with the connection URI of your Mongo DB instance:
        +  `mongodb:[//[user[:password]@]host[:port]][/database]`

3. Start ZeKKe services with the following command

```sh
$ ./gradlew clean bootRun
```

> Look at [Mongo collections](mongo/collections/collections.md) for sample data sets.

REST API
--------

Endpoint documentation, requests and responses [here](API.md)

License
-------

    Copyright 2017 Daniel Pedraza-Arcega

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
