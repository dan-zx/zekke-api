package com.github.danzx.zekke.message;

import java.util.Locale;

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
