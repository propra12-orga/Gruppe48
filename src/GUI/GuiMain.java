package GUI;

import javax.swing.JFrame;


public final class GuiMain extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int OFFSET = 20;                       //OFFSET ist der Abstand zwischen der Map ind dem Rand des Fensters

	 public GuiMain() {
	        InitUI();
	    }
/*  Neues Board laden
 * 
 */
    public void InitUI() {
        Board board = new Board();
        add(board);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(board.getBoardWidth() + OFFSET,
                board.getBoardHeight() + 2*OFFSET);
        setLocationRelativeTo(null);
        setTitle("Bomberman Gruppe48");
    }


    public static void main(String[] args) {
        GuiMain guimain = new GuiMain();
        guimain.setVisible(true);
    }
}