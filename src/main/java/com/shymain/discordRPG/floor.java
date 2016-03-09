package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class Floor {
	
	public static String file = System.getProperty("user.home") + "/discordRPG/floors.json";
	
	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
			+ "\"shop\":"
			+ "{"
            + "\"health_potion\":10,"
            + "\"iron_pickaxe\":100"
            + "},"
            + "\"monsters\":["
            + "\"Gnome\""
            + "]"
    		+ "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = new JSONObject(template);
		json.getJSONObject("floors").put("1", floor);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}

	public static Encounter getMonster(int floor) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Monster.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(file));
		JSONObject mob = new JSONObject();
		JSONObject afloor = new JSONObject();
		afloor = json2.getJSONObject("floors").getJSONObject(Integer.toString(floor));
		Random r = new Random();
		int ran = r.nextInt(afloor.getJSONArray("monsters").length());
		String name = afloor.getJSONArray("monsters").getString(ran);
		mob = json.getJSONObject("monsters").getJSONObject(name);
		Encounter newEncounter = new Encounter(name, mob.getInt("maxhealth"), mob.getInt("health"), mob.getInt("attack"), mob.getInt("speed"));
		return newEncounter;
	}
	
}
