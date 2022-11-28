package kazusa.service.app.pixiv;

import kazusa.infrastructure.Warehouse.model.down;
import kazusa.infrastructure.Warehouse.model.http;
import kazusa.service.app.reptile;
import kazusa.service.field.brace.JdkHttpclient;
import kazusa.service.field.brace.downloader;
import kazusa.service.field.core.analysis.pixiv.pixiv;

import java.net.http.HttpResponse;

import static kazusa.infrastructure.Warehouse.model.down.getDown;
import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.infrastructure.config.config.config;

/**
 * 动图下载
 */
public class gif implements reptile {

    http http = getHttp();

    JdkHttpclient<String> stringJdkHttpclient = new JdkHttpclient<>();

    JdkHttpclient<byte[]> httpclient = new JdkHttpclient<>();

    down down = getDown();

    downloader downloader = new downloader();

    /**
     * 动图ID
     */
    String imgId = "93503875";

    @Override
    public void reptile() throws Exception {
        config();
        String imgUri = "https://www.pixiv.net/ajax/illust/" + imgId + "/ugoira_meta?lang=zh";
        http.setUri(imgUri);
        HttpResponse<String> json = stringJdkHttpclient.getHttpclient(HttpResponse.BodyHandlers.ofString());
        imgUri = new pixiv().getGif(json.body(), 0);
        System.out.println(imgUri);

        http.setUri(imgUri);
        HttpResponse<byte[]> httpResponse = httpclient.getHttpclient(HttpResponse.BodyHandlers.ofByteArray());
        down.setType("zip");
        downloader.downloader(httpResponse.body(),"gif");
    }
}