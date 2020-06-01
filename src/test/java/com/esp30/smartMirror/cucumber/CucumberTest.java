package com.esp30.smartMirror.cucumber;

import com.esp30.smartMirror.SmartMirrorApplication;
import com.esp30.smartMirror.controllers.ApiController;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue="com.esp30.smartMirror.bdd.stepdefs", plugin = {"pretty", "json:target/cucumber/report.json", "html:target/cucumber/html", "junit:target/cucumber/report_junit.xml"})
@ContextConfiguration(classes = ApiController.class)
public class CucumberTest {
}
