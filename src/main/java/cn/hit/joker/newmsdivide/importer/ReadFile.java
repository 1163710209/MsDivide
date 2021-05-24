
package cn.hit.joker.newmsdivide.importer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * read json file
 */
@Slf4j
public class ReadFile {
//    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);

    /**
     * 读取json文件得到json字符串
     *
     * @param filePath 文件路径
     * @return json字符串
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String readFromJsonFile(String filePath) throws IOException, URISyntaxException {

        if (filePath==null||filePath.length()==0) {
            log.error("文件路径不能为空");
            throw new NullPointerException("文件路径不能为空！");
        }

        // 获取资源的绝对路径的url
        URL url = ReadFile.class.getClassLoader().getResource(filePath);

        if (url == null) {
            log.error("{} 路径错误，或路径指向的文件不存在", filePath);
            throw new NullPointerException("文件路径错误或未找到此文件");
        }

        // 将url转为uri再转为path
        Path resPath = Paths.get(url.toURI());
        String fileString = new String(Files.readAllBytes(resPath), StandardCharsets.UTF_8);

        log.info("路径为 {} 的文件读取成功", filePath);
        return fileString;
    }

}
