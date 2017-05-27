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
package com.github.danzx.zekke.message;

import java.util.Locale;

/**
 * Messages source contract.
 *  
 * @author Daniel Pedraza-Arcega
 */
public interface MessageSource {

    /**
     * Gets a message for the given key the underlying source formatted with the given format
     * arguments. Optionally, it can throw a MessageNotFoundException when the message is not found. 
     *
     * @param messageKey the key for the desired message.
     * @param messageArguments the objects to be formatted and substituted in the message.
     * @return otherwise the message for the given key formatted with the given format arguments.
     */
    String getMessage(String messageKey, Object... messageArguments);

    /**
     * Gets a message for the given key the underlying source formatted with the given format
     * arguments. Optionally, it can throw a MessageNotFoundException when the message is not found.
     *
     * @param messageKey the key for the desired message.
     * @param locale the locale of the message.
     * @param messageArguments the objects to be formatted and substituted in the message.
     * @return otherwise the message for the given key formatted with the given format arguments.
     */
    String getMessage(String messageKey, Locale locale, Object... messageArguments);
}
