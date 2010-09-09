package alexico;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import excepciones.ExcepALexico;
import excepciones.ExcepCaracMalForm;
import excepciones.ExcepEOFCom;
import excepciones.ExcepIdentNoValido;
import excepciones.ExcepSimbNoValido;

public class Lexer {

    private final String RXLOD = "[a-zA-Z0-9]";
    private final String RXNLOD = "[^a-zA-Z0-9]";
    private final String RXLETRA = "[a-zA-Z]";
    private final String RXDIGITO = "[0-9]";
    private final String RXALFABETO = "[a-zA-Z0-9+*\\-=<>()\\[\\]{.,;:'\t\n\r ]";
    private final int TCOMENTLLAVE = 0;
    private final int TCOMENTPARENT = 1;
    private char caractual;
    private int nlinea;
    private boolean eof = false;
    private List<String> Palabras;
    private BufferedReader reader;

    // -------------------------------------------------------------------------
    // Dada una cadena input (representaci�n de string de un char)
    // determina si machea con match, y no machea con nomatch.
    // Por ejemplo si machea con todos los caracteres (match=RXALFABETO) menos
    // 'r' (nomatch=[r]).
    public static boolean matchbut(String input, String match, String nomatch) {
        boolean machea1, machea2;

        machea1 = Pattern.matches(match, input);
        machea2 = Pattern.matches(nomatch, input);

        return ((machea1 == true) && (machea2 == false));
    }

    // -------------------------------------------------------------------------
    // Crea una nueva instancia del Lexer, dado un archivo de entrada
    // En el caso de que el archivo de entrada no exista, o no se pueda leer
    // de �l, produce una excepci�n de entrada-salida
    public Lexer(String fileurl) throws IOException {
        FileInputStream fis = new FileInputStream(fileurl);
        InputStreamReader isr = new InputStreamReader(fis);
        reader = new BufferedReader(isr);

        nlinea = 1;

        // Crea la lista de palabras reservadas que detecta
        Palabras = Arrays.asList("div", "or", "and", "not", "if", "then", "else", "of", "while", "do", "begin", "end",
                "const", "var", "type", "array", "function", "procedure", "program");

        // Lee un caracter, para estar siempre uno adelantado
        caractual = leerchar();
    }

    // -------------------------------------------------------------------------
    // Lee un caracter del buffer de entrada
    // En el caso de llegar al final de un archivo, lo indica en la variable eof
    private char leerchar() throws IOException {
        char c = '$';
        if (reader.ready()) {
            c = (char) reader.read();

        } else {
            eof = true;
        }
        return c;
    }

    // -------------------------------------------------------------------------
    // Identifica un s�mbolo de acuerdo a su lexema
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
        if (lexema.equals("..")) {
            T = new Token(Token.TDOBLEPUNTO, lexema, nlinea);
        }

