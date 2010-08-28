package prototipos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Lexer {
	public class Token {
		public static final int TEOF = 0;
		public static final int TIDENTIFICADOR = 1;
		public static final int TNUMERO = 2;
		public static final int TPALRES_IF = 3;
		public static final int TPALRES_THEN = 4;
		public static final int TPALRES_ELSE = 5;
		public static final int TPALRES_OF = 6;
		public static final int TPALRES_WHILE = 7;
		public static final int TPALRES_DO = 8;
		public static final int TPALRES_BEGIN = 9;
		public static final int TPALRES_END = 10;
		public static final int TPALRES_CONST = 11;
		public static final int TPALRES_VAR = 12;
		public static final int TPALRES_TYPE = 13;
		public static final int TPALRES_ARRAY = 14;
		public static final int TPALRES_FUNCTION = 15;
		public static final int TPALRES_PROGRAM = 16;
		public static final int TPALRES_PROCEDURE = 17;
		public static final int TSIMBOLOREL_SIN_IGUAL = 18;
		public static final int TSIMBOLO_IGUAL = 19;
		public static final int TOPERARIT = 20;
		public static final int TOPERBOOL = 21;
		public static final int TPARENTA = 22;
		public static final int TPARENTC = 23;
		public static final int TCORA = 24;
		public static final int TCORC = 25;
		public static final int TASIGN = 26;
		public static final int TPUNTO = 27;
		public static final int TCOMA = 28;
		public static final int TPUNTO_Y_COMA = 29;
		public static final int TDOSPUNTOS = 30;
		public static final int TCOMMILLA_SIMPLE = 31;
		public static final int TDOBLEPUNTO = 32;

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
	public int nlinea;
	public int ncol;
	boolean eor = false;
	BufferedReader reader;

	public Lexer(String fileurl) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileurl);
		InputStreamReader isr = new InputStreamReader(fis);
		reader = new BufferedReader(isr);

		nlinea = 1;
		ncol = 1;

		caractual = leerchar();
	}

	private char leerchar() {
		try {
			if (reader.ready()) {
				ncol++;
				char c = (char) reader.read();
				return c;
			} else {
				eor = true;
				return '$';
			}
		} catch (IOException e) {
			// MANEJAR MEJOR ESTO
			eor = true;
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
		return new Token(Token.TIDENTIFICADOR, lexema);
	}

	private Token scanNum(String lexema) {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXDIGITO, String.valueOf(caractual))) {
			lexema += caractual;
			caractual = leerchar();
		}
		return new Token(Token.TNUMERO, lexema);
	}

	private void scanEspacios() {
		while (Pattern.matches("[ \t\n\r]", String.valueOf(caractual)) && !eor) {
			if (Pattern.matches("[\n]", String.valueOf(caractual))) {
				nlinea++;
				ncol = 1;
			}
			caractual = leerchar();
		}
	}

	private void scanComentario() {
		int nlinc = nlinea;
		while (matchbut(String.valueOf(caractual), "(.|[\n\r])", "[}]") && !eor) {
			if (Pattern.matches("[\n]", String.valueOf(caractual))) {
				nlinea++;
			}
			caractual = leerchar();
		}
		if (eor) {
			// Llego a fin de linea en comentario
			System.out.println("Excepci�n: fin de linea en comentario. (Linea de inicio de comentario:" + nlinc + ")");
		}
		if (Pattern.matches("[}]", String.valueOf(caractual))) {
			// Termino el comentario
			caractual = leerchar();
		}
	}

	private ArrayList<String> filtini(char ini, ArrayList<String> P) {
		ArrayList<String> Result = new ArrayList<String>();

		for (String palabra : P) {
			if (palabra.length() > 0) {
				if (palabra.charAt(0) == ini) {
					String spal = palabra.substring(1);
					if (spal.length() > 0) {
						Result.add(spal);
					}
				}
			}
		}

		return Result;
	}

	private Token scanPR(ArrayList<String> Palabras, String lexema) {
		if (Palabras.size() > 0) {
			HashSet<Character> Caracteres = new HashSet<Character>();
			for (String palabra : Palabras) {
				if (palabra.length() > 0) {
					Caracteres.add(palabra.charAt(0));
				}
			}

			for (char primercar : Caracteres) {
				String carUp = "" + Character.toUpperCase(primercar);
				String carLo = "" + Character.toLowerCase(primercar);

				if (Pattern.matches("[" + carUp + carLo + "]", String.valueOf(caractual))) {
					lexema += caractual;
					caractual = leerchar();

					return scanPR(filtini(primercar, Palabras), lexema);
				}
			}
			// Leo un caracter alfanum distinto de los primeros caracteres de las
			// palabras reservadas
			if (Pattern.matches(RXLOD, String.valueOf(caractual)) && (!Caracteres.contains(caractual))) {
				return scanIdent(lexema);
			}
			if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
				return new Token(Token.TIDENTIFICADOR, lexema);
			}
		} else {
			// Encontre una palabra reservada?
			if (Pattern.matches(RXLOD, String.valueOf(caractual))) {
				return scanIdent(lexema);
			}
			if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
				return new Token(Token.TPR, lexema);
			}
		}
		// Caso contrario que siga el nextToken
		return null;
	}

	public Token nextToken() {
		String lexema = "";

		scanEspacios();

		if (!eor) {
			// == PROBANDO ==
			 if (Pattern.matches(RXLETRA, String.valueOf(caractual))) {
			 ArrayList<String> Palabras = new ArrayList<String>();
			 Palabras.add("div");
			 Palabras.add("or");
			 Palabras.add("and");
			 Palabras.add("not");
			 Palabras.add("if");
			 Palabras.add("then");
			 Palabras.add("else");
			 Palabras.add("of");
			 Palabras.add("while");
			 Palabras.add("do");
			 Palabras.add("begin");
			 Palabras.add("end");
			 Palabras.add("const");
			 Palabras.add("var");
			 Palabras.add("type");
			 Palabras.add("array");
			 Palabras.add("function");
			 Palabras.add("procedure");
			 Palabras.add("program");
			 Token t = scanPR(Palabras, lexema);
			 if (t != null) {
			 return t;
			 }
			 }
			// == PROBANDO ==

			// PALABRA RESERVADA VAR
			// Leo vV
			/*
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

						// Si no es letra o digito, complet� la palabra reservada
						// 'var'
						if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
							return new Token(Token.TPALRES_VAR, lexema);
						}
						// Si es letra o digito, entonces no es la PR 'var', sino un
						// identificador
						if (Pattern.matches(RXLOD, String.valueOf(caractual))) {
							return scanIdent(lexema);
						}
					}

					// Leo un caracter alfanum distinto de rR, luego es un
					// identificador
					if (matchbut(String.valueOf(caractual), RXLOD, "[rR]")) {
						return scanIdent(lexema);
					}

					// No leo un caracter alfanum, por lo tanto es un identificador
					// 'va'
					if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
						return new Token(Token.TIDENTIFICADOR, lexema);
					}
				}

				// Leo un caracter alfanum distinto de aA, por lo tanto no es la PR
				// 'var', sino un identificador
				if (matchbut(String.valueOf(caractual), RXLOD, "[aA]")) {
					return scanIdent(lexema);
				}

				// No leo un caracter alfnum, es un identificador 'v'
				if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
					return new Token(Token.TIDENTIFICADOR, lexema);
				}
			}
			*/// FIN DE PALABRA RESERVADA VAR

			// Leo un caracter distinto de vV
			// Dever�a considerar todos los caracteres de inicio que no
			// corresponden a
			// palabras reservadas
			if (matchbut(String.valueOf(caractual), RXLETRA, "[vV]")) {
				return scanIdent(lexema);
			}

			// Leo un digito, es un numero
			if (Pattern.matches(RXDIGITO, String.valueOf(caractual))) {
				return scanNum(lexema);
			}

			// Leo {, inicio de comentario
			if (Pattern.matches("[{]", String.valueOf(caractual))) {
				scanComentario();
				return nextToken();
			}

			// Cualquier otro caracter
			// A Completar con los simbolos restantes
			if (matchbut(String.valueOf(caractual), ".", "[a-zA-Z0-9{]")) {
				lexema += caractual;
				caractual = leerchar();
				return new Token(Token.TOTRO, lexema);
			}

			// HUBO UN ERROR
			return new Token(-1, "ERROR");
		} else {
			// PARA INDICAR NOMAS, CAMBIAR
			return new Token(Token.TEOF, "<EOF>");
		}
	}
	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Lexer L = new Lexer("./Ejemplo1.pas");

		Token T = L.nextToken();
		System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "', numero de linea=" + L.nlinea + "|"
				+ (L.ncol - 1) + ".");
		while (T.tipo != Token.TEOF) {
			T = L.nextToken();
			System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "', numero de linea=" + L.nlinea + "|"
					+ (L.ncol - 1) + ".");
		}
	}

}
