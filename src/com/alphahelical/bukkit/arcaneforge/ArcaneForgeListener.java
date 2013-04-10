/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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

		if(anvil.getResult() == null) {
			p.sendMessage("This item cannot be repaired.");
			return;
		}
		
		if(anvil.getLevelCost() < Config.getMinLevel()) {
			p.sendMessage("This item is not arcane enough to repair here.");
			return;
		}
		
		if(anvil.getLevelCost() > Config.getMaxLevel()) {
			p.sendMessage("This item is too arcane even for this forge.");
			return;
		}			
		
		this.getPlugin().getLogger().info(String.format("VirtualAnvil:\nold: %s\nnew: %s\nlvl: %d\nscrap: %d\n\n",
				item, anvil.getResult(), anvil.getLevelCost(), anvil.getScrapCost()));
		
		switch(e.getAction()) {
			case COST:
				p.sendMessage(Util.costMessage(anvil));
				break;
			case REPAIR:
				if(Util.canPlayerAfford(p, anvil.getLevelCost()) &&
						Util.canPlayerAfford(p, mi.getBaseMaterial(), anvil.getScrapCost())) {
					
					// Schedule RepairItemTask, because setting held item during RIGHT_CLICK_BLOCK fails.
					// This appears to be a bug in CraftBukkit, with no known solution.
					Runnable task = new RepairItemTask(p, item, anvil, e.getBlock());
					this.getPlugin().getServer().getScheduler().runTask(this.getPlugin(), task);
				} else {
					String msg = String.format("You cannot pay for that repair:\n    %s", Util.costMessage(anvil));
					p.sendMessage(msg);
				}
				break;
		}
	}
	
}
