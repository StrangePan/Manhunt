package com.bendude56.hunted;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;

public class SpConnect {

	private HashMap<Player, GenericLabel> lbls = new HashMap<Player, GenericLabel>();

	public void showTime(String label, int min, int sec, Player p) {
		GenericLabel lbl;
		if (lbls.containsKey(p)) {
			lbl = lbls.get(p);
		} else {
			lbl = new GenericLabel();
			lbl.setTextColor(new Color(240F / 255F, 220F / 255F, 0F)).setX(5)
					.setY(5);
			lbls.put(p, lbl);
			SpoutManager.getPlayer(p).getMainScreen()
					.attachWidget(HuntedPlugin.getInstance(), lbl);
		}
		lbl.setText(label + ": " + toString(min) + ":" + toString(sec));
		lbl.doResize();
		lbl.setDirty(true);
	}

	public void showTime(String label, int hour, int min, int sec, Player p) {
		GenericLabel lbl;
		if (lbls.containsKey(p)) {
			lbl = lbls.get(p);
		} else {
			lbl = new GenericLabel();
			lbl.setTextColor(new Color(240F / 255F, 220F / 255F, 0F)).setX(5)
					.setY(5);
			lbls.put(p, lbl);
			SpoutManager.getPlayer(p).getMainScreen()
					.attachWidget(HuntedPlugin.getInstance(), lbl);
		}
		lbl.setText(label + ": " + toString(hour) + ":" + toString(min) + ":"
				+ toString(sec));
		lbl.doResize();
		lbl.setDirty(true);
	}

	private String toString(Integer i) {
		String s = i.toString();
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	public void hideTime(Player p) {
		if (lbls.containsKey(p)) {
			SpoutManager.getPlayer(p).getMainScreen().removeWidget(lbls.get(p));
			lbls.remove(p);
		}
	}
}
