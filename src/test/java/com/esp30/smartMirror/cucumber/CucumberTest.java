package com.esp30.smartMirror.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue="com.esp30.smartMirror.bdd.stepdefs", plugin = {"pretty", "json:target/cucumber/report.json", "html:target/cucumber/html", "junit:target/cucumber/report_junit.xml"})
public class CucumberTest {
}
