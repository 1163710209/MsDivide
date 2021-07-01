package cn.hit.joker.newmsdivide.functionDecomposition;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.ReadFile;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.classImporter.Deploy;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.utils.WriteFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author joker
 * @version 1.0
 * @date 2021/7/1 20:32
 * @description 在健康养老案例上使用 功能分解 划分得到的微服务方案
 */
public class HealthCareDivideResult {
    @Test
    public void analyzeDivideResult() {
        String path = "cases/healthPension/functional decomposition/divideResult.json";
        String input;
        List<Microservice> msList = new ArrayList<>();
        // get msList
        try {
            input = ReadFile.readFromJsonFile(path);
//            System.out.println(input);
            JSONObject msJson = JSON.parseObject(input);
            msJson.entrySet().forEach(entry -> {
                String msName = entry.getKey();
                List<UmlClass> classList = new ArrayList<>();
                JSONArray classArray = (JSONArray) entry.getValue();
                for (int i = 0; i < classArray.size(); i++) {
                    classList.add(new UmlClass(classArray.getString(i)));
                }
                msList.add(new Microservice(msName, classList));
            });
            System.out.println(msList);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        InputData inputData = MainSystem.getInputData();
        ClassDiagram classDiagram = inputData.getClassDiagram();
        boolean suit = true;
        for (Microservice microservice : msList) {
            Set<Deploy.Location> deploySet = MainSystem.checkDeployLocation(microservice, classDiagram.getClassList());
            if (deploySet.size() > 0) {
                microservice.setDeployLocationSet(deploySet);
            } else {
                suit = false;
            }
        }

        if (!suit) {
            System.out.println("当前划分不满足部署位置约束！");
        } else {
            System.out.println("当前划分满足部署位置约束！");
        }

        MicroserviceAnalyzer.addAllToMs(msList, inputData);

        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, inputData.getClassDiagram());
        double communicatePrice = 0;
        if (suit) {
            communicatePrice = MicroserviceAnalyzer.getCommunicatePrice(msList, inputData.getSequenceDiagramList());
        }
        double[] value = MicroserviceAnalyzer.getAverageValueSupport(msList);

        StringBuilder builder = new StringBuilder();
        builder.append("------------------------------\n")
                .append("微服务划分方案为：\n")
                .append("微服务的数量为：" + msList.size() + "\n")
                .append("微服务为：" + msList + "\n")
                .append("聚合度为：" + cohesionDegree + "\n")
                .append("耦合度为：" + coupingDegree + "\n")
                .append("是否符合部署位置约束：" + suit + "\n")
                .append("通讯代价为：" + communicatePrice + "\n")
                .append("平均每个微服务支持的质量指标数：" + value[0] + "\n")
                .append("平均每个质量指标关联的微服务数：" + value[1] + "\n")
                .append("-------------------------------------------\n");

        String outPath = "src/main/resources/cases/healthPension/functional decomposition";
        String fileName = "functionDecomposition-" + msList.size() + ".txt";
        WriteFile.writeToFile(outPath, builder.toString(), fileName);
    }
}
