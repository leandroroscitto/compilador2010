package asemantico;

import java.io.IOException;
import java.util.ArrayList;

import sintetizados.TStamvar;
import sintetizados.TStiVa;
import sintetizados.TStiva;
import sintetizados.TSlistpform;
import sintetizados.TStileCoVa;
import sintetizados.TSintetizado;
import sintetizados.TSlexema;
import sintetizados.TStamreser;
import sintetizados.TStiVaCo;
import sintetizados.TStipo;
import sintetizados.TSlistlexema;
import sintetizados.TSesMenos;
import sintetizados.TStiSi;
import sintetizados.TStifvaf;
import tablasimb.Procedimiento;
import tablasimb.Simbolo;
import tablasimb.TablaSimbolos;
import tablasimb.Tipo;
import tablasimb.Variable;
import tipos.TTipo;
import tipos.TVoid;

import alexico.Lexer;
import alexico.Token;
import auxiliares.ListaParametrosAct;
import auxiliares.ListaParametrosForm;
import auxiliares.MEPa;
import auxiliares.ParametroAct;
import auxiliares.ParametroForm;
import excepciones.ExcepALexico;
import excepciones.ExcepASemantico;
import excepciones.ExcepASintatico;

public class Parser {

	private Lexer ALexico;
	private Token TActual;

	private TablaSimbolos TablaSimb;
	private MEPa mepa = new MEPa("a.out");

	public Parser(String fileurl) throws ExcepALexico, IOException {
		ALexico = new Lexer(fileurl);
		TActual = ALexico.nextToken();
	}

	private void leerToken() throws ExcepALexico, IOException {
		TActual = ALexico.nextToken();
	}

	
	private void apilarVariable(boolean esVariable,String lexema){
		if (esVariable){
			// Si es variable, todabia no la apilo
			Variable varexp = (Variable)TablaSimb.obtener_de_tabla(lexema, new int[]{Simbolo.VARIABLE});
			if (varexp.esPorvalor){
				if (varexp.tipo_de_estructura.clase == TTipo.TPARREGLO){
					// Es un arreglo por valor
					mepa.Mimprimir("PUAR",String.valueOf(varexp.nivelL),String.valueOf(varexp.desp),String.valueOf(varexp.tipo_de_estructura.tammemoria));
				}else{
					// Es una variable por valor
					mepa.Mimprimir("APVL",String.valueOf(varexp.nivelL),String.valueOf(varexp.desp));
				}
			}else{
				if (varexp.tipo_de_estructura.clase == TTipo.TPARREGLO){
					// Es un arreglo por referencia
					mepa.Mimprimir("PUAI",String.valueOf(varexp.nivelL),String.valueOf(varexp.desp),String.valueOf(varexp.tipo_de_estructura.tammemoria));
				}else{
					// Es una variable por referencia
					mepa.Mimprimir("APVI",String.valueOf(varexp.nivelL),String.valueOf(varexp.desp));
				}
			}
		}
	}
	
	private void apilarCompente(boolean esComponente,String lexema){
		if (esComponente){
			Variable arreglo = (Variable) TablaSimb.obtener_de_tabla(lexema,new int[]{Simbolo.VARIABLE});

			if (arreglo.esPorvalor){
				mepa.Mimprimir("APAR",String.valueOf(arreglo.nivelL),String.valueOf(arreglo.desp));
			}else{
				// Es por referencia
				mepa.Mimprimir("APAI",String.valueOf(arreglo.nivelL),String.valueOf(arreglo.desp));
			}
		}
	}
	/*--------------------------------------------------------------*/
	// ***********GRAMATICA***********
	/*--------------------------------------------------------------*/
	// <signo> : TOPERMAS | TOPERMENOS
	public TSesMenos signo() throws ExcepALexico, IOException, ExcepASintatico {
		if ((TActual.tipo == Token.TOPERMAS) || (TActual.tipo == Token.TOPERMENOS)) {
			leerToken();
			return;
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
	public TStiva constante() throws ExcepALexico, IOException, ExcepASintatico {
		// Si comienza con un numero, caracter o identificador
		if ((TActual.tipo == Token.TNUMERO) || (TActual.tipo == Token.TCARACTER)
				|| (TActual.tipo == Token.TIDENTIFICADOR)) {
			leerToken();
			return;
		}
		// Si comienza con un signo
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			signo();
			constanteP();
			return;
		}
		// Si no es nada de lo de arriba
		throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
	}

	// <constante'>:
	// TNUMERO |
	// TIDENTIFICADOR
	public TStiva constanteP() throws ExcepALexico, IOException, ExcepASintatico {
		if ((TActual.tipo == Token.TNUMERO) || (TActual.tipo == Token.TIDENTIFICADOR)) {
			leerToken();
			return;
		} else {
			throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
		}
	}

	// <constante de subrango> :
	// TNUMERO |
	// TIDENTIFICADOR |
	// <signo> <constante'>
	public TStiva constante_de_subrango() throws ExcepALexico, IOException, ExcepASintatico {
		// Si comienza con un numero, caracter o identificador
		if ((TActual.tipo == Token.TNUMERO) || (TActual.tipo == Token.TIDENTIFICADOR)) {
			leerToken();
			return;
		}
		// Si comienza con un signo
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			signo();
			constanteP();
			return;
		}
		// Si no es nada de lo de arriba
		throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
	}

