package org.coteji.fs;

import org.coteji.core.Test;
import org.coteji.core.TestsSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JavaClassesTestsSource implements TestsSource {
    @Override
    public void readPropertyFile(String filePath) {
        System.out.println("Read file: " + filePath);
    }

    @Override
    public Test getTest(String searchCriteria) {
        System.out.println("Test retrieved with filter " + searchCriteria);
        return null;
    }

    @Override
    public List<Test> getTests(String searchCriteria) {
        System.out.println("Tests retrieved with filter " + searchCriteria);
        return null;
    }

    @Override
    public List<Test> getAllTests() {
        System.out.println("All Tests retrieved");
        return null;
    }
}
