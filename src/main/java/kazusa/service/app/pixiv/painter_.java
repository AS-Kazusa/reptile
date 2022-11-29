package kazusa.service.app.pixiv;

import kazusa.infrastructure.Warehouse.model.http;
import kazusa.service.app.reptile;
import kazusa.service.field.brace.JdkHttpclient;
import kazusa.service.field.brace.downloader;
import kazusa.service.field.core.analysis.pixiv.pixiv;

import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.infrastructure.config.config.config;
import static kazusa.service.app.pixiv.gif.logs;
import static kazusa.service.field.core.analysis.pixiv.pixiv.imgIds;

public class painter_ implements reptile {

    http http = getHttp();

    JdkHttpclient<String> stringJdkHttpclient = new JdkHttpclient<>();

    pixiv pixivAnalysis = new pixiv();

    /**
     * 作者id
     */
    String uid = "11246082";

    /**
     * 图片名去重
     */
    Long l = 0L;

    /**
     * 爬取该作者所有动图,目前应该只能爬取id下第一张
     * @throws Exception
     */
    @Override
    public void reptile() throws Exception {
        // 开始计时
        long time = new Date().getTime();

        // 配置信息
        config();
        Thread.sleep(5000);
        getAPI();
        System.out.println("爬取完成,请确认:" + "\t耗时:" + ((new Date().getTime() - time) / 1000) + "s");
        System.out.println("日志记录爬取失败uri:" + logs.size());
        for (String log : logs) {
            System.out.println(log);
        }
    }

    private void getAPI() throws Exception {
        String imgIdUri = "https://www.pixiv.net/ajax/user/" + uid + "/profile/all";
        System.out.println(imgIdUri);

        // 配置uri
        http.setUri(imgIdUri);
        HttpResponse<String> json = stringJdkHttpclient.getHttpclient(HttpResponse.BodyHandlers.ofString());
        // 解析json获取该作者所有作品ID
        pixivAnalysis.getImgID(json.body());
        Thread.sleep(5000);
        gif gif = new gif();
        for (String imgId : imgIds) {
            // 拼接作品ID
            gif.imgId = imgId;
            gif.reptile();
        }
    }
}
