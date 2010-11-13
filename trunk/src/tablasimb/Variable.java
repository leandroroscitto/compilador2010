package tablasimb;

import tipos.TTipo;

public class Variable extends Tipado{
	public int nivelL;
	public int desp;
	public boolean esPorvalor;
	
	public Variable(String l, TTipo te, int n, int d, boolean espv){
		super(l,Simbolo.VARIABLE,te);
		nivelL = n;
		desp = d;
		esPorvalor = espv;
	}
}
