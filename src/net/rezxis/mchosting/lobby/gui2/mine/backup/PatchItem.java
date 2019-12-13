package net.rezxis.mchosting.lobby.gui2.mine.backup;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.DBBackup;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.enums.BackupAction;
import net.rezxis.mchosting.network.packet.sync.SyncBackupPacket;

public class PatchItem extends GUIItem {

	private DBBackup obj;
	
	public PatchItem(DBBackup obj) {
		super(getIcon());
		this.obj = obj;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.WATER_LILY);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"バックアップで復元。");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		e.getWhoClicked().sendMessage(ChatColor.GREEN+"バックアップで復元中。");
		HashMap<String,String> map = new HashMap<>();
		map.put("id",obj.getId()+"");
		SyncBackupPacket p = new SyncBackupPacket(obj.getOwner(), BackupAction.PATCH, map);
		Lobby.instance.ws.send(new Gson().toJson(p));
		return GUIAction.CLOSE;
	}
}
