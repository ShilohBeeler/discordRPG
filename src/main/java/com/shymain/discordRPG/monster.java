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

public class monster {
	
	public static String file = System.getProperty("user.home")+"/discordRPG/monsters.json";
	public static HashMap<IUser, encounter> currentFights = new HashMap<IUser, encounter>();
	
	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
		           + "monster: \"Gnome\","
		           + "health: 10,"
		           + "maxhealth: 10,"
		           + "attack: 1,"
		           + "speed: 10,"
		           + "loottables: {"
		           + "iron_ore: 1,"
		           + "coins: 5"
		           + "}"
		           +"}";
		JSONObject json = new JSONObject(discordRPG.readFile(file));
		JSONObject monster = new JSONObject(template);
		json.getJSONArray("monsters").put(monster);
		FileWriter r = new FileWriter(file);
		r.write(json.toString());
		r.flush();
		r.close();
	}
	
	
	public static void startFight(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException
	{
		event.getMessage().getChannel().sendMessage(event.getMessage().getAuthor().mention() + " has challenged an angry Gnome to a duel!");
		encounter newEncounter = new encounter("Gnome", 10, 10, 1, 10);
		currentFights.put(event.getMessage().getAuthor(), newEncounter);
		System.out.println(currentFights.toString());
		event fight = new event("Defend", event.getMessage().getAuthor(), event.getMessage().getChannel());
		discordRPG.eventStorage.put(event.getMessage().getAuthor(), fight);
		System.out.println(discordRPG.eventStorage.toString());
		discordRPG.timedEvents.put(discordRPG.eventStorage.get(event.getMessage().getAuthor()), newEncounter.speed);
		}
	
	public static void attack(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		int attack = currentFights.get(user).attack;
		int health = currentFights.get(user).health;
		int maxhealth = currentFights.get(user).maxhealth;
		int speed = currentFights.get(user).speed;
		String name = currentFights.get(user).name;
		JSONObject json = new JSONObject(discordRPG.readFile(player.file));
		for(int i = 0; i<json.getJSONArray("players").length(); i++)
		{
			if(json.getJSONArray("players").getJSONObject(i).getString("player").equalsIgnoreCase(user.getID()))
			{
				int fighting = json.getJSONArray("players").getJSONObject(i).getJSONObject("stats").getInt("fighting");
				int playerattack = 1 + (fighting/5);
				int playerhealth = json.getJSONArray("players").getJSONObject(i).getInt("health");
				int maxplayerhealth = json.getJSONArray("players").getJSONObject(i).getInt("maxhealth");
				health -= playerattack;
				if(health>0)
				{
				currentFights.get(user).health = health;
				channel.sendMessage(user.mention() + " strikes the " + name + " for " + playerattack +" damage!\n"
						+ "The " + name + " currently has " + health + "/" + maxhealth + " health remaining.\n"
						+ user.getName() + " has " + playerhealth + " of " + maxplayerhealth + " health left.");
				FileWriter r = new FileWriter(player.file);
				r.write(json.toString());
				r.flush();
				r.close();
				}else{
				JSONObject object = new JSONObject(discordRPG.readFile(file));
				String dropType = null;
				int dropNumber = 0;
				for(int j = 0; j<object.length(); j++)
				{
					if(object.getJSONArray("monsters").getJSONObject(j).getString("monster").equalsIgnoreCase(name))
					{
						Random r = new Random();
						int k = r.nextInt(object.getJSONArray("monsters").getJSONObject(j).getJSONObject("loottables").names().length());
						dropType = object.getJSONArray("monsters").getJSONObject(j).getJSONObject("loottables").names().getString(k);
						dropNumber = object.getJSONArray("monsters").getJSONObject(j).getJSONObject("loottables").getInt(dropType);
					}
				}
				channel.sendMessage(user.mention() + " has defeated the " + name + "!\n"
						+ "It drops: "+ dropNumber +"x "+dropType+"!");
				discordRPG.timedEvents.remove(discordRPG.eventStorage.get(user));
				discordRPG.eventStorage.remove(user);
				currentFights.remove(user);
				}
				
			}
		}
	}
	
	public static void defend(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		int attack = currentFights.get(user).attack;
		int health = currentFights.get(user).health;
		int maxhealth = currentFights.get(user).maxhealth;
		int speed = currentFights.get(user).speed;
		String name = currentFights.get(user).name;
		JSONObject json = new JSONObject(discordRPG.readFile(player.file));
		for(int i = 0; i<json.getJSONArray("players").length(); i++)
		{
			if(json.getJSONArray("players").getJSONObject(i).getString("player").equalsIgnoreCase(user.getID()))
			{
				int fighting = json.getJSONArray("players").getJSONObject(i).getJSONObject("stats").getInt("fighting");
				int playerhealth = json.getJSONArray("players").getJSONObject(i).getInt("health");
				int maxplayerhealth = json.getJSONArray("players").getJSONObject(i).getInt("maxhealth");
				playerhealth -= attack;
				if(playerhealth>0)
				{
				json.getJSONArray("players").getJSONObject(i).remove("health");
				json.getJSONArray("players").getJSONObject(i).put("health", playerhealth);
				currentFights.get(user).health = health;
				channel.sendMessage("The " + name + " strikes " + user.mention() + " for " + attack +" damage!\n"
						+ user.getName() + " has " + playerhealth + " of " + maxplayerhealth + " health left.");
				FileWriter r = new FileWriter(player.file);
				r.write(json.toString());
				r.flush();
				r.close();
				event fight = new event("Defend", user, channel);
				discordRPG.timedEvents.put(fight, speed);
				discordRPG.eventStorage.remove(user);
				discordRPG.eventStorage.put(user, fight);
				}
				
			}
		}
	}
	
}
