import java.util.TimerTask;

public class Task extends TimerTask {

	public void run() {
		bomb.isExploded= true;
		System.out.println("Bombe explodierte nach 3 sekunden:"+bomb.isExploded);
		bomb.bombe.cancel();
	}

}