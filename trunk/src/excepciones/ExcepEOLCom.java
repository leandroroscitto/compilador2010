package excepciones;

public class ExcepEOLCom extends ExcepALexico {
	private static final long serialVersionUID = 1L;

	public ExcepEOLCom(int nlinc) {
		super("Fin de l�nea en comentario. (Linea de inicio de comentario:" + nlinc + ")", nlinc);
	}
}
