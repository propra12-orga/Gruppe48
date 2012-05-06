public class bomb extends field{
	public int xKoordinate; 
	public int yKoordinate;
	public bomb()
	{
		createBomb();
		BOOM();
	}
	public void createBomb(){  //erzeugt eine Bombe abhängig von der Spielerposition
		getPlayerPosition();
		bomb s = new bomb();
		setBomb(s,xKoordinate,yKoordinate);
	}
	public void getPlayerPosition(){ //greift auf die Spielerposition im field.java zurück
		xKoordinate = field.x;
		yKoordinate = field.y;
		
	}
	public void BOOM(){
		//timer schreiben
	
		}
		
	
	public int radius(int x) {  //radius verstellbar für später aufgaben +1... erhöhen
		int a[] = new int[3];
		a[0] = xKoordinate +1;   //rechts vom spieler
		a[1] = xKoordinate -1;   //links vom spieler
		a[2] = yKoordinate +1;   //über dem spieler
		a[3] = yKoordinate -1;   //unter dem spieler
		
		return  a[x];
	}
	
}
