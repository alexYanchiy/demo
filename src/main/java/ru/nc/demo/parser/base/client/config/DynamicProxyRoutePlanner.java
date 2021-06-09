package ru.nc.demo.parser.base.client.config;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import ru.nc.demo.parser.base.client.proxy.Proxy;

public class DynamicProxyRoutePlanner implements HttpRoutePlanner {
    private DefaultProxyRoutePlanner defaultProxyRoutePlanner;

    public DynamicProxyRoutePlanner(HttpHost host){
        defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(host);
    }

    public void setProxy(Proxy proxy) {
        defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(new HttpHost(proxy.getHost(), proxy.getPort()));
    }

    @Override
    public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        return defaultProxyRoutePlanner.determineRoute(target, request, context);
    }

}
