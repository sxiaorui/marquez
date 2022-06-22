package com.xencio.marquez.common;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author songyr
 * @date 2022/06/22
 */
public class XencioHttpUtil {

    private static HttpClient httpClient;

    private XencioHttpUtil() {}

    public static HttpClient createClient() {
        if (httpClient == null) {
            synchronized (XencioHttpUtil.class) {
                if (httpClient == null){
                    httpClient = HttpClients.createDefault();
                }
            }
        }
        return httpClient;
    }
}
