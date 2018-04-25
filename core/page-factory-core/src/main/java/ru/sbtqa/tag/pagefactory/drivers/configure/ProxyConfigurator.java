package ru.sbtqa.tag.pagefactory.drivers.configure;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;

public class ProxyConfigurator {

    public static Proxy configureProxy() {
        BrowserMobProxy browserMobProxy = new BrowserMobProxyServer();
        browserMobProxy.start(0);
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
        
        return seleniumProxy;
    }
}
