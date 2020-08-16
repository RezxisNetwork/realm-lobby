package net.rezxis.mchosting.lobby.gui2.vote;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBVote;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class VoteStatusMenu extends GUIWindow {

	public VoteStatusMenu(Player player) {
		super(player, "VOTE STATUS", 3, Lobby.instance);
	}
	//|0 |1 |2 |3 |4 |5 |6 |7 |8 |
	//|9 |10|11|12|13|14|15|16|17|
	//|18|19|20|21|22|23|24|25|26|

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		DBVote dv = Tables.getVTable().getVoteByUUID(this.getPlayer().getUniqueId());
		if (dv == null) {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Japan"),Locale.JAPANESE);
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -2);
			dv = new DBVote(-1,getPlayer().getUniqueId(),0,0,new Date(), c.getTime());
			Tables.getVTable().insert(dv);
		}
		GlassPaneItem fake = new GlassPaneItem((short) 15);
		HashMap<Integer,GUIItem> items = new HashMap<>();
		for (int i = 0; i < 7; i++) {
			items.put(10+i, new VoteBonusInfoItem(dv, i));
		}
		for (int i = 0;i < 10; i++) {
			items.put(i, fake);
		}
		for (int i = 17; i < 27; i++) {
			if (i != 22) {
				items.put(i, fake);
			} else {
				items.put(i, new MyVoteStatusItem(this.getPlayer(), dv));
			}
		}
		
		return items;
	}
}
