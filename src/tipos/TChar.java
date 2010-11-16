package tipos;

public class TChar extends TCharInt {
	
	public TChar(){
		super("Char",TTipo.TPCARACTER);
	}
	
	public boolean comparar(TTipo t){
		return (t.clase == TPCARACTER);
	}
}
