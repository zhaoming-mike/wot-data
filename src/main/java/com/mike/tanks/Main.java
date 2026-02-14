package com.mike.tanks;

import com.fasterxml.jackson.databind.JsonNode;
import com.mike.tanks.utils.APIUtils;
import com.mike.tanks.utils.CountryCodeConverter;
import com.mike.tanks.utils.FeishuBitableClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Throwable {

        JsonNode vehicles = APIUtils.getAllVehiclesNode("1,2,3,4,5,6,7,8,9,10,11");

        java.util.List<Map<String, Object>> recordsList = new java.util.ArrayList<>();

        for (JsonNode next : vehicles.get("data")) {
            JsonNode tankId = next.get("tank_id");
            JsonNode is_premium = next.get("is_premium");
            String is_premiumStr = cutBoolean(is_premium);
            JsonNode tier = next.get("tier");
            String tierStr = intToRoman(tier);
            JsonNode images = next.get("images");
            JsonNode big_icon = images.get("big_icon");

            String big_icon_url = cut(big_icon);

            JsonNode nation = next.get("nation");
            String nationStr = cut(nation);
            nationStr = CountryCodeConverter.convert(nationStr);
            JsonNode name = next.get("name");
            JsonNode short_name = next.get("short_name");
            String nameStr = cut(name);
            String short_nameStr = cut(short_name);
            JsonNode defaultProfile = next.get("default_profile");
            JsonNode ammo = defaultProfile.get("ammo");
            JsonNode gun = defaultProfile.get("gun");
            JsonNode dispersion = gun.get("dispersion");
            JsonNode move_down_arc = gun.get("move_down_arc");
            JsonNode jsonNode = ammo.get(1);
            if (jsonNode == null) {
                jsonNode = ammo.get(0);
            }
            JsonNode ammoA = jsonNode;
            JsonNode ammoAType = ammoA.get("type");
            JsonNode ammoADamage = ammoA.get("damage");
            JsonNode ammoADamage0 = ammoADamage.get(1);
            System.out.println(tankId + "\t" + nationStr + "\t" + tierStr + "\t" + is_premiumStr + "\t" + ammoADamage0 + "\t" + dispersion + "\t" + move_down_arc + "\t" + short_nameStr + "\t" + big_icon_url);

            //push2WX(tankId, nationStr, tierStr, is_premiumStr, ammoADamage0, dispersion, move_down_arc, short_nameStr, big_icon_url);

            recordsList.add(makeRecord(tankId, nationStr, tierStr, is_premiumStr, ammoADamage0, dispersion, move_down_arc, short_nameStr, big_icon_url));

        }
        push2FS(recordsList);
    }

    private static Map<String, Object> makeRecord(JsonNode tankId, String nationStr, String tierStr, String isPremiumStr, JsonNode ammoADamage0, JsonNode dispersion, JsonNode moveDownArc, String shortNameStr, String bigIconUrl) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("TANK ID", tankId.intValue());
        fields.put("国家", nationStr);
        fields.put("等级", tierStr);
        fields.put("金币车", isPremiumStr);
        fields.put("默认炮火均伤", ammoADamage0.intValue());
        fields.put("百米散布", dispersion.floatValue());
        fields.put("俯角（度°）", moveDownArc.intValue());
        fields.put("坦克名称", shortNameStr);

        Map<String, Object> link = new HashMap<>();
        link.put("text", "点击查看");
        link.put("link", bigIconUrl);
        fields.put("预览", link);
        return fields;
    }

    public static void push2FS(java.util.List<Map<String, Object>> recordsList) {

        // 请替换为您的实际配置
        String appId = "cli_a6cf3875673e900c";
        String appSecret = "5BiHt0nbF6It10jlnneTMha1eaLz2t2Z";

        //多维表格链接中 base 和 ?table 之间的那串字符
        String appToken = "Z3GxbAzWKawbxCsqHp1clPkMnfe";

        //多维表格链接中 ?table 等号后面的那串字符
        String tableId = "tblDS2MBxhAFY4pp";

        //例子：https://microming.feishu.cn/base/Z3GxbAzWKawbxCsqHp1clPkMnfe?table=tblDS2MBxhAFY4pp&view=vewp8CtDzq

        FeishuBitableClient client = new FeishuBitableClient(appId, appSecret, appToken, tableId);

        try {
            /*// 示例1：添加单条记录
            Map<String, Object> fields = new HashMap<>();
            fields.put("TANK ID", 123);
            fields.put("国家", "美国");
            fields.put("等级", "V");
            fields.put("金币车", "是");
            fields.put("默认炮火均伤", 400);
            fields.put("百米散布", .34);
            fields.put("俯角（度°）", 10);
            fields.put("坦克名称", "PTZ-78");

            Map<String, Object> link = new HashMap<>();
            link.put("text", "点击查看");
            link.put("link", "http://api.worldoftanks.asia/static/2.77.0/wot/encyclopedia/vehicle/ussr-R04_T-34.png");
            fields.put("预览", link);

            String recordId = client.addRecord(fields);
            System.out.println("添加记录成功，记录ID: " + recordId);*/

            // 示例2：批量添加多条记录
            java.util.List<String> recordIds = client.batchAddRecords(recordsList);
            System.out.println("批量添加成功，记录ID列表: " + recordIds);

        } catch (IOException e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void push2WX(JsonNode tankId, String nationStr, String tierStr, String is_premiumStr, JsonNode ammoADamage0, JsonNode dispersion, JsonNode move_down_arc, String short_nameStr, String big_icon_url) {
        String json = "{\n" +
                "  \"schema\": {\n" +
                "    \"fFvlRJ\": \"TANK ID\",\n" +
                "    \"f2Ug8c\": \"国家\",\n" +
                "    \"fsSD6d\": \"等级\",\n" +
                "    \"fd3J7k\": \"金币车\",\n" +
                "    \"fRCJUi\": \"默认火炮均伤\",\n" +
                "    \"fy4KZG\": \"百米散布\",\n" +
                "    \"f5VSKa\": \"俯角（度°）\",\n" +
                "    \"fprcbn\": \"坦克名称\",\n" +
                "    \"f1DnGA\": \"预览\"\n" +
                "  },\n" +
                "  \"add_records\": [\n" +
                "    {\n" +
                "      \"values\": {\n" +
                "        \"fFvlRJ\": " + tankId + ",\n" +
                "        \"f2Ug8c\": \"" + nationStr + "\",\n" +
                "        \"fsSD6d\": \"" + tierStr + "\",\n" +
                "        \"fd3J7k\": \"" + is_premiumStr + "\",\n" +
                "        \"fRCJUi\": " + ammoADamage0 + ",\n" +
                "        \"fy4KZG\": " + dispersion + ",\n" +
                "        \"f5VSKa\": " + move_down_arc + ",\n" +
                "        \"fprcbn\": \"" + short_nameStr + "\",\n" +
                "        \"f1DnGA\": [{\"link\": \"" + big_icon_url + "\", \"text\": \"点击查看\"}]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        //速度太慢，不要用这个方法更新（单条记录可以使用）
        //APIUtils.pushData2WX(json);
    }

    @NotNull
    private static String cut(JsonNode node) {
        String text = node.toString();
        text = text.replaceAll("\"", "");
        return text;
    }

    @NotNull
    private static String cutBoolean(JsonNode node) {
        String text = node.toString();
        text = text.replaceAll("\"", "");
        return "true".equalsIgnoreCase(text) ? "是" : "否";
    }

    public static String intToRoman(JsonNode num) {
        String numStr = num.toString();
        switch (numStr) {
            case "1" : return "I";
            case "2" : return "II";
            case "3" : return "III";
            case "4" : return "IV";
            case "5" : return "V";
            case "6" : return "VI";
            case "7" : return "VII";
            case "8" : return "VIII";
            case "9" : return "IX";
            case "10" : return "X";
            case "11" : return "XI";
            default: return null;
        }
    }

}