// File: taskscheduler/test/TestRunner.java
package taskscheduler.test;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

/**
 * Test runner to execute all JUnit tests
 */
public class TestRunner {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectPackage("taskscheduler.test"))
            .filters(includeClassNamePatterns(".*Test"))
            .build();
        
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        
        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println("\n═══════ TEST EXECUTION SUMMARY ═══════");
        System.out.println("Tests found: " + summary.getTestsFoundCount());
        System.out.println("Tests started: " + summary.getTestsStartedCount());
        System.out.println("Tests successful: " + summary.getTestsSucceededCount());
        System.out.println("Tests failed: " + summary.getTestsFailedCount());
        System.out.println("Tests skipped: " + summary.getTestsSkippedCount());
        System.out.println("═════════════════════════════════════\n");
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("Failed tests:");
            summary.getFailures().forEach(failure -> {
                System.out.println("  - " + failure.getTestIdentifier().getDisplayName());
                System.out.println("    " + failure.getException().getMessage());
            });
        }
    }
}