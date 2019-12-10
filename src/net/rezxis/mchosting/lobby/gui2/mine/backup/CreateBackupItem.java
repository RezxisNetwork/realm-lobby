package net.rezxis.mchosting.lobby.gui2.mine.backup;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBBackup;
import net.rezxis.mchosting.databse.DBPlayer;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.enums.BackupAction;
import net.rezxis.mchosting.network.packet.sync.SyncBackupPacket;

public class CreateBackupItem extends GUIItem {

	public CreateBackupItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.ANVIL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"バックアップを作成");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		ArrayList<DBBackup> list = Lobby.instance.bTable.getBackups(e.getWhoClicked().getUniqueId().toString());
		DBPlayer player = Lobby.instance.pTable.get(e.getWhoClicked().getUniqueId());
		if (list.size() >= player.getRank().getMaxBackups()) {
			e.getWhoClicked().sendMessage(ChatColor.RED+"あなたのランクで作成できるバックアップ数は"+player.getRank().getMaxBackups());
			return GUIAction.NO_ACTION;
		} else {
			e.getWhoClicked().sendMessage(ChatColor.GREEN+"バックアップを作成。");
			HashMap<String,String> map = new HashMap<>();
			map.put("name", "バックアップ");
			Lobby.instance.ws.send(new Gson().toJson(new SyncBackupPacket(e.getWhoClicked().getUniqueId().toString(), BackupAction.TAKE, map)));
		}
		return GUIAction.NO_ACTION;
	}
}
