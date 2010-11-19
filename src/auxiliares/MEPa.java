package auxiliares;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import tipos.TTipo;

public class MEPa {
	public static final int maxint = 32767;

	private PrintStream PS;

	public boolean MestaEnFuncion = false;
	public String MLexemaUnidad = "";

	public boolean MretFuncion = false;

	public int MposVar = 0;

	public int EtiquetaAct = 11;

	public MEPa(String codint) throws FileNotFoundException {
		PS = new PrintStream(codint);
	}

	public void Mimprimir(String... args) {
		for (String arg : args) {
			PS.print(arg);
			PS.print(" ");
		}
		PS.println();
	}

	public String MobtProxEti() {
		EtiquetaAct++;
		return "L" + String.valueOf(EtiquetaAct);
	}

	public boolean chequear_tipo_oper(TTipo t1, TTipo t2, String lexema) {
		if (t1.comparar(t2)) {
			if (lexema.equals("+") || lexema.equals("-") || lexema.equals("*") || lexema.equals("div")) {
				return t1.clase == TTipo.TPENTERO;
			} else {
				// Es AND o OR
				return t1.clase == TTipo.TPBOOLEAN;
			}
		} else {
			return false;
		}
	}

	public String CharToMepa(String caracter) {
		int codigo = caracter.charAt(0);
		return String.valueOf(codigo);
	}

	public void generarEntSal() {
		// Imprimir entero
		Mimprimir("L4", "ENPR", "1");
		Mimprimir("IMPR");
		Mimprimir("RTPR", "1", ",", "1");

		// Imprimir entero y nueva linea
		Mimprimir("L5", "ENPR", "1");
		Mimprimir("IMLN");
		Mimprimir("RTPR", "1", ",", "1");

		// Leo entero
		Mimprimir("L6", "ENPR", "1");
		Mimprimir("LEER");
		Mimprimir("RTPR", "1", ",", "1");

		// Leo entero y nueva linea
		Mimprimir("L7", "ENPR", "1");
		Mimprimir("LELN");
		Mimprimir("RTPR", "1", ",", "1");

		// Imprimir caracter
		Mimprimir("L8", "ENPR", "1");
		Mimprimir("IMCH");
		Mimprimir("RTPR", "1", ",", "1");

		// Imprimir caracter y nueva linea
		Mimprimir("L9", "ENPR", "1");
		Mimprimir("IMCN");
		Mimprimir("RTPR", "1", ",", "1");

		// Leo caracter
		Mimprimir("L10", "ENPR", "1");
		Mimprimir("LECH");
		Mimprimir("RTPR", "1", ",", "1");

		// Leo caracter y nueva linea
		Mimprimir("L11", "ENPR", "1");
		Mimprimir("LECN");
		Mimprimir("RTPR", "1", ",", "1");
	}

	public void cerrarSalida() {
		PS.close();
	}
}
