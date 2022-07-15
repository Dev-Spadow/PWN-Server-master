package com.arlania.world.content.gim;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.arlania.world.content.gim.container.Container;
import com.arlania.world.content.gim.container.Container.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Mobjunk
 *
 */

public class Group {

	/**
	 * List of all the groups
	 */
	public static List<Group> groups;
	
	/**
	 * Grabs the group if the code matches
	 * @param code
	 * @return
	 */
	public static Group forUnique(String code) {
    	for(Group group : groups)
    		if(group.uniqueCode.toLowerCase().equals(code.toLowerCase()))
    			return group;
		return null;
    }
	
	/**
	 * Grabs a group based on owner
	 * @param code
	 * @return
	 */
	public static Group forOwner(String owner) {
    	for(Group group : groups)
    		if(group.owner.equals(owner))
    			return group;
		return null;
    }
	
	/**
	 * Owner of the group
	 */
	public String owner;
	/**
	 * Unique code required to join the group
	 */
	public String uniqueCode;
	/**
	 * Public group
	 */
	public boolean public_group;
	/**
	 * Members of the group
	 */
	public ArrayList<String> members;
	/**
	 * Items within the storage
	 */
	public Container storage;
	
	public Group(String owner, String uniqueCode, boolean public_group) {
		this.owner = owner;
		this.uniqueCode = uniqueCode;
		this.public_group = public_group;
		this.members = new ArrayList<String>(5);
		this.storage = new Container(Type.ALWAYS_STACK, SharedStorage.SIZE);
		groups.add(this);
	}
	
	/**
	 * Saves all the groups
	 */
	public void save() {
		try {
			File file = new File("data/group-ironman/groups.json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter(file);
			gson.toJson(groups, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all the groups on start up
	 */
	public static void load() {
		try {
			File file = new File("data/group-ironman/groups.json");
			if(file.exists() == false)
				return;

			FileReader reader = new FileReader(file);
			groups = new Gson().fromJson(reader, new TypeToken<List<Group>>(){}.getType());

			System.out.println("Loaded "+groups.size()+" group iron man groups.");
			
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
}
