package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class Config {
	private Config() {}
	
	private static Plugin plugin;
	protected static Plugin getPlugin() {
		if (plugin == null)
			plugin = Bukkit.getServer().getPluginManager().getPlugin("Arcane Forge");
		return plugin;
	}

	private static Material defaultForgeMaterial = Material.GOLD_BLOCK;
	public static Material getForgeMaterial() {
		String mat = getPlugin().getConfig().getString("forge-material").toUpperCase();
		Material out = Material.matchMaterial(mat);

		if (out == null) {
			out = defaultForgeMaterial;
			getPlugin().getLogger().severe(String.format("Material '%s' invalid: using '%s' instead.", mat, out));
		}
		
		return out;
	}
	
	public static int getMinLevel() {
		return getPlugin().getConfig().getInt("min-level");
	}
	
	public static int getMaxLevel() {
		return getPlugin().getConfig().getInt("max-level");
	}
	
	public static int getMaxScrapAmount() {
		return getPlugin().getConfig().getInt("max-scrap-amount");
	}
	
}
