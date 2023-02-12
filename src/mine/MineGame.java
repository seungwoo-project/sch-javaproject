package mine;
import javax.swing.JFrame;


public class MineGame extends JFrame{
	private MineGUI mineGUI;

	public MineGame() {
		setUndecorated(true);
		mineGUI = new MineGUI();
		setContentPane(mineGUI);	// �������� �����̳ʸ� mineGUI �гη� ����
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
