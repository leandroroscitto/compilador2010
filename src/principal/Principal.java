package principal;

import java.io.IOException;

import prototipos.Lexer;
import prototipos.Token;
import excepciones.ExcepALexico;


public class Principal {
	public static String getNombreT(int TToken){
		String nombre = "";
		
		switch (TToken){
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
			case Token.TCOMMILLA_SIMPLE:
				nombre = "TCOMMILLA_SIMPLE";
				break;
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
			default:
				nombre = String.valueOf(TToken);
		}
		
		return nombre;
	}
	
	public static void main(String[] args) throws ExcepALexico, IOException {
		if (args.length > 0) {
			System.out.println("Archivo de entrada:'"+args[0]+"':");
			System.out.println("====================================");
			System.out.println();
			
			// Considera el primer paramatro de entrada
			// como el nombre del archivo de entrada
			// y ignora el resto
			Lexer ALexico = new Lexer(args[0]);

			// Lee el primer token
			Token tok = ALexico.nextToken();
			
			// Y mientas no sea fin de archivo
			// sigue leyendo y mostrando por pantalla
			while (tok.tipo != Token.TEOF) {
				
				System.out.println("Tipo del token:" + getNombreT(tok.tipo) + ", lexema:'" + tok.lexema + "', número de linea:"
						+ tok.nlinea + ".");
				System.out.println("________________________________________________________________________________");
				// Lee el proximo token
				tok = ALexico.nextToken();
			}
			
			System.out.println();
			System.out.println("El proceso se completó con éxito.");
		} else{
			// Faltó el parametro de entrada
			System.out.println("De como parámetro al menos un nombre para el archivo de entrada.");
		}
	}
}
