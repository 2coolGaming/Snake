package com.schiebros.snake;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Deprecated
public class GameParser {

	private GameParser() {}
	
	@SuppressWarnings("all")
	public synchronized static String retrieveNameFromFile(File f) throws FileNotFoundException {
			Scanner scan = new Scanner(f);
			while (scan.hasNextLine()) {
				String current = scan.nextLine();
				if (current.toLowerCase().startsWith("#")) {
					continue;
				}
				if (current.toLowerCase().startsWith("name: ")) {
					return current.replaceFirst("name: ", "");
				}
			}
			return null;
	}
	
	@SuppressWarnings("all")
	public synchronized static String retrieveVersionFromFile(File f) throws FileNotFoundException {
		Scanner scan = new Scanner(f);
		
		while (scan.hasNextLine()) {
			String current = scan.nextLine();
			if (current.toLowerCase().startsWith("#"))
				continue;
			if (current.toLowerCase().startsWith("version: "))
				return current.replaceFirst("version: ", "");
		}

		return null;
	}
	
}
