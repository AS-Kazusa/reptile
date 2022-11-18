package kazusa.service.field.core;


import kazusa.infrastructure.Warehouse.model.http;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
 */
public class analysis {

    //String getUri = "[a-zA-z]+://(?!www.)\\S.[^\u4e00-\u9fa5<'\"]*";

    /**
     * 存放资源uri
     */
    public static Map<Integer,String> map = new HashMap<>(48);

    /**
     * 存放资源类型
     */
    public static List types;

    /**
     * 正则处理
     */
    public static class regular {

        public void getUri(String json) {
            int i = 0;

            json = json.replace("\\", "/");
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
                    map.put(i,imgUri);
                    i++;
                }
            }
        }
    }

    /**
     * Jsoup框架作为html解析器解析页面源代码获取链接资源
     */
    public static class jsoup {

        public static void main(String[] args) throws IOException, InterruptedException {
            Connection.Response response = new jsoup().getUri("");
            byte[] bytes = response.bodyAsBytes();
        }

        /**
         * @return 返回uri资源链接
         * @throws IOException
         * @throws InterruptedException
         */
        public Connection.Response getUri(String s) throws IOException, InterruptedException {
            Document document = Jsoup.parse(s);
            Element element = document.body();
            Elements elements = document.getAllElements();
            Element elementKey = null;
            for (int i = 0; i < elements.size(); i++) {
                // 从封装多条标签对象获取为一条标签封装对象
                elementKey = elements.get(i);
            }
            return Jsoup
                    // 根据该标签属性key获取value资源链接
                    .connect(elementKey.attr("标签key"))
                    .ignoreContentType(true)
                    .execute();
        }
    }

    /**
     * Selenium处理
     */
    public static class selenium {

        public static String Xpath;

        public void getUri(EdgeDriver edgeDriver,String attribute) throws IOException {
            // 获取注册驱动对象访问指定页面并根据xPath语法定位标签
            WebElement webElement = edgeDriver.findElement(By.xpath(Xpath));
            // 根据属性获取资源超链接
            String s = webElement.getAttribute(attribute);
            map.put(0,s);
        }
    }
}