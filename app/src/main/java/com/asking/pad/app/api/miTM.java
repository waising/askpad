package com.asking.pad.app.api;

/**
 * Created by jswang on 2017/7/21.
 */

public class miTM implements javax.net.ssl.TrustManager,
        javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public boolean isServerTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }
    public boolean isClientTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }
    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}
