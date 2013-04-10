/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.alphahelical.bukkit.MaterialInfo;
import com.alphahelical.bukkit.anvil.VirtualAnvil;

/**
 * @author Keith Beckman
 *
 */
public class RepairItemTask implements Runnable {
	
	private Player player;
	private ItemStack oldItem;
	private VirtualAnvil anvil;
	private Block block;
	
	public RepairItemTask(Player player, ItemStack oldItem, VirtualAnvil anvil, Block block) {
		this.player = player;
		this.oldItem = oldItem;
		this.anvil = anvil;
		this.block = block;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		
		if (this.oldItem.equals(this.player.getInventory().getItemInHand())) {
			MaterialInfo mi = new MaterialInfo(this.oldItem);
			
			if(Util.canPlayerAfford(this.player, anvil.getLevelCost()) &&
					Util.canPlayerAfford(this.player, mi.getBaseMaterial(), anvil.getScrapCost())) {

				// TODO: a way to ensure atomicity and rollback?
				Util.debitPlayer(this.player, anvil.getLevelCost()); // TODO test the two debit operations
				Util.debitPlayer(this.player, mi.getBaseMaterial(), anvil.getScrapCost());				
				this.player.setItemInHand(anvil.getResult());

				this.block.getLocation().getWorld().strikeLightningEffect(this.block.getLocation());
			} else {
				String msg = String.format("You cannot pay for that repair:\n    %s", Util.costMessage(anvil));
				this.player.sendMessage(msg);
			}
			
		} else {
			this.player.sendMessage("Item removed from hand before repair was completed. You have not been charged.");
		}
		
	}

}
