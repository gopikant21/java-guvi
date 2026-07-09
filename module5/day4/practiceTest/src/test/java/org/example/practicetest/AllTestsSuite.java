package org.example.practicetest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "org.example.practicetest.controller",
        "org.example.practicetest.service",
        "org.example.practicetest.repository",
        "org.example.practicetest.security",
        "org.example.practicetest.exception",
        "org.example.practicetest.integration"
})
@SelectClasses({PracticeTestApplicationTests.class})
class AllTestsSuite {
}

