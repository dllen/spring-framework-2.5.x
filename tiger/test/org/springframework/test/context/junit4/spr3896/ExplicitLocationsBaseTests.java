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

package org.springframework.test.context.junit4.spr3896;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * <p>
 * JUnit 4 based unit test for verifying support for the
 * {@link ContextConfiguration#inheritLocations() inheritLocations} flag of
 * {@link ContextConfiguration @ContextConfiguration} indirectly proposed in <a
 * href="http://opensource.atlassian.com/projects/spring/browse/SPR-3896"
 * target="_blank">SPR-3896</a>.
 * </p>
 *
 * @author Sam Brannen
 * @since 2.5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DefaultLocationsBaseTests-context.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class ExplicitLocationsBaseTests {

	// XXX Remove suite() once we've migrated to Ant 1.7 with JUnit 4 support.
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ExplicitLocationsBaseTests.class);
	}


	@Autowired
	protected Employee employee;


	@Test
	public void verifyEmployeeSetFromBaseContextConfig() {
		assertNotNull("The employee should have been autowired.", this.employee);
		assertEquals("John Smith", this.employee.getName());
	}
}