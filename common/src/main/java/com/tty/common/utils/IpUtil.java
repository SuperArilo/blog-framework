package com.tty.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {
    private static final Logger logger = Logger.getLogger(IpUtil.class);

    public static String getIpAdder(HttpServletRequest request) {
        String ipAddress = getHeaderIp(request);
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = getLocalIp(request);
        }
        return formatIp(ipAddress);
    }

    private static String getHeaderIp(HttpServletRequest request) {
        String[] headers = {
                "x-forwarded-for",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return null;
    }

    private static String getLocalIp(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if ("127.0.0.1".equals(ipAddress)) {
            try {
                InetAddress inet = InetAddress.getLocalHost();
                ipAddress = inet.getHostAddress();
            } catch (UnknownHostException e) {
                logger.error("Failed to get local IP address", e);
            }
        }
        return ipAddress;
    }

    private static String formatIp(String ipAddress) {
        if (ipAddress != null && ipAddress.length() > 15 && ipAddress.contains(",")) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        if (ipAddress != null && ipAddress.contains(":")) { // Check for IPv6 address
            ipAddress = ipAddress.replace(":", "-");
        }
        return ipAddress;
    }
}
