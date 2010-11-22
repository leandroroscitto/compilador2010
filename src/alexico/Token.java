package alexico;

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
    public static final int TSIMBOLO_IGUAL = 18;
    public static final int TSIMBOLO_DISTINTO = 19;
    public static final int TSIMBOLO_MENOR = 20;
    public static final int TSIMBOLO_MAYOR = 21;
    public static final int TSIMBOLO_MENORIGUAL = 22;
    public static final int TSIMBOLO_MAYORIGUAL = 23;
    public static final int TOPERMAS = 24;
    public static final int TOPERMENOS = 25;
    public static final int TOPERMULT = 26;
    public static final int TOPERDIV = 27;
    public static final int TOPER_AND = 28;
    public static final int TOPER_OR = 29;
    public static final int TOPER_NOT = 30;
    public static final int TPARENTA = 31;
    public static final int TPARENTC = 32;
    public static final int TCORA = 33;
    public static final int TCORC = 34;
    public static final int TASIGN = 35;
    public static final int TPUNTO = 36;
    public static final int TCOMA = 37;
    public static final int TPUNTO_Y_COMA = 38;
    public static final int TDOSPUNTOS = 39;
    // public static final int TCOMMILLA_SIMPLE = 40;
    public static final int TDOBLEPUNTO = 41;
    public static final int TCARACTER = 42;

    // Retorna el nombre de un tipo de token, segun su codigo
    public static String getNombreT(int TToken) {
        String nombre = "";

        switch (TToken) {
            case Token.TIDENTIFICADOR:
                nombre = "TIDENTIFICADOR";
                break;
            case Token.TNUMERO:
                nombre = "TNUMERO";
                break;
            case Token.TOPER_AND:
                nombre = "TOPER_AND";
                break;
            case Token.TOPER_OR:
                nombre = "TOPER_OR";
                break;
            case Token.TOPER_NOT:
                nombre = "TOPER_NOT";
                break;
            case Token.TSIMBOLO_IGUAL:
                nombre = "TSIMBOLO_IGUAL";
                break;
            case Token.TSIMBOLO_DISTINTO:
                nombre = "TSIMBOLO_DISTINTO";
                break;
            case Token.TSIMBOLO_MENOR:
                nombre = "TSIMBOLO_MENOR";
                break;
            case Token.TSIMBOLO_MAYOR:
                nombre = "TSIMBOLO_MAYOR";
                break;
            case Token.TSIMBOLO_MENORIGUAL:
                nombre = "TSIMBOLO_MENORIGUAL";
                break;
            case Token.TSIMBOLO_MAYORIGUAL:
                nombre = "TSIMBOLO_MAYORIGUAL";
                break;
            case Token.TOPERMAS:
                nombre = "TOPERMAS";
                break;
            case Token.TOPERMENOS:
                nombre = "TOPERMENOS";
                break;
            case Token.TOPERMULT:
                nombre = "TOPERMULT";
                break;
            case Token.TPARENTA:
                nombre = "TPARENTA";
                break;
            case Token.TPARENTC:
                nombre = "TPARENTC";
                break;
            case Token.TCORA:
                nombre = "TCORA";
                break;
            case Token.TCORC:
                nombre = "TCORC";
                break;
            case Token.TASIGN:
                nombre = "TASIGN";
                break;
            case Token.TPUNTO:
                nombre = "TPUNTO";
                break;
            case Token.TCOMA:
                nombre = "TCOMA";
                break;
            case Token.TPUNTO_Y_COMA:
                nombre = "TPUNTO_Y_COMA";
                break;
            case Token.TDOSPUNTOS:
                nombre = "TDOSPUNTOS";
                break;
            // case Token.TCOMMILLA_SIMPLE :
            // nombre = "TCOMMILLA_SIMPLE";
            // break;
            case Token.TDOBLEPUNTO:
                nombre = "TDOBLEPUNTO";
                break;
            case Token.TPALRES_IF:
                nombre = "TPALRES_IF";
                break;
            case Token.TPALRES_THEN:
                nombre = "TPALRES_THEN";
                break;
            case Token.TPALRES_ELSE:
                nombre = "TPALRES_ELSE";
                break;
            case Token.TPALRES_OF:
                nombre = "TPALRES_OF";
                break;
            case Token.TPALRES_WHILE:
                nombre = "TPALRES_WHILE";
                break;
            case Token.TPALRES_DO:
                nombre = "TPALRES_DO";
                break;
            case Token.TPALRES_BEGIN:
                nombre = "TPALRES_BEGIN";
                break;
            case Token.TPALRES_END:
                nombre = "TPALRES_END";
                break;
            case Token.TPALRES_CONST:
                nombre = "TPALRES_CONST";
                break;
            case Token.TPALRES_VAR:
                nombre = "TPALRES_VAR";
                break;
            case Token.TPALRES_TYPE:
                nombre = "TPALRES_TYPE";
                break;
            case Token.TPALRES_ARRAY:
                nombre = "TPALRES_ARRAY";
                break;
            case Token.TPALRES_FUNCTION:
                nombre = "TPALRES_FUNCTION";
                break;
            case Token.TPALRES_PROGRAM:
                nombre = "TPALRES_PROGRAM";
                break;
            case Token.TPALRES_PROCEDURE:
                nombre = "TPALRES_PROCEDURE";
                break;
            case Token.TOPERDIV:
                nombre = "TOPERDIV";
                break;
            case Token.TCARACTER:
                nombre = "TCARACTER";
                break;
            default:
                nombre = String.valueOf(TToken);
        }

        return nombre;
    }
    public int tipo;
    public String lexema;
    public int nlinea;

    public Token(int t, String l, int n) {
        tipo = t;
        lexema = l.toLowerCase();
        nlinea = n;
    }

    @Override
    public String toString() {
        return "Tipo: " + getNombreT(tipo) + ", lexema: " + lexema
                + ", linea: " + nlinea + ".";
    }
}
