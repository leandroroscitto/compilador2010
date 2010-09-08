package excepciones;

import alexico.Token;

public class ExcepASintatico extends Exception {
	private static final long serialVersionUID = 1L;

	// Numero de linea del inicio de la excepcion
	public int NLinea;
	
	// Ultimo token leido
	public Token UltToken;
	
	public ExcepASintatico(String msg, int nlin, Token tok){
		super(msg+" Token("+tok+")");
		NLinea = nlin;
		UltToken = tok;
	}
	
}