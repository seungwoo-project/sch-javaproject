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

	// 버퍼 이미지
	private Image bufferImage;
	private Graphics screenGraphic;

	// 스크린 이미지
	Image[] screen;
	int select=0;

	// 버튼 이미지
	private JButton leftbtn =new JButton();
	private JButton centerbtn=new JButton();
	private JButton rightbtn =new JButton();

	public MultiGame() {
		screen = new Image[5];
		for(int i=0; i<5; i++)
			screen[i]=new ImageIcon("src/images/screen"+i+".png").getImage();

		// 메인스크린
		setUndecorated(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);

		// 왼쪽 버튼
		leftbtn.setBounds(43, 553, 50, 50);
		leftbtn.setBorderPainted(false);
		leftbtn.setContentAreaFilled(false);
		leftbtn.setFocusPainted(false);
		// 왼쪽 버튼 리스너 (화면전환)
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
		
		// 게임시작 버튼
		centerbtn.setBounds(133, 548, 181, 62);
		centerbtn.setBorderPainted(false);
		centerbtn.setContentAreaFilled(false);
		centerbtn.setFocusPainted(false);
		// 게임시작 버튼 리스너 (화면전환)
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
		
		// 오른쪽 버튼
		rightbtn.setBounds(356, 553, 50, 50);
		rightbtn.setBorderPainted(false);
		rightbtn.setContentAreaFilled(false);
		rightbtn.setFocusPainted(false);
		// 오른쪽 버튼 리스너 (화면전환)
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

	// 스크린 초기값
	private void init() {
		select = 0;
		setFocusable(true); // 버튼에 포커싱이 되지 않도록 프레임에 포커스를 두어 키리스너 활성화시킨다.
		addKeyListener(new KeyListener());
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

		for(int i=0; i<5; i++) {
			if(select == i) {
				g.drawImage(screen[i], 0, 0, null);
			}
		}
		paintComponents(g);	// 버튼이 보이도록 활성화
		this.repaint();
	}

	// 키 입력시 이벤트 발생
	// 키리스너와 어댑터차이 -> 리스너는 메소드를 다 달아줘야하는반면 어댑터는 원하는 메소드만 호출가능
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
