package com.arlania.world.content.discordbot.command;

import java.util.ArrayList;
import java.util.List;

//import io.github.classgraph.ClassGraph;
//import io.github.classgraph.ClassInfo;
//import io.github.classgraph.ScanResult;

public class Main<T> {

	/*private List<Command> loadCommands(List<Command> command) {
		return loadClassesImplementing(Command.class, false, "dirpath");
	}*/

	/**
	 * Returns loaded instances of all classes found implementing the provided class
	 * in the provided packages.
	 *
	 * @param clazz     The class to look for.
	 * @param recursive Whether or not it should recurse through packages.
	 * @param dirs      The packages to look in.
	 * @param <T>       The type we want to look for and return.
	 * @return An [ArrayList] of instantiated objects. </T>
	 */
//	private List<T> loadClassesImplementing(Class<T> clazz, boolean recursive, String... dirs) {
//		List<T> classes = new ArrayList<>();
//
//		ClassGraph cg = new ClassGraph().enableAllInfo();
//		cg = recursive ? cg.whitelistPackages(dirs) : cg.whitelistPackagesNonRecursive(dirs);
//
//		ScanResult classesScanned = cg.scan();
//		for (ClassInfo classInfo : classesScanned.getClassesImplementing(clazz.getCanonicalName())) {
//			try {
//				classes.add(clazz.cast(classInfo.loadClass().newInstance()));
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return classes;
//
//	}
}
