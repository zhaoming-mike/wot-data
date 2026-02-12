package com.mike.tanks;

import com.fasterxml.jackson.databind.JsonNode;
import com.mike.tanks.utils.APIUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws Throwable {

        //Object players = APIUtils.findPlayer("MIKE_MING_0923");
        //System.out.println("" + players);

        //Object vehicles = APIUtils.getAllVehicles("3", "sweden");
        //System.out.println("" + APIUtils.aa(vehicles));

        //JsonNode sweden = APIUtils.getAllVehiclesNode("3", "sweden");
        //JsonNode sweden = APIUtils.getAllVehiclesNode("10,11");
        JsonNode sweden = APIUtils.getAllVehiclesNode("1,2,3,4,5,6,7,8,9,10,11");
        Iterator<JsonNode> data = sweden.get("data").iterator();
        while(data.hasNext()) {
            JsonNode next = data.next();
            JsonNode tankId = next.get("tank_id");
            JsonNode is_premium = next.get("is_premium");
            JsonNode tier = next.get("tier");
            JsonNode images = next.get("images");
            JsonNode big_icon = images.get("big_icon");

            String big_icon_url = cut(big_icon);

            JsonNode nation = next.get("nation");
            String nationStr = cut(nation);
            JsonNode name = next.get("name");
            String nameStr = cut(name);
            JsonNode defaultProfile = next.get("default_profile");
            JsonNode ammo = defaultProfile.get("ammo");
            JsonNode gun = defaultProfile.get("gun");
            JsonNode dispersion = gun.get("dispersion");
            JsonNode move_down_arc = gun.get("move_down_arc");
            JsonNode jsonNode = ammo.get(1);
            if(jsonNode == null) {
                jsonNode = ammo.get(0);
            }
            JsonNode ammoA = jsonNode;
            JsonNode ammoAType = ammoA.get("type");
            JsonNode ammoADamage = ammoA.get("damage");
            JsonNode ammoADamage0 = ammoADamage.get(1);
//            System.out.printf("%-10s %-10s %-30s %-30s %-30s" , tankId, nation, ammoADamage0, ammoAType, name);
//            System.out.println();
            //System.out.printf("%-10s %-10s %-4s %-4s %-4s %-4s %-30s" , tankId, nation, tier, ammoADamage0, dispersion, move_down_arc, name);
            //System.out.println();


            System.out.println(tankId + "\t" + nationStr + "\t" + tier + "\t" + is_premium + "\t" + ammoADamage0 + "\t" + dispersion + "\t" + move_down_arc + "\t" + nameStr + "\t" + big_icon_url);
        }
    }

    @NotNull
    private static String cut(JsonNode node) {
        String text = node.toString();
        text = text.replaceAll("\"", "");
        return text;
    }


}