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

    b. Create the stored procedure function [`getNextSequenceValue.js`](mongo/functions/getNextSequenceValue.js) in mongo.

    c. Create the geographic [index](mongo/indexes.js) in the location field of waypoints collections.

2. Start ZeKKe services

```sh
$ ./gradlew clean bootRun
```

> Look at [Mongo collections](mongo/collections/collections.md) for sample data sets.

REST API
--------

Endpoint documentation, requests and responses [here](API.md)

Technologies/libraries used
---------------------------

* MongoDB
* Morphia
* EmbedMongo
* Spring Boot
* Jersey
* Orika Mapper
* Hibernate Validator
* JSON Patch
* JWT
* Logback Groovy
* AssertJ
* JUnit + JUnitParams

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
