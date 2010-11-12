package auxiliares;

import java.io.FileNotFoundException;

public class Prueba {

	public static void main(String[] args) throws FileNotFoundException{
		MEPa m = new MEPa("mepa.txt");
		m.Mimprimir(new String[]{"12","23","SUMA"});
	}
	
}
