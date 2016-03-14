package com.shymain.discordRPG;

import java.io.FileWriter;
import java.io.IOException;

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
	
	public static String getShop()
	{
		
	}
	
	public static String getPrefix()
	{
		
	}
	
	public static String getAdmin()
	{
		
	}
	
	public static boolean isSetup()
	{
		
	}
	
	public static void setShop(IChannel channel)
	{
		
	}
	
	public static void setAdmin(IChannel channel)
	{
		
	}
	
	public static void setPrefix(String prefix)
	{
		
	}
}
