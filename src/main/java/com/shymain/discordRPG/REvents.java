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

	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
		+ "\"skill\": \"mining\","
        + "\"attempt_message\":\"You swing your pick at the rock.\","
        + "\"failure_message\":\"But there was no ore left.\","
        + "\"refresh_message\":\"A vein appears in one of the rocks.\","
        + "\"required_message\":\"You don't have a pickaxe equipped!\","
        + "\"requires\":\"can_mine\""
    	+ "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject event = new JSONObject(template);
		json.getJSONObject("events").put("rock", event);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
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
		if(json2.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("events").isNull(event))
		{
			channel.sendMessage("You're in the wrong place to do this.");
			return;
		}
		JSONObject fevent = json2.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("events").getJSONObject(event);
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
		if(fevent.getInt("current")==0)
		{
			channel.sendMessage(revent.getString("attempt_message")+"\n"+revent.getString("failure_message"));
		}else{
			int current = fevent.getInt("current");
			current--;
			fevent.remove("current");
			fevent.put("current", current);
			FileWriter r = new FileWriter(Floor.file);
			r.write(json2.toString(3));
			r.flush();
			r.close();
			int skill = Player.getSkill(user, skill_type);
			skill -= fevent.getInt("required_level");
			skill++;
			Random ran = new Random();
			int factor = ran.nextInt(skill);
			factor /= 5;
			dropNo += factor;
			Player.inventoryAdd(user, fevent.getString("drops"), dropNo);
			Event eventRefresh = new Event(event+"RefreshEvent", user, channel);
			DiscordRPG.timedEvents.put(eventRefresh, fevent.getInt("refresh_time"));
			channel.sendMessage(revent.getString("attempt_message") + "\nYou get " + dropNo + " " + fevent.getString("drops") + "!");
			Player.addXP(user, channel, "mining", fevent.getInt("xp"));
		}
	}
	
	public static void refreshEvent(String event, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		event = event.replace("RefreshEvent", "");
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Floor.file));
		JSONObject fevent = json2.getJSONObject("floors").getJSONObject(channel.getID()).getJSONObject("events").getJSONObject(event);
		fevent.increment("ready");
		FileWriter r = new FileWriter(file);
		r.write(json2.toString(3));
		r.flush();
		r.close();
		JSONObject revent = json.getJSONObject("events").getJSONObject(event);
		channel.sendMessage(revent.getString("refresh_message")+"\n"
				+ fevent.getInt("ready") + " of " + fevent.getInt("max") + " now available.");
		
	}
}
