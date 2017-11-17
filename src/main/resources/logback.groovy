appender('console', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%-5level in %logger - %message%n'
    }
}

logger('javax.management', WARN)
logger('ma.glasnost.orika', WARN)
logger('net.rakugakibox.spring.boot.orika', WARN)
logger('org.apache.catalina', WARN)
logger('org.apache.coyote', WARN)
logger('org.apache.tomcat', WARN)
logger('org.glassfish.jersey', WARN)
logger('org.hibernate.validator', WARN)
logger('org.jboss.logging', WARN)
logger('org.jvnet.hk2', WARN)
logger('org.mongodb.driver', WARN)
logger('org.mongodb.morphia', WARN)
logger('org.springframework', WARN)

root(WARN, ['console'])
