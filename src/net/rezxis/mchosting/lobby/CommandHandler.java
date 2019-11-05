package net.rezxis.mchosting.lobby;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.gui.GuiOpener;
import net.rezxis.mchosting.lobby.gui.MainGui;
import net.rezxis.mchosting.network.packet.sync.SyncStopServer;

public class CommandHandler {

	@SuppressWarnings("deprecation")
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("menu")) {
			GuiOpener.open((Player) sender, MainGui.gui);
		} else if (cmd.getName().equalsIgnoreCase("connect")) {
			DBServer server = Lobby.instance.sTable.get(((Player)sender).getUniqueId());
			if (server == null) {
				sender.sendMessage(ChatColor.RED+"あなたはサーバーを持っていません。");
				return true;
			}
			if (server.getPort() == -1) {
				sender.sendMessage(ChatColor.RED+"あなたのサーバーは起動していません。");
				return true;
			}
			Lobby.instance.connect((Player)sender);
		} else if (cmd.getName().equalsIgnoreCase("stopall")) {
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
		}
		return true;
	}
}
