package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

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
	
	public static void refine(String refinery, String item, IChannel channel, IUser user) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject refineries = json.getJSONObject("refineries");
		if(refineries.isNull(refinery))
		{
			channel.sendMessage("That refinery does not exist.");
			return;
		}
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Floor.file));
		if(!json2.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("refineries").getBoolean(refinery))
		{
			channel.sendMessage("You can't do that here!");
			return;
		}
		if(refineries.getJSONObject(refinery).isNull(item))
		{
			channel.sendMessage("You cannot refine this item.");
			return;
		}
		JSONObject json3 = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject player = json3.getJSONObject("players").getJSONObject(user.getID());
		if(player.getJSONObject("inventory").isNull(item))
		{
			channel.sendMessage("You don't have that item!");
			return;
		}
		if(player.getJSONObject("inventory").getInt(item)<refineries.getJSONObject(refinery).getInt("required"))
		{
			channel.sendMessage("You need " + refineries.getJSONObject(refinery).getInt("required") + item + " to refine them.");
		}
		Player.inventoryRemove(user, item, refineries.getJSONObject(refinery).getInt("required"));
		Player.inventoryAdd(user, refineries.getJSONObject(refinery).getJSONObject(item).getString("output"), refineries.getJSONObject(refinery).getInt("result"));
		channel.sendMessage("You refine " + refineries.getJSONObject(refinery).getInt("required") + " " + item +", and retrieve "+ refineries.getJSONObject(refinery).getInt("result") + " "+ refineries.getJSONObject(refinery).getString("output") + ".");
	}
	
	
}
