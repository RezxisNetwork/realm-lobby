package net.rezxis.mchosting.lobby.gui2.mine;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.gui2.mine.world.WorldManageMenu;

public class WorldManageItem extends GUIItem {
	
	public WorldManageItem() {
		super(getIconManageWorld());
	}
	
	private static ItemStack getIconManageWorld() {
		ItemStack is = new ItemStack(Material.GRASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"ワールドを管理");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new WorldManageMenu((Player) e.getWhoClicked()).delayShow();
		//e.getWhoClicked().sendMessage(ChatColor.RED+"この機能は現在調整中です。 ");
		return GUIAction.CLOSE;
	}
}
