package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.player.DBPlayer.Rank;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;

public class ServerTypeItem extends GUIItem {

	private String stype;
	private String type;
	private String name;
	
	public ServerTypeItem(String name, String type, String stype) {
		super(getIconWorldType(new String[] {"NORMAL","CUSTOM"},stype));
		this.stype = stype;
		this.type = type;
		this.name = name;
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
		if (stype.equalsIgnoreCase("NORMAL")) {
			stype = "CUSTOM";
		} else if (stype.equalsIgnoreCase("CUSTOM")) {
			stype = "NORMAL";
		}
		DBPlayer player = Lobby.instance.pTable.get(e.getWhoClicked().getUniqueId());
		if (player.getRank() != Rank.DEVELOPER) {
			stype = "NORMAL";
			e.getWhoClicked().sendMessage(ChatColor.RED+"この機能は現在調整中です。");
		}
		new CreateServerMenu((Player)e.getWhoClicked(), name, type, stype).delayShow();
		return GUIAction.CLOSE;
	}
}
