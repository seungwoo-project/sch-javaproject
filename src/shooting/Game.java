package shooting;
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
	private int score; // ��ǥ ���ھ� ���޽� ��������

	private Image player = new ImageIcon("src/shooting_images/player.png").getImage();

	private int playerX, playerY; // �÷��̾� ��ǥ
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);
	private int playerSpeed = 10;
	private int playerHp = 30;

	private boolean up, down, left, right, shooting; // ������, ���� ����
	private boolean isOver, isWin;	// �¸� , ���� ����

	private ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>(); // ���ݷ����� ����Ʈ
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>(); // �� ��ü ����Ʈ
	private ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>(); // �� ���ݷ����� ����Ʈ
	private ArrayList<BossAttack> bossAttackList = new ArrayList<BossAttack>(); // ���� ���ݷ����� ����Ʈ
	private ArrayList<Item> itemList = new ArrayList<Item>(); // ������ ����Ʈ

	private PlayerAttack playerAttack;
	private Enemy enemy;
	private EnemyAttack enemyAttack;
	private Boss boss;
	private BossAttack bossAttack;
	private Item item;
	private int value = 25;  // ���ݼӵ� ��������
	private int mp;	// mp��

	// ������ �� �޼ҵ� ȣ��
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
						playerAttackProcess();
						enemyAppearProcess(2);	// ���ӽ��� �� �ʱ� ������ü n�� ����
						enemyMoveProcess();
						enemyAttackProcess();
						bossAppearProcess();
						bossMoveProcess();
						bossAttackProcess();
						itemEatProcess();
						cnt++;
						itemAppearProcess();
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
	// �����
	public void reset() {
		isWin = false;
		isOver = false;
		cnt = 0;
		score = 0;
		value = 25;
		playerHp = 30;
		mp = 0;
		playerX = 220;
		playerY = 700;

		playerAttackList.clear();
		enemyList.clear();
		enemyAttackList.clear();
		boss=null;
		bossAttackList.clear();
		itemList.clear();
	}
	// �÷��̾� ������
	private void keyProcess() {
		if (up && playerY - playerSpeed > 0)
			playerY -= playerSpeed;
		if (down && playerY + playerHeight + playerSpeed < ShootingGame.SCREEN_HEIGHT)
			playerY += playerSpeed;
		if (left && playerX - playerSpeed > 0)
			playerX -= playerSpeed;
		if (right && playerX + playerWidth + playerSpeed < ShootingGame.SCREEN_WIDTH)
			playerX += playerSpeed;
		// �÷��̾� ���� �� ���ݼӵ�
		if (shooting && cnt % value == 0) {
			playerAttack = new PlayerAttack(playerX + 17, playerY - 30);
			playerAttackList.add(playerAttack);
		}
	}

	private void playerAttackProcess() {
		for (int i = 0; i < playerAttackList.size(); i++) {
			playerAttack = playerAttackList.get(i);
			playerAttack.fire();

			for (int j = 0; j < enemyList.size(); j++) {
				enemy = enemyList.get(j);
				// �� ��Ʈ�ڽ� ũ��
				if (playerAttack.x > enemy.x - 10 && playerAttack.x < enemy.x + 10 + enemy.width
						&& playerAttack.y > enemy.y && playerAttack.y < enemy.y + enemy.height) {
					enemy.hp -= playerAttack.attack;
					playerAttackList.remove(playerAttack);
				}
				if (enemy.hp <= 0) {
					enemyList.remove(enemy);
					score += 1000;
				}
				// ���� ��Ʈ�ڽ� ũ��
				if (boss != null) {
					if (playerAttack.x > boss.x - 10 && playerAttack.x < boss.x + boss.width && playerAttack.y > boss.y
							&& playerAttack.y < boss.y + boss.height - 20) {
						boss.hp -= playerAttack.attack;
						playerAttackList.remove(playerAttack);
					}
					if (boss.hp <= 0) {
						boss = null;
						isWin = true;
					}
				}
			}
		}
	}

	// �� ��ü ��ü ����
	private void enemyAppearProcess(int n) {
		if (cnt == 0) {
			for (int i = 0; i < n; i++) {
				enemy = new Enemy((int) (Math.random() * 350) + 30, (int) (Math.random() * 300) + 30);
				enemyList.add(enemy);
			}
		}
		if (score<5000) {
			if(cnt % 120 == 0) {
			enemy = new Enemy((int) (Math.random() * 350) + 30, (int) (Math.random() * 300) + 30);
			enemyList.add(enemy);
			}
		}
		else if (score>=5000) {
			if(cnt % 50 == 0) {
				enemy = new Enemy((int) (Math.random() * 350) + 30, (int) (Math.random() * 300) + 30);
				enemyList.add(enemy);
				}
		}
	}

	// �� ��ƨ��� ������
	private void enemyMoveProcess() {
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			enemy.move();
			if (enemy.x <= 0 || enemy.x >= ShootingGame.SCREEN_WIDTH - enemy.width - 10)
				enemy.movex = enemy.movex * -1;

			if (enemy.y <= 0 || enemy.y >= ShootingGame.SCREEN_HEIGHT / 2 - enemy.height)
				enemy.movey = enemy.movey * -1;
		}
	}

	// �� ����
	private void enemyAttackProcess() {
		for (int i = 0; i < enemyList.size(); i++) {
			if (cnt % 30 == 0) {
				enemyAttack = new EnemyAttack(enemyList.get(i).x + 11, enemyList.get(i).y + 40);
				enemyAttackList.add(enemyAttack);
			}
		}

		for (int i = 0; i < enemyAttackList.size(); i++) {
			enemyAttack = enemyAttackList.get(i);
			enemyAttack.fire();

			if (enemyAttack.x > playerX - 3 && enemyAttack.x < playerX + 3 + playerWidth && enemyAttack.y > playerY
					&& enemyAttack.y < playerY + playerHeight) {
				playerHp -= enemyAttack.attack;
				enemyAttackList.remove(enemyAttack);
				if (playerHp <= 0)
					isOver = true;
			}
		}
	}

	// ���� ��ü ����
	private void bossAppearProcess() {
		if (score == 5000 && boss == null) {
			boss = new Boss(200, 200);
		}
	}

	// ���� ��ƨ��� ������
	private void bossMoveProcess() {
		if (boss != null) {
			boss.move();
			if (boss.x <= 0 || boss.x >= ShootingGame.SCREEN_WIDTH - boss.width)
				boss.movex = boss.movex * -1;

			if (boss.y <= 0 || boss.y >= ShootingGame.SCREEN_HEIGHT / 2 - boss.height)
				boss.movey = boss.movey * -1;
		}
	}

	// ���� ����
	private void bossAttackProcess() {
		if (boss != null) {
			if (cnt % 20 == 0) {
				bossAttack = new BossAttack(boss.x + 35, boss.y + 130);
				bossAttackList.add(bossAttack);
			}

			for (int i = 0; i < bossAttackList.size(); i++) {
				bossAttack = bossAttackList.get(i);
				bossAttack.fire();

				if (bossAttack.x > playerX - 3 && bossAttack.x < playerX + 3 + playerWidth && bossAttack.y > playerY
						&& bossAttack.y < playerY + playerHeight) {
					playerHp -= bossAttack.attack;
					bossAttackList.remove(bossAttack);
					if (playerHp <= 0)
						isOver = true;
				}
			}
		}
	}

	// ������ ����
	private void itemAppearProcess() {
		if (cnt % 200 == 0) {
			item = new Item((int) (Math.random() * 430) + 30, (int) (Math.random() * 360) + 430,
					(int) (Math.random() * 4) + 1);
			itemList.add(item);
		}
	}

	// ������ �Ա�
	private void itemEatProcess() {
		if (item != null) {
			for (int i = 0; i < itemList.size(); i++) {
				item = itemList.get(i);

				if (itemList.get(i).x+(itemList.get(i).width/2) > playerX && itemList.get(i).x+(itemList.get(i).width/2) < playerX + playerWidth
						&& itemList.get(i).y+(itemList.get(i).height/2) > playerY && itemList.get(i).y+(itemList.get(i).height/2) < playerY + playerHeight) {
					// ���ݼӵ� ����
					if (itemList.get(i).select == 2 || itemList.get(i).select == 3 || itemList.get(i).select == 4) {
						if (value != 5) {
							value -= 5;
							mp++;
						} else
							value = 5;
					}
					// Hp ȸ�� ����
					if (itemList.get(i).select == 1) {
						if (playerHp != 30)
							playerHp += 5;
						else
							playerHp += 0;
					}
					itemList.remove(item);
				}
			}
		}
	}

	public void gameDraw(Graphics g) {
		playerDraw(g);
		enemyDraw(g);
		infoDraw(g);
		bossDraw(g);
		itemDraw(g);
	}

	// ���� �۾�
	public void infoDraw(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("�ձٸ��", Font.BOLD, 40));
		g.drawString("SCORE : " + score, 15, 60);

		g.setColor(Color.WHITE);
		g.setFont(new Font("�ձٸ��", Font.PLAIN, 30));
		g.drawString("HP : ", 10, 760);

		g.setColor(Color.WHITE);
		g.setFont(new Font("�ձٸ��", Font.PLAIN, 30));
		g.drawString("MP : ", 10, 800);
		if (isOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("�ձٸ��", Font.BOLD, 40));
			g.drawString("�й�!", 200, 420);
			g.setFont(new Font("�ձٸ��", Font.BOLD, 28));
			g.drawString("������ �������� escŰ�� ��������", 13, 480);
			g.drawString("�ٽ� �����Ϸ��� RŰ�� ��������", 28, 520);
		}
		if (isWin) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("�ձٸ��", Font.BOLD, 40));
			g.drawString("�¸�!", 200, 420);
			g.setFont(new Font("�ձٸ��", Font.BOLD, 28));
			g.drawString("������ �������� escŰ�� ��������", 13, 480);
			g.drawString("�ٽ� �����Ϸ��� RŰ�� ��������", 28, 520);
		}
	}

	// �÷��̾� ü�¹� �� �÷��̾� ���� �׸���
	public void playerDraw(Graphics g) {
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.RED);
		g.fillRect(80, 740, playerHp * 12, 20);
		for (int i = 0; i < playerAttackList.size(); i++) {
			playerAttack = playerAttackList.get(i);
			g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
		}
	}

	// �� ��ü �� ü�¹� �� �� ���� �׸���
	public void enemyDraw(Graphics g) {
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			g.drawImage(enemy.image, enemy.x, enemy.y, null);
			g.setColor(Color.GREEN);
			g.fillRect(enemy.x - 12, enemy.y - 40, enemy.hp * 2, 10);
		}
		for (int i = 0; i < enemyAttackList.size(); i++) {
			enemyAttack = enemyAttackList.get(i);
			g.drawImage(enemyAttack.image, enemyAttack.x, enemyAttack.y, null);
		}
	}

	// ���� ��ü �� ü�¹� �� ���� ���� �׸���
	public void bossDraw(Graphics g) {
		if (boss != null) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("�ձٸ��", Font.PLAIN, 30));
			g.drawString("Boss: ", 10, 20);
			g.drawImage(boss.image, boss.x, boss.y, null);
			g.setColor(Color.RED);
			g.fillRect(90, 9, boss.hp, 10);
			for (int i = 0; i < bossAttackList.size(); i++) {
				bossAttack = bossAttackList.get(i);
				g.drawImage(bossAttack.image, bossAttack.x, bossAttack.y, null);
			}
		}
	}

	// ������ �� mp�� �׸���
	public void itemDraw(Graphics g) {
		if (item != null) {
			g.setColor(Color.BLUE);
			g.fillRect(80, 780, mp * 90, 20);
			for (int i = 0; i < itemList.size(); i++) {
				item = itemList.get(i);
				g.drawImage(item.image, item.x, item.y, null);
			}
		}
	}
	// ���� �Լ�
	public boolean isOver() {
		return isOver;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}

}