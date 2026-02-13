package com.mike.tanks;

import com.mike.tanks.utils.APIUtils;

import java.sql.SQLOutput;

public class Push {

    public static void main(String[] args) {

        String url = "https://qyapi.weixin.qq.com/cgi-bin/wedoc/smartsheet/webhook?key=X85In3V7MR8ZmFgr0kUxbx06tcaKhxafEtayjvaqwIfO6P2EnhJJaKdqMmyv8F4p32snULjeT9TnMgUCIqHHpskErPOyxRDeWG8JlSw3IWc";




        //String json = "{\"schema\":{\"fFvlRJ\":\"文本\"},\"add_records\":[{\"values\":{\"fFvlRJ\":\"赵明\"}}]}";
        String json = "{\"add_records\":[{\"values\":{\"fFvlRJ\":\"赵明\", \"f2Ug8c\" : \" ZHAOMING \"}}]}";


        //String jsonUpdate = "{\"add_records\":[{\"values\":{\"fFvlRJ\":\"赵明\", \"f2Ug8c\" : \" ZHAOMING \"}}]}";

        String jsonUpdate = "{\"update_records\": [{ \"record_id\": \"msxGYP\", \"values\":{\"fFvlRJ\":\"赵明\", \"f2Ug8c\" : \" ZHAOMING1235 \"}}]}";

        json = "{\n" +
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
                "        \"fFvlRJ\": 1,\n" +
                "        \"f2Ug8c\": \"测试文本\",\n" +
                "        \"fsSD6d\": \"测试文本\",\n" +
                "        \"fd3J7k\": \"测试文本\",\n" +
                "        \"fRCJUi\": 1,\n" +
                "        \"fy4KZG\": 1,\n" +
                "        \"f5VSKa\": 1,\n" +
                "        \"fprcbn\": \"测试文本\",\n" +
                "        \"f1DnGA\": [{\"link\": \"123\", \"text\": \"点击查看\"}]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        APIUtils.pushData2WX(json);

    }
}
