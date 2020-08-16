package net.rezxis.mchosting.lobby.vote;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;
import com.yapzhenyie.GadgetsMenu.player.OfflinePlayerManager;
import com.yapzhenyie.GadgetsMenu.player.PlayerManager;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.crates.CrateTypes;
import net.rezxis.mchosting.database.object.player.DBVote;

public class VoteBonus {
	
	public static final VoteBonus v0;
	public static final VoteBonus v1;
	public static final VoteBonus v2;
	public static final VoteBonus v3;
	public static final VoteBonus v4;
	public static final VoteBonus v5;
	public static final VoteBonus v6;
	public static HashMap<Integer, VoteBonus> bonuses;
	
	static {
		bonuses = new HashMap<>();
		HashMap<Action,Integer> a0 = new HashMap<>();
		a0.put(Action.VOTEBOX, 1);
		v0 = new VoteBonus(0, a0);
		HashMap<Action,Integer> a1 = new HashMap<>();
		a1.put(Action.VOTEBOX, 2);
		v1 = new VoteBonus(0, a1);
		HashMap<Action,Integer> a2 = new HashMap<>();
		a2.put(Action.VOTEBOX, 2);
		v2 = new VoteBonus(0, a2);
		
		HashMap<Action,Integer> a3 = new HashMap<>();
		a3.put(Action.RANK, 1);
		v3 = new VoteBonus(0, a3);
		
		HashMap<Action,Integer> a4 = new HashMap<>();
		a4.put(Action.VOTEBOX, 2);
		a4.put(Action.MYSTERY4, 2);
		v4 = new VoteBonus(0, a4);
		HashMap<Action,Integer> a5 = new HashMap<>();
		a5.put(Action.VOTEBOX, 2);
		a5.put(Action.MYSTERY4, 1);
		a5.put(Action.MYSTERY5, 1);
		v5 = new VoteBonus(0, a5);
		HashMap<Action,Integer> a6 = new HashMap<>();
		a6.put(Action.VOTEBOX, 2);
		a6.put(Action.MYSTERY5, 2);
		v6= new VoteBonus(0, a6);
		bonuses.put(0, v0);
		bonuses.put(1, v1);
		bonuses.put(2, v2);
		bonuses.put(3, v3);
		bonuses.put(4, v4);
		bonuses.put(5, v5);
		bonuses.put(6, v6);
	}
	
	public static void give(OfflinePlayer player, DBVote dv, int kind) {
		VoteBonus vb = bonuses.get(kind);
		for (Entry<Action,Integer> e : vb.action.entrySet()) {
			OfflinePlayerManager gadPlayer = GadgetsMenuAPI.getOfflinePlayerManager(player.getUniqueId());
			if (e.getKey() == Action.VOTEBOX) {
				Tables.getCTable().giveManyCrates(player.getUniqueId(), CrateTypes.VOTE, e.getValue());
			} else if (e.getKey() == Action.MYSTERY4) {
				gadPlayer.giveMysteryBoxes(System.currentTimeMillis()+(24 * 3600 * 1000 * 7), false, null, e.getValue(), 100, 0, 0, 0, 100, 0);
			} else if (e.getKey() == Action.MYSTERY5) {
				gadPlayer.giveMysteryBoxes(System.currentTimeMillis()+(24 * 3600 * 1000 * 7), false, null, e.getValue(), 100, 0, 0, 0, 0, 100);
			} else if (e.getKey() == Action.RANK) {
				Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Japan"),Locale.JAPANESE);
				c.add(Calendar.DAY_OF_WEEK, 1);
				System.out.println(c.getTime().toLocaleString());
				dv.setRank(c.getTime());
			}
		}
	}
	
	public int type;
	public HashMap<Action, Integer> action;
	
	public VoteBonus(int type, HashMap<Action, Integer> action) {
		this.type = type;
		this.action = action;
	}
	
	public enum Action {
		VOTEBOX,RANK,MYSTERY4,MYSTERY5;
	}
}
