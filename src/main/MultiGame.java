package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import mine.MineGame;
import pingpong.PingPongGame;
import run.RunGame;
import shooting.ShootingGame;


public class MultiGame extends JFrame {
	public static final int SCREEN_WIDTH = 443;
	public static final int SCREEN_HEIGHT = 624;

	// ���� �̹���
	private Image bufferImage;
	private Graphics screenGraphic;

	// ��ũ�� �̹���
	Image[] screen;
	int select=0;

	// ��ư �̹���
	private JButton leftbtn =new JButton();
	private JButton centerbtn=new JButton();
	private JButton rightbtn =new JButton();

	public MultiGame() {
		screen = new Image[5];
		for(int i=0; i<5; i++)
			screen[i]=new ImageIcon("src/images/screen"+i+".png").getImage();

		// ���ν�ũ��
		setUndecorated(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);

		// ���� ��ư
		leftbtn.setBounds(43, 553, 50, 50);
		leftbtn.setBorderPainted(false);
		leftbtn.setContentAreaFilled(false);
		leftbtn.setFocusPainted(false);
		// ���� ��ư ������ (ȭ����ȯ)
		leftbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(select==0)
					select = 4;
				else 
					select -= 1;
				requestFocus();
			}
		});
		add(leftbtn);
		
		// ���ӽ��� ��ư
		centerbtn.setBounds(133, 548, 181, 62);
		centerbtn.setBorderPainted(false);
		centerbtn.setContentAreaFilled(false);
		centerbtn.setFocusPainted(false);
		// ���ӽ��� ��ư ������ (ȭ����ȯ)
		centerbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(select != 0) {
					leftbtn.setVisible(false);
					centerbtn.setVisible(false);
					rightbtn.setVisible(false);
					switch (select) {
					case 1: 
						new RunGame();
						setVisible(false);
						break;
					case 2: 
						new ShootingGame();
						setVisible(false);
						break;
					case 3: 
						new MineGame();
						setVisible(false);
						break;
					case 4: 
						new PingPongGame();
						setVisible(false);
						break;
					default:
						break;
					}
				}
			}
		});
		add(centerbtn);
		
		// ������ ��ư
		rightbtn.setBounds(356, 553, 50, 50);
		rightbtn.setBorderPainted(false);
		rightbtn.setContentAreaFilled(false);
		rightbtn.setFocusPainted(false);
		// ������ ��ư ������ (ȭ����ȯ)
		rightbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(select==4)
					select = 0;
				else 
					select += 1;
				requestFocus();
			}
		});
		add(rightbtn);

		init();
	}
	public static void main(String[] args) {
		new MultiGame();
	}

	// ��ũ�� �ʱⰪ
	private void init() {
		select = 0;
		setFocusable(true); // ��ư�� ��Ŀ���� ���� �ʵ��� �����ӿ� ��Ŀ���� �ξ� Ű������ Ȱ��ȭ��Ų��.
		addKeyListener(new KeyListener());
	}

	// �ε巯�� ȭ������� �����̹��� ����
	public void paint(Graphics g) {
		bufferImage = createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}

	// ȭ����ȯ
	public void screenDraw(Graphics g) {

		for(int i=0; i<5; i++) {
			if(select == i) {
				g.drawImage(screen[i], 0, 0, null);
			}
		}
		paintComponents(g);	// ��ư�� ���̵��� Ȱ��ȭ
		this.repaint();
	}

	// Ű �Է½� �̺�Ʈ �߻�
	// Ű�����ʿ� ��������� -> �����ʴ� �޼ҵ带 �� �޾�����ϴ¹ݸ� ����ʹ� ���ϴ� �޼ҵ常 ȣ�Ⱑ��
	class KeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			}
		}
	}
}
