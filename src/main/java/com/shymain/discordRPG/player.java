package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class Player {
	
	public static String file = System.getProperty("user.home")+"/discordRPG/players.json";
	
	public static void create(IUser user) throws JSONException, IOException
	{
		String template = "{"
           + "stats:" 
           + "{"
           +     "mining: { level: 1, xp: 0},"
           +     "fighting: { level: 1, xp: 0},"
           +     "magic: { level: 1, xp: 0}"
           +     "woodcutting: { level: 1, xp: 0}"
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
           +     "hand: \"sword\""
           + "},"
           + "rank: 1,"
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
	
	public static String rankUp(IUser user) throws JSONException, IOException{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		player.increment("rank");
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
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		Iterator<?> keys = player.getJSONObject("inventory").keys();
		String output = "```\nInventory:\n";
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			int number = player.getJSONObject("inventory").getInt(key);
			output += key + ": " + number + ".\n";
		}
		output += "```";
		event.getMessage().getChannel().sendMessage(output);
	}
	
	public static void heal(MessageReceivedEvent event, int health) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(event.getMessage().getAuthor().getID());
		int currenthealth = player.getInt("health");
		currenthealth += health;
		if(currenthealth>player.getInt("maxhealth"))
		{
			currenthealth = player.getInt("maxhealth");
		}
		player.remove("health");
		player.put("health", currenthealth);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void addXP(IUser user, IChannel channel, String skill, int xp) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		JSONObject thisSkill = player.getJSONObject("stats").getJSONObject(skill);
		int level = thisSkill.getInt("level");
		int exp = thisSkill.getInt("xp");
		exp += xp;
		while(exp > (level * level - level + 10))
		{
			level++;
			exp -= (level * level - level + 10);
			channel.sendMessage("You have leveled up your " + skill + " to level "+ level +"!");
		}
		thisSkill.remove("level");
		thisSkill.put("level", level);
		thisSkill.remove("xp");
		thisSkill.put("xp", exp);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void equip(IUser user, IChannel channel, String item) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		String slot = "";
		switch(item){
		case "iron_axe":
		case "iron_pickaxe":
		case "sword":
			slot = "hand";
			break;
		case "iron_body":
			slot = "body";
			break;
		case "iron_head":
			slot = "head";
			break;
		case "iron_feet":
			slot = "feet";
			break;
		default:
			channel.sendMessage("That cannot be equipped!");
			return;
		}
		if(!player.getJSONObject("equipment").getString(slot).equalsIgnoreCase("empty"))
		{
			channel.sendMessage("You already have something equipped in your " + slot + " slot.");
			return;
		}
		player.getJSONObject("equipment").remove(slot);
		player.getJSONObject("equipment").put(slot, item);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("You equip the " + item + ".");
	}
	
	public static void unequip(IUser user, IChannel channel, String slot) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		if(player.getJSONObject("equipment").isNull(slot) || player.getJSONObject("equipment").getString(slot).equalsIgnoreCase("empty"))
		{
			channel.sendMessage("Unfortunately, you don't have anything equipped there.");
			return;
		}
		String item = player.getJSONObject("equipment").getString(slot);
		player.getJSONObject("equipment").remove(slot);
		player.getJSONObject("equipment").put(slot, "empty");
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		Player.inventoryAdd(user, item, 1);
		channel.sendMessage("You store the " + item + " in your inventory.");
	}
	
	public static String getSlot(IUser user, IChannel channel, String slot) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		String item = player.getJSONObject("equipment").getString(slot);
		return item;
	}
	
	public static int getSkill(IUser user, String skill) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		int skill_level = player.getJSONObject("stats").getJSONObject(skill).getInt("level");
		return skill_level;
	}
	
	public static void getEquip(IUser user, IChannel channel) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		Iterator<?> keys = player.getJSONObject("equipment").keys();
		String output = "```\nEquipment:\n";
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			String equipped = player.getJSONObject("equipment").getString(key);
			output += key + ": " + equipped + ".\n";
		}
		output += "```";
		channel.sendMessage(output);
	}
	
	public static void getSkills(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		Iterator<?> keys = player.getJSONObject("stats").keys();
		String output = "```\nSkills:\n";
		while(keys.hasNext())
		{
			String key = (String)keys.next();
			int level = player.getJSONObject("stats").getJSONObject(key).getInt("level");
			int xp = player.getJSONObject("stats").getJSONObject(key).getInt("xp");
			String maxExp = Integer.toString(level * level - level + 10);
			String levelS = Integer.toString(level);
			String exp = Integer.toString(xp);
			output += key + ": Level " + levelS + ". " + exp + "/" + maxExp + "xp to next level.\n";
		}
		output += "```";
		channel.sendMessage(output);
	}
}
