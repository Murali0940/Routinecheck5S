package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import utils.EmailReportService;
import utils.TestExecutionReport;

public class EmailReportListener
                implements ISuiteListener {

        @Override
        public void onStart(ISuite suite) {

                TestExecutionReport.clear();

                System.out.println(
                                "======================================");

                System.out.println(
                                "TEST SUITE STARTED");

                System.out.println(
                                "======================================");
        }

        @Override
        public void onFinish(ISuite suite) {

                System.out.println(
                                "======================================");

                System.out.println(
                                "ALL TESTS COMPLETED");

                System.out.println(
                                "======================================");

                String report = TestExecutionReport.getReport();

                System.out.println(report);

                EmailReportService.sendEmail(report);
        }
}