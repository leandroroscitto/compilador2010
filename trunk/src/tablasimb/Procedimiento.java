package tablasimb;

import auxiliares.ListaParametrosAct;
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
	
	public boolean chequearParametros(ListaParametrosAct list){
		if (list.size() == cpf){
  			for (int i=0; i<cpf; i++){
  				// Si el parametro es por referencia
  				// Debe ser una variable
				if (!(ppf[i]) && !(list.get(i).esVariable)){
					return false;
				}
				
				// Chequea el tipo del parametro
				if (!(list.get(i).tipo.comparar(tpf[i]))){
					return false;
				}
			}
  			// Todos los parametros coinciden en tipo y pasaje.
  			return true;
		}else{
			// La cantidad de parametros no corresponde con el de la unidad.
			return false;
		}
	}
}
