/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import com.alphahelical.bukkit.MaterialInfo;
import com.alphahelical.bukkit.anvil.VirtualAnvil;

/**
 * @author kbeckman
 *
 */
public class ArcaneForgeListener implements Listener {

	private ArcaneForgePlugin plugin;
	
	private ArcaneForgePlugin getPlugin() {
		return plugin;
	}

	private void setPlugin(ArcaneForgePlugin plugin) {
		this.plugin = plugin;
	}

	public ArcaneForgeListener(ArcaneForgePlugin plugin) {
		this.setPlugin(plugin);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.isCancelled())
			return;
		
		if (! e.hasBlock() || ! e.hasItem() || e.getPlayer().isSneaking() || ! Util.isArcaneForge(e.getClickedBlock()))
			return;
		
		ArcaneForgeActions a = null;
		
		switch(e.getAction()) {
			case LEFT_CLICK_BLOCK:
				a = ArcaneForgeActions.COST;
				break;
			case RIGHT_CLICK_BLOCK:
				a = ArcaneForgeActions.REPAIR;
				break;
			default:
				return;
		}
		
		ArcaneForgeEvent evt = new ArcaneForgeEvent(e.getPlayer(), e.getClickedBlock(), a);
		this.getPlugin().getServer().getPluginManager().callEvent(evt);
		
		if (evt.isCancelled())
			e.setCancelled(true);
		
	}
	
	@EventHandler
	public void onArcaneForge(ArcaneForgeEvent e) {
		if (e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		MaterialInfo mi = new MaterialInfo(item);
		
		if (! mi.hasBaseMaterial()) {
			e.getPlayer().sendMessage("You cannot repair that.");
			e.setCancelled(true);
			return;
		}
		
		ItemStack scrap = new ItemStack(mi.getBaseMaterial(), 1); // TODO: do greater repair level per try?
		
		VirtualAnvil anvil = new VirtualAnvil(e.getPlayer(), item, scrap, null);

		String msgCost = String.format("Cost to repair from %d to %d: %d lvl, %d %s",
				anvil.getOldRemainingDurability(), anvil.getNewRemainingDurability(),
				anvil.getLevelCost(), anvil.getScrapCost(),
				mi.getBaseMaterial().toString().toLowerCase()
				);

		switch(e.getAction()) {
			case COST:
				p.sendMessage(msgCost);
				break;
			case REPAIR:
				if(Util.canPlayerAfford(p, anvil.getLevelCost()) &&
						Util.canPlayerAfford(p, mi.getBaseMaterial(), anvil.getScrapCost())) {
					p.setItemInHand(anvil.getResult());
					Util.debitPlayer(p, anvil.getLevelCost());
					Util.debitPlayer(p, mi.getBaseMaterial(), anvil.getScrapCost());
					e.getBlock().getLocation().getWorld().strikeLightningEffect(e.getBlock().getLocation());
				} else {
					String msg = String.format("You cannot pay for that repair:\n%s", msgCost);
					p.sendMessage(msg);
					e.setCancelled(true);
				}
				break;
		}
	}
	
}
