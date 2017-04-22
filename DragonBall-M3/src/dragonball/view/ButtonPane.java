package dragonball.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.game.Game;
import dragonball.model.game.GameState;
import dragonball.model.world.World;

public class ButtonPane extends JPanel {
   
    private WorldView WorldView; 	
    private Game game;
    private JButton[][] buttons;
    
    public ButtonPane(int row, int col)
    {
      super(new GridLayout(row, col));
      buttons = new JButton[row][col];
    
 for (int i = 0; i < buttons.length; i++) {
         for (int j = 0; j < buttons[i].length; j++) {
           int curRow = i;
           int  curCol = j;
            buttons[i][j] = new JButton();
        //    buttons[i][j].setBorder(null);
        //    buttons[i][j].setOpaque(false);
      //      buttons[i][j].setContentAreaFilled(true);
            buttons[i][j].addKeyListener(onStep);
            buttons[i][j].addKeyListener(new KeyAdapter() {
               public void keyPressed(KeyEvent e) {
            	 
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_BEGIN:
                	  if (curRow<0&&curCol<0)
                		  buttons[9][9].requestFocus();
                	  break;
                  case KeyEvent.VK_UP:
                     if (curRow > 0)
                        buttons[curRow-1][curCol].requestFocus();
                     break;
                  case KeyEvent.VK_DOWN:
                     if (curRow < buttons.length - 1)
                        buttons[curRow+1][curCol].requestFocus();
                     break;
                  case KeyEvent.VK_LEFT:
                     if (curCol > 0)
                        buttons[curRow][curCol-1].requestFocus();
                     break;
                  case KeyEvent.VK_RIGHT:
                     if (curCol < buttons[curRow+1].length - 1)
                        buttons[curRow][curCol +1].requestFocus();
                     break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i][j]);
         }
      }

   }

public void onCollectibleFound (Collectible collectible)
{
	game.onCollectibleFound(collectible);
	if(game.getState()== GameState.WORLD)
	{
		if(collectible == Collectible.SENZU_BEAN)
		{
			JOptionPane.showMessageDialog(WorldView, "Congratulations, now You have " + game.getPlayer().getSenzuBeans() + " senzu beans!", "You have found a " + collectible + "!", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(WorldView, "Congratulations, now You have " + game.getPlayer().getDragonBalls() + " DragonBalls!", "You have found a " + collectible + "!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

public void onFoeEncountered (NonPlayableFighter foe)
{
	game.onFoeEncountered(foe);
	if(game.getState() == GameState.WORLD)
	{
		JOptionPane.showMessageDialog(WorldView, "Ops, You have encountered a foe." ,"Foe Encountered" , JOptionPane.INFORMATION_MESSAGE);
	}
	
}

   private KeyListener onStep = new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
         if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            ((JButton) e.getComponent()).doClick();
         }
      }
   };
   
}
   
   

   