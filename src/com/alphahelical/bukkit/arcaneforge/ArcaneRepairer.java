/**
 * 
 */
package com.alphahelical.bukkit.arcaneforge;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.alphahelical.bukkit.MaterialInfo;
import com.alphahelical.bukkit.anvil.VirtualAnvil;

/**
 * @author kbeckman
 *
 */
public class ArcaneRepairer {

	public ArcaneRepairer(ItemStack input) {
		if(input == null) throw new NullArgumentException("input");
		
		this.input = input;
		this.anvil = this.makeAnvil();
	}
	
	private ItemStack input;
	public ItemStack getInput() {
		return this.input;
	}
	
	public ItemStack getResult() {
		return this.anvil == null ? null : this.anvil.getResult();
	}
	
	public Integer getScrapCost() {
		return this.anvil == null ? null : this.anvil.getScrapCost();
	}
	
	public Integer getLevelCost() {
		return this.anvil == null ? null : this.anvil.getLevelCost();
	}
	
	public boolean canRepair() {
		return (this.getBaseMaterial() != null);
	}
	
	private MaterialInfo materialInfo;
	public Material getBaseMaterial() {
		if (this.materialInfo == null)
			this.materialInfo = new MaterialInfo(this.getInput());
		
		return this.materialInfo.hasBaseMaterial() ? this.materialInfo.getBaseMaterial() : null;
	}
	
	private VirtualAnvil anvil;
	private VirtualAnvil makeAnvil() {
		VirtualAnvil anvil = null;
		MaterialInfo mi = new MaterialInfo(this.getInput());
		
		if (this.canRepair()) {
			ItemStack scrap = new ItemStack(mi.getBaseMaterial(), Config.getMaxScrapAmount());
			anvil = new VirtualAnvil(/* Player */ null, this.getInput(), scrap, /* new name */ null);
		}
		return anvil;
	}
	
	public String getCostMessage() {
		return Util.costMessage(this.anvil);
	}
}
