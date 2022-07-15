package com.arlania.world.content.collectionlog;

import com.arlania.world.entity.impl.player.Player;

public class OldCollectionLog {

	public enum CollectionLogData {

		JAD(52000, "Jad", "Bosses");

		private CollectionLogData(final int textId, final String name, final String category) {
			this.textId = textId;
			this.name = name;
			this.category = category;
		}

		private final int textId;
		private final String name, category;

	}

	private Player player;

	public void openInterface() {
		player.getPacketSender().sendInterface(65500);

	}

	private final void sendData(int tab) {
		switch (tab) {
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;

		case 5:

			break;
		case 6:

			break;
		}
	}

	private final void sendBosses() {

	}

}
