package net.rezxis.mchosting.lobby.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.gui.EmptyItem;
import net.rezxis.mchosting.gui.ExecutableItemStack;
import net.rezxis.mchosting.gui.GuiPresenter;
import net.rezxis.mchosting.lobby.Lobby;

public class WorldManageGui implements GuiPresenter {

	@Override
	public List<ExecutableItemStack> getOptions(Player player) {
		ArrayList<ExecutableItemStack> items = new ArrayList<>();
		items.add(new InfoItem());
		items.add(new EmptyItem());
		items.add(new DownloadItem());
		return items;
	}

	@Override
	public String getLabel() {
		return "Manage world";
	}

	@Override
	public String getEmptyMessage() {
		return "Something went to worng";
	}
	
	private static ItemStack getIconDownload() {
		ItemStack is = new ItemStack(Material.BOOK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Download world from server");
		is.setItemMeta(im);
		return is;
	}
	
	private static ItemStack getIconUpload() {
		ItemStack is = new ItemStack(Material.WEB);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"Upload world to server");
		is.setItemMeta(im);
		return is;
	}
	
	private static ItemStack getIconInfo() {
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"World management");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.RED+"you can use only transfer.sh to upload worlds.");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	private class DownloadItem extends ExecutableItemStack {
		
		public DownloadItem() {
			super(getIconDownload());
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			new Thread(()->{
				player.sendMessage("uploading world..");
				//H20WorldUploadPacket packet = new H20WorldUploadPacket(player.getUniqueId().toString());
				//Lobby.instance.ws.send(new Gson().toJson(packet));
			}).start();
			return ExecReturn.CLOSE;
		}
	}
	
	private class InfoItem extends ExecutableItemStack {

		public InfoItem() {
			super(getIconInfo());
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			// TODO Auto-generated method stub
			return ExecReturn.CLOSE;
		}
	}
}
