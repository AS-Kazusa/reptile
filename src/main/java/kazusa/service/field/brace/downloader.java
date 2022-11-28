package kazusa.service.field.brace;

import kazusa.infrastructure.Warehouse.model.down;
import kazusa.infrastructure.Warehouse.model.http;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

import static kazusa.infrastructure.Warehouse.model.down.getDown;
import static kazusa.infrastructure.config.config.config;

/**
 * 资源下载器
 */
@Slf4j
public class downloader {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException, NoSuchProviderException, ExecutionException, InterruptedException, KeyManagementException {
        config();
        http.getHttp().setUri("");
        JdkHttpclient<byte[]> httpclient = new JdkHttpclient<>();
        HttpResponse<byte[]> httpResponse = httpclient.getHttpclient(HttpResponse.BodyHandlers.ofByteArray());
        new downloader().downloader(httpResponse.body(),"");
    }

    down down = getDown();

    public void downloader(byte[] bytes, String imgName) throws IOException {
        if (bytes != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            FileUtils.copyInputStreamToFile(byteArrayInputStream,new File(down.getPath() + imgName + "." + down.getType()));
            log.info("下载:" + imgName);
        }
    }
}