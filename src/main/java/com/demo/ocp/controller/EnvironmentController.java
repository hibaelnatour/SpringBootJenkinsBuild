package com.demo.ocp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test for Spring Boot with a Hello World
 */
@RestController
@RequestMapping("/env")
public class EnvironmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentController.class);

    @Value( "${spring.profiles.active}" )
    private String profile;
    /**
     * Which environment we are.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    String getEnv() {
        LOGGER.info("Get Environment profile : " + this.profile);
        return this.profile;
    }
}
