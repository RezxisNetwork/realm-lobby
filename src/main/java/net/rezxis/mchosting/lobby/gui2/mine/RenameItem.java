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
import net.rezxis.mchosting.gui.anvil.AnvilGUI;
import net.rezxis.mchosting.lobby.Lobby;

public class RenameItem extends GUIItem {


	public RenameItem(DBServer server) {
		super(getIcon(server));
	}
	
	private static ItemStack getIcon(DBServer server) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"サーバー名 : "+server.getDisplayName());
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
						return AnvilGUI.Response.text("サーバー名を入れてください。");
					}
					String[] denied = new String[] {"/","\\","."};
					if (Tables.getSTable().getServerByName(text) != null) {
						pl.sendMessage(ChatColor.RED+"その名前は既に使われています。");
						return AnvilGUI.Response.close();
					} else if (text.equalsIgnoreCase("lobby")) {
						pl.sendMessage(ChatColor.RED+"lobbyはサーバー名に使えません。");
						return AnvilGUI.Response.close();
					}
					for (String deny : denied) {
						if (text.contains(deny)) {
							pl.sendMessage(ChatColor.RED+"特殊記号などは使用禁止されています。");
							return AnvilGUI.Response.close();
						}
					}
					DBServer server = Tables.getSTable().get(pl.getUniqueId());
					server.setDisplayName(text.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR)));
					server.update();
					new MyRealmMenu(pl).delayShow();
					return AnvilGUI.Response.close();
				})
				.text("サーバー名を入れてください。")
				.plugin(Lobby.instance)
				.open((Player) e.getWhoClicked());
			}
		}, 2);
		return GUIAction.CLOSE;
	}
}
