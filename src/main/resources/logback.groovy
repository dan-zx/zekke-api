appender('console', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%d{yyyy/MM/dd HH:mm:ss.SSS} %-5level in %logger.%method\\(\\) - %message%n'
    }
}

root(ALL, ['console'])
