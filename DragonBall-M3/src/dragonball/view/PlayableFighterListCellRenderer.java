package dragonball.view;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import dragonball.model.character.fighter.PlayableFighter;

public class PlayableFighterListCellRenderer extends DefaultListCellRenderer
{
	public Component getListCellRendererComponent(JList list, Object fighter, int index, boolean isSelected, boolean cellHasFocus)
	 {
		 if (fighter instanceof PlayableFighter)
		 {
			 fighter = ((PlayableFighter)fighter).getName();
		 }
		 super.getListCellRendererComponent(list, fighter, index, isSelected, cellHasFocus);
		 return this;
	 }
}