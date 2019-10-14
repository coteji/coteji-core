/*
 * Copyright (c) 2019 Coteji AUTHORS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.coteji.fs;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.utils.SourceRoot;
import org.coteji.core.Test;
import org.coteji.core.TestsSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class JavaClassesTestsSource implements TestsSource {

    private static final String TESTS_DIR = "path/to/tests";
    private static final String TEST_METHODS = "@Test";

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
        List<Test> result = new ArrayList<>();
        Path packagePath = new File(TESTS_DIR).toPath();
        try {
            new SourceRoot(packagePath)
                    .tryToParse().stream().filter(ParseResult::isSuccessful)
                    .map(unit -> unit.getResult()
                            .orElseThrow(() -> new RuntimeException("No result in compilation unit")))
                    .forEach(unit -> unit.findAll(MethodDeclaration.class)
                            .forEach(method -> {
                                        if (isTest(method)) {
                                            Test test = new Test();
                                            test.setName(method.getNameAsString());

                                            result.add(test);
                                        }
                                    }
                            ));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private boolean isTest(MethodDeclaration method) {
        if (TEST_METHODS == null || TEST_METHODS.isEmpty() || TEST_METHODS.equals("any")) {
            return true;
        } else if (TEST_METHODS.startsWith("any:")) {
            return method.findFirst(Modifier.class, m -> m.getKeyword().asString().equals(TEST_METHODS.substring(4))).isPresent();
        } else if (TEST_METHODS.startsWith("contains:")) {
            return method.getNameAsString().contains(TEST_METHODS.substring(9));
        } else if (TEST_METHODS.startsWith("startsWith:")) {
            return method.getNameAsString().startsWith(TEST_METHODS.substring(11));
        } else if (TEST_METHODS.startsWith("endsWith:")) {
            return method.getNameAsString().endsWith(TEST_METHODS.substring(9));
        } else if (TEST_METHODS.startsWith("@")) {
            return method.findFirst(AnnotationExpr.class, a -> a.getNameAsString().equals(TEST_METHODS.substring(1))).isPresent();
        }
        return false;
    }
}
