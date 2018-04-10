package com.oryx.context;

import java.util.UUID;

public class IServer {
    public static String serverName = "Oryx";
    public static String serverhost = "localhost";
    public static String serverport = "8680";

    public static String getBaseUrl() {
        return "http://" + serverhost + ":" + serverport + "/oryx-server/";
    }

    public static String BASE_URL = IServer.getBaseUrl();
    //authentification
    private static String LOGIN_URL = IServer.BASE_URL + "login";
    private static String LOGOUT_URL = IServer.BASE_URL + "logout";
    private static String SIGNIN_URL = IServer.BASE_URL + "signin";

    //products
    public static String PRODUCT_BASE_URL = IServer.BASE_URL + "protected/products";
    public static String PRODUCT_DESCRIPTION_URL = IServer.PRODUCT_BASE_URL + "/description";
}