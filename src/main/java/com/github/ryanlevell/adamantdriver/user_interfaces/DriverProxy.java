package com.github.ryanlevell.adamantdriver.user_interfaces;

import net.lightbody.bmp.BrowserMobProxy;

/**
 * Allows users to set additional proxy capabilities - interceptors, headers,
 * etc.
 * <p>
 * 
 * The class that implements this interface must be set via testng.xml or
 * command line with key <b>PROXY_CLASS</b>.
 * 
 * @author ryan
 *
 */
public interface DriverProxy {

	/**
	 * Allow custom proxy methods to be called.
	 * 
	 * @param proxy
	 *            The proxy object that will be used in the test.
	 */
	void getProxy(BrowserMobProxy proxy);
}
