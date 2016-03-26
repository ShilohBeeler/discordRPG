package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
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
		if(json2.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("refineries").isNull(refinery))
		{
			channel.sendMessage("You can't do that here!");
			return;
		}
		if(refineries.getJSONObject(refinery).getJSONObject("input").isNull(item))
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
		JSONObject item_ = refineries.getJSONObject(refinery).getJSONObject("input").getJSONObject(item);
		if(player.getJSONObject("inventory").getInt(item)<item_.getInt("required"))
		{
			channel.sendMessage("You need " + item_.getInt("required") + item + " to refine them.");
		}
		Player.inventoryRemove(user, item, item_.getInt("required"));
		Player.inventoryAdd(user, item_.getString("output"), item_.getInt("result"));
		Player.addXP(user, channel, refineries.getJSONObject(refinery).getString("skill"), item_.getInt("xp"));
		channel.sendMessage("You refine " + item_.getInt("required") + " " + item +", and retrieve "+ item_.getInt("result") + " "+ item_.getString("output") + ".");
	}
	
	public static void create(String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String template = "{"
        +    "skill:\"none\","
        +    "input:{}"
        +	 "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("refineries").has(name))
		{
			channel.sendMessage("This refinery already exists.");
			return;
		}
		JSONObject refinery = new JSONObject(template);
		json.getJSONObject("refineries").put(name, refinery);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Refinery added!");
	}
	
	public static void delete(String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("refineries").isNull(name))
		{
			channel.sendMessage("This refinery doesn't exist.");
			return;
		}
		json.getJSONObject("refineries").remove(name);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Refinery deleted!");
	}
	
	public static void skillSet(String refinery, String skill, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("refineries").isNull(refinery))
		{
			channel.sendMessage("This refinery doesn't exist.");
			return;
		}
		json.getJSONObject("refineries").getJSONObject(refinery).remove("skill");
		json.getJSONObject("refineries").getJSONObject(refinery).put("skill", skill);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Input added!");
	}
	
	public static void inputCreate(String refinery, String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String template = "{"
	            +    "output:\"coins\","
	            +    "required:1,"
	            +    "result:1,"
	            +    "level:1,"
	            +    "xp:1"
	            +    "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("refineries").isNull(refinery))
		{
			channel.sendMessage("This refinery doesn't exist.");
			return;
		}
		if(json.getJSONObject("refineries").getJSONObject(refinery).getJSONObject("input").has(name))
		{
			channel.sendMessage("This input already exists.");
			return;
		}
		JSONObject input = new JSONObject(template);
		json.getJSONObject("refineries").getJSONObject(refinery).getJSONObject("input").put(name, input);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Input added!");
	}
	
	public static void inputDelete(String refinery, String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("refineries").isNull(refinery))
		{
			channel.sendMessage("This refinery doesn't exist.");
			return;
		}
		if(json.getJSONObject("refineries").getJSONObject(refinery).getJSONObject("input").isNull(name))
		{
			channel.sendMessage("This input doesn't exist.");
			return;
		}
		json.getJSONObject("refineries").getJSONObject(refinery).getJSONObject("input").remove(name);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Input deleted!");
	}
	
	public static void inputEdit(String refinery, String name, String key, String value, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("refineries").isNull(refinery))
		{
			channel.sendMessage("This refinery doesn't exist.");
			return;
		}
		if(json.getJSONObject("refineries").getJSONObject(refinery).getJSONObject("input").isNull(name))
		{
			channel.sendMessage("This input doesn't exist.");
			return;
		}
		JSONObject input = json.getJSONObject("refineries").getJSONObject(refinery).getJSONObject("input").getJSONObject(name);
		if(input.isNull(key))
		{
			channel.sendMessage("Not a valid field. You can change: output, required, result, level, xp.");
			return;
		}
		int val = 0;
		if(StringUtils.isNumeric(value))
		{
			val = Integer.parseInt(value);
		}
		input.remove(key);
		if(val!=0)
		{
			input.put(key, val);
		}else
		{
			input.put(key, value);
		}
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Input edited!");
	}
}