	// <defincion de constante> :
	// TIDENTIFICADOR TSIMBOLO_IGUAL <constante>
	public TSintetizado definicion_de_constante() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
				leerToken();
				constante();
				return;
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
	public TStiSi tipo() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR || TActual.tipo == Token.TNUMERO || TActual.tipo == Token.TCARACTER
				|| TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			tipo_simple();
			return;
		}
		if (TActual.tipo == Token.TPALRES_ARRAY) {
			tipo_arreglo();
			return;
		}
		throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
	}

	// <definicion de tipo> :
	// TIDENTIFICADOR TSIMBOLO_IGUAL <tipo>
	public TSintetizado definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
				leerToken();
				tipo();
				return;
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
	public TStipo tipo_simple() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			tipo_simpleP();
			return;
		}
		if (TActual.tipo == Token.TNUMERO) {
			leerToken();
			if (TActual.tipo == Token.TDOBLEPUNTO) {
				leerToken();
				constante_de_subrango();
				return;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo '..'.", TActual.nlinea, TActual);
			}
		}
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			leerToken();
			tipo_simple_de_signo();
			return;
		}
		throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
	}

	// <tipo simple de signo>:
	// TNUMERO TDOBLEPUNTO <constante de subrango> |
	// TIDENTIFICADOR TDOBLEPUNTO <constante de subrango> |
	public TStipo tipo_simple_de_signo(boolean esMenos) throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TNUMERO || TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			if (TActual.tipo == Token.TDOBLEPUNTO) {
				leerToken();
				constante_de_subrango();
				return;
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
	public TStifvaf tipo_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TDOBLEPUNTO) {
			leerToken();
			constante_de_subrango();
			return;
		} else {
			return;
		}
	}

	// <tipo arreglo> :
	// TPALRES_ARRAY TCORA <tipo simple> TCORC TPALRES_OF <tipo simple>
	public TStipo tipo_arreglo() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TPALRES_ARRAY) {
			leerToken();
			if (TActual.tipo == Token.TCORA) {
				leerToken();
				tipo_simple();
				if (TActual.tipo == Token.TCORC) {
					leerToken();
					if (TActual.tipo == Token.TPALRES_OF) {
						leerToken();
						tipo_simple();
						return;
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
	public TStamvar declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			siguiente_identificador();
			if (TActual.tipo == Token.TDOSPUNTOS) {
				leerToken();
				tipo();
				return;
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
	public TSlistlexema siguiente_identificador(ArrayList<String> lista) throws ExcepALexico, IOException,
			ExcepASintatico {
		if (TActual.tipo == Token.TCOMA) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				leerToken();
				siguiente_identificador();
				return;
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			return;
		}
	}

	// <variable'> :
	// TCORA <expresion> TCORC |
	// lambda
	public TStiCo variableP(String lexema) throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TCORA) {
			leerToken();
			expresion();
			if (TActual.tipo == Token.TCORC) {
				leerToken();
				return;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ']'.", TActual.nlinea, TActual);
			}
		} else {
			return;
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
	public TStileCoVa factor() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			factorP();
			return;
		}
		if (TActual.tipo == Token.TNUMERO || TActual.tipo == Token.TCARACTER) {
			leerToken();
			return;
		}
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			expresion();
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		}
		if (TActual.tipo == Token.TOPER_NOT) {
			leerToken();
			factor();
			return;
		}

		throw new ExcepASintatico("Factor invalido.", TActual.nlinea, TActual);
	}

	// <factor'> :
	// <variable'> |
	// <designador de funcion'> |
	// lambda
	public TStiVaCo factorP(String lexema) throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TCORA) {
			variableP();
			return;
		}
		if (TActual.tipo == Token.TPARENTA) {
			designador_de_funcionP();
			return;
		}
		// En el caso de lambda
		return;
	}

	// <termino> :
	// <factor><termino'>
	public TStileCoVa termino() throws IOException, ExcepALexico, ExcepASintatico {
		factor();
		terminoP();
		return;
	}

	// <termino'> :
	// <operador de multiplicacion><factor><termino'> |
	// lambda
	public TStiVaCo terminoP(TTipo tipof, boolean esComponente, boolean esVariable, String lexema) throws ExcepALexico,
			IOException, ExcepASintatico {
		if (TActual.tipo == Token.TOPERMULT || TActual.tipo == Token.TOPERDIV || TActual.tipo == Token.TOPER_AND) {
			operador_de_multiplicacion();
			factor();
			terminoP();
			return;
		} else {
			return;
		}
	}

	// <expresion simple> :
	// <termino><expresion simple'> |
	// <signo><termino><expresion simple'>
	public TStileCoVa expresion_simple() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR || TActual.tipo == Token.TNUMERO || TActual.tipo == Token.TCARACTER
				|| TActual.tipo == Token.TPARENTA || TActual.tipo == Token.TOPER_NOT) {
			termino();
			expresion_simpleP();
			return;
		}
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS) {
			signo();
			termino();
			expresion_simpleP();
			return;
		}

		throw new ExcepASintatico("Expresion invalida.", TActual.nlinea, TActual);
	}

	// <expresion simple'> :
	// <operador de suma><termino><expresion simple'> |
	// lambda
	public TStiVaCo expresion_simpleP(TTipo tipof, boolean esComponente, boolean esVariable, String lexema)
			throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TOPERMAS || TActual.tipo == Token.TOPERMENOS || TActual.tipo == Token.TOPER_OR) {
			operador_de_suma();
			termino();
			expresion_simpleP();
			return;
		} else {
			return;
		}
	}

	// <expresion> :
	// <expresion simple><expresion'>
	public TStileCoVa expresion() throws ExcepALexico, IOException, ExcepASintatico {
		expresion_simple();
		expresionP();
		return;
	}

	// <expresion'> :
	// <operador de relacion><expresion simple> |
	// lambda
	public TStiVaCo expresionP(TTipo tipof, boolean esComponente, boolean esVariable, String lexema)
			throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TSIMBOLO_IGUAL || TActual.tipo == Token.TSIMBOLO_DISTINTO
				|| TActual.tipo == Token.TSIMBOLO_MAYOR || TActual.tipo == Token.TSIMBOLO_MAYORIGUAL
				|| TActual.tipo == Token.TSIMBOLO_MENOR || TActual.tipo == Token.TSIMBOLO_MENORIGUAL) {
			operador_de_relacion();
			expresion_simple();
			return;
		} else {
			return;
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
			leerToken();
			return;
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
			leerToken();
			return;
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
		if (TActual.tipo == Token.TSIMBOLO_IGUAL || TActual.tipo == Token.TSIMBOLO_DISTINTO
				|| TActual.tipo == Token.TSIMBOLO_MAYOR || TActual.tipo == Token.TSIMBOLO_MAYORIGUAL
				|| TActual.tipo == Token.TSIMBOLO_MENOR || TActual.tipo == Token.TSIMBOLO_MENORIGUAL) {
			leerToken();
			return;
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
	public TStipo designador_de_funcionP(String lexema) throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			parametro_actual();
			siguiente_parametro_actual();
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		} else {
			return;
		}
	}

	// <siguiente parametro actual> :
	// TCOMA <parametro actual><siguiente parametro actual> |
	// lambda
	public TSlistpact siguiente_parametro_actual(ListaParametrosAct lista, String lexema, int posicion)
			throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TCOMA) {
			leerToken();
			parametro_actual();
			siguiente_parametro_actual();
			return;
		} else {
			return;
		}
	}

	/*--------------------------------------------------------------*/
	// ***********SENTENCIAS***********
	/*--------------------------------------------------------------*/
	// <sentencia> :
	// <sentencia simple> |
	// <sentencia estructurada> |
	// lambda
	public TStipo sentencia() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			sentencia_simple();
			return;
		}
		if ((TActual.tipo == Token.TPALRES_BEGIN) || (TActual.tipo == Token.TPALRES_IF)
				|| (TActual.tipo == Token.TPALRES_WHILE)) {
			sentencia_estructurada();
			return;
		}
		return;
	}

	// <sentencia simple> :
	// TIDENTIFICADOR <sentencia simple'>
	public TStipo sentencia_simple() throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			leerToken();
			sentencia_simpleP();
			return;
		} else {
			throw new ExcepASintatico("Sentencia invalida.", TActual.nlinea, TActual);
		}
	}

	// <sentencia simple'> :
	// <sentencia de asignacion'> |
	// <sentencia de procedimiento'> |
	// lambda
	public TStipo sentencia_simpleP(String lexema) throws ExcepALexico, IOException, ExcepASintatico {
		if ((TActual.tipo == Token.TCORA) || (TActual.tipo == Token.TASIGN)) {
			sentencia_de_asignacionP();
			return;
		}
		if (TActual.tipo == Token.TPARENTA) {
			sentencia_de_procedimientoP();
			return;
		}
		// throw new
		// ExcepASintatico("Se esperaba una sentencia de asignacion o una sentencia de procedimiento validos.",
		// TActual.nlinea, TActual);
		return;
	}

	// <sentencia de asignacion'> :
	// <variable'> TASIGN <expresion> |
	// TASIGN <expresion>
	public TStipo sentencia_de_asignacionP(String lexema) throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TCORA) {
			variableP();
		}
		if (TActual.tipo == Token.TASIGN) {
			leerToken();
			expresion();
			return;
		} else {
			throw new ExcepASintatico("Se esperaba el simbolo ':='.", TActual.nlinea, TActual);
		}
	}

	// <sentencia de procedimiento'> :
	// TPARENTA <parametro actual><siguiente parametro actual> TPARENTC
	public TStipo sentencia_de_procedimientoP(String lexema) throws ExcepALexico, IOException, ExcepASintatico {
		if (TActual.tipo == Token.TPARENTA) {
			leerToken();
			parametro_actual();
			siguiente_parametro_actual();
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		}
	}

	// <parametro actual> :
	// <expresion>
	public TStiVa parametro_actual(String lexema, int posicion) throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TStileCoVa expresion = expresion();
		//--
		TStiVa retorno = new TStiVa();
		retorno.tipo = expresion.tipo;
		retorno.esVariable = expresion.esVariable;
		if(TablaSimb.existe_en_tabla(lexema, new int[]{Simbolo.PROCEDIMIENTO}, false)){
			Procedimiento proc = (Procedimiento) TablaSimb.obtener_de_tabla(lexema, new int[]{Simbolo.PROCEDIMIENTO});
			
			if(posicion < proc.cpf){
				
				if(expresion.tipo.comparar(proc.tpf[posicion])){
					
					if((expresion.esVariable) && (!expresion.esCompomente)){
						
						Variable var = (Variable) TablaSimb.obtener_de_tabla(expresion.lexema, new int[]{Simbolo.VARIABLE});
						
						if(proc.ppf[posicion]){
							
							if(var.esPorvalor){
								
								if(var.tipo_de_estructura.clase == TTipo.TPARREGLO){
									mepa.Mimprimir("PUAR",String.valueOf(var.nivelL),String.valueOf(var.desp),String.valueOf(var.tipo_de_estructura.tammemoria));
								}else{
									mepa.Mimprimir("APVL",String.valueOf(var.nivelL),String.valueOf(var.desp));
								}
							}else{
								if(var.tipo_de_estructura.clase == TTipo.TPARREGLO){
									mepa.Mimprimir("PUAI",String.valueOf(var.nivelL),String.valueOf(var.desp),String.valueOf(var.tipo_de_estructura.tammemoria));
								}else{
									mepa.Mimprimir("APVI",String.valueOf(var.nivelL),String.valueOf(var.desp));
								}
							}
						}else{
							if(var.esPorvalor){
								mepa.Mimprimir("APDR",String.valueOf(var.nivelL),String.valueOf(var.desp));

							}else{
								mepa.Mimprimir("APVL",String.valueOf(var.nivelL),String.valueOf(var.desp));
							}
						}
					}else if(expresion.esVariable && expresion.esCompomente){
						Variable arreglo = (Variable) TablaSimb.obtener_de_tabla(expresion.lexema, new int[]{Simbolo.VARIABLE});
						
						if(proc.ppf[posicion]){
							if(arreglo.esPorvalor){
								mepa.Mimprimir("APAR",String.valueOf(arreglo.nivelL),String.valueOf(arreglo.desp));
							}else{
								mepa.Mimprimir("APAI",String.valueOf(arreglo.nivelL),String.valueOf(arreglo.desp));
							}
						}else{
							if(arreglo.esPorvalor){
								mepa.Mimprimir("APDR",String.valueOf(arreglo.nivelL),String.valueOf(arreglo.desp));
								mepa.Mimprimir("SUMA");
							}else{
								mepa.Mimprimir("APVL",String.valueOf(arreglo.nivelL),String.valueOf(arreglo.desp));
								mepa.Mimprimir("SUMA");								
							}
						}
					}else{
						if(!proc.ppf[posicion]){
							throw new ExcepASemantico("Se esperaba una variable por referencia y se encontro una expresion", TActual.nlinea);
						}
					}
				}else{
					throw new ExcepASemantico("Error de tipo en el parametro "+posicion+".", TActual.nlinea);
				}
			}else{
				throw new ExcepASemantico("La cantidad de parametros actuales supera la cantidad de parametros formales.", TActual.nlinea);
			}
		}else{
			throw new ExcepASemantico("Procedimiento no declarado.", TActual.nlinea);
		}
		//--
		return retorno;
	}

	// <sentencia estructurada> :
	// <sentencia compuesta> |
	// <sentencia if> |
	// <sentencia while>
	public TStipo sentencia_estructurada() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
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
	public TStipo sentencia_compuesta() throws ExcepALexico, IOException, ExcepASintatico {
		//--
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		//--
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
					throw new ExcepASintatico("Se esperaba un identificador o la palabra reservada 'begin'.",
							TActual.nlinea, TActual);
				}
			}
		} else {
			throw new ExcepASintatico("Se esperaba la palabra reservada 'begin'.", TActual.nlinea, TActual);
		}
	}

	// <sentencia compuesta'> :
	// TPUNTO_Y_COMA<sentencia><sentencia compuesta'> |
	// lambda
	public TStipo sentencia_compuestaP() throws ExcepALexico, IOException, ExcepASintatico {
		//--
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		//--
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
	public TStipo sentencia_if() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_IF) {
			leerToken();
			TStileCoVa retExpresion = expresion();
			//--
			if(retExpresion.tipo.clase != TTipo.TPBOOLEAN){
				throw new ExcepASemantico("Se esperaba una expresion booleana en la condicion del if.", TActual.nlinea);
			}
			
			if(retExpresion.esCompomente){
				apilarCompente(retExpresion.esCompomente, retExpresion.lexema);
			}else{
				apilarVariable(retExpresion.esVariable, retExpresion.lexema);
			}
			
			String etielse = mepa.MobtProxEti();
			String etifinelse = mepa.MobtProxEti();
			
			mepa.Mimprimir("DSVF",etielse);
			//--
			if (TActual.tipo == Token.TPALRES_THEN) {
				leerToken();
				sentencia();
				TStipo retorno = sentencia_ifP(etielse,etifinelse);
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
	public TStipo sentencia_ifP(String etielse, String etifinelse) throws ExcepALexico, IOException, ExcepASintatico {
		//--
		TStipo retorno = new TStipo();
		retorno.tipo = new TVoid();
		//--
		if (TActual.tipo == Token.TPALRES_ELSE) {
			leerToken();
			//--
			mepa.Mimprimir("DSVS",etifinelse);
			mepa.Mimprimir(etielse,"NADA");
			//--
			sentencia();
			//--
			mepa.Mimprimir(etifinelse,"NADA");
			//--
			return retorno;
		} else {
			//--
			mepa.Mimprimir(etielse,"NADA");
			//--
			return retorno;
		}
	}

	// <sentencia while> :
	// TPALRES_WHILE <expresion> TPALRES_DO <sentencia>
	public TStipo sentencia_while() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		//--
		String eticomwhile = mepa.MobtProxEti();
		mepa.Mimprimir(eticomwhile,"NADA");
		//--
		if (TActual.tipo == Token.TPALRES_WHILE) {
			leerToken();
			TStileCoVa retExpresion = expresion();
			//--
			if(retExpresion.tipo.clase != TTipo.TPBOOLEAN){
				throw new ExcepASemantico("Se esperaba una expresion booleana en la condicion del while", TActual.nlinea);
			}
			
			if(retExpresion.esCompomente){
				apilarCompente(retExpresion.esCompomente, retExpresion.lexema);
			}else{
				apilarVariable(retExpresion.esVariable, retExpresion.lexema);
			}
			
			String etifinwhile = mepa.MobtProxEti();
			mepa.Mimprimir("DSVF",etifinwhile);
			//--
			if (TActual.tipo == Token.TPALRES_DO) {
				leerToken();
				TStipo retSentencia = sentencia();
				//--
				TStipo retorno = new TStipo();
				retorno.tipo = retSentencia.tipo;
				
				mepa.Mimprimir("DSVS",eticomwhile);
				mepa.Mimprimir(etifinwhile,"NADA");
				//--
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
	public TSintetizado declaracion_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_PROCEDURE) {
			TSlexema retEncabezado = encabezado_de_procedimiento();
			//--
			boolean MestaEnFuncionAux = mepa.MestaEnFuncion;
			String MLexemaUnidadAux = mepa.MLexemaUnidad;
			mepa.MestaEnFuncion = false;
			mepa.MLexemaUnidad = retEncabezado.lexema; 
			//--
			bloque();
			//--
			Procedimiento proc = (Procedimiento)TablaSimb.obtener_de_tabla(mepa.MLexemaUnidad, new int[]{Simbolo.PROCEDIMIENTO});
			mepa.Mimprimir("RTPR",String.valueOf(proc.nivelL+1),String.valueOf(proc.tampf));
			mepa.MestaEnFuncion = MestaEnFuncionAux;
			mepa.MLexemaUnidad = MLexemaUnidadAux;
			//--
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
	public TSintetizado bloque() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		parte_de_definicion_de_constantes();
		parte_de_definicion_de_tipos();
		TStamreser retParDecVar = parte_de_declaracion_de_variables();
		//--
		int mtamreser = retParDecVar.Mtamreser;
		mepa.Mimprimir("RMEM",String.valueOf(mtamreser));
		String etiqueta = mepa.MobtProxEti();
		mepa.Mimprimir("DSVS",etiqueta);
		//--
		parte_de_declaracion_de_funciones_y_procedimientos();
		//--
		mepa.Mimprimir(etiqueta,"NADA");
		//--
		parte_de_sentencias();
		//--
		mepa.Mimprimir("LMEM",String.valueOf(mtamreser));
		//--
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
				//--
				String identificador = TActual.lexema;
				//--
				leerToken();
				TSlistpform retEncabProc = encabezado_de_procedimientoP();
				//--
				if(mepa.MestaEnFuncion){
					if(identificador.equals(mepa.MLexemaUnidad)){
						throw new ExcepASemantico("Redefinicion de funcion",TActual.nlinea);
					}
				}
				if(!TablaSimb.existe_en_tabla(identificador, new int[]{Simbolo.PROCEDIMIENTO}, true)){
					ListaParametrosForm lista = retEncabProc.lista;
					int n = lista.size();
					for(int i=0;i<n;i++){
						ParametroForm parametro = lista.get(i);
						TablaSimb.guardar_variable_en_tabla(parametro.lexema, parametro.tipo, TablaSimb.Mnivelact, -(n+3-(i+1)), parametro.esPorValor);
					}
					String etiqueta = mepa.MobtProxEti();
					TablaSimb.guardar_procedimiento_en_tabla(identificador, lista, TablaSimb.Mnivelact-1, etiqueta);
					
					retorno.lexema = identificador;
					
					mepa.Mimprimir(etiqueta,"ENPR",String.valueOf(TablaSimb.Mnivelact));
				}
				//--
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
			//--
			TSlistpform retSeccionPar = seccion_de_parametros_formales(new ListaParametrosForm());
			TSlistpform retSigSeccionPar = siguiente_seccion_de_parametros_formales(retSeccionPar.lista);
			//--
			if (TActual.tipo == Token.TPARENTC) {
				leerToken();
				return retSigSeccionPar;
			} else {
				throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
			}
		} else {
			//--
			TSlistpform retorno = new TSlistpform();
			retorno.lista = new ListaParametrosForm();
			//--
			return retorno;
		}
	}

	// <siguiente seccion de parametros formales> :
	// TPUNTO_Y_COMA <seccion de parametros formales><siguiente seccion de
	// parametros formales> |
	// lambda
	public TSlistpform siguiente_seccion_de_parametros_formales(ListaParametrosForm lista) throws ExcepALexico,
			IOException, ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPUNTO_Y_COMA) {
			leerToken();
			//--
			TSlistpform retseccionPar = seccion_de_parametros_formales(lista);
			TSlistpform retSigseccion = siguiente_seccion_de_parametros_formales(retseccionPar.lista);
			//--
			return retSigseccion;
		} else {
			//--
			TSlistpform list = new TSlistpform();
			list.lista = lista;
			//--
			return list;
		}
	}

	// <seccion de parametros formales> :
	// <grupo de parametros> |
	// TPALRES_VAR <grupo de parametros>
	public TSlistpform seccion_de_parametros_formales(ListaParametrosForm lista) throws ExcepALexico, IOException,
			ExcepASintatico, ExcepASemantico {
		if (TActual.tipo == Token.TPALRES_VAR) {
			leerToken();
			TSlistpform retGrupo = grupo_de_parametros(lista,false);
			return retGrupo;
		}
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			TSlistpform retGrupoPar = grupo_de_parametros(lista,true);
			return retGrupoPar;
		}

		throw new ExcepASintatico("Seccion de parametros formales invalida.", TActual.nlinea, TActual);
	}

	// <grupo de parametros> :
	// TIDENTIFICADOR <siguiente grupo de parametros> TDOSPUNTOS TIDENTIFICADOR
	public TSlistpform grupo_de_parametros(ListaParametrosForm lista, boolean esValor) throws ExcepALexico, IOException,
			ExcepASintatico, ExcepASemantico {
		//--
		TSlistpform retorno = new TSlistpform();
		//--
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			String identificador1 = TActual.lexema;
			leerToken();
			//--
			ArrayList<String> listalex = new ArrayList<String>();
			listalex.add(identificador1);
			//--
			TSlistlexema retSigGrupo = siguiente_grupo_de_parametros(listalex);
			if (TActual.tipo == Token.TDOSPUNTOS) {
				leerToken();
				if (TActual.tipo == Token.TIDENTIFICADOR) {
					String identificador2 = TActual.lexema;
					leerToken();
					//--
					if(TablaSimb.existe_en_tabla(identificador2, new int[]{Simbolo.TIPO}, false)){
						Tipo tipobt = (Tipo) TablaSimb.obtener_de_tabla(identificador2, new int[]{Simbolo.TIPO});
						ArrayList<String> lista2 = retSigGrupo.lista;
						for(String lexema:lista2){
							lista.add(new ParametroForm(lexema, tipobt.tipo_de_estructura, esValor));
						}
					}
					retorno.lista = lista;
					//--
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
	public TSlistlexema siguiente_grupo_de_parametros(ArrayList<String> listaLexema) throws ExcepALexico, IOException,
			ExcepASintatico, ExcepASemantico {
		//--
		TSlistlexema retGrupo;
		//--
		if (TActual.tipo == Token.TCOMA) {
			leerToken();
			if (TActual.tipo == Token.TIDENTIFICADOR) {
				String identificador = TActual.lexema;
				leerToken();
				//--
				if(listaLexema.contains(identificador)){
					throw new ExcepASemantico("Identificador ya utilizado", TActual.nlinea);
				}else{
					listaLexema.add(identificador);
				}
				retGrupo = siguiente_grupo_de_parametros(listaLexema);
				//--
				return retGrupo;
			} else {
				throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
			}
		} else {
			//--
			retGrupo = new TSlistlexema();
			retGrupo.lista = listaLexema;
			//--
			return retGrupo;
		}
	}

	// <parte de definicion de constantes> :
	// TPALRES_CONST <definicion de constante> TPUNTO_Y_COMA <siguiente
	// definicion de constante> |
	// lambda
	public TSintetizado parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico {
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
	public TSintetizado siguiente_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException {
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
	public TSintetizado parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico {
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
	public TSintetizado siguiente_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException {
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
			return new TSintetizado();
		}
	}

	// <parte de declaracion de variables> :
	// TPALRES_VAR <declaracion de variable> TPUNTO_Y_COMA <siguiente
	// declaracion de variable> |
	// lambda
	public TStamreser parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico {
		//--
		mepa.MposVar = 0;	
		TStamreser tamreser = new TStamreser();
		tamreser.Mtamreser = 0;
		//--
		if (TActual.tipo == Token.TPALRES_VAR) {
			leerToken();
			TStamvar retDeclaracion = declaracion_de_variable();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				//--
				mepa.MposVar = mepa.MposVar + retDeclaracion.Mtam_var;
				//--
				siguiente_declaracion_de_variable();
				//--
				tamreser.Mtamreser = mepa.MposVar;
				mepa.MposVar = 0;
				//--
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
	public TSintetizado siguiente_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
		if (TActual.tipo == Token.TIDENTIFICADOR) {
			TStamvar retDeclaracion = declaracion_de_variable();
			//--
			mepa.MposVar = mepa.MposVar + retDeclaracion.Mtam_var; 
			//--
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
	public TSintetizado parte_de_declaracion_de_funciones_y_procedimientos() throws ExcepALexico, IOException,
			ExcepASintatico, ExcepASemantico {
		siguiente_declaracion_de_procedimiento_o_funcion();
		return new TSintetizado();
	}

	// <siguiente declaracion de procedimiento o funcion> :
	// <declaracion de procedimiento o funcion> TPUNTO_Y_COMA <siguiente
	// declaracion de procedimiento o funcion> |
	// lambda
	public TSintetizado siguiente_declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException,
			ExcepASintatico, ExcepASemantico {
		if ((TActual.tipo == Token.TPALRES_FUNCTION) || (TActual.tipo == Token.TPALRES_PROCEDURE)) {
			//--
			TablaSimb.crear_nivel_lexico();
			//--
			declaracion_de_procedimiento_o_funcion();
			if (TActual.tipo == Token.TPUNTO_Y_COMA) {
				leerToken();
				//--
				TablaSimb.eliminar_nivel_lexico();
				//--
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
	public TSintetizado declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
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
	public TSintetizado parte_de_sentencias() throws ExcepALexico, IOException, ExcepASintatico , ExcepASemantico{
		sentencia_compuesta();
		return new TSintetizado();
	}

	/*--------------------------------------------------------------*/
	// ***********DECLARACION DE PROCEDIMIENTOS***********
	/*--------------------------------------------------------------*/
	// <declaracion de funcion> :
	// <encabezado de funcion><bloque>
	public TSintetizado declaracion_de_funcion() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
		TSlexema retEncabezado = encabezado_de_funcion();
		//--
		boolean MestaFuncionAux = mepa.MestaEnFuncion;
		String MLexemaUnidadAux = mepa.MLexemaUnidad;
		
		mepa.MestaEnFuncion = true;
		mepa.MLexemaUnidad = retEncabezado.lexema;
		
		boolean MretFuncionAux = mepa.MretFuncion;
		mepa.MretFuncion = false;
		//--
		bloque();
		//--
		if(!mepa.MretFuncion){
			throw new ExcepASemantico("No se le asigno ningun valor de retorno a la funcion.", TActual.nlinea);
		}
		mepa.MretFuncion = MretFuncionAux;
		Procedimiento proc = (Procedimiento) TablaSimb.obtener_de_tabla(mepa.MLexemaUnidad, new int[]{Simbolo.PROCEDIMIENTO});
		
		mepa.Mimprimir("RTPR",String.valueOf(proc.nivelL+1),String.valueOf(proc.tampf));
		mepa.MestaEnFuncion = MestaFuncionAux;
		mepa.MLexemaUnidad = MLexemaUnidadAux;
		//--
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
							if (!TablaSimb.existe_en_tabla(identificador1, new int[] { Simbolo.FUNCION }, true)) {
								if (TablaSimb.existe_en_tabla(identificador2, new int[] { Simbolo.TIPO }, false)) {
									ListaParametrosForm lista = retefp.lista;
									int n = lista.size();
									for (int i = 0; i < n; i++) {
										ParametroForm parametro = lista.get(i);
										TablaSimb.guardar_variable_en_tabla(parametro.lexema, parametro.tipo,
												TablaSimb.Mnivelact, -(n + 3 - (i + 1)), parametro.esPorValor);
									}
									String etiqueta = mepa.MobtProxEti();

									Tipo tipobt = (Tipo) (TablaSimb.obtener_de_tabla(identificador2, new int[] { Simbolo.TIPO }));
									TablaSimb.guardar_funcion_en_tabla(identificador1, lista, tipobt.tipo_de_estructura,
											TablaSimb.Mnivelact - 1, -(n + 3), etiqueta);

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
	public TSintetizado programa() throws ExcepALexico, IOException, ExcepASintatico, ExcepASemantico {
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
				mepa.generarEntSal();
				// =
				leerToken();
				if (TActual.tipo == Token.TPUNTO_Y_COMA) {
					leerToken();
					bloque();
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
		throw new ExcepASintatico("Se esperaba el final del archivo, pero se encontro algo mas...", TActual.nlinea,
				TActual);
	}
}
