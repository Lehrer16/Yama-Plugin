package com.YamaReminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class YamaReminderTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(YamaReminder.class);
		RuneLite.main(args);
	}
}