package auxiliares;

import java.util.ArrayList;

public class ListaParametrosAct extends ArrayList<ParametroAct>{
	private static final long serialVersionUID = 1L;
	
	public ListaParametrosAct(){
		super();
	}
	
	public void agregarParametro(ParametroAct p){
		this.add(p);
	}
}
