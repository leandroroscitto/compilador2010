package tablasimb;

import tipos.TTipo;

public class Parametro {
	public String lexema;
	public TTipo tipo;
	public boolean esPorValor;
	
	public Parametro(String l, TTipo te, boolean v){
		lexema = l;
		tipo = te;
		esPorValor = v;
	}
}
