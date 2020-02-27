package net.rezxis.mchosting.lobby;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.rezxis.mchosting.network.ClientHandler;
import net.rezxis.mchosting.network.packet.Packet;
import net.rezxis.mchosting.network.packet.PacketType;
import net.rezxis.mchosting.network.packet.ServerType;
import net.rezxis.mchosting.network.packet.lobby.LobbyServerCreated;
import net.rezxis.mchosting.network.packet.lobby.LobbyServerStarted;
import net.rezxis.mchosting.network.packet.lobby.LobbyServerStopped;
import net.rezxis.mchosting.network.packet.sync.SyncAuthSocketPacket;

public class WSClientHandler implements ClientHandler {

	public static Gson gson = new Gson();
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		SyncAuthSocketPacket packet = new SyncAuthSocketPacket(ServerType.LOBBY, null);
		Lobby.instance.ws.send(new Gson().toJson(packet));
	}

	@Override
	public void onMessage(String message) {
		System.out.println("Received : "+message);
		Packet packet = gson.fromJson(message, Packet.class);
		PacketType type = packet.type;
		if (type == PacketType.ServerStarted) {
			LobbyServerStarted p = gson.fromJson(message, LobbyServerStarted.class);
			Player player =Bukkit.getPlayer(UUID.fromString(p.player));
			if (player == null)
				return;
			if (player.isOnline()) {
				TextComponent c = new TextComponent("接続");
				c.setUnderlined(true);
                c.setColor(ChatColor.GOLD);
                c.setClickEvent(new ClickEvent(Action.RUN_COMMAND,"/connect"));
                c.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("接続！").create()));
                TextComponent msg = new TextComponent("サーバーが起動しました。");
                msg.setColor(ChatColor.AQUA);
                msg.addExtra(c);
                player.spigot().sendMessage(msg);
				//Bukkit.getPlayer(UUID.fromString(sPacket.getPlayer())).sendMessage(ChatColor.AQUA+"サーバーが起動しました！");
			}
		} else if (type == PacketType.ServerStopped) {
			LobbyServerStopped p = gson.fromJson(message, LobbyServerStopped.class);
			Player player = Bukkit.getPlayer(UUID.fromString(p.player));
			if (player != null)
				if (player.isOnline())
					player.sendMessage(ChatColor.AQUA+"サーバーが停止しました！");
		} else if (type == PacketType.ServerCreated) {
			LobbyServerCreated p = gson.fromJson(message, LobbyServerCreated.class);
			Player player = Bukkit.getPlayer(UUID.fromString(p.player));
			if (player != null)
				if (player.isOnline())
					player.sendMessage(ChatColor.AQUA+"サーバーが作成されました。起動してください！");
		}
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed / code : "+code+" / reason : "+reason+" / remote : "+remote);
	}

	@Override
	public void onMessage(ByteBuffer buffer) {
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

}
