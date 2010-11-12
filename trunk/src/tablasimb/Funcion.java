package tablasimb;

import tipos.TTipo;

public class Funcion extends Procedimiento{
	public TTipo salida;
	public int desp;

	public Funcion(String l, int cantpf, TTipo[] tipopf, boolean[] pasajepf,
			int tapf, int n, String eti, TTipo s, int d) {
		super(l, cantpf, tipopf, pasajepf, tapf, n, eti);
		salida = s;
		desp = d;
		tipo_de_simbolo = Simbolo.FUNCION;
	}

}
