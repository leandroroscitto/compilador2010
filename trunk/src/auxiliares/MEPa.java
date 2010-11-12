package auxiliares;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class MEPa {
	private PrintStream PS;
	public boolean MestaEnFuncion;
	public String MLexemaUnidad;
	
	public MEPa(String codint) throws FileNotFoundException{
		PS = new PrintStream(codint);
	}
	
	public void Mimprimir(String[] args){
		for (String arg:args){
			PS.print(arg);
			PS.print(" ");
		}
		PS.println();
	}
	
	public void cerrarSalida(){
		PS.close();
	}
}
