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
	private int rows = 16; // 행 개수
	private int cols = 16; // 열 개수
	private int mine = 40; // 지뢰 개수
	private int checkMine = 40; // 깃발 개수
	private int cellSize = 20; // 깃발 개수
	private int flagCount = 0; // 발견한 지뢰 개수

	private JPanel progressPanel = null;
	private JPanel gamePanel = null;
	private JLabel[] mineLabel = null;
	private JLabel[] timeLabel = null;
	private JButton smileButton = null;
	private Tile[][] tile = null;
	private Timer timer = new Timer(1000, this);
	private int time = 0;

	// 이미지 변수
	private ImageIcon[] numImgList = null;
	private ImageIcon[] smileImgList = null;
	private ImageIcon[] mineImgList = null;
	private ImageIcon[] tileNumImgList = null;
	private ImageIcon defaultImage = null;
	private ImageIcon closeImage = null;
	private ImageIcon pressedImage = null;
	private ImageIcon flagImage = null;
	private ImageIcon minusImage = null;

	// 리스너 내부클래스 변수
	private TileActionListener tileActionListener = null;
	private TileMouseListener tileMouseListener = null;

	public MineGUI() {
		setLayout(new BorderLayout(0, 10));
		setBackground(background);
		MatteBorder mb1 = new MatteBorder (6, 6, 6, 6, background);
		MatteBorder mb2 = new MatteBorder (4, 3, 0, 0, Color.white);	// 패널 테두리 생성
		setBorder(new CompoundBorder(mb2, mb1));						// 두 개의 보더 효과를 동시에 설정
		setImageLoading();
		setProgressPane();
		setGamePane();
		setFocusable(true);
		addKeyListener(new KeyListener());
	}

	/* 메소드 구현 */

	// 이미지 생성 메소드
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

	// 진행률 창 설정 (남은 지뢰 개수, 스마일 버튼, 시간 등)
	public void setProgressPane() {
		progressPanel = new JPanel();
		progressPanel.setLayout(new GridLayout(1, 3, 70, 0));
		progressPanel.setBackground(background);
		JPanel lPanel = new JPanel(); // 왼쪽 패널
		JPanel cPanel = new JPanel(); // 중앙 패널
		JPanel rPanel = new JPanel(); // 오른쪽 패널
		JPanel minePanel = new JPanel(); // 남은 지뢰 개수를 보여주는 패널
		JPanel timePanel = new JPanel(); // 진행시간을 보여주는 패널

		minePanel.setLayout(new GridLayout(1, 3));
		timePanel.setLayout(new GridLayout(1, 3));

		// 남은 지뢰 개수 초기 설정 및 패널에 추가
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
	
		// 스마일 버튼 초기 설정 및 패널에 추가
		smileButton = new JButton(smileImgList[0]);
		smileButton.addActionListener(new smileButtonListener());
		smileButton.setPressedIcon(smileImgList[4]);
		smileButton.setBorder(new EmptyBorder(0, 0, 0, 0));			// 버튼 경계 없게 설정
		cPanel.add(smileButton);
		cPanel.setBackground(background);

		// 진행시간 초기 설정 및 패널에 추가
		timeLabel = new JLabel[3];
		for (int i = 0; i < 3; i++) {
			timeLabel[i] = new JLabel(numImgList[0]);
			timePanel.add(timeLabel[i]);
		}
		timePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, background, Color.white, new Color(123,123,123), background));
		rPanel.add(timePanel);
		rPanel.setBackground(background);

		// 전체 패널에 추가
		progressPanel.add(lPanel);
		progressPanel.add(cPanel);
		progressPanel.add(rPanel);
		progressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(progressPanel, BorderLayout.NORTH);
	}

	// 게임 창 설정
	public void setGamePane() {
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(rows, cols));
		gamePanel.setPreferredSize(new Dimension(cellSize * cols, cellSize * rows)); // 기본 크기 설정
		setButtonTile();
		add(gamePanel, BorderLayout.CENTER);
		game = Game.READY;
	}

	// 타일 생성
	public void setButtonTile() {
		int[][] map = setMine(); // 지뢰 생성

		tile = new Tile[rows][cols];
		tileActionListener = new TileActionListener();
		tileMouseListener = new TileMouseListener();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tile[r][c] = new Tile(map[r][c], r, c, closeImage);
				tile[r][c].setBackground(background);

				tile[r][c].addActionListener(tileActionListener);
				tile[r][c].addMouseListener(tileMouseListener);

				tile[r][c].setPressedIcon(pressedImage); 	// 버튼을 눌렀을 때 표시할 아이콘 설정
				tile[r][c].setDisabledIcon(defaultImage); 	// 버튼이 비활성화될 시 표시할 아이콘 설정
				tile[r][c].setRolloverEnabled(false); 		// 마우스를 갖다 댔을 때, 경계 진하게 x
				gamePanel.add(tile[r][c]);
			}
		}
	}

	// 지뢰 생성 및 지뢰 주위에 있는 타일 숫자 설정
	public int[][] setMine() {
		int cnt = 0;
		int[][] map = new int[rows][cols];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				map[r][c] = 0; // 모든 타일을 0으로 초기화

		int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 }; // 지뢰 주변 타일의 좌표 값 (지뢰 기준)

		while (true) {
			int x = (int) (Math.random() * rows);
			int y = (int) (Math.random() * cols); // 지뢰를 랜덤으로 배치
			if (map[x][y] == -1)
				continue;
			map[x][y] = -1;
			cnt++;
			for (int i = 0; i < 9; i++) {
				try {
						// 주변 지뢰 타일을 제외하고 값 증가
					if (map[x + dx[i]][y + dy[i]] != -1)
						map[x + dx[i]][y + dy[i]]++;
				} catch (IndexOutOfBoundsException e) {
				} 		// 인덱스 범위가 넘어갈 시 예외 처리
			}

			if (cnt == mine)
				break; 	// 설정한 개수만큼 지뢰가 생성되면 정지
		}
		return map;
	}
	
	// 주변 빈공간 오픈 (재귀함수)
	public void spaceTileOpen(int r, int c) {
		if (!validRange(r, c)) return;						// 범위 밖 x
		if (tile[r][c].getState() == State.OPEN) return;	// 이미 열린 곳 x
		if (tile[r][c].getFace() == -1) return;				// 지뢰 부분 x
		if (tile[r][c].getState() == State.CLOSE) {		
			tile[r][c].setState(State.OPEN);			
			tile[r][c].setEnabled(false);					// 타일이 닫혔으면 열어주고 버튼 비활성화
		} else return;
		if (tile[r][c].getFace() != 0 && tile[r][c].getFace() != -1) {	
			tile[r][c].setDisabledIcon(tileNumImgList[tile[r][c].getFace() - 1]);
			return;											// 값이 있는 곳이면 이미지 설정하고 리턴
		}
		
		// 주변에 있는 타일 호출
		spaceTileOpen(r - 1, c - 1);
		spaceTileOpen(r - 1, c);
		spaceTileOpen(r - 1, c + 1);
		spaceTileOpen(r, c - 1);
		spaceTileOpen(r, c + 1);
		spaceTileOpen(r + 1, c - 1);
		spaceTileOpen(r + 1, c);
		spaceTileOpen(r + 1, c + 1);
	}
	
	// 넘어온 좌표값의 범위가 벗어났는지 확인 (true, false)
	public boolean validRange(int r, int c) {
		return ((r >= 0 && r < rows) && (c >= 0 && c < cols));
	}
	
	// 남은 깃발 개수 표시
	public void showMineCount() {
		int one = checkMine % 10;
		int ten = checkMine / 10;
		if (checkMine < 0) {						// 깃발 표시를 지뢰 개수보다 많이 했을 시, 마이너스 표시
			// 절대값 구하기
			one = Math.abs(checkMine % 10);
			ten = Math.abs(checkMine / 10);
			mineLabel[0].setIcon(minusImage);
		} else if(checkMine == 0) {					// 마이너스에서 다시 돌아왔을 때를 위해 이미지 설정
			mineLabel[0].setIcon(numImgList[0]);
		}
		mineLabel[1].setIcon(numImgList[ten]);
		mineLabel[2].setIcon(numImgList[one]);
	}
	
	// 마우스를 양쪽 클릭했을 때, 주변 타일들이 눌린 효과를 보여줌
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
	
	// 마우스 양쪽 클릭을 뗐을 때, 값만큼 주변 타일에 깃발을 표시했다면 주변 타일이 눌려지는 효과를 보여줌
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
	
	// 지뢰를 모두 찾았는지 확인
	public boolean isAllCheckFlagMine() {
		if (game != Game.READY) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (tile[r][c].getFace() != -1 && tile[r][c].getState() == State.FLAG) 
						return false;		// 지뢰가 아닌 곳을 깃발로 표시했을 때 false 리턴
					if (tile[r][c].getFace() != -1 && tile[r][c].getState() != State.OPEN) 
						return false;		// 지뢰가 아닌 곳이 열리지 않았을 때 false 리턴 
				}
			}
			return (flagCount == mine);		// 발견한 지뢰 수가 실제 지뢰수랑 같은지 판단
		} else
			return false;
	}
	
	// 게임 종료 메소드 (졌을 때)
	public void GameOver() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tile[r][c].removeActionListener(tileActionListener);
				tile[r][c].removeMouseListener(tileMouseListener);		// 더이상 버튼이 작동하지 않도록 설정
				if (tile[r][c].getFace() == -1 && tile[r][c].getState() != State.FLAG) {
					tile[r][c].setDisabledIcon(mineImgList[0]);			// 깃발 표시가 안 된 지뢰는 표시
					tile[r][c].setEnabled(false);						// 지뢰는 비활성화 -> 나타나게 함
				} else if (tile[r][c].getFace() != -1 && tile[r][c].getState() == State.FLAG) {
					tile[r][c].setDisabledIcon(mineImgList[2]);			// 지뢰가 아닌데 깃발 표시를 한 타일은 따로 표시
					tile[r][c].setEnabled(false);
				}
			}
		}
		game = Game.END;
	}
	
	// 게임 종료 메소드 (이겼을 때)
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
	
	// 게임 재시작 메소드
	public void reStartGame() {
		remove(progressPanel);
		remove(gamePanel);
		setVisible(false);
		setProgressPane();
		setGamePane();
		setVisible(true);
	}
	
	// 타이머 actionPerformed 메소드 구현
	public void actionPerformed(ActionEvent e) {
		// time을 자릿수별로 구한 뒤, 각 레이블에 이미지 설정
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
	
	/* 내부클래스 구현 */

	// 스마일 아이콘을 눌렀을 때, 게임이 재시작되는 클래스
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

	// 버튼을 눌렀을 때(왼쪽 클릭만), 조건에 따라 실행되는 클래스
	class TileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (game == Game.READY) {
				timer.start(); 					// 처음으로 클릭했을 때 타이머 시작
				game = Game.START;
			}
			Tile t = (Tile) e.getSource();
			if (t.getState() == State.CLOSE) { 	// 타일이 닫힌 상태일 때만 실행 (깃발 표시가 있는 타일은 x)
				if (t.getFace() == -1) { 		// 지뢰를 눌렀을 때
					timer.stop();
					GameOver();
					t.setDisabledIcon(mineImgList[1]);
					return; 					// 뒷문장을 실행하지 않기 위해 리턴
				} else if (t.getFace() != 0) {	// 값이 있는 곳을 눌렀을 때
					t.setState(State.OPEN);
					t.setDisabledIcon(tileNumImgList[t.getFace() - 1]);
					t.setEnabled(false);		// 버튼 비활성화
				} else {						
					spaceTileOpen(t.getRow(), t.getCol());
				}								// 빈공간이면 자신을 포함한 주변 빈공간 오픈
			}
			if (isAllCheckFlagMine()) { 		// 지뢰를 모두 다 찾았는지 검사
				GameWin();
			}
			requestFocus();
		}
	}

	// 마우스 왼쪽 클릭, 오른쪽 클릭에 따라 다른 실행을 보여주는 클래스
	// 마우스어댑터는 마우스리스너에서 필요한 인터페이스만 구현하여 쓸 수 있도록 만든 클래스
	class TileMouseListener extends MouseAdapter {	
		private boolean bothLeft = false;
		private boolean bothRight = false;
		private boolean both = false;						// 마우스를 양쪽 클릭했는지 확인하는 변수
		
		public void mousePressed(MouseEvent e) {			// 마우스를 눌렀을 때
			Tile t = (Tile)e.getSource();
			if (SwingUtilities.isLeftMouseButton(e)) {		// 버튼을 마우스 왼쪽 클릭했을 때
				smileButton.setIcon(smileImgList[1]);
				if (t.getState() == State.OPEN) 
					bothLeft = true;
			}
			if (SwingUtilities.isRightMouseButton(e)) {		// 버튼을 마우스 오른쪽 클릭했을 때
				// 버튼이 열려있지 않을 때만 실행
				if (t.getState() != State.OPEN) {	
					if (t.getState() == State.CLOSE) {		// 닫혀있다면 깃발 표시
						t.setIcon(flagImage);
						t.setState(State.FLAG);
						checkMine -= 1;						// 남은 깃발 개수 감소
						if (t.getFace() == -1)
							flagCount++;					// 이 타일이 지뢰가 맞다면 발견한 지뢰 개수 증가
						showMineCount();					// 남은 깃발 개수 표시
					} else if (t.getState() == State.FLAG) {// 깃발이 표시된 상태라면 깃발 해제
						t.setIcon(closeImage);
						t.setState(State.CLOSE);
						checkMine += 1;						// 남은 깃발 개수 증가
						if (t.getFace() == -1)
							flagCount--;					// 이 타일이 지뢰가 맞다면 발견한 지뢰 개수 감소
						showMineCount();					// 남은 깃발 개수 표시
					}
				}
				if (isAllCheckFlagMine()) 					// 지뢰를 모두 찾았는지 검사
					GameWin();
				
				if (t.getState() == State.OPEN) 			// both mouse check
					bothRight = true;
			}
			if (bothLeft == true && bothRight == true) {
				aroundTilePressed(t.getRow(), t.getCol());
				both = true;
			}
		}

		public void mouseReleased(MouseEvent e) {			// 마우스를 뗐을 때
			Tile t = (Tile) e.getSource();
			if (SwingUtilities.isLeftMouseButton(e)) {		// 마우스 왼쪽을 뗐을 때
				smileButton.setIcon(smileImgList[0]);
				if (t.getState() == State.OPEN)
					bothLeft = false;
			}
			if (SwingUtilities.isRightMouseButton(e)) {		// 마우스 오른쪽을 뗐을 때
				if (t.getState() == State.OPEN)
					bothRight = false;
				
			}

			if (bothLeft == false && bothRight == false) {
				if(both) {
					aroundTileReleased(t.getRow(), t.getCol());
					both = false;
					if(isAllCheckFlagMine())				// 양쪽 클릭으로 게임을 끝냈는지 확인
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
	
	// 키 입력시 이벤트 발생
	// 키리스너와 어댑터차이 -> 리스너는 메소드를 다 달아줘야하는반면 어댑터는 원하는 메소드만 호출가능
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