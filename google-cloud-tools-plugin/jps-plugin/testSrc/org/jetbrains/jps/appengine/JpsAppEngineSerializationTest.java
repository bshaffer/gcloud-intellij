/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.jps.appengine;

import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.jps.appengine.model.JpsAppEngineExtensionService;
import org.jetbrains.jps.appengine.model.JpsAppEngineModuleExtension;
import org.jetbrains.jps.appengine.model.PersistenceApi;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.JpsSerializationTestCase;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author nik
 */
public class JpsAppEngineSerializationTest extends JpsSerializationTestCase {
  public static final String PROJECT_PATH = "serialization/appEngine";

  public void testLoad() {
    loadProject(PROJECT_PATH + "/appEngine.ipr");
    JpsModule module = assertOneElement(myProject.getModules());
    assertEquals("appEngine", module.getName());
    JpsAppEngineModuleExtension extension = JpsAppEngineExtensionService.getInstance().getExtension(module);
    assertNotNull(extension);
    assertEquals(PersistenceApi.JPA2, extension.getPersistenceApi());
    assertEquals(FileUtil.toSystemIndependentName(getTestDataFileAbsolutePath(PROJECT_PATH) + "/src"), assertOneElement(extension.getFilesToEnhance()));
    assertTrue(extension.isRunEnhancerOnMake());
  }

  @Override
  protected String getTestDataFileAbsolutePath(String relativePath) {
    return new File(getTestDataPath(), relativePath).getAbsolutePath();
  }

  public static File getTestDataPath() {
    try {
      URL resource = JpsAppEngineSerializationTest.class.getResource("/serialization");
      File testDataRoot = Paths.get(resource.toURI()).toFile().getParentFile();
      return testDataRoot;
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
