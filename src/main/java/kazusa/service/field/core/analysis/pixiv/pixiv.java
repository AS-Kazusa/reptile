package kazusa.service.field.core.analysis.pixiv;


import kazusa.infrastructure.Warehouse.model.img;
import kazusa.service.field.brace.JdkHttpclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.infrastructure.config.config.config;

/**
 * 页面解析
 * String getUri = "[a-zA-z]+://(?!www.)\\S.[^\u4e00-\u9fa5<'\"]*";
 */
public class pixiv {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException, NoSuchProviderException, ExecutionException, InterruptedException, KeyManagementException {
        String uri = "";
        config();
        getHttp().setUri(uri);
        HttpResponse<String> json = new JdkHttpclient<String>().getHttpclient(HttpResponse.BodyHandlers.ofString());
        new pixiv().getImgName(json.body());
    }

    /**
     * 正则处理
     */
    public void getUri(String json) {
        json = json.replace("\\", "/");
        getImgName(json);
        getImg(json,0);
    }

    /**
     * 存放资源名字
     */
    List<String> imgNames = new ArrayList<>();

    public void getImgName(String json) {
        String name = "title\":\"[\\[\\] 0-9a-zA-Z\\\\]*";
        // 传入正则规则
        Pattern pattern = Pattern.compile(name);
        // 获取匹配字符
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            imgNames.add(unicodeToString(matcher.group(0).substring(8)));
        }
    }

    /**
     * 存放资源
     */
    public static Map<Integer, img> map = new HashMap<>(48);

    /**
     * 存放资源类型
     */
    public static List types;

    public void getImg(String json,int i) {
        String getUri = "https:////i.pximg.net//c//250x250_80_a2//[\\w-]*//img//[0-9/a-zA-Z_]*.(jpg|png)";
        // 传入正则规则
        Pattern pattern = Pattern.compile(getUri);
        // 获取匹配字符
        Matcher matcher = pattern.matcher(json);
        while(matcher.find()) {
            String uri = matcher.group(0);
            String substring = "/img//[0-9/]*";
            // 传入正则规则
            Pattern pattern1 = Pattern.compile(substring);
            // 获取匹配字符
            Matcher matcher1 = pattern1.matcher(uri);
            while (matcher1.find()) {
                String s = matcher1.group(0);
                String imgUri = "https://i.pximg.net/img-original" + s + "_p";
                map.put(i,new img(imgNames.get(i),imgUri));
                i++;
            }
        }
    }

    public String getGif(String json,int i) {
        json = json.replace("\\", "/");

        String getUri = "https:////i.pximg.net//img-zip-ugoira//img//[0-9/]*_ugoira1920x1080.zip";
        // 传入正则规则
        Pattern pattern = Pattern.compile(getUri);
        // 获取匹配字符
        Matcher matcher = pattern.matcher(json);
        String imgUri = "";
        while (matcher.find()) {
            imgUri = matcher.group(0);
            Pattern pattern_ = Pattern.compile("[0-9/]*_");
            Matcher matcher_ = pattern_.matcher(imgUri);
            while (matcher_.find()) {
                String s = matcher_.group(0);
                imgUri = "https://i.pximg.net/img-zip-ugoira/img" + s + "ugoira1920x1080.zip";
            }
        }
        return imgUri;
    }

    /**
     * @param str 传入带有16进制utf-8编码字符的字符串
     * @return 返回16进制utf-8编码字符转为中文字符串
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}