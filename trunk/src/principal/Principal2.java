package principal;

import java.io.IOException;
import java.io.PrintStream;

import asintatico.Parser;
import excepciones.ExcepALexico;
import excepciones.ExcepASintatico;

public class Principal2 {

    public static void main(String[] args) throws ExcepALexico, ExcepASintatico {
        args = new String[1];
        args[0] = "Ejemplo1.pas";
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

                // Crea el analizador sintactico, con el nombre
                // del archivo de entrada como parametro
                Parser ASintactico = new Parser(args[0]);

                // Comienza con el no terminal programa, seguido
                // por el final de archivo
                ASintactico.programa();
                ASintactico.eof();

                // Si llega aqui sin producer una excepcion, el codigo
                // de entrada es sintacticamente correcto

                Salida.println();
                Salida.println("El proceso se completo con exito.");
            } catch (IOException e) {
                System.out.println("IOException capturada, no se pudo leer del archivo.");
            }
        } else {
            // Falto el parametro de entrada
            System.out.println("De como parametro al menos un nombre para el archivo de entrada.");
        }
    }
}
