package com.arlania.world.content.discordbot;


import javax.security.auth.login.LoginException;

import com.arlania.world.World;
//import net.dv8tion.jda.api.AccountType;
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.JDABuilder;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot {
	
}
/*public class DiscordBot extends ListenerAdapter {

	//public static JDA jda;

	public void initialize() {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken("NjE5NTI4OTc1NTEzMDI2NTcx.XXJjeA.Y80sRiyW1EFwOg0dX9sMTS8UHKg");
		builder.addEventListeners(new Main());
		//try {
			//jda = builder.build();
		//} catch (LoginException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}

	private final String PREFIX = "::";

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		String message = event.getMessage().getContentDisplay();

		String[] command = message.split("-");

		if (command[0].equalsIgnoreCase(PREFIX + "players")) {
			event.getChannel().sendMessage("There are currently " + World.getPlayers().size() + " players online.")
					.queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "vote")) {

			event.getChannel()
					.sendMessage(event.getMember().getAsMention() + "http://frostyps.everythingrs.com/services/vote")
					.queue();

		}

		if (command[0].equalsIgnoreCase(PREFIX + "store")) {

			event.getChannel()
					.sendMessage(event.getMember().getAsMention() + "http://frostyps.everythingrs.com/services/store")
					.queue();

		}

		if (command[0].equalsIgnoreCase(PREFIX + "website")) {

			event.getChannel().sendMessage(event.getMember().getAsMention() + "http://frostyps.com/").queue();

		}

	}
}*/
