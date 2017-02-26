package com.github.danzx.zekke;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class DeleteMeTest {

    @Test
    public void test() {
        assertThat(DeleteMe.always()).isTrue();
    }
}
