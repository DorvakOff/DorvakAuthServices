package com.dorvak.das.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {

    private static final String[] IP_HEADER_CANDIDATES = {"X-Real-IP", "X-Forwarded-For"};

    public static String getIp(HttpServletRequest request) {

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }

        return request.getRemoteAddr();
    }
}
