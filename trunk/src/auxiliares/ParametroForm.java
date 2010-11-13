package auxiliares;

import tipos.TTipo;

public class ParametroForm {
	public String lexema;
	public TTipo tipo;
	public boolean esPorValor;
	
	public ParametroForm(String l, TTipo te, boolean v){
		lexema = l;
		tipo = te;
		esPorValor = v;
	}
}
