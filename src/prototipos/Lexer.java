package prototipos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Lexer {
	public class Token {
		public static final int TEOF = 0;
		public static final int TIDENT = 1;
		public static final int TNUM = 2;
		public static final int TPRVAR = 3;
		public static final int TOTRO = 4;
		public static final int TPRCONS = 5;

		public int tipo;
		public String lexema;

		public Token(int t, String l) {
			tipo = t;
			lexema = l;
		}
	}

	private final String RXLOD = "[a-zA-Z0-9]";
	private final String RXNLOD = "[^a-zA-Z0-9]";
	private final String RXLETRA = "[a-zA-Z]";
	private final String RXDIGITO = "[0-9]";

	public static boolean matchbut(String input, String match, String nomatch) {
		boolean machea1, machea2;

		machea1 = Pattern.matches(match, input);
		machea2 = Pattern.matches(nomatch, input);

		return ((machea1 == true) && (machea2 == false));
	}

	char caractual;
	boolean eof = false;
	BufferedReader reader;

	public Lexer(String fileurl) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileurl);
		InputStreamReader isr = new InputStreamReader(fis);
		reader = new BufferedReader(isr);

		caractual = leerchar();
	}

	private char leerchar() {
		try {
			char c = (char) reader.read();
			// System.out.println(c);
			return c;
		} catch (IOException e) {
			// $ no es un simbolo del alfabeto, se lo considera fin de linea
			return '$';
		}
	}

	private Token scanIdent(String lexema) {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXLOD, String.valueOf(caractual))) {
			lexema += caractual;
			caractual = leerchar();
		}
		return new Token(Token.TIDENT, lexema);
	}

	private Token scanNum(String lexema) {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXDIGITO, String.valueOf(caractual))) {
			lexema += caractual;
			caractual = leerchar();
		}
		return new Token(Token.TNUM, lexema);
	}

	private void scanEspacios() {
		while (Pattern.matches("[ \t\n\r]", String.valueOf(caractual))) {
			caractual = leerchar();
		}
	}

	private Token scanPR(ArrayList<String> Palabras) {
		String lexema = "";

		HashSet<Character> Caracteres = new HashSet<Character>();
		for (String palabra : Palabras) {
			if (palabra.length() > 1) {
				Caracteres.add(palabra.charAt(0));
			}
		}

		for (char primercar : Caracteres) {
			String carUp = "" + Character.toUpperCase(primercar);
			String carLo = "" + Character.toLowerCase(primercar);
			
			if (Pattern.matches("[" + carUp + carLo + "]", String.valueOf(caractual))) {
				lexema += caractual;
				caractual = leerchar();
				
				
			}
		}

		// Leo un caracter alfanum distinto de aA
		if (matchbut(String.valueOf(caractual), RXLOD, "[aA]")) {
			return scanIdent(lexema);
		}
		// No leo un caracter alfnum
		if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
			return new Token(Token.TIDENT, lexema);
		}
	}

	public Token nextToken() {
		String lexema = "";

		scanEspacios();

		// Leo vV
		if (Pattern.matches("[vV]", String.valueOf(caractual))) {
			lexema += caractual;
			caractual = leerchar();

			// Leo aA
			if (Pattern.matches("[aA]", String.valueOf(caractual))) {
				lexema += caractual;
				caractual = leerchar();

				// Leo rR
				if (Pattern.matches("[rR]", String.valueOf(caractual))) {
					lexema += caractual;
					caractual = leerchar();

					if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
						return new Token(Token.TPRVAR, lexema);
					}
					if (Pattern.matches(RXLOD, String.valueOf(caractual))) {
						return scanIdent(lexema);
					}
				}
				// Leo un caracter alfanum distinto de rR
				if (matchbut(String.valueOf(caractual), RXLOD, "[rR]")) {
					return scanIdent(lexema);
				}
				// No leo un caracter alfanum
				if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
					return new Token(Token.TIDENT, lexema);
				}
			}
			// Leo un caracter alfanum distinto de aA
			if (matchbut(String.valueOf(caractual), RXLOD, "[aA]")) {
				return scanIdent(lexema);
			}
			// No leo un caracter alfnum
			if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
				return new Token(Token.TIDENT, lexema);
			}
		}
		// Leo cC
		if (Pattern.matches("[cC]", String.valueOf(caractual))) {
			lexema += caractual;
			caractual = leerchar();

			// Leo oO
			if (Pattern.matches("[oO]", String.valueOf(caractual))) {
				lexema += caractual;
				caractual = leerchar();

				// Leo nN
				if (Pattern.matches("[nN]", String.valueOf(caractual))) {
					lexema += caractual;
					caractual = leerchar();

					if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
						return new Token(Token.TPRCONS, lexema);
					}
					if (Pattern.matches(RXLOD, String.valueOf(caractual))) {
						return scanIdent(lexema);
					}
				}
				// Leo un caracter alfanum distinto de nN
				if (matchbut(String.valueOf(caractual), RXLOD, "[nN]")) {
					return scanIdent(lexema);
				}
				// No leo un caracter alfanum
				if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
					return new Token(Token.TIDENT, lexema);
				}
			}
			// Leo un caracter alfanum distinto de oO
			if (matchbut(String.valueOf(caractual), RXLOD, "[oO]")) {
				return scanIdent(lexema);
			}
			// No leo un caracter alfnum
			if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
				return new Token(Token.TIDENT, lexema);
			}
		}
		// Leo un caracter distinto de vVcC
		if (matchbut(String.valueOf(caractual), RXLETRA, "[vVcC]")) {
			return scanIdent(lexema);
		}
		// Leo un digito
		if (Pattern.matches(RXDIGITO, String.valueOf(caractual))) {
			return scanNum(lexema);
		}
		// Leo el fin de linea
		if (Pattern.matches("[$]", String.valueOf(caractual))) {
			return new Token(Token.TEOF, "<EOF>");
		}

		if (matchbut(String.valueOf(caractual), ".*", "[a-zA-Z0-9$]")) {
			lexema += caractual;
			caractual = leerchar();
			return new Token(Token.TOTRO, lexema);
		}

		// HUBO UN ERROR
		return new Token(-1, "ERROR");

	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Lexer L = new Lexer("./Ejemplo1.pas");

		Token T = L.nextToken();
		System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "'.");
		while (T.tipo != Token.TEOF) {
			T = L.nextToken();
			System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "'.");
		}
	}

}
