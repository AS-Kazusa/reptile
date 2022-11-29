package kazusa.service.app.pixiv;

import kazusa.infrastructure.Warehouse.model.down;
import kazusa.infrastructure.Warehouse.model.http;
import kazusa.infrastructure.Warehouse.model.log;
import kazusa.service.app.reptile;
import kazusa.service.field.brace.JdkHttpclient;
import kazusa.service.field.brace.downloader;
import kazusa.service.field.core.analysis.pixiv.pixiv;

import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static kazusa.infrastructure.Warehouse.model.down.getDown;
import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.infrastructure.config.config.config;
import static kazusa.service.field.utils.utils.ramo;

/**
 * 动图下载
 */
public class gif implements reptile {

    http http = getHttp();

    JdkHttpclient<String> stringJdkHttpclient = new JdkHttpclient<>();

    JdkHttpclient<byte[]> httpclient = new JdkHttpclient<>();

    pixiv pixivAnalysis = new pixiv();

    down down = getDown();

    downloader downloader = new downloader();

    SimpleDateFormat data = new SimpleDateFormat("yyyy年MM月dd日E H小时mm分ss秒");

    public static List<String> logs = new ArrayList<>();

    /**
     * 动图ID
     */
    public String imgId = "93503875";

    @Override
    public void reptile() throws Exception {
        config();
        String imgUri = "https://www.pixiv.net/ajax/illust/" + imgId + "/ugoira_meta?lang=zh";
        System.out.println(imgUri);

        http.setUri(imgUri);
        HttpResponse<String> json = stringJdkHttpclient.getHttpclient(HttpResponse.BodyHandlers.ofString());
        imgUri = pixivAnalysis.getGif(json.body(), 0);
        Thread.sleep(ramo());
        http.setUri(imgUri);
        HttpResponse<byte[]> httpResponse = httpclient.getHttpclient(HttpResponse.BodyHandlers.ofByteArray());
        if (httpResponse.statusCode() != 200) {
            logs.add(new log(data.format(new Date()),httpResponse.statusCode(),imgUri).toString());
            return;
        }
        down.setType("zip");
        downloader.downloader(httpResponse.body(),"gif");
    }
}