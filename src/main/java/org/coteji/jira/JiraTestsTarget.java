package org.coteji.jira;

import org.coteji.core.Test;
import org.coteji.core.TestsTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JiraTestsTarget implements TestsTarget {
    @Override
    public void readPropertyFile(String filePath) {
        System.out.println("Read file: " + filePath);
    }

    @Override
    public void push(Test test) {
        System.out.println("Test pushed");
    }

    @Override
    public void pushAll(List<Test> tests) {
        System.out.println("All tests pushed");
    }
}
