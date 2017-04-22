package dragonball.view;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


import dragonball.controller.Controller;

public class MainMenu extends JFrame
{
	private JButton NewGame;
	private JButton saved;
	private JButton close;
	private ImageIcon picture;

	JLabel BackGround;
	
	
	public MainMenu()
	{
		super();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Image icon = new javax.swing.ImageIcon("ball.png").getImage();

		this.setIconImage(icon);
		this.getContentPane().setLayout(new BorderLayout());
		this.setVisible(true);
		this.setTitle("DragonBall Adventures!");
		
		picture = new ImageIcon("Trial02.jpg");
		
		BackGround = new JLabel(picture);
		BackGround.setBounds(0, 0, 500, 500);
		BackGround.setLayout(null);
		this.add(BackGround);
		
		NewGame = new JButton("New Game");
		NewGame.setFont(new Font("Impact",Font.PLAIN,30));
		NewGame.setForeground(Color.BLACK);
		NewGame.setContentAreaFilled(false);
		NewGame.setBounds(420, 560, 250, 50);
		BackGround.add(NewGame);
		
		saved = new JButton("Load Saved Game");
		saved.setFont(new Font("Impact",Font.PLAIN,30));
		saved.setForeground(Color.BLACK);
		saved.setContentAreaFilled(false);
		saved.setBounds(680, 560, 250, 50);
		BackGround.add(saved);
		
		close = new JButton("Exit Game");
		close.setFont(new Font("Impact",Font.PLAIN,30));
		close.setBounds(600, 700, 150, 50);
		BackGround.add(close);
		

		
Image iconr = new javax.swing.ImageIcon("ball.jpg").getImage();

this.setIconImage(iconr);
		
		this.repaint();
		this.validate();
	}
	
	public void setListener(Controller c)
	{
		this.NewGame.addActionListener(c);
		this.saved.addActionListener(c);
		close.addActionListener(c);
	}
	
	public static void main (String [] args)
	{
		new MainMenu();
	}
}