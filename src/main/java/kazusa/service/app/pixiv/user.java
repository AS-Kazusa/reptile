package kazusa.service.app.pixiv;

import kazusa.infrastructure.Warehouse.model.http;
import kazusa.infrastructure.Warehouse.model.img;
import kazusa.infrastructure.Warehouse.model.log;
import kazusa.service.app.reptile;
import kazusa.service.field.brace.JdkHttpclient;
import kazusa.service.field.brace.downloader;
import kazusa.service.field.core.analysis.pixiv.pixiv;
import org.apache.commons.lang3.StringUtils;

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
import static kazusa.service.field.core.analysis.pixiv.pixiv.map;
import static kazusa.service.field.core.analysis.pixiv.pixiv.types;
import static kazusa.service.field.utils.utils.ramo;

// https://www.pixiv.net/ajax/user/22695263/illusts/bookmarks?tag=&offset=0&limit=48&rest=hide&lang=zh
public class user implements reptile {

    http http = getHttp();

    JdkHttpclient<String> stringJdkHttpclient = new JdkHttpclient<>();

    pixiv pixivAnalysis = new pixiv();

    JdkHttpclient<byte[]> httpclient = new JdkHttpclient<>();

    downloader downloader = new downloader();

    /**
     * 时间格式化
     */
    SimpleDateFormat data = new SimpleDateFormat("yyyy年MM月dd日E H小时mm分ss秒");

    /**
     * 日志
     */
    List<String> logs = new ArrayList<>();

    /**
     * 用户id
     */
    String uid = "22695263";

    /**
     * show爬取公开收藏
     * hide爬取不公开收藏
     */
    String uriRestParameter = "show";

    /**
     * json显示起始值
     */
    Long size = 0L;

    /**
     * 页数
     */
    long  pages = 7;

    /**
     * json显示imgUri数
     */
    Long length = 6 * 8 * (pages - 1);

    /**
     * 图片名去重
     */
    Long l = 0L;

    /**
     * 账户收藏图片爬取
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws URISyntaxException
     * @throws NoSuchProviderException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws KeyManagementException
     */
    public void reptile() throws Exception {
        // 开始计时
        long time = new Date().getTime();
        // 配置信息
        config();
        getJson();
        System.out.println("爬取完成,请确认:" + (size - 48) + "\t耗时:" + ((new Date().getTime() - time) / 1000) + "s");
        System.out.println("日志记录爬取失败uri:" + logs.size());
        for (String log : logs) {
            System.out.println(log);
        }
    }

    public void getJson() throws Exception {
        // 获取所有json
        while (size <= length) {
            String jsonUri = "https://www.pixiv.net/ajax/user/"+ uid + "/illusts/bookmarks?tag=&offset=" + size + "&limit=48&rest=" + uriRestParameter + "&lang=zh";
            System.out.println(jsonUri);
            // 配置uri
            http.setUri(jsonUri);

            // 获取json
            HttpResponse<String> json = stringJdkHttpclient.getHttpclient(HttpResponse.BodyHandlers.ofString());

            // 解析json下所有imgUri
            pixivAnalysis.getUri(json.body());
            Thread.sleep(5000);
            getImgUri();
            // 单页显示最大图片数
            size += 48;
        }
    }

    public void getImgUri() throws Exception {
        // 获取所有imgUri
        for (int j = 0; j < map.size(); j++) {
            img img = map.get(j);
            String imgUri = img.getUri();

            // imgUri下图片数
            int k = 0;
            // 图片类型
            int type = 0;
            // 保存该imgUri下资源正确类型
            int temp = 0;
            HttpResponse<byte[]> httpResponse = null;
            do {
                do {
                    // 判断不为该imgUri下第一张图片
                    if(k >= 1) {
                        // 获取正确类型
                        type = temp;
                        // 判断是否为该imgUri下第一张图片的二次操作
                        if (httpResponse.statusCode() != 200) {
                            break;
                        }
                    }

                    // 配置imgUri
                    http.setUri(imgUri + k + types.get(type++));
                    httpResponse = httpclient.getHttpclient(HttpResponse.BodyHandlers.ofByteArray());

                    Thread.sleep(ramo());
                } while (httpResponse.statusCode() != 200 && type < types.size());

                // 日志记录
                if (k == 0 && httpResponse.statusCode() != 200) {
                    logs.add(new log(data.format(new Date()),httpResponse.statusCode(),imgUri + k).toString());
                }

                if (httpResponse.statusCode() == 200) {
                    type--;
                    // 保存正确资源类型
                    temp = type;
                    System.out.println(imgUri + k + types.get(type));
                    // 防重名
                    if (StringUtils.isBlank(img.getName()) || k != 0) {
                        downloader.downloader(httpResponse.body(),l + "");
                    } else {
                        downloader.downloader(httpResponse.body(),img.getName());
                    }
                    l++;
                }
                k++;
                // 判断该imgUri下无资源退出,这里值变为取反没搞懂
            } while (httpResponse.statusCode() == 200);
        }
    }
}