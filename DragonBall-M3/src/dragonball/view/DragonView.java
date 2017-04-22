package dragonball.view;


import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class DragonView extends JFrame{
	
   
	private ArrayList<JButton>Dragonbuttons;
	
	public DragonView(){
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	
		this.setTitle("Dragon Mode");
		 setExtendedState(JFrame.MAXIMIZED_BOTH);
	setUndecorated(true);

		
	
		//URL url = this.getClass().getResource("shenron1.gif");
		ImageIcon imageIcon = new ImageIcon("shenron1.jpg");
		JLabel imageplace = new JLabel(imageIcon);
		//imageplace.setLayout(null);	
		//JLabel label = new JLabel(new ImageIcon(ImageIO.read(new File(this.DragonName+".gif"))));
		//this.setContentPane(imageplace);
		this.add(imageplace);
		Image icon = new javax.swing.ImageIcon("ball.png").getImage();

		this.setIconImage(icon);
		
		
		
		
		JButton close = new JButton("Exit Game");
		close.setIcon(new ImageIcon("exithere.jpg"));
		
		Dragonbuttons=new ArrayList<JButton>();
		Dragonbuttons.add(close);		
		close.setContentAreaFilled(false);
 
		close.setBounds(20, 20, 100, 100);
		imageplace.add(close);

		JButton senzuBeans = new JButton("Senzu Beans");
		senzuBeans.setBounds(320, 645, 110, 15);
		imageplace.add(senzuBeans);
		
		
		this.Dragonbuttons.add(senzuBeans);
		JButton abilityPoints = new JButton("Ability Points");
		abilityPoints.setBounds(485, 670, 120, 15);
		imageplace.add(abilityPoints);
		
		
		
		this.Dragonbuttons.add(abilityPoints);		
		
		
		JButton superAttack = new JButton("Super Attack");
		this.Dragonbuttons.add(superAttack);
	
		superAttack.setBounds(830, 670, 110, 15);
		imageplace.add(superAttack);
	
	
	
	
	
		JButton ultimateAttack = new JButton("Ultimate Attack");
		ultimateAttack.setBounds(1000, 645, 110, 15);
		imageplace.add(ultimateAttack);
		
		
		this.Dragonbuttons.add(ultimateAttack);

		ultimateAttack.setOpaque(false);
		ultimateAttack.setBorder(null);
		ultimateAttack.setContentAreaFilled(false);
		
		superAttack.setOpaque(false);
		superAttack.setBorder(null);
		superAttack.setContentAreaFilled(false);
		
		abilityPoints.setOpaque(false);
		abilityPoints.setBorder(null);
		abilityPoints.setContentAreaFilled(false);
		
		senzuBeans.setOpaque(false);
		senzuBeans.setBorder(null);
		senzuBeans.setContentAreaFilled(false);
		imageplace.setBackground(Color.black);
		this.getContentPane().setBackground(Color.BLACK);
	  
		ultimateAttack.setForeground(Color.RED);
		superAttack.setForeground(Color.BLUE);
		senzuBeans.setForeground(Color.GREEN);
		abilityPoints.setForeground(Color.MAGENTA);
		ultimateAttack.setFont(new Font("Arial",Font.BOLD,12));
		superAttack.setFont(new Font("Arial",Font.BOLD,12));
		senzuBeans.setFont(new Font("Arial",Font.BOLD,12));
		abilityPoints.setFont(new Font("Arial",Font.BOLD,12));
	  
		 Image iconr = new javax.swing.ImageIcon("ball.jpg").getImage();

			this.setIconImage(iconr);
	
		this.repaint();
		this.validate();
		
	}
	
	public ArrayList<JButton> getDragonbuttons() {
		return Dragonbuttons;
	}
	
}
