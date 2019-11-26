package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.gui.anvil.AnvilGUI;

public class ServerNameItem extends GUIItem {

	private String name;
	private String type;
	
	public ServerNameItem(String name, String type) {
		super(getIcon(name));
		this.name = name;
		this.type = type;
	}
	
	private static ItemStack getIcon(String name) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"サーバー名 : "+name);
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GREEN+"変更");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		e.getWhoClicked().closeInventory();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
			public void run() {
				new AnvilGUI.Builder()
				.onClose(pl -> {
				})
				.onComplete((pl,text) -> {
					if (text == null || text.equalsIgnoreCase("")) {
						return AnvilGUI.Response.text(name);
					}
					String[] denied = new String[] {"/","\\","."};
					if (Lobby.instance.sTable.getServerByName(text) != null) {
						pl.sendMessage(ChatColor.RED+"その名前は既に使われています。");
						return AnvilGUI.Response.close();
					} else if (text.contains("lobby")) {
						pl.sendMessage(ChatColor.RED+"lobbyはサーバー名に使えません。");
						return AnvilGUI.Response.close();
					}
					for (String deny : denied) {
						if (text.contains(deny)) {
							pl.sendMessage(ChatColor.RED+"特殊記号などは使用禁止されています。");
							return AnvilGUI.Response.close();
						}
					}
					Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
						public void run() {
							new CreateServerMenu(pl,text, type).delayShow();
						}
					}, 2);
					return AnvilGUI.Response.close();
				})
				.text(name)
				.plugin(Lobby.instance)
				.open((Player) e.getWhoClicked());
			}
		}, 2);
		return GUIAction.CLOSE;
	}
}