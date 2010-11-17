package auxiliares;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import tipos.TTipo;

public class MEPa {
	private PrintStream PS;
	
	public boolean MestaEnFuncion = false;
	public String MLexemaUnidad = "";
	
	public boolean MretFuncion = false;
	
	public int MposVar = 0;
	
	public int EtiquetaAct = 0;
	
	public MEPa(String codint) throws FileNotFoundException{
		PS = new PrintStream(codint);
	}
	
	public void Mimprimir(String... args){
		for (String arg:args){
			PS.print(arg);
			PS.print(" ");
		}
		PS.println();
	}
	
	public String MobtProxEti(){
		EtiquetaAct++;
		return "label"+String.valueOf(EtiquetaAct);
	}
	
	public boolean chequear_tipo_oper(TTipo t1,TTipo t2,String lexema){
		// TODO
		return true;
	}
	
	public void generarEntSal(){
		// Imprimir entero
		Mimprimir("writeE","ENPR","1");
		Mimprimir("IMPR");
		Mimprimir("RTPR","1","1");
		
		// Imprimir entero y nueva linea
		Mimprimir("writelnE","ENPR","1");
		Mimprimir("IMLN");
		Mimprimir("RTPR","1","1");
		
		// Leo entero
		Mimprimir("readE","ENPR","1");
		Mimprimir("LEER");
		Mimprimir("RTPR","1","1");
		
		// Leo entero y nueva linea
		Mimprimir("writelnE","ENPR","1");
		Mimprimir("LELN");
		Mimprimir("RTPR","1","1");
		
		// Imprimir caracter
		Mimprimir("writeE","ENPR","1");
		Mimprimir("IMCH");
		Mimprimir("RTPR","1","1");
		
		// Imprimir caracter y nueva linea
		Mimprimir("writelnE","ENPR","1");
		Mimprimir("IMCN");
		Mimprimir("RTPR","1","1");
		
		// Leo caracter
		Mimprimir("readE","ENPR","1");
		Mimprimir("LECH");
		Mimprimir("RTPR","1","1");
		
		// Leo caracter y nueva linea
		Mimprimir("writelnE","ENPR","1");
		Mimprimir("LECN");
		Mimprimir("RTPR","1","1");
	}
	
	public void cerrarSalida(){
		PS.close();
	}
}
