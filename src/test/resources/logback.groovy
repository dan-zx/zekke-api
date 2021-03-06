appender('console', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%d{yyyy/MM/dd HH:mm:ss.SSS} %-5level in %logger.%method\\(\\) - %message%n'
    }
}

logger('javax.management', WARN)
logger('com.mongodb', WARN)
logger('de.flapdoodle.embed', WARN)
logger('ma.glasnost.orika', WARN)
logger('net.rakugakibox.spring.boot.orika', WARN)
logger('org.apache.catalina', WARN)
logger('org.apache.commons.beanutils', WARN)
logger('org.apache.coyote', WARN)
logger('org.apache.tomcat', WARN)
logger('org.glassfish.jersey', WARN)
logger('org.hibernate.validator', WARN)
logger('org.jboss.logging', WARN)
logger('org.jvnet.hk2', WARN)
logger('org.mongodb.driver', WARN)
logger('org.mongodb.morphia', WARN)
logger('org.springframework', WARN)

logger('com.github.danzx.zekke.test.mongo.EmbeddedMongo', WARN)

root(ALL, ['console'])
