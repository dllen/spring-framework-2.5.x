/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springframework.web.servlet.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContextException;

/**
 * Abstract implementation of the {@link org.springframework.web.servlet.HandlerMapping}
 * interface, detecting URL mappings for handler beans through introspection of all
 * defined beans in the application context.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see #determineUrlsForHandler
 */
public abstract class AbstractDetectingUrlHandlerMapping extends AbstractUrlHandlerMapping {

	private boolean detectHandlersInAncestorContexts = false;


	/**
	 * Set whether to detect handler beans in ancestor ApplicationContexts.
	 * <p>Default is "false": Only handler beans in the current
	 * ApplicationContext will be detected, that is, only in the context
	 * that this BeanNameUrlHandlerMapping itself is defined in (typically
	 * the current DispatcherServlet's context).
	 * <p>Switch this flag on to detect handler beans in ancestor contexts
	 * (typically the Spring root WebApplicationContext) as well.
	 */
	public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts) {
		this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
	}


	/**
	 * Calls the {@link #detectHandlers()} method in addition to the
	 * superclass's initialization.
	 */
	public void initApplicationContext() throws ApplicationContextException {
		super.initApplicationContext();
		detectHandlers();
	}

	/**
	 * Register all handlers found in the current ApplicationContext.
	 * Any bean whose name appears to be a URL is considered a handler.
	 * @throws org.springframework.beans.BeansException if the handler couldn't be registered
	 * @see #determineUrlsForHandler
	 */
	protected void detectHandlers() throws BeansException {
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for URL mappings in application context: " + getApplicationContext());
		}
		String[] beanNames = (this.detectHandlersInAncestorContexts ?
				BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) :
				getApplicationContext().getBeanNamesForType(Object.class));

		// Take any bean name or alias that begins with a slash.
		for (int i = 0; i < beanNames.length; i++) {
			String beanName = beanNames[i];
			String[] urls = determineUrlsForHandler(beanName);
			if (urls.length > 0) {
				// URL paths found: Let's consider it a handler.
				registerHandler(urls, beanName);
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Rejected bean name '" + beanNames[i] + "': no URL paths identified");
				}
			}
		}
	}


	/**
	 * Determine the URLs for the given handler bean.
	 * @param beanName the name of the candidate bean
	 * @return the URLs determined for the bean, or an empty array if none
	 */
	protected abstract String[] determineUrlsForHandler(String beanName);

}