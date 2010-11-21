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
			Parser ASintactico;
			try {
				Salida = System.out;
				if (args.length == 1) {
					// Crea el analizador sintactico, con el nombre
					// del archivo de entrada como parametro
					ASintactico = new Parser(args[0]);
				} else {
					// Crea el analizador sintactico, con el nombre
					// del archivo de entrada y salida como parametro
					ASintactico = new Parser(args[0],args[1]);
				}
				Salida.println("Archivo de entrada:'" + args[0] + "':");

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
