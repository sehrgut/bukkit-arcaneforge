/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alphahelical.util.EnumUtil;

/**
 * @author kbeckman
 *
 */
public class ArcaneForgeCommandExecutor implements CommandExecutor {

	private enum Commands {
		COST, REPAIR, UNKNOWN;
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Commands cmdname = EnumUtil.find(Commands.class, cmd.getName(), Commands.UNKNOWN);

		switch(cmdname) {
			case COST:
				return doCost(sender, label, args);
			case REPAIR:
				return doRepair(sender, label, args);
			case UNKNOWN:
			default:
				sender.sendMessage("Don't know how it happened, but I seem to be registered for commands I don't know.");
				return false;
		}

	}

	private boolean doCost(CommandSender sender, String label, String[] args) {
		if (! (sender instanceof Player)) {
			sender.sendMessage("Error: Cannot calculate repair cost of held item for console.");
			return false;
		}
		
		return false;
	}
	
	private boolean doRepair(CommandSender sender, String label, String[] args) {
		if (! (sender instanceof Player)) {
			sender.sendMessage("Error: Makes no sense to repair held items from console.");
			return false;
		}
		
		return false;
	}
}
