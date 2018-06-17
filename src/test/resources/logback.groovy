import ch.qos.logback.classic.jul.LevelChangePropagator

import org.slf4j.bridge.SLF4JBridgeHandler

def lcp = new LevelChangePropagator()
lcp.context = context
lcp.resetJUL = true
context.addListener(lcp)

java.util.logging.LogManager.getLogManager().reset()
SLF4JBridgeHandler.removeHandlersForRootLogger()
SLF4JBridgeHandler.install()
java.util.logging.Logger.getLogger('global').setLevel(java.util.logging.Level.FINEST)

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
logger('org.glassfish', WARN)
logger('org.glassfish.jersey.server.spring.SpringComponentProvider', OFF)
logger('org.hibernate.validator', WARN)
logger('org.jboss.logging', WARN)
logger('org.jvnet.hk2', WARN)
logger('org.mongodb.driver', WARN)
logger('org.mongodb.morphia', WARN)
logger('org.springframework', WARN)
logger('sun.net', WARN)

logger('com.github.danzx.zekke.test.mongo.EmbeddedMongo', WARN)

root(ALL, ['console'])
