package principal;

import java.io.IOException;
import java.io.PrintStream;

import asemantico.Parser;
import excepciones.ExcepALexico;
import excepciones.ExcepASemantico;
import excepciones.ExcepASintatico;

public class Principal3 {

	public static void main(String[] args) throws ExcepALexico, ExcepASintatico {
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

				// Crea el analizador sintactico, con el nombre
				// del archivo de entrada como parametro
				Parser ASintactico = new Parser(args[0]);

				// Comienza con el no terminal programa, seguido
				// por el final de archivo
				ASintactico.programa();
				ASintactico.eof();

				// Si llega aqui sin producer una excepcion, el codigo
				// de entrada es sintacticamente correcto

				Salida.println("El proceso se completo con exito.");
			} catch (IOException e) {
				System.out.println("IOException capturada, no se pudo leer del archivo.");
			} catch (ExcepALexico e) {
				System.out.println("Error lexico: " + e.getMessage());
			} catch (ExcepASintatico e) {
				System.out.println("Error sintactico: " + e.getMessage());
			} catch (ExcepASemantico e) {
				System.out.println("Error semantico: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("Error general: " + e.getMessage());
			}
		} else {
			// Falto el parametro de entrada
			System.out.println("De como parametro al menos un nombre para el archivo de entrada.");
		}
		System.out.println();
	}
}
