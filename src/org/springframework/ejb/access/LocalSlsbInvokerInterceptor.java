/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package org.springframework.ejb.access;

import java.lang.reflect.InvocationTargetException;

import javax.ejb.EJBLocalObject;

import org.aopalliance.intercept.AspectException;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interceptor that invokes a local Stateless Session Bean, after caching
 * the home object. A local EJB home can never go stale.
 * @author Rod Johnson
 * @version $Id: LocalSlsbInvokerInterceptor.java,v 1.3 2003-12-19 11:28:42 jhoeller Exp $
 */
public class LocalSlsbInvokerInterceptor extends AbstractSlsbInvokerInterceptor {

	protected EJBLocalObject newSessionBeanInstance() {
		if (logger.isDebugEnabled()) {
			logger.debug("Trying to create reference to remote EJB");
		}

		EJBLocalObject session = (EJBLocalObject) getHomeBeanWrapper().invoke(CREATE_METHOD, null);

		if (logger.isDebugEnabled()) {
			logger.debug("Obtained reference to remote EJB: " + session);
		}
		return session;
	}

	/**
	 * This is the last invoker in the chain:
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		EJBLocalObject ejb = newSessionBeanInstance();
		try {
			return invocation.getMethod().invoke(ejb, invocation.getArguments());
		}
		catch (InvocationTargetException ex) {
			logger.info("Method of local EJB [" + getJndiName() + "] threw exception", ex.getTargetException());
			throw ex.getTargetException();
		}
		catch (Throwable t) {
			throw new AspectException("Failed to invoke local EJB [" + getJndiName() + "]", t);
		}
	}

}
