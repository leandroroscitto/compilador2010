package principal;

import java.io.FileNotFoundException;

import prototipos.Lexer;
import prototipos.Token;
import excepciones.ExcepALexico;

public class Principal {
	public static void main(String[] args) throws FileNotFoundException, ExcepALexico {
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
				
				System.out.println("Tipo del token:" + tok.tipo + ", lexema:'" + tok.lexema + "', número de linea:"
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
