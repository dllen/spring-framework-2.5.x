/*
 * Copyright 2007 the original author or authors.
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.test.context.junit4;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;

/**
 * <p>
 * Verifies proper handling of JUnit's {@link org.junit.Ignore @Ignore} and
 * Spring's
 * {@link org.springframework.test.annotation.IfProfileValue @IfProfileValue}
 * and {@link ProfileValueSourceConfiguration @ProfileValueSourceConfiguration}
 * (with an <em>explicit, custom defined {@link ProfileValueSource}</em>)
 * annotations in conjunction with the {@link SpringJUnit4ClassRunner}.
 * </p>
 *
 * @author Sam Brannen
 * @since 2.5
 * @see EnabledAndIgnoredSpringRunnerTests
 */
@ProfileValueSourceConfiguration(HardCodedProfileValueSourceSpringRunnerTests.HardCodedProfileValueSource.class)
public class HardCodedProfileValueSourceSpringRunnerTests extends EnabledAndIgnoredSpringRunnerTests {

	// XXX Remove suite() once we've migrated to Ant 1.7 with JUnit 4 support.
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(HardCodedProfileValueSourceSpringRunnerTests.class);
	}

	@BeforeClass
	public static void setProfileValue() {
		numTestsExecuted = 0;
		// Set the system property to something other than VALUE as a sanity
		// check.
		System.setProperty(NAME, "999999999999");
	}


	public static class HardCodedProfileValueSource implements ProfileValueSource {

		public String get(final String key) {
			return (key.equals(NAME) ? VALUE : null);
		}
	}
}