
public class main implements Runnable {
	
	gui guiComp = new gui();
	

	public static void main(String[] args) {
		
		new Thread(new main()).start();

	}
	
	@Override
	public void run() {
		
		while (true) {
			guiComp.repaint();
			
			if (guiComp.isrestarted == false) {
				guiComp.isWon();
			}
		}
	}
}
