package asemantico;

import java.io.IOException;
import java.util.ArrayList;

import sintetizados.TStamvar;
import sintetizados.TStiva;
import sintetizados.TSlistpform;
import sintetizados.TSintetizado;
import sintetizados.TSlexema;
import sintetizados.TStamreser;
import sintetizados.TStipo;
import sintetizados.TSlistlexema;
import sintetizados.TSesMenos;
import sintetizados.TStiSi;
import sintetizados.TStifvaf;
import tablasimb.Constante;
import tablasimb.Funcion;
import tablasimb.Procedimiento;
import tablasimb.Simbolo;
import tablasimb.TablaSimbolos;
import tablasimb.Tipo;
import tablasimb.Variable;
import tipos.TArreglo;
import tipos.TBoolean;
import tipos.TChar;
import tipos.TEntero;
import tipos.TSimple;
import tipos.TSubrango;
import tipos.TTipo;
import tipos.TVoid;

import alexico.Lexer;
import alexico.Token;
import auxiliares.ListaParametrosForm;
import auxiliares.MEPa;
import auxiliares.ParametroForm;
import excepciones.ExcepALexico;
import excepciones.ExcepASemantico;
import excepciones.ExcepASintatico;

public class Parser {

	private Lexer ALexico;
	private Token TActual;

	private TablaSimbolos TablaSimb;
	private MEPa mepa;

	public Parser(String fileurl) throws ExcepALexico, IOException {
		ALexico = new Lexer(fileurl);
		TActual = ALexico.nextToken();
		mepa = new MEPa(fileurl + ".out");
	}

	public Parser(String fileurl, String fileout) throws ExcepALexico, IOException {
		ALexico = new Lexer(fileurl);
		TActual = ALexico.nextToken();
		mepa = new MEPa(fileout + ".out");
	}

	private void leerToken() throws ExcepALexico, IOException {
		TActual = ALexico.nextToken();
	}

	/*--------------------------------------------------------------*/
	// ***********GRAMATICA***********
	/*--------------------------------------------------------------*/
	// <signo> : TOPERMAS | TOPERMENOS
	public TSesMenos signo() throws ExcepALexico, IOException, ExcepASintatico {
		if ((TActual.tipo == Token.TOPERMAS) || (TActual.tipo == Token.TOPERMENOS)) {
			String oper = TActual.lexema;
			leerToken();
			// --
			TSesMenos retorno = new TSesMenos();
			retorno.esMenos = oper.equals("-");
			// --
			return retorno;
		}
		throw new ExcepASintatico("Se esperaba un signo '+' o '-'.", TActual.nlinea, TActual);
	}

	/*--------------------------------------------------------------*/
	// ***********CONSTANTES***********
	/*--------------------------------------------------------------*/
	// <constante> :
	// TNUMERO |
	// TCARACTER |
	// TIDENTIFICADOR |
	// <signo> <constante'>
	public TStiva constante() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// Si comienza con un numero, caracter o identificador
		TStiva retorno = new TStiva();
		if (TActual.tipo == Token.TNUMERO) {
			int valor = Integer.parseInt(TActual.lexema);
			// --
			// Controla que no se pasa del maximo entero permitido.
			if (valor > MEPa.maxint) {
				throw new ExcepASemantico("La contastante entera supera el maximo predefinido.", TActual.nlinea);
			}
			// --
			leerToken();
			// --
			retorno.tipo = new TEntero();
			retorno.valor = valor;
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TCARACTER) {
			int valor = Integer.parseInt(mepa.CharToMepa(TActual.lexema));
			leerToken();
			// --
			retorno.tipo = new TChar();
			retorno.valor = valor;
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.CONSTANTE }, false)) {
				Constante id = (Constante) TablaSimb.obtener_de_tabla(identificador, new int[] { Simbolo.CONSTANTE });
				if (id.tipo_de_estructura.clase == TTipo.TPENTERO) {
					retorno.tipo = id.tipo_de_estructura;
					retorno.valor = id.valor;
				} else {
					throw new ExcepASemantico("Se esperaba un entero en el subrango.", TActual.nlinea);
				}
			} else {
				throw new ExcepASemantico("Costante no declarada.", TActual.nlinea);
			}
			// --
			return retorno;
		}
		// Si comienza con un signo
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			TSesMenos retSigno = signo();
			TStiva retConsP = constanteP();
			// --
			if (retConsP.tipo.clase == TTipo.TPENTERO) {
				retorno.tipo = retConsP.tipo;
				if (retSigno.esMenos) {
					retorno.valor = (-1) * retConsP.valor;
				} else {
					retorno.valor = retConsP.valor;
				}
			} else {
				throw new ExcepASemantico("Se esperaba un entero luego de un signo.", TActual.nlinea);
			}
			// --
			return retorno;
		}
		// Si no es nada de lo de arriba
		throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
	}

	// <constante'>:
	// TNUMERO |
	// TIDENTIFICADOR

	public TStiva constanteP() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStiva retorno = new TStiva();
		if (TActual.tipo == Token.TNUMERO) {
			int valor = Integer.parseInt(TActual.lexema);
			// --
			// Controla que no se pasa del maximo entero permitido.
			if (valor > MEPa.maxint) {
				throw new ExcepASemantico("La contastante entera supera el maximo predefinido.", TActual.nlinea);
			}
			// --
			leerToken();
			// --
			retorno.tipo = new TEntero();
			retorno.valor = valor;
			// --
			return retorno;
		} else if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.CONSTANTE }, false)) {
				Constante id = (Constante) TablaSimb.obtener_de_tabla(identificador, new int[] { Simbolo.CONSTANTE });
				if (id.tipo_de_estructura.clase == TTipo.TPENTERO) {
					retorno.tipo = id.tipo_de_estructura;
					retorno.valor = id.valor;
				} else {
					throw new ExcepASemantico("Se esperaba un entero en el subrango.", TActual.nlinea);
				}
			} else {
				throw new ExcepASemantico("Costante no declarada.", TActual.nlinea);
			}
			// --
			return retorno;
		} else {
			throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
		}
	}

	// <constante de subrango> :
	// TNUMERO |
	// TIDENTIFICADOR |
	// <signo> <constante'>
	public TStiva constante_de_subrango() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStiva retorno = new TStiva();
		// Si comienza con un numero, caracter o identificador
		if (TActual.tipo == Token.TNUMERO) {
			int valor = Integer.parseInt(TActual.lexema);
			// --
			// Controla que no se pasa del maximo entero permitido.
			if (valor > MEPa.maxint) {
				throw new ExcepASemantico("La contastante entera supera el maximo predefinido.", TActual.nlinea);
			}
			// --
			leerToken();
			// --
			retorno.tipo = new TEntero();
			retorno.valor = valor;
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.CONSTANTE }, false)) {
				Constante id = (Constante) TablaSimb.obtener_de_tabla(identificador, new int[] { Simbolo.CONSTANTE });
				if (id.tipo_de_estructura.clase == TTipo.TPENTERO) {
					retorno.tipo = id.tipo_de_estructura;
					retorno.valor = id.valor;
				} else {
					throw new ExcepASemantico("Se esperaba un entero en el subrango.", TActual.nlinea);
				}
			} else {
				throw new ExcepASemantico("Costante no declarada.", TActual.nlinea);
			}
			// --
			return retorno;
		}
		// Si comienza con un signo
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			TSesMenos retSigno = signo();
			TStiva retConsP = constanteP();
			// --
			if (retConsP.tipo.clase == TTipo.TPENTERO) {
				retorno.tipo = retConsP.tipo;
				if (retSigno.esMenos) {
					retorno.valor = (-1) * retConsP.valor;
				} else {
					retorno.valor = retConsP.valor;
				}
			} else {
				throw new ExcepASemantico("Se esperaba un entero luego de un signo.", TActual.nlinea);
			}
			// --
			return retorno;
		}
		// Si no es nada de lo de arriba
		throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
	}

	// <defincion de constante> :
	// TIDENTIFICADOR TSIMBOLO_IGUAL <constante>
	public TSintetizado definicion_de_constante() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (mepa.MestaEnFuncion) {
				if (identificador.equals(mepa.MLexemaUnidad)) {
					throw new ExcepASemantico("Se esta redefiniendo la funcion.", TActual.nlinea);
				}
			}
			if (TablaSimb.existe_en_tabla(identificador, TablaSimb.TodosSimb, true)) {
				throw new ExcepASemantico("Identificador ya utilizado.", TActual.nlinea);
			}
			// --
			if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
				leerToken();
				TStiva retConstante = constante();
				// --
				TablaSimb.guardar_constante_en_tabla(identificador, retConstante.tipo, retConstante.valor);
				// --
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '='.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
		}
	}

	/*--------------------------------------------------------------*/
	// ***********TIPOS***********
	/*--------------------------------------------------------------*/
	// <tipo> :
	// <tipo simple> |
	// <tipo arreglo>
	public TStiSi tipo() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStiSi retorno = new TStiSi();
		if (TActual.tipo == Token.TIDENTIFICADOR || TActual.tipo == Token.TNUMERO || TActual.tipo == Token.TCARACTER || TActual.tipo == Token.TOPERMAS
				|| TActual.tipo == Token.TOPERMENOS) {
			TStipo retTipoSimp = tipo_simple();
			// --
			retorno.tipo = retTipoSimp.tipo;
			retorno.esSimple = true;
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TPALRES_ARRAY) {
			TStipo retTipoarr = tipo_arreglo();
			// --
			retorno.tipo = retTipoarr.tipo;
			retorno.esSimple = false;
			// --
			return retorno;
		}
		throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
	}

	// <definicion de tipo> :
	// TIDENTIFICADOR TSIMBOLO_IGUAL <tipo>
	public TSintetizado definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (mepa.MestaEnFuncion) {
				if (identificador.equals(mepa.MLexemaUnidad)) {
					throw new ExcepASemantico("Se esta redefiniendo la funcion.", TActual.nlinea);
				}
			}
			if (TablaSimb.existe_en_tabla(identificador, TablaSimb.TodosSimb, true)) {
				throw new ExcepASemantico("Identificador ya utilizado.", TActual.nlinea);
			}
			// --
			if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
				leerToken();
				TStiSi retTipo = tipo();
				// --
				TablaSimb.guardar_tipo_en_tabla(identificador, retTipo.tipo);
				// --
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '='.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
		}
	}

	// <tipo simple> :
	// TIDENTIFICADOR <tipo simple'> |
	// TNUMERO TDOBLEPUNTO <constante de subrango> |
	// TOPERMAS <tipo simple de signo>
	// TOPERMENOS <tipo simple de signo>
	public TStipo tipo_simple() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			TStifvaf retTipoSimpP = tipo_simpleP();
			// --
			TStipo retorno = new TStipo();
			if (retTipoSimpP.tipof == null) {
				if (TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.TIPO }, false)) {
					Tipo id = (Tipo) TablaSimb.obtener_de_tabla(identificador, new int[] { Simbolo.TIPO });
					if (id.tipo_de_estructura.clase != TTipo.TPARREGLO) {
						retorno.tipo = id.tipo_de_estructura;
					} else {
						throw new ExcepASemantico("El tipo simple no puede ser arreglo", TActual.nlinea);
					}
				} else {
					throw new ExcepASemantico("Tipo no declarado", TActual.nlinea);
				}
			} else {
				if (TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.CONSTANTE }, false)) {
					Constante id = (Constante) TablaSimb.obtener_de_tabla(identificador, new int[] { Simbolo.CONSTANTE });
					// Controla que las dos constantes sean del mismo tipo, Entero
					// (Tipo simple ya es entero, porque es constante de subrango).
					if (id.tipo_de_estructura.clase == TTipo.TPENTERO) {
						// Controla que el valor de la primera constante sea menor o
						// igual que el de la segunda.
						if (id.valor <= retTipoSimpP.valorf) {
							retorno.tipo = new TSubrango(id.valor, retTipoSimpP.valorf - id.valor);
						} else {
							throw new ExcepASemantico("El valor del primer elemento del subrango tiene que ser menor igual que el del segundo.", TActual.nlinea);
						}
					} else {
						throw new ExcepASemantico("Los dos elementos del subrango deben ser enteros.", TActual.nlinea);
					}
				} else {
					throw new ExcepASemantico("Constante no declarada.", TActual.nlinea);
				}
			}
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TNUMERO) {
			int valor = Integer.parseInt(TActual.lexema);
			// --
			// Controla que no se pasa del maximo entero permitido.
			if (valor > MEPa.maxint) {
				throw new ExcepASemantico("La contastante entera supera el maximo predefinido.", TActual.nlinea);
			}
			// --
			leerToken();
			if (TActual.tipo == Token.TDOBLEPUNTO) {
				leerToken();
				TStiva retConstSubr = constante_de_subrango();
				// --
				// Controla que la primera variable sea menor que la segunda.
				TStipo retorno = new TStipo();
				if (valor <= retConstSubr.valor) {
					retorno.tipo = new TSubrango(valor, retConstSubr.valor - valor);
				} else {
					throw new ExcepASemantico("El valor del primer elemento del subrango tiene que ser menor igual que el del segundo.", TActual.nlinea);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '..'.", TActual.nlinea, TActual);
			}
		}
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			String oper = TActual.lexema;
			leerToken();
			// --
			boolean esMenos = oper.equals("-");
			// --
			return tipo_simple_de_signo(esMenos);
		}
		throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
	}

	// <tipo simple de signo>:
	// TNUMERO TDOBLEPUNTO <constante de subrango> |
	// TIDENTIFICADOR TDOBLEPUNTO <constante de subrango> |
	public TStipo tipo_simple_de_signo(boolean esMenos) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			if (TActual.tipo == Token.TDOBLEPUNTO) {
				leerToken();
				TStiva retConstSub = constante_de_subrango();
				// --
				TStipo retorno = new TStipo();
				if (TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.CONSTANTE }, false)) {
					Constante id = (Constante) TablaSimb.obtener_de_tabla(identificador, new int[] { Simbolo.CONSTANTE });
					// controla que las dos constantes sean del mismo tipo, Entero.
					// (Constante de subrango ya lo es)
					if (id.tipo_de_estructura.clase == TTipo.TPENTERO) {
						int valor;
						if (esMenos) {
							valor = (-1) * id.valor;
						} else {
							valor = id.valor;
						}
						// controla que el valor de la primera constante sea menor o
						// igual que el de la segunda.
						if (valor <= retConstSub.valor) {
							retorno.tipo = new TSubrango(valor, retConstSub.valor - valor);
						} else {
							throw new ExcepASemantico("El primer elemento del subrango debe ser menor o igual al segundo", TActual.nlinea);
						}
					} else {
						throw new ExcepASemantico("El valor del subrango debe ser un entero.", TActual.nlinea);
					}
				} else {
					throw new ExcepASemantico("Constante no declarada.", TActual.nlinea);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '..'.", TActual.nlinea, TActual);
			}
		} else if (TActual.tipo == Token.TNUMERO) {
			int valor = Integer.parseInt(TActual.lexema);
			// --
			// Controla que no se pasa del maximo entero permitido.
			if (valor > MEPa.maxint) {
				throw new ExcepASemantico("La contastante entera supera el maximo predefinido.", TActual.nlinea);
			}
			// --
			leerToken();
			if (TActual.tipo == Token.TDOBLEPUNTO) {
				leerToken();
				TStiva retConstSub = constante_de_subrango();
				// --
				if (esMenos) {
					valor = (-1) * valor;
				}
				// Controla que la primera constante sea menor o igual que la
				// segunda.
				TStipo retorno = new TStipo();
				if (valor <= retConstSub.valor) {
					retorno.tipo = new TSubrango(valor, retConstSub.valor - valor);
				} else {
					throw new ExcepASemantico("El primer elemento del subrango debe ser menor o igual al segundo", TActual.nlinea);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '..'.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
		}
	}

	// <tipo simple'> :
	// TDOBLEPUNT <constante de subrango> |
	// lambda
	public TStifvaf tipo_simpleP() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStifvaf retorno = new TStifvaf();
		if (TActual.tipo == Token.TDOBLEPUNTO) {
			leerToken();
			TStiva retConstSubr = constante_de_subrango();
			// --
			retorno.tipof = retConstSubr.tipo;
			retorno.valorf = retConstSubr.valor;
			// --
			return retorno;
		} else {
			// --
			retorno.tipof = null;
			retorno.valorf = 0;
			// --
			return retorno;
		}
	}

	// <tipo arreglo> :
	// TPALRES_ARRAY TCORA <tipo simple> TCORC TPALRES_OF <tipo simple>
	public TStipo tipo_arreglo() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_ARRAY) {
			leerToken();
			if (TActual.tipo == Token.TCORA) {
				leerToken();
				TStipo retTipoSimp1 = tipo_simple();
				// --
				if (!retTipoSimp1.tipo.esSubrango()) {
					throw new ExcepASemantico("El indice del arreglo debe ser un subrango de enteros", TActual.nlinea);
				}
				// --
				if (TActual.tipo == Token.TCORC) {
					leerToken();
					if (TActual.tipo == Token.TPALRES_OF) {
						leerToken();
						TStipo retTipoSimp2 = tipo_simple();
						// --
						TStipo retorno = new TStipo();

						retorno.tipo = new TArreglo((TSubrango) retTipoSimp1.tipo, (TSimple) retTipoSimp2.tipo);
						// --
						return retorno;
					} else {
						throw new ExcepASintatico("Se esperaba la palabra reservada 'of'.", TActual.nlinea, TActual);
					}
				} else {
					throw new ExcepASintatico("Se esperaba el simbolo ']'.", TActual.nlinea, TActual);
				}
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '['.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'array'.", TActual.nlinea, TActual);
		}
	}

	/*--------------------------------------------------------------*/
	// ***********VARIABLES***********
	/*--------------------------------------------------------------*/
	// <declaracion de variable> :
	// TIDENTIFICADOR <siguiente identificador> TDOSPUNTOS <tipo>
	public TStamvar declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (mepa.MestaEnFuncion) {
				if (identificador.equals(mepa.MLexemaUnidad)) {
					throw new ExcepASemantico("Se intento redefinir la funcion.", TActual.nlinea);
				}
			}
			ArrayList<String> lista = new ArrayList<String>();
			if (TablaSimb.existe_en_tabla(identificador, TablaSimb.TodosSimb, true)) {
				throw new ExcepASemantico("Identificador ya utilizado.", TActual.nlinea);
			} else {
				lista.add(identificador);
			}
			// --
			TSlistlexema retSigId = siguiente_identificador(lista);
			if (TActual.tipo == Token.TDOSPUNTOS) {
				leerToken();
				TStiSi retTipo = tipo();
				// --
				int tamano;
				int mtamvar;
				if (retTipo.tipo.esTipoSimple()) {
					tamano = 1;
				} else {
					// es un arreglo.
					tamano = retTipo.tipo.tammemoria;
				}
				mtamvar = tamano * retSigId.lista.size();

				int indice = 0;
				for (String lexema : retSigId.lista) {
					TablaSimb.guardar_variable_en_tabla(lexema, retTipo.tipo, TablaSimb.Mnivelact, mepa.MposVar + (indice * tamano), true);
					indice++;
				}
				TStamvar retorno = new TStamvar();
				retorno.Mtam_var = mtamvar;
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ':'.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
		}
	}

	// <siguiente identificador> :
	// TCOMA TIDENTIFICADOR <siguiente identificador> |
	// lambda
	public TSlistlexema siguiente_identificador(ArrayList<String> lista) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TCOMA) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				String identificador = TActual.lexema;
				leerToken();
				// --
				if (mepa.MestaEnFuncion) {
					if (identificador.equals(mepa.MLexemaUnidad)) {
						throw new ExcepASemantico("Se intento redefinir la funcion.", TActual.nlinea);
					}
				}
				if (TablaSimb.existe_en_tabla(identificador, TablaSimb.TodosSimb, true)) {
					throw new ExcepASemantico("Identificador ya utilizado.", TActual.nlinea);
				} else {
					if (lista.contains(identificador)) {
						throw new ExcepASemantico("Identificador repetido.", TActual.nlinea);
					} else {
						lista.add(identificador);
					}
				}
				// --
				return siguiente_identificador(lista);
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			TSlistlexema retorno = new TSlistlexema();
			retorno.lista = lista;
			return retorno;
		}
	}

	// <variable'> :
	// TCORA <expresion> TCORC |
	// lambda
	public TStipo variableP(String lexema) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStipo retorno = new TStipo();
		if (TActual.tipo == Token.TCORA) {
			leerToken();
			TStipo retExp = expresion(false);
			if (TActual.tipo == Token.TCORC) {
				leerToken();
				// --
				if (TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.VARIABLE }, false)) {
					Variable var = (Variable) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.VARIABLE });
					if (var.tipo_de_estructura.clase == TTipo.TPARREGLO) {
						TArreglo arreglo = (TArreglo) var.tipo_de_estructura;
						if (retExp.tipo.clase == TTipo.TPENTERO) {
							// control fuera de rango
							mepa.Mimprimir("CONT", String.valueOf(arreglo.base), ",", String.valueOf(arreglo.base + arreglo.tamano));
							// normaliza el indice del arreglo.
							mepa.Mimprimir("APCT", String.valueOf(arreglo.base));
							mepa.Mimprimir("SUST");

							retorno.tipo = arreglo.tbase;
						} else {
							throw new ExcepASemantico("Se esperaba un entero en la expresion.", TActual.nlinea);
						}
					} else {
						throw new ExcepASemantico("Se esperaba un arreglo.", TActual.nlinea);
					}
				} else {
					throw new ExcepASemantico("Variable no declarada.", TActual.nlinea);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ']'.", TActual.nlinea, TActual);
			}
		} else {
			// nunca deberia entrar a este caso.
			return retorno;
		}
	}

	/*--------------------------------------------------------------*/
	// ***********EXPRESIONES***********
	/*--------------------------------------------------------------*/
	// <factor> :
	// TIDENTIFICADOR <factor'> |
	// TNUMERO |
	// TCARACTER |
	// TPARENTA <expresion> TPARENTC |
	// TOPER_NOT <factor>
	public TStipo factor(boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			// --
			if (!TablaSimb.existe_en_tabla(identificador, new int[] { Simbolo.FUNCION, Simbolo.CONSTANTE, Simbolo.VARIABLE }, false)) {
				throw new ExcepASemantico("Identificador no declarado.", TActual.nlinea);
			}
			// --
			return factorP(identificador, esReferencia);
		}
		if (TActual.tipo == Token.TNUMERO) {
			String numero = TActual.lexema;
			// --
			int valor = Integer.parseInt(numero);
			// Controla que no se pasa del maximo entero permitido.
			if (valor > MEPa.maxint) {
				throw new ExcepASemantico("La contastante entera supera el maximo predefinido.", TActual.nlinea);
			}
			// --
			leerToken();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// apila la constante numero.
			mepa.Mimprimir("APCT", numero);
			TStipo retorno = new TStipo();

			retorno.tipo = new TEntero();
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TCARACTER) {
			String caracter = TActual.lexema;
			leerToken();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// apila la constante caracter.
			mepa.Mimprimir("APCT", mepa.CharToMepa(caracter));
			TStipo retorno = new TStipo();

			retorno.tipo = new TChar();
			// --
			return retorno;
		}
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			TStipo retorno = expresion(esReferencia);
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		}
		if (TActual.tipo == Token.TOPER_NOT) {
			leerToken();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// --
			TStipo retfactor = factor(false);
			// --
			if (retfactor.tipo.clase == TTipo.TPBOOLEAN) {
				// apila el negativo del tope de la pila.
				mepa.Mimprimir("NEGA");
			} else {
				throw new ExcepASemantico("Se esperaba un tipo booleano", TActual.nlinea);
			}
			// --
			TStipo retorno = new TStipo();
			retorno.tipo = new TBoolean();
			return retorno;
		}

		throw new ExcepASintatico("Factor invalido.", TActual.nlinea, TActual);
	}

	// <factor'> :
	// <variable'> |
	// <designador de funcion'> |
	// lambda
	public TStipo factorP(String lexema, boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TCORA) {
			TStipo retVarP = variableP(lexema);
			// --
			if (!TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.VARIABLE }, false)) {
				throw new ExcepASemantico("Variable no declarada.", TActual.nlinea);
			}
			Variable arreglo = (Variable) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.VARIABLE });
			if (esReferencia) {
				// se pasa por referencia
				if (arreglo.esPorvalor) {
					// y vino por valor
					mepa.Mimprimir("APDC", String.valueOf(arreglo.nivelL), ",", String.valueOf(arreglo.desp));
					// imprimir(APDR,arreglo.nivelL,arreglo.desp);
					// imprimir(SUMA);
				} else {
					// o vino por referencia
					// se apila la direccion y se le apila el offset
					mepa.Mimprimir("APVL", String.valueOf(arreglo.nivelL), ",", String.valueOf(arreglo.desp));
					mepa.Mimprimir("SUMA");
				}
			} else {
				// se pasa por valor
				if (arreglo.esPorvalor) {
					// y viene por valor
					mepa.Mimprimir("APAR", String.valueOf(arreglo.nivelL), ",", String.valueOf(arreglo.desp));
				} else {
					// o vino por referencia.
					mepa.Mimprimir("APAI", String.valueOf(arreglo.nivelL), ",", String.valueOf(arreglo.desp));
				}
			}
			// --
			return retVarP;
		}
		if (TActual.tipo == Token.TPARENTA) {
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una variable por referencia.", TActual.nlinea);
			}
			// --
			return designador_de_funcionP(lexema);
		}
		// En el caso de lambda
		// --
		TTipo tipo;
		if (TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.VARIABLE, Simbolo.CONSTANTE, Simbolo.FUNCION }, false)) {
			Simbolo id = TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.VARIABLE, Simbolo.CONSTANTE, Simbolo.FUNCION });
			if (id.tipo_de_simbolo == Simbolo.FUNCION) {
				if (esReferencia) {
					throw new ExcepASemantico("Se esperaba una variable por referencia.", TActual.nlinea);
				}
				Funcion fun = (Funcion) id;
				tipo = fun.salida;
				// reserva el espacio en la memoria para el retorno de la funcion.
				mepa.Mimprimir("RMEM", String.valueOf(tipo.tammemoria));
				// llama a la funcion.
				mepa.Mimprimir("LLPR", fun.etiqueta);
			} else if (id.tipo_de_simbolo == Simbolo.CONSTANTE) {
				if (esReferencia) {
					throw new ExcepASemantico("Se esperaba una variable por referencia.", TActual.nlinea);
				}
				Constante cons = (Constante) id;
				tipo = cons.tipo_de_estructura;
				// apila la constante.
				mepa.Mimprimir("APCT", String.valueOf(cons.valor));
			} else {
				// si es variable.
				Variable var = (Variable) id;
				tipo = var.tipo_de_estructura;
				// se esta pasando por referencia.
				if (esReferencia) {
					// y nos vino por valor.
					if (var.esPorvalor) {
						// apila la direccion de la variable (para arreglos es lo
						// mismo).
						mepa.Mimprimir("APDR", String.valueOf(var.nivelL),",", String.valueOf(var.desp));
					} else {
						// vino por referencia.
						// apilamos directamente el valor (es la direccion).
						mepa.Mimprimir("APVL", String.valueOf(var.nivelL), String.valueOf(var.desp));
					}
				} else {
					// y nos vino por valor.
					if (var.esPorvalor) {
						if (var.tipo_de_estructura.clase == TTipo.TPARREGLO) {
							// es un arreglo por valor.
							mepa.Mimprimir("PUAR", String.valueOf(var.nivelL), ",", String.valueOf(var.desp), ",", String.valueOf(var.tipo_de_estructura.tammemoria));
						} else {
							// es una variable por valor.
							mepa.Mimprimir("APVL", String.valueOf(var.nivelL), ",", String.valueOf(var.desp));
						}
					} else {
						if (var.tipo_de_estructura.clase == TTipo.TPARREGLO) {
							// es un arreglo por referencia.
							mepa.Mimprimir("PUAI", String.valueOf(var.nivelL), ",", String.valueOf(var.desp), ",", String.valueOf(var.tipo_de_estructura.tammemoria));
						} else {
							// es una variable por referencia.
							mepa.Mimprimir("APVI", String.valueOf(var.nivelL), ",", String.valueOf(var.desp));
						}
					}
				}
			}
		} else {
			throw new ExcepASemantico("Identificador no declarada.", TActual.nlinea);
		}
		TStipo retorno = new TStipo();
		retorno.tipo = tipo;
		// --
		return retorno;
	}

	// <termino> :
	// <factor><termino'>
	public TStipo termino(boolean esReferencia) throws IOException, ExcepALexico, ExcepASintatico, ExcepASemantico {
		TStipo retFac = factor(esReferencia);
		return terminoP(retFac.tipo, esReferencia);
	}

	// <termino'> :
	// <operador de multiplicacion><factor><termino'> |
	// lambda
	public TStipo terminoP(TTipo tipof, boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TOPERMULT || TActual.tipo == Token.TOPERDIV || TActual.tipo == Token.TOPER_AND) {
			TSlexema retOpermult = operador_de_multiplicacion();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// --
			TStipo retFac = factor(false);
			// --
			if (mepa.chequear_tipo_oper(tipof, retFac.tipo, retOpermult.lexema)) {
				String lexema = retOpermult.lexema;

				if (lexema.equals("*")) {
					mepa.Mimprimir("MULT");
				} else if (lexema.equals("div")) {
					// Control de divicion por cero
					mepa.Mimprimir("DIVC");
					// Hace la division
					mepa.Mimprimir("DIVI");
				} else {
					// Es AND
					mepa.Mimprimir("CONJ");
				}
			} else {
				throw new ExcepASemantico("El operador no esta definido para los operandos suministrados.", TActual.nlinea);
			}
			// --
			return terminoP(tipof, false);
		} else {
			TStipo retorno = new TStipo();
			retorno.tipo = tipof;
			return retorno;
		}
	}

	// <expresion simple> :
	// <termino><expresion simple'> |
	// <signo><termino><expresion simple'>
	public TStipo expresion_simple(boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR || TActual.tipo == Token.TNUMERO || TActual.tipo == Token.TCARACTER || TActual.tipo == Token.TPARENTA
				|| TActual.tipo == Token.TOPER_NOT) {
			TStipo retTerm = termino(esReferencia);
			return expresion_simpleP(retTerm.tipo, esReferencia);
		}
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			TSesMenos retSig = signo();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// --
			TStipo retTerm = termino(false);
			// --
			if (retTerm.tipo.clase == TTipo.TPENTERO) {
				if (retSig.esMenos) {
					// Invierte el signo
					mepa.Mimprimir("UMEN");
				}
			} else {
				throw new ExcepASemantico("Se esperaba una expresion aritmetica luego de un signo.", TActual.nlinea);
			}
			// --
			return expresion_simpleP(retTerm.tipo, false);
		}

		throw new ExcepASintatico("Expresion invalida.", TActual.nlinea, TActual);
	}

	// <expresion simple'> :
	// <operador de suma><termino><expresion simple'> |
	// lambda
	public TStipo expresion_simpleP(TTipo tipof, boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS || TActual.tipo == Token.TOPER_OR) {
			TSlexema retOpsuma = operador_de_suma();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// --
			TStipo retTerm = termino(false);
			// --
			if (mepa.chequear_tipo_oper(tipof, retTerm.tipo, retOpsuma.lexema)) {
				String lexema = retOpsuma.lexema;

				if (lexema.equals("+")) {
					mepa.Mimprimir("SUMA");
				} else if (lexema.equals("-")) {
					mepa.Mimprimir("SUST");
				} else {
					// Es OR
					mepa.Mimprimir("DISJ");
				}
			} else {
				throw new ExcepASemantico("El operador no esta definido para los operandos suministrados.", TActual.nlinea);
			}
			// --
			return expresion_simpleP(tipof, false);
		} else {
			// --
			TStipo retorno = new TStipo();
			retorno.tipo = tipof;
			// --
			return retorno;
		}
	}

	// <expresion> :
	// <expresion simple><expresion'>
	public TStipo expresion(boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStipo retExpsimp = expresion_simple(esReferencia);
		return expresionP(retExpsimp.tipo, esReferencia);
	}

	// <expresion'> :
	// <operador de relacion><expresion simple> |
	// lambda
	public TStipo expresionP(TTipo tipof, boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// --
		TStipo retorno = new TStipo();
		// --
		if (TActual.tipo == Token.TSIMBOLO_IGUAL || TActual.tipo == Token.TSIMBOLO_DISTINTO || TActual.tipo == Token.TSIMBOLO_MAYOR
				|| TActual.tipo == Token.TSIMBOLO_MAYORIGUAL || TActual.tipo == Token.TSIMBOLO_MENOR || TActual.tipo == Token.TSIMBOLO_MENORIGUAL) {
			TSlexema reOprel = operador_de_relacion();
			// --
			if (esReferencia) {
				throw new ExcepASemantico("Se esperaba una referencia a una variable.", TActual.nlinea);
			}
			// --
			TStipo retExpsimp = expresion_simple(false);
			// --
			if ((tipof.clase == TTipo.TPENTERO) && (retExpsimp.tipo.clase == TTipo.TPENTERO)) {
				retorno.tipo = new TBoolean();

				String lexema = reOprel.lexema;

				if (lexema.equals("<")) {
					mepa.Mimprimir("CMME");
				} else if (lexema.equals("<=")) {
					// Calcula el inverso y lo niega
					mepa.Mimprimir("CMMA");
					mepa.Mimprimir("NEGA");
				} else if (lexema.equals(">")) {
					mepa.Mimprimir("CMMA");
				} else if (lexema.equals(">=")) {
					// Calcula el inverso y lo niega
					mepa.Mimprimir("CMME");
					mepa.Mimprimir("NEGA");
				} else if (lexema.equals("<>")) {
					mepa.Mimprimir("CMDG");
				} else {
					// Es igual
					mepa.Mimprimir("CMIG");
				}
			} else {
				throw new ExcepASemantico("Se esperaba dos expresiones aritmeticas con el operador de relacion.", TActual.nlinea);
			}
			// --
			return retorno;
		} else {
			retorno.tipo = tipof;
			return retorno;
		}
	}

	/*--------------------------------------------------------------*/
	// ***********OPERADORES***********
	/*--------------------------------------------------------------*/
	// <operador de multiplicacion> :
	// TOPERMULT |
	// TOPER_DIV |
	// TOPER_AND
	public TSlexema operador_de_multiplicacion() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TOPERMULT || TActual.tipo == Token.TOPERDIV || TActual.tipo == Token.TOPER_AND) {
			// --
			TSlexema retorno = new TSlexema();
			retorno.lexema = TActual.lexema;
			// --
			leerToken();
			return retorno;
		} else {
			throw new ExcepASintatico("Se esperaba una operador de multiplicacion.", TActual.nlinea, TActual);
		}
	}

	// <operador de suma> :
	// TOPERMAS |
	// TOPERMENOS |
	// TOPER_OR
	public TSlexema operador_de_suma() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS || TActual.tipo == Token.TOPER_OR) {
			// --
			TSlexema retorno = new TSlexema();
			retorno.lexema = TActual.lexema;
			// --
			leerToken();
			return retorno;
		} else {
			throw new ExcepASintatico("Se esperaba una operador de suma.", TActual.nlinea, TActual);
		}
	}

	// <operador de relacion> :
	// TSIMBOLO_IGUAL |
	// TSIMBOLO_DISTINTO |
	// TSIMBOLO_MENOR |
	// TSIMBOLO_MENORIGUAL |
	// TSIMBOLO_MAYOR |
	// TSIMBOLO_MAYORIGUAL
	public TSlexema operador_de_relacion() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TSIMBOLO_IGUAL || TActual.tipo == Token.TSIMBOLO_DISTINTO || TActual.tipo == Token.TSIMBOLO_MAYOR
				|| TActual.tipo == Token.TSIMBOLO_MAYORIGUAL || TActual.tipo == Token.TSIMBOLO_MENOR || TActual.tipo == Token.TSIMBOLO_MENORIGUAL) {
			// --
			TSlexema retorno = new TSlexema();
			retorno.lexema = TActual.lexema;
			// --
			leerToken();
			return retorno;
		} else {
			throw new ExcepASintatico("Se esperaba una operador de relacion.", TActual.nlinea, TActual);
		}
	}

	/*--------------------------------------------------------------*/
	// ***********DESIGNADOR DE FUNCION***********
	/*--------------------------------------------------------------*/
	// <designador de funcion'> :
	// TPARENTA <parametro actual> <siguiente parametro actual> TPARENTC |
	// lambda
	public TStipo designador_de_funcionP(String lexema) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// --
		int posicion = 0;
		boolean esReferencia;
		Funcion fun;
		TStipo retorno = new TStipo();

		if (TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.FUNCION }, false)) {
			// Reserva el espacio de memoria para el retorno de la funcion
			fun = (Funcion) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.FUNCION });

			int tam;
			if (fun.salida.clase == TTipo.TPARREGLO) {
				tam = fun.salida.tammemoria;
			} else {
				tam = 1;
			}

			mepa.Mimprimir("RMEM", String.valueOf(tam));

			// La cantidad de los parametros no se pasa de la que requiere la
			// unidad.
			if (fun.cpf > posicion) {
				if (fun.ppf[posicion]) {
					esReferencia = false;
				} else {
					esReferencia = true;
				}
			} else {
				throw new ExcepASemantico("Se supero la cantidad de parametros formales.", TActual.nlinea);
			}
		} else {
			throw new ExcepASemantico("Funcion no declarada.", TActual.nlinea);
		}
		// --
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			TStipo retParAct = parametro_actual(esReferencia);
			// --
			// Chequeo de tipos
			if (!retParAct.tipo.comparar(fun.tpf[posicion])) {
				throw new ExcepASemantico("Los tipos no coinciden.", TActual.nlinea);
			}
			// --
			siguiente_parametro_actual(false, lexema, posicion + 1);
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				// --
				// Si es una funcion predefinida
				if (fun.nivelL == TablaSimb.MNivelPre) {
					if (lexema.equals("succ")) {
						mepa.Mimprimir("APCT", "1");
						mepa.Mimprimir("SUMA");
						retorno.tipo = retParAct.tipo;
					} else if (lexema.equals("pred")) {
						mepa.Mimprimir("APCT", "1");
						mepa.Mimprimir("SUST");
						retorno.tipo = retParAct.tipo;
					} else if (lexema.equals("ord")) {
						retorno.tipo = new TEntero();
					} else {
						// Sino es CHR
						retorno.tipo = new TChar();
					}
				} else {
					retorno.tipo = fun.salida;

					mepa.Mimprimir("LLPR", fun.etiqueta);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		} else {
			// Nunca entra al lambda
			return retorno;
		}
	}

	// <siguiente parametro actual> :
	// TCOMA <parametro actual><siguiente parametro actual> |
	// lambda
	public TSintetizado siguiente_parametro_actual(boolean esProcedimiento, String lexema, int posicion) throws ExcepALexico, IOException, ExcepASintatico,
			ExcepASemantico {
		// --
		int UNIDAD;
		boolean esReferencia;
		TSintetizado retorno = new TSintetizado();
		Procedimiento uni;
		// --
		if (TActual.tipo == Token.TCOMA) {
			leerToken();
			// --
			if (esProcedimiento) {
				UNIDAD = Simbolo.PROCEDIMIENTO;
			} else {
				UNIDAD = Simbolo.FUNCION;
			}

			// Suponemos que ya chequeo que existe
			uni = (Procedimiento) TablaSimb.obtener_de_tabla(lexema, new int[] { UNIDAD });

			// La cantidad de parametros no se pasa de la que requiere la unidad.
			if (uni.cpf > posicion) {
				if (uni.ppf[posicion]) {
					esReferencia = false;
				} else {
					esReferencia = true;
				}
			} else {
				throw new ExcepASemantico("Se supero la cantidad de parametros formales.", TActual.nlinea);
			}
			// --
			TStipo retParAct = parametro_actual(esReferencia);
			// --
			// Hace el chequeo de tipos
			if (!retParAct.tipo.comparar(uni.tpf[posicion])) {
				throw new ExcepASemantico("Los tipos no coinciden.", TActual.nlinea);
			}
			// --
			siguiente_parametro_actual(esProcedimiento, lexema, posicion + 1);
			return retorno;
		} else {
			// --
			if (esProcedimiento) {
				UNIDAD = Simbolo.PROCEDIMIENTO;
			} else {
				UNIDAD = Simbolo.FUNCION;
			}

			// Suponemos que ya chequeo que existe
			uni = (Procedimiento) TablaSimb.obtener_de_tabla(lexema, new int[] { UNIDAD });

			if (uni.cpf != posicion) {
				throw new ExcepASemantico("Faltan parametros en pasaje de la unidad.", TActual.nlinea);
			}
			// --
			return retorno;
		}
	}

	/*--------------------------------------------------------------*/
	// ***********SENTENCIAS***********
	/*--------------------------------------------------------------*/
	// <sentencia> :
	// <sentencia simple> |
	// <sentencia estructurada> |
	// lambda
	public TStipo sentencia() throws Exception {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			return sentencia_simple();
		}
		if ((TActual.tipo == Token.TPALRES_BEGIN) || (TActual.tipo == Token.TPALRES_IF) || (TActual.tipo == Token.TPALRES_WHILE)) {
			return sentencia_estructurada();
		}
		// --
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		return retorno;
	}

	// <sentencia simple> :
	// TIDENTIFICADOR <sentencia simple'>
	public TStipo sentencia_simple() throws Exception {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador = TActual.lexema;
			leerToken();
			return sentencia_simpleP(identificador);
		} else {
			throw new ExcepASintatico("Sentencia invalida.", TActual.nlinea, TActual);
		}
	}

	// <sentencia simple'> :
	// <sentencia de asignacion'> |
	// <sentencia de procedimiento'> |
	// lambda
	public TStipo sentencia_simpleP(String lexema) throws Exception {
		if ((TActual.tipo == Token.TCORA) || (TActual.tipo == Token.TASIGN)) {
			return sentencia_de_asignacionP(lexema);
		}
		if (TActual.tipo == Token.TPARENTA) {
			return sentencia_de_procedimientoP(lexema);
		}
		// --
		if (TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.PROCEDIMIENTO }, false)) {
			Procedimiento proc = (Procedimiento) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.PROCEDIMIENTO });

			if (proc.cpf == 0) {
				// Llamamos al procedimiento
				mepa.Mimprimir("LLPR", String.valueOf(proc.etiqueta));
			} else {
				throw new ExcepASemantico("Procedimiento sin parametros no declarado.", TActual.nlinea);
			}
		} else {
			throw new ExcepASemantico("Procedimiento no declarado.", TActual.nlinea);
		}
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		return retorno;
	}

	// <sentencia de asignacion'> :
	// <variable'> TASIGN <expresion> |
	// TASIGN <expresion>
	public TStipo sentencia_de_asignacionP(String lexema) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// --
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		if (TActual.tipo == Token.TCORA) {
			TStipo retVarp = variableP(lexema);

			if (TActual.tipo == Token.TASIGN) {
				leerToken();
				TStipo retExp = expresion(false);
				// --
				if (retVarp.tipo.comparar(retExp.tipo)) {
					if (!TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.VARIABLE }, false)) {
						throw new ExcepASemantico("Variable no declarada.", TActual.nlinea);
					}
					Variable arreglo = (Variable) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.VARIABLE });
					if (arreglo.esPorvalor) {
						mepa.Mimprimir("ALAR", String.valueOf(arreglo.nivelL), ",", String.valueOf(arreglo.desp));
					} else {
						// Es por referencia
						mepa.Mimprimir("ALAI", String.valueOf(arreglo.nivelL), ",", String.valueOf(arreglo.desp));
					}
				} else {
					throw new ExcepASemantico("Tipos no compatibles", TActual.nlinea);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ':='.", TActual.nlinea, TActual);
			}
		} else {
			if (TActual.tipo == Token.TASIGN) {
				leerToken();
				TStipo retExp = expresion(false);
				// --
				if (mepa.MestaEnFuncion && (mepa.MLexemaUnidad.equals(lexema))) {
					if (!TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.FUNCION }, false)) {
						throw new ExcepASemantico("Funcion no declarada.", TActual.nlinea);
					}
					Funcion fun = (Funcion) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.FUNCION });
					if (fun.salida.comparar(retExp.tipo)) {
						// Recuerda que se realizo la asignacion.
						mepa.MretFuncion = true;
						if (fun.salida.clase == TTipo.TPARREGLO) {
							// Es un arreglo, es una asignacion masiva.
							mepa.Mimprimir("POAR", String.valueOf(fun.nivelL), ",", String.valueOf(fun.desp), ",", String.valueOf(fun.salida.tammemoria));
						} else {
							// Apila la variable en la pila
							mepa.Mimprimir("ALVL", String.valueOf(fun.nivelL), ",", String.valueOf(fun.desp));
						}
					} else {
						throw new ExcepASemantico("Tipos no compatibles.", TActual.nlinea);
					}
				} else if (TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.VARIABLE }, false)) {
					Variable var = (Variable) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.VARIABLE });
					if (var.tipo_de_estructura.comparar(retExp.tipo)) {
						// Si es por valor, asigna directamente
						if (var.esPorvalor) {
							if (var.tipo_de_estructura.clase == TTipo.TPARREGLO) {
								// Asignacion masiva de arreglo directo
								mepa.Mimprimir("POAR", String.valueOf(var.nivelL), ",", String.valueOf(var.desp), ",",
										String.valueOf(var.tipo_de_estructura.tammemoria));
							} else {
								// Apila el valor de la pila en la variable
								mepa.Mimprimir("ALVL", String.valueOf(var.nivelL), ",", String.valueOf(var.desp));
							}
						} else {
							if (var.tipo_de_estructura.clase == TTipo.TPARREGLO) {
								// Asignacion masiva de arreglo directo
								mepa.Mimprimir("POAI", String.valueOf(var.nivelL), ",", String.valueOf(var.desp), ",",
										String.valueOf(var.tipo_de_estructura.tammemoria));
							} else {
								// Apila el valor de la pila en la variable
								mepa.Mimprimir("ALVI", String.valueOf(var.nivelL), ",", String.valueOf(var.desp));
							}
						}
					} else {
						throw new ExcepASemantico("Tipo no compatible.", TActual.nlinea);
					}
				} else {
					throw new ExcepASemantico("Variable no declarada.", TActual.nlinea);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ':='.", TActual.nlinea, TActual);
			}
		}
	}

	// <sentencia de procedimiento'> :
	// TPARENTA <parametro actual><siguiente parametro actual> TPARENTC
	public TStipo sentencia_de_procedimientoP(String lexema) throws Exception {
		// --
		boolean esReferencia;
		Procedimiento proc;
		int posicion;

		TStipo retorno;

		retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			// --
			if (TablaSimb.existe_en_tabla(lexema, new int[] { Simbolo.PROCEDIMIENTO }, false)) {
				proc = (Procedimiento) TablaSimb.obtener_de_tabla(lexema, new int[] { Simbolo.PROCEDIMIENTO });

				posicion = 0;
				// La cantidad de parametros no se pasa de la que requiere la
				// unidad.
				if (proc.cpf > posicion) {

					if (proc.ppf[posicion]) {
						esReferencia = false;
					} else {
						esReferencia = true;
					}
				} else {
					throw new ExcepASemantico("Se supero la cantidad de parametros formales.", TActual.nlinea);
				}
			} else {
				throw new ExcepASemantico("Procedimiento no declarado.", TActual.nlinea);
			}
			// --
			TStipo retParact = parametro_actual(esReferencia);
			// --
			// Chequeo de tipos
			if (!retParact.tipo.comparar(proc.tpf[posicion])) {
				throw new ExcepASemantico("Los tipos no coinciden.", TActual.nlinea);
			}
			// --
			siguiente_parametro_actual(true, lexema, posicion + 1);
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				// --
				if (proc.nivelL == TablaSimb.MNivelPre) {
					if (lexema.equals("write")) {
						if (retParact.tipo.clase == TTipo.TPENTERO) {
							//mepa.Mimprimir("LLPR", "L4");
							mepa.Mimprimir("IMPR");
						} else {
							//mepa.Mimprimir("LLPR", "L8");
							mepa.Mimprimir("IMCH");
						}
					} else if (lexema.equals("writeln")) {
						if (retParact.tipo.clase == TTipo.TPENTERO) {
							//mepa.Mimprimir("LLPR", "L5");
							mepa.Mimprimir("IMLN");
						} else {
							//mepa.Mimprimir("LLPR", "L9");
							mepa.Mimprimir("IMCN");
						}
					} else if (lexema.equals("read")) {
						if (retParact.tipo.clase == TTipo.TPENTERO) {
							//mepa.Mimprimir("LLPR", "L6");
							mepa.Mimprimir("LEER");
						} else {
							//mepa.Mimprimir("LLPR", "L10");
							mepa.Mimprimir("LECH");
						}
					} else if (lexema.equals("readln")) {
						if (retParact.tipo.clase == TTipo.TPENTERO) {
							//mepa.Mimprimir("LLPR", "L7");
							mepa.Mimprimir("LELN");
						} else {
							//mepa.Mimprimir("LLPR", "L11");
							mepa.Mimprimir("LECN");
						}
					} else {
						throw new Exception("Error en la funcion sentencia de procedmiento'.");
					}
				} else {
					// Llamamos al procedimento
					mepa.Mimprimir("LLPR", proc.etiqueta);
				}
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		} else {
			return retorno;
		}
	}

	// <parametro actual> :
	// <expresion>
	public TStipo parametro_actual(boolean esReferencia) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStipo expresion = expresion(esReferencia);
		return expresion;
	}

	// <sentencia estructurada> :
	// <sentencia compuesta> |
	// <sentencia if> |
	// <sentencia while>
	public TStipo sentencia_estructurada() throws Exception {
		if (TActual.tipo == Token.TPALRES_BEGIN) {
			return sentencia_compuesta();
		}
		if (TActual.tipo == Token.TPALRES_IF) {
			return sentencia_if();
		}
		if (TActual.tipo == Token.TPALRES_WHILE) {
			return sentencia_while();
		}
		throw new ExcepASintatico("Sentencia invalida.", TActual.nlinea, TActual);
	}

	// <sentencia compuesta> :
	// TPALRES_BEGIN <sentencia><sentencia compuesta'> TPALRES_END
	public TStipo sentencia_compuesta() throws Exception {
		// --
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		if (TActual.tipo == Token.TPALRES_BEGIN) {
			leerToken();
			sentencia();
			sentencia_compuestaP();
			if (TActual.tipo == Token.TPALRES_END) {
				leerToken();
				return retorno;
			} else {
				if (TActual.tipo == Token.TIDENTIFICADOR || TActual.tipo == Token.TPALRES_BEGIN) {
					throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
				} else {
					throw new ExcepASintatico("Se esperaba un identificador o la palabra reservada 'begin'.", TActual.nlinea, TActual);
				}
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'begin'.", TActual.nlinea, TActual);
		}
	}

	// <sentencia compuesta'> :
	// TPUNTO_Y_COMA<sentencia><sentencia compuesta'> |
	// lambda
	public TStipo sentencia_compuestaP() throws Exception {
		// --
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		if (TActual.tipo == Token.TPUNTO_Y_COMA) {
			leerToken();
			sentencia();
			sentencia_compuestaP();
			return retorno;
		} else {
			return retorno;
		}
	}

	// <sentencia if> :
	// TPALRES_IF <expresion> TPALRES_THEN <sentencia> <sentencia if'>
	public TStipo sentencia_if() throws Exception {
		if (TActual.tipo == Token.TPALRES_IF) {
			leerToken();
			TStipo retExpresion = expresion(false);
			// --
			if (retExpresion.tipo.clase != TTipo.TPBOOLEAN) {
				throw new ExcepASemantico("Se esperaba una expresion booleana en la condicion del if.", TActual.nlinea);
			}

			String etielse = mepa.MobtProxEti();
			String etifinelse = mepa.MobtProxEti();

			// Indica el salto condicional al else, si la expresion falso.
			mepa.Mimprimir("DSVF", etielse);
			// --
			if (TActual.tipo == Token.TPALRES_THEN) {
				leerToken();
				sentencia();
				TStipo retorno = sentencia_ifP(etielse, etifinelse);
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba la palabra reservada 'then'.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'if'.", TActual.nlinea, TActual);
		}
	}

	// <sentencia if'> :
	// TPALRES_ELSE <sentencia> |
	// lambda
	public TStipo sentencia_ifP(String etielse, String etifinelse) throws Exception {
		// --
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		// --
		if (TActual.tipo == Token.TPALRES_ELSE) {
			leerToken();
			// --
			// Indica el salto incondicional saltando el else.
			mepa.Mimprimir("DSVS", etifinelse);
			// Marca el inicio del else.
			mepa.Mimprimir(etielse, "NADA");
			// --
			sentencia();
			// --
			// Marca la sentencia que sigue al fin del else, y el if que lo
			// contiene.
			mepa.Mimprimir(etifinelse, "NADA");
			// --
			return retorno;
		} else {
			// --
			// Marca la sentencia que sigue al fin del if.
			mepa.Mimprimir(etielse, "NADA");
			// --
			return retorno;
		}
	}

	// <sentencia while> :
	// TPALRES_WHILE <expresion> TPALRES_DO <sentencia>
	public TStipo sentencia_while() throws Exception {
		// --
		String eticomwhile = mepa.MobtProxEti();
		// Marca el inicio del while
		mepa.Mimprimir(eticomwhile, "NADA");
		// --
		if (TActual.tipo == Token.TPALRES_WHILE) {
			leerToken();
			TStipo retExpresion = expresion(false);
			// --
			if (retExpresion.tipo.clase != TTipo.TPBOOLEAN) {
				throw new ExcepASemantico("Se esperaba una expresion booleana en la condicion del while", TActual.nlinea);
			}

			String etifinwhile = mepa.MobtProxEti();
			// Si es falso, saltamos fuera del while
			mepa.Mimprimir("DSVF", etifinwhile);
			// --
			if (TActual.tipo == Token.TPALRES_DO) {
				leerToken();
				TStipo retSentencia = sentencia();
				// --
				TStipo retorno = new TStipo();
				retorno.tipo = retSentencia.tipo;

				// Salta al inicio del while
				mepa.Mimprimir("DSVS", eticomwhile);
				mepa.Mimprimir(etifinwhile, "NADA");
				// --
				return retorno;
			} else {
				throw new ExcepASintatico("Se esperaba la palabra reservada 'do'.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'while'.", TActual.nlinea, TActual);
		}
	}

	/*--------------------------------------------------------------*/
	// ***********DECLARACION DE PROCEDIMIENTOS***********
	/*--------------------------------------------------------------*/
	// <declaracion de procedimiento> :
	// <encabezado de procedimiento><bloque>
	public TSintetizado declaracion_de_procedimiento() throws Exception {
		if (TActual.tipo == Token.TPALRES_PROCEDURE) {
			TSlexema retEncabezado = encabezado_de_procedimiento();
			// --
			boolean MestaEnFuncionAux = mepa.MestaEnFuncion;
			String MLexemaUnidadAux = mepa.MLexemaUnidad;
			mepa.MestaEnFuncion = false;
			mepa.MLexemaUnidad = retEncabezado.lexema;
			// --
			bloque(false);
			// --
			Procedimiento proc = (Procedimiento) TablaSimb.obtener_de_tabla(mepa.MLexemaUnidad, new int[] { Simbolo.PROCEDIMIENTO });
			// Libera el espacio reservado para los PARAMETROS de entrada y retorna
			// del procedimento.
			mepa.Mimprimir("RTPR", String.valueOf(proc.nivelL + 1), ",", String.valueOf(proc.tampf));
			mepa.MestaEnFuncion = MestaEnFuncionAux;
			mepa.MLexemaUnidad = MLexemaUnidadAux;
			// --
			return new TSintetizado();
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'procedure'.", TActual.nlinea, TActual);
		}
	}

	// <bloque> :
	// <parte de defincion de constantes>
	// <parte de defincion de tipos>
	// <parte de declaracion de variables>
	// <parte de declaracion de funciones y procedimientos>
	// <parte de sentencias>
	public TSintetizado bloque(boolean esPrograma) throws Exception {
		parte_de_definicion_de_constantes();
		parte_de_definicion_de_tipos();
		TStamreser retParDecVar = parte_de_declaracion_de_variables();
		// --
		int mtamreser = retParDecVar.Mtamreser;
		mepa.Mimprimir("RMEM", String.valueOf(mtamreser));
		String etiqueta = mepa.MobtProxEti();
		mepa.Mimprimir("DSVS", etiqueta);
		
		if (esPrograma){
			//mepa.generarEntSal();
		}
		// --
		parte_de_declaracion_de_funciones_y_procedimientos();
		// --
		mepa.Mimprimir(etiqueta, "NADA");
		// --
		parte_de_sentencias();
		// --
		// Liberar la memoria de las variables locales
		mepa.Mimprimir("LMEM", String.valueOf(mtamreser));
		// --
		return new TSintetizado();
	}

	// <encabezado de procedimiento> :
	// TPALRES_PROCEDURE TIDENTIFICADOR <encabezado de procedimiento'>
	// TPUNTO_Y_COMA
	public TSlexema encabezado_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TSlexema retorno = new TSlexema();
		if (TActual.tipo == Token.TPALRES_PROCEDURE) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				// --
				String identificador = TActual.lexema;
				// --
				leerToken();
				TSlistpform retEncabProc = encabezado_de_procedimientoP();
				// --
				if (mepa.MestaEnFuncion) {
					if (identificador.equals(mepa.MLexemaUnidad)) {
						throw new ExcepASemantico("Redefinicion de funcion", TActual.nlinea);
					}
				}
				if (!TablaSimb.existe_en_tabla(identificador, TablaSimb.TodosSimb, true)) {
					ListaParametrosForm lista = retEncabProc.lista;
					int n = lista.size();
					for (int i = 0; i < n; i++) {
						ParametroForm parametro = lista.get(i);
						TablaSimb.guardar_variable_en_tabla(parametro.lexema, parametro.tipo, TablaSimb.Mnivelact, -(n + 3 - (i + 1)), parametro.esPorValor);
					}
					String etiqueta = mepa.MobtProxEti();
					TablaSimb.guardar_procedimiento_en_tabla(identificador, lista, TablaSimb.Mnivelact - 1, etiqueta);

					retorno.lexema = identificador;

					mepa.Mimprimir(etiqueta, "ENPR", String.valueOf(TablaSimb.Mnivelact));
				} else {
					throw new ExcepASemantico("Identificador ya declarado.", TActual.nlinea);
				}
				// --
				if (TActual.tipo == Token.TPUNTO_Y_COMA) {
					leerToken();
					return retorno;
				} else {
					throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
				}
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'procedure'.", TActual.nlinea, TActual);
		}
	}

	// <encabezado de procedimiento'> :
	// TPARENTA <seccion de parametros formales><siguiente seccion de parametros
	// formales> TPARENTC |
	// lambda
	public TSlistpform encabezado_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			// --
			TSlistpform retSeccionPar = seccion_de_parametros_formales(new ListaParametrosForm());
			TSlistpform retSigSeccionPar = siguiente_seccion_de_parametros_formales(retSeccionPar.lista);
			// --
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return retSigSeccionPar;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		} else {
			// --
			TSlistpform retorno = new TSlistpform();
			retorno.lista = new ListaParametrosForm();
			// --
			return retorno;
		}
	}

	// <siguiente seccion de parametros formales> :
	// TPUNTO_Y_COMA <seccion de parametros formales><siguiente seccion de
	// parametros formales> |
	// lambda
	public TSlistpform siguiente_seccion_de_parametros_formales(ListaParametrosForm lista) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPUNTO_Y_COMA) {
			leerToken();
			// --
			TSlistpform retseccionPar = seccion_de_parametros_formales(lista);
			TSlistpform retSigseccion = siguiente_seccion_de_parametros_formales(retseccionPar.lista);
			// --
			return retSigseccion;
		} else {
			// --
			TSlistpform list = new TSlistpform();
			list.lista = lista;
			// --
			return list;
		}
	}

	// <seccion de parametros formales> :
	// <grupo de parametros> |
	// TPALRES_VAR <grupo de parametros>
	public TSlistpform seccion_de_parametros_formales(ListaParametrosForm lista) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_VAR) {
			leerToken();
			TSlistpform retGrupo = grupo_de_parametros(lista, false);
			return retGrupo;
		}
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			TSlistpform retGrupoPar = grupo_de_parametros(lista, true);
			return retGrupoPar;
		}

		throw new ExcepASintatico("Seccion de parametros formales invalida.", TActual.nlinea, TActual);
	}

	// <grupo de parametros> :
	// TIDENTIFICADOR <siguiente grupo de parametros> TDOSPUNTOS TIDENTIFICADOR
	public TSlistpform grupo_de_parametros(ListaParametrosForm lista, boolean esValor) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// --
		TSlistpform retorno = new TSlistpform();
		// --
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador1 = TActual.lexema;
			leerToken();
			// --
			ArrayList<String> listalex = new ArrayList<String>();
			listalex.add(identificador1);
			// --
			TSlistlexema retSigGrupo = siguiente_grupo_de_parametros(listalex);
			if (TActual.tipo == Token.TDOSPUNTOS) {
				leerToken();
				if (TActual.tipo == Token.TIDENTIFICADOR) {
					String identificador2 = TActual.lexema;
					leerToken();
					// --
					if (TablaSimb.existe_en_tabla(identificador2, new int[] { Simbolo.TIPO }, false)) {
						Tipo tipobt = (Tipo) TablaSimb.obtener_de_tabla(identificador2, new int[] { Simbolo.TIPO });
						ArrayList<String> lista2 = retSigGrupo.lista;
						for (String lexema : lista2) {
							lista.add(new ParametroForm(lexema, tipobt.tipo_de_estructura, esValor));
						}
					}
					retorno.lista = lista;
					// --
					return retorno;
				} else {
					throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
				}
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ':'.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
		}
	}

	// <siguiente grupo de parametros> :
	// TCOMA TIDENTIFICADOR <siguiente grupo de parametros> |
	// lambda
	public TSlistlexema siguiente_grupo_de_parametros(ArrayList<String> listaLexema) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// --
		TSlistlexema retGrupo;
		// --
		if (TActual.tipo == Token.TCOMA) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				String identificador = TActual.lexema;
				leerToken();
				// --
				if (listaLexema.contains(identificador)) {
					throw new ExcepASemantico("Identificador ya utilizado", TActual.nlinea);
				} else {
					listaLexema.add(identificador);
				}
				retGrupo = siguiente_grupo_de_parametros(listaLexema);
				// --
				return retGrupo;
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			// --
			retGrupo = new TSlistlexema();
			retGrupo.lista = listaLexema;
			// --
			return retGrupo;
		}
	}

	// <parte de definicion de constantes> :
	// TPALRES_CONST <definicion de constante> TPUNTO_Y_COMA <siguiente
	// definicion de constante> |
	// lambda
	public TSintetizado parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_CONST) {
			leerToken();
			definicion_de_constante();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				siguiente_definicion_de_constante();
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			return new TSintetizado();
		}
	}

	// <siguiente definicion de constante> :
	// <defincion de constante> TPUNTO_Y_COMA <siguiente definicion de
	// constante> |
	// lambda
	public TSintetizado siguiente_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			definicion_de_constante();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				siguiente_definicion_de_constante();
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			return new TSintetizado();
		}
	}

	// <parte de definicion de tipos> :
	// TPALRES_TYPE <definicion de tipo> TPUNTO_Y_COMA <siguiente definicion de
	// tipo> |
	// lambda
	public TSintetizado parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_TYPE) {
			leerToken();
			definicion_de_tipo();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				siguiente_definicion_de_tipo();
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			return new TSintetizado();
		}
	}

	// <siguiente definicion de tipo> :
	// <defincion de tipo> TPUNTO_Y_COMA <siguiente definicion de tipo> |
	// lambda
	public TSintetizado siguiente_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			definicion_de_tipo();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				siguiente_definicion_de_tipo();
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			// No hace nada
			return new TSintetizado();
		}
	}

	// <parte de declaracion de variables> :
	// TPALRES_VAR <declaracion de variable> TPUNTO_Y_COMA <siguiente
	// declaracion de variable> |
	// lambda
	public TStamreser parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// --
		mepa.MposVar = 0;
		TStamreser tamreser = new TStamreser();
		tamreser.Mtamreser = 0;
		// --
		if (TActual.tipo == Token.TPALRES_VAR) {
			leerToken();
			TStamvar retDeclaracion = declaracion_de_variable();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				// --
				mepa.MposVar = mepa.MposVar + retDeclaracion.Mtam_var;
				// --
				siguiente_declaracion_de_variable();
				// --
				tamreser.Mtamreser = mepa.MposVar;
				mepa.MposVar = 0;
				// --
				return tamreser;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			return tamreser;
		}
	}

	// <siguiente declaracion de variable> :
	// <declaracion de variable> TPUNTO_Y_COMA <siguiente declaracion de
	// variable> |
	// lambda
	public TSintetizado siguiente_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException, ExcepASemantico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			TStamvar retDeclaracion = declaracion_de_variable();
			// --
			// Mpos_var es global -- se esta haciendo un loop sin heredar ni
			// sintetizar.
			mepa.MposVar = mepa.MposVar + retDeclaracion.Mtam_var;
			// --
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				siguiente_declaracion_de_variable();
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			return new TSintetizado();
		}
	}

	// <parte de declaracion de funciones y procedimientos> :
	// <siguiente declaracion de procedimiento o funcion>
	public TSintetizado parte_de_declaracion_de_funciones_y_procedimientos() throws Exception {
		siguiente_declaracion_de_procedimiento_o_funcion();
		return new TSintetizado();
	}

	// <siguiente declaracion de procedimiento o funcion> :
	// <declaracion de procedimiento o funcion> TPUNTO_Y_COMA <siguiente
	// declaracion de procedimiento o funcion> |
	// lambda
	public TSintetizado siguiente_declaracion_de_procedimiento_o_funcion() throws Exception {
		if ((TActual.tipo == Token.TPALRES_FUNCTION) || (TActual.tipo == Token.TPALRES_PROCEDURE)) {
			// --
			TablaSimb.crear_nivel_lexico();
			// --
			declaracion_de_procedimiento_o_funcion();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				// --
				TablaSimb.eliminar_nivel_lexico();
				// --
				siguiente_declaracion_de_procedimiento_o_funcion();
				return new TSintetizado();
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
			}
		} else {
			return new TSintetizado();
		}
	}

	// <declaracion de procedimiento o funcion> :
	// <declaracion de procedimiento> |
	// <declaracion de funcion>
	public TSintetizado declaracion_de_procedimiento_o_funcion() throws Exception {
		if (TActual.tipo == Token.TPALRES_PROCEDURE) {
			declaracion_de_procedimiento();
			return new TSintetizado();
		}
		if (TActual.tipo == Token.TPALRES_FUNCTION) {
			declaracion_de_funcion();
			return new TSintetizado();
		}
		throw new ExcepASintatico("Declaracion de procedimiento o funcion invalida.", TActual.nlinea, TActual);
	}

	// <parte de sentencias> :
	// <sentencia compuesta>
	public TSintetizado parte_de_sentencias() throws Exception {
		sentencia_compuesta();
		return new TSintetizado();
	}

	/*--------------------------------------------------------------*/
	// ***********DECLARACION DE PROCEDIMIENTOS***********
	/*--------------------------------------------------------------*/
	// <declaracion de funcion> :
	// <encabezado de funcion><bloque>
	public TSintetizado declaracion_de_funcion() throws Exception {
		TSlexema retEncabezado = encabezado_de_funcion();
		// --
		boolean MestaFuncionAux = mepa.MestaEnFuncion;
		String MLexemaUnidadAux = mepa.MLexemaUnidad;

		mepa.MestaEnFuncion = true;
		mepa.MLexemaUnidad = retEncabezado.lexema;

		// Determina si al nombre de la funcion se le asigno un valor.
		boolean MretFuncionAux = mepa.MretFuncion;
		mepa.MretFuncion = false;
		// --
		bloque(false);
		// --
		if (!mepa.MretFuncion) {
			// No se devolvio el valor de la funcion en el bloque.
			throw new ExcepASemantico("No se le asigno ningun valor de retorno a la funcion.", TActual.nlinea);
		}
		// Restaura el valor anterior.
		mepa.MretFuncion = MretFuncionAux;
		Procedimiento proc = (Procedimiento) TablaSimb.obtener_de_tabla(mepa.MLexemaUnidad, new int[] { Simbolo.PROCEDIMIENTO });

		// Libera el espacio reservado para los PARAMETROS de entrada y retorna
		// del procedimento.
		mepa.Mimprimir("RTPR", String.valueOf(proc.nivelL + 1), ",", String.valueOf(proc.tampf));
		mepa.MestaEnFuncion = MestaFuncionAux;
		mepa.MLexemaUnidad = MLexemaUnidadAux;
		// --
		return new TSintetizado();
	}

	// <encabezado de funcion> :
	// TPALES_FUNCTION TIDENTIFICADOR <encabezado de funcion'> TDOSPUNTOS
	// TIDENTIFICADOR TPUNTO_Y_COMA
	public TSlexema encabezado_de_funcion() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TSlexema ret;
		if (TActual.tipo == Token.TPALRES_FUNCTION) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				String identificador1 = TActual.lexema;
				leerToken();
				TSlistpform retefp = encabezado_de_funcionP();
				if (TActual.tipo == Token.TDOSPUNTOS) {
					leerToken();
					if (TActual.tipo == Token.TIDENTIFICADOR) {
						String identificador2 = TActual.lexema;
						leerToken();
						if (TActual.tipo == Token.TPUNTO_Y_COMA) {
							leerToken();
							// =
							if (mepa.MestaEnFuncion) {
								if (identificador1.equals(mepa.MLexemaUnidad)) {
									throw new ExcepASemantico("Redefinicion de funcion.", TActual.nlinea);
								}
							}
							if (!TablaSimb.existe_en_tabla(identificador1, TablaSimb.TodosSimb, true)) {
								if (TablaSimb.existe_en_tabla(identificador2, new int[] { Simbolo.TIPO }, false)) {
									ListaParametrosForm lista = retefp.lista;
									int n = lista.size();
									for (int i = 0; i < n; i++) {
										ParametroForm parametro = lista.get(i);
										TablaSimb.guardar_variable_en_tabla(parametro.lexema, parametro.tipo, TablaSimb.Mnivelact, -(n + 3 - (i + 1)),
												parametro.esPorValor);
									}
									String etiqueta = mepa.MobtProxEti();

									Tipo tipobt = (Tipo) (TablaSimb.obtener_de_tabla(identificador2, new int[] { Simbolo.TIPO }));
									TablaSimb.guardar_funcion_en_tabla(identificador1, lista, tipobt.tipo_de_estructura, TablaSimb.Mnivelact - 1, -(n + 3), etiqueta);

									ret = new TSlexema();
									ret.lexema = identificador1;

									mepa.Mimprimir(etiqueta, "ENPR", String.valueOf(TablaSimb.Mnivelact));
								} else {
									throw new ExcepASemantico("Tipo no definido.", TActual.nlinea);
								}
							} else {
								throw new ExcepASemantico("Identificador ya declarado: " + identificador1 + ".", TActual.nlinea);
							}
							// =
							return ret;
						} else {
							throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
						}
					} else {
						throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
					}
				} else {
					throw new ExcepASintatico("Se esperaba el simbolo ':'.", TActual.nlinea, TActual);
				}
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'function'.", TActual.nlinea, TActual);
		}
	}

	// <encabezado de funcion'> :
	// TPARENTA <seccion de parametros formales><siguiente seccion de parametros
	// formales> TPARENTC |
	// lambda
	public TSlistpform encabezado_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		// =
		TSlistpform ret;
		ListaParametrosForm lista = new ListaParametrosForm();
		// =
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			TSlistpform retspf = seccion_de_parametros_formales(lista);
			TSlistpform retsspf = siguiente_seccion_de_parametros_formales(retspf.lista);
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return retsspf;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		} else {
			// =
			ret = new TSlistpform();
			ret.lista = lista;
			// =
			return ret;
		}
	}

	/*--------------------------------------------------------------*/
	// ***********PROGRAMAS***********
	/*--------------------------------------------------------------*/
	// <programa> :
	// TPALRES_PROGRAM TIDENTIFICADOR TPUNTO_Y_COMA <bloque> TPUNTO
	public TSintetizado programa() throws Exception {
		// =
		TablaSimb = new TablaSimbolos();
		TablaSimb.cargarPredefinidas();
		// =
		if (TActual.tipo == Token.TPALRES_PROGRAM) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				// =
				mepa.Mimprimir("INPP");
				TablaSimb.crear_nivel_lexico();
				TablaSimb.guardar_programa_en_tabla(TActual.lexema);
				// =
				leerToken();
				if (TActual.tipo == Token.TPUNTO_Y_COMA) {
					leerToken();
					bloque(true);
					if (TActual.tipo == Token.TPUNTO) {
						leerToken();
						// =
						mepa.Mimprimir("PARA");
						// =
						return new TSintetizado();
					} else {
						throw new ExcepASintatico("Se esperaba el simbolo '.'.", TActual.nlinea, TActual);
					}
				} else {
					throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
				}
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'program'.", TActual.nlinea, TActual);
		}
	}

	/*--------------------------------------------------------------*/
	// ***********GRAMATICA***********
	/*--------------------------------------------------------------*/
	// Si no lee el token que marca el final de archivo luego del ultimo
	// punto del codigo fuente, se levanta una excepcion.
	public void eof() throws ExcepASintatico {
		if (TActual.tipo == Token.TEOF) {
			return;
		}
		throw new ExcepASintatico("Se esperaba el final del archivo, pero se encontro algo mas...", TActual.nlinea, TActual);
	}
}
