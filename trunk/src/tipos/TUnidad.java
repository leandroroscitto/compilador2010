package tipos;

public class TUnidad extends TTipo {
	
	public TUnidad(){
		super("Unidad",TTipo.TPUNIDAD,0);
	}
	
	public boolean esTipoSimple(){
		return false;
	}
}
