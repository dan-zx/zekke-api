/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.persistence.internal.mongo;

/**
 * Collections fields names.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Fields {

    private Fields() {
        throw new AssertionError();
    }

    public static class Common {
        public static final String ID = "_id";

        private Common() {
            throw new AssertionError();
        }
    }

    public static class Waypoint extends Common {
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String LOCATION = "location";
        public static final String PATHS = "paths";

        private Waypoint() {}
    }

    public static class Path extends Common {
        public static final String DISTANCE = "distance";
        public static final String TO_WAYPOINT = "to_waypoint";

        private Path() {}
    }

    public static class Sequence extends Common {
        public static final String SEQ = "seq";

        private Sequence() {}
    }

    public static class Function {
        public static final String RESULT = "retval";

        private Function() {
            throw new AssertionError();
        }
    }
}