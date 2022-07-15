package com.arlania.world.entity.impl.npc;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class InstancedNpc extends NPC {

	private int respawnTimer;
	private Player instanceOwner;
	private long startTimer;
	private long startTimer2;
	private boolean aoeNpc = false;

	public InstancedNpc(int id, Position position) {
		super(id, position);
	}

	public void configure(Player owner, int secondsTilRespawn) {
		this.instanceOwner = owner;
		if (instanceOwner.getAoEInstance() != null)
			aoeNpc = true;
		this.respawnTimer = secondsTilRespawn;
	}

	public void spawnAndMonitor() {
		startTimer = System.nanoTime();
		InstancedNpc holder = this;
		holder.setSpawnedFor(instanceOwner);
		World.register(holder);
		TaskManager.submit(new Task(2, holder, false) {
			@Override
			public void execute() {
				if (aoeNpc && instanceOwner.getAoEInstance() == null) {
					World.deregister(holder);
					stop();
					return;
				}
				final long elapsedTime = System.nanoTime() - startTimer;
				double seconds = (double) elapsedTime / 1_000_000_000.0;
				if ((int) seconds >= 2) { // checks every 5 secs to see if it needs to despawn
					if (World.getNpcs().contains(holder) || !holder.isDying()) { // if alive
						if (!World.getPlayers().contains(instanceOwner)) { // if player disconnected
							World.deregister(holder);
							stop();
							return;
						}
						if (!instanceOwner.getPosition().isWithinDistance(holder.getPosition(), 100)) { // if player
																										// left location
							if (instanceOwner.getAoEInstance() == null) {
								World.deregister(holder);
								// System.out.println("Your " + holder.getDefinition().getName() + " instance
								// has despawned because you are too far away.");
								stop();
							} else if (instanceOwner.getAoEInstance() != null) {
								if(instanceOwner.getAoEInstance().getRespawning()) {
									try {
										Thread.sleep(5000);
										instanceOwner.getAoEInstance().setRespawning(false);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									World.deregister(holder);
									// System.out.println("Your " + holder.getDefinition().getName() + " instance
									// has despawned because you are too far away.");
									stop();
								}
							}
						} else {
							startTimer = System.nanoTime();
						}
					}
				}
			}
		});
	}

	public void runRespawn() {
		World.deregister(this);
		startTimer2 = System.nanoTime();
		InstancedNpc holder = this;
		TaskManager.submit(new Task(2, holder, false) {
			@Override
			public void execute() {
				final long elapsedTime = System.nanoTime() - startTimer2;
				double seconds = (double) elapsedTime / 1_000_000_000.0;
				if ((int) seconds >= respawnTimer) {
					holder.restoreHp();
					holder.setPosition(holder.getDefaultPosition());
					spawnAndMonitor();
					stop();
				}
			}
		});
	}
}
