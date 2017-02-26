package com.github.danzx.zekke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteMe {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteMe.class);

    private DeleteMe() {
        throw new AssertionError();
    }

    public static boolean always() {
        LOGGER.debug("Delete me!!");
        return true;
    }
}
