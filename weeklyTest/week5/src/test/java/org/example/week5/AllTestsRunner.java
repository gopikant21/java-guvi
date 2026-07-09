package org.example.week5;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.test.context.ActiveProfiles;


@Suite
@SuiteDisplayName("Spring Boot Banking API - Complete Test Suite")
@SelectPackages({
    "org.example.week5.controller",
    "org.example.week5.service",
    "org.example.week5.repository",
    "org.example.week5.security",
    "org.example.week5.exception",
    "org.example.week5.integration"
})
@SelectClasses({
    Week5ApplicationTests.class
})
public class AllTestsRunner {

}
