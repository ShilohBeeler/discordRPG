package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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
            + "\"events\":{"
            + "\"Rock\":{"
            + "\"ready\":3,"
            + "\"max\":3,"
            + "\"xp\":3,"
            + "\"refresh_time\": 60,"
            + "\"drops\":\"iron_ore\""
            + "},"
            + "\"Tree\":{"
            + "\"ready\":10,"
            + "\"max\":10,"
            + "\"xp\":2,"
            + "\"refresh_time\": 60,"
            + "\"drops\":\"oak_wood\""
            + "}"            
            + "}"
    		+ "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = new JSONObject(template);
		json.getJSONObject("floors").put("149548522058809344", floor);
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
	
	public static void mineRock(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = json.getJSONObject("floors").getJSONObject(channel.getID());
		JSONObject rock = floor.getJSONObject("events").getJSONObject("Rock");
		int dropNo = 1;
		if(rock.getInt("ready")==0)
		{
			channel.sendMessage("You swing your pick at the rock.\nBut there was no ore left.");
		}else{
			int ready = rock.getInt("ready");
			ready--;
			rock.remove("ready");
			rock.put("ready", ready);
			FileWriter r = new FileWriter(file);
			r.write(json.toString(3));
			r.flush();
			r.close();
			int skill = Player.getSkill(user, "mining");
			Random ran = new Random();
			int factor = ran.nextInt(skill);
			factor /= 5;
			dropNo += factor;
			Player.inventoryAdd(user, rock.getString("drops"), dropNo);
			Event rockRefresh = new Event("RockRefreshEvent", user, channel);
			DiscordRPG.timedEvents.put(rockRefresh, rock.getInt("refresh_time"));
			channel.sendMessage("You swing your pick at the rock.\nYou get " + dropNo + " " + rock.getString("drops") + "!");
			Player.addXP(user, channel, "mining", rock.getInt("xp"));
		}
	}
	
	public static void addRock(IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject rock = json.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("events").getJSONObject("Rock");
		rock.increment("ready");
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("A vein reappears in one of the rocks.\n"
				+ rock.getInt("ready") + " of " + rock.getInt("max") + " rocks now available.");
		
	}
	
	public static void cutTree(IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject floor = json.getJSONObject("floors").getJSONObject(channel.getID());
		JSONObject tree = floor.getJSONObject("events").getJSONObject("Tree");
		int dropNo = 1;
		if(tree.getInt("ready")==0)
		{
			channel.sendMessage("You swing your axe at the tree.\nAll that's left of the trees is a field of stumps.");
		}else{
			int ready = tree.getInt("ready");
			ready--;
			tree.remove("ready");
			tree.put("ready", ready);
			FileWriter r = new FileWriter(file);
			r.write(json.toString(3));
			r.flush();
			r.close();
			int skill = Player.getSkill(user, "woodcutting");
			Random ran = new Random();
			int factor = ran.nextInt(skill);
			factor /= 5;
			dropNo += factor;
			Player.inventoryAdd(user, tree.getString("drops"), dropNo);
			Event treeRefresh = new Event("TreeRefreshEvent", user, channel);
			int refresh_time = tree.getInt("refresh_time");
			if(Ambient.weather.equalsIgnoreCase("rain"))
			{
				refresh_time /= 2;
			}
			DiscordRPG.timedEvents.put(treeRefresh, refresh_time);
			channel.sendMessage("You swing your axe at the tree.\nYou get " + dropNo + " " + tree.getString("drops") + "!");
			Player.addXP(user, channel, "woodcutting", tree.getInt("xp"));
		}
	}
	
	public static void addTree(IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject tree = json.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("events").getJSONObject("Tree");
		tree.increment("ready");
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("A tree has grown anew in the fields.\n"
				+ tree.getInt("ready") + " of " + tree.getInt("max") + " trees now available.");
		
	}
	
}
