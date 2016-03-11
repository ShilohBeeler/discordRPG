package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class Item {

	public static String file = Store.file2;
	
	public static void create(IUser user, IChannel channel, String item) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject items = json.getJSONObject("items");
		if(items.has(item))
		{
			channel.sendMessage("Item already exists.");
			return;
		}
		JSONObject forJSON = new JSONObject("{to_remove:false,value:0,flavor_text:\"Change me!\"}");
		items.put(item, forJSON);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void value(IUser user, IChannel channel, String item, String key, String value) throws IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject items = json.getJSONObject("items");
		int meep;
		if(!items.has(item))
		{
			channel.sendMessage("Item doesn't exist.");
			return;
		}
		if(StringUtils.isNumeric(value))
		{
			meep=Integer.parseInt(value);
			items.getJSONObject(item).remove(key);
			items.getJSONObject(item).put(key, meep);
		}else if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
		{
			Boolean tf = Boolean.valueOf(value);
			items.getJSONObject(item).remove(key);
			items.getJSONObject(item).put(key, tf);
		}else
		{
			items.getJSONObject(item).remove(key);
			items.getJSONObject(item).put(key, value);
		}
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static void remove(IUser user, IChannel channel, String item) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject items = json.getJSONObject("items");
		if(!items.has(item))
		{
			channel.sendMessage("Item doesn't exist.");
			return;
		}
		items.remove(item);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
	
	public static boolean getBool(String item, String field) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject items = json.getJSONObject("items");
		if(items.getJSONObject(item).isNull(field))
		{
			return false;
		}
		return items.getJSONObject(item).getBoolean(field);
	}
	
	public static String getString(String item, String field) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject items = json.getJSONObject("items");
		if(items.getJSONObject(item).isNull(field))
		{
			return "";
		}
		return items.getJSONObject(item).getString(field);
	}
	
	public static int getInt(String item, String field) throws JSONException, IOException
	{
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject items = json.getJSONObject("items");
		if(items.getJSONObject(item).isNull(field))
		{
			return 0;
		}
		return items.getJSONObject(item).getInt(field);
	}
}
