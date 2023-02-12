package mine;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.MultiGame;

public class MineGUI extends JPanel implements ActionListener {
	private Color background = new Color(189, 189, 189);
	private Game game = Game.READY;
	private int rows = 16; // �� ����
	private int cols = 16; // �� ����
	private int mine = 40; // ���� ����
	private int checkMine = 40; // ��� ����
	private int cellSize = 20; // ��� ����
	private int flagCount = 0; // �߰��� ���� ����

	private JPanel progressPanel = null;
	private JPanel gamePanel = null;
	private JLabel[] mineLabel = null;
	private JLabel[] timeLabel = null;
	private JButton smileButton = null;
	private Tile[][] tile = null;
	private Timer timer = new Timer(1000, this);
	private int time = 0;

	// �̹��� ����
	private ImageIcon[] numImgList = null;
	private ImageIcon[] smileImgList = null;
	private ImageIcon[] mineImgList = null;
	private ImageIcon[] tileNumImgList = null;
	private ImageIcon defaultImage = null;
	private ImageIcon closeImage = null;
	private ImageIcon pressedImage = null;
	private ImageIcon flagImage = null;
	private ImageIcon minusImage = null;

	// ������ ����Ŭ���� ����
	private TileActionListener tileActionListener = null;
	private TileMouseListener tileMouseListener = null;

	public MineGUI() {
		setLayout(new BorderLayout(0, 10));
		setBackground(background);
		MatteBorder mb1 = new MatteBorder (6, 6, 6, 6, background);
		MatteBorder mb2 = new MatteBorder (4, 3, 0, 0, Color.white);	// �г� �׵θ� ����
		setBorder(new CompoundBorder(mb2, mb1));						// �� ���� ���� ȿ���� ���ÿ� ����
		setImageLoading();
		setProgressPane();
		setGamePane();
		setFocusable(true);
		addKeyListener(new KeyListener());
	}

	/* �޼ҵ� ���� */

	// �̹��� ���� �޼ҵ�
	public void setImageLoading() {
		numImgList = new ImageIcon[10];
		for (int i = 0; i < 10; i++)
			numImgList[i] = new ImageIcon("src/mine_images/num" + i + ".gif");

		smileImgList = new ImageIcon[5];
		for (int i = 0; i < 5; i++)
			smileImgList[i] = new ImageIcon("src/mine_images/smile" + (i + 1) + ".gif");

		mineImgList = new ImageIcon[3];
		for (int i = 0; i < 3; i++)
			mineImgList[i] = new ImageIcon("src/mine_images/mine" + (i + 1) + ".gif");

		tileNumImgList = new ImageIcon[7];
		for (int i = 0; i < 7; i++)
			tileNumImgList[i] = new ImageIcon("src/mine_images/t" + (i + 1) + ".gif");

		defaultImage = new ImageIcon("src/mine_images/default.gif");
		closeImage = new ImageIcon("src/mine_images/close.gif");
		pressedImage = new ImageIcon("src/mine_images/pressed.gif");
		flagImage = new ImageIcon("src/mine_images/flag.gif");
		minusImage = new ImageIcon("src/mine_images/minus.gif");
	}

