package com.esp30.smartMirror.cucumber;

import com.esp30.smartMirror.SmartMirrorApplication;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SmartMirrorApplication.class, loader = SpringBootContextLoader.class)
public class CucumberSpringConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CucumberSpringConfiguration.class);

    @Before
    public void setUp() {
        log.info("-------------- Spring Context Initialized For Executing Cucumber Tests --------------");
    }
}

