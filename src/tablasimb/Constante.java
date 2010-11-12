package tablasimb;

import tipos.TTipo;

public class Constante extends Tipado{
	public int valor;
	
	public Constante(String l, TTipo te, int v){
		super(l,Simbolo.CONSTANTE,te);
		valor = v;
	}
}
