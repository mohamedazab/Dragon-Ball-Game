package dragonball.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import dragonball.controller.Controller;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.player.Player;


public class WorldView extends JFrame
{	
	private JTextArea player;
	private JTextArea fighter;
	private JLabel p1 ;
	private JLabel p2;
	private JButton save;
	private JButton create;
	private JButton assignAttack;
	private JComboBox upgrade;
	private ImageIcon c ;
	private JButton[][] b;
	private ImageIcon i, t;
	private JComboBox<Fighter> switchFighter;
	private JButton close;
	private ImageIcon boss ;

	public JButton[][] getB()
	{
		return b;
	}

	public void setB(JButton[][] b)
	{
		this.b = b;
	}

	public JButton getSave() {
		return save;
	}

	public JButton getCreate() {
		return create;
	}

	public WorldView () 
	{
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);
		setTitle("World");
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {}
		   
		    );
		
		t=new ImageIcon("ki.jpg");
		p1=new JLabel(t);
		p1.setPreferredSize(new Dimension(500,150));
		p1.setLayout(null);
		p1.setBackground(Color.RED);
		
		close = new JButton("Exit Game");
		close.setBounds(1195, 5, 165, 20);
		p1.add(close);
		
		ImageIcon d = new ImageIcon("tun.png");
		
		player = new JTextArea();
		player.setEditable(false);
		player.setOpaque(false);
	    player.setBackground(new Color(0,0,0,0));
		player.setBounds(1302, 31, 90, 110);
		p1.add(player);
		
		fighter = new JTextArea();
		fighter.setEditable(false);
		fighter.setOpaque(false);
	    fighter.setBackground(new Color(0,0,70,40));
		fighter.setBounds(110, 23, 90, 130);
		p1.add(fighter);
		
		this.save = new JButton("Save Game");
		save.setBounds(445,5,100,15);
		p1.add(save);
		
        String m[] = {"Health points","KI","stamina","Blast damage","Physical Damage"};
		upgrade = new JComboBox(m);
		upgrade.setBounds(974,5,130,20);
		p1.add(this.upgrade);
		
		switchFighter = new JComboBox<Fighter>();
		switchFighter.setBounds(785,5,130,20);
		p1.add(switchFighter);
		
		ImageIcon de = new ImageIcon("ni.png");
		JLabel n = new JLabel("Switch Fighter: ");
		n.setBounds(695, -40,100,110);
		p1.add(n);
		JLabel ye = new JLabel(de);
		ye.setBounds(688, -35, 100, 100);
		p1.add(ye);
		
		ImageIcon ds = new ImageIcon("ni1.png");
		JLabel k = new JLabel("Upgrade: ");
		k.setBounds(920, -38, 100, 100);
		p1.add(k);
		JLabel te = new JLabel(ds);
		te.setBounds(895, -36, 100, 100);
		p1.add(te);
		
		create = new JButton("Create Character");
		create.setBounds(300,5,140,15);
		p1.add(create);
		
		
		
		assignAttack = new JButton("Assign Attack");
		assignAttack.setBounds(550, 5, 140, 15);
		p1.add(assignAttack);
	
		
	
		JLabel l1 = new JLabel("Fighter's Name: ");
		l1.setBounds(16, 5, 100, 50);
		p1.add(l1);
		
		JLabel l2= new JLabel("Level: ");
		l2.setBounds(70, 20, 100, 50);
		p1.add(l2);
		
		JLabel l3= new JLabel("Health Points: ");
		l3.setBounds(26, 35, 150, 50);
		p1.add(l3);
		
		JLabel l4= new JLabel("Blast Damage: ");
		l4.setBounds(22, 50, 150, 50);
		p1.add(l4);
		
		JLabel l5 = new JLabel("Physical Damage: ");
		l5.setBounds(3, 65, 150, 50);
		p1.add(l5);
		JLabel l6= new JLabel("KI: ");
		l6.setBounds(90, 80, 100, 50);
		p1.add(l6);
		
		JLabel l7= new JLabel("Stamina: ");
		l7.setBounds(54, 95, 100, 50);
		p1.add(l7);
		
		JLabel l8= new JLabel("Ability Points: ");
		l8.setBounds(28, 110, 150, 50);
		p1.add(l8);
		
		JLabel mn = new JLabel(d);
		mn.setBounds(-20, -20, 300, 200);
		p1.add(mn);
		
		
		JLabel l9=new JLabel("Player's Name: ");
		l9.setBounds(1215, 14, 150, 50);
		p1.add(l9);
		
		JLabel l10 = new JLabel("Senzu Beans: ");
		l10.setBounds(1223, 29, 150, 50);
		p1.add(l10);
		
		JLabel l11 = new JLabel ("Dragon Balls: ");
		l11.setBounds(1223, 44, 100, 50);
		p1.add(l11);
		
		JLabel l12 = new JLabel("Explored Maps: ");
		l12.setBounds(1215, 59, 100, 50);
		p1.add(l12);
		
		JLabel l13 = new JLabel("Active Fighter: ");
		l13.setBounds(1221, 74, 100, 50);
		p1.add(l13);
		
		JLabel l14 = new JLabel("Max Fighter's Lvl: ");
		l14.setBounds(1203, 89, 120, 50);
		p1.add(l14);
		
		ImageIcon cee = new ImageIcon("tunn.png");
		JLabel pn = new JLabel(cee);
		pn.setBounds(1130, -20, 300, 200);
		p1.add(pn);
		

		
		this.add(p1, BorderLayout.NORTH);
		
		
		i = new ImageIcon("images1.jpg"); 
		p2 = new JLabel(i);
		
		
		p2.setLayout(new GridLayout(10,10));
	    p2.setVisible(true);
		this.add(p2, BorderLayout.CENTER);
		c = new ImageIcon("images2.png");
		 b = new JButton[10][10];
		
		
  	    setVisible(true);
  	    for (int i = 0; i < 10; i++){
  	      for (int j = 0; j < 10; j++)
  	      {
  	    	
  	        b[i][j] = new JButton();
  	       b[i][j].setContentAreaFilled(false);
  	        p2.add(b[i][j]);}
  	      
  	    }
  	    boss=new ImageIcon("boss.png");
  	 
  	    
  	  Image iconr = new javax.swing.ImageIcon("ball.jpg").getImage();

		this.setIconImage(iconr);
  	 
		   
  	    fighter.setFont(new Font("Arial",Font.BOLD,12));
  	  	player.setFont(new Font("Arial",Font.BOLD,12));

		
		 repaint();
		 validate();
		
	}
	
	public JButton getClose() {
		return close;
	}

	public void setClose(JButton close) {
		this.close = close;
	}

	public void addFighter(PlayableFighter fighter)
	{
		 
			switchFighter.addItem(fighter);
		
		switchFighter.setRenderer(new PlayableFighterListCellRenderer());
		this.repaint();
		this.validate();
	}
	
	public ImageIcon getC() {
		return c;
	}
	public ImageIcon getBoss() {
		return boss;
	}
	public void setC(ImageIcon c) {
		this.c = c;
	}

	public void setPlayer(JTextArea player) {
		this.player = player;
	}

	public void setFighter(JTextArea fighter) {
		this.fighter = fighter;
	}



	public void setP1(JLabel p1)
	{
		this.p1 = p1;
	}

	public void setP2(JLabel p2)
	{
		this.p2 = p2;
	}

	public void setSave(JButton save)
	{
		this.save = save;
	}

	public void setCreate(JButton create)
	{
		this.create = create;
	}

	public JTextArea getPlayer()
	{
		return player;
	}

	public JTextArea getFighter()
	{
		return fighter;
	}


	public JLabel getP1()
	{
		return p1;
	}

	public JLabel getP2()
	{
		return p2;
	}

	public void updateFighter(PlayableFighter fighter)
	{
		String f = fighter.getName() + "\n" ;
		f += fighter.getLevel() + "\n";
		f += fighter.getHealthPoints() + "/" + fighter.getMaxHealthPoints() + "\n";
		f += fighter.getBlastDamage() + "\n";
		f += fighter.getPhysicalDamage() + "\n";
		f += fighter.getKi() + "/" + fighter.getMaxKi() + "\n";
		f += fighter.getStamina() + "/" + fighter.getMaxStamina() + "\n";
		f += fighter.getAbilityPoints() + "\n";
		this.fighter.setText(f);
	}
	
	public void updatePlayer(Player player)
	{
		String p = player.getName() + "\n";
		p += player.getSenzuBeans() + "\n";
		p += player.getDragonBalls() + "\n";
		p += player.getExploredMaps() + "\n";
		p += player.getActiveFighter().getName() + "\n";
		p += player.getMaxFighterLevel() + "\n";
		this.player.setText(p);
	}

	public void setListener(Controller o)
	{
		for (int i = 0; i < this.b.length; i++)
		{
			for (int j = 0; j < b.length; j++)
			{
				b[i][j].addKeyListener(o);
			}
		}
		this.switchFighter.addActionListener(o);
		this.upgrade.addActionListener(o);
		this.assignAttack.addActionListener(o);
		this.create.addActionListener(o);
		this.save.addActionListener(o);
		this.close.addActionListener(o);
	}
}