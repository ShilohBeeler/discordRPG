package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IChannel;

public class Config {
	
	public static String file = System.getProperty("user.home") + "/discordRPG/config.json";

	public static void initialize() throws IOException
	{
		String template =
				"{config:{"
			   +     "\"prefix\":\".\","
			   +     "\"admin\":\"\","
			   +     "\"shop\":\"\","
			   +     "\"setup\":false"
			   + "}}";
		JSONObject json = new JSONObject(template);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static String getShop() throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		return json.getJSONObject("config").getString("shop");
	}
	
	public static String getPrefix() throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		return json.getJSONObject("config").getString("prefix");
	}
	
	public static String getAdmin() throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		return json.getJSONObject("config").getString("admin");
	}
	
	public static boolean isSetup() throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		return json.getJSONObject("config").getBoolean("setup");
	}
	
	public static void setShop(IChannel channel) throws IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject config = json.getJSONObject("config");
		config.remove("shop");
		config.put("shop", channel.getID());
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void setAdmin(IChannel channel) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject config = json.getJSONObject("config");
		config.remove("admin");
		config.put("admin", channel.getID());
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void setPrefix(String prefix) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject config = json.getJSONObject("config");
		config.remove("prefix");
		config.put("prefix", prefix);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
}
