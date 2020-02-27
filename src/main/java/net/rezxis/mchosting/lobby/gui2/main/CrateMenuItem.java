package net.rezxis.mchosting.lobby.gui2.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.gui2.crate.CrateMenu;

public class CrateMenuItem extends GUIItem {

	public CrateMenuItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.LAVA_BUCKET);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"報酬箱"+ChatColor.LIGHT_PURPLE+"を開ける。");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new CrateMenu((Player) e.getWhoClicked()).delayShow();
		return GUIAction.CLOSE;
	}
}
