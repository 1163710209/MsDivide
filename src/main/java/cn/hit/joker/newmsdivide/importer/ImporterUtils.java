package cn.hit.joker.newmsdivide.importer;

import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class ImporterUtils {

    /**
     * 按路径读取json文件，反序列化为 T class
     *
     * @param filepath: 文件路径
     * @param type 参数类型
     * @param <T> 泛型类型
     * @return T class
     */
    private static <T> T readJsonToObject(String filepath, final Class<T> type) throws IOException, URISyntaxException {
        // get json string
        String jsonString = ReadFile.readFromJsonFile(filepath);
        ObjectMapper objectMapper = new ObjectMapper();
        T resultClass = objectMapper.readValue(jsonString, type);
        log.info("对象 {} 反序列化成功！", type);
        return resultClass;
    }

    /**
     * import classDiagram
     *
     * @param filepath: 文件路径
     * @return classDiagram
     * @throws IOException io
     * @throws URISyntaxException uri
     */
    public static ClassDiagram importClassDiagram(String filepath) throws IOException, URISyntaxException {
        ClassDiagram classDiagram = readJsonToObject(filepath, ClassDiagram.class);
        log.info("类图 {} 导入成功！", classDiagram.getName());
        return classDiagram;
    }

    /**
     * import classDiagram
     *
     * @param filepath: 文件路径
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static SequenceDiagram importSequenceDiagram(String filepath) throws IOException, URISyntaxException {
        SequenceDiagram sequenceDiagram = readJsonToObject(filepath, SequenceDiagram.class);
        log.info("时序图 {} 导入成功！", sequenceDiagram.getName());
        return sequenceDiagram;
    }

}
