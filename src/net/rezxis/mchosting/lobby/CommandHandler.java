package net.rezxis.mchosting.lobby;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.ServerStatus;
import net.rezxis.mchosting.lobby.gui2.crate.CrateMenu;
import net.rezxis.mchosting.lobby.gui2.main.MainMenu;
import net.rezxis.mchosting.lobby.gui2.mine.MyRealmMenu;
import net.rezxis.mchosting.lobby.gui2.servers.ServersMenu;
import net.rezxis.mchosting.network.packet.sync.SyncRebootServer;
import net.rezxis.mchosting.network.packet.sync.SyncStartServer;
import net.rezxis.mchosting.network.packet.sync.SyncStopServer;

public class CommandHandler {

	private static Gson gson = new Gson();
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String name = cmd.getName();
		if (name.equalsIgnoreCase("menu") || name.equalsIgnoreCase("realm")) {
			new MainMenu((Player) sender).delayShow();
		} else if (name.equalsIgnoreCase("servers")) {
			new ServersMenu((Player) sender, 1, false,"players").delayShow();
		} else if (name.equalsIgnoreCase("manage")) {
			new MyRealmMenu((Player) sender).delayShow();
		} else if (name.equalsIgnoreCase("crate")) {
			new CrateMenu((Player) sender).delayShow();
		}else if (name.equalsIgnoreCase("connect")) {
			DBServer server = Lobby.instance.sTable.get(((Player)sender).getUniqueId());
			if (server == null) {
				sender.sendMessage(ChatColor.RED+"あなたはサーバーを持っていません。");
				return true;
			}
			if (server.getStatus() != ServerStatus.RUNNING) {
				sender.sendMessage(ChatColor.RED+"あなたのサーバーは起動していません。");
				return true;
			}
			Lobby.instance.connect((Player)sender);
		} else if (name.equalsIgnoreCase("stopall")) {
			if (sender.isOp()) {
				ArrayList<DBServer> online = Lobby.instance.sTable.getOnlineServers();
				for (DBServer s : online) {
					SyncStopServer packet = new SyncStopServer(s.getOwner().toString());
					Lobby.instance.ws.send(gson.toJson(packet));
				}
			} else {
				sender.sendMessage(ChatColor.RED+"The Command is not allowed to use");
			}
		} else if (name.equalsIgnoreCase("manager")) {
			if (sender.isOp()) {
				if (args.length == 0) {
					printHelp(sender);
				} else {
					if (args[0].equalsIgnoreCase("help")) {
						printHelp(sender);
					} else if (args[0].equalsIgnoreCase("stop")) {
						if (args.length == 2) {
							OfflinePlayer opl = Bukkit.getOfflinePlayer(args[1]);
							if (opl == null) {
								sender.sendMessage(ChatColor.RED+"There is no player which name is "+args[1]);
							} else {
								DBServer server = Lobby.instance.sTable.get(opl.getUniqueId());
								if (server == null) {
									sender.sendMessage(ChatColor.RED+"There is no server which owner is "+args[1]);
								} else {
									sender.sendMessage(ChatColor.GREEN+"Sending stop signal to host...");
									sender.sendMessage(ChatColor.GREEN+"if you can't check server stop, use /manager kill <MCID>");
									Lobby.instance.ws.send(gson.toJson(new SyncStopServer(opl.getUniqueId().toString())));
								}
							}
						} else {
							sender.sendMessage(ChatColor.GREEN+"Usage : /manager stop <MCID>");
						}
					} else if (args[0].equalsIgnoreCase("start")) {
						if (args.length == 2) {
							OfflinePlayer opl = Bukkit.getOfflinePlayer(args[1]);
							if (opl == null) {
								sender.sendMessage(ChatColor.RED+"There is no player which name is "+args[1]);
							} else {
								DBServer server = Lobby.instance.sTable.get(opl.getUniqueId());
								if (server == null) {
									sender.sendMessage(ChatColor.RED+"There is no server which owner is "+args[1]);
								} else {
									sender.sendMessage(ChatColor.GREEN+"Sending start signal to host...");
									Lobby.instance.ws.send(gson.toJson(new SyncStartServer(opl.getUniqueId().toString())));
								}
							}
						} else {
							sender.sendMessage(ChatColor.GREEN+"Usage : /manager start <MCID>");
						}
					} else if (args[0].equalsIgnoreCase("restart")) {
						if (args.length == 2) {
							OfflinePlayer opl = Bukkit.getOfflinePlayer(args[1]);
							if (opl == null) {
								sender.sendMessage(ChatColor.RED+"There is no player which name is "+args[1]);
							} else {
								DBServer server = Lobby.instance.sTable.get(opl.getUniqueId());
								if (server == null) {
									sender.sendMessage(ChatColor.RED+"There is no server which owner is "+args[1]);
								} else {
									sender.sendMessage(ChatColor.GREEN+"Sending restart signal to host...");
									Lobby.instance.ws.send(gson.toJson(new SyncRebootServer(opl.getUniqueId().toString())));
								}
							}
						} else {
							sender.sendMessage(ChatColor.GREEN+"Usage : /manager restart <MCID>");
						}
					} else if (args[0].equalsIgnoreCase("reset")) {
						if (args.length == 2) {
							OfflinePlayer opl = Bukkit.getOfflinePlayer(args[1]);
							if (opl == null) {
								sender.sendMessage(ChatColor.RED+"There is no player which name is "+args[1]);
							} else {
								DBServer server = Lobby.instance.sTable.get(opl.getUniqueId());
								if (server == null) {
									sender.sendMessage(ChatColor.RED+"There is no server which owner is "+args[1]);
								} else {
									server.setStatus(ServerStatus.STOP);
									server.setPlayers(0);
									server.setPort(-1);
									server.update();
									sender.sendMessage(ChatColor.GREEN+"Reseted server status.");
								}
							}
						} else {
							sender.sendMessage(ChatColor.GREEN+"Usage : /manager reset <MCID>");
						}
					} else if (args[0].equalsIgnoreCase("status")) {
						if (args.length == 2) {
							OfflinePlayer opl = Bukkit.getOfflinePlayer(args[1]);
							if (opl == null) {
								sender.sendMessage(ChatColor.RED+"There is no player which name is "+args[1]);
							} else {
								DBServer server = Lobby.instance.sTable.get(opl.getUniqueId());
								if (server == null) {
									sender.sendMessage(ChatColor.RED+"There is no server which owner is "+args[1]);
								} else {
									sender.sendMessage(ChatColor.GREEN+"Server Status");
									sender.sendMessage(ChatColor.GREEN+"status : "+server.getStatus().name());
									sender.sendMessage(ChatColor.GREEN+"players : "+server.getPlayers());
									sender.sendMessage(ChatColor.GREEN+"visible : "+server.isVisible());
								}
								DBPlayer dp = Lobby.instance.pTable.get(opl.getUniqueId());
								sender.sendMessage(ChatColor.GREEN+"Player Status");
								sender.sendMessage(ChatColor.GREEN+"Rank : "+dp.getRank().name());
								sender.sendMessage(ChatColor.GREEN+"Coins : "+dp.getCoin());
							}
						} else {
							sender.sendMessage(ChatColor.GREEN+"Usage : /manager status <MCID>");
						}
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED+"The Command is not allowed to use");
			}
		} else if (name.equalsIgnoreCase("vote")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.RED+"投票方法: /vote <投票対象サーバーのオーナー名>");
			} else {
				OfflinePlayer opl = Bukkit.getOfflinePlayer(args[0]);
				if (opl == null) {
					sender.sendMessage(ChatColor.RED+args[0]+"は存在しません。");
					return true;
				} else {
					DBPlayer self = Lobby.instance.pTable.get(((Player)sender).getUniqueId());
					if (self.getNextVote().after(new Date())) {
						sender.sendMessage(ChatColor.RED+"投票は一日一回のみです。");
						return true;
					} else {
						DBServer server = Lobby.instance.sTable.get(opl.getUniqueId());
						if (server == null) {
							sender.sendMessage(ChatColor.RED+args[0]+"はサーバーを持ってません。");
							return true;
						} else {
							server.addVote(1);
							server.update();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(new Date());
							calendar.add(Calendar.DAY_OF_WEEK, 1);
							self.setNextVote(calendar.getTime());
							self.update();
							sender.sendMessage(server.getDisplayName()+ChatColor.GREEN+"に投票しました。");
						}
					}
				}
			}
		}
		return true;
	}
	
	private static void printHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN+"============RezxisRealm Manager============");
		sender.sendMessage(ChatColor.RED+"/manager status <MCID> : show player status");
		sender.sendMessage(ChatColor.RED+"/manager stop <MCID> : send stop signal to server");
		sender.sendMessage(ChatColor.RED+"/manager reset <MCID> : reset status server status to 'STOP'");
		sender.sendMessage(ChatColor.RED+"/manager start <MCID> : send start signal to server");
		sender.sendMessage(ChatColor.RED+"/manager restart <MCID> : send restart signal to server");
		sender.sendMessage(ChatColor.GREEN+"===========================================");
	}
	/*
	 *   stop
    start
    restart
    help
	 */
}
