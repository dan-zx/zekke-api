package com.github.danzx.zekke.message.impl;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

import com.github.danzx.zekke.message.MessageNotFoundException;
import com.github.danzx.zekke.message.MessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMessageDecorator implements MessageSource {

    public static final String DEFAULT_MISSING_KEY_FORMAT = "???%s???";

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageDecorator.class);

    private final MessageSource messageSource;
    private String missingKeyFormat = DEFAULT_MISSING_KEY_FORMAT;

    public DefaultMessageDecorator(MessageSource messageSource) {
        this.messageSource = requireNonNull(messageSource);
    }

    @Override
    public String getMessage(String messageKey, Object... messageArguments) {
        try {
            return messageSource.getMessage(messageKey, messageArguments);
        } catch (MessageNotFoundException ex) {
            LOGGER.warn("Can't find message for key: [{}]", messageKey, ex);
            return String.format(missingKeyFormat, messageKey);
        }
    }

    @Override
    public String getMessage(String messageKey, Locale locale, Object... messageArguments) {
        try {
            return messageSource.getMessage(messageKey, locale, messageArguments);
        } catch (MessageNotFoundException ex) {
            LOGGER.warn("Can't find message for key: [{}]", messageKey, ex);
            return String.format(missingKeyFormat, messageKey);
        }
    }

    public void setMissingKeyFormat(String missingKeyFormat) {
        this.missingKeyFormat = requireNonNull(missingKeyFormat);
    }
}