        return T;
    }

    // -------------------------------------------------------------------------
    // Identifica el tipo de una palabra reservada, de acuerdo a su lexema
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
    // Filtra todas las palabras de P que comiencen con el caracter ini, y
    // devuelve estas palabras sin ese caracter inicial.
    private List<String> filtini(char ini, List<String> P) {
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
    // Construye o continua la construccion de un identificador
    private Token scanIdent(String lexema) throws IOException {
        lexema += caractual;
        caractual = leerchar();
        while (!eof && Pattern.matches(RXLOD, String.valueOf(caractual))) {
            lexema += caractual;
            caractual = leerchar();
        }
        return new Token(Token.TIDENTIFICADOR, lexema, nlinea);
    }

    // -------------------------------------------------------------------------
    // Construye o continua la construccion de un numero (secuencia pura de
    // digitos)
    private Token scanNum(String lexema) throws ExcepIdentNoValido, IOException {
        lexema += caractual;
        caractual = leerchar();
        while (!eof && Pattern.matches(RXDIGITO, String.valueOf(caractual))) {
            lexema += caractual;
            caractual = leerchar();
        }
        if (Pattern.matches(RXLETRA, String.valueOf(caractual))) {
            throw new ExcepIdentNoValido(nlinea);
        }
        return new Token(Token.TNUMERO, lexema, nlinea);
    }

    // -------------------------------------------------------------------------
    // Construye o continua la construccion de un simbolo
    private Token scanSimb(String lexema) throws IOException {
        lexema += caractual;
        caractual = leerchar();
        String lexematmp = lexema + caractual;
        if (!eof && Pattern.matches("<>|<=|>=|:=|\\.\\.", lexematmp)) {
            // lexema += caractual;
            caractual = leerchar();
            return identificar_simbolo(lexematmp);
        } else {
            return identificar_simbolo(lexema);
        }
    }

    // -------------------------------------------------------------------------
    // Saltea todos los espacios, tabs y finales de linea
    // Actualiza el numero de linea
    private void scanEspacios() throws IOException {
        while (!eof && Pattern.matches("[ \t\n\r]", String.valueOf(caractual))) {
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
    // En el caso de llegar a eof, lanza un excepci�n.
    private void scanComentario(int tipo) throws IOException, ExcepEOFCom {
        int nlinc = nlinea;
        boolean salir = false;

        while (!salir && !eof) {
            // Cada vez que se lee un fin de linea, aumenta el numero de linea
            if (Pattern.matches("[\n]", String.valueOf(caractual))) {
                nlinea++;
            }

            // Si lee un simbolo { y se comenzo con }, termino el comentario
            if ((caractual == '}') && (tipo == TCOMENTLLAVE)) {
                caractual = leerchar();
                salir = true;
            }

            // Si lee un simbolo * seguido de ) y se comenzo con (*, termino el
            // comentario
            if ((caractual == '*') && (tipo == TCOMENTPARENT)) {
                caractual = leerchar();
                if (!eof && (caractual == ')')) {
                    caractual = leerchar();
                    salir = true;
                }
            }

            if (!salir && !eof) {
                caractual = leerchar();
            }
        }

        // Llego a fin de linea en comentario
        if (!salir && eof) {
            throw new ExcepEOFCom(nlinc);
        }

    }

    // -------------------------------------------------------------------------
    // Una vez leida una comilla simple, determina si se trata de un caracter
    // valido, o sea un solo simbolo delimitado por dos comillas simples
    private Token scanCar() throws IOException, ExcepCaracMalForm, ExcepSimbNoValido {
        // Ya leyo una comilla simple, espera leer un solo caracter
        // Cualquier caracter entre los codigos ASCII 32 y 126
        caractual = leerchar();
        int codigo = (int) caractual;
        if ((codigo > 31) && (codigo < 127)) {
            // En este caso espera leer otra comilla simple, sino
            // se produce una excepcion
            String caracter = String.valueOf(caractual);
            caractual = leerchar();
            if (caractual == '\'') {
                caractual = leerchar();
                return new Token(Token.TCARACTER, caracter, nlinea);
            } else {
                throw new ExcepCaracMalForm(nlinea);
            }
        } else {
            // Si el caracter no esta dentro del rango aceptado
            // si produce una excepcion de simbolo no valido
            throw new ExcepSimbNoValido(caractual, nlinea);
        }
    }

    // -------------------------------------------------------------------------
    // Luego de leer un caracter letra, determina si el proximo token
    // es una palabra reservada del lenguaje, o si es un identificador
    // (�nicas dos posibilidades)
    // Retorna el token correspondiente en cada caso.
    private Token scanPR(List<String> Palabras, String lexema) throws IOException {
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
                // En el caso de una letra o digito, es un identificador, busco si
                // hay mas letras o digitos
                return scanIdent(lexema);
            }
            if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
                // Si no es letra o digito, lo que ya le� no forma una palabra
                // reservada, asi que es un identificador
                return new Token(Token.TIDENTIFICADOR, lexema, nlinea);
            }
        } else {
            // Encontre una palabra reservada?
            if (Pattern.matches(RXLOD, String.valueOf(caractual))) {
                // Si sigo leyendo letras o digitos, entonces no, es un
                // identificador
                return scanIdent(lexema.toLowerCase());
            }
            if (Pattern.matches(RXNLOD, String.valueOf(caractual))) {
                // En el caso de que no sea letra o digito, ya encontre una palabra
                // reservada
                // La identifico segun su lexema
                return identificar_palabra_reservada(lexema);
            }
        }

        // Caso contrario que siga el nextToken
        // Nunca llegar�a a este punto
        return null;
    }

    // -------------------------------------------------------------------------
    // Devuelve los token extraidos del archivo de entrada
    public Token nextToken() throws ExcepALexico, IOException {
        String lexema = "";

        scanEspacios();

        if (!eof) {
            // Si no esta en el alfabeto de entrada levanta una excepcion
            if (!Pattern.matches(RXALFABETO, String.valueOf(caractual))) {
                throw new ExcepSimbNoValido(caractual, nlinea);
            }

            // Si lee una letra, es una palabra reservada o un identificador, no
            // hay otra opcion
            if (Pattern.matches(RXLETRA, String.valueOf(caractual))) {
                return scanPR(Palabras, lexema);
            }

            // Leo un digito, es un numero
            if (Pattern.matches(RXDIGITO, String.valueOf(caractual))) {
                return scanNum(lexema);
            }

            // Si leo una comilla simple, es el posible inicio de un caracter
            if (caractual == '\'') {
                return scanCar();
            }

            // Leo {, inicio de comentario
            if (caractual == '{') {
                caractual = leerchar();
                scanComentario(TCOMENTLLAVE);
                return nextToken();
            }

            // Leo (, si el proximo caracter es *, entonces es el inicio de un
            // comentario
            if (caractual == '(') {
                lexema += caractual;
                caractual = leerchar();
                if (caractual == '*') {
                    caractual = leerchar();
                    scanComentario(TCOMENTPARENT);
                    return nextToken();
                } else {
                    // En el caso de que el proximo caracter no sea *, entonces
                    // devuelve el token de tipo parentesis abierto
                    return new Token(Token.TPARENTA, lexema, nlinea);
                }
            }

            // Cualquier otro caracter
            if (matchbut(String.valueOf(caractual), ".", "[a-zA-Z0-9{(']")) {
                return scanSimb(lexema);
            }

            // HUBO UN ERROR
            // Nunca deberia llegar aca
            System.out.println(caractual + "," + lexema);
            return new Token(-1, "ERROR", nlinea);
        } else {
            // Llego al final de linea, devuelve el token correspondiente
            return new Token(Token.TEOF, "<EOF>", nlinea);
        }
    }
    // -------------------------------------------------------------------------
}
