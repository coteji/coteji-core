package org.coteji.core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    @Autowired
    private TestsSource testsSource;

    @Autowired
    private TestsTarget testsTarget;

    @Parameter(names = "-source", description = "Path to properties file of your tests source")
    private String sourcePropertyFile = "source.properties";

    @Parameter(names = "-target", description = "Path to properties file of your tests target")
    private String targetPropertyFile = "target.properties";

    @Parameter(names = "-filter", description = "Source tests filter. See format in the docs of your source type")
    private String filter;

    @Parameter(names = "-single", description = "Set this flag if you need just one test")
    private boolean singleTest;

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(CotejiConfig.class);
        Runner runner = ctx.getBean(Runner.class);
        JCommander.newBuilder().addObject(runner).build().parse(args);
        runner.run();
    }

    private void run() {
        testsSource.readPropertyFile(sourcePropertyFile);
        testsTarget.readPropertyFile(targetPropertyFile);
        if (filter == null) {
            testsTarget.pushAll(testsSource.getAllTests());
        } else if (singleTest) {
            testsTarget.push(testsSource.getTest(filter));
        } else {
            testsTarget.pushAll(testsSource.getTests(filter));
        }
    }
}