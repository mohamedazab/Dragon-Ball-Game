package dragonball.view;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import dragonball.model.attack.Attack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.UltimateAttack;

public class AttackListCellRenderer extends DefaultListCellRenderer
{
	 public Component getListCellRendererComponent(JList list, Object attack, int index, boolean isSelected, boolean cellHasFocus)
	 {
			 attack = ((Attack)attack).getName();
		 
		 super.getListCellRendererComponent(list, attack, index, isSelected, cellHasFocus);
		 return this;
	 }
}