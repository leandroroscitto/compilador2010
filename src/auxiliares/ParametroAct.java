package auxiliares;

import tipos.TTipo;

public class ParametroAct {
	public TTipo tipo;
	public boolean esVariable;
	
	public ParametroAct(TTipo te, boolean v){
		tipo = te;
		esVariable = v;
	}
}
