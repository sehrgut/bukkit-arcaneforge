/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author kbeckman
 *
 */
public class ArcaneForgeEvent extends Event implements Cancellable {

	private Block block;

	public Block getBlock() {
		return block;
	}

	private void setBlock(Block block) {
		this.block = block;
	}

	private Player player;

	public Player getPlayer() {
		return player;
	}

	private void setPlayer(Player player) {
		this.player = player;
	}

	private ArcaneForgeActions action;

	public ArcaneForgeActions getAction() {
		return action;
	}

	private void setAction(ArcaneForgeActions action) {
		this.action = action;
	}

	private boolean cancel;
	
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return this.cancel;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#setCancelled(boolean)
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	private static final HandlerList handlers = new HandlerList();
	
	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public ArcaneForgeEvent(Player player, Block block, ArcaneForgeActions action) {
		this.setBlock(block);
		this.setPlayer(player);
		this.setAction(action);
	}
	
}
