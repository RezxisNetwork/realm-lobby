package net.rezxis.mchosting.lobby.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.databse.ServerStatus;
import net.rezxis.mchosting.gui.EmptyItem;
import net.rezxis.mchosting.gui.ExecutableItemStack;
import net.rezxis.mchosting.gui.GuiOpener;
import net.rezxis.mchosting.gui.GuiPresenter;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.lobby.ServerListener;
import net.rezxis.mchosting.network.packet.sync.SyncCreateServer;
import net.rezxis.mchosting.network.packet.sync.SyncDeleteServer;
import net.rezxis.mchosting.network.packet.sync.SyncStartServer;
import net.rezxis.mchosting.network.packet.sync.SyncStopServer;
import net.wesjd.anvilgui.AnvilGUI;

public class ServerManagementGui implements GuiPresenter {
	
	private int i;
	private String name;
	private String type;
	
	public ServerManagementGui(int i, String name, String type) {
		this.i = i;
		this.name = name;
		this.type = type;
	}
	
	@Override
	public List<ExecutableItemStack> getOptions(Player player) {
		ArrayList<ExecutableItemStack> items = new ArrayList<>();
		DBServer server = Lobby.instance.sTable.get(player.getUniqueId());
		if (this.name != null) {
			items.add(new ServerCreateSign());
			items.add(new WorldTypeItem(type));
			items.add(new EmptyItem());
			items.add(new EmptyItem());
			items.add(new DoServerCreate());
		} else if (i == 0) {
			if (server == null) {
				items.add(new EmptyItem());
				items.add(new EmptyItem());
				items.add(new EmptyItem());
				items.add(new EmptyItem());
				items.add(new ServerCreate());
			} else {
				items.add(new StatusItem(server));
				if (server.getStatus() == ServerStatus.STOP) {
					items.add(new WorldManageItem());
					items.add(new ServerRenameSign(server));
					items.add(new ServerEditMotd(server));
					items.add(new EmptyItem());
					items.add(new EmptyItem());
					items.add(new EmptyItem());
					items.add(new EmptyItem());
					//delete item
					items.add(new DeleteItem());
				} else if (server.getStatus() == ServerStatus.RUNNING) {
					items.add(new EmptyItem());
					items.add(new EmptyItem());
					items.add(new EmptyItem());
					items.add(new ConnectItem());
				}
			}
		}
		return items;
	}

	@Override
	public String getLabel() {
		return "Manager for your server";
	}

	@Override
	public String getEmptyMessage() {
		return "Something went to worng";
	}
	
