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

package org.springframework.web.servlet.view;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.support.RequestContext;

/**
 * AbstractTemplateView provides template based view technologies such as
 * Velocity and FreeMarker, with the ability to use request and session
 * attributes in their model and the option to expose helper objects
 * for Spring's Velocity/FreeMarker macro library.
 *
 * <p>JSP/JSTL and other view technologies automatically have access to the
 * HttpServletRequest object and thereby the request/session attributes
 * for the current user. Furthermore, they are able to create and cache
 * helper objects as request attributes themselves.
 *
 * @author Darren Davison
 * @author Juergen Hoeller
 * @since 1.0.2
 */
public abstract class AbstractTemplateView extends AbstractUrlBasedView {

	/**
	 * Variable name of the RequestContext instance in the template model,
	 * available to Spring's macros: e.g. for creating BindStatus objects.
	 */
	public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE = "springMacroRequestContext";


	private boolean exposeRequestAttributes = false;

	private boolean exposeSessionAttributes = false;

	private boolean exposeSpringMacroHelpers = false;
	
	private boolean allowRequestOverride = false;
	
	private boolean allowSessionOverride = false;


	/**
	 * Set whether all request attributes should be added to the
	 * model prior to merging with the template. Default is false.
	 */
	public void setExposeRequestAttributes(boolean exposeRequestAttributes) {
		this.exposeRequestAttributes = exposeRequestAttributes;
	}

	/**
	 * Set whether all HttpSession attributes should be added to the
	 * model prior to merging with the template. Default is false.
	 */
	public void setExposeSessionAttributes(boolean exposeSessionAttributes) {
		this.exposeSessionAttributes = exposeSessionAttributes;
	}

	/**
	 * Set whether HttpServletRequest attributes are allowed to override (hide)
	 * controller generated model attributes of the same name. Default is false,
	 * which causes an exception to be thrown if request attributes of the same
	 * name as model attributes are found.
	 */
	public void setAllowRequestOverride(boolean allowRequestOverride) {
		this.allowRequestOverride = allowRequestOverride;
	}
    
	/**
	 * Set whether HttpSession attributes are allowed to override (hide)
	 * controller generated model attributes of the same name. Default is false,
	 * which causes an exception to be thrown if session attributes of the same
	 * name as model attributes are found.
	 */
	public void setAllowSessionOverride(boolean allowSessionOverride) {
		this.allowSessionOverride = allowSessionOverride;
	}

	/**
	 * Set whether to expose a RequestContext for use by Spring's macro library,
	 * under the name "springBindRequestContext". Default is false.
	 * <p>Currently needed for Spring's Velocity and FreeMarker default macros.
	 * Note that this is <b>not</b> required for templates that use HTML
	 * forms <b>unless</b> you wish to take advantage of the Spring helper macros.
	 * @see #SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE
	 */
	public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
		this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
	}

	protected final void renderMergedOutputModel(
			Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (this.exposeRequestAttributes) {
			for (Enumeration en = request.getAttributeNames(); en.hasMoreElements();) {
				String attribute = (String) en.nextElement();
				if (model.containsKey(attribute) && !this.allowRequestOverride) {
					throw new ServletException("Cannot expose request attribute '" + attribute +
						"' because of an existing model object of the same name");
				}
				Object attributeValue = request.getAttribute(attribute);
				if (logger.isDebugEnabled()) {
					logger.debug("Exposing request attribute '" + attribute +
							"' with value [" + attributeValue + "] to model");
				}
				model.put(attribute, attributeValue);
			}
		}

		if (this.exposeSessionAttributes) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				for (Enumeration en = session.getAttributeNames(); en.hasMoreElements();) {
					String attribute = (String) en.nextElement();
					if (model.containsKey(attribute) && !this.allowSessionOverride) {
						throw new ServletException("Cannot expose session attribute '" + attribute +
							"' because of an existing model object of the same name");
					}
					Object attributeValue = session.getAttribute(attribute);
					if (logger.isDebugEnabled()) {
						logger.debug("Exposing session attribute '" + attribute +
								"' with value [" + attributeValue + "] to model");
					}
					model.put(attribute, attributeValue);
				}
			}
		}

		if (this.exposeSpringMacroHelpers) {
			if (model.containsKey(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE)) {
				throw new ServletException(
						"Cannot expose bind macro helper '" + SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE +
						"' because of an existing model object of the same name");
			}
			// expose RequestContext instance for Spring macros
			model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, new RequestContext(request, model));
		}

		renderMergedTemplateModel(model, request, response);
	}

	/**
	 * Subclasses must implement this method to actually render the view.
	 * @param model combined output Map, with request attributes and
	 * session attributes merged into it if required
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception if rendering failed
	 */
	protected abstract void renderMergedTemplateModel(
			Map model, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
