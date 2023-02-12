package run;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
	private int delay = 20;
	private long pretime;
	private int cnt;
	private int score=0; 

	private Image playerLeft = new ImageIcon("src/run_images/playerLeft.png").getImage();
	private Image playerRight = new ImageIcon("src/run_images/playerRight.png").getImage();
	private Image player = playerRight;
	private Image icon = new ImageIcon("src/run_images/icon.png").getImage();

	private int playerX, playerY; // 플레이어 좌표
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);
	private int playerSpeed = 100;
	private int playerHp = 5;

	private boolean  left, right; // 왼쪽, 오른쪽 변수
	private boolean isOver, isWin;	// 승리 , 실패 조건

	private ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(); // 장애물 리스트
	private Obstacle obstacle;
	private Obstacle obstacle1;
	
	// 쓰레드 런 메소드 호출
	@Override
	public void run() {

		reset();
		while (true) {
			while (!isOver && !isWin) {
				pretime = System.currentTimeMillis();
				if (System.currentTimeMillis() - pretime < delay) {
					try {
						Thread.sleep(delay - System.currentTimeMillis() + pretime);
						keyProcess();
						obstacleProcess();
					cnt++;
					score++;
					if(score>=1000)
						isWin=true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// 재시작
	public void reset() {
		isWin = false;
		isOver = false;
		cnt = 0;
		score = 0;
		playerHp = 5;
		playerX = 220;
		playerY = 650;

		obstacleList.clear();
	}
	// 플레이어 움직임
	private void keyProcess() {
		if (left && playerX - playerSpeed > 0) {
			player = playerLeft;
			playerX -= 94;
			setLeft(false);
		}
		if (right && playerX + playerWidth + playerSpeed < RunGame.SCREEN_WIDTH) {
			player = playerRight;
			playerX += 94;
			setRight(false);
		}
	}


	// 장애물 생성
	private void obstacleProcess() {
			if (cnt % 20 == 0) {
				int[] a= {20,112,206,297,387};
				 int i=((int)(Math.random()*5));
				obstacle = new Obstacle(a[i], 0); //20 ,112 , 206 , 297 , 387
				obstacleList.add(obstacle);
				 i=((int)(Math.random()*5));
				obstacle1 = new Obstacle(a[i], 0); //20 ,112 , 206 , 297 , 387
				obstacleList.add(obstacle1);
			}

		for (int i = 0; i < obstacleList.size(); i++) {
			obstacle = obstacleList.get(i);
			obstacle1 = obstacleList.get(i);
			if(score>500) {
				obstacle.setSpeed(10);
				obstacle1.setSpeed(10);
				obstacle.produce();
				obstacle1.produce();
			}
			obstacle.produce();
			obstacle1.produce();

			if (obstacle.x+obstacle.width/2 > playerX - 3 && obstacle.x+obstacle.width/2 < playerX + 3 + playerWidth && obstacle.y+obstacle.height-20 > playerY
					&& obstacle.y+obstacle.height-20 < playerY + playerHeight) {
				playerHp -= obstacle.attack;
				obstacleList.remove(obstacle);
				if (playerHp <= 0)
					isOver = true;
			
			}
		}
	}


	public void gameDraw(Graphics g) {
		playerDraw(g);
		obstacledraw(g);
		infoDraw(g);
	}

	// 정보 글씨
	public void infoDraw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, score/2 * 1, 20); //SCORE 1000일때 완주
		g.drawImage(icon, score/2-20, -5, null);
		if (isOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 40));
			g.drawString("실패!", 200, 420);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 28));
			g.drawString("게임을 끝내려면 esc키를 누르세요", 13, 480);
			g.drawString("다시 시작하려면 R키를 누르세요", 28, 520);
		}
		if (isWin) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 40));
			g.drawString("완주!", 200, 420);
			g.setFont(new Font("둥근모꼴", Font.BOLD, 28));
			g.drawString("게임을 끝내려면 esc키를 누르세요", 13, 480);
			g.drawString("다시 시작하려면 R키를 누르세요", 28, 520);
		}
	}

	// 플레이어 그리기
	public void playerDraw(Graphics g) {
		g.drawImage(player, playerX, playerY, null);
	}

	// 장애물 그리기
	public void obstacledraw(Graphics g) {
		for (int i = 0; i < obstacleList.size(); i++) {
			obstacle = obstacleList.get(i);
			g.drawImage(obstacle.image, obstacle.x, obstacle.y, null);
		}
	}

	// 세터 함수
	public boolean isOver() {
		return isOver;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

}