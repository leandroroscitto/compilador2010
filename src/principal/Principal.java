package principal;

import java.io.IOException;
import java.io.PrintStream;

import alexico.Lexer;
import alexico.Token;
import excepciones.ExcepALexico;

public class Principal {
	// Retorna el nombre de un tipo de token, según su codigo
	public static String getNombreT(int TToken) {
		String nombre = "";

		switch (TToken) {
			case Token.TIDENTIFICADOR :
				nombre = "TIDENTIFICADOR";
				break;
			case Token.TNUMERO :
				nombre = "TNUMERO";
				break;
			case Token.TOPER_AND :
				nombre = "TOPER_AND";
				break;
			case Token.TOPER_OR :
				nombre = "TOPER_OR";
				break;
			case Token.TOPER_NOT :
				nombre = "TOPER_NOT";
				break;
			case Token.TSIMBOLO_IGUAL :
				nombre = "TSIMBOLO_IGUAL";
				break;
			case Token.TSIMBOLO_DISTINTO :
				nombre = "TSIMBOLO_DISTINTO";
				break;
			case Token.TSIMBOLO_MENOR :
				nombre = "TSIMBOLO_MENOR";
				break;
			case Token.TSIMBOLO_MAYOR :
				nombre = "TSIMBOLO_MAYOR";
				break;
			case Token.TSIMBOLO_MENORIGUAL :
				nombre = "TSIMBOLO_MENORIGUAL";
				break;
			case Token.TSIMBOLO_MAYORIGUAL :
				nombre = "TSIMBOLO_MAYORIGUAL";
				break;
			case Token.TOPERMAS :
				nombre = "TOPERMAS";
				break;
			case Token.TOPERMENOS :
				nombre = "TOPERMENOS";
				break;
			case Token.TOPERMULT :
				nombre = "TOPERMULT";
				break;
			case Token.TPARENTA :
				nombre = "TPARENTA";
				break;
			case Token.TPARENTC :
				nombre = "TPARENTC";
				break;
			case Token.TCORA :
				nombre = "TCORA";
				break;
			case Token.TCORC :
				nombre = "TCORC";
				break;
			case Token.TASIGN :
				nombre = "TASIGN";
				break;
			case Token.TPUNTO :
				nombre = "TPUNTO";
				break;
			case Token.TCOMA :
				nombre = "TCOMA";
				break;
			case Token.TPUNTO_Y_COMA :
				nombre = "TPUNTO_Y_COMA";
				break;
			case Token.TDOSPUNTOS :
				nombre = "TDOSPUNTOS";
				break;
			// case Token.TCOMMILLA_SIMPLE :
			// nombre = "TCOMMILLA_SIMPLE";
			// break;
			case Token.TDOBLEPUNTO :
				nombre = "TDOBLEPUNTO";
				break;
			case Token.TPALRES_IF :
				nombre = "TPALRES_IF";
				break;
			case Token.TPALRES_THEN :
				nombre = "TPALRES_THEN";
				break;
			case Token.TPALRES_ELSE :
				nombre = "TPALRES_ELSE";
				break;
			case Token.TPALRES_OF :
				nombre = "TPALRES_OF";
				break;
			case Token.TPALRES_WHILE :
				nombre = "TPALRES_WHILE";
				break;
			case Token.TPALRES_DO :
				nombre = "TPALRES_DO";
				break;
			case Token.TPALRES_BEGIN :
				nombre = "TPALRES_BEGIN";
				break;
			case Token.TPALRES_END :
				nombre = "TPALRES_END";
				break;
			case Token.TPALRES_CONST :
				nombre = "TPALRES_CONST";
				break;
			case Token.TPALRES_VAR :
				nombre = "TPALRES_VAR";
				break;
			case Token.TPALRES_TYPE :
				nombre = "TPALRES_TYPE";
				break;
			case Token.TPALRES_ARRAY :
				nombre = "TPALRES_ARRAY";
				break;
			case Token.TPALRES_FUNCTION :
				nombre = "TPALRES_FUNCTION";
				break;
			case Token.TPALRES_PROGRAM :
				nombre = "TPALRES_PROGRAM";
				break;
			case Token.TPALRES_PROCEDURE :
				nombre = "TPALRES_PROCEDURE";
				break;
			case Token.TOPERDIV :
				nombre = "TOPERDIV";
				break;
			case Token.TCARACTER :
				nombre = "TCARACTER";
				break;
			default :
				nombre = String.valueOf(TToken);
		}

		return nombre;
	}

	public static void main(String[] args) throws ExcepALexico {
		if (args.length > 0) {
			PrintStream Salida;
			try {
				if (args.length == 1) {
					// Hay un solo argumento, lo considero el archivo de entrada
					// La salida es por pantalla
					Salida = System.out;
				} else {
					// Hay al menos 2 argumentos, el primero es el archivo de entrada
					// y el segundo el de salida
					Salida = new PrintStream(args[1]);
				}
				Salida.println("Archivo de entrada:'" + args[0] + "':");
				Salida.println("====================================");
				Salida.println();

				// Considera el primer paramatro de entrada
				// como el nombre del archivo de entrada
				Lexer ALexico = new Lexer(args[0]);

				// Lee el primer token
				Token tok = ALexico.nextToken();

				// Y mientas no sea fin de archivo
				// sigue leyendo y mostrando por pantalla
				while (tok.tipo != Token.TEOF) {
					Salida.println("Tipo del token:" + getNombreT(tok.tipo) + ", lexema:'" + tok.lexema
							+ "', numero de linea:" + tok.nlinea + ".");
					Salida.println("________________________________________________________________________________");
					// Lee el proximo token
					tok = ALexico.nextToken();
				}

				Salida.println();
				Salida.println("El proceso se completo con exito.");
			} catch (IOException e) {
				System.out.println("IOException capturada, no se pudo leer del archivo.");
			}
		} else {
			// Faltó el parametro de entrada
			System.out.println("De como parametro al menos un nombre para el archivo de entrada.");
		}
	}
}
