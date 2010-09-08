package asintatico;

import java.io.IOException;

import alexico.Lexer;
import alexico.Token;
import excepciones.ExcepALexico;
import excepciones.ExcepASintatico;

public class Parser {
	private Lexer ALexico;
	private Token TActual;

	public Parser(String fileurl) throws ExcepALexico, IOException {
		ALexico = new Lexer(fileurl);
		TActual = ALexico.nextToken();
	}

	private void leerToken() throws ExcepALexico, IOException {
		TActual = ALexico.nextToken();
	}

	public boolean signo() throws ExcepALexico, IOException, ExcepASintatico {
		if ((TActual.tipo == Token.TOPERMAS)
				|| (TActual.tipo == Token.TOPERMENOS)) {
			leerToken();
			return true;
		}

		throw new ExcepASintatico("Se esperaba un signo + o -.", TActual.nlinea,
				TActual);
	}

	public boolean constante() throws ExcepALexico, IOException, ExcepASintatico {
		if ((TActual.tipo == Token.TNUMERO)
				|| (TActual.tipo == Token.TIDENTIFICADOR)
				|| (TActual.tipo == Token.TCARACTER)) {
			leerToken();
			return true;
		}
		if ((TActual.tipo == Token.TOPERMAS)
				|| (TActual.tipo == Token.TOPERMENOS)) {
			leerToken();
			constanteP();
			return true;
		}

		throw new ExcepASintatico("Se esperaba una constante.", TActual.nlinea,
				TActual);
	}

	public boolean constanteP() throws ExcepALexico, IOException,
			ExcepASintatico {
		if ((TActual.tipo == Token.TNUMERO)
				|| (TActual.tipo == Token.TIDENTIFICADOR)) {
			leerToken();
			return true;
		} else {
			throw new ExcepASintatico(
					"Se esperaba un numero o identificador para la constante.",
					TActual.nlinea, TActual);
		}
	}

	public boolean definicion_de_constante() throws ExcepALexico, IOException,
			ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
				leerToken();
				constante();
				return true;
			} else {
				throw new ExcepASintatico(
						"Error en la definicion de la constante, se esperaba un simbolo '='.",
						TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico(
					"Error en la definicion de la constante, se esperaba un identificador.",
					TActual.nlinea, TActual);
		}
	}

	public boolean programa() throws ExcepALexico, IOException, ExcepASintatico {
		definicion_de_constante();
		// TODO: Terminar
		return true;
	}

	public boolean eof() throws ExcepASintatico {
		if (TActual.tipo == Token.TEOF) {
			return true;
		}

		System.out.println(TActual);
		throw new ExcepASintatico(
				"Se esperaba el final del archivo, pero se encontro algo mas...",
				TActual.nlinea, TActual);
	}
}
