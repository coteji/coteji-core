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
