package tablasimb;

import tipos.TTipo;

public class Variable extends Tipado{
	public int nivelL;
	public int desp;
	
	public Variable(String l, TTipo te, int n, int d){
		super(l,Simbolo.VARIABLE,te);
		nivelL = n;
		desp = d;
	}
}
