package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class REvents {
	
	public static String file = System.getProperty("user.home") + "/discordRPG/events.json";

	public static void initialize()
	{
		
	}
	
	public static void doEvent(String event, IUser user, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject events = json.getJSONObject("events");
		
		if(events.isNull(event))
		{
			channel.sendMessage("A strange distortion appears, preventing you from doing this.");
			return;
		}
		JSONObject revent = events.getJSONObject(event);
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Floor.file));
		if(json2.getJSONObject("events").isNull(event))
		{
			channel.sendMessage("You're in the wrong place to do this.");
			return;
		}
		JSONObject fevent = json2.getJSONObject("events").getJSONObject(event);
		if(!revent.getString("requires").equalsIgnoreCase("none"))
		{
			String holding = Player.getSlot(user, channel, "hand");
			boolean required = Item.getBool(holding, revent.getString("requires"));
			if(!required)
			{
				channel.sendMessage(revent.getString("required_message"));
				return;
			}
		}
		String skill_type = revent.getString("skill");
		if(Player.getSkill(user, skill_type)<fevent.getInt("required_level"))
		{
			channel.sendMessage(
					"You do not have a high enough skill level to do this! You need to be level " + 
					Integer.toString(fevent.getInt("required_level")) + " in "+ skill_type);
			return;
		}
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
	
	public static void refreshEvent(IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
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
}
