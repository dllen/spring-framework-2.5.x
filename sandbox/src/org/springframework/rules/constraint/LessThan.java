/*
 * Copyright 2002-2004 the original author or authors.
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
package org.springframework.rules.constraint;

import java.util.Comparator;

import org.springframework.rules.closure.BinaryConstraint;

/**
 * Predicate that tests if one comparable object is less than another.
 *
 * @author Keith Donald
 */
public class LessThan extends ComparisonBinaryPredicate implements
		BinaryConstraint {

	private static final LessThan INSTANCE = new LessThan();

	public LessThan() {
		super();
	}

	public LessThan(Comparator comparator) {
		super(comparator);
	}

	protected boolean testCompareResult(int result) {
		return result < 0;
	}

	public static BinaryConstraint instance() {
		return INSTANCE;
	}

	public static BinaryConstraint instance(Comparator c) {
		return new LessThan(c);
	}

	public String toString() {
		return RelationalOperator.LESS_THAN.toString();
	}

}