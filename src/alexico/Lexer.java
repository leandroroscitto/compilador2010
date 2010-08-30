package alexico;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import excepciones.ExcepComMalForm;
import excepciones.ExcepEOFCom;
import excepciones.ExcepIdentNoValid;
import excepciones.ExcepSimbNoValido;

public class Lexer {
	private final String RXLOD = "[a-zA-Z0-9]";
	private final String RXNLOD = "[^a-zA-Z0-9]";
	private final String RXLETRA = "[a-zA-Z]";
	private final String RXDIGITO = "[0-9]";
	private final String RXALFABETO = "[a-zA-Z0-9+*\\-=<>()\\[\\]{}.,;:'\t\n\r ]";
	private final String RXSIMBOLO = "[.=>]";

	char caractual;
	public int nlinea;
	boolean eof = false;
	ArrayList<String> Palabras;
	BufferedReader reader;

	// -------------------------------------------------------------------------
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

	// -------------------------------------------------------------------------
	// Crea una nueva instancia del Lexer, dado un archivo de entrada
	public Lexer(String fileurl) throws ExcepSimbNoValido, IOException {
		FileInputStream fis = new FileInputStream(fileurl);
		InputStreamReader isr = new InputStreamReader(fis);
		reader = new BufferedReader(isr);

		nlinea = 1;

		// Crea la lista de palabras reservadas que detecta
		Palabras = new ArrayList<String>();
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

		// Lee un caracter, para estar siempre uno adelantado
		caractual = leerchar();
	}

