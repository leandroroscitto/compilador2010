package pruebas;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Generador {
	private static List<Character> Letras = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
	private static List<Character> LetraDigito = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9');
	private static List<Character> Digitos = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	private static List<Character> Simbolos = Arrays.asList('=', '>', '<', ':', '.', '+', '-', '(', ')', ',', ';');
	/*private static List<Character> Simbolos2 = Arrays.asList('=', '>', '<', ':', '.', '+', '-', '\'', '*', '(', ')',
			',', ';');*/

	private static String genIdent(int longitud) {
		Random R = new Random();
		String ident = "";
		ident += Letras.get(R.nextInt(Letras.size()));
		for (int i = 0; i < longitud; i++) {
			ident += LetraDigito.get(R.nextInt(LetraDigito.size()));
		}

		return ident;
	}

	private static String genNumero(int longitud) {
		Random R = new Random();
		String numero = "";
		for (int i = 0; i < longitud; i++) {
			numero += Digitos.get(R.nextInt(Digitos.size()));
		}

		return numero;
	}

	private static String genSimbolo(int longitud) {
		Random R = new Random();
		String simb = "";
		for (int i = 0; i < longitud; i++) {
			simb += Simbolos.get(R.nextInt(Simbolos.size()));
		}

		return simb;
	}

	private static String genComentario(int longitud) {
		Random R = new Random();
		String com = "";
		if (R.nextBoolean()) {
			com += '{';
			for (int i = 0; i < 1; i++) {
				//com += (char) (R.nextInt(127-32) + 32);
				com += "COMENTARIOOOOOOO";
			}
			com += '}';
		} else {
			com += "(*";
			for (int i = 0; i < 1; i++) {
				//com += (char) (R.nextInt(127-32) + 32);
				com += "COMENTARIOOOOOOO";
			}
			com += "*)";
		}

		return com;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String output = "Salida.pas";
		PrintStream PS = new PrintStream(output);

		Random R = new Random();
		for (int i = 0; i < 100; i++) {
			if (i % 5 == 0) {
				PS.println();
			}

			int k = R.nextInt(4);
			String palabra = "";
			switch (k) {
				case 0 :
					palabra = genIdent(R.nextInt(20)) + genSimbolo(R.nextInt(2) + 1);
					break;
				case 1 :
					palabra = genNumero(R.nextInt(10)) + genSimbolo(R.nextInt(2) + 1);
					break;
				case 2 :
					palabra = genSimbolo(R.nextInt(2) + 1);
					break;
				case 3:
					palabra = genComentario(R.nextInt(20));
					break;
			}
			PS.print(palabra);
		}
	}

}
