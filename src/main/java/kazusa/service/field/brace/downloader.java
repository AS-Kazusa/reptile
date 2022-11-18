package kazusa.service.field.brace;

import kazusa.infrastructure.Warehouse.model.down;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static kazusa.infrastructure.Warehouse.model.down.getDown;

/**
 * 资源下载器
 */

@Slf4j
public class downloader {

    public static void main(String[] args) {
        Double d = 0.00;
        d++;
        System.out.println(1.00 / 100);
        System.out.println(d);
    }

    down down = getDown();

    public static List<String> logs = new ArrayList<>();

    public void downloader(byte[] bytes, Long i) throws IOException {
        if (bytes != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            FileUtils.copyInputStreamToFile(byteArrayInputStream,new File(down.getPath() + i + "." + down.getType()));
            log.info("下载:" + i);
            return;
        }
        logs.add("第" + i + "图片爬取失败");
    }
}