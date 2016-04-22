package com.github.ryanlevell.adamantdriver.interfaces;

import com.github.ryanlevell.adamantdriver.user_interfaces.DriverProxy;

import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class TestProxy implements DriverProxy {

	public void getProxy(BrowserMobProxy proxy) {
		proxy.addResponseFilter(new ResponseFilter() {
			public void filterResponse(HttpResponse response, HttpMessageContents contents,
					HttpMessageInfo messageInfo) {
				contents.setTextContents(contents.getTextContents() + "Body replaced by proxy!");
			}
		});
	}
}
