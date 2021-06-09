package ru.nc.demo.parser.base.client;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.protocol.HttpContext;

/**
 * Дефолтная имплементация {@link RedirectStrategy} </br>
 * Пропускает только ответы с кодом (200) и (301, 302), при условии, что не превышено максимальное количество редиректов для запроса
 *
 * @author NiggerCat
 */
public class DefaultRedirectStrategy implements RedirectStrategy {

    // Текущее количество редиректов
    private int currentRedirectCount = 0;

    // Максимальное количество редиректов
    private int maxRedirectCount;

    // Склад cookies
    private CookieStore cookieStore;

    /**
     * Конструктор класса DefaultRedirectStrategy
     *
     * @param maxRedirectCount максимальное количество редиректов
     */
    public DefaultRedirectStrategy(int maxRedirectCount, CookieStore cookieStore) {
        this.maxRedirectCount = maxRedirectCount;
        this.cookieStore = cookieStore;
    }

    @Override
    public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {

        int responseCode = httpResponse.getStatusLine().getStatusCode();
        if ((responseCode == 301 || responseCode == 302)
                && currentRedirectCount < maxRedirectCount) {
            this.currentRedirectCount++;
            return true;
        }

        this.currentRedirectCount = 0;
        return false;
    }

    @Override
    public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        String locationURL = "";
        String redirectedURL = null;

        Header locationHeader = httpResponse.getFirstHeader("Location");
        if (locationHeader != null) {
            locationURL = locationHeader.getValue();
        }

        Object hostObject = httpContext.getAttribute("http.target_host");
        if (hostObject == null) {
            redirectedURL = locationURL;
        } else {
            if (locationURL.startsWith("/")) {
                redirectedURL = hostObject.toString() + locationURL;
            } else {
                redirectedURL = locationURL;
            }
        }

        return RequestBuilder.create("GET").setUri(redirectedURL).build();
    }

}
