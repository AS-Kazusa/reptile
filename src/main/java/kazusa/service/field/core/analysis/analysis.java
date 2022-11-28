package kazusa.service.field.core.analysis;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class analysis {

    /**
     * 存放资源uri
     */
    public static Map<Integer,String> map = new HashMap<>(48);

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

        public void getUri(EdgeDriver edgeDriver, String attribute) throws IOException {
            // 获取注册驱动对象访问指定页面并根据xPath语法定位标签
            WebElement webElement = edgeDriver.findElement(By.xpath(Xpath));
            // 根据属性获取资源超链接
            String s = webElement.getAttribute(attribute);
            map.put(0,s);
        }
    }
}
