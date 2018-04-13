package ru.sbtqa.tag.pagefactory.web.configure;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;

public class ProxyConfigurator {

    public static Proxy configureProxy() {
        BrowserMobProxy browserMobProxy = new BrowserMobProxyServer();
        browserMobProxy.start(0);
        return ClientUtil.createSeleniumProxy(browserMobProxy);
    }
}
