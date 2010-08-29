package excepciones;

public class ExcepEOFCom extends ExcepALexico {
	private static final long serialVersionUID = 1L;

	public ExcepEOFCom(int nlinc) {
		super("Fin de linea en comentario. (Linea de inicio de comentario:" + nlinc + ")", nlinc);
	}
}
