/*
 * Copyright 2016 Yu Sheng. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.ysheng.auth.common.test.utility;

import com.ysheng.auth.common.utility.ReflectionUtil;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link com.ysheng.auth.common.utility.ReflectionUtil}.
 */
public class ReflectionUtilTest {

  @Test
  public void succeedsToGetAnnotatedFieldNamesAndValues() throws Throwable {
    ReflectionTestClass testObject = getTestInstance();
    Map<String, Object> hash = ReflectionUtil.getAnnotatedFieldNamesAndValues(
        ReflectionTestClass.class,
        ReflectionTestAnnotation.class,
        testObject);

    assertThat(hash.size(), is(5));
    assertThat(hash.containsKey("privateField"), is(true));
    assertThat(hash.get("privateField"), equalTo("value_privateField"));
    assertThat(hash.containsKey("protectedField"), is(true));
    assertThat(hash.get("protectedField"), equalTo("value_protectedField"));
    assertThat(hash.containsKey("enumField"), is(true));
    assertThat(hash.get("enumField"), equalTo(ReflectionTestEnum.FOO));
    assertThat(hash.containsKey("integerField"), is(true));
    assertThat(hash.get("integerField"), equalTo(1234));
    assertThat(hash.containsKey("stringField"), is(true));
    assertThat(hash.get("stringField"), equalTo("value_stringField"));
  }

  @Test
  public void succeedsToGetAnnotatedFieldNamesAndValuesWithNullValue() throws Throwable {
    ReflectionTestClass testObject = getTestInstance();
    testObject.integerField = null;
    Map<String, Object> hash = ReflectionUtil.getAnnotatedFieldNamesAndValues(
        ReflectionTestClass.class,
        ReflectionTestAnnotation.class,
        testObject);

    assertThat(hash.size(), is(4));
    assertThat(hash.containsKey("privateField"), is(true));
    assertThat(hash.get("privateField"), equalTo("value_privateField"));
    assertThat(hash.containsKey("protectedField"), is(true));
    assertThat(hash.get("protectedField"), equalTo("value_protectedField"));
    assertThat(hash.containsKey("enumField"), is(true));
    assertThat(hash.get("enumField"), equalTo(ReflectionTestEnum.FOO));
    assertThat(hash.containsKey("stringField"), is(true));
    assertThat(hash.get("stringField"), equalTo("value_stringField"));
  }

  @Test
  public void succeedsToSetFieldValues() throws Throwable {
    ReflectionTestClass testObject = getTestInstance();
    Map<String, String> testHash = getTestHash();
    ReflectionUtil.setFieldValues(
        ReflectionTestClass.class,
        testObject,
        testHash);

    assertThat(testObject.notAnnotatedField, equalTo("value_notAnnotatedField2"));
    assertThat(testObject.privateField, equalTo("value_privateField2"));
    assertThat(testObject.protectedField, equalTo("value_protectedField2"));
    assertThat(testObject.enumField, equalTo(ReflectionTestEnum.BAR));
    assertThat(testObject.integerField, equalTo(5678));
    assertThat(testObject.stringField, equalTo("value_stringField2"));
  }

  @Test
  public void succeedsToSetFieldValuesWithMissingField() throws Throwable {
    ReflectionTestClass testObject = getTestInstance();
    Map<String, String> testHash = getTestHash();
    testHash.remove("stringField");
    ReflectionUtil.setFieldValues(
        ReflectionTestClass.class,
        testObject,
        testHash);

    assertThat(testObject.notAnnotatedField, equalTo("value_notAnnotatedField2"));
    assertThat(testObject.privateField, equalTo("value_privateField2"));
    assertThat(testObject.protectedField, equalTo("value_protectedField2"));
    assertThat(testObject.enumField, equalTo(ReflectionTestEnum.BAR));
    assertThat(testObject.integerField, equalTo(5678));
    assertThat(testObject.stringField, equalTo("value_stringField"));
  }

  private ReflectionTestClass getTestInstance() {
    ReflectionTestClass testInstance = new ReflectionTestClass();
    testInstance.notAnnotatedField = "value_notAnnotatedField";
    testInstance.privateField = "value_privateField";
    testInstance.protectedField = "value_protectedField";
    testInstance.enumField = ReflectionTestEnum.FOO;
    testInstance.integerField = 1234;
    testInstance.stringField = "value_stringField";

    return testInstance;
  }

  private Map<String, String> getTestHash() {
    Map<String, String> testHash = new HashMap<>();
    testHash.put("notAnnotatedField", "value_notAnnotatedField2");
    testHash.put("privateField", "value_privateField2");
    testHash.put("protectedField", "value_protectedField2");
    testHash.put("enumField", ReflectionTestEnum.BAR.toString());
    testHash.put("integerField", "5678");
    testHash.put("stringField", "value_stringField2");

    return testHash;
  }

  /**
   * Test annotation.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  private @interface ReflectionTestAnnotation {
  }

  /**
   * Test enum.
   */
  private enum ReflectionTestEnum {
    FOO,
    BAR
  }

  /**
   * Test class.
   */
  private class ReflectionTestClass {

    public String notAnnotatedField;

    @ReflectionTestAnnotation
    private String privateField;

    @ReflectionTestAnnotation
    protected String protectedField;

    @ReflectionTestAnnotation
    public ReflectionTestEnum enumField;

    @ReflectionTestAnnotation
    public Integer integerField;

    @ReflectionTestAnnotation
    public String stringField;
  }
}
