package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.server.Version;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.sync.SyncCreateServer;

public class DoCreateServerItem extends GUIItem {

	private CreateServerMenu menu;
	
	public DoCreateServerItem(CreateServerMenu menu) {
		super(getIcon());
		this.menu = menu;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"作成");
		ArrayList<String> list = new ArrayList<>();
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		SyncCreateServer packet = new SyncCreateServer(e.getWhoClicked().getUniqueId().toString(), menu.name, menu.type, menu.stype, menu.version.name());
		Lobby.instance.ws.send(new Gson().toJson(packet));
		e.getWhoClicked().sendMessage(ChatColor.AQUA+"作成中");
		return GUIAction.CLOSE;
	}
}
