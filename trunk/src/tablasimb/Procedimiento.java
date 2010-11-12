package tablasimb;

import tipos.TTipo;

public class Procedimiento extends Unidad{
	public int cpf;
	public TTipo[] tpf;
	public boolean[] ppf;
	public int tampf;
	public int nivelL;
	public String etiqueta;
	
	public Procedimiento(String l, int cantpf, TTipo[] tipopf, boolean[] pasajepf, int tapf, int n, String eti){
		super(l,Simbolo.PROCEDIMIENTO);
		cpf = cantpf;
		tpf = tipopf;
		ppf = pasajepf;
		tampf = tapf;
		nivelL = n;
		etiqueta = eti;
	}
}
