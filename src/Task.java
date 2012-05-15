import java.util.TimerTask;

public class Task extends TimerTask {

	public void run() {
		System.out.println("HALLO");
		bomb.bombe.cancel();
	}

}