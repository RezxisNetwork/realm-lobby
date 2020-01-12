package net.rezxis.mchosting.lobby;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.print.attribute.standard.PDLOverrideSupported;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.DBThirdParty;
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
		} else if (name.equalsIgnoreCase("connect")) {
			DBServer server = Tables.getSTable().get(((Player)sender).getUniqueId());
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
				ArrayList<DBServer> online = Tables.getSTable().getOnlineServers();
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
								DBServer server = Tables.getSTable().get(opl.getUniqueId());
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
								DBServer server = Tables.getSTable().get(opl.getUniqueId());
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
								DBServer server = Tables.getSTable().get(opl.getUniqueId());
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
								DBServer server = Tables.getSTable().get(opl.getUniqueId());
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
								DBServer server = Tables.getSTable().get(opl.getUniqueId());
								if (server == null) {
									sender.sendMessage(ChatColor.RED+"There is no server which owner is "+args[1]);
								} else {
									sender.sendMessage(ChatColor.GREEN+"Server Status");
									sender.sendMessage(ChatColor.GREEN+"status : "+server.getStatus().name());
									sender.sendMessage(ChatColor.GREEN+"players : "+server.getPlayers());
									sender.sendMessage(ChatColor.GREEN+"visible : "+server.isVisible());
								}
								DBPlayer dp = Tables.getPTable().get(opl.getUniqueId());
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
		} else if (name.equalsIgnoreCase("prefix")) {
			Player player = (Player)sender;
			DBPlayer dp = Tables.getPTable().get(player.getUniqueId());
			if (!dp.isSupporter()) {
				player.sendMessage(ChatColor.RED+"Prefixはサポーターで解禁されます。");
				return true;
			}
			if (args.length != 0) {
				String prefix = "";
				for (String split : args) {
					prefix += split;
				}
				prefix = prefix.replaceAll("&", "§");
				dp.setPrefix(prefix+ChatColor.RESET);
				dp.update();
				player.sendMessage(ChatColor.GREEN+"Prefixは"+prefix+" に変更されました。");
			} else {
				dp.setPrefix("");
				dp.update();
				player.sendMessage(ChatColor.GREEN+"Prefixを初期化しました。設定は /prefix <prefix> です。");
			}
		} else if (name.equalsIgnoreCase("fly")) {
			Player player = (Player) sender;
			DBPlayer dp = Tables.getPTable().get(player.getUniqueId());
			if (dp.isSupporter()) {
				if (player.getAllowFlight()) {
					player.setAllowFlight(false);
					player.sendMessage(ChatColor.GREEN+"flyを無効化しました。");
				} else {
					player.setAllowFlight(true);
					player.sendMessage(ChatColor.GREEN+"flyを有効化しました。");
				}
			} else {
				player.sendMessage(ChatColor.RED+"この機能はサポーターで使えます。");
			}
		} else if (name.equalsIgnoreCase("thirdparty")) {
			Player player = (Player) sender;
			if (args.length == 0) {
				thirdHelp(player);
			} else {
				if (args[0].equalsIgnoreCase("status")) {
					DBThirdParty dtp = Tables.getTTable().getByUUID(player.getUniqueId());
					if (dtp == null) {
						player.sendMessage(ChatColor.RED+"登録されていません。");
						return true;
					}
					player.sendMessage(ChatColor.RED+"Key : "+dtp.getKey());
					player.sendMessage(ChatColor.RED+"online : "+dtp.isOnline());
					player.sendMessage(ChatColor.RED+"locked : "+dtp.isLocked());
					player.sendMessage(ChatColor.RED+"Host : "+dtp.getHost());
					player.sendMessage(ChatColor.RED+"Port : "+dtp.getPort());
					player.sendMessage(ChatColor.RED+"players : "+dtp.getPlayers());
					player.sendMessage(ChatColor.RED+"max : "+dtp.getMax());
					player.sendMessage(ChatColor.RED+"name : "+dtp.getName());
					player.sendMessage(ChatColor.RED+"motd : "+dtp.getMotd());
					player.sendMessage(ChatColor.RED+"icon : "+dtp.getIcon());
					player.sendMessage(ChatColor.RED+"vote : "+dtp.getScore());
					player.sendMessage(ChatColor.RED+"Expire in "+dtp.getExpire().toLocaleString());
					player.sendMessage(ChatColor.RED+"=========END OF THIRDPARTY STATUS=========");
				} else if (args[0].equalsIgnoreCase("renew")) {
					DBThirdParty dtp = Tables.getTTable().getByUUID(player.getUniqueId());
					if (dtp == null) {
						player.sendMessage(ChatColor.RED+"登録されていません。");
						return true;
					}
					if (dtp.isOnline()) {
						player.sendMessage(ChatColor.RED+"あなたのサーバーはオンラインです。");
						return true;
					}
					dtp.setKey(RandomStringUtils.randomAlphabetic(10));
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.WEEK_OF_MONTH, 1);
					dtp.setExpire(calendar.getTime());
					dtp.update();
					player.sendMessage(ChatColor.RED+"更新されました。");
				} else if (args[0].equalsIgnoreCase("register")) {
					DBThirdParty dtp = Tables.getTTable().getByUUID(player.getUniqueId());
					if (dtp != null) {
						player.sendMessage(ChatColor.RED+"すでに登録されています。");
						return true;
					}
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.WEEK_OF_MONTH, 1);
					dtp = new DBThirdParty(-1,RandomStringUtils.randomAlphabetic(10),
							player.getUniqueId(),false,false,calendar.getTime(),
							"",-1,0,0,"","",true,0,"EMERALD_BLOCK");
					Tables.getTTable().insert(dtp);
					player.sendMessage(ChatColor.RED+"登録されました。 有効期限は一週間です。");
				}
			}
		}
		return true;
	}
	
	private static void thirdHelp(Player player) {
		player.sendMessage(ChatColor.GREEN+"============================");
		player.sendMessage(ChatColor.RED+"/thirdparty status : Show your thirdparty status");
		player.sendMessage(ChatColor.RED+"/thirdparty renew : Renew your thirdparty key");
		player.sendMessage(ChatColor.GREEN+"============================");
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
