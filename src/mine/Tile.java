package mine;
import javax.swing.*;

public class Tile extends JButton {
	private State state		= null;
	private int face		= 0;	// �� (����: -1)
	private int row			= 0;	// �� ��ġ
	private int col			= 0;	// �� ��ġ
	
	public Tile(int face, int row, int col, Icon icon) {
		super(icon);
		this.state = State.CLOSE;
		this.face = face;
		this.row = row;
		this.col = col;
	}
	public void setFace(int face) { this.face = face; }
	public int getFace() { return this.face; }
	public int getRow() { return this.row; }
	public int getCol() { return this.col; }
	public void setState(State STATE) { this.state = STATE; }
	public State getState() { return this.state; }
}