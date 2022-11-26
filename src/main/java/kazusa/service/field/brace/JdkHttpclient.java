package kazusa.service.field.brace;

import kazusa.infrastructure.Warehouse.model.header;
import kazusa.infrastructure.Warehouse.model.http;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.service.field.utils.SSLCertificateIgnored.SslIgnored;

public class JdkHttpclient<T> {

    http http = getHttp();

    /**
     * 配置请求发送对象
     * @return 返回请求发送对象
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public HttpClient httpClientConfig() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        HttpClient.Builder newBuilder = HttpClient.newBuilder();
        // 配置使用http版本
        newBuilder.version(HttpClient.Version.HTTP_2);
        // 配置证书信息为忽略
        newBuilder.sslContext(SslIgnored());
        if (http.isAgentIF()) {
            // IP代理:传入代理IP和端口
            newBuilder.proxy(ProxySelector.of(new InetSocketAddress(http.getProxyIP(), http.getPort())));
        }
        // 配置单线程池
        newBuilder.executor(Executors.newSingleThreadExecutor());
        /*
            配置缓存cookie设置,传入
            cookie管理器(可为null,为null则创建InMemoryCookieStore类的默认cookie管理器)
            与cookie缓存策略(设置为只接受来自原始服务器的cookie)
         */
        newBuilder.cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        // 默认不重定向,设置允许任意重定向,即https也可重定向到http
        newBuilder.followRedirects(HttpClient.Redirect.ALWAYS);
        return newBuilder.build();
    }

    /**
     * 配置请求封装对象
     * @return 返回请求封装对象
     * @throws URISyntaxException
     */
    public HttpRequest.Builder httpRequestConfig() throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(http.getUri()));
    }

    public static List<header> list = new ArrayList<>();

    /**
     * @param httpGet 传入需配置get请求对象
     */
    public HttpRequest getRequestConfig(HttpRequest.Builder httpGet) {
        for (header header : list) {
            // 判断是否添加该请求头信息
            if (header.isHeaderIF()) {
                httpGet.setHeader(header.getHeaderKey(),header.getHeaderValue());
            }
        }
        return httpGet.build();
    }

    /**
     * @param stringBodyHandler 传入响应对象
     * @return 返回响应数据封装对象
     * @throws IOException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     * @throws URISyntaxException
     * @throws ExecutionException
     */
    public HttpResponse<T> getHttpclient(HttpResponse.BodyHandler<T> stringBodyHandler) throws IOException, InterruptedException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, URISyntaxException, ExecutionException {
        HttpClient httpClient = httpClientConfig();
        HttpRequest.Builder builder = httpRequestConfig();
        HttpRequest httpRequest = getRequestConfig(builder.GET());
        // 同步发送请求,传入请求方式,返回类型
        return httpClient.send(httpRequest,stringBodyHandler);
    }
}