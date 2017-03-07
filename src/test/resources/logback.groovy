appender('console', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%d{yyyy/MM/dd HH:mm:ss.SSS} %-5level in %logger.%method\\(\\) - %message%n'
    }
}

logger('org.apache.commons.beanutils', WARN)
logger('org.hibernate.validator', WARN)
logger('org.jboss.logging', WARN)

root(ALL, ['console'])
