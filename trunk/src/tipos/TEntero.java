package tipos;

public class TEntero extends TCharIntSub {
	
	public TEntero(){
		super("Entero",TTipo.TPENTERO);
	}
	
	public boolean comparar(TTipo t){
		return (t.clase == TPENTERO || t.clase == TPCHARINT || t.clase == TPSUBRANGO);
	}
}
