package ru.nc.demo.parser.base.client;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Имплементация интерфейса {@link CookieStore}
 * с сохранением куков в БД
 *
 * @author NiggerCat
 */
@Component("dbCookieStore")
public class DBCookieStore implements CookieStore {

    // Список куков
    private Vector<Cookie> cookieList = new Vector<>();

    public DBCookieStore() {
    }

    @Override
    public void addCookie(Cookie cookie) {
        boolean wasAdded = false;
        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals(cookie.getName())) {
                cookieList.set(i, cookie);
                wasAdded = true;
            }
        }

        if (!wasAdded) {
            cookieList.add(cookie);
        }

        // todo сделать сохранение в БД
    }

    @Override
    public void clear() {
        cookieList.clear();
    }

    @Override
    public boolean clearExpired(Date date) {
        boolean wasDeleted = false;
        for (int i = cookieList.size() - 1; i >= 0; i--) {
            if (cookieList.get(i).isExpired(date)) {
                cookieList.remove(i);
                wasDeleted = true;
            }
        }

        return wasDeleted;
    }

    @Override
    public List<Cookie> getCookies() {
        clearExpired(new Date(System.currentTimeMillis()));
        return cookieList;
    }

    /**
     * Метод для поиска значения cookie в указанном складе
     *
     * @param cookieStore  склад cookie
     * @param cookieName   имя искомого cookie
     * @return             значение искомого cookie
     */
    public static String getCookieForName(CookieStore cookieStore, String cookieName) {
        String cookieValue = null;
        List<Cookie> cookieList = cookieStore.getCookies();
        for (int cookieIndex = cookieList.size() - 1; cookieIndex >= 0; cookieIndex--) {
            Cookie cookie = cookieList.get(cookieIndex);
            if (cookie.getName().contains(cookieName)) {
                cookieValue = cookie.getValue();
                cookieIndex = -1;
            }
        }

        return cookieValue;
    }

}
