package com.github.danzx.zekke.message;

import java.util.Optional;

import com.github.danzx.zekke.base.Buildable;

public class MessageNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2460914822489548196L;

    private MessageNotFoundException() {}

    private MessageNotFoundException(String message) {
        super(message);
    }

    private MessageNotFoundException(Throwable cause) {
        super(cause);
    }

    private MessageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class Builder implements Buildable<MessageNotFoundException> {
        private String message;
        private Throwable cause;

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        private Optional<String> getMessage() {
            return Optional.ofNullable(message);
        }

        private Optional<Throwable> getCause() {
            return Optional.ofNullable(cause);
        }

        @Override
        public MessageNotFoundException build() {
            Optional<String> optMessage = getMessage();
            Optional<Throwable> optCause = getCause();

            if (optCause.isPresent()) {
                if (optMessage.isPresent()) return new MessageNotFoundException(message, cause);
                return new MessageNotFoundException(cause);
            } else {
                if (optMessage.isPresent()) return new MessageNotFoundException(message);
                return new MessageNotFoundException();
            }
        }
    }
}
