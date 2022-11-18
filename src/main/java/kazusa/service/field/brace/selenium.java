package kazusa.service.field.brace;

import org.openqa.selenium.edge.EdgeDriver;

public class selenium {

    public static void main(String[] args) {
        EdgeDriver edgeDriver = selenium.edgeDriver("uri");
    }

    /**
     * 驱动软件路径
     */
    public static String seleniumDrivePath;

    public static EdgeDriver edgeDriver(String uri) {
        // 读取加载驱动
        System.setProperty("webdriver.edge.driver",seleniumDrivePath);
        // 打开浏览器:开启Driver会话
        EdgeDriver edgeDriver = new EdgeDriver();
        // 指定访问网站
        edgeDriver.get(uri);
        // 获取Selenium打开的页面源代码
        String pageSource = edgeDriver.getPageSource();
        System.out.println(pageSource);
        return edgeDriver;
    }
}