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
package com.github.danzx.zekke.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ThrowablesTest {

    @Test
    @Parameters(method = "throwablesAndExectedOptionals")
    public void shouldGetRootCauseReturnNonNullOptional(Throwable throwable, Optional<Throwable> expected) {
        assertThat(Throwables.getRootCause(throwable)).isNotNull().isEqualTo(expected);
    }

    public Object[] throwablesAndExectedOptionals() {
        Exception ex1 = new Exception();
        Optional<Throwable> opt1 = Optional.of(ex1);

        Exception ex2Cause = new Exception();
        Exception ex2 = new Exception(ex2Cause);
        Optional<Throwable> opt2 = Optional.of(ex2Cause);

        Exception ex3Cause = new Exception();
        Exception ex3 = new Exception(new Exception(new Exception(ex3Cause)));
        Optional<Throwable> opt3 = Optional.of(ex3Cause);

        Exception ex4Cause = new Exception((Throwable)null);
        Exception ex4 = new Exception(new Exception(new Exception(ex4Cause)));
        Optional<Throwable> opt4 = Optional.of(ex4Cause);
        return new Object[][] {
            {null, Optional.empty()},
            {ex1, opt1},
            {ex2, opt2},
            {ex3, opt3},
            {ex4, opt4}
        };
    }
}
