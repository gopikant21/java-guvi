package org.example.backend.unit;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("org.example.backend.unit")
public class AllUnitTestsSuite {
    // Runs all tests under org.example.backend.unit package.
}

