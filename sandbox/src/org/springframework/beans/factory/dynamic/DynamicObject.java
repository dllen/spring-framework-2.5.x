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

package org.springframework.beans.factory.dynamic;

import org.springframework.beans.BeansException;

/**
 * Interface to be implemented by dynamic objects,
 * which support reloading and optionally polling for
 * updates.
 * @author Rod Johnson
 */
public interface DynamicObject extends ExpirableObject {
	
	int getLoadCount();
	
	void refresh() throws BeansException;
	
	/**
	 * @return the number of milliseconds after refreshing
	 * that this object will go stale.  
	 * 0 means no expiry 
	 **/
	long getExpiryMillis();
	
	long getLastRefreshMillis();

	boolean isAutoRefresh();
	
	void setAutoRefresh(boolean autoRefresh);
}