package com.arlania.world.content.newminigames;

/*
 * @author Skwishy
 * If a controller is used even after the player
 * logs out we must specify the Controller here
 * 
 * Example: GodWardsDungeonController
 * 1) Is not a GodWardsDungeonController
 * 2) Player can loggout and log in
 * 3) Must save LiveController name to player
 * 4) Must load LiveController via name
 * 5) code: LiveControllers.valueOf("GodWardsDungeonController");
 */

public enum LiveControllers {

	FFA(null);

	LiveControllers(Controller controller) {
		this.controller = controller;
	}

	private Controller controller;

	public Controller getController() {
		return this.controller;
	}

}
