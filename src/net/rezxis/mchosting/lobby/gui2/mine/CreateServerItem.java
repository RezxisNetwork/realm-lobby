package net.rezxis.mchosting.lobby.gui2.mine;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.gui2.mine.create.CreateServerMenu;

public class CreateServerItem extends GUIItem {

	public CreateServerItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.ANVIL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"鯖を作ります");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new CreateServerMenu((Player)e.getWhoClicked(),e.getWhoClicked().getName()+"のサーバー", "DEFAULT").delayShow();
		return GUIAction.CLOSE;
	}
}
