package dragonball.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.concurrent.Task;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;








import com.sun.glass.events.WindowEvent;

import dragonball.model.attack.Attack;
import dragonball.model.attack.MaximumCharge;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.battle.Battle;
import dragonball.model.battle.BattleEvent;
import dragonball.model.battle.BattleEventType;
import dragonball.model.battle.BattleOpponent;
import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.dragon.Dragon;
import dragonball.model.dragon.DragonWish;
import dragonball.model.dragon.DragonWishType;
import dragonball.model.exceptions.DuplicateAttackException;
import dragonball.model.exceptions.MapIndexOutOfBoundsException;
import dragonball.model.exceptions.MaximumAttacksLearnedException;
import dragonball.model.exceptions.MissingFieldException;
import dragonball.model.exceptions.NotASaiyanException;
import dragonball.model.exceptions.NotEnoughAbilityPointsException;
import dragonball.model.exceptions.NotEnoughKiException;
import dragonball.model.exceptions.NotEnoughSenzuBeansException;
import dragonball.model.exceptions.UnknownAttackTypeException;
import dragonball.model.game.Game;
import dragonball.model.game.GameListener;
import dragonball.model.game.GameState;
import dragonball.model.player.Player;
import dragonball.model.world.World;
import dragonball.view.BattleView;
import dragonball.view.DragonView;
import dragonball.view.MainMenu;
import dragonball.view.WorldView;

public class Controller implements GameListener, ActionListener, KeyListener
{
	transient private BattleView battleView;
	transient private Game game;
	transient private WorldView worldView;
	transient private DragonView dragonView;
	transient private MainMenu newG;
	transient private Battle currbattle;	
	transient private int prow;
	transient private int pcol;
	
	
	
	
	public void gamestart()
	{
		newG =new MainMenu();	
		newG.setListener(this);
		newG.setVisible(true);
	}
	
	public Controller()
	{
		gamestart();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton ac=(JButton)e.getSource();
			String txt=ac.getText();
			if(txt.equals("New Game")||txt.equals("Load Saved Game")||txt.equals("Exit Game"))
			{
				if(txt.equals("New Game"))
				{ 
					String r=JOptionPane.showInputDialog(newG,"Enter Your Player Name");
					try
					{
						if(r==null||r.equals(""))
						{
							if(r!=null)
								JOptionPane.showMessageDialog(newG,"Enter a Valid Name");
						}else{
							this.game=new Game();
							this.game.getPlayer().setName(r);
				
							String fightername=	JOptionPane.showInputDialog(newG,"Choose a name for your Fighter");
							if(fightername!=null&&!(fightername.equals("")))
							{
								String[] possibilities = {"Sayian", "Namakian", "Earthling","Frieza","Majin"};
								String input = (String) JOptionPane.showInputDialog(worldView, "Please, choose your Fighter's race", "Create your Fighter", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
								this.newG.dispose();
								this.worldView=new WorldView();
								try
								{
									game.getPlayer().createFighter(input.charAt(0), fightername);
									this.worldView.setListener(this);
									this.setworldpos();
									worldView.updateFighter(game.getPlayer().getActiveFighter());
									worldView.updatePlayer(game.getPlayer());
									worldView.addFighter(game.getPlayer().getActiveFighter());
									worldView.setVisible(true);
									
									this.game.getPlayer().getActiveFighter().setAbilityPoints(20);
								}
								catch(NullPointerException e1)
								{
									
								}
							}else{
								if(fightername!=null)
									JOptionPane.showMessageDialog(newG,"Enter a Valid Name");
							}
						}
					}
					catch (MissingFieldException | UnknownAttackTypeException e1)
					{
						JOptionPane.showMessageDialog(newG,"Error Loading The Game");	
					}
				}
				else if (txt.equals("Load Saved Game"))
				{
					String r=JOptionPane.showInputDialog("Write the ittle of the last saved file");
					try
					{  
						game=new Game();
						game.load(r);
						newG.dispose();		
					
						worldView=new WorldView();
						  
						worldView.updateFighter(game.getPlayer().getActiveFighter());
						worldView.updatePlayer(game.getPlayer());
						
						this.worldView.setListener(this);
						this.setworldpos();
                        for (int i = 0; i < game.getPlayer().getFighters().size(); i++)
                        {
                        	worldView.addFighter(game.getPlayer().getFighters().get(i));
						} 
                     
						worldView.setVisible(true);
					}
					catch (ClassNotFoundException | IOException e1)
					{
						JOptionPane.showMessageDialog(newG,"No Such File Exist");	
					}
					catch (NullPointerException e1)
					{
					
					}
				
				}else{
					int choice = JOptionPane.showConfirmDialog(this.newG, "Are you sure you want to exit? Any unsaved data will be lost.");
					if (choice == JOptionPane.YES_OPTION)
					{
						System.exit(0);
						if(newG!=null)
						{
							newG.dispose();
						}
						if(worldView!=null)
						{
							worldView.dispose();
						}
						if(dragonView!=null)
						{
							dragonView.dispose();
						}
						if(battleView!=null)
						{
							battleView.dispose();
						}
					}
				}
				if(game !=null)
				{
					game.setListener(this);
				}
			}else{
				if(this.game.getState()==GameState.DRAGON)
				{
					this.dragonstate(txt);
			
				}else
					if(this.game.getState()==GameState.WORLD)
					{	
						this.WorldActions(e);
					}
					else if(this.game.getState()==GameState.BATTLE)
					{
						this.battlestate(e);	
					}
			}
		}else{
			if(e.getSource() instanceof JComboBox)
			{
				if(this.game.getState()==GameState.BATTLE)
				{
				   this.battlestate(e);	
				}
				else
				if(this.game.getState()==GameState.WORLD)
				{	
					this.WorldActions(e);
				}	
			}
		}
	}
	
