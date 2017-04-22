package dragonball.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import dragonball.controller.Controller;
import dragonball.model.attack.Attack;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughKiException;

public class BattleView extends JFrame
{
	private JLabel mainPanel;
	private JLabel commandPanel;
	private JTextArea turn;
	private JTextArea enemyStats;
	private JTextArea playerStats;
	private JProgressBar foeHP;
	private JProgressBar playerHP;
	private int turnNumber;
	private JComboBox<Attack> attacks;
	private JButton use;
	private JButton a;
	private JTextArea battleText;
	
	public JTextArea getTurn() {
		return turn;
	}

	public void setTurn(JTextArea turn) {
		this.turn = turn;
	}

	public JComboBox<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(JComboBox<Attack> attacks) {
		this.attacks = attacks;
	}

	public JButton getUse() {
		return use;
	}

	public void setUse(JButton use) {
		this.use = use;
	}

	public JButton getBlock() {
		return block;
	}

	public void setBlock(JButton block) {
		this.block = block;
	}

	private JButton block;
	
	public BattleView ()
	{
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		Image icon = new javax.swing.ImageIcon("ball.png").getImage();
		this.setIconImage(icon);
		setUndecorated(true);
		this.setVisible(true);
		this.setTitle("Battle Mode!");
		
		turnNumber = 0;
		
		//reszed 1450
		ImageIcon l= new ImageIcon("bbg.jpg");
		mainPanel = new JLabel(l);
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(1000,575));
		this.add(mainPanel, BorderLayout.NORTH);
		
		//resized 1450
		
		ImageIcon s=new ImageIcon("ban.png");
		commandPanel = new JLabel(s);
		commandPanel.setLayout(null);
		commandPanel.setPreferredSize(new Dimension(5,300));
		this.add(commandPanel, BorderLayout.CENTER);
				
		this.use = new JButton("Use");
		use.setForeground(Color.GREEN);
		use.setBounds(950, 45, 150, 150);
		use.setOpaque(false);
		use.setBorder(null);
		use.setContentAreaFilled(false);
		commandPanel.add(use);
		
		block = new JButton("Block");
		block.setForeground(Color.RED);
		block.setBounds(780,45,150,150);
		block.setOpaque(false);
		block.setBorder(null);
		block.setContentAreaFilled(false);
		commandPanel.add(block);
		
		
		turn = new JTextArea();
		turn.setEditable(false);
		turn.setOpaque(false);
		turn.setBackground(new Color(0,0,0,0));
		turn.setBounds(640, 25, 134, 97);
		mainPanel.add(turn);
		ImageIcon x = new ImageIcon("lbox.jpg");
		JLabel z = new JLabel(x);
		z.setBounds(600, 15, 134, 97);
		mainPanel.add(z);

		battleText = new JTextArea();
		battleText.setForeground(Color.BLACK);
		battleText.setEditable(false);
		battleText.setOpaque(false);
		battleText.setBounds(20,20,730,500);
		battleText.setFont(new Font("Impact",Font.ITALIC,30));
		commandPanel.add(battleText);
		
		JTextArea lel = new JTextArea("Which move do you want to use?");
		lel.setForeground(Color.BLUE);
		lel.setEditable(false);
		lel.setOpaque(false);
		lel.setBackground(new Color(0,0,0,0));
		lel.setBounds(800,10,500,50);
		commandPanel.add(lel);
	
		
		//resized 350 148
		ImageIcon k=new ImageIcon("box.jpg");
		JLabel n=new JLabel(k);
		n.setBounds(10, 40, 350, 148);
		enemyStats = new JTextArea();
		enemyStats.setEditable(false);
		enemyStats.setOpaque(false);
	    enemyStats.setBackground(new Color(0,0,0,0));
		enemyStats.setBounds(20, 20, 350, 148);
		n.add(enemyStats);
		mainPanel.add(n);
		
		foeHP = new JProgressBar();
		foeHP.setBounds(13, 15, 343, 20);
		mainPanel.add(foeHP);
		
		playerStats = new JTextArea();
		mainPanel.add(playerStats);
		playerStats.setEditable(false);
		playerStats.setOpaque(false);
		playerStats.setBackground(new Color(0,0,0,0));
		playerStats.setBounds(1030, 425, 350, 150);
		ImageIcon w1 =new ImageIcon("box.jpg");
		JLabel q= new JLabel(w1);
		q.setBounds(1015, 405, 350, 150);
		mainPanel.add(q);
		
