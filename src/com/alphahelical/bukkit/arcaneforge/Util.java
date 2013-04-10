package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {

	private Util () {}
	
	public static boolean isArcaneForge(Block b) {
		return b.getType().equals(Config.getForgeMaterial());
	}
	
	public static boolean canPlayerAfford(Player p, Material m, int amount) {
		return p.getInventory().contains(m, amount);
	}
	
	public static boolean canPlayerAfford(Player p, int levels) {
		return p.getLevel() >= levels;
	}
	
	public static void debitPlayer(Player p, int levels) {
		p.setLevel(p.getLevel() - levels);
	}
	
	public static void debitPlayer(Player p, Material m, int amount) {
		p.getInventory().remove(new ItemStack(m, amount));
	}
}
