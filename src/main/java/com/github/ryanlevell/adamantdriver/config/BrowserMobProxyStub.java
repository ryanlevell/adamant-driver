package com.github.ryanlevell.adamantdriver.config;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.NotImplementedException;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.MitmManager;

import com.github.ryanlevell.adamantdriver.config.AdamantProperties.Prop;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.BlacklistEntry;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.proxy.auth.AuthType;
import net.lightbody.bmp.proxy.dns.AdvancedHostResolver;

// TODO add WebDriverStub as well instead of AdamantDriver?

/**
 * Used to temporarily store a {@link BrowserMobProxy} object in the data
 * provider.
 * 
 * @author ryan
 *
 */
public class BrowserMobProxyStub implements BrowserMobProxy {

	/**
	 * Throws {@link NotImplementedException}.<br>
	 * Possible to erroneously have this implementation in a test if
	 * {@link Prop#USE_INCLUDED_PROXY} is not true<br>
	 * and the test method has injected the {@link BrowserMobProxy} parameter.
	 */
	private static void throwNotImplemented() {
		throw new NotImplementedException(
				"BrowserMobProxy was not initialized. Did you set use_included_proxy to true?");
	}

	public void start() {
		throwNotImplemented();
	}

	public void start(int paramInt) {
		throwNotImplemented();
	}

	public void start(int paramInt, InetAddress paramInetAddress) {
		throwNotImplemented();
	}

	public void start(int paramInt, InetAddress paramInetAddress1, InetAddress paramInetAddress2) {
		throwNotImplemented();
	}

	public boolean isStarted() {
		throwNotImplemented();
		return false;
	}

	public void stop() {
		throwNotImplemented();
	}

	public void abort() {
		throwNotImplemented();
	}

	public InetAddress getClientBindAddress() {
		throwNotImplemented();
		return null;
	}

	public int getPort() {
		throwNotImplemented();
		return 0;
	}

	public InetAddress getServerBindAddress() {
		throwNotImplemented();
		return null;
	}

	public Har getHar() {
		throwNotImplemented();
		return null;
	}

	public Har newHar() {
		throwNotImplemented();
		return null;
	}

	public Har newHar(String paramString) {
		throwNotImplemented();
		return null;
	}

	public Har newHar(String paramString1, String paramString2) {
		throwNotImplemented();
		return null;
	}

	public void setHarCaptureTypes(Set<CaptureType> paramSet) {
		throwNotImplemented();
	}

	public void setHarCaptureTypes(CaptureType... paramVarArgs) {
		throwNotImplemented();
	}

	public EnumSet<CaptureType> getHarCaptureTypes() {
		throwNotImplemented();
		return null;
	}

	public void enableHarCaptureTypes(Set<CaptureType> paramSet) {
		throwNotImplemented();
	}

	public void enableHarCaptureTypes(CaptureType... paramVarArgs) {
		throwNotImplemented();
	}

	public void disableHarCaptureTypes(Set<CaptureType> paramSet) {
		throwNotImplemented();
	}

	public void disableHarCaptureTypes(CaptureType... paramVarArgs) {
		throwNotImplemented();
	}

	public Har newPage() {
		throwNotImplemented();
		return null;
	}

	public Har newPage(String paramString) {
		throwNotImplemented();
		return null;
	}

	public Har newPage(String paramString1, String paramString2) {
		throwNotImplemented();
		return null;
	}

	public Har endHar() {
		throwNotImplemented();
		return null;
	}

	public void setReadBandwidthLimit(long paramLong) {
		throwNotImplemented();
	}

	public void setWriteBandwidthLimit(long paramLong) {
		throwNotImplemented();
	}

	public void setLatency(long paramLong, TimeUnit paramTimeUnit) {
		throwNotImplemented();
	}

	public void setConnectTimeout(int paramInt, TimeUnit paramTimeUnit) {
		throwNotImplemented();
	}

	public void setIdleConnectionTimeout(int paramInt, TimeUnit paramTimeUnit) {
		throwNotImplemented();
	}

	public void setRequestTimeout(int paramInt, TimeUnit paramTimeUnit) {
		throwNotImplemented();
	}

	public void autoAuthorization(String paramString1, String paramString2, String paramString3,
			AuthType paramAuthType) {
		throwNotImplemented();
	}

	public void stopAutoAuthorization(String paramString) {
		throwNotImplemented();
	}

	public void rewriteUrl(String paramString1, String paramString2) {
		throwNotImplemented();
	}

	public void rewriteUrls(Map<String, String> paramMap) {
		throwNotImplemented();
	}

	public Map<String, String> getRewriteRules() {
		throwNotImplemented();
		return null;
	}

	public void removeRewriteRule(String paramString) {
		throwNotImplemented();
	}

	public void clearRewriteRules() {
		throwNotImplemented();
	}

	public void blacklistRequests(String paramString, int paramInt) {
		throwNotImplemented();
	}

	public void blacklistRequests(String paramString1, int paramInt, String paramString2) {
		throwNotImplemented();
	}

	public void setBlacklist(Collection<BlacklistEntry> paramCollection) {
		throwNotImplemented();
	}

	public Collection<BlacklistEntry> getBlacklist() {
		throwNotImplemented();
		return null;
	}

	public void clearBlacklist() {
		throwNotImplemented();
	}

	public void whitelistRequests(Collection<String> paramCollection, int paramInt) {
		throwNotImplemented();
	}

	public void addWhitelistPattern(String paramString) {
		throwNotImplemented();
	}

	public void enableEmptyWhitelist(int paramInt) {
		throwNotImplemented();
	}

	public void disableWhitelist() {
		throwNotImplemented();
	}

	public Collection<String> getWhitelistUrls() {
		throwNotImplemented();
		return null;
	}

	public int getWhitelistStatusCode() {
		throwNotImplemented();
		return 0;
	}

	public boolean isWhitelistEnabled() {
		throwNotImplemented();
		return false;
	}

	public void addHeaders(Map<String, String> paramMap) {
		throwNotImplemented();
	}

	public void addHeader(String paramString1, String paramString2) {
		throwNotImplemented();
	}

	public void removeHeader(String paramString) {
		throwNotImplemented();
	}

	public void removeAllHeaders() {
		throwNotImplemented();
	}

	public Map<String, String> getAllHeaders() {
		throwNotImplemented();
		return null;
	}

	public void setHostNameResolver(AdvancedHostResolver paramAdvancedHostResolver) {
		throwNotImplemented();
	}

	public AdvancedHostResolver getHostNameResolver() {
		throwNotImplemented();
		return null;
	}

	public boolean waitForQuiescence(long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
		throwNotImplemented();
		return false;
	}

	public void setChainedProxy(InetSocketAddress paramInetSocketAddress) {
		throwNotImplemented();
	}

	public InetSocketAddress getChainedProxy() {
		throwNotImplemented();
		return null;
	}

	public void addFirstHttpFilterFactory(HttpFiltersSource paramHttpFiltersSource) {
		throwNotImplemented();
	}

	public void addLastHttpFilterFactory(HttpFiltersSource paramHttpFiltersSource) {
		throwNotImplemented();
	}

	public void addResponseFilter(ResponseFilter paramResponseFilter) {
		throwNotImplemented();
	}

	public void addRequestFilter(RequestFilter paramRequestFilter) {
		throwNotImplemented();
	}

	public void setMitmDisabled(boolean paramBoolean) {
		throwNotImplemented();
	}

	public void setMitmManager(MitmManager paramMitmManager) {
		throwNotImplemented();
	}

	public void setTrustAllServers(boolean paramBoolean) {
		throwNotImplemented();
	}
}
