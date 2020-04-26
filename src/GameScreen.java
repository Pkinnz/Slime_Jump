
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameScreen extends JPanel implements Runnable ,KeyListener{
	public static final float GRAVITY = 0.1f;
	private Thread thread;
	SoundPlay sound;
	private int i = 0;
	public boolean gameProcess = false, lavaLeft = true;
	private float x = 0;
	private float y = 0;
	private float speedY = 0;
	private int gameLive = 0 , tim = 10 , xLava = 0;
	private int score , highScore = 0;
	private int[] nPlat = new int[10] ;
	private boolean isPlat = false;
	private int randomN , countJ = 0, checkpoint = 0;
	private int xPlat = 2, randomP = 0;


	Font bigFont = new Font("Serif", Font.BOLD ,20);
	Font bigFont1 = new Font("Serif", Font.BOLD ,30);

	public GameScreen(){
		thread = new Thread(this);
	}

	public void startGame() {
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			if(gameProcess) {
				if(y>=600.00){
					gameProcess = false;
		        	gameLive = 2;
		        	sound = new SoundPlay("/data/DeadSound2.wav");
		            sound.play();
		        	if(score > highScore) {
		        		highScore = score;
		        	}
				}else if(y >= 500.00 && y <= 520) {
					if(countJ == 0 && x >= 0 && x < 40) {
						speedY = -8;
						y += speedY;
						countJ++;
						sound = new SoundPlay("/data/JumpSound.wav");
			      sound.play();
						randomPlat();

					}else {
						for(int a=0;a<5-checkpoint;a++) {
							if(nPlat[a]-45 < x && x < (nPlat[a]+50)) {
								speedY = -8;
								y += speedY;
								isPlat = true;
								if(score % 10 == 0 && score > 45){
									xPlat++;
									xPlat++;
								}
								if(score > 40) {
									checkpoint = 4;
								}else if(score > 30) {
									checkpoint = 3;
								}else if(score > 20) {
									checkpoint = 2;
								}else if(score > 10) {
									checkpoint = 1;
								}
								sound = new SoundPlay("/data/JumpSound.wav");
					            sound.play();
								randomPlat();
								break;
							}
						}
						if(!isPlat) {
							speedY += GRAVITY;
							y += speedY;
						}

					}

				}else {
					speedY += GRAVITY;
					y += speedY;
				}
				if(x >= 386) {
					x = 386;
				}else if(x < 0) {
					x = 0;
				}
				isPlat = false;
			}
			try {
				if(gameProcess) {
					i++;
				}
				Thread.sleep(tim);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}



	public void paint(Graphics g) {
		super.paint(g);
		BufferedImage bg,start,lava,cha,dead,thophy,sDead;
		try {
			dead = ImageIO.read(new File("data/Dead.png"));
			bg = ImageIO.read(new File("data/bg-grid.png"));
			start = ImageIO.read(new File("data/Start.png"));
			lava = ImageIO.read(new File("data/Lava.png"));
			cha = ImageIO.read(new File("data/Slimeee.png"));
			thophy = ImageIO.read(new File("data/thophy2.png"));
			sDead = ImageIO.read(new File("data/SlimeeeDead.png"));
			if(!gameProcess && gameLive == 0) {
				g.drawImage(start, 0, 0, null);
			}
			else if (gameLive == 2 && !gameProcess){
				g.drawImage(dead, 0, 0, null);
				g.setFont(bigFont1);
				g.setColor(Color.ORANGE);
				g.drawString("Your Score "+score+" Point.", 80 , 100);
				g.drawImage(sDead,(int) x,(int) y, null);
			}
			else if(gameLive == 1 && gameProcess){
				score = i/70;
				g.drawImage(bg, 0, 0, null);
				g.setFont(bigFont);
				g.drawString("Score : "+score, 300, 50);
				g.drawString("High Score : "+highScore, 50, 50);
				g.drawImage(thophy, 20, 30, null);
				drawPlat(g);
			}
			if(gameLive == 2){
				g.drawImage(lava, xLava, 650, null);
			}else if(lavaLeft){
				xLava -= 1;
				g.drawImage(lava, xLava, 650, null);
				if(xLava < -500){
					lavaLeft = false;
				}
			}else{
				xLava += 1;
				g.drawImage(lava, xLava, 650, null);
				if(xLava > -20){
					lavaLeft = true;
				}
			}

			if(gameLive != 2) {
				g.drawImage(cha,(int) x,(int) y, null);
			}
		} catch (IOException e) {

		}
		repaint();
	}

	public void checkOverlap(int n,int k) {
		for(int f=0;f<k;f++) {
			if((nPlat[f]-50) < n && n < (nPlat[f]+50)) {
				randomN = (int)(Math.random()*400);
				checkOverlap(randomN, k);
			}
		}
	}
	public void randomPlat(){
		for(int k=0;k<5-checkpoint;k++) {
			randomN = (int)(Math.random()*400);
			checkOverlap(randomN, k);
			nPlat[k] = randomN;
		}
	}
	public void drawPlat(Graphics g) {
		BufferedImage plat;
		try {
			plat = ImageIO.read(new File("data/p-green.png"));
			if(score >= 50){
				if(score % 3 == 0){
					randomP = (int)(Math.random()*10);
				}
				if(randomP < 5 && nPlat[0] > 0 && nPlat[0] < 400){
					nPlat[0] -= xPlat;
				}else if(randomP >= 5 && nPlat[0] > 0 && nPlat[0] < 400){
					nPlat[0] += xPlat;
				}else if(nPlat[0] <= 0){
					randomP = (int)(Math.random()*10);
					nPlat[0] += xPlat;
				}else{
					randomP = (int)(Math.random()*10);
					nPlat[0] -= xPlat;
				}
			}
			for(int j=0;j<5-checkpoint;j++) {
				g.drawImage(plat, nPlat[j], 550,null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
        	if(gameProcess) {
        		x -= 10;
        	}
        }
        else if (code == KeyEvent.VK_RIGHT) {
        	if(gameProcess) {
        		x += 10;
        	}
        }
        else if (code == KeyEvent.VK_SPACE) {
        	if(gameLive == 0 ) {
        		gameProcess = true;
                gameLive = 1;
        	}

        }
        else if(code == KeyEvent.VK_R) {
        	if(!gameProcess) {
        		x = 0;
            	y = 0;
            	speedY = 0;
            	i = 0;
            	for(int g=0;g<5;g++) {
            		nPlat[g] = 0;
            	}
            	checkpoint = 0;
            	gameProcess = true;
                gameLive = 1;
        	}

        }
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}


}
