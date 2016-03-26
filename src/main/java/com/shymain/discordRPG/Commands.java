package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.HTTP429Exception;

public class Commands {

	public static String file = System.getProperty("user.home") + "/discordRPG/commands.json";

	public static void initialize() throws JSONException, IOException
	{
		String template = "{"
        +    "type:\"event\","
        +    "tied_to:\"rock\""
        +	 "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject command = new JSONObject(template);
		json.getJSONObject("commands").put("mine", command);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void tieEvent(IChannel channel, String command, String tied_to) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String template = "{"
		+    "type:\"event\","
		+    "tied_to:\""+tied_to+"\""
		+	 "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject command1 = new JSONObject(template);
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(REvents.file));
		if(json.getJSONObject("commands").isNull(command))
		{
			channel.sendMessage("This command does not exist.");
			return;
		}
		if(json2.getJSONObject("events").isNull(tied_to))
		{
			channel.sendMessage("This event does not exist.");
			return;
		}
		json.getJSONObject("commands").remove(command);
		json.getJSONObject("commands").put(command, command1);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Success!");
	}
	
	public static void tieRefinery(IChannel channel, String command, String tied_to) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String template = "{"
		+    "type:\"refinery\","
		+    "tied_to:\""+tied_to+"\""
		+	 "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject command1 = new JSONObject(template);
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Refinery.file));
		if(json.getJSONObject("commands").isNull(command))
		{
			channel.sendMessage("This command does not exist.");
			return;
		}
		if(json2.getJSONObject("refineries").isNull(tied_to))
		{
			channel.sendMessage("This refinery does not exist.");
			return;
		}
		json.getJSONObject("commands").remove(command);
		json.getJSONObject("commands").put(command, command1);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Success!");
	}
	
	public static void create(String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		String template = "{"
        +    "type:\"none\","
        +    "tied_to:\"none\""
        +	 "}";
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("commands").has(name))
		{
			channel.sendMessage("This command already exists.");
			return;
		}
		JSONObject command = new JSONObject(template);
		json.getJSONObject("commands").put(name, command);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Command added!");
	}
	
	public static void delete(String name, IChannel channel) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		if(json.getJSONObject("commands").isNull(name))
		{
			channel.sendMessage("This command doesn't exist.");
			return;
		}
		json.getJSONObject("commands").remove(name);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage("Command deleted!");
	}
}
