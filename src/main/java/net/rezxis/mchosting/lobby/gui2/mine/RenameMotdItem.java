package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.wesjd.anvilgui.AnvilGUI;

public class RenameMotdItem extends GUIItem {
	
	public RenameMotdItem(DBServer server) {
		super(getIcon(server));
	}
	
	private static ItemStack getIcon(DBServer server) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Motd : "+server.getMotd());
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GREEN+"変更");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
			public void run() {
				new AnvilGUI.Builder()
				.onClose(pl -> {
				})
				.onComplete((pl,text) -> {
					if (text == null) {
						return AnvilGUI.Response.text("Motdを入れてください。");
					}
					DBServer server = Tables.getSTable().get(pl.getUniqueId());
					server.setMotd(text.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR)));
					server.update();
					new MyRealmMenu(pl).delayShow();
					return AnvilGUI.Response.close();
				})
				.text("Motdを入れてください。")
				.plugin(Lobby.instance)
				.open((Player) e.getWhoClicked());
			}
		}, 2);
		return GUIAction.CLOSE;
	}
}
