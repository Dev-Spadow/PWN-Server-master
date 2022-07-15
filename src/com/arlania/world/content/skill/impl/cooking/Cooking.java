package com.arlania.world.content.skill.impl.cooking;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.Skill;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.input.impl.EnterAmountToCook;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.StarterTasks;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dailytasks.TaskType;
import com.arlania.world.entity.impl.player.Player;

public class Cooking {
	
	public static void selectionInterface(Player player, CookingData cookingData) {
		if(cookingData == null)
			return;
		player.setSelectedSkillingItem(cookingData.getRawItem());
		player.setInputHandling(new EnterAmountToCook());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(cookingData.getCookedItem()).getName()).sendInterfaceModel(1746, cookingData.getCookedItem(), 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to cook?");
	}
	
	public static void cook(final Player player, final int rawFish, final int amount) {
		final CookingData fish = CookingData.forFish(rawFish);
		if(fish == null)
			return;
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if(!CookingData.canCook(player, rawFish))
			return;
		player.performAnimation(new Animation(896));
		player.setCurrentTask(new Task(2, player, false) {
			int amountCooked = 0;
			@Override
			public void execute() {
				if(!CookingData.canCook(player, rawFish)) {
					stop();
					return;
				}
				player.performAnimation(new Animation(896));
				player.getInventory().delete(rawFish, 1);
				player.getInventory().add(10835, 5);
				player.getPointsHandler().setSkillPoints(10, true);
				if(!CookingData.success(player, 3, fish.getLevelReq(), fish.getStopBurn())) {
					player.getInventory().add(fish.getBurntItem(), 1);
					player.getPacketSender().sendMessage("You accidently burn the "+fish.getName()+".");
                    DailyTasks.INSTANCE.updateTaskProgress(TaskType.COOKING, player, fish.getBurntItem(), 1);
				} else {
					player.getInventory().add(fish.getCookedItem(), 1);
					player.getSkillManager().addExperience(Skill.COOKING, fish.getXp());
					player.getInventory().add(10835, 2);
					player.getPacketSender().sendMessage("@red@You've just received " + 10 + " skilling points.");
					player.getPointsHandler().incrementSkillPoints(10);
					if(fish == CookingData.SALMON) {
						//Achievements.finishAchievement(player, AchievementData.COOK_A_SALMON);
					} else if(fish == CookingData.ROCKTAIL) {
					} else if(fish == CookingData.SHARK) {
					}
                    DailyTasks.INSTANCE.updateTaskProgress(TaskType.COOKING, player, fish.getCookedItem(), 1);
				}
				amountCooked++;
				if(amountCooked >= amount)
					stop();
			}
			@Override
			public void stop() {
				setEventRunning(false);
				player.setSelectedSkillingItem(-1);
				player.performAnimation(new Animation(65535));
			}		
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
