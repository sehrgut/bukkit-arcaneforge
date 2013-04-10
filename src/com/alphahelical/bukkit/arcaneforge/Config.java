package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.Material;

public class Config {
	private Config() {}
	
	public static Material getForgeMaterial() {
		return Material.GOLD_BLOCK;
	}
	
	public static int getMinLevel() {
		return 41;
	}
	
	public static int getMaxLevel() {
		return 100;
	}
	
	
}
