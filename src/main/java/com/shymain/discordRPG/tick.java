package com.shymain.discordRPG;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import org.json.JSONException;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.util.HTTP429Exception;

public class tick extends TimerTask{

	public void run()
	{
		System.out.println("running.");
		Iterator<Map.Entry<event,Integer>> iter = discordRPG.timedEvents.entrySet().iterator();
		System.out.println(iter.hasNext());
		while (iter.hasNext()) {
		    Map.Entry<event,Integer> entry = iter.next();
		    int temp = entry.getValue();
		    temp--;
		    System.out.println(temp);
		    System.out.println(entry.getKey().user.getName());
		    if(temp==0)
		    {
		    	try {
		    		iter.remove();
					discordRPG.eventDistributer(entry.getKey().eventType, entry.getKey().user, entry.getKey().channel);
				} catch (JSONException | IOException | MissingPermissionsException | HTTP429Exception
						| DiscordException e) {
					e.printStackTrace();
				}
		    }else{
		    	entry.setValue(temp);
		    }
		}
	}
	
}
