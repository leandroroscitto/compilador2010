package tablasimb;

import tipos.TTipo;

public class Tipado extends Simbolo{
	public TTipo tipo_de_estructura;
	
	public Tipado(String l, int ts, TTipo te){
		super(l,ts);
		tipo_de_estructura = te;
	}
}
