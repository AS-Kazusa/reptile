package kazusa.service.app.pixiv;

import kazusa.infrastructure.Warehouse.model.down;
import kazusa.infrastructure.Warehouse.model.http;
import kazusa.service.app.reptile;
import kazusa.service.field.brace.JdkHttpclient;
import kazusa.service.field.brace.downloader;

import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String s = json.body();

        s = s.replace("\\", "/");

        String getUri = "https:////i.pximg.net//img-zip-ugoira//img//[0-9/]*_ugoira1920x1080.zip";
        // 传入正则规则
        Pattern pattern = Pattern.compile(getUri);
        // 获取匹配字符
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            imgUri = matcher.group(0);
            Pattern pattern_ = Pattern.compile("[0-9/]*_");
            Matcher matcher_ = pattern_.matcher(imgUri);
            while (matcher_.find()) {
                imgUri = matcher_.group(0);
            }
        }
        imgUri = "https://i.pximg.net/img-zip-ugoira/img" + imgUri + "ugoira1920x1080.zip";
        System.out.println(imgUri);
        http.setUri(imgUri);
        HttpResponse<byte[]> httpResponse = httpclient.getHttpclient(HttpResponse.BodyHandlers.ofByteArray());
        down.setType("zip");
        downloader.downloader(httpResponse.body(),0L);
    }
}