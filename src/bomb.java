public class bomb extends field{
	public int xKoordinate; 
	public int yKoordinate;
	public bomb()
	{
		createBomb();
		BOOM();
	}
	public void createBomb(){  //erzeugt eine Bombe abh�ngig von der Spielerposition
		getPlayerPosition();
		bomb s = new bomb();
		setBomb(s,xKoordinate,yKoordinate);
	}
	public void getPlayerPosition(){ //greift auf die Spielerposition im field.java zur�ck
		xKoordinate = field.x;
		yKoordinate = field.y;
		
	}
	public void BOOM(){
		//timer schreiben
	
		}
		
	
	public int radius(int x) {  //radius verstellbar f�r sp�ter aufgaben +1... erh�hen
		int a[] = new int[3];
		a[0] = xKoordinate +1;   //rechts vom spieler
		a[1] = xKoordinate -1;   //links vom spieler
		a[2] = yKoordinate +1;   //�ber dem spieler
		a[3] = yKoordinate -1;   //unter dem spieler
		
		return  a[x];
	}
	
}
