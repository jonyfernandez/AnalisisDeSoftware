package backend;

public class Token {

    public static int OPERADOR = 1, OPERANDO = 2;
    
    int tipo;
    String valor;
    int contador;
    
    public Token(int tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.contador = 0;
    }
    
    public String getValor() {
        return valor;
    }
    
    public void aumentarContador() {
        contador++;
    }
}
