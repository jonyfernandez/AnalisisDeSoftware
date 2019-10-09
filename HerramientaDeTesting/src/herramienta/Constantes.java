package herramienta;

public class Constantes {
    public static final String CIERRE_DE_COMENTARIO = "*/";
    public static final String COMENTARIO_UNA_LINEA = "//";
    public static final String APERTURA_DE_COMENTARIO = "/*";
    public static final String PUNTO_Y_COMA = ";";
    public static final String SALTO_DE_LINEA = "\n";
    //necesito separar los operadores de halstead segun la expresion que voy a usar para encontrarlos
    public static final String OPERADORES_PALABRA ="while|if|for|foreach|case|default|continue|goto|catch";//se los cuenta si estan antes de un espacio o parentesis
    public static final String OPERADORES_COMPUESTOS ="\\+\\+|--|==|&&|<=|>=|\\|\\|!=";//"operadores que se construyen a partir de otros" cuentan su aparicion sin condiciones.
    public static final String OPERADORES_CARACTER = "\\?|!|=|\\*|/|\\+|-|<|>";// se cuentan cuando aparecen solos y no como parte de un operador compuesto
    public static final String TIPOS_DE_DATO = "int|string|float|char|double|Integer|String|Character|Double|bool|Boolean|File";
    public static final String RECO_COMENTARIOS = "El porcentaje de líneas comentadas es muy bajo, se recomienda comentar más el método.\n";
    public static final String RECO_CC = "La Complejidad Ciclomatica del método es muy alta, se recomienda modularizarlo.\n";
    public static final String RECO_FANOUT= "Debido a su Fan Out elevado, se recomienda testear este método mas exhaustivamente.\n";
    public static final String RECO_HALSTEAD= "Debido a la relacion entre la longitud de Halstead y la cantidad de lineas, se recomienda dar formato al código para hacerlo mas facil de entender.\n";
}