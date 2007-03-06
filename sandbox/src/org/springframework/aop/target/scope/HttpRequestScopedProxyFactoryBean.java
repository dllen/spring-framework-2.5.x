/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springframework.aop.target.scope;

import org.springframework.web.filter.ThreadBoundHttpRequestScopeIdentifierResolver;
import org.springframework.web.filter.HttpRequestScopeMap;

/**
 * <p>Convenience factory bean that stores scoped objects to and retrieves 
 * scoped objects from the HTTP request.
 *
 * <p>To use this factory bean the HTTP request needs to be bound to the current
 * thread. Look at {@link org.springframework.web.filter.RequestBindingFilter} or
 * {@link org.springframework.web.filter.RequestBindingHandlerInterceptor} to
 * bind the HTTP request to the current thread.
 *
 * @see org.springframework.web.filter.RequestBindingFilter
 * @see org.springframework.web.filter.RequestBindingHandlerInterceptor
 * @see org.springframework.web.filter.ThreadBoundHttpRequestScopeIdentifierResolver
 * @see org.springframework.web.filter.HttpRequestScopeMap
 * @see org.springframework.aop.target.scope.ScopedProxyFactoryBean
 * @see org.springframework.aop.target.scope.ScopedTargetSource
 * @author Steven Devijver
 * @since 1.3
 */
public class HttpRequestScopedProxyFactoryBean extends ScopedProxyFactoryBean {
	
	private static final long serialVersionUID = -2181836338908771974L;

	public HttpRequestScopedProxyFactoryBean() {
		super();
		setScopeIdentifierResolver(new ThreadBoundHttpRequestScopeIdentifierResolver());
		setScopeMap(new HttpRequestScopeMap());
	}
}