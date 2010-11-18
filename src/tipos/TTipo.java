package tipos;

public class TTipo {
	public static int TPCARACTER = 0;
	public static int TPENTERO = 1;
	public static int TPBOOLEAN = 2;
	public static int TPSUBRANGO = 3;
	public static int TPARREGLO = 4;
	public static int TPUNIDAD = 5;
	public static int TPVOID = 6;
	public static int TPCHARINT = 7;
	
	public String nombre;
	public int clase;
	
	public int tammemoria;
	
	public TTipo(String n, int c, int tam){
		nombre = n;
		clase = c;
		tammemoria = tam;
	}
	
	public boolean esTipoSimple(){
		return false;
	}
	
	public boolean comparar(TTipo tipo){
		return (this.clase == tipo.clase);
	}
	
	public boolean esSubrango(){
		return (this.clase == TPSUBRANGO);
	}
}
