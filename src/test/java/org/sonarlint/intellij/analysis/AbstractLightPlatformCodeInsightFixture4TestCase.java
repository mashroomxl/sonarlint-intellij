/*
 * SonarLint for IntelliJ IDEA
 * Copyright (C) 2015-2020 SonarSource
 * sonarlint@sonarsource.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonarlint.intellij.analysis;

import com.intellij.openapi.module.Module;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLightPlatformCodeInsightFixture4TestCase extends LightPlatformCodeInsightFixtureTestCase {

  protected static final Path FAKE_JDK_ROOT_PATH = Paths.get("src/test/resources/fake_jdk/").toAbsolutePath();

  @NotNull
  // For backward compatibility with IntelliJ 2018
  // @Override
  protected Module getModuleCompat() {
    // https://github.com/JetBrains/intellij-community/blob/c45901a53c1812c77c3beed88cb5418d84a8b1e2/platform/testFramework/src/com/intellij/testFramework/fixtures/LightPlatformCodeInsightFixtureTestCase.java#L24
    try {
      final Field myModule = LightPlatformCodeInsightFixtureTestCase.class.getDeclaredField("myModule");
      return (Module) myModule.get(this);
    } catch (NoSuchFieldException e) {
      final Method getModule;
      try {
        Class basePlatformTestCaseClass = Class.forName("com.intellij.testFramework.fixtures.BasePlatformTestCase");
        getModule = basePlatformTestCaseClass.getDeclaredMethod("getModule");
        return (Module) getModule.invoke(this);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
        throw new IllegalStateException(ex);
      }
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

}
