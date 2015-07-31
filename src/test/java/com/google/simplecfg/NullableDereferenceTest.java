/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.simplecfg;

import static com.google.common.truth.Truth.assertThat;
import com.google.simplecfg.ast.CompilationUnit;
import com.google.simplecfg.ast.ExtendJFinding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;

/**
 * Integration tests for the nullable dereference checker.
 * <p>
 * Tests are grouped by the type of null guard used, and then split arbitrarily into separate
 * tests/files in order to not have too many positive/negative finding tests in a single test file.
 */
@RunWith(JUnit4.class)
public class NullableDereferenceTest {

  @Test public void suggestedFixEndsWithNewline() {
    CompilationUnit unit = StmtCfgTest.parseFile("NullableNullGuard01");
    Collection<ExtendJFinding> findings = unit.findings();
    assertThat(findings).isNotEmpty();
    ExtendJFinding finding = findings.iterator().next();
    assertThat(finding.fixes).hasSize(1);
    assertThat(finding.fixes.iterator().next().newText).endsWith("\n");
  }

  @Test public void nullGuard01() {
    Collection<String> findings = StmtCfgTest.findings("NullableNullGuard01");
    assertThat(findings).containsExactly(
        "testdata/NullableNullGuard01.javax:42:25: Dereferencing p, which was declared @Nullable.",
        "testdata/NullableNullGuard01.javax:49:12: Dereferencing p, which was declared @Nullable.",
        "testdata/NullableNullGuard01.javax:62:12: Dereferencing p, which was declared @Nullable."
        );
  }

  @Test public void nullGuard02() {
    Collection<String> findings = StmtCfgTest.findings("NullableNullGuard02");
    assertThat(findings).containsExactly(
        "testdata/NullableNullGuard02.javax:49:7: Dereferencing p, which was declared @Nullable.",
        "testdata/NullableNullGuard02.javax:54:7: Dereferencing p, which was declared @Nullable."
        );
  }

  @Test public void methodNullGuard01() {
    Collection<String> findings = StmtCfgTest.findings("NullableMethodNullGuard01");
    assertThat(findings).isEmpty();
  }

  @Test public void dataflowFalsePositives01() {
    Collection<String> findings = StmtCfgTest.findings("NullableDataflow01");
    assertThat(findings).containsExactly(
        "testdata/NullableDataflow01.javax:31:7: Dereferencing p, which was declared @Nullable.",
        "testdata/NullableDataflow01.javax:38:7: Dereferencing p, which was declared @Nullable."
        );
  }
}
