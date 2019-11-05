package net.rezxis.mchosting.lobby;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.rezxis.mchosting.gui.GuiOpener;
import net.rezxis.mchosting.lobby.gui.MainGui;

public class ServerListener implements Listener {

	public static ItemStack menu;
	
	public ServerListener() {
		menu = new ItemStack(Material.NETHER_STAR);
		ItemMeta im = menu.getItemMeta();
		im.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"Main Menu");
		menu.setItemMeta(im);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		event.getPlayer().getInventory().setItem(4, menu);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().getInventory().setItem(4, menu);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() != null)
			if (event.getItem().equals(menu)) {
				GuiOpener.open(event.getPlayer(), new MainGui());
			}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
}