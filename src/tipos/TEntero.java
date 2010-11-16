package tipos;

public class TEntero extends TCharInt {
	
	public TEntero(){
		super("Entero",TTipo.TPENTERO);
	}
	
	public boolean comparar(TTipo t){
		return (t.clase == TPENTERO);
	}
}