	// -------------------------------------------------------------------------
	// Lee un caracter del buffer de entrada
	// En el caso de llegar al final de un archivo, lo indica
	// en la variable eof
	private char leerchar() throws ExcepSimbNoValido, IOException {
		char c = '$';
		if (reader.ready()) {
			c = (char) reader.read();

		} else {
			eof = true;
		}

		return c;
	}
	// -------------------------------------------------------------------------
	// Construye o continua la construccion de un identificador
	private Token scanIdent(String lexema) throws ExcepSimbNoValido, IOException {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXLOD, String.valueOf(caractual)) && !eof) {
			lexema += caractual;
			caractual = leerchar();
		}
		return new Token(Token.TIDENTIFICADOR, lexema, nlinea);
	}
	// -------------------------------------------------------------------------
	// Construye o continua la construccion de un numero
	// (secuencia pura de digitos)
	private Token scanNum(String lexema) throws ExcepIdentNoValid, ExcepSimbNoValido, IOException {
		lexema += caractual;
		caractual = leerchar();
		while (Pattern.matches(RXDIGITO, String.valueOf(caractual)) && !eof) {
			lexema += caractual;
			caractual = leerchar();
		}
		if (Pattern.matches(RXLETRA, String.valueOf(caractual))) {
			throw new ExcepIdentNoValid(nlinea);
		}
		return new Token(Token.TNUMERO, lexema, nlinea);
	}

	// -------------------------------------------------------------------------
	private boolean Posible_Simbolo_Compuesto(String lexema) {
		return (lexema.equals("<") || lexema.equals(">") || lexema.equals(".") || lexema.equals(":"));
	}
	// -------------------------------------------------------------------------
	private Token identificar_simbolo(String lexema) {
		Token T = null;

		if (lexema.equals("=")) {
			T = new Token(Token.TSIMBOLO_IGUAL, lexema, nlinea);
		}
		if (lexema.equals("<>")) {
			T = new Token(Token.TSIMBOLO_DISTINTO, lexema, nlinea);
		}
		if (lexema.equals("<")) {
			T = new Token(Token.TSIMBOLO_MENOR, lexema, nlinea);
		}
		if (lexema.equals(">")) {
			T = new Token(Token.TSIMBOLO_MAYOR, lexema, nlinea);
		}
		if (lexema.equals("<=")) {
			T = new Token(Token.TSIMBOLO_MENORIGUAL, lexema, nlinea);
		}
		if (lexema.equals(">=")) {
			T = new Token(Token.TSIMBOLO_MAYORIGUAL, lexema, nlinea);
		}
		if (lexema.equals("+")) {
			T = new Token(Token.TOPERMAS, lexema, nlinea);
		}
		if (lexema.equals("-")) {
			T = new Token(Token.TOPERMENOS, lexema, nlinea);
		}
		if (lexema.equals("*")) {
			T = new Token(Token.TOPERMULT, lexema, nlinea);
		}
		if (lexema.equals("(")) {
			T = new Token(Token.TPARENTA, lexema, nlinea);
		}
		if (lexema.equals(")")) {
			T = new Token(Token.TPARENTC, lexema, nlinea);
		}
		if (lexema.equals("[")) {
			T = new Token(Token.TCORA, lexema, nlinea);
		}
		if (lexema.equals("]")) {
			T = new Token(Token.TCORC, lexema, nlinea);
		}
		if (lexema.equals(":=")) {
			T = new Token(Token.TASIGN, lexema, nlinea);
		}
		if (lexema.equals(".")) {
			T = new Token(Token.TPUNTO, lexema, nlinea);
		}
		if (lexema.equals(",")) {
			T = new Token(Token.TCOMA, lexema, nlinea);
		}
		if (lexema.equals(";")) {
			T = new Token(Token.TPUNTO_Y_COMA, lexema, nlinea);
		}
		if (lexema.equals(":")) {
			T = new Token(Token.TDOSPUNTOS, lexema, nlinea);
		}
		if (lexema.equals("'")) {
			T = new Token(Token.TCOMMILLA_SIMPLE, lexema, nlinea);
		}
		if (lexema.equals("..")) {
			T = new Token(Token.TDOBLEPUNTO, lexema, nlinea);
		}
		return T;
	}

	// -------------------------------------------------------------------------
	// Construye o continua la construccion de un simbolo
	private Token scanSimb(String lexema) throws ExcepSimbNoValido, IOException {
		lexema += caractual;
		caractual = leerchar();
		while (Posible_Simbolo_Compuesto(lexema) && Pattern.matches(RXSIMBOLO, String.valueOf(caractual)) && !eof) {
			lexema += caractual;
			caractual = leerchar();
		}
		return identificar_simbolo(lexema);
	}

	// -------------------------------------------------------------------------
	// Saltea todos los espacios, tabs y finales de linea
	// Actualiza el numero de linea
	private void scanEspacios() throws ExcepSimbNoValido, IOException {
		while (Pattern.matches("[ \t\n\r]", String.valueOf(caractual)) && !eof) {
			if (Pattern.matches("[\n]", String.valueOf(caractual))) {
				nlinea++;
			}
			caractual = leerchar();
		}
	}

	// -------------------------------------------------------------------------
	// Una vez leido el simbolo '{', saltea todo caracter a continuacion
	// (incluyendo bajadas de linea y retorno de carro) hasta encontrar
	// el simbolo '}'.
	// En el caso de llegar a eof, lanza un excepción.
	private void scanComentario() throws ExcepSimbNoValido, IOException, ExcepEOFCom {
		int nlinc = nlinea;
		while (matchbut(String.valueOf(caractual), "(.|[\n\r])", "[}]") && !eof) {
			if (Pattern.matches("[\n]", String.valueOf(caractual))) {
				nlinea++;
			}
			caractual = leerchar();
		}

		// Llego a fin de linea en comentario
		if (eof) {
			throw new ExcepEOFCom(nlinc);
		}

		// Termino el comentario
		if (Pattern.matches("[}]", String.valueOf(caractual))) {
			caractual = leerchar();
		}
	}

	// -------------------------------------------------------------------------
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

	// -------------------------------------------------------------------------
	private Token identificar_palabra_reservada(String lexema) {
		Token T = null;
		String lexemaminusc = lexema.toLowerCase();

		if (lexemaminusc.equals("if")) {
			return new Token(Token.TPALRES_IF, lexema, nlinea);
		}
		if (lexemaminusc.equals("then")) {
			return new Token(Token.TPALRES_THEN, lexema, nlinea);
		}
		if (lexemaminusc.equals("else")) {
			return new Token(Token.TPALRES_ELSE, lexema, nlinea);
		}
		if (lexemaminusc.equals("of")) {
			return new Token(Token.TPALRES_OF, lexema, nlinea);
		}
		if (lexemaminusc.equals("while")) {
			return new Token(Token.TPALRES_WHILE, lexema, nlinea);
		}
		if (lexemaminusc.equals("do")) {
			return new Token(Token.TPALRES_DO, lexema, nlinea);
		}
		if (lexemaminusc.equals("begin")) {
			return new Token(Token.TPALRES_BEGIN, lexema, nlinea);
		}
		if (lexemaminusc.equals("end")) {
			return new Token(Token.TPALRES_END, lexema, nlinea);
		}
		if (lexemaminusc.equals("const")) {
			return new Token(Token.TPALRES_CONST, lexema, nlinea);
		}
		if (lexemaminusc.equals("var")) {
			return new Token(Token.TPALRES_VAR, lexema, nlinea);
		}
		if (lexemaminusc.equals("type")) {
			return new Token(Token.TPALRES_TYPE, lexema, nlinea);
		}
		if (lexemaminusc.equals("array")) {
			return new Token(Token.TPALRES_ARRAY, lexema, nlinea);
		}
		if (lexemaminusc.equals("function")) {
			return new Token(Token.TPALRES_FUNCTION, lexema, nlinea);
		}
		if (lexemaminusc.equals("program")) {
			return new Token(Token.TPALRES_PROGRAM, lexema, nlinea);
		}
		if (lexemaminusc.equals("procedure")) {
			return new Token(Token.TPALRES_PROCEDURE, lexema, nlinea);
		}
		if (lexemaminusc.equals("div")) {
			return new Token(Token.TOPERDIV, lexema, nlinea);
		}
		if (lexemaminusc.equals("and")) {
			return new Token(Token.TOPER_AND, lexema, nlinea);
		}
		if (lexemaminusc.equals("or")) {
			return new Token(Token.TOPER_OR, lexema, nlinea);
		}
		if (lexemaminusc.equals("not")) {
			return new Token(Token.TOPER_NOT, lexema, nlinea);
		}
		return T;
	}

	// -------------------------------------------------------------------------
	// Luego de leer un caracter letra, determina si el proximo token
	// es una palabra reservada del lenguaje, o si es un identificador
	// (únicas dos posibilidades)
	// Retorna el token correspondiente en cada caso.
	private Token scanPR(ArrayList<String> Palabras, String lexema) throws ExcepSimbNoValido, IOException {
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
				return scanIdent(lexema.toLowerCase());
			}
			if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
				// esto hay que hacerlo en una funcion aparte.
				return identificar_palabra_reservada(lexema);
			}
		}

		// Caso contrario que siga el nextToken
		return null;
	}

	// -------------------------------------------------------------------------
	public Token nextToken() throws ExcepIdentNoValid, ExcepSimbNoValido, IOException, ExcepEOFCom, ExcepComMalForm {
		String lexema = "";

		scanEspacios();

		if (!eof) {
			// Si no esta en el alfabeto de entrada
			// levanta una excepción
			if (!Pattern.matches(RXALFABETO, String.valueOf(caractual))) {
				throw new ExcepSimbNoValido(caractual, nlinea);
			}

			// Si lee una letra, es una palabra reservada o un
			// identificador, no hay otra opción
			if (Pattern.matches(RXLETRA, String.valueOf(caractual))) {
				Token t = scanPR(Palabras, lexema);
				return t;
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

			// Si encuentro simbolo que cierra comentario, pero no estoy
			// dentro de comentario, tiro la excepcion
			if (Pattern.matches("[}]", String.valueOf(caractual))) {
				throw new ExcepComMalForm(nlinea);
			}

			// Cualquier otro caracter
			// A Completar con los simbolos restantes
			if (matchbut(String.valueOf(caractual), ".", "[a-zA-Z0-9{}]")) {
				return scanSimb(lexema);
			}

			// HUBO UN ERROR
			return new Token(-1, "ERROR", nlinea);
		} else {
			// PARA INDICAR NOMAS, CAMBIAR
			return new Token(Token.TEOF, "<EOF>", nlinea);
		}
	}
	// -------------------------------------------------------------------------
	/**
	 * @param args
	 * @throws ExcepIdentNoValid
	 * @throws IOException
	 * @throws ExcepSimbNoValido
	 * @throws ExcepEOFCom
	 * @throws ExcepComMalForm
	 */
	public static void main(String[] args) throws ExcepIdentNoValid, ExcepSimbNoValido, IOException, ExcepEOFCom,
			ExcepComMalForm {
		// TODO Auto-generated method stub
		Lexer L = new Lexer("./ALUMNOS.pas");

		Token T = L.nextToken();
		System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "', numero de linea=" + T.nlinea + ".");
		while (T.tipo != Token.TEOF) {
			T = L.nextToken();
			System.out.println("Tipo: " + T.tipo + ", lexema:'" + T.lexema + "', numero de linea=" + T.nlinea + ".");
		}
	}
	// -------------------------------------------------------------------------
}
