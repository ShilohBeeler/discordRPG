package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class Floor {
	
	public static String file = System.getProperty("user.home") + "/discordRPG/floors.json";
	
	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
            +"\"events\": {"
            +    "rock:{"
            +        "\"max\":5,"
            +        "\"ready\":5,"
            +        "\"required_level\": 1,"
            +        "\"xp\": 3,"
            +        "\"refresh_time\": 60,"
            +        "\"drops\": \"iron_ore\""
            +    "}"
            +"},"
            +"\"refineries\":{"
            +    "furnace: true,"
            +    "lumber_mill:false"
            +"}"
            +"}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = new JSONObject(template);
		json.getJSONObject("floors").put("157558660732682241", floor);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}

	public static Encounter getMonster(MessageReceivedEvent event) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Monster.file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Store.file));
		JSONObject json3 = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject mob = new JSONObject();
		JSONObject player = json3.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		JSONArray monsters = json2.getJSONObject("ranks").getJSONObject(Integer.toString(player.getInt("rank"))).getJSONArray("monsters");
		Random r = new Random();
		int ran = r.nextInt(monsters.length());
		String name = monsters.getString(ran);
		mob = json.getJSONObject("monsters").getJSONObject(name);
		Encounter newEncounter = new Encounter(name, mob.getInt("maxhealth"), mob.getInt("health"), mob.getInt("attack"), mob.getInt("speed"));
		return newEncounter;
	}
	
	public static void addRefinery(String refinery, String channelID, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = json.getJSONObject(channelID);
		if(floor.getJSONObject("refineries").has(refinery))
		{
			channel.sendMessage("This floor already has this refinery!");
			return;
		}
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Refinery.file));
		if(json2.getJSONObject("refineries").isNull(refinery))
		{
			channel.sendMessage("That refinery does not exist!");
			return;
		}
		floor.getJSONObject("refineries").put(refinery, true);
		FileWriter r = new FileWriter(DiscordRPG.readFile(file));
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void deleteRefinery(String refinery, String channelID, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = json.getJSONObject(channelID);
		if(!floor.getJSONObject("refineries").has(refinery))
		{
			channel.sendMessage("This floor doesn't have this refinery!");
			return;
		}
		floor.getJSONObject("refineries").remove(refinery);
		FileWriter r = new FileWriter(DiscordRPG.readFile(file));
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void createFloor(String channelID, IChannel channel) throws JSONException, IOException
	{
		String template = "{"
	           +"\"events\": {"
	           +"},"
	           +"\"refineries\":{"
	           +"}"
	           +"}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = new JSONObject(template);
		json.getJSONObject("floors").put(channelID, floor);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void deleteFloor(String channelID, IChannel channel) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		json.getJSONObject("floors").remove(channelID);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void eventCreate(String channelID, String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String template = "{"
	            +        "\"max\":1,"
	            +        "\"ready\":1,"
	            +        "\"required_level\": 1,"
	            +        "\"xp\": 1,"
	            +        "\"refresh_time\": 60,"
	            +        "\"drops\": \"coins\""
	            +    "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("floors").isNull(channelID))
		{
			channel.sendMessage("This floor doesn't exist.");
			return;
		}
		if(json.getJSONObject("floors").getJSONObject(channelID).getJSONObject("events").has(name))
		{
			channel.sendMessage("This event already exists.");
			return;
		}
		JSONObject input = new JSONObject(template);
		json.getJSONObject("floors").getJSONObject(channelID).getJSONObject("events").put(name, input);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Event added!");
	}
	
	public static void eventDelete(String channelID, String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("floors").isNull(channelID))
		{
			channel.sendMessage("This floor doesn't exist.");
			return;
		}
		if(json.getJSONObject("floors").getJSONObject(channelID).getJSONObject("events").isNull(name))
		{
			channel.sendMessage("This event doesn't exist.");
			return;
		}
		json.getJSONObject("floors").getJSONObject(channelID).getJSONObject("events").remove(name);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Input deleted!");
	}
	
	public static void eventEdit(String channelID, String name, String key, String value, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("floors").isNull(channelID))
		{
			channel.sendMessage("This floor doesn't exist.");
			return;
		}
		if(json.getJSONObject("floors").getJSONObject(channelID).getJSONObject("events").isNull(name))
		{
			channel.sendMessage("This event doesn't exist.");
			return;
		}
		JSONObject input = json.getJSONObject("floors").getJSONObject(channelID).getJSONObject("events").getJSONObject(name);
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
