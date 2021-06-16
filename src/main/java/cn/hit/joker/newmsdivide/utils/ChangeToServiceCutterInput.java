package cn.hit.joker.newmsdivide.utils;

import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassRelation;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/15 20:28
 * @description change input json to service cutter can accept json
 */
public class ChangeToServiceCutterInput {
    /**
     * change classDiagram to json which service cutter accepted
     *
     * @param classDiagram
     * @return
     */
    public static String Change(ClassDiagram classDiagram) {
        JSONObject json = new JSONObject();
        json.put("name", classDiagram.getName());

        // handle class list
        JSONArray classArray = new JSONArray();
        classDiagram.getClassList().forEach(umlClass -> {
            JSONObject classJson = new JSONObject();
            // handle class name
            classJson.put("name", umlClass.getName());
            // handle class attributes
            JSONArray attributes = new JSONArray();
            attributes.addAll(umlClass.getAttributes());
            if (attributes.size() == 0) {
                attributes.add(umlClass.getName());
            }
            classJson.put("nanoentities", attributes);
            // add to classArray
            classArray.add(classJson);
        });
        json.put("entities", classArray);

        // handle class relation
        JSONArray relations = new JSONArray();
        classDiagram.getClassRelationList().forEach(classRelation -> {
            JSONObject relationJson = new JSONObject();
            relationJson.put("origin", classRelation.getOrigin().getName());
            relationJson.put("destination", classRelation.getDestination().getName());
            String relationType = changeRelationName(classRelation.getType());
            if (relationType != null) {
                relationJson.put("type", relationType);
                relations.add(relationJson);
            }
        });
        json.put("relations", relations);

        return json.toJSONString();
    }

    private static String changeRelationName(ClassRelation.RelationType relationType) {
        switch (relationType) {
            case ClassAggregate:
                return "AGGREGATION";
            case ClassComposition:
            case ClassAssociation:
                return "COMPOSITION";
            case ClassInheritance:
                return "INHERITANCE";
            default:
                return null;
        }
    }
}
