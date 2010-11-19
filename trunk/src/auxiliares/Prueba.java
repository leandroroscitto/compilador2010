package auxiliares;

import java.io.FileNotFoundException;

public class Prueba {

	public static void main(String[] args) throws FileNotFoundException{
		MEPa m = new MEPa("mepa.txt");
		m.generarEntSal();
		m.Mimprimir("12","23","SUMA");
		m.Mimprimir(m.CharToMepa("Z"));
	}
	
}
