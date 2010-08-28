package prototipos;

public class Token {
	public static final int TPR = -2;
	public static final int TOTRO = -1;
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
	public int nlinea;

	public Token(int t, String l, int n) {
		tipo = t;
		lexema = l;
		nlinea = n;
	}
}