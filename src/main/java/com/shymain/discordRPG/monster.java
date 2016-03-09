/*
 * Undummy startFight to take from json file for floor.
 * Make it possible to add monsters instead of making initialize static.
 */

package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class Monster {
	
	public static String file = System.getProperty("user.home")+"/discordRPG/monsters.json";
	public static HashMap<IUser, Encounter> currentFights = new HashMap<IUser, Encounter>();
	
	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
		           + "health: 10,"
		           + "maxhealth: 10,"
		           + "attack: 1,"
		           + "speed: 10,"
		           + "loottables: {"
		           + "iron_ore: 1,"
		           + "coins: 5"
		           + "}"
		           +"}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject monster = new JSONObject(template);
		json.getJSONObject("monsters").put("Gnome", monster);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void startFight(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		Encounter newEncounter = Floor.getMonster(event);
		event.getMessage().getChannel().sendMessage("You have challenged an angry " + newEncounter.name + " to a duel!");
		currentFights.put(event.getMessage().getAuthor(), newEncounter);
		System.out.println(currentFights.toString());
		Event fight = new Event("Defend", event.getMessage().getAuthor(), event.getMessage().getChannel());
		DiscordRPG.eventStorage.put(event.getMessage().getAuthor(), fight);
		System.out.println(DiscordRPG.eventStorage.toString());
		DiscordRPG.timedEvents.put(DiscordRPG.eventStorage.get(event.getMessage().getAuthor()), newEncounter.speed);
		}
	
	public static void attack(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		int attack = currentFights.get(user).attack;
		int health = currentFights.get(user).health;
		int maxhealth = currentFights.get(user).maxhealth;
		int speed = currentFights.get(user).speed;
		String name = currentFights.get(user).name;
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		int fighting = player.getJSONObject("stats").getInt("fighting");
		int playerattack = 1 + (fighting/5);
		int playerhealth = player.getInt("health");
		int maxplayerhealth = player.getInt("maxhealth");
		health -= playerattack;
		if(health>0)
		{
		currentFights.get(user).health = health;
		channel.sendMessage(user.mention() + " strikes the " + name + " for " + playerattack +" damage!\n"
				+ "The " + name + " currently has " + health + "/" + maxhealth + " health remaining.\n"
				+ user.getName() + " has " + playerhealth + " of " + maxplayerhealth + " health left.");
		FileWriter r = new FileWriter(Player.file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		}else{
		JSONObject object = new JSONObject(DiscordRPG.readFile(file));
		String dropType = null;
		int dropNumber = 0;
		JSONObject monster = object.getJSONObject("monsters").getJSONObject(name);
		Random r = new Random();
		int k = r.nextInt(monster.getJSONObject("loottables").names().length()-1);
		dropType = monster.getJSONObject("loottables").names().getString(k);
		dropNumber = monster.getJSONObject("loottables").getInt(dropType);
		channel.sendMessage(user.mention() + " has defeated the " + name + "!\n"
				+ "It drops: "+ dropNumber +"x "+dropType+"!");
		Player.inventoryAdd(user, dropType, dropNumber);
		DiscordRPG.timedEvents.remove(DiscordRPG.eventStorage.get(user));
		DiscordRPG.eventStorage.remove(user);
		currentFights.remove(user);
		}
	}
	
	public static void defend(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		int attack = currentFights.get(user).attack;
		int health = currentFights.get(user).health;
		int maxhealth = currentFights.get(user).maxhealth;
		int speed = currentFights.get(user).speed;
		String name = currentFights.get(user).name;
		JSONObject json = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject player = json.getJSONObject("players").getJSONObject(user.getID());
		int fighting = player.getJSONObject("stats").getInt("fighting");
		int playerhealth = player.getInt("health");
		int maxplayerhealth = player.getInt("maxhealth");
		playerhealth -= attack;
		if(playerhealth>0)
		{
		player.remove("health");
		player.put("health", playerhealth);
		currentFights.get(user).health = health;
		channel.sendMessage("The " + name + " strikes " + user.mention() + " for " + attack +" damage!\n"
				+ user.getName() + " has " + playerhealth + " of " + maxplayerhealth + " health left.");
		FileWriter r = new FileWriter(Player.file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		Event fight = new Event("Defend", user, channel);
		DiscordRPG.timedEvents.put(fight, speed);
		DiscordRPG.eventStorage.remove(user);
		DiscordRPG.eventStorage.put(user, fight);
		}
	}
	
}
