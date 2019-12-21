package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.ShopItem;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.sync.SyncDeleteServer;

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
		DBServer server = Lobby.instance.sTable.get(e.getWhoClicked().getUniqueId());
		int coin = 0;
		for (ShopItem item : server.getShop().getItems()) {
			coin += item.getEarned();
		}
		DBPlayer player = Lobby.instance.pTable.get(e.getWhoClicked().getUniqueId());
		player.addCoin(coin);
		player.update();
		SyncDeleteServer packet = new SyncDeleteServer(e.getWhoClicked().getUniqueId().toString());
		Lobby.instance.ws.send(new Gson().toJson(packet));
		e.getWhoClicked().sendMessage(ChatColor.RED+"削除されました。");
		return GUIAction.CLOSE;
	}
}
