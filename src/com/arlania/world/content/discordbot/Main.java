/*
package com.arlania.world.content.discordbot;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import com.arlania.world.World;
import com.arlania.world.content.PlayerPunishment;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerSaving;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {

	public static JDA jda;
	
	
	public void initialize() {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken("NjE4NTA5NjYzMDEyNjUxMDI4.XXAXyQ.GuW2M2YCbdvfwuwSvezxsd7gnIY");
		builder.addEventListeners(new Main());
		try {
			jda = builder.build();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final String PREFIX = "::";

	*/
/**
	 * Yes i know its not a good idea to put all the logic here like that, will edit later.
	 *//*

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String channel = event.getChannel().getName();
		System.out.println("Message sent in channel: " + channel);

		String message = event.getMessage().getContentDisplay();

		String[] command = message.split("-");

		if (command[0].equalsIgnoreCase(PREFIX + "primetorvalegs")) {
			event.getChannel().sendMessage(event.getMember().getAsMention() + " check for clean shengos in your bank")
					.queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "commands")) {
			event.getChannel().sendMessage(event.getMember().getAsMention()
					+ " Command list can be found from here: https://Coronascaps.net/forums/index.php?/topic/434-command-list/")
					.queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "rules")) {
			event.getChannel().sendMessage(event.getMember().getAsMention()
					+ " Rules can be found from here: https://Pwnlite317.com/community/")
					.queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "guides")) {
			event.getChannel()
					.sendMessage(
							"Guides can be found from here: https://Pwnlite317.com/community/")
					.queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "players")) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Players online");
			embed.setColor(Color.RED);
			embed.setDescription("");
			if (World.getPlayers().size() == 0) {
				event.getChannel().sendMessage("There are currently no players online").queue();
			}
			World.getPlayers().forEach(p -> {
				embed.addField(p.getUsername(), null, false);
			});
			event.getChannel().sendMessage(embed.build()).queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "ban")) {

			if (!channel.equalsIgnoreCase("staff-chat"))
				return;

			String name = command[1];

			String reason = command[2];

			if (!PlayerSaving.playerExists(name)) {
				event.getChannel().sendMessage("Player " + name + " does not exist.").queue();
				return;
			}

			if (PlayerPunishment.banned(name)) {
				event.getChannel().sendMessage("Player " + name + " already has an active ban.").queue();
				return;
			}

			PlayerPunishment.ban(name);

			Player toBan = World.getPlayerByName(name);
			if (toBan != null) {
				World.deregister(toBan);
			}

			TextChannel textChannel = event.getGuild().getTextChannelsByName("discord-punishment-logs", true).get(0);
			textChannel
					.sendMessage(name + " was banned by " + event.getMember().getAsMention() + "\r\nReason: " + reason)
					.queue();

			event.getChannel().sendMessage("Successfully banned player " + name).queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "mute")) {

			if (!channel.equalsIgnoreCase("staff-chat"))
				return;

			String name = command[1];

			String reason = command[2];

			if (!PlayerSaving.playerExists(name)) {
				event.getChannel().sendMessage("Player " + name + " does not exist.").queue();
				return;
			}

			if (PlayerPunishment.muted(name)) {
				event.getChannel().sendMessage("Player " + name + " already has an active mute.").queue();
				return;
			}

			PlayerPunishment.mute(name);

			Player plr = World.getPlayerByName(name);

			if (plr != null) {
				plr.getPacketSender()
						.sendMessage("You have been muted by " + event.getMember().getEffectiveName() + ".");
			}

			TextChannel textChannel = event.getGuild().getTextChannelsByName("discord-punishment-logs", true).get(0);
			textChannel
					.sendMessage(name + " was muted by " + event.getMember().getAsMention() + "\r\nReason: " + reason)
					.queue();

			event.getChannel().sendMessage("Successfully muted player " + name).queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "unmute")) {

			if (!channel.equalsIgnoreCase("staff-chat"))
				return;

			String name = command[1];

			if (!PlayerSaving.playerExists(name)) {
				event.getChannel().sendMessage("Player " + name + " does not exist.").queue();
				return;
			}

			if (!PlayerPunishment.muted(name)) {
				event.getChannel().sendMessage("Player " + name + " isn't even muted.").queue();
				return;
			}

			PlayerPunishment.unmute(name);

			Player plr = World.getPlayerByName(name);

			if (plr != null) {
				plr.getPacketSender()
						.sendMessage("You have been unmuted by " + event.getMember().getEffectiveName() + ".");
			}

			event.getChannel().sendMessage("Successfully unmuted player " + name).queue();
		}

		if (command[0].equalsIgnoreCase(PREFIX + "vote")) {

			event.getChannel()
					.sendMessage(event.getMember().getAsMention() + " Vote for Pwnlite @ https://Pwnlite317.everythingrs.com/services/vote")
					.queue();

		}

		if (command[0].equalsIgnoreCase(PREFIX + "store")) {

			event.getChannel()
					.sendMessage(event.getMember().getAsMention() + " Support Pwnlite @ https://Pwnlite317.everythingrs.com/services/store")
					.queue();

		}

		if (command[0].equalsIgnoreCase(PREFIX + "forum")) {

			event.getChannel()
					.sendMessage(event.getMember().getAsMention() + " Pwnlite forum link: https://Pwnlite317.com/community/")
					.queue();

		}

	}
}
*/
