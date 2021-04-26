package com.example.boot;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * @author wzh
 * @since 2021/4/23
 */
public class BaseTest {
    @Test
    public void testClassPath() {
        try {
            System.out.println(ResourceUtils.getURL("/").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
