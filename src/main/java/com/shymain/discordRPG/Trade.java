package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

public class Trade {
	
	public static HashMap<String, String> openTrades;
	public static String file = System.getProperty("user.home")+"/discordRPG/trades.json";

	public static void open(IChannel channel, IUser user, IUser recipient) throws JSONException, IOException, MissingPermissionsException, HTTP429Exception, DiscordException
	{
		if(openTrades.containsKey(user.getID()) || openTrades.containsValue(user.getID()))
		{
			channel.sendMessage("You are already in a trade, or have already been asked to trade!");
			return;
		}
		if(openTrades.containsKey(recipient.getID()) || openTrades.containsValue(recipient.getID()))
		{
			channel.sendMessage("That user is already in a trade, or has already been asked to trade!");
			return;
		}
		String template = ""
				+ "{"
				+ "trade_accepted:false,"
				+ "recipient:" + recipient + ","
				+ "user1:" + user.getName() + ","
				+ "user1_inventory:{},"
				+ "user2:" + recipient.getName() + ","
				+ "user2_inventory:{},"
				+ "trade_close:false,"
				+ "first_close:\"\""
				+ "}";
		JSONObject trade = new JSONObject(template);
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject trades = json.getJSONObject("trades");
		openTrades.put(recipient.getID(), user.getID());
		trades.put(user.getID(), trade);
		channel.sendMessage(recipient.mention() + ": " + user.mention() + " has offered to trade! Please either .trade accept or .trade reject this offer.");
	}
	
	public static void accept(IChannel channel, IUser recipient) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(!openTrades.containsKey(recipient))
		{
			channel.sendMessage("No one has requested to trade with you!");
			return;
		}
		IUser user = channel.getGuild().getUserByID(openTrades.get(recipient.getID()));
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject trades = json.getJSONObject("trades");
		JSONObject trade = trades.getJSONObject(user.getID());
		if(trade.getBoolean("trade_accepted"))
		{
			channel.sendMessage("You have already accepted the trade!");
			return;
		}
		trade.remove("trade_accepted");
		trade.put("trade_accepted", true);
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage(recipient.mention() + " has accepted the trade from " + user.mention() + ".");
	}
	
	public static void reject(IChannel channel, IUser recipient) throws MissingPermissionsException, HTTP429Exception, DiscordException, JSONException, IOException
	{
		if(!openTrades.containsKey(recipient))
		{
			channel.sendMessage("No one has requested to trade with you!");
			return;
		}
		IUser user = channel.getGuild().getUserByID(openTrades.get(recipient.getID()));
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject trades = json.getJSONObject("trades");
		trades.remove(user.getID());
		openTrades.remove(recipient.getID());
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
		channel.sendMessage(recipient.mention() + " has rejected the trade from " + user.mention() + ".");
	}
	
	public static void add(IChannel channel, IUser user, String item, int amount) throws MissingPermissionsException, HTTP429Exception, DiscordException, IOException
	{
		if(!openTrades.containsKey(user.getID()) && !openTrades.containsValue(user.getID()))
		{
			channel.sendMessage("You are not in a trade!");
			return;
		}
		String id = user.getID();
		if(openTrades.containsKey(user.getID()))
		{
			id = openTrades.get(user.getID());
		}
		JSONObject json = new JSONObject(DiscordRPG.readFile(file));
		JSONObject trades = json.getJSONObject("trades");
		if(!trades.getJSONObject(id).getBoolean("trade_accepted"))
		{
			channel.sendMessage("This trade has not been accepted by both parties!");
		}
		JSONObject json2 = new JSONObject(DiscordRPG.readFile(Player.file));
		JSONObject player = json2.getJSONObject("players").getJSONObject(user.getID());
		
		FileWriter r = new FileWriter(file);
		r.write(json.toString(3));
		r.flush();
		r.close();
	}
}
