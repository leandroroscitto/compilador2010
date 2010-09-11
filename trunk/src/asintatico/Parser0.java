package asintatico;

import java.io.IOException;

import alexico.Lexer;
import alexico.Token;
import excepciones.ExcepALexico;
import excepciones.ExcepASintatico;

public class Parser0 {

    private Lexer ALexico;
    private Token TActual;
    private int debug = 0;

    // Crea una nueva instancia de un analizador sintactico
    public Parser0(String fileurl) throws ExcepALexico, IOException {
        ALexico = new Lexer(fileurl);
        TActual = ALexico.nextToken();
    }

    // Lee un nuevo token del analizador lexico
    private void leerToken() throws ExcepALexico, IOException {
        TActual = ALexico.nextToken();

        if (debug == 1) {
            System.out.println(TActual.lexema);
        }
    }

    // <signo> ::= TOPERMAS | TOPERMENOS
    public boolean signo() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un signo + o -.", TActual.nlinea,
                TActual);
    }

    // <constante> ::= TNUMERO | TIDENTIFICADOR | TCARACTER
    //           | TOPERMAS<constante'> | TOPERMENOS<constante'>
    public boolean constante() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TIDENTIFICADOR)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            return true;
        }
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            constanteP();
            return true;
        }

        throw new ExcepASintatico("Se esperaba una constante.", TActual.nlinea,
                TActual);
    }

    // <constante'> ::= TNUMERO | TIDENTIFICADOR
    public boolean constanteP() throws ExcepALexico, IOException,
            ExcepASintatico {
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TIDENTIFICADOR)) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico(
                    "Se esperaba un numero o identificador para la constante.",
                    TActual.nlinea, TActual);
        }
    }

    // <definicion de constante> ::= TIDENTIFICADOR TSIMBOLO_IGUAL <constante>
    public boolean definicion_de_constante() throws ExcepALexico, IOException,
            ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
                leerToken();
                constante();
                return true;
            } else {
                throw new ExcepASintatico(
                        "Error en la definicion de la constante, se esperaba un simbolo '='.",
                        TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico(
                    "Error en la definicion de la constante, se esperaba un identificador.",
                    TActual.nlinea, TActual);
        }
    }

    // <definicion de constante'> ::= TSIMBOLO_IGUAL <constante>
    public boolean definicion_de_constanteP() throws ExcepALexico, IOException,
            ExcepASintatico {
        if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
            leerToken();
            constante();
            return true;
        } else {
            throw new ExcepASintatico(
                    "Error en la definicion de la constante, se esperaba un simbolo '='.",
                    TActual.nlinea, TActual);
        }
    }

    // <tipo simple> ::= TIDENTIFICADOR <tipo simple'> | TNUMERO <tipo subrango'>
    // | <TOPERMAS><constante'><tipo subrango'> | <TOPERMENOS><constante'><tipo subrango'>
    // | TCARACTER <tipo subrango'>
    public boolean tipo_simple() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            tipo_simpleP();
            return true;
        }
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            tipo_subrangoP();
            return true;
        }
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            constanteP();
            tipo_subrangoP();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_ARRAY) {
            leerToken();
            tipo_arregloP();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un tipo simple.", TActual.nlinea, TActual);
    }

    // <tipo simple'> ::= TDOBLEPUNTO <constante> | lambda
    public boolean tipo_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TDOBLEPUNTO) {
            leerToken();
            constante();
            return true;
        } else {
            return true;
        }
    }

    // <tipo subrango'> ::= TDOBLEPUNTO <constante>
    public boolean tipo_subrangoP() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TDOBLEPUNTO) {
            leerToken();
            constante();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un '..' para el subrango.", TActual.nlinea, TActual);
    }

    // <tipo arreglo'> ::= TCORA <tipo simple> TCORC TPALRES_OF <tipo simple>
    public boolean tipo_arregloP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCORA) {
            leerToken();
            tipo_simple();
            if (TActual.tipo == Token.TCORC) {
                leerToken();
                if (TActual.tipo == Token.TPALRES_OF) {
                    leerToken();
                    tipo_simple();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba la palabra reservada 'of' para el arreglo.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un ']' para el arreglo.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un '[' para el arreglo.", TActual.nlinea, TActual);
        }
    }

    // <tipo> ::= TIDENTIFICADOR <tipo simple'> | TNUMERO <tipo subrango'>
    // | TOPERMAS <constante'><tipo subrango'> | TOPERMENOS <constante'><tipo subrango'>
    // | TCARACTER <tipo subrango'> | TPALRES_ARRAY <tipo arreglo'>
    public boolean tipo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            tipo_simpleP();
            return true;
        }
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            tipo_subrangoP();
            return true;
        }
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            constanteP();
            tipo_subrangoP();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_ARRAY) {
            leerToken();
            tipo_arregloP();
            return true;
        }

        throw new ExcepASintatico("Se espera un tipo.", TActual.nlinea, TActual);
    }

    // <definicion de tipo> ::= TIDENTIFICADOR TSIMBOLO_IGUAL <tipo>
    public boolean definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
                leerToken();
                tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un simbolo igual en la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se espera un identificador en la definicion de tipo.", TActual.nlinea, TActual);
        }
    }

    // <definicion de tipo'> ::= TSIMBOLO_IGUAL <tipo>
    public boolean definicion_de_tipoP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
            leerToken();
            tipo();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un simbolo igual en la definicion de tipo.", TActual.nlinea, TActual);
        }
    }

    // <declaracion de variable> ::= TIDENTIFICADOR <siguiente identificador> TDOSPUNTOS <tipo>
    public boolean declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            siguiente_identificador();
            if (TActual.tipo == Token.TDOSPUNTOS) {
                leerToken();
                tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba ':' en la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un identificador en la declaracion de variable.", TActual.nlinea, TActual);
        }
    }

    // <declaracion de variable'> ::= <siguiente identificador> TDOSPUNTOS <tipo>
    public boolean declaracion_de_variableP() throws ExcepASintatico, ExcepALexico, IOException {
        siguiente_identificador();
        if (TActual.tipo == Token.TDOSPUNTOS) {
            leerToken();
            tipo();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba ':' en la declaracion de variable.", TActual.nlinea, TActual);
        }
    }

    // <siguiente identificador> ::= TCOMA TIDENTIFICADOR <siguiente identificador> | lambda
    public boolean siguiente_identificador() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCOMA) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                siguiente_identificador();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <variable> ::= TIDENTIFICADOR <variable'>
    public boolean variable() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            variableP();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un identificador para la variable.", TActual.nlinea, TActual);
        }
    }

    // <variable'> ::= TCORA <expresion> TCORC | lambda
    public boolean variableP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCORA) {
            leerToken();
            expresion();
            if (TActual.tipo == Token.TCORC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ']' en la variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <variable''> ::= <expresion> TCORC
    public boolean variablePP() throws ExcepALexico, IOException, ExcepASintatico {
        expresion();
        if (TActual.tipo == Token.TCORC) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un ']' en la variable.", TActual.nlinea, TActual);
        }
    }

    // <factor> ::= TIDENTIFICADOR <factor'> | TNUMERO | TCARACTER
    public boolean factor() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            factorP();
            return true;
        }
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            return true;
        }
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            expresion();
            if (TActual.tipo == Token.TPARENTC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ')' en el factor.", TActual.nlinea, TActual);
            }
        }
        if (TActual.tipo == Token.TOPER_NOT) {
            leerToken();
            factor();
            return true;
        }

        throw new ExcepASintatico("Factor mal formado.", TActual.nlinea, TActual);
    }

    // <factor'> ::= TCORA <variable''> | TPARENTA <designador de funcion''> | lambda
    public boolean factorP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCORA) {
            leerToken();
            variablePP();
            return true;
        }
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            designador_de_funcionPP();
            return true;
        }

        return true;
    }

    // <termino> ::= <factor><termino'>
    public boolean termino() throws IOException, ExcepALexico, ExcepASintatico {
        factor();
        terminoP();
        return true;
    }

    // <termino'> ::= TOPERMULT <factor><termino'> | TOPERDIV <factor><termino'>
    // | TOPER_AND <factor>
    public boolean terminoP() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TOPERMULT)
                || (TActual.tipo == Token.TOPERDIV)
                || (TActual.tipo == Token.TOPER_AND)) {
            leerToken();
            factor();
            terminoP();
            return true;
        } else {
            return true;
        }
    }

    // <expresion simple> ::= <termino><expresion'> | <signo><termino><expresion simple'>
    public boolean expresion_simple() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR
                || TActual.tipo == Token.TNUMERO
                || TActual.tipo == Token.TCARACTER
                || TActual.tipo == Token.TPARENTA
                || TActual.tipo == Token.TOPER_NOT) {
            termino();
            expresion_simpleP();
            return true;
        }
        if (TActual.tipo == Token.TOPERMAS
                || TActual.tipo == Token.TOPERMENOS) {
            signo();
            termino();
            expresion_simpleP();
            return true;
        }

        throw new ExcepASintatico("Error en la expresion simple.", TActual.nlinea, TActual);
    }

    // <operador de suma> ::= TOPERMAS | TOPERMENOS | TOPER_OR
    public boolean operador_de_suma() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TOPERMAS
                || TActual.tipo == Token.TOPERMENOS
                || TActual.tipo == Token.TOPER_OR) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un operador de suma (+|-|or).", TActual.nlinea, TActual);
        }
    }

    public boolean operador_de_relacion() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TSIMBOLO_IGUAL
                || TActual.tipo == Token.TSIMBOLO_DISTINTO
                || TActual.tipo == Token.TSIMBOLO_MENOR
                || TActual.tipo == Token.TSIMBOLO_MAYOR
                || TActual.tipo == Token.TSIMBOLO_MENORIGUAL
                || TActual.tipo == Token.TSIMBOLO_MAYORIGUAL) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un operador de relacion.", TActual.nlinea, TActual);
        }
    }

    // <operador de multiplicacion> ::= TOPERMULT | TOPERDIV | TOPER_AND
    // <expresion simple'> ::= <operador de suma><termino><expresion simple'> | lambda
    public boolean expresion_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TOPERMAS
                || TActual.tipo == Token.TOPERMENOS
                || TActual.tipo == Token.TOPER_OR) {
            operador_de_suma();
            termino();
            expresion_simpleP();
            return true;
        } else {
            return true;
        }
    }

    // <expresion> ::= <expresion simple><expresion'>
    public boolean expresion() throws ExcepALexico, IOException, ExcepASintatico {
        expresion_simple();
        expresionP();
        return true;
    }

    // <expresion'> ::= <operador de relacion><expresion simple> | lambda
    public boolean expresionP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TSIMBOLO_IGUAL
                || TActual.tipo == Token.TSIMBOLO_DISTINTO
                || TActual.tipo == Token.TSIMBOLO_MENOR
                || TActual.tipo == Token.TSIMBOLO_MAYOR
                || TActual.tipo == Token.TSIMBOLO_MENORIGUAL
                || TActual.tipo == Token.TSIMBOLO_MAYORIGUAL) {
            operador_de_relacion();
            expresion_simple();
            return true;
        } else {
            return true;
        }
    }

    // <designador de funcion'> ::= TPARENTA <parametro actual><siguiente parametro> TPARENTC
    public boolean designador_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            parametro_actual();
            siguiente_parametro();
            if (TActual.tipo == Token.TPARENTC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un \")\" al final de los parametros.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un \"(\" al comienzo de los parametros.", TActual.nlinea, TActual);
        }
    }

    // <designador de funcion''> ::= <parametro actual><siguiente parametro> TPARENTC
    public boolean designador_de_funcionPP() throws ExcepALexico, IOException, ExcepASintatico {
        parametro_actual();
        siguiente_parametro();
        if (TActual.tipo == Token.TPARENTC) {
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un \")\" al final de los parametros.", TActual.nlinea, TActual);
        }
    }

    // <parametro actual> ::= <expresion>
    public boolean parametro_actual() throws ExcepALexico, IOException, ExcepASintatico {
        expresion();
        return true;
    }

    // <siguiente parametro> ::= TCOMA <parametro actual><siguiente parametro> | lambda
    public boolean siguiente_parametro() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCOMA) {
            leerToken();
            parametro_actual();
            siguiente_parametro();
            return true;
        } else {
            return true;
        }

    }

    // <parte de definicion de constantes> ::= TPALRES_CONST <definicion de constante> TPUNTO_Y_COMA <sig definicion de constante> | lambda
    public boolean parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_CONST) {
            leerToken();
            definicion_de_constante();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de constante.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <sig definicion de constante> ::= TIDENTIFICADOR <definicion de constante'> TPUNTO_Y_COMA <sig definicion de constante> | lambda
    public boolean sig_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            definicion_de_constanteP();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de constante.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de definicion de tipos> ::= TPALRES_TYPE <definicion de tipo> TPUNTO_Y_COMA <sig definicion de tipo> | lambda
    public boolean parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_TYPE) {
            leerToken();
            definicion_de_tipo();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <sig definicion de tipo> ::= TIDENTIFICADOR <definicion de tipo'> TPUNTO_Y_COMA <sig definicion de tipo> | lambda
    public boolean sig_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            definicion_de_tipoP();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de declaracion de variables> ::= TPALRES_VAR <declaracion de variable> TPUNTO_Y_COMA <sig declaracion de variable> | lambda
    public boolean parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_VAR) {
            leerToken();
            declaracion_de_variable();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_declaracion_de_variable();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <sig declaracion de variable> ::= TIDENTIFICADOR <declaracion de variable'> TPUNTO_Y_COMA <sig declaracion de variable> | lambda
    public boolean sig_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            declaracion_de_variableP();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_declaracion_de_variable();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de declaracion de funciones y procedimientos> ::= <sig declaracion de procedimiento o funcion> TPUNTO_Y_COMA | lambda
    public boolean parte_de_declaracion_de_funciones_y_procedimientos() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_PROCEDURE
                || TActual.tipo == Token.TPALRES_FUNCTION) {
            sig_declaracion_de_procedimiento_o_funcion();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la declaracion de de funciones y procedimientos.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <sig declaracion de procedimiento o funcion> ::=
    // TPALRES_PROCEDURE TIDENTIFICADOR <encabezado de procedimiento'><bloque><sig declaracion de procedimiento o funcion>
    // | TPALRES_FUNCTION TIDENTIFICADOR <encabezado de funcion'><bloque><sig declaracion de procedimiento o funcion>
    // | lambda
    public boolean sig_declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_PROCEDURE) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                encabezado_de_procedimientoP();
                bloque();
                sig_declaracion_de_procedimiento_o_funcion();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un identificador en la declaracion del procedimiento.", TActual.nlinea, TActual);
            }
        }
        if (TActual.tipo == Token.TPALRES_FUNCTION) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                encabezado_de_funcionP();
                bloque();
                sig_declaracion_de_procedimiento_o_funcion();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un identificador en la declaracion de la funcion.", TActual.nlinea, TActual);
            }
        }

        return true;
    }

    // <encabezado de procedimiento'> ::= TPUNTO_Y_COMA | TPARENTA <seccion de parametros formales>
    // <sig seccion de parametros form> TPARENTC TPUNTO_Y_COMA
    public boolean encabezado_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
            leerToken();
            return true;
        }
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            seccion_de_parametros_formales();
            sig_seccion_de_parametros_form();
            if (TActual.tipo == Token.TPARENTC) {
                leerToken();
                if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                    leerToken();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba \";\" al final del encabezado de procedimiento .", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba \")\" al final de los parametros .", TActual.nlinea, TActual);
            }
        }
        throw new ExcepASintatico("Se esperaba un encabezado de procedimiento.", TActual.nlinea, TActual);
    }

    // <encabezado de funcion'> ::= TDOSPUNTOS TIDENTIFICADOR TPUNTO_Y_COMA
    // | TPARENTA <seccion de parametros formales><sig seccion de parametros form>
    // TPARENTC TDOSPUNTOS TIDENTIFICADOR TPUNTO_Y_COMA
    public boolean encabezado_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TDOSPUNTOS) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                    leerToken();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba un \";\" al final del encabezado de la funcion.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un IDENTIFICADOR al final del encabezado de la funcion.", TActual.nlinea, TActual);
            }
        } else {
            if (TActual.tipo == Token.TPARENTA) {
                leerToken();
                seccion_de_parametros_formales();
                sig_seccion_de_parametros_form();
                if (TActual.tipo == Token.TPARENTC) {
                    leerToken();
                    if (TActual.tipo == Token.TDOSPUNTOS) {
                        leerToken();
                        if (TActual.tipo == Token.TIDENTIFICADOR) {
                            leerToken();
                            return true;
                        } else {
                            throw new ExcepASintatico("Se esperaba un IDENTIFICADOR al final del encabezado de la funcion.", TActual.nlinea, TActual);
                        }
                    } else {
                        throw new ExcepASintatico("Se esperaba \":\" al final de los parametros.", TActual.nlinea, TActual);
                    }
                } else {
                    throw new ExcepASintatico("Se esperaba \")\" al final de los parametros .", TActual.nlinea, TActual);
                }
            } else {
                return true;
            }
        }
    }

    // <sig seccion de parametros form> ::= TPUNTO_Y_COMA <seccion de parametros formales>
    // <sig seccion de parametros form> | lqmbda
    public boolean sig_seccion_de_parametros_form() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
            leerToken();
            seccion_de_parametros_formales();
            sig_seccion_de_parametros_form();
            return true;
        } else {
            return true;
        }
    }

    // <seccion de parametros formales> ::= TIDENTIFICADOR <grupo de parametros'>
    // | TPALRES_VAR <grupo de parametros>
    public boolean seccion_de_parametros_formales() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            grupo_de_parametrosP();
            return true;
        } else {
            if (TActual.tipo == Token.TPALRES_VAR) {
                leerToken();
                grupo_de_parametros();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba la palabra reservada VAR o un IDENTIFICADOR", TActual.nlinea, TActual);
            }
        }
    }

    // <grupo de parametros'> ::= <siguiente identificador> TDOSPUNTOS TIDENTIFICADOR
    public boolean grupo_de_parametrosP() throws ExcepALexico, IOException, ExcepASintatico {
        siguiente_identificador();
        if (TActual.tipo == Token.TDOSPUNTOS) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba la palabra un IDENTIFICADOR para el parametro", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba el simbolo \":\" luego del IDENTIFICADOR parametro", TActual.nlinea, TActual);
        }
    }

    // <grupo de parametros> ::= TIDENTIFICAOR <siguiente identificador> TDOSPUNTOS TIDENTIFICADOR
    public boolean grupo_de_parametros() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            siguiente_identificador();
            if (TActual.tipo == Token.TDOSPUNTOS) {
                leerToken();
                if (TActual.tipo == Token.TIDENTIFICADOR) {
                    leerToken();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba la palabra un IDENTIFICADOR para el parametro", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba el simbolo \":\" luego del IDENTIFICADOR parametro", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un IDENTIFICADOR para el parametro", TActual.nlinea, TActual);
        }
    }

    // <sentencia compuesta> ::= TPALRES_BEGIN <sentencia><siguiente sentencia> TPALRES_END
    public boolean sentencia_compuesta() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_BEGIN) {
            leerToken();
            sentencia();
            siguiente_sentencia();
            if (TActual.tipo == Token.TPALRES_END) {
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba la palabra reservada END al finalizar la sentencia", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada BEGIN al comienzo de la sentencia", TActual.nlinea, TActual);
        }
    }

    // <sentencia> ::= TIDENTIFICADOR <sentencia simple> | <sentencia estructurada>
    public boolean sentencia() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            sentencia_simple();
            return true;
        } else {
            return sentencia_estructurada();
        }
    }

    // <sentencia simple> ::= TIDENTIFICADOR <sentencia simple'> | lambda
    public boolean sentencia_simple() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            sentencia_simpleP();
            return true;
        } else {
            return true;
        }
    }

    // <sentencia simple'> ::= <sentencia de asignacion'>
    // | <sentencia de procedimiento'>
    public boolean sentencia_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPARENTA) {
            sentencia_de_procedimientoP();
            return true;
        }
        if (TActual.tipo == Token.TCORA
                || TActual.tipo == Token.TASIGN) {
            sentencia_de_asignacionP();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un parentesis o corchete.", TActual.nlinea, TActual);
    }

    // <sentencia de asignacion> ::= TIDENTIFICADOR <variable'> TASIGN <expresion>
    public boolean sentencia_de_asignacion() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            variableP();
            if (TActual.tipo == Token.TASIGN) {
                leerToken();
                expresion();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ':=' en la sentencia.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un identificador en la asignacion.", TActual.nlinea, TActual);
        }
    }

    // <sentencia de asignacion'> ::= <variable'> TASIGN <expresion>
    public boolean sentencia_de_asignacionP() throws ExcepASintatico, ExcepALexico, IOException {
        variableP();
        if (TActual.tipo == Token.TASIGN) {
            leerToken();
            expresion();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un ':=' en la sentencia.", TActual.nlinea, TActual);
        }
    }

    public boolean sentencia_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            sentencia_de_procedimientoP();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un identificador en la asignacion.", TActual.nlinea, TActual);
        }
    }

    // <sentencia de procedimiento'> ::= TPARENTA <parametro actual><siguiente parametro> TPARENTC
    // | lambda
    public boolean sentencia_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            parametro_actual();
            siguiente_parametro();
            if (TActual.tipo == Token.TPARENTC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un parentesis ')'.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <sentencia estructurada> ::= TPALRES_IF <sentencia if>
    // | TPALRES_WHILE <sentencia while> | <sentencia compuesta>
    public boolean sentencia_estructurada() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_IF) {
            sentencia_if();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_WHILE) {
            sentencia_while();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_BEGIN) {
            sentencia_compuesta();
            return true;
        }

        throw new ExcepASintatico("Se esperaba una sentencia estructurada.", TActual.nlinea, TActual);
    }

    // <siguiente sentencia> ::= TPUNTO_Y_COMA <sentencia><siguiente sentencia>
    // | lambda
    public boolean siguiente_sentencia() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
            leerToken();
            sentencia();
            siguiente_sentencia();
            return true;
        } else {
            return true;
        }
    }

    // <sentencia if> ::= TPALRES_IF <expresion> TPALRES_THEN <sentencia><sentencia if'>
    public boolean sentencia_if() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_IF) {
            leerToken();
            expresion();
            if (TActual.tipo == Token.TPALRES_THEN) {
                leerToken();
                sentencia();
                sentencia_ifP();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba la palabra reservada 'then'.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'if'.", TActual.nlinea, TActual);
        }
    }

    // <sentencia if'> ::= TPALRES_ELSE <sentencia> | lambda
    public boolean sentencia_ifP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_ELSE) {
            leerToken();
            sentencia();
            return true;
        } else {
            return true;
        }
    }

    // <sentencia while> ::= TPALRES_WHILE <expresion> TPALRES_DO <sentencia>
    public boolean sentencia_while() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_WHILE) {
            leerToken();
            expresion();
            if (TActual.tipo == Token.TPALRES_DO) {
                leerToken();
                sentencia();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba la palabra reservada 'do'.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'while'.", TActual.nlinea, TActual);
        }
    }

    // <bloque> ::= <parte de definicion de constantes> <parte de defincion de tipos>
    // <parte de declaracion de variables> <parte de declaracion de funciones y procedimientos>
    // <sentencia compuesta>
    public boolean bloque() throws ExcepALexico, IOException, ExcepASintatico {
        parte_de_definicion_de_constantes();
        parte_de_definicion_de_tipos();
        parte_de_declaracion_de_variables();
        parte_de_declaracion_de_funciones_y_procedimientos();
        sentencia_compuesta();
        return true;
    }

    // <programa> ::= <encabezado de programa><bloque> TPUNTO
    public boolean programa() throws ExcepALexico, IOException, ExcepASintatico {
        encabezado_de_programa();
        bloque();
        if (TActual.tipo == Token.TPUNTO) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba el punto al final del programa.", TActual.nlinea, TActual);
        }
    }

    // <encabezado de programa> ::= TPALRES_PROGRAMA TIDENTIFICADOR TPUNOT_Y_COMA
    public boolean encabezado_de_programa() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TPALRES_PROGRAM) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                    leerToken();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba ';' al final del encabezado del programa.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un identificador en el encabezado de programa.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("El codigo debe comenzar con 'program'.", TActual.nlinea, TActual);
        }
    }

    public boolean eof() throws ExcepASintatico {
        if (TActual.tipo == Token.TEOF) {
            return true;
        }
        System.out.println(TActual);
        throw new ExcepASintatico(
                "Se esperaba el final del archivo, pero se encontro algo mas...",
                TActual.nlinea, TActual);
    }
}
