package net.rezxis.mchosting.lobby;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.databse.ServerStatus;
import net.rezxis.mchosting.lobby.gui2.main.MainMenu;
import net.rezxis.mchosting.lobby.gui2.mine.MyRealmMenu;
import net.rezxis.mchosting.lobby.gui2.servers.ServersMenu;
import net.rezxis.mchosting.network.packet.sync.SyncStopServer;

public class CommandHandler {

	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String name = cmd.getName();
		if (name.equalsIgnoreCase("menu") || name.equalsIgnoreCase("realm")) {
			new MainMenu((Player) sender).delayShow();
		} else if (name.equalsIgnoreCase("servers")) {
			new ServersMenu((Player) sender, 1, false).delayShow();
		} else if (name.equalsIgnoreCase("manage")) {
			new MyRealmMenu((Player) sender).delayShow();
		} else if (name.equalsIgnoreCase("connect")) {
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
				Gson gson = new Gson();
				for (DBServer s : online) {
					SyncStopServer packet = new SyncStopServer(s.getOwner().toString());
					Lobby.instance.ws.send(gson.toJson(packet));
				}
			} else {
				sender.sendMessage(ChatColor.RED+"The Command is not allowed to use");
			}
		} else if (name.equalsIgnoreCase("menu2")) {
			new MainMenu((Player)sender).delayShow();
		}
		return true;
	}
}