		playerHP = new JProgressBar();
		playerHP.setBounds(1015,375,340,20);
		mainPanel.add(playerHP);
		
		a = new JButton("Exit Game");
		a.setBounds(1250,5,100,20);
		mainPanel.add(a);
		
		ImageIcon r = new ImageIcon("frieza.png");
		JLabel y = new JLabel(r);
		y.setBounds(850,-40,500,500);
		mainPanel.add(y);
		
		ImageIcon v= new ImageIcon("goku.png");
		JLabel u = new JLabel(v);
		u.setBounds(50,140,500,500);
		mainPanel.add(u);
		this.repaint();
		this.validate();
		
		use.setFont(new Font("Arial",Font.BOLD,40));
		block.setFont(new Font("Arial",Font.BOLD,40));
		turn.setFont(new Font("Arial",Font.BOLD,15));
		enemyStats.setFont(new Font("Arial",Font.BOLD,15));
		playerStats.setFont(new Font("Arial",Font.BOLD,15));
		lel.setFont(new Font("Arial",Font.BOLD,30));
	  
		 Image iconr = new javax.swing.ImageIcon("ball.jpg").getImage();

			this.setIconImage(iconr);
		
	}
	
	public void addAttacks(Attack[] allAttacks)
	{
		attacks = new JComboBox<Attack>(allAttacks);
		attacks.setBounds(1120,45,150,150);
		attacks.setOpaque(false);
		attacks.setBorder(null);
		commandPanel.add(attacks);
		attacks.setRenderer(new AttackListCellRenderer());
		this.repaint();
		this.validate();
	}
	
	public void updateEnemyStats(NonPlayableFighter foe)
	{
		String enemyStats = "Name : "+foe.getName() + "\n";
		enemyStats += "Level : "+foe.getLevel() + "\n";
		enemyStats += "Health : "+foe.getHealthPoints() + "/" + foe.getMaxHealthPoints() + "\n";
		enemyStats += "Stamina : "+foe.getStamina() + "/" + foe.getMaxStamina() + "\n";
		enemyStats += "KI : "+foe.getKi() + "/" + foe.getMaxKi();
		
		this.foeHP.setValue((int)((double)foe.getHealthPoints()/(double)foe.getMaxHealthPoints()*100));
		
		this.enemyStats.setText(enemyStats);
		
		this.repaint();
		this.validate();
	}
	
	public void updatePlayerStats(PlayableFighter me)
	{
		String playerStats = "Name : "+me.getName() + "\n";
		playerStats += "Level : "+me.getLevel() + "\n";
		playerStats += "Health : "+me.getHealthPoints() + "/" + me.getMaxHealthPoints() + "\n";
		playerStats += "Stamina : "+me.getStamina() + "/" + me.getMaxStamina() + "\n";
		playerStats += "kI : "+me.getKi() + "/" + me.getMaxKi();
		if((me instanceof Saiyan)&&(((Saiyan)me).isTransformed()))
		{
			playerStats += "\n SUPER SAIYAN!";
		}
		
		this.playerHP.setValue((int)((double)me.getHealthPoints()/(double)me.getMaxHealthPoints()*100));
		
		this.playerStats.setText(playerStats);
		
		this.repaint();
		this.validate();
	}
	
	public void updateTurn(String whosturn)
	{
		
		
	    String turn = "";
		
		turn += whosturn + "\n" + (++turnNumber);
		this.turn.setText(turn);
		
		this.repaint();
		this.validate();
		
		
	}
	
	public void updateBattleText(boolean me, String inputText)
	{	
				String battleTextIn = "";
				while(inputText.length()>40)
				{
					battleTextIn += inputText.substring(0,39);
					if(inputText.charAt(40)==' ')
					{
						battleTextIn += "\n";
					}else{
						battleTextIn += "-\n";
					}
					inputText= inputText.substring(39, (inputText.length()));
				}
				battleTextIn += inputText;
				
				battleText.setText(battleTextIn);
				if(me)
				{
					battleText.setForeground(Color.BLACK);
				}else{
					battleText.setForeground(Color.RED);
				}
			
		
		this.repaint();
		this.validate();
	}

	public static void main(String[] args) {
		//new BattleView();
	}
	
	
	public void setListener (Controller c)
	{
		attacks.addActionListener(c);
		use.addActionListener(c);
		block.addActionListener(c);
		a.addActionListener(c);
	}
}