	// ����� â ���� (���� ���� ����, ������ ��ư, �ð� ��)
	public void setProgressPane() {
		progressPanel = new JPanel();
		progressPanel.setLayout(new GridLayout(1, 3, 70, 0));
		progressPanel.setBackground(background);
		JPanel lPanel = new JPanel(); // ���� �г�
		JPanel cPanel = new JPanel(); // �߾� �г�
		JPanel rPanel = new JPanel(); // ������ �г�
		JPanel minePanel = new JPanel(); // ���� ���� ������ �����ִ� �г�
		JPanel timePanel = new JPanel(); // ����ð��� �����ִ� �г�

		minePanel.setLayout(new GridLayout(1, 3));
		timePanel.setLayout(new GridLayout(1, 3));

		// ���� ���� ���� �ʱ� ���� �� �гο� �߰�
		mineLabel = new JLabel[3];
		mineLabel[0] = new JLabel(numImgList[0]);
		mineLabel[1] = new JLabel(numImgList[4]);
		mineLabel[2] = new JLabel(numImgList[0]);
		minePanel.add(mineLabel[0]);
		minePanel.add(mineLabel[1]);
		minePanel.add(mineLabel[2]);
		minePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, background, Color.white, new Color(123,123,123), background));
		lPanel.add(minePanel);
		lPanel.setBackground(background);
	
		// ������ ��ư �ʱ� ���� �� �гο� �߰�
		smileButton = new JButton(smileImgList[0]);
		smileButton.addActionListener(new smileButtonListener());
		smileButton.setPressedIcon(smileImgList[4]);
		smileButton.setBorder(new EmptyBorder(0, 0, 0, 0));			// ��ư ��� ���� ����
		cPanel.add(smileButton);
		cPanel.setBackground(background);

		// ����ð� �ʱ� ���� �� �гο� �߰�
		timeLabel = new JLabel[3];
		for (int i = 0; i < 3; i++) {
			timeLabel[i] = new JLabel(numImgList[0]);
			timePanel.add(timeLabel[i]);
		}
		timePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, background, Color.white, new Color(123,123,123), background));
		rPanel.add(timePanel);
		rPanel.setBackground(background);

		// ��ü �гο� �߰�
		progressPanel.add(lPanel);
		progressPanel.add(cPanel);
		progressPanel.add(rPanel);
		progressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(progressPanel, BorderLayout.NORTH);
	}

	// ���� â ����
	public void setGamePane() {
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(rows, cols));
		gamePanel.setPreferredSize(new Dimension(cellSize * cols, cellSize * rows)); // �⺻ ũ�� ����
		setButtonTile();
		add(gamePanel, BorderLayout.CENTER);
		game = Game.READY;
	}

	// Ÿ�� ����
	public void setButtonTile() {
		int[][] map = setMine(); // ���� ����

		tile = new Tile[rows][cols];
		tileActionListener = new TileActionListener();
		tileMouseListener = new TileMouseListener();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tile[r][c] = new Tile(map[r][c], r, c, closeImage);
				tile[r][c].setBackground(background);

				tile[r][c].addActionListener(tileActionListener);
				tile[r][c].addMouseListener(tileMouseListener);

				tile[r][c].setPressedIcon(pressedImage); 	// ��ư�� ������ �� ǥ���� ������ ����
				tile[r][c].setDisabledIcon(defaultImage); 	// ��ư�� ��Ȱ��ȭ�� �� ǥ���� ������ ����
				tile[r][c].setRolloverEnabled(false); 		// ���콺�� ���� ���� ��, ��� ���ϰ� x
				gamePanel.add(tile[r][c]);
			}
		}
	}

	// ���� ���� �� ���� ������ �ִ� Ÿ�� ���� ����
	public int[][] setMine() {
		int cnt = 0;
		int[][] map = new int[rows][cols];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				map[r][c] = 0; // ��� Ÿ���� 0���� �ʱ�ȭ

		int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 }; // ���� �ֺ� Ÿ���� ��ǥ �� (���� ����)

		while (true) {
			int x = (int) (Math.random() * rows);
			int y = (int) (Math.random() * cols); // ���ڸ� �������� ��ġ
			if (map[x][y] == -1)
				continue;
			map[x][y] = -1;
			cnt++;
			for (int i = 0; i < 9; i++) {
				try {
						// �ֺ� ���� Ÿ���� �����ϰ� �� ����
					if (map[x + dx[i]][y + dy[i]] != -1)
						map[x + dx[i]][y + dy[i]]++;
				} catch (IndexOutOfBoundsException e) {
				} 		// �ε��� ������ �Ѿ �� ���� ó��
			}

			if (cnt == mine)
				break; 	// ������ ������ŭ ���ڰ� �����Ǹ� ����
		}
		return map;
	}
	
	// �ֺ� ����� ���� (����Լ�)
	public void spaceTileOpen(int r, int c) {
		if (!validRange(r, c)) return;						// ���� �� x
		if (tile[r][c].getState() == State.OPEN) return;	// �̹� ���� �� x
		if (tile[r][c].getFace() == -1) return;				// ���� �κ� x
		if (tile[r][c].getState() == State.CLOSE) {		
			tile[r][c].setState(State.OPEN);			
			tile[r][c].setEnabled(false);					// Ÿ���� �������� �����ְ� ��ư ��Ȱ��ȭ
		} else return;
		if (tile[r][c].getFace() != 0 && tile[r][c].getFace() != -1) {	
			tile[r][c].setDisabledIcon(tileNumImgList[tile[r][c].getFace() - 1]);
			return;											// ���� �ִ� ���̸� �̹��� �����ϰ� ����
		}
		
		// �ֺ��� �ִ� Ÿ�� ȣ��
		spaceTileOpen(r - 1, c - 1);
		spaceTileOpen(r - 1, c);
		spaceTileOpen(r - 1, c + 1);
		spaceTileOpen(r, c - 1);
		spaceTileOpen(r, c + 1);
		spaceTileOpen(r + 1, c - 1);
		spaceTileOpen(r + 1, c);
		spaceTileOpen(r + 1, c + 1);
	}
	
	// �Ѿ�� ��ǥ���� ������ ������� Ȯ�� (true, false)
	public boolean validRange(int r, int c) {
		return ((r >= 0 && r < rows) && (c >= 0 && c < cols));
	}
	
	// ���� ��� ���� ǥ��
	public void showMineCount() {
		int one = checkMine % 10;
		int ten = checkMine / 10;
		if (checkMine < 0) {						// ��� ǥ�ø� ���� �������� ���� ���� ��, ���̳ʽ� ǥ��
			// ���밪 ���ϱ�
			one = Math.abs(checkMine % 10);
			ten = Math.abs(checkMine / 10);
			mineLabel[0].setIcon(minusImage);
		} else if(checkMine == 0) {					// ���̳ʽ����� �ٽ� ���ƿ��� ���� ���� �̹��� ����
			mineLabel[0].setIcon(numImgList[0]);
		}
		mineLabel[1].setIcon(numImgList[ten]);
		mineLabel[2].setIcon(numImgList[one]);
	}
	
	// ���콺�� ���� Ŭ������ ��, �ֺ� Ÿ�ϵ��� ���� ȿ���� ������
	public void aroundTilePressed(int r, int c) {
		int [] dx = {-1,-1,-1,0,0,1,1,1};
		int [] dy = {-1,0,1,-1,1,-1,0,1};
		
		for (int i = 0; i < 8; i++) { 
			try {
				if ( tile[r+dx[i]][c+dy[i]].getState() != State.FLAG && tile[r+dx[i]][c+dy[i]].getState() != State.OPEN )
					tile[r+dx[i]][c+dy[i]].setIcon(pressedImage);
			} catch(IndexOutOfBoundsException e){}
		}
	}
	
	// ���콺 ���� Ŭ���� ���� ��, ����ŭ �ֺ� Ÿ�Ͽ� ����� ǥ���ߴٸ� �ֺ� Ÿ���� �������� ȿ���� ������
	public void aroundTileReleased(int r, int c) {
		int flagCnt = 0;
		int [] dx = {-1,-1,-1,0,0,1,1,1};
		int [] dy = {-1,0,1,-1,1,-1,0,1};
		
		for (int i = 0; i < 8; i++) {
			try {
				if ( tile[r+dx[i]][c+dy[i]].getState() == State.FLAG ) flagCnt++;
			} catch(IndexOutOfBoundsException e){}
		}
		if ( tile[r][c].getFace() == flagCnt ) {
			for (int i = 0; i < 8; i++) {
				try {
					if ( tile[r+dx[i]][c+dy[i]].getState() == State.CLOSE) {
						Tile t = tile[r+dx[i]][c+dy[i]];
						if ( t.getFace() == -1 ) {
							timer.stop();
							GameOver();
							t.setDisabledIcon( mineImgList[1] );
							return;
						}
						if ( t.getFace() != 0 ) {
							t.setState( State.OPEN );
							t.setDisabledIcon( tileNumImgList[t.getFace() - 1] );
							t.setEnabled( false );
						} else {
							spaceTileOpen( t.getRow(), t.getCol() );
						}
					}
				} catch(IndexOutOfBoundsException e){}
			}
		} else {
			for (int i = 0; i < 8; i++) { 
				try {
					if ( tile[r+dx[i]][c+dy[i]].getState() != State.FLAG && tile[r+dx[i]][c+dy[i]].getState() != State.OPEN )
						tile[r+dx[i]][c+dy[i]].setIcon(closeImage);
				} catch(IndexOutOfBoundsException e){}
			}
		}
	}
	
	// ���ڸ� ��� ã�Ҵ��� Ȯ��
	public boolean isAllCheckFlagMine() {
		if (game != Game.READY) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (tile[r][c].getFace() != -1 && tile[r][c].getState() == State.FLAG) 
						return false;		// ���ڰ� �ƴ� ���� ��߷� ǥ������ �� false ����
					if (tile[r][c].getFace() != -1 && tile[r][c].getState() != State.OPEN) 
						return false;		// ���ڰ� �ƴ� ���� ������ �ʾ��� �� false ���� 
				}
			}
			return (flagCount == mine);		// �߰��� ���� ���� ���� ���ڼ��� ������ �Ǵ�
		} else
			return false;
	}
	
	// ���� ���� �޼ҵ� (���� ��)
	public void GameOver() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tile[r][c].removeActionListener(tileActionListener);
				tile[r][c].removeMouseListener(tileMouseListener);		// ���̻� ��ư�� �۵����� �ʵ��� ����
				if (tile[r][c].getFace() == -1 && tile[r][c].getState() != State.FLAG) {
					tile[r][c].setDisabledIcon(mineImgList[0]);			// ��� ǥ�ð� �� �� ���ڴ� ǥ��
					tile[r][c].setEnabled(false);						// ���ڴ� ��Ȱ��ȭ -> ��Ÿ���� ��
				} else if (tile[r][c].getFace() != -1 && tile[r][c].getState() == State.FLAG) {
					tile[r][c].setDisabledIcon(mineImgList[2]);			// ���ڰ� �ƴѵ� ��� ǥ�ø� �� Ÿ���� ���� ǥ��
					tile[r][c].setEnabled(false);
				}
			}
		}
		game = Game.END;
	}
	
	// ���� ���� �޼ҵ� (�̰��� ��)
	public void GameWin() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tile[r][c].removeActionListener(tileActionListener);
				tile[r][c].removeMouseListener(tileMouseListener);
			}
		}
		timer.stop();
		smileButton.setIcon( smileImgList[3]);
		game = Game.END;
	}
	
	// ���� ����� �޼ҵ�
	public void reStartGame() {
		remove(progressPanel);
		remove(gamePanel);
		setVisible(false);
		setProgressPane();
		setGamePane();
		setVisible(true);
	}
	
	// Ÿ�̸� actionPerformed �޼ҵ� ����
	public void actionPerformed(ActionEvent e) {
		// time�� �ڸ������� ���� ��, �� ���̺� �̹��� ����
		if (time == 1000) {
			timer.stop();
			return;
		}
		time = time + 1;
		int one = time % 10;
		int ten = time / 10;
		int hund = 0;
		if (ten / 10 != 0) {
			hund = ten / 10;
			ten = ten % 10;
		}
		timeLabel[0].setIcon(numImgList[hund]);
		timeLabel[1].setIcon(numImgList[ten]);
		timeLabel[2].setIcon(numImgList[one]);
	}
	
	/* ����Ŭ���� ���� */

	// ������ �������� ������ ��, ������ ����۵Ǵ� Ŭ����
	class smileButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			time = 0;
			checkMine = mine;
			flagCount = 0;
			reStartGame();
			requestFocus();
		}
	}

	// ��ư�� ������ ��(���� Ŭ����), ���ǿ� ���� ����Ǵ� Ŭ����
	class TileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (game == Game.READY) {
				timer.start(); 					// ó������ Ŭ������ �� Ÿ�̸� ����
				game = Game.START;
			}
			Tile t = (Tile) e.getSource();
			if (t.getState() == State.CLOSE) { 	// Ÿ���� ���� ������ ���� ���� (��� ǥ�ð� �ִ� Ÿ���� x)
				if (t.getFace() == -1) { 		// ���ڸ� ������ ��
					timer.stop();
					GameOver();
					t.setDisabledIcon(mineImgList[1]);
					return; 					// �޹����� �������� �ʱ� ���� ����
				} else if (t.getFace() != 0) {	// ���� �ִ� ���� ������ ��
					t.setState(State.OPEN);
					t.setDisabledIcon(tileNumImgList[t.getFace() - 1]);
					t.setEnabled(false);		// ��ư ��Ȱ��ȭ
				} else {						
					spaceTileOpen(t.getRow(), t.getCol());
				}								// ������̸� �ڽ��� ������ �ֺ� ����� ����
			}
			if (isAllCheckFlagMine()) { 		// ���ڸ� ��� �� ã�Ҵ��� �˻�
				GameWin();
			}
			requestFocus();
		}
	}

	// ���콺 ���� Ŭ��, ������ Ŭ���� ���� �ٸ� ������ �����ִ� Ŭ����
	// ���콺����ʹ� ���콺�����ʿ��� �ʿ��� �������̽��� �����Ͽ� �� �� �ֵ��� ���� Ŭ����
	class TileMouseListener extends MouseAdapter {	
		private boolean bothLeft = false;
		private boolean bothRight = false;
		private boolean both = false;						// ���콺�� ���� Ŭ���ߴ��� Ȯ���ϴ� ����
		
		public void mousePressed(MouseEvent e) {			// ���콺�� ������ ��
			Tile t = (Tile)e.getSource();
			if (SwingUtilities.isLeftMouseButton(e)) {		// ��ư�� ���콺 ���� Ŭ������ ��
				smileButton.setIcon(smileImgList[1]);
				if (t.getState() == State.OPEN) 
					bothLeft = true;
			}
			if (SwingUtilities.isRightMouseButton(e)) {		// ��ư�� ���콺 ������ Ŭ������ ��
				// ��ư�� �������� ���� ���� ����
				if (t.getState() != State.OPEN) {	
					if (t.getState() == State.CLOSE) {		// �����ִٸ� ��� ǥ��
						t.setIcon(flagImage);
						t.setState(State.FLAG);
						checkMine -= 1;						// ���� ��� ���� ����
						if (t.getFace() == -1)
							flagCount++;					// �� Ÿ���� ���ڰ� �´ٸ� �߰��� ���� ���� ����
						showMineCount();					// ���� ��� ���� ǥ��
					} else if (t.getState() == State.FLAG) {// ����� ǥ�õ� ���¶�� ��� ����
						t.setIcon(closeImage);
						t.setState(State.CLOSE);
						checkMine += 1;						// ���� ��� ���� ����
						if (t.getFace() == -1)
							flagCount--;					// �� Ÿ���� ���ڰ� �´ٸ� �߰��� ���� ���� ����
						showMineCount();					// ���� ��� ���� ǥ��
					}
				}
				if (isAllCheckFlagMine()) 					// ���ڸ� ��� ã�Ҵ��� �˻�
					GameWin();
				
				if (t.getState() == State.OPEN) 			// both mouse check
					bothRight = true;
			}
			if (bothLeft == true && bothRight == true) {
				aroundTilePressed(t.getRow(), t.getCol());
				both = true;
			}
		}

		public void mouseReleased(MouseEvent e) {			// ���콺�� ���� ��
			Tile t = (Tile) e.getSource();
			if (SwingUtilities.isLeftMouseButton(e)) {		// ���콺 ������ ���� ��
				smileButton.setIcon(smileImgList[0]);
				if (t.getState() == State.OPEN)
					bothLeft = false;
			}
			if (SwingUtilities.isRightMouseButton(e)) {		// ���콺 �������� ���� ��
				if (t.getState() == State.OPEN)
					bothRight = false;
				
			}

			if (bothLeft == false && bothRight == false) {
				if(both) {
					aroundTileReleased(t.getRow(), t.getCol());
					both = false;
					if(isAllCheckFlagMine())				// ���� Ŭ������ ������ ���´��� Ȯ��
						GameWin();
				}
			}

			if (game == Game.END) {
				if(isAllCheckFlagMine()) 
					smileButton.setIcon(smileImgList[3]);
				else 
					smileButton.setIcon(smileImgList[2]);
			}
			requestFocus();
		}
	}
	
	// Ű �Է½� �̺�Ʈ �߻�
	// Ű�����ʿ� ��������� -> �����ʴ� �޼ҵ带 �� �޾�����ϴ¹ݸ� ����ʹ� ���ϴ� �޼ҵ常 ȣ�Ⱑ��
	class KeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				new MultiGame();
				setVisible(false);
				break;
			}
		}
	}
}