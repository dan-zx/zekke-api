package com.github.danzx.zekke.message.impl;

import static com.github.danzx.zekke.util.Strings.requireNonBlank;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import com.github.danzx.zekke.message.MessageNotFoundException;
import com.github.danzx.zekke.message.MessageSource;

public class ResourceBundleMessageSource implements MessageSource {
    
    private final String basename;
    private final Set<Locale> supportedLocales;

    public ResourceBundleMessageSource(String basename) {
        this(basename, singleton(Locale.ROOT));
    }

    public ResourceBundleMessageSource(String basename, Set<Locale> supportedLocales) {
        this.basename = requireNonBlank(basename);
        requireNonNull(supportedLocales);
        this.supportedLocales = supportedLocales.isEmpty() ? singleton(Locale.ROOT) : unmodifiableSet(supportedLocales);
    }

    @Override
    public String getMessage(String messageKey, Object... messageArguments) throws MessageNotFoundException {
        return getMessage(messageKey, Locale.ROOT, messageArguments);
    }

    @Override
    public String getMessage(String messageKey, Locale locale, Object... messageArguments) throws MessageNotFoundException {
        requireNonBlank(messageKey);
        requireNonNull(locale);
        if (messageArguments == null) messageArguments = new Object[0];
        String message;
        try {
            message = getMessagesBundle(locale).getString(messageKey);
        } catch (MissingResourceException ex) {
            throw new MessageNotFoundException.Builder()
                .withMessage(ex.getMessage())
                .cause(ex)
                .build();
        }
        if (messageArguments.length > 0) message = MessageFormat.format(message, messageArguments);
        return message;
    }

    protected String getBasename() {
        return basename;
    }

    protected Set<Locale> getSupportedLocales() {
        return supportedLocales;
    }

    protected ResourceBundle getMessagesBundle(Locale locale) {
        return supportedLocales.contains(locale) ? ResourceBundle.getBundle(basename, locale) : ResourceBundle.getBundle(basename, Locale.ROOT);
    }
}
