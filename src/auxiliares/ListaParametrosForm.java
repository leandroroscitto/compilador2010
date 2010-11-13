package auxiliares;

import java.util.ArrayList;

public class ListaParametrosForm extends ArrayList<ParametroForm>{
	private static final long serialVersionUID = 1L;
	
	public ListaParametrosForm(){
		super();
	}
	
	public void agregarParametro(ParametroForm p){
		this.add(p);
	}
}
