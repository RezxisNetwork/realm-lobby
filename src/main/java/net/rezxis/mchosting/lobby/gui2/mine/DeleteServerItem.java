package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.gui2.mine.delete.DeleteConfirmMenu;

public class DeleteServerItem extends GUIItem {
	
	public DeleteServerItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.DARK_RED+"サーバーを削除");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.DARK_RED+"消したサーバーは戻せません");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new DeleteConfirmMenu((Player) e.getWhoClicked(),0).delayShow();
		return GUIAction.CLOSE;
	}
}
