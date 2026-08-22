package org.apitest;

import org.testng.TestNG;

public class MainRunner {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("      RoutineCheck5S Automation");
        System.out.println("====================================");

        TestNG testNG = new TestNG();

        testNG.setTestSuites(java.util.Collections.singletonList("testng.xml"));

        testNG.run();

        System.out.println("====================================");
        System.out.println("       Test execution completed");
        System.out.println("====================================");
    }

}
