package kazusa.service.app.pixiv;

import kazusa.infrastructure.Warehouse.model.http;
import kazusa.infrastructure.Warehouse.model.log;
import kazusa.service.field.brace.downloader;
import kazusa.service.field.brace.JdkHttpclient;
import kazusa.service.field.core.analysis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.infrastructure.config.config.config;
import static kazusa.service.field.core.analysis.map;
import static kazusa.service.field.core.analysis.types;

public class painter {

    private painter() {}

    static List<String> jsonUris = new ArrayList<>();

    /**
     * 保存所有需要爬取jsonUri
     */
    public static painter setJsonUris() {
        jsonUris.add("");

        return new painter();
    }

    http http = getHttp();

    JdkHttpclient<String> stringJdkHttpclient = new JdkHttpclient<>();

    analysis.regular regular = new analysis.regular();

    JdkHttpclient<byte[]> httpclient = new JdkHttpclient<>();

    downloader downloader = new downloader();

    private int ramo() {
        return (int) (Math.random() * 9);
    }

    SimpleDateFormat data = new SimpleDateFormat("yyyy年MM月dd日E H小时mm分ss秒");

    List<String> logs = new ArrayList<>();

    /**
     * 爬取图片数
     */
    Long i = 0L;

    /**
     * 画师作品爬取
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws URISyntaxException
     * @throws NoSuchProviderException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws KeyManagementException
     */
    public void reptile() throws IOException, NoSuchAlgorithmException, URISyntaxException, NoSuchProviderException, ExecutionException, InterruptedException, KeyManagementException {
        // 开始计时
        long time = new Date().getTime();

        // 配置信息
        config();

        // 获取所有json
        for (String jsonUri : jsonUris) {
            System.out.println(jsonUri);
            // 配置uri
            http.setUri(jsonUri);

            // 获取json
            HttpResponse<String> json = stringJdkHttpclient.getHttpclient(HttpResponse.BodyHandlers.ofString());

            // 解析json下所有imgUri
            regular.getUri(json.body());
            Thread.sleep(8000);

            // 获取所有imgUri
            for (int j = 0; j < map.size(); j++) {
                String imgUri = map.get(j);

                // imgUri下图片数
                int k = 0;
                // 图片类型
                int l = 0;
                // 保存该imgUri下资源正确类型
                int temp = 0;
                HttpResponse<byte[]> httpResponse = null;
                do {
                    do {
                        // 判断不为该imgUri下第一张图片
                        if(k >= 1) {
                            // 获取正确类型
                            l = temp;
                            // 不为该imgUri下第一张图片的二次操作退出
                            if (httpResponse.statusCode() != 200) {
                                break;
                            }
                        }

                        // 配置imgUri
                        http.setUri(imgUri + k + types.get(l++));
                        httpResponse = httpclient.getHttpclient(HttpResponse.BodyHandlers.ofByteArray());

                        Thread.sleep(ramo() * 1000L);
                    } while (httpResponse.statusCode() != 200 && l < types.size());

                    // 日志记录
                    if (k == 0 && httpResponse.statusCode() != 200) {
                        logs.add(new log(data.format(new Date()),httpResponse.statusCode(),imgUri + k).toString());
                    }

                    if (httpResponse.statusCode() == 200) {
                        l--;
                        // 保存正确资源类型
                        temp = l;
                        System.out.println(imgUri + k + types.get(l));
                        downloader.downloader(httpResponse.body(),i);
                        i++;
                    }
                    k++;
                // 判断该imgUri下无资源退出,这里值变为取反原因不明
                } while (httpResponse.statusCode() == 200);
            }
        }
        System.out.println("爬取完成,请确认:" + "\t耗时:" + ((new Date().getTime() - time) / 1000) + "s");
        System.out.println("日志记录爬取失败uri:" + logs.size());
        for (String log : logs) {
            System.out.println(log);
        }
    }
}