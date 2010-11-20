package tipos;

public class TChar extends TCharIntSub {
	
	public TChar(){
		super("Char",TTipo.TPCARACTER);
	}
	
	public boolean comparar(TTipo t){
		return (t.clase == TPCARACTER || t.clase == TPCHARINT);
	}
}
