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
	private final String RXLOD = "[a-zA-Z0-9]";
	private final String RXNLOD = "[^a-zA-Z0-9]";
	private final String RXLETRA = "[a-zA-Z]";
	private final String RXDIGITO = "[0-9]";
	private final String RXALFABETO = "[a-zA-Z0-9+\\*-=<>()\\[\\].,;:'\t\n\r ]";

	char caractual;
	public int nlinea;
	boolean eof = false;
	BufferedReader reader;

	// Dada una cadena input (representación de string de un char)
	// determina si machea con match, y no machea con nomatch.
	// Por ejemplo si machea con todos los caracteres (match=RXALFABETO) menos
	// 'r' (nomatch=[r]).

	// Se puede mover a una clase con funciones auxiliares
	public static boolean matchbut(String input, String match, String nomatch) {
		boolean machea1, machea2;

		machea1 = Pattern.matches(match, input);
		machea2 = Pattern.matches(nomatch, input);

		return ((machea1 == true) && (machea2 == false));
	}

	// Crea una nueva instancia del Lexer, dado un archivo de entrada
	public Lexer(String fileurl) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileurl);
		InputStreamReader isr = new InputStreamReader(fis);
		reader = new BufferedReader(isr);

		nlinea = 1;

		// Lee un caracter, para estar siempre uno adelantado
		caractual = leerchar();
	}

	// Lee un caracter del buffer de entrada
	// En el caso de llegar al final de un archivo, lo indica
	// en la variable eof
	private char leerchar() {
		try {
			if (reader.ready()) {
				char c = (char) reader.read();

				// Si no esta en el alfabeto de entrada
				// levanta una excepción
				if (Pattern.matches(RXALFABETO, String.valueOf(c))) {
					return c;
				} else {
					System.out.println("Excepción: caracter no válido.");
					// PARA CORTAR LA EJECUCION, NO NECESARIO DESPUES DE LA EXCEPCION
					eof = true;
					return '?';
				}
			} else {
				eof = true;
				return '$';
			}
		} catch (IOException e) {
			// MANEJAR MEJOR ESTO
			eof = true;
			return '$';
		}
	}

	// Construye o continua la construccion de un identificador
	private Token scanIdent(String lexema) {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXLOD, String.valueOf(caractual)) && !eof) {
			lexema += caractual;
			caractual = leerchar();
		}
		return new Token(Token.TIDENTIFICADOR, lexema, nlinea);
	}

	// Construye o continua la construccion de un numero
	// (secuencia pura de digitos)
	private Token scanNum(String lexema) {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXDIGITO, String.valueOf(caractual)) && !eof) {
			lexema += caractual;
			caractual = leerchar();
		}
		return new Token(Token.TNUMERO, lexema, nlinea);
	}

	// Saltea todos los espacios, tabs y finales de linea
	// Actualiza el numero de linea
	private void scanEspacios() {
		while (Pattern.matches("[ \t\n\r]", String.valueOf(caractual)) && !eof) {
			if (Pattern.matches("[\n]", String.valueOf(caractual))) {
				nlinea++;
			}
			caractual = leerchar();
		}
	}

	// Una vez leido el simbolo '{', saltea todo caracter a continuacion
	// (incluyendo bajadas de linea y retorno de carro) hasta encontrar
	// el simbolo '}'.
	// En el caso de llegar a eof, lanza un excepción.
	private void scanComentario() {
		int nlinc = nlinea;
		while (matchbut(String.valueOf(caractual), "(.|[\n\r])", "[}]") && !eof) {
			if (Pattern.matches("[\n]", String.valueOf(caractual))) {
				nlinea++;
			}
			caractual = leerchar();
		}

		// Llego a fin de linea en comentario
		if (eof) {
			System.out.println("Excepción: fin de linea en comentario. (Linea de inicio de comentario:" + nlinc + ")");
		}

		// Termino el comentario
		if (Pattern.matches("[}]", String.valueOf(caractual))) {
			caractual = leerchar();
		}
	}

	// Filtra todas las palabras de P que comiencen con el caracter
	// ini, y devuelve estas palabras sin ese caracter inicial.
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

	// Luego de leer un caracter letra, determina si el proximo token
	// es una palabra reservada del lenguaje, o si es un identificador
	// (únicas dos posibilidades)
	// Retorna el token correspondiente en cada caso.
	private Token scanPR(ArrayList<String> Palabras, String lexema) {
		if (Palabras.size() > 0) {
			HashSet<Character> Caracteres = new HashSet<Character>();
			for (String palabra : Palabras) {
				if (palabra.length() > 0) {
					Caracteres.add(palabra.charAt(0));
				}
			}

			for (char primercar : Caracteres) {
				String carUp = String.valueOf(Character.toUpperCase(primercar));
				String carLo = String.valueOf(Character.toLowerCase(primercar));

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
				return new Token(Token.TIDENTIFICADOR, lexema, nlinea);
			}
			// CONROLAR QUE EL SIMBOLO ESTE DENTRO DEL ALFABETO
			// (VER HACERLO DIRECTAMENTE EN leerchar())
		} else {
			// Encontre una palabra reservada?
			if (Pattern.matches(RXLOD, String.valueOf(caractual))) {
				return scanIdent(lexema);
			}
			if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
				return new Token(Token.TPR, lexema, nlinea);
			}
		}

		// Caso contrario que siga el nextToken
		return null;
	}

	public Token nextToken() {
		String lexema = "";

		scanEspacios();

		if (!eof) {
			// Si lee una letra, es una palabra reservada o un
			// identificador, no hay otra opción
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
				return new Token(Token.TOTRO, lexema, nlinea);
			}

			// HUBO UN ERROR
			return new Token(-1, "ERROR", nlinea);
		} else {
			// PARA INDICAR NOMAS, CAMBIAR
			return new Token(Token.TEOF, "<EOF>", nlinea);
		}
	}
	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Lexer L = new Lexer("./identifNoValido.pas");

		Token T = L.nextToken();
		System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "', numero de linea=" + T.nlinea + ".");
		while (T.tipo != Token.TEOF) {
			T = L.nextToken();
			System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "', numero de linea=" + T.nlinea + ".");
		}
	}

}
