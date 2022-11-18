package kazusa.infrastructure.Warehouse.model;

import lombok.Data;

@Data
public class http {

    private http() {}

    private static http http = new http();

    public static http getHttp() {
        return http;
    }

    /**
     * 请求uri
     */
    private String uri;

    /**
     * 是否开启代理
     */
    private boolean agentIF;

    /**
     * 代理IP
     */
    private String proxyIP;

    /**
     * 代理端口
     */
    private int port;

    /**
     * 设置是否开启异步发送请求
     */
    private boolean asynchronous;
}