	public static ItemStack getIconRename(DBServer server) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"サーバー名 : "+server.getDisplayName());
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GREEN+"変更");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconMotd(DBServer server) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Motd : "+server.getMotd());
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GREEN+"変更");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconManageWorld() {
		ItemStack is = new ItemStack(Material.GRASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"manage world");
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconServerCreate() {
		ItemStack is = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Create server");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GOLD+"サーバーを作成");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconServerStatus(DBServer server) {
		ItemStack is = null;
		if (server.getStatus() == ServerStatus.RUNNING) {
			is = new ItemStack(Material.EMERALD_BLOCK);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"server : online");
			ArrayList<String> list = new ArrayList<>();
			list.add(ChatColor.RED+"サーバーを停止する");
			im.setLore(list);
			is.setItemMeta(im);
		} else if (server.getStatus() == ServerStatus.STOP){
			is = new ItemStack(Material.REDSTONE_BLOCK);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED+"server : offline");
			ArrayList<String> list = new ArrayList<>();
			list.add(ChatColor.GREEN+"サーバーを起動する");
			im.setLore(list);
			is.setItemMeta(im);
		} else if (server.getStatus() == ServerStatus.STARTING) {
			is = new ItemStack(Material.WOOL,1,DyeColor.ORANGE.getWoolData());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"起動中");
			is.setItemMeta(im);
		} else if (server.getStatus() == ServerStatus.STOPPING) {
			is = new ItemStack(Material.WOOL,1,DyeColor.RED.getWoolData());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"停止中");
			is.setItemMeta(im);
		} else if (server.getStatus() == ServerStatus.REBOOTING) {
			is = new ItemStack(Material.WOOL,1,DyeColor.ORANGE.getWoolData());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"再起動中");
			is.setItemMeta(im);
		}
		return is;
	}
	
	public static ItemStack getIconDeleteItem() {
		ItemStack is = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.DARK_RED+"サーバーを削除");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.DARK_RED+"消したサーバーは戻せません");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconCreateSignItem(String name) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"サーバー名 : "+name);
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.GREEN+"変更");
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconCreateServerDo() {
		ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"作成");
		ArrayList<String> list = new ArrayList<>();
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconConnect() {
		ItemStack is = new ItemStack(Material.MINECART);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"接続");
		ArrayList<String> list = new ArrayList<>();
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconWorldType(String[] all, String current) {
		ItemStack is = new ItemStack(Material.GRASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"World Type");
		ArrayList<String> lore = new ArrayList<>();
		for (String line : all) {
			if (line.contains(current)) {
				lore.add(ChatColor.AQUA+line);
			} else {
				lore.add(ChatColor.GRAY+line);
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	private class WorldManageItem extends ExecutableItemStack {
		public WorldManageItem() {
			super(getIconManageWorld());
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			/*Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
				public void run() {
					GuiOpener.open(player, new WorldManageGui());
				}
			}, 2);*/
			player.sendMessage(ChatColor.RED+"この機能は現在調整中です。 ");
			return ExecReturn.CLOSE;
		}
	}
	
	private class DeleteItem extends ExecutableItemStack {
		public DeleteItem() {
			super(getIconDeleteItem());
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			SyncDeleteServer packet = new SyncDeleteServer(player.getUniqueId().toString());
			Lobby.instance.ws.send(new Gson().toJson(packet));
			player.sendMessage(ChatColor.RED+"削除されました。");
			return ExecReturn.CLOSE;
		}
	}
	
	private class StatusItem extends ExecutableItemStack {
		private DBServer server;
		public StatusItem(DBServer server) {
			super(getIconServerStatus(server));
			this.server = server;
		}
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			if (server.getStatus() == ServerStatus.RUNNING) {
				//stop
				SyncStopServer packet = new SyncStopServer(player.getUniqueId().toString());
				Lobby.instance.ws.send(new Gson().toJson(packet));
				player.sendMessage(ChatColor.RED+"停止中。");
			} else if (server.getStatus() == ServerStatus.STOP) {
				//start
				SyncStartServer packet = new SyncStartServer(player.getUniqueId().toString());
				Lobby.instance.ws.send(new Gson().toJson(packet));
				player.sendMessage(ChatColor.GREEN+"起動中！");
			}
			return ExecReturn.CLOSE;
		}
	}
	
	private class ServerRenameSign extends ExecutableItemStack {
		public ServerRenameSign(DBServer server) {
			super(getIconRename(server));
		}

		@SuppressWarnings("deprecation")
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
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
						if (Lobby.instance.sTable.getServerByName(text) != null) {
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
						DBServer server = Lobby.instance.sTable.get(pl.getUniqueId());
						server.setDisplayName(text.replace("&", "§"));
						server.update();
						return AnvilGUI.Response.close();
					})
					.text("サーバー名を入れてください。")
					.plugin(Lobby.instance)
					.open(player);
				}
			}, 2);
			return ExecReturn.CLOSE;
		}
	}
	
	private class ServerEditMotd extends ExecutableItemStack {
		public ServerEditMotd(DBServer server) {
			super(getIconMotd(server));
		}

		@SuppressWarnings("deprecation")
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
				public void run() {
					new AnvilGUI.Builder()
					.onClose(pl -> {
					})
					.onComplete((pl,text) -> {
						if (text == null) {
							return AnvilGUI.Response.text("Motdを入れてください。");
						}
						DBServer server = Lobby.instance.sTable.get(pl.getUniqueId());
						server.setMotd(text.replace("&", "§"));
						server.update();
						return AnvilGUI.Response.close();
					})
					.text("Motdを入れてください。")
					.plugin(Lobby.instance)
					.open(player);
				}
			}, 2);
			return ExecReturn.CLOSE;
		}
	}
	
	private class ServerCreateSign extends ExecutableItemStack {
		public ServerCreateSign() {
			super(getIconCreateSignItem(name));
		}

		@SuppressWarnings("deprecation")
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			/*player.sendMessage(ChatColor.AQUA+"サーバー名をchatに入力してください。");
			ServerListener.list.add(player.getUniqueId());*/
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
						if (Lobby.instance.sTable.getServerByName(text) != null) {
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
						Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
							public void run() {
								GuiOpener.open(pl, new ServerManagementGui(0,text.replace("&", "§"),type));
							}
						}, 2);
						return AnvilGUI.Response.close();
					})
					.text("サーバー名を入れてください。")
					.plugin(Lobby.instance)
					.open(player);
				}
			}, 2);
			return ExecReturn.CLOSE;
		}
	}
	
	private class WorldTypeItem extends ExecutableItemStack {
		
		public String type = "DEFAULT";
		
		public WorldTypeItem(String type) {
			super(getIconWorldType(new String[] {"DEFAULT","FLAT","VOID"},type));
			this.type = type;
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			if (type.equalsIgnoreCase("DEFAULT")) {
				type = "FLAT";
			} else if (type.equalsIgnoreCase("FLAT")) {
				type = "VOID";
			} else if (type.equalsIgnoreCase("VOID")) {
				type = "DEFAULT";
			}
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, 
					new Runnable() {
				public void run() {
			GuiOpener.open(player, new ServerManagementGui(0,name,type));
			}},2);
			return ExecReturn.CLOSE;
		}
	}
	
	private class DoServerCreate extends ExecutableItemStack {
		public DoServerCreate() {
			super(getIconCreateServerDo());
			// TODO Auto-generated constructor stub
		}
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			SyncCreateServer packet = new SyncCreateServer(player.getUniqueId().toString(), name, type);
			Lobby.instance.ws.send(new Gson().toJson(packet));
			player.sendMessage(ChatColor.AQUA+"作成中");
			return ExecReturn.CLOSE;
		}
	}
	
	private class ConnectItem extends ExecutableItemStack {
		public ConnectItem() {
			super(getIconConnect());
		}
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			Lobby.instance.connect(player);
			return ExecReturn.CLOSE;
		}
	}
	
	private class ServerCreate extends ExecutableItemStack {
		public ServerCreate() {
			super(getIconServerCreate());
		}
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, 
					new Runnable() {
				public void run() {
			GuiOpener.open(player, new ServerManagementGui(0,player.getName()+"のサーバー","DEFAULT"));
			}},2);
			return ExecReturn.CLOSE;
		}
	}
}