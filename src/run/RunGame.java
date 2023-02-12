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
	// 버퍼 이미지
	private Image bufferImage;
	private Graphics screenGraphic;
	// 스크린화면 이미지
	private Image mainScreen = new ImageIcon("src/run_images/main_screen.png").getImage();
	private Image exScreen = new ImageIcon("src/run_images/ex_screen.png").getImage();
	private Image gameScreen = new ImageIcon("src/run_images/game_screen.png").getImage();
	// 버튼 이미지
	private JButton startbtn =new JButton();
	private JButton exbtn=new JButton();
	private JButton quitbtn =new JButton();
	// 화면전환 트리거
	private boolean isMainScreen, isExScreen, isGameScreen;
	//game객체 생성
	private Game game = new Game();

	public RunGame() {
		// 메인스크린
		setUndecorated(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		// 게임시작 버튼
		startbtn.setBounds(200, 0, 100, 180);
		startbtn.setBorderPainted(false);
		startbtn.setContentAreaFilled(false);
		startbtn.setFocusPainted(false);
		// 게임시작 버튼 리스너 (화면전환)
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
		// 게임설명 버튼
		exbtn.setBounds(315, 0, 100, 180);
		exbtn.setBorderPainted(false);
		exbtn.setContentAreaFilled(false);
		exbtn.setFocusPainted(false);
		// 게임설명 버튼 리스너 (화면전환)
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
		// 뒤로가기 버튼
		quitbtn.setBounds(410, 0, 80, 80);
		quitbtn.setBorderPainted(false);
		quitbtn.setContentAreaFilled(false);
		quitbtn.setFocusPainted(false);
		// 뒤로가기 버튼 리스너 (화면전환)
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
	// 스크린 초기값
	private void init() {
		isMainScreen = true;
		isExScreen = false;
		isGameScreen = false;
		setFocusable(true); // 버튼에 포커싱이 되지 않도록 프레임에 포커스를 두어 키리스너 활성화시킨다.
		addKeyListener(new KeyListener());
	}

	

	// 버튼 보이는 여부
	private void btnvisible(JButton btn, boolean onoff) {
		btn.setVisible(onoff);
	}

	// 부드러운 화면움직임 버퍼이미지 구현
	public void paint(Graphics g) {
		bufferImage = createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}
	// 화면전환
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
		
		paintComponents(g);	// 버튼이 보이도록 활성화
		this.repaint();
	}

	// 키 입력시 이벤트 발생
	// 키리스너와 어댑터차이 -> 리스너는 메소드를 다 달아줘야하는반면 어댑터는 원하는 메소드만 호출가능
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

		// 키 땔시 이벤트 발생
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			}
		}
	}
}
