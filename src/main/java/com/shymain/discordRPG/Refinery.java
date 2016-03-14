package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Refinery {
	
	public static String file = System.getProperty("user.home") + "/discordRPG/refineries.json";

	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
            +"skill: \"smelting\","
            +"inputs:{"
            +    "iron_ore:{"
            +        "output:\"iron_bar\","
            +        "required:1,"
            +        "result:1,"
            +        "level:1,"
            +        "xp:3"
            +    "},"
            +    "generic:{"
            +        "output:\"generic_r\","
            +        "required:5,"
            +        "result:3,"
            +        "level:10,"
            +        "xp:100"
            +    "}"
            + "}"
        	+"}";
				JSONObject json = new JSONObject(DiscordRPG.readFile(file));
				JSONObject refinery = new JSONObject(template);
				json.getJSONObject("refineries").put("furnace", refinery);
				FileWriter r = new FileWriter(file);
				r.write(json.toString(3));
				r.flush();
				r.close();
	}
	
	public static void refine(String refinery, String item, IChannel channel, IUser user)
	{
		
	}
	
}
