package mine;
import javax.swing.JFrame;


public class MineGame extends JFrame{
	private MineGUI mineGUI;

	public MineGame() {
		setUndecorated(true);
		mineGUI = new MineGUI();
		setContentPane(mineGUI);	// 프레임의 컨테이너를 mineGUI 패널로 설정
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