	public void WorldActions(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton s=(JButton) e.getSource();
			String txt=s.getText();
	
			if(txt.equals("Save Game"))
			{
				String s1 = (String)JOptionPane.showInputDialog(worldView,"Enter the name you want");
				try
				{
					this.game.save(s1);
				}
				catch (IOException e1)
				{
					JOptionPane.showMessageDialog(worldView,"Cannot save game");
				}
			}
			
			if(txt.equals("Create Character"))
			{
				String s1 = (String)JOptionPane.showInputDialog(worldView,"Enter the Fighter's name");
				if(s1!=null)
				{
					if(s1.equals(""))
						JOptionPane.showMessageDialog(worldView, "Please enter a valid name for your Fighter");
					else{
						String[] possibilities = {"Sayian", "Namakian", "earthling","Frieza","Majin"};
						String input = (String) JOptionPane.showInputDialog(worldView, "Please, choose your Fighter's race", "Create yor Fighter", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
					
						try
						{
							if(s1.equals(""))
								throw new NullPointerException();
							this.game.getPlayer().createFighter(input.charAt(0), s1);
							int i=this.game.getPlayer().getFighters().size()-1;
							this.worldView.addFighter(this.game.getPlayer().getFighters().get(i));
						}
						catch(NullPointerException e1)
						{
				
						}
					}
				}
			}
			
			if(txt.equals("Assign Attack"))
			{
				SuperAttack [] SA = (SuperAttack[]) game.getPlayer().getSuperAttacks().toArray(new SuperAttack[game.getPlayer().getSuperAttacks().size()]);
				UltimateAttack [] UA = (UltimateAttack[]) game.getPlayer().getUltimateAttacks().toArray(new UltimateAttack[game.getPlayer().getUltimateAttacks().size()]);
				
				Attack[] playerAttacks = new Attack[SA.length + UA.length];
				String[] choices = new String [playerAttacks.length];
				int i = 0;
				
				for(int j = 0 ; j<SA.length ; j++)
				{
					playerAttacks[j] = SA[j];
				}
				for(int j = 0 ; j<UA.length ; j++)
				{
					playerAttacks[j+SA.length] = UA[j];
				}
				
				for(Attack attack : playerAttacks)
				{ 
					if(i<SA.length)
					{
						choices[i] = "SA " + playerAttacks[i].getName();
						i++;
					}else{
						choices[i] = "UA " + playerAttacks[i].getName();
						i++;
					}
				}
				String input = null;
				if(choices.length > 0)
				{
					input = (String) JOptionPane.showInputDialog(worldView, "Please choose an attack you would like to have.", "Assign an Attack to your Fighter", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				}else{
					JOptionPane.showMessageDialog(worldView, "You still don't have any attacks!");
				}
				
				SuperAttack[] FSA = (SuperAttack[]) game.getPlayer().getActiveFighter().getSuperAttacks().toArray(new SuperAttack[game.getPlayer().getActiveFighter().getSuperAttacks().size()]);
				UltimateAttack[] FUA = (UltimateAttack[]) game.getPlayer().getActiveFighter().getUltimateAttacks().toArray(new UltimateAttack[game.getPlayer().getActiveFighter().getUltimateAttacks().size()]);

				Attack[] fighterAttacks = new Attack[FSA.length + FUA.length];
				String[] choices2 = new String [fighterAttacks.length];
				i = 0;
				
				for(int j = 0 ; j<FSA.length ; j++)
				{
					fighterAttacks[j] = FSA[j];
				}
				for(int j = 0 ; j<FUA.length ; j++)
				{
					fighterAttacks[j+FSA.length] = FUA[j];
				}
				
				for(Attack attack : fighterAttacks)
				{
					if(i<FSA.length)
					{
						choices2[i] = "SA " + attack.getName();
						i++;
					}else{
						choices2[i] = "UA " + attack.getName();
						i++;
					}
				}
				
				String input2 = null;
				if(input!=null&&choices2.length>0)
				{
					input2 = (String) JOptionPane.showInputDialog(worldView, "Please, choose an attack you would like to remove. Cancel if none.", "Assign an attack to your Fighter", JOptionPane.QUESTION_MESSAGE, null, choices2, choices2[0]);
				}else{
					if(input!=null)
					JOptionPane.showMessageDialog(worldView, "The attack cannot replace any of your Fighters' attacks. Your Fighter has no attacks to choose from");
				}
				
				Attack newAttack = null;
				Attack oldAttack = null;
				for (i = 0 ; input!=null&&i < playerAttacks.length ; i++)
				{
					if ((input).substring(3).equals((playerAttacks[i]).getName()) )
					{
						newAttack = playerAttacks[i];
					}
					if(input2!=null && i < fighterAttacks.length)
					{
						
						if((input2).substring(3).equals(fighterAttacks[i].getName()))
						{
							oldAttack = fighterAttacks[i];
						}
					}
				}
				if(newAttack!=null)
				{
					if (newAttack instanceof SuperAttack)
					{
						try
						{
							game.getPlayer().assignAttack(game.getPlayer().getActiveFighter(), (SuperAttack) newAttack, (SuperAttack)oldAttack);
							JOptionPane.showMessageDialog(worldView, "Congratulations! You just learned " + newAttack.getName());
						}
						catch (MaximumAttacksLearnedException | DuplicateAttackException | NotASaiyanException | ClassCastException e1)
						{
							if(e1 instanceof ClassCastException)
								JOptionPane.showMessageDialog(worldView,"You cannot replace Ultimate Attack with a Super Attack");
							else
							JOptionPane.showMessageDialog(worldView,e1.getMessage());
						}
					}
					else if (newAttack instanceof UltimateAttack)
					{
						try
						{
							game.getPlayer().assignAttack(game.getPlayer().getActiveFighter(), (UltimateAttack) newAttack, (UltimateAttack)oldAttack);
							JOptionPane.showMessageDialog(worldView, "Congratulations! You just learned " + newAttack.getName());
						}
						catch (MaximumAttacksLearnedException | DuplicateAttackException | NotASaiyanException | ClassCastException e1)
						{
							if(e1 instanceof ClassCastException)
								JOptionPane.showMessageDialog(worldView,"You cannot replace Super Attack with an Ultimate Attack");
							else
								JOptionPane.showMessageDialog(worldView,e1.getMessage());
						}
					}
				}
				 
			}
		}
		else if (e.getSource() instanceof JComboBox)
		{
			if(((JComboBox)e.getSource()).getSelectedItem() instanceof String)
			{
				String ability = (String) ((JComboBox)e.getSource()).getSelectedItem();
				try
				{
					switch (ability)
					{
					case "Health points":
						this.game.getPlayer().upgradeFighter(this.game.getPlayer().getActiveFighter(), 'H');
						JOptionPane.showMessageDialog(worldView, "You just spent one ability point on Health");
						break;
					case "KI":
						this.game.getPlayer().upgradeFighter(this.game.getPlayer().getActiveFighter(), 'K');
						JOptionPane.showMessageDialog(worldView, "You just spent one ability point on Ki");
						break;
					case "stamina":
						this.game.getPlayer().upgradeFighter(this.game.getPlayer().getActiveFighter(), 'S');
						JOptionPane.showMessageDialog(worldView, "You just spent one ability point on Stamina");
						break;
					case "Blast damage":
						this.game.getPlayer().upgradeFighter(this.game.getPlayer().getActiveFighter(), 'B');
						JOptionPane.showMessageDialog(worldView, "You just spent one ability point on Blast Damage");
						break;
					case "Physical Damage":
						this.game.getPlayer().upgradeFighter(this.game.getPlayer().getActiveFighter(), 'P');
						JOptionPane.showMessageDialog(worldView, "You just spent one ability point on Physical Damage");
						break;
					}
					this.worldView.updateFighter(this.game.getPlayer().getActiveFighter());
					this.worldView.updatePlayer(this.game.getPlayer());
				}
				catch (NotEnoughAbilityPointsException e1)
				{
					JOptionPane.showMessageDialog(worldView, e1.getMessage());
				}
			}
			else if(((JComboBox)e.getSource()).getSelectedItem() instanceof PlayableFighter)
			{
				PlayableFighter fighter = (PlayableFighter) ((JComboBox)e.getSource()).getSelectedItem();
				this.game.getPlayer().setActiveFighter(fighter);
			}
		}
		
		this.worldView.updateFighter(this.game.getPlayer().getActiveFighter());
		this.worldView.updatePlayer(this.game.getPlayer());
	}





	public void dragonstate (String wishtype)
	{	
		Dragon d = game.getDragons().get(new Random().nextInt(game.getDragons().size())); 
		DragonWish[] wishesList=d.getWishes();
		DragonWish chosen;
		if(wishtype.equals("Senzu Beans"))
			chosen=wishesList[0];
		else 
			if(wishtype.equals("Ability Points"))
				chosen=wishesList[1];
			else
				if(wishtype.equals("Super Attack"))
					chosen=wishesList[2];
				else
					chosen=wishesList[3];
	
		this.game.getPlayer().chooseWish(chosen);
		JOptionPane.showMessageDialog(dragonView, "Your wish has been granted! You now have " + chosen.toString());
		this.setworldpos();
		this.worldView.setVisible(true);
		worldView.updatePlayer(this.game.getPlayer());
		worldView.updateFighter(this.game.getPlayer().getActiveFighter());
		dragonView.dispose();
	}
	
	@Override
	public void onDragonCalled(Dragon dragon)
	{ 
		this.worldView.setVisible(false);
		
		dragonView=new DragonView();
		
		for (int i = 0; i < dragonView.getDragonbuttons().size(); i++)
		{
			dragonView.getDragonbuttons().get(i).addActionListener(this);	
		}
		dragonView.setVisible(true);
	}

	@Override
	public void onCollectibleFound(Collectible collectible)
	{
		if(collectible == Collectible.SENZU_BEAN)
		{
			
			JOptionPane.showMessageDialog(worldView, "Now you have " + game.getPlayer().getSenzuBeans() + " senzu beans!", "You have found a " + collectible + "!", JOptionPane.INFORMATION_MESSAGE/* TODO addIcon*/);
		}else{
			if(game.getPlayer().getDragonBalls()<7)
				JOptionPane.showMessageDialog(worldView, "Now you have " + game.getPlayer().getDragonBalls() + " DragonBalls!", "You have found a " + collectible + "!", JOptionPane.INFORMATION_MESSAGE/* TODO addIcon*/);
			else
			{
				JOptionPane.showMessageDialog(this.dragonView,"You have collected 7 Dragon Balls \n Choose a wish from the Dragon's wish list");      		
			}
		}
		this.worldView.updateFighter(this.game.getPlayer().getActiveFighter());
		this.worldView.updatePlayer(this.game.getPlayer());
	}
	
	@Override
	public void onBattleEvent(BattleEvent e)
	{ 
		//this method is called by action performed
		
		BattleEventType b=e.getType();
		currbattle=(Battle) e.getSource();
		
		NonPlayableFighter foe = (NonPlayableFighter)this.currbattle.getFoe();
		PlayableFighter me = (PlayableFighter)this.currbattle.getMe();
		
		if(b==BattleEventType.STARTED)
		{	
			JOptionPane.showMessageDialog(worldView,"Get ready for the fight");
			
			this.worldView.setVisible(false);
		
			this.battleView=new BattleView();
      
			ArrayList<Attack> a=this.currbattle.getAssignedAttacks();
			Attack [] att=new Attack[a.size()];
			for (int i = 0; i < att.length; i++)
			{
				att[i]=a.get(i);
			}
			this.battleView.addAttacks(att);
			
			battleView.setListener(this);
			
			this.battleView.updateBattleText(true, "The enemy, " + ((NonPlayableFighter)(this.currbattle.getFoe())).getName() + " wants to fight you!");
			
		}else{
			if(b==BattleEventType.ENDED)
			{
				BattleOpponent w=e.getWinner();
		
				if(w==this.game.getPlayer().getActiveFighter())
				{
					JOptionPane.showMessageDialog(battleView,"You Won");
				}else{
					JOptionPane.showMessageDialog(battleView,"You Lost");
				}
			
				this.setworldpos();
				this.worldView.updateFighter(game.getPlayer().getActiveFighter());
				this.worldView.updatePlayer(this.game.getPlayer());
				this.worldView.setVisible(true);
				this.battleView.dispose();
		 
			}
		}
			
		this.battleView.updatePlayerStats(me);
		this.battleView.updateEnemyStats(foe);
	
		if(b==BattleEventType.NEW_TURN)
		{
			String whosturn = ((Fighter)this.currbattle.getAttacker()).getName();
			battleView.updateTurn(whosturn);
			
			if(this.currbattle.getAttacker()==this.currbattle.getFoe())
			{		
				Timer timer=new Timer();
					
				timer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						Attack attack = null;
						try
						{
							attack =currbattle.play();
							battleView.updateBattleText(false, ((NonPlayableFighter)(currbattle.getFoe())).getName() + " attacked you!");
						}
						catch (NotEnoughKiException e)
						{
							currbattle.block();
							battleView.updateBattleText(false, ((NonPlayableFighter)(currbattle.getFoe())).getName() + " is blocking");
						}
						if(attack == null)
						{
							battleView.updateBattleText(false, ((NonPlayableFighter)(currbattle.getFoe())).getName() + " is blocking");
						} else {
							battleView.updateBattleText(false, ((NonPlayableFighter)(currbattle.getFoe())).getName() + " used the attack " + attack.getName());
						}
					}
				}, 1000);
			}
		}
	}
	
	public void battlestate(ActionEvent e)
	{
		if(currbattle.getAttacker()==currbattle.getMe())
		{
			if(e.getSource() instanceof JButton)
			{
				String uOrb=((JButton)e.getSource()).getText();
				if(uOrb.equals("Use"))
				{
					try
					{
						this.currbattle.use(this.game.getPlayer(), Collectible.SENZU_BEAN);
						this.battleView.updateBattleText(true, "You  used a senzu bean.");
					}
					catch (NotEnoughSenzuBeansException e1)
					{
						JOptionPane.showMessageDialog(battleView,e1.getMessage());
					}
				}else{
					if(uOrb.equals("Block"))
					{
						this.battleView.updateBattleText(true, "You are blocking.");
						this.currbattle.block();
					}	
				}
			}else{
				if(e.getSource() instanceof JComboBox)
				{
					Attack attack = (Attack) battleView.getAttacks().getSelectedItem();	
					try
					{
						this.currbattle.attack(attack);
						this.battleView.updateBattleText(true, "You used the attack " + attack.getName() + ".");
					}
					catch (NotEnoughKiException e1)
					{
						JOptionPane.showMessageDialog(battleView,e1.getMessage());
					}		
				}
			}
		}else
			JOptionPane.showMessageDialog(battleView,"Please, wait for the foe to play!");
	}
	

	@Override
	public void keyPressed(KeyEvent e)
	{
		JButton [][]buttons=worldView.getB();
		
		switch (e.getKeyCode()) {
		
        case KeyEvent.VK_UP:
          
        	try
        	{
        		this.game.getWorld().moveUp();
        	}
        	catch( MapIndexOutOfBoundsException e1)
        	{
        		JOptionPane.showMessageDialog(worldView,e1.getMessage());
            }
        	break;
        case KeyEvent.VK_DOWN:
        	try
        	{
        		this.game.getWorld().moveDown();
            }
        	catch( MapIndexOutOfBoundsException e1)
        	{
          	  	JOptionPane.showMessageDialog(worldView,e1.getMessage());	 
            }
        	break;
        case KeyEvent.VK_LEFT:
        	try
        	{
        		this.game.getWorld().moveLeft();
            }
        	catch( MapIndexOutOfBoundsException e1)
        	{
          	 
          	  	JOptionPane.showMessageDialog(worldView,e1.getMessage());	
          	  
            }
        	break;
        case KeyEvent.VK_RIGHT:
        	try
        	{
        		this.game.getWorld().moveRight();
            }
        	catch( MapIndexOutOfBoundsException e1)
        	{
        		JOptionPane.showMessageDialog(worldView,e1.getMessage());	  
            }
        	break;
        default:
           break;
        }
		this.setworldpos();
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub	
	}
	
	
	public void setworldpos()
	{	
		this.worldView.getB()[prow][pcol].setIcon(null);
		this.prow=this.game.getWorld().getPlayerRow();
		this.pcol=this.game.getWorld().getPlayerColumn();
		this.worldView.getB()[0][0].setIcon(worldView.getBoss());
		this.worldView.getB()[prow][pcol].setIcon(worldView.getC());
		this.worldView.getB()[prow][pcol].requestFocus();
	}
	
	public static void main (String[]args)
	{ 			
		new Controller();
	}
}