package backend;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Halstead {
	
	private static final  String RGX_ID = "(?<operando>[a-zA-Z_][\\w\\.\\<\\>]*)";
	private static final  String[] RGX_OPERADOR = new String[] {"(?<op>if)", "(?<op>while)", "(?<op>for)", "(?<op>case)", "(?<op>\\|\\|)",
            "(?<op>\\&\\&)", "(?<op>==?)", "(?<op>>=?)", "(?<op><=?)",
            "(?<op>\\+\\+?)", "(?<op>--?)", "(?<op>\\*)", "(?<op>\\/)",
            "(?<op>\\[)", "(?<op>\\])", "(?<op>\\()", "(?<op>\\))", "(?<op>;)", "(?<op>,)",
            "(?<op>\\{)", "(?<op>\\})"};
	private static final  String[] RGX_OPERANDO = new String[] {"=\\s*" + RGX_ID + "\\s*;", "\\[\\s*" + RGX_ID + "\\s*\\]",
            "\\(\\s*" + RGX_ID + "\\s*\\)", RGX_ID + "\\s*==?", "[\\(|\\&\\&|\\|\\|]\\s*" + RGX_ID + "\\s*>=?",
            "[\\(|\\&\\&|\\|\\|]\\s*" + RGX_ID + "\\s*<=?", RGX_ID + "\\s*\\+\\+?", RGX_ID + "\\s*--?",
            RGX_ID + "\\s*\\["};
	
	public static ArrayList<Token> extraerTokens(String str, int tipo) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        String[] regex;
        
        if (tipo == Token.OPERADOR)
            regex = RGX_OPERADOR;
        else if (tipo == Token.OPERANDO)
            regex = RGX_OPERANDO;
        else
            return null; // tipo incorrecto
        
        for (String token : regex) {
            Pattern pat = Pattern.compile(token);
            Matcher mat = pat.matcher(str);
            while(mat.find()) {
                String key;
                if(tipo == Token.OPERANDO) {
                    key = mat.group("operando");
                    if (key.equals("true") || key.equals("false"))
                        continue;
                }
                else // tipo == OPERADOR
                    key = mat.group("op");
                if (key != null) {
                    tokens.add(new Token(tipo, key));
                }
            }
        }
        return tokens;
    }
	
	private static int contarTokens(ArrayList<Token> tokens) {
        ArrayList<String> tokens_unicos = new ArrayList<String>();
        for (Token token : tokens) {
            if (!tokens_unicos.contains(token.getValor()))
                tokens_unicos.add(token.getValor());
        }
        return tokens_unicos.size();
    }
    
    private static int contarTotalTokens(ArrayList<Token> tokens) {
        return tokens.size();
    }
    
    public static int calcularLongitud(ArrayList<Token> operadores, ArrayList<Token> operandos) {
        return contarTotalTokens(operadores) + contarTotalTokens(operandos);
    }
    
    public static double calcularVolumen(ArrayList<Token> operadores, ArrayList<Token> operandos, int longitud) {
        double n = (double)(contarTokens(operadores) + contarTokens(operandos));
        return calcularLongitud(operadores, operandos) * Math.sqrt(n);
    }
}
