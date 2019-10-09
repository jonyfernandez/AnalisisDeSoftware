package backend;

import java.util.ArrayList;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;

public class Metodo {

	private String nombre;
	private String cuerpo; //Codigo
	private int fanOut = -1;
	private int fanIn = -1;
	private int longitud = -1;
	private double volumen = -1;
	private int complejidadCiclomatica = -1;
	private MethodDeclaration nodo;
	private ArrayList<Token> operadores;
	private ArrayList<Token> operandos;
	
	public Metodo(MethodDeclaration nodo) {
		this.nodo = nodo;
		this.setNombre(nodo.getNameAsString());
		
		String declaracion = nodo.getBody().toString().replaceAll("/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/", "").replaceAll("//.*[\n\r]", "").replaceAll("^\\s*\n", "");
        
		this.setCuerpo("");
        String[] body = declaracion.split("\\n");
        for (int i = 0; i < body.length; i++) {
            if (i != 0 && i != body.length - 1)
                this.setCuerpo(getCuerpo() + body[i]);
        }
	}
	
	/**
	 * Calcula complejidad ciclomatica, longitud y volumen
	 * Fan In y Fan Out deben calcularse a parte
	 */
	public void procesar() {
		calcularComplejidadCiclomatica();
		calcularHalstead();
	}
	
	private void calcularHalstead() {
		operadores = new ArrayList<Token>();
		operandos = new ArrayList<Token>();
		operadores.addAll(Halstead.extraerTokens(cuerpo, Token.OPERADOR));
		operandos.addAll(Halstead.extraerTokens(cuerpo, Token.OPERANDO));
		longitud = Halstead.calcularLongitud(operadores, operandos);
		volumen = Halstead.calcularVolumen(operadores, operandos, longitud);
	}

	private void calcularComplejidadCiclomatica() {
		if(complejidadCiclomatica > 0)
			return;
		complejidadCiclomatica = 1;
		
		new VoidVisitorAdapter<Object>() {
			//Contar ifs, AND y OR
            @Override
            public void visit(IfStmt n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
                if(n.getCondition() instanceof BinaryExpr) 
                	contarAndYOr((BinaryExpr)n.getCondition());
            }
            
            //Contar cases, que son Entries del Switch con Labels (sin Label es Default, que no cuenta)
            @Override
            public void visit(SwitchEntryStmt n, Object arg) {
                super.visit(n, arg);
                if(n.getLabel().isPresent())
                	complejidadCiclomatica++;
            }
            
            //Contar catchs. Decimos que el par try-catch cuenta como 1
            @Override
            public void visit(CatchClause n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
            }
            
            //Contar throws. Decimos que cada throw cuenta como 1
            @Override
            public void visit(ThrowStmt n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
            }
            
            //Contar whiles, AND y OR. No incluye do-while.
            @Override
            public void visit(WhileStmt n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
                if(n.getCondition() instanceof BinaryExpr) 
                	contarAndYOr((BinaryExpr)n.getCondition());
            }
            
            //Contar do-while, AND y OR
            @Override
            public void visit(DoStmt n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
                if(n.getCondition() instanceof BinaryExpr) 
                	contarAndYOr((BinaryExpr)n.getCondition());
            }
            
            //Contar fors
            @Override
            public void visit(ForStmt n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
            }
            
            //Contar foreachs
            @Override
            public void visit(ForeachStmt n, Object arg) {
                super.visit(n, arg);
                complejidadCiclomatica++;
            }
        }.visit(nodo, null);
	}
	
	/**
	 * Cuenta los AND y OR en una expresion
	 * @param cond
	 */
	private void contarAndYOr(BinaryExpr cond) {
		if(cond.getOperator().equals(BinaryExpr.Operator.AND) || cond.getOperator().equals(BinaryExpr.Operator.OR))
			complejidadCiclomatica++;
		if(cond.getRight() instanceof BinaryExpr)
			contarAndYOr((BinaryExpr)cond.getRight());
		if(cond.getLeft() instanceof BinaryExpr)
			contarAndYOr((BinaryExpr)cond.getLeft());
	}

	public String getNombre() {
		return nombre;
	}
	private void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getFanOut() {
		return fanOut;
	}
	public void setFanOut(int fanOut) {
		this.fanOut = fanOut;
	}
	public int getFanIn() {
		return fanIn;
	}
	public void setFanIn(int fanIn) {
		this.fanIn = fanIn;
	}
	public int getLongitud() {
		return longitud;
	}
	public double getVolumen() {
		return volumen;
	}
	public int getComplejidadCiclomatica() {
		return complejidadCiclomatica;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	
	public String getCod() { 
	    return new PrettyPrinter().print(nodo); 
	 } 
}
