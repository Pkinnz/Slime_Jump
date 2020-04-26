import javax.swing.JFrame;

public class GameWindow extends JFrame {
	private GameScreen game;
	
	public GameWindow() {
		super("Slime Jump");
		setSize(450, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(50, 70);
		setResizable(false);
		game = new GameScreen();
		add(game);
		addKeyListener(game);
		
	}
	
	public void startGame() {
		game.startGame();
	}
		

	public static void main(String[] args) {
		GameWindow gw = new GameWindow();
		gw.setVisible(true);
		gw.startGame();
	}

}
