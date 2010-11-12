package tipos;

public class TVoid extends TTipo {
	
	public TVoid(){
		super("Void",TTipo.TPVOID,0);
	}
	
	public boolean esTipoSimple(){
		return false;
	}
}
