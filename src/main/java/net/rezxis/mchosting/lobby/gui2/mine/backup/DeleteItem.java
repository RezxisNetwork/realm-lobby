package net.rezxis.mchosting.lobby.gui2.mine.backup;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.internal.DBBackup;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.enums.BackupAction;
import net.rezxis.mchosting.network.packet.sync.SyncBackupPacket;

public class DeleteItem extends GUIItem {

	private DBBackup obj;
	
	public DeleteItem(DBBackup obj) {
		super(getIcon());
		this.obj = obj;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.BARRIER);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RED+"削除");
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.RED+"一度削除すると戻せません。");
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		HashMap<String,String> map = new HashMap<>();
		map.put("id", obj.getId()+"");
		SyncBackupPacket p = new SyncBackupPacket(obj.getOwner(),BackupAction.DELETE,map);
		Lobby.instance.ws.send(new Gson().toJson(p));
		e.getWhoClicked().sendMessage(ChatColor.RED+"削除中");
		return GUIAction.CLOSE;
	}
}
