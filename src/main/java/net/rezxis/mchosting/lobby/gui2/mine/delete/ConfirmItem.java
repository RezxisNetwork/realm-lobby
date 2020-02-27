package net.rezxis.mchosting.lobby.gui2.mine.delete;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.ShopItem;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.sync.SyncDeleteServer;

public class ConfirmItem extends GUIItem {

	private int phase;
	
	public ConfirmItem(int phase) {
		super(getIcon());
		this.phase = phase;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"削除");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		if (phase == 3) {
			DBServer server = Tables.getSTable().get(e.getWhoClicked().getUniqueId());
			int coin = 0;
			for (ShopItem item : server.getShop().getItems()) {
				coin += item.getEarned();
			}
			DBPlayer player = Tables.getPTable().get(e.getWhoClicked().getUniqueId());
			player.addCoin(coin);
			player.update();
			SyncDeleteServer packet = new SyncDeleteServer(e.getWhoClicked().getUniqueId().toString());
			Lobby.instance.ws.send(new Gson().toJson(packet));
			e.getWhoClicked().sendMessage(ChatColor.RED+"削除されました。");
		} else {
			new DeleteConfirmMenu((Player) e.getWhoClicked(),phase+1).delayShow();
		}
		return GUIAction.CLOSE;
	}
}
