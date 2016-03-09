package com.shymain.discordRPG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserJoinEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class Player {
	
	public static String file = System.getProperty("user.home")+"/discordRPG/players.json";
	
	public static void create(IUser user) throws JSONException, IOException
	{
		String template = "{"
           + "stats:" 
           + "{"
           +     "mining: 1,"
           +     "fighting: 1,"
           +     "magic: 1"
           + "},"
           + "inventory:"
           + "{"
           +     "steak: 1,"
           +     "iron_axe: 1"
           + "},"
           + "equipment:"
           + "{"
           +     "head: \"iron_head\","
           +     "body: \"iron_body\","
           +     "feet: \"iron_feet\","
           +     "weapon: \"sword\""
           + "},"
           + "floor: 1,"
           + "health: 10,"
           + "maxhealth: 10"
           +"}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = new JSONObject(template);
		json.getJSONObject("players").put(user.getID(), player);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static String statsUp(IUser user, String stat) throws JSONException, IOException{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		if(!player.getJSONObject("stats").has(stat))
		{
			return "StatNotFoundError";
		}
		player.getJSONObject("stats").increment(stat);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		return "Success";
	}
	
	public static String floorUp(IUser user) throws JSONException, IOException{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		player.increment("floor");
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		return "Success";
	}
	
	public static String inventoryAdd(IUser user, String item, int number) throws JSONException, IOException{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		if(player.getJSONObject("inventory").has(item))
		{
			for(int j = 0; j < number; j++)
			{
				player.getJSONObject("inventory").increment(item);
			}
		}else
		{
			player.getJSONObject("inventory").put(item, number);
		}
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		return "Success";
	}
	
	public static String inventoryRemove(IUser user, String item, int number) throws JSONException, IOException{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		if(player.getJSONObject("inventory").has(item))
			{
			int itemno = player.getJSONObject("inventory").getInt(item);
			for(int j = 0; j < number; j++)
			{
				itemno--;
			}
			if(itemno<0)
			{
				return "ItemNumberError";
			}
			player.getJSONObject("inventory").remove(item);
			if(itemno!=0)
			{
				player.getJSONObject("inventory").put(item, itemno);
			}
		}else
		{
			return "ItemNotFoundError";
		}
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		return "Success";
	}
	
	public static void getInventory(MessageReceivedEvent event) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		Iterator<String> keys = player.getJSONObject("inventory").keys();
		String output = "```\nInventory:\n";
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			int number = player.getJSONObject("inventory").getInt(key);
			String numbers = Integer.toString(number);
			output += key + ": " + number + ".\n";
		}
		output += "```";
		event.getMessage().getChannel().sendMessage(output);
	}
	
}
