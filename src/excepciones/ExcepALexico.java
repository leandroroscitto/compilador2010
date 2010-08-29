package excepciones;

public class ExcepALexico extends Exception {
	private static final long serialVersionUID = 1L;

	// Numero de linea del inicio del comentario
	public int NLinea;

	public ExcepALexico(String msg, int nlin) {
		super(msg);
		NLinea = nlin;
	}
}
