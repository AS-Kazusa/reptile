package kazusa.infrastructure.config;

import kazusa.infrastructure.Warehouse.model.down;
import kazusa.infrastructure.Warehouse.model.header;
import kazusa.infrastructure.Warehouse.model.http;
import kazusa.service.field.brace.selenium;
import kazusa.service.field.core.analysis;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static kazusa.infrastructure.Warehouse.model.down.getDown;
import static kazusa.infrastructure.Warehouse.model.http.getHttp;
import static kazusa.service.field.brace.JdkHttpclient.list;
import static kazusa.service.field.core.analysis.types;

public class config {

    public static void config() throws IOException {
        File file = new File("." + "/src/main/resources/config.yml");
        String path = file.getCanonicalPath();
        Yaml yaml = new Yaml();
        HashMap<String,Object> hashMap = yaml.load(new FileInputStream(path));

        HashMap<String,Object> httpConfigHashMap = (HashMap) hashMap.get("httpConfig");
        HttpConfig(httpConfigHashMap);

        HashMap<String,Object> analysisConfigHashMap = (HashMap) hashMap.get("analysisConfig");
        analysisConfig(analysisConfigHashMap);

        HashMap<String,Object> downloaderConfigHashMap = (HashMap) hashMap.get("downloaderConfig");
        DownloadConfig(downloaderConfigHashMap);
    }

    /**
     * http配置
     */
    private static void HttpConfig(HashMap<String,Object> hashMap) {
        http http = getHttp();
        http.setAgentIF((boolean) hashMap.get("agentIF"));
        http.setProxyIP((String) hashMap.get("proxyIP"));
        http.setPort((int) hashMap.get("port"));
        http.setAsynchronous((boolean) hashMap.get("asynchronousIF"));
        list.add(new header((boolean) hashMap.get("cookieIF"),"Cookie",(String) hashMap.get("cookie")));
        list.add(new header((boolean) hashMap.get("refererIF"),"referer",(String) hashMap.get("referer")));
        list.add(new header((boolean) hashMap.get("userAgentIF"),"User-Agent",(String) hashMap.get("userAgent")));
    }

    /**
     * selenium配置
     */
    private static void analysisConfig(HashMap<String,Object> hashMap) {
        types = (List) hashMap.get("type");
        HashMap<String,Object> seleniumConfig = (HashMap) hashMap.get("seleniumConfig");
        selenium.seleniumDrivePath = (String) seleniumConfig.get("seleniumDrivePath");
        analysis.selenium.Xpath = (String) seleniumConfig.get("Xpath");
    }

    /**
     * 下载器配置
     */
    private static void DownloadConfig(HashMap<String,Object> hashMap) {
        down down = getDown();
        down.setPath((String) hashMap.get("path"));
        down.setType((String) hashMap.get("type"));
    }
}