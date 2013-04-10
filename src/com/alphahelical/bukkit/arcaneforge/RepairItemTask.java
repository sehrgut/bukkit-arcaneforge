/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.alphahelical.bukkit.MaterialInfo;

/**
 * @author Keith Beckman
 *
 */
public class RepairItemTask implements Runnable {
	
	private Player player;
	private ArcaneRepairer repair;
	private Block block;
	
	public RepairItemTask(Player player, ArcaneRepairer repair, Block block) {
		if(player == null) throw new NullArgumentException("player");
		if(repair == null) throw new NullArgumentException("repair");
		
		this.player = player;
		this.repair = repair;
		this.block = block;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		
		if (this.repair.getInput().equals(this.player.getInventory().getItemInHand())) {
			MaterialInfo mi = new MaterialInfo(this.repair.getInput());
			
			if(Util.canPlayerAfford(this.player, this.repair.getLevelCost()) &&
					Util.canPlayerAfford(this.player, mi.getBaseMaterial(), this.repair.getScrapCost())) {

				// TODO: a way to ensure atomicity and rollback?
				Util.debitPlayer(this.player, this.repair.getLevelCost()); // TODO test the two debit operations
				Util.debitPlayer(this.player, mi.getBaseMaterial(), this.repair.getScrapCost());				
				this.player.setItemInHand(this.repair.getResult());

//				  TODO: configure lightning?
				Location strikeLoc = null;
				if (this.block == null) {
					strikeLoc = this.player.getLocation();
				} else {
					strikeLoc = this.block.getLocation();
				}
				strikeLoc.getWorld().strikeLightningEffect(this.block.getLocation());
				
			} else {
				String msg = String.format("You cannot pay for that repair:\n    %s", this.repair.getCostMessage());
				this.player.sendMessage(msg);
			}
			
		} else {
			this.player.sendMessage("Item removed from hand before repair was completed. You have not been charged.");
		}
		
	}

}
