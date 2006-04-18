/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.scripting;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.scripting.bsh.BshScriptFactory;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.jruby.JRubyScriptFactory;

/**
 * @author Rob Harrop
 * @since 2.0
 */
public class LangNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("groovy", new ScriptBeanDefinitionParser(GroovyScriptFactory.class));
		registerBeanDefinitionParser("jruby", new ScriptBeanDefinitionParser(JRubyScriptFactory.class));
		registerBeanDefinitionParser("bsh", new ScriptBeanDefinitionParser(BshScriptFactory.class));
	}
}
