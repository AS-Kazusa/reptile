package kazusa.service.field.utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * SSL证书忽略:用于访问使用https协议网站
 */
public class SSLCertificateIgnored {

    public static SSLContext SslIgnored() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("SSL","SunJSSE");
        //X509ExtendedTrustManager：忽略证书验证，忽略主机名检查
        sslcontext.init(null, new TrustManager[]{new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
            }
        }}, new java.security.SecureRandom());
        return sslcontext;
    }
}