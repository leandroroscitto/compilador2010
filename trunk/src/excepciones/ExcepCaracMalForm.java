package excepciones;

public class ExcepCaracMalForm extends ExcepALexico{
	private static final long serialVersionUID = 1L;

	public ExcepCaracMalForm(int nlin) {
		super("Caracter mal formado en la linea: "+nlin+".", nlin);
	}
}
