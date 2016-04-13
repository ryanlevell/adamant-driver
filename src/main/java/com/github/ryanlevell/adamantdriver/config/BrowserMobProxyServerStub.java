package com.github.ryanlevell.adamantdriver.config;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.MitmManager;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.BlacklistEntry;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.proxy.auth.AuthType;
import net.lightbody.bmp.proxy.dns.AdvancedHostResolver;

// TODO add into data provider injection
// TODO add WebDriverStub as well instead of AdamantDriver?

public class BrowserMobProxyServerStub implements BrowserMobProxy {

	public void start() {
	}

	public void start(int paramInt) {
	}

	public void start(int paramInt, InetAddress paramInetAddress) {
	}

	public void start(int paramInt, InetAddress paramInetAddress1, InetAddress paramInetAddress2) {
	}

	public boolean isStarted() {
		return false;
	}

	public void stop() {
	}

	public void abort() {
	}

	public InetAddress getClientBindAddress() {
		return null;
	}

	public int getPort() {
		return 0;
	}

	public InetAddress getServerBindAddress() {
		return null;
	}

	public Har getHar() {
		return null;
	}

	public Har newHar() {
		return null;
	}

	public Har newHar(String paramString) {
		return null;
	}

	public Har newHar(String paramString1, String paramString2) {
		return null;
	}

	public void setHarCaptureTypes(Set<CaptureType> paramSet) {
	}

	public void setHarCaptureTypes(CaptureType... paramVarArgs) {
	}

	public EnumSet<CaptureType> getHarCaptureTypes() {
		return null;
	}

	public void enableHarCaptureTypes(Set<CaptureType> paramSet) {
	}

	public void enableHarCaptureTypes(CaptureType... paramVarArgs) {
	}

	public void disableHarCaptureTypes(Set<CaptureType> paramSet) {
	}

	public void disableHarCaptureTypes(CaptureType... paramVarArgs) {
	}

	public Har newPage() {
		return null;
	}

	public Har newPage(String paramString) {
		return null;
	}

	public Har newPage(String paramString1, String paramString2) {
		return null;
	}

	public Har endHar() {
		return null;
	}

	public void setReadBandwidthLimit(long paramLong) {
	}

	public void setWriteBandwidthLimit(long paramLong) {
	}

	public void setLatency(long paramLong, TimeUnit paramTimeUnit) {
	}

	public void setConnectTimeout(int paramInt, TimeUnit paramTimeUnit) {
	}

	public void setIdleConnectionTimeout(int paramInt, TimeUnit paramTimeUnit) {
	}

	public void setRequestTimeout(int paramInt, TimeUnit paramTimeUnit) {
	}

	public void autoAuthorization(String paramString1, String paramString2, String paramString3,
			AuthType paramAuthType) {
	}

	public void stopAutoAuthorization(String paramString) {
	}

	public void rewriteUrl(String paramString1, String paramString2) {
	}

	public void rewriteUrls(Map<String, String> paramMap) {
	}

	public Map<String, String> getRewriteRules() {
		return null;
	}

	public void removeRewriteRule(String paramString) {
	}

	public void clearRewriteRules() {
	}

	public void blacklistRequests(String paramString, int paramInt) {
	}

	public void blacklistRequests(String paramString1, int paramInt, String paramString2) {
	}

	public void setBlacklist(Collection<BlacklistEntry> paramCollection) {
	}

	public Collection<BlacklistEntry> getBlacklist() {
		return null;
	}

	public void clearBlacklist() {
	}

	public void whitelistRequests(Collection<String> paramCollection, int paramInt) {
	}

	public void addWhitelistPattern(String paramString) {
	}

	public void enableEmptyWhitelist(int paramInt) {
	}

	public void disableWhitelist() {
	}

	public Collection<String> getWhitelistUrls() {
		return null;
	}

	public int getWhitelistStatusCode() {
		return 0;
	}

	public boolean isWhitelistEnabled() {
		return false;
	}

	public void addHeaders(Map<String, String> paramMap) {
	}

	public void addHeader(String paramString1, String paramString2) {
	}

	public void removeHeader(String paramString) {
	}

	public void removeAllHeaders() {
	}

	public Map<String, String> getAllHeaders() {
		return null;
	}

	public void setHostNameResolver(AdvancedHostResolver paramAdvancedHostResolver) {
	}

	public AdvancedHostResolver getHostNameResolver() {
		return null;
	}

	public boolean waitForQuiescence(long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
		return false;
	}

	public void setChainedProxy(InetSocketAddress paramInetSocketAddress) {
	}

	public InetSocketAddress getChainedProxy() {
		return null;
	}

	public void addFirstHttpFilterFactory(HttpFiltersSource paramHttpFiltersSource) {
	}

	public void addLastHttpFilterFactory(HttpFiltersSource paramHttpFiltersSource) {
	}

	public void addResponseFilter(ResponseFilter paramResponseFilter) {
	}

	public void addRequestFilter(RequestFilter paramRequestFilter) {
	}

	public void setMitmDisabled(boolean paramBoolean) {
	}

	public void setMitmManager(MitmManager paramMitmManager) {
	}

	public void setTrustAllServers(boolean paramBoolean) {
	}
}
