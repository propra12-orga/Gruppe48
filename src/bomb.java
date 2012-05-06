public class bomb {
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
		field.setBomb(s,xKoordinate,yKoordinate);
	}
	public void getPlayerPosition(){ //greift auf die Spielerposition im field.java zurück
		xKoordinate = field.x;
		yKoordinate = field.y;
		
	}
	public void BOOM(){
		//timer schreiben
		/*if (fieldGenerator.Map[radius1()][yKoordinate]==2){
			fieldContent.setContent(0);
			
		}
		*/
	}
	public int radius1() {
		int a = xKoordinate +1;	
		
		return  a;
	}
	public int radius2() {
	    int b = xKoordinate -1;		
		
		return  b;
	}
	public int radius3() {
		int c = yKoordinate +1;
		
		
		return  c;
	}
	public int radius4() {
		int d = yKoordinate -1;
		
		return  d;
	}
}
