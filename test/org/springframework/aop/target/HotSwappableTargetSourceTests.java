/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package org.springframework.aop.target;

import java.io.InputStream;

import junit.framework.TestCase;

import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.interceptor.SideEffectBean;
import org.springframework.beans.factory.support.ClasspathBeanDefinitionRegistryLocation;
import org.springframework.beans.factory.xml.XmlBeanFactory;

/**
 * 
 * @author Rod Johnson
 * @version $Id: HotSwappableTargetSourceTests.java,v 1.2 2003-12-19 15:49:58 johnsonr Exp $
 */
public class HotSwappableTargetSourceTests extends TestCase {

	/** Initial count value set in bean factory XML */
	private static final int INITIAL_COUNT = 10;

	private XmlBeanFactory beanFactory;
	
	public HotSwappableTargetSourceTests(String s) {
		super(s);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// Load from classpath, NOT a file path
		InputStream is = getClass().getResourceAsStream("hotSwapTests.xml");
		this.beanFactory = new XmlBeanFactory(is, new ClasspathBeanDefinitionRegistryLocation("hotSwapTests.xml"));
	}
	
	/**
	 * We must simulate container shutdown, which should clear
	 * threads
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() {
		// Will call pool.close()
		this.beanFactory.destroySingletons();
	}

	/**
	 * Check it works like a normal invoker
	 *
	 */
	public void testBasicFunctionality() {
		SideEffectBean target1 = (SideEffectBean) beanFactory.getBean("target1");
		SideEffectBean proxied = (SideEffectBean) beanFactory.getBean("swappable");
		assertEquals(INITIAL_COUNT, proxied.getCount() );
		proxied.doWork();
		assertEquals(INITIAL_COUNT + 1, proxied.getCount() );
		
		proxied = (SideEffectBean) beanFactory.getBean("swappable");
		proxied.doWork();
		assertEquals(INITIAL_COUNT + 2, proxied.getCount() );
	}
	
	public void testValidSwaps() {
		SideEffectBean target1 = (SideEffectBean) beanFactory.getBean("target1");
		SideEffectBean target2 = (SideEffectBean) beanFactory.getBean("target2");
		
		SideEffectBean proxied = (SideEffectBean) beanFactory.getBean("swappable");
	//	assertEquals(target1, ((Advised) proxied).getTarget());
		assertEquals(target1.getCount(), proxied.getCount() );
		proxied.doWork();
		assertEquals(INITIAL_COUNT + 1, proxied.getCount() );
	
		HotSwappableTargetSource swapper = (HotSwappableTargetSource) beanFactory.getBean("swapper");
		Object old = swapper.swap(target2);
		assertEquals("Correct old target was returned", target1, old);
		
		// TODO should be able to make this assertion: need to fix target handling
		// in AdvisedSupport
		//assertEquals(target2, ((Advised) proxied).getTarget());
		
		assertEquals(20, proxied.getCount());
		proxied.doWork();
		assertEquals(21, target2.getCount());
		
		// Swap it back
		swapper.swap(target1);
		assertEquals(target1.getCount(), proxied.getCount());
	}
	
	
	/**
	 * 
	 * @param invalid
	 * @return the message
	 */
	private AopConfigException testRejectsSwapToInvalidValue(Object invalid) {
		HotSwappableTargetSource swapper = (HotSwappableTargetSource) beanFactory.getBean("swapper");
		AopConfigException aopex = null;
		try {
			swapper.swap(invalid);
			fail("Shouldn't be able to swap to invalid value [" + invalid + "]");
		}
		catch (AopConfigException ex) {
			// Ok
			aopex = ex;
		}
		
		// It shouldn't be corrupted, it should still work
		testBasicFunctionality();
		return aopex;
	}
	
	public void testRejectsSwapToNull() {
		AopConfigException ex = testRejectsSwapToInvalidValue(null);
		assertTrue(ex.getMessage().indexOf("null") != -1);
	}
	
	// TODO test reject swap to wrong interface or class?
	// how to decide what's valid?
	
	
}
