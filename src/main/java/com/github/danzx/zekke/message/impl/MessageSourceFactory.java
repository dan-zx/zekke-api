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
package com.github.danzx.zekke.message.impl;

import com.github.danzx.zekke.message.MessageSource;

/**
 * Creates message sources.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class MessageSourceFactory {

    private MessageSourceFactory() {
        throw new AssertionError();
    }

    /** @return the default message source for this application. */
    public static MessageSource defaultSource() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final String MESSAGES_BASENAME = "com.github.danzx.zekke.Messages";
        private static final MessageSource INSTANCE = new DefaultMessageDecorator(new ResourceBundleMessageSource(MESSAGES_BASENAME));

        private InstanceHolder() {
            throw new AssertionError();
        }
    }
}
