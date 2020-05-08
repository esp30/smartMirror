package com.esp30.smartMirror.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", plugin = {"pretty",
        "json:target/cucumber-report.json"})
public class CucumberTest {
}
