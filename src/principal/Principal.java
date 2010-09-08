package principal;

import java.io.IOException;
import java.io.PrintStream;

import alexico.Lexer;
import alexico.Token;
import excepciones.ExcepALexico;

public class Principal {

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
                    if (tok.tipo == -1) {
                        throw new ExcepALexico("Error no determinado", tok.nlinea);
                    }
                    Salida.println("Tipo del token:" + Token.getNombreT(tok.tipo) + ", lexema:'" + tok.lexema
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
            // Falt� el parametro de entrada
            System.out.println("De como parametro al menos un nombre para el archivo de entrada.");
        }
    }
}
