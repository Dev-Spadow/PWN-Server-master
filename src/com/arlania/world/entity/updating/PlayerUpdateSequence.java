package com.arlania.world.entity.updating;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NpcAggression;
import com.arlania.world.entity.impl.player.Player;

public class PlayerUpdateSequence implements UpdateSequence<Player> {

	/** Used to block the game thread until updating is completed. */
	private final Phaser synchronizer;
	/** The thread pool that will update players in parallel. */
	private final ExecutorService updateExecutor;

	/**
	 * Create a new {@link PlayerUpdateSequence}.
	 *
	 * @param synchronizer
	 *            used to block the game thread until updating is completed.
	 * @param updateExecutor
	 *            the thread pool that will update players in parallel.
	 */
	public PlayerUpdateSequence(Phaser synchronizer, ExecutorService updateExecutor) {
		this.synchronizer = synchronizer;
		this.updateExecutor = updateExecutor;
	}

	@Override
	public void executePreUpdate(Player t) {
		if (!t.getSession().getChannel().isConnected()) {
			World.deregister(t);
		}
		try {
			t.getSession().handleQueuedMessages();
			t.process();
			if (t.getWalkToTask() != null)
				t.getWalkToTask().tick();
			t.getMovementQueue().sequence();
			NpcAggression.target(t);
		} catch (Exception e) {
			e.printStackTrace();
			World.deregister(t);
		}
	}

	@Override
	public void executeUpdate(Player player) {
		updateExecutor.execute(() -> {
			try {
				synchronized (player) {
					PlayerUpdating.update(player);
					NPCUpdating.update(player);
				}
			} catch (Exception e) {
				e.printStackTrace();
				World.deregister(player);
			} finally {
				synchronizer.arriveAndDeregister();
			}
		});
	}

	@Override
	public void executePostUpdate(Player t) {
		try {
			PlayerUpdating.resetFlags(t);
		} catch (Exception e) {
			e.printStackTrace();
			World.deregister(t);
		}
	}
}
