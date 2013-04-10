package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.alphahelical.bukkit.MaterialInfo;
import com.alphahelical.bukkit.anvil.VirtualAnvil;

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
		p.getInventory().removeItem(new ItemStack(m, amount));
	}

	public static String costMessage(VirtualAnvil anvil) {
		// TODO: this won't work if VirtualAnvil ever returns a different Material than its input
		MaterialInfo mi = new MaterialInfo(anvil.getResult().getType());
		
		return String.format("Cost to repair from %d to %d: %d lvl, %d %s",
				anvil.getOldRemainingDurability(), anvil.getNewRemainingDurability(),
				anvil.getLevelCost(), anvil.getScrapCost(),
				mi.getBaseMaterial().toString().toLowerCase()
				);
	}

}
