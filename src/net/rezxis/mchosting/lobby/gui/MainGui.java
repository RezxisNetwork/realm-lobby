package net.rezxis.mchosting.lobby.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.EmptyItem;
import net.rezxis.mchosting.gui.ExecutableItemStack;
import net.rezxis.mchosting.gui.GuiOpener;
import net.rezxis.mchosting.gui.GuiPresenter;
import net.rezxis.mchosting.lobby.Lobby;

public class MainGui implements GuiPresenter {

	public static MainGui gui;
	
	static {
		gui = new MainGui();
	}
	
	@Override
	public List<ExecutableItemStack> getOptions(Player player) {
		ArrayList<ExecutableItemStack> items = new ArrayList<>();
		items.add(new OpenServerlistItem());
		items.add(new EmptyItem());
		items.add(new EmptyItem());
		items.add(new EmptyItem());
		items.add(new OpenManagementItem());
		return items;
	}

	@Override
	public String getLabel() {
		return "Main Menu";
	}

	@Override
	public String getEmptyMessage() {
		return "Something went to worng!";
	}
	
	public static ItemStack getIconManager() {
		ItemStack is = new ItemStack(Material.COMMAND);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Manager for you server");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GOLD+"Click to open manager");
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconServerList() {
		ItemStack is = new ItemStack(Material.COMPASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Servers list");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GOLD+"Click to open servers list");
		is.setItemMeta(im);
		return is;
	}
	
	private class OpenServerlistItem extends ExecutableItemStack {
		public OpenServerlistItem() {
			super(getIconServerList());
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, 
					new Runnable() {
				public void run() {
					GuiOpener.open(player, new ServersGui());
			}},2);
			return ExecReturn.CLOSE;
		}
	}
	
	private class OpenManagementItem extends ExecutableItemStack {
		public OpenManagementItem() {
			super(getIconManager());
		}
		
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, 
					new Runnable() {
				public void run() {
			GuiOpener.open(player, new ServerManagementGui(0,null,"DEFAULT"));
			}},2);
			return ExecReturn.CLOSE;
		}
	}
}
