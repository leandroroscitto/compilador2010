package excepciones;

public class ExcepIdentNoValid extends ExcepALexico {
	private static final long serialVersionUID = 1L;

	public ExcepIdentNoValid(int nlin) {
		super("Se encontro un identificador mal formado en la linea " + nlin + ".", nlin);
	}

}
