package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.player.DBPlayer.Rank;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class ServerTypeItem extends GUIItem {

	private CreateServerMenu menu;
	
	public ServerTypeItem(CreateServerMenu menu) {
		super(getIconWorldType(new String[] {"NORMAL","CUSTOM"},menu.stype));
		this.menu = menu;
	}
	
	private static ItemStack getIconWorldType(String[] all, String current) {
		ItemStack is = new ItemStack(Material.GRASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"サーバーの種類");
		ArrayList<String> lore = new ArrayList<>();
		for (String line : all) {
			if (line.equalsIgnoreCase(current)) {
				lore.add(ChatColor.AQUA+line);
			} else {
				lore.add(ChatColor.GRAY+line);
			}
		}
		lore.add(ChatColor.RED+"CUSTOMは上級者向けです。/realmの機能は使えませんが");
		lore.add(ChatColor.RED+"pluginなどは自由にアップロードできます。");
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		if (menu.stype.equalsIgnoreCase("NORMAL")) {
			menu.stype = "CUSTOM";
		} else if (menu.stype.equalsIgnoreCase("CUSTOM")) {
			menu.stype = "NORMAL";
		}
		DBPlayer player = Tables.getPTable().get(e.getWhoClicked().getUniqueId());
		if (player.getRank() != Rank.DEVELOPER) {
			menu.stype = "NORMAL";
			e.getWhoClicked().sendMessage(ChatColor.RED+"この機能は現在調整中です。");
		}
		return GUIAction.UPDATE;
	}
}
