package run;
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

import main.MultiGame;

public class RunGame extends JFrame {
	public static final int SCREEN_WIDTH = 490;
	public static final int SCREEN_HEIGHT = 830;
	// ���� �̹���
	private Image bufferImage;
	private Graphics screenGraphic;
	// ��ũ��ȭ�� �̹���
	private Image mainScreen = new ImageIcon("src/run_images/main_screen.png").getImage();
	private Image exScreen = new ImageIcon("src/run_images/ex_screen.png").getImage();
	private Image gameScreen = new ImageIcon("src/run_images/game_screen.png").getImage();
	// ��ư �̹���
	private JButton startbtn =new JButton();
	private JButton exbtn=new JButton();
	private JButton quitbtn =new JButton();
	// ȭ����ȯ Ʈ����
	private boolean isMainScreen, isExScreen, isGameScreen;
	//game��ü ����
	private Game game = new Game();

	public RunGame() {
		// ���ν�ũ��
		setUndecorated(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		// ���ӽ��� ��ư
		startbtn.setBounds(200, 0, 100, 180);
		startbtn.setBorderPainted(false);
		startbtn.setContentAreaFilled(false);
		startbtn.setFocusPainted(false);
		// ���ӽ��� ��ư ������ (ȭ����ȯ)
		startbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startbtn.setVisible(false);
				exbtn.setVisible(false);
				isMainScreen = false;
				isGameScreen = true;
				game.start();
			}
		});

		add(startbtn);
		// ���Ӽ��� ��ư
		exbtn.setBounds(315, 0, 100, 180);
		exbtn.setBorderPainted(false);
		exbtn.setContentAreaFilled(false);
		exbtn.setFocusPainted(false);
		// ���Ӽ��� ��ư ������ (ȭ����ȯ)
		exbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startbtn.setVisible(false);
				exbtn.setVisible(false);
				isMainScreen = false;
				isExScreen = true;
			}
		});
		add(exbtn);
		// �ڷΰ��� ��ư
		quitbtn.setBounds(410, 0, 80, 80);
		quitbtn.setBorderPainted(false);
		quitbtn.setContentAreaFilled(false);
		quitbtn.setFocusPainted(false);
		// �ڷΰ��� ��ư ������ (ȭ����ȯ)
		quitbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quitbtn.setVisible(false);
				isExScreen = false;
				isMainScreen = true;
			}
		});
		add(quitbtn);

		init();

	}
	// ��ũ�� �ʱⰪ
	private void init() {
		isMainScreen = true;
		isExScreen = false;
		isGameScreen = false;
		setFocusable(true); // ��ư�� ��Ŀ���� ���� �ʵ��� �����ӿ� ��Ŀ���� �ξ� Ű������ Ȱ��ȭ��Ų��.
		addKeyListener(new KeyListener());
	}

	

	// ��ư ���̴� ����
	private void btnvisible(JButton btn, boolean onoff) {
		btn.setVisible(onoff);
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

		if (isMainScreen) {
			g.drawImage(mainScreen, 0, 0, null);
			btnvisible(startbtn, true);
			btnvisible(exbtn, true);
			btnvisible(quitbtn, false);
		}
		
		if (isExScreen) {
			g.drawImage(exScreen, 0, 0, null);
			btnvisible(startbtn, false);
			btnvisible(exbtn, false);
			btnvisible(quitbtn, true);
		}
		
		if (isGameScreen) {
			g.drawImage(gameScreen, 0, 0, null);
			game.gameDraw(g);
		}
		
		paintComponents(g);	// ��ư�� ���̵��� Ȱ��ȭ
		this.repaint();
	}

	// Ű �Է½� �̺�Ʈ �߻�
	// Ű�����ʿ� ��������� -> �����ʴ� �޼ҵ带 �� �޾�����ϴ¹ݸ� ����ʹ� ���ϴ� �޼ҵ常 ȣ�Ⱑ��
	class KeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				game.setLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				game.setRight(true);
				break;
			case KeyEvent.VK_R:
				if (game.isOver()||game.isWin())
					game.reset();
				break;

			case KeyEvent.VK_ESCAPE:
				new MultiGame();
				setVisible(false);
				break;
			}
		}

		// Ű ���� �̺�Ʈ �߻�
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			}
		}
	}
}
