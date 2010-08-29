package excepciones;

public class ExcepComMalForm extends ExcepALexico {
	private static final long serialVersionUID = 1L;

	public ExcepComMalForm(int nlin) {
		super("Comentario mal formado en la linea " + nlin + ".", nlin);
	}

}
