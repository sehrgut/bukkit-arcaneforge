/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.alphahelical.bukkit.PlayerInteractActions;
import static com.alphahelical.bukkit.PlayerInteractActions.*;

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
		
		if (! e.hasBlock() || ! e.hasItem() || ! Util.isArcaneForge(e.getClickedBlock()))
			return;
		
		PlayerInteractActions a = null;
		
		switch(e.getAction()) {
			case LEFT_CLICK_BLOCK:
				a = e.getPlayer().isSneaking() ? SNEAK_HIT : HIT;
				break;
			case RIGHT_CLICK_BLOCK:
				a = e.getPlayer().isSneaking() ? SNEAK_USE : USE;
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

		ArcaneRepairer rep = new ArcaneRepairer(p.getItemInHand());
		
		if (! rep.canRepair()) {
			e.getPlayer().sendMessage("You cannot repair that.");
			e.setCancelled(true);
			return;
		}
		
		if(rep.getResult() == null) {
			p.sendMessage("This item cannot be repaired.");
			return;
		}
		
		if(rep.getLevelCost() < Config.getMinLevel()) {
			p.sendMessage("This item is not arcane enough to repair here.");
			return;
		}
		
		if(rep.getLevelCost() > Config.getMaxLevel()) {
			p.sendMessage("This item is too arcane even for this forge.");
			return;
		}			
		
		switch(e.getAction()) {
			case HIT:
			case SNEAK_HIT:
				p.sendMessage(rep.getCostMessage());
				break;
			case USE:
			case SNEAK_USE:
				if(Util.canPlayerAfford(p, rep.getLevelCost()) &&
						Util.canPlayerAfford(p, rep.getBaseMaterial(), rep.getScrapCost())) {
					
					// Schedule RepairItemTask, because setting held item during RIGHT_CLICK_BLOCK fails.
					// This appears to be a bug in CraftBukkit, with no known solution.
					Runnable task = new RepairItemTask(p, rep, e.getBlock());
					this.getPlugin().getServer().getScheduler().runTask(this.getPlugin(), task);
				} else {
					String msg = String.format("You cannot pay for that repair:\n    %s", rep.getCostMessage());
					p.sendMessage(msg);
				}
				break;
		}
	}
	
}
