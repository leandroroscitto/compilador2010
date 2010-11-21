package auxiliares;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import asemantico.Parser;

public class Prueba {

	public static void main(String[] args) throws Exception {
		// Obtiene los dos archivos de entradas con las listas correspondientes
		String ListaPE = args[0];
		String ListaPF = args[1];

		FileInputStream fis;
		InputStreamReader isr;
		BufferedReader reader;

		String archivo;

		System.out.println("Pruebas exitosas:");

		fis = new FileInputStream(ListaPE);
		isr = new InputStreamReader(fis);
		reader = new BufferedReader(isr);

		while (reader.ready()) {
			archivo = reader.readLine();
			try {
				Parser p = new Parser(archivo);
				p.programa();
				p.eof();
			} catch (Exception e) {
				System.out.println("No deberia haber fallado, fue en " + archivo + ", y se produjo:" + e.getMessage());
			}
		}
		System.out.println("Todo OK.");
		System.out.println();

		System.out.println("Pruebas fallidas:");

		fis = new FileInputStream(ListaPF);
		isr = new InputStreamReader(fis);
		reader = new BufferedReader(isr);

		while (reader.ready()) {
			archivo = reader.readLine();
			try {
				Parser p = new Parser(archivo);
				p.programa();
				p.eof();
			} catch (Exception e) {
				System.out.println("Fallo " + archivo + ", lo que produjo es: " + e.getMessage());
			}
		}
	}
}
