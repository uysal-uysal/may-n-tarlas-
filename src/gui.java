import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class gui extends JFrame {

	Random random = new Random();
	Date startedDate = new Date();
	Timer timer = new Timer();

	
	//how many mines at the row and column
	public int width = 24;
	public int height = 12;
	
	//spacing between of boxes
	int buttonGridSpacing = 7;
	public int difficult = 25;
	public int continueMines = 30;

	
	public int mouseX = -100;
	public int mouseY = -100;
	
	//coordinates of smile drawing and center of smile button
	public int smileX = 605;
	public int smileY = 5;
	public int resX = 640;
	public int resY = 40;
	
	//coordinates flag drawing and clickable area of flag button
	public int flagX = 445;
	public int flagY = 5;
	public int flagButtonX = flagX + 35;
	public int flagButtonY = flagY + 35;
	
	//this means flag button active or deactive
	public boolean flag = false;

	//counter panels coordinates
	public int counterX = 1100 ;
	public int counterY = 5;	
	public int sec = 0;

	//if game still running smile is happy but if you lose smile is not happy
	public boolean isHappy = true;

	//checking game status of win lose and restarted
	public boolean win = false;
	public boolean lose = false;
	public boolean isrestarted = false;
	public boolean iscontinue = false;
	

	int neighs = 0;
	public int point = 0;
	public int currentMines = 0;
	public int releasedClean = 0;
	public int currentFlags = 95;
	
	//arrays of all boxes, mines, flagged mines and released mines
	int[][] mines = new int[width][height];
	int[][] neighbours = new int[width][height];
	boolean[][] released = new boolean[width][height];
	boolean[][] flagged = new boolean[width][height];
	
	public gui() {
		
		draw();
		findMines();

		
		this.setTitle("Mayın Tarlası");
		this.setSize(1920, 1080);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		Board board = new Board();
		this.setContentPane(board);
	
		mouseMovement move = new mouseMovement();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);

	}

	
	public class Board extends JPanel {
		
		public void paintComponent(Graphics graph) {
			
			//boxes, smile, flag, time counter in short everything drawing this component 
			
			graph.setColor(new Color(41, 53, 63));
			graph.fillRect(0, 0, 1920, 1080);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					graph.setColor(Color.gray);
					if (mouseX >= buttonGridSpacing+i*80+buttonGridSpacing/2 && mouseX < i*80+80-buttonGridSpacing/2 && mouseY >= buttonGridSpacing+j*80+80+26 && mouseY < buttonGridSpacing+j*80+177 ) {
						graph.setColor(Color.darkGray);
					}
					
					//************
					//mines painted yellow for easy testable
					if (mines[i][j] == 1) {
						graph.setColor(Color.yellow);
					}
					
					//**************
					
					if (released[i][j] == true) {
						graph.setColor(Color.white);
						if (neighbours[i][j] == 0 ) {
							graph.setColor(new Color(41, 53, 63));

						}
						
						//if mines released the boxes will be red 
						if (mines[i][j] == 1) {
							graph.setColor(Color.red);
						}
					}
					
					graph.fillRect(buttonGridSpacing+i*80, buttonGridSpacing+j*80+80, 80-2*buttonGridSpacing, 80-2*buttonGridSpacing);
					
					//this part if you release the clear box and this box has a neighs, show neighs number.
					//but you released the mines, draw the mine on the box
					if (released[i][j] == true) {
						graph.setColor(Color.black);
						if (mines[i][j] == 0) {
							if (neighbours[i][j] > 0) {
								graph.setFont(new Font("Thoma" , Font.BOLD, 40));
								graph.drawString(Integer.toString(neighbours[i][j]), i*80+27, j*80+135);
							}
						}
						else {
							graph.fillRect(i*80+30, j*80+100, 20, 40);
							graph.fillRect(i*80+20, j*80+110, 40, 20);
							graph.fillRect(i*80+25, j*80+105, 30, 30);
						}
					}

					//flag drawing on flagged boxes
					if (flagged[i][j] == true) {
						graph.setColor(Color.black);
						graph.fillRect(i*80+34, j*80+80+15, 7, 45);
						graph.fillRect(i*80+24, j*80+80+53, 25, 8);
						graph.setColor(Color.red);
						graph.fillRect(i*80+34, j*80+80+11, 7, 6);
						graph.fillRect(i*80+28, j*80+80+15, 13, 6);
						graph.fillRect(i*80+22, j*80+80+21, 19, 6);
						graph.fillRect(i*80+15, j*80+80+27, 25, 6);
					}
				}
			}
			
			
			//smile so restart button drawin
			graph.setColor(Color.yellow);
			graph.fillOval(smileX, smileY, 70, 70);
			graph.setColor(Color.black);
			graph.fillOval(smileX+15, smileY+20, 10, 10);
			graph.fillOval(smileX+45, smileY+20, 10, 10);
			
			if (isHappy) {
				graph.fillRect(smileX+20, smileY+50, 30, 5);	
				graph.fillRect(smileX+15, smileY+45, 5, 5);	
				graph.fillRect(smileX+50, smileY+45, 5, 5);	
			}
			else {
				graph.fillRect(smileX+20, smileY+50, 30, 5);	
			}
			
			
			//time counter drawing at top-right of screen
			if (lose == false && win == false) {
				sec = (int) (new Date().getTime() - startedDate.getTime()) / 1000 ;
			}
			if (sec > 999) {
				sec = 999;
			}
			graph.setColor(Color.red);
			graph.setFont(new Font("Tahoma", Font.ROMAN_BASELINE, 75));
			
			if (sec < 10) {
				graph.drawString("00" + Integer.toString(sec), counterX+50, counterY+62);
			}
			else if (sec < 100) {
				graph.drawString("0" + Integer.toString(sec), counterX+50, counterY+62);
			}
			else {
				graph.drawString(Integer.toString(sec), counterX+50, counterY+62);
			}
			
			//message at you win or lose
			if (win) {
				graph.setColor(Color.green);
				graph.fillRect(0, 87, 1920, 1080);
				graph.setColor(Color.white);
				graph.setFont(new Font("Thoma" , Font.BOLD, 92));
				graph.drawString("You Win!", 754, 555);
			}
			if (lose) {
				graph.setColor(Color.red);
				graph.fillRect(0, 87, 1920, 1080);
				graph.setColor(Color.white);
				graph.setFont(new Font("Thoma" , Font.BOLD, 92));
				graph.drawString("You Lose!", 754, 555);
				graph.setFont(new Font("Thoma", Font.BOLD, 32));
				graph.setColor(Color.white);
				graph.drawString("You wanna continue ?", 759, 675);
				graph.setColor(Color.green);
				graph.fillRect(1100, 635, 50, 50);
				graph.setColor(Color.black);
				graph.setFont(new Font("Thoma", Font.BOLD, 24));
				graph.drawString(" Yes?", 1094, 668);
			}

			
			//flag drawing at top of screen
			if (flag) {
				graph.setColor(Color.white);
				graph.fillRect(flagX, flagY+5, 65, 65);
			}
			graph.setColor(Color.black);
			graph.fillRect(flagX+31, flagY+15, 7, 45);
			graph.fillRect(flagX+21, flagY+53, 25, 8);
			graph.setColor(Color.red);
			graph.fillRect(flagX+31, flagY+11, 7, 6);
			graph.fillRect(flagX+25, flagY+15, 13, 6);
			graph.fillRect(flagX+19, flagY+21, 19, 6);
			graph.fillRect(flagX+13, flagY+27, 25, 6);
			
			
			//find mines at the board and print at mid-right on screen
			graph.setColor(Color.white);
			graph.setFont(new Font("Thoma", Font.BOLD, 36));
			graph.drawString("Current Mines: ", counterX-300, counterY+57);
			graph.drawString(Integer.toString(currentMines), counterX-45, counterY+57);
			
			//flag counter
			graph.setColor(Color.white);
			graph.setFont(new Font("Thoma", Font.BOLD, 36));
			graph.drawString("Current Flags: ", counterX+225, counterY+57);
			graph.drawString(Integer.toString(currentFlags), counterX+495, counterY+57);

			
			//calculate your point with flagged mine boxes and print with win or lose message
			if (lose || win) {
				graph.setColor(Color.white);
				graph.setFont(new Font("Thoma", Font.BOLD, 36));
				graph.drawString("Your point : ", 755, 625);
				graph.drawString(Integer.toString(point), 983, 627);
				graph.setFont(new Font("Thoma", Font.BOLD, 26));
				graph.drawString("Your play time : ", 755, 718);
				graph.drawString(Integer.toString(sec), 1005, 720);
			}
			
			
			//drawing 3 different boxes to change game difficult
			graph.setColor(Color.green);
			graph.fillRect(45, 25, 35, 35);
			
			graph.setColor(Color.orange);
			graph.fillRect(95, 25, 35, 35);
			
			graph.setColor(Color.red);
			graph.fillRect(145, 25, 35, 35);
			
			graph.setColor(Color.white);
			graph.setFont(new Font("Thoma", Font.LAYOUT_NO_LIMIT_CONTEXT,18));
			graph.drawString("difficult :", 48, 22);
		}
	}
	
	
	public class mouseMovement implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
		}
		
		@Override
		//get the cursor's x-y location
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}

	
	public class Click implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			//everything you see here : 
			//find boxes of contain mine and if this box has a mine, mine counter increases
			//if you flagged box of contain mine your point increases but if you reflag the box your point will be decreases
			//
			if (boxX() != -1 && boxY() != -1 ) {
				
				if (flag == true && released[boxX()][boxY()] == false) {
					if (flagged[boxX()][boxY()] == false && currentFlags > 0) {
						currentFlags--;
						if (mines[boxX()][boxY()] == 1) {
							currentMines--;
						}
						flagged[boxX()][boxY()] = true;
					}
					else {
						if (flagged[boxX()][boxY()] == true) {
							currentMines++;
							currentFlags++;
							point--;
						}
						flagged[boxX()][boxY()] = false;
					}
				}
				else {
					if (flagged[boxX()][boxY()] == false) {
						released[boxX()][boxY()] = true;
					}
				}
				
				if (mines[boxX()][boxY()] == 1 && flagged[boxX()][boxY()] == true ) {
					point++;
				}
			}
			
			//clicked at smile button game will be restart
			if (inSmile()) {
				restart();
			}
			
			//clicked at flag button boolean flag is change to true and you can put flag on boxes
			if (inFlag()) {
				if (flag == false) {
					flag = true;
				}
				else {
					flag = false;
				}
			}
			
			//if clicked easy-mid-hard button change game terms. for example if click at easy button grid is 9x9, estimated mines 10-17
			if (inEasy()) {
				currentFlags = 25;
				continueMines = 5;
				difficult = 15;
				width = 9;
				height = 9;
				restart();
			}
			if (inMid()) {
				currentFlags = 60;
				continueMines = 25;
				width = 16 ;
				height = 9;
				difficult = 35;
				restart();
			}
			if (inHard()) {
				currentFlags = 95;
				continueMines = 30;
				width = 24;
				height = 12;
				difficult = 30;
				restart();
			}
			if (inContinue()) {
				isHappy = true;
				iscontinue = true;
				isWon();
				continueGame();
				
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}
	
	public void draw() {
		
		//locating mines according to your changed difficult level
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (random.nextInt(100) < difficult) {
					mines[i][j] = 1;
				}
				else {
					mines[i][j] = 0;
				}
				released[i][j] = false;
				flagged[i][j] = false;
			}
		}
		
		//finding your released box's neighs
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				neighs = 0;
				for (int m = 0; m < width; m++) {
					for (int n = 0; n < height; n++) {
						if (!((m == i && n == j)) ) {
							if (isNeighs(i, j, m, n) == true) {
								neighs++;
							}
						}
					}
				}
				neighbours[i][j] = neighs;
			}
		}
	}
	
	public void findMines() {
		
		//finding mines to show you mine count
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (mines[i][j] == 1) {
					currentMines++;
				}
			}
		}
	}
	
	public int boxX () {
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (mouseX >= buttonGridSpacing+i*80+buttonGridSpacing/2 && mouseX < i*80+80-buttonGridSpacing/2 && mouseY >= buttonGridSpacing+j*80+80+26 && mouseY < buttonGridSpacing+j*80+177 ) {
					return i;
				}
			}
		}
		return -1;
	}

	public int boxY () {
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (mouseX >= buttonGridSpacing+i*80+buttonGridSpacing/2 && mouseX < i*80+80-buttonGridSpacing/2 && mouseY >= buttonGridSpacing+j*80+80+26 && mouseY < buttonGridSpacing+j*80+177 ) {
					return j;
				}
			}
		}
		return -1;
	}
	
	//controling is neigh or not
	public boolean isNeighs(int mX, int mY, int cX, int cY ) {
		
		if (mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1) {
			return true;
		}
		return false;
	}
	
	//checking your cursor in the smile circle or not
	public boolean inSmile() {
		
		int a = (int) Math.sqrt(Math.abs(mouseX-resX)*Math.abs(mouseX-resX)+Math.abs(mouseY-resY)*Math.abs(mouseY-resY));
		if (a < 35) {
			return true;
		}
		return false;
	}
	
	//same think at the inSmile
	public boolean inFlag() {
		
		int a = (int) Math.sqrt(Math.abs(mouseX-flagButtonX)*Math.abs(mouseX-flagButtonX)+Math.abs(mouseY-flagButtonY)*Math.abs(mouseY-flagButtonY));
		if (a < 35) {
			return true;
		}
		return false;
	}

	//checking your cursor in the easyDifBox or not
	public boolean inEasy() {
		
		if (mouseX < 88 && mouseX > 48 && mouseY < 85 && mouseY > 50 ) {
			return true;
		}
		return false;
	}
	
	//same think at inEasy
	public boolean inMid() {
		
		if (mouseX < 133 && mouseX > 98 && mouseY < 85 && mouseY > 50 ) {
			return true;
		}
		return false;
	}
	
	//same think at inEasy
	public boolean inHard() {
		
		if (mouseX < 182 && mouseX > 147 && mouseY < 85 && mouseY > 50 ) {
			return true;
		}
		return false;
	}
	
	public boolean inContinue() {
		if (mouseX < 1154 && mouseX > 1104 && mouseY < 710 && mouseY > 660 ) {
			return true;
		}
		return false;
	}
	
	//if you released the mine box you lose, if you flagged all mines box you won. isWon checking this
	public void isWon() {
		
		if (iscontinue == false) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (released[i][j] == true && mines[i][j] == 1) {
							lose = true;
							isHappy = false;
					}
				}
			}
		}
		
		if (iscontinue == true) {
			lose = false;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (released[i][j] == true && mines[i][j] == 1) {
							isHappy = false;
							
					}
				}
			}
		}

		
		if (currentMines == 0 || releasedBoxes() + allMines() == width*height) {
			win = true;
		}
	}
	
	
	public int allMines() {
		
		int all = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (this.mines[i][j] == 1) {
					all++;
				}
			}
		}
		return all;
	}
	
	//if you click a box, this box released and saved the array of released with x and y cord
	public int releasedBoxes() {
		int released = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (this.released[i][j] == true ) {
					released++;
				}
			}
		}
		return released;
	}
	
	public int releasedCleanBoxes() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (this.released[i][j] == true && mines[i][j] == 0 ) {
					releasedClean++;
				}
			}
		}
		return releasedClean;
	}
	
	public void continueGame () {
		

		
		int i = random.nextInt(width);
		int j = random.nextInt(height);
		
		for (int k = 0; k < continueMines; k++) {

			if (mines[i][j] == 0 && released[i][j] == false) {
				mines[i][j] = 1;
				currentMines++;
			}
			i = random.nextInt(width);
			j = random.nextInt(height);
		}
		lose = false;
		
	}
	
	
	//if you clicked to smile, game was restart and all of thing back to default value
	public void restart() {
		
		isrestarted = true;
		startedDate = new Date();
		isHappy = true;
		win = false;
		lose = false;
		flag = false;
		currentMines = 0;
		point = 0;
		draw();
		findMines();
		isrestarted = false;
		iscontinue = false;

	}
}
