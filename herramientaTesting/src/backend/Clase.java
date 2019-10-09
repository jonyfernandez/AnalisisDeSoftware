package backend;

import java.util.HashMap;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Clase {
	private HashMap<String, Metodo> metodos;
	private String nombre;
	public Clase(ClassOrInterfaceDeclaration nodo) {
		this.setNombre(nodo.getNameAsString());
		this.metodos = new HashMap<String, Metodo>();
		
		for(MethodDeclaration metodoDcl : nodo.getMethods()) {
			Metodo metodo = new Metodo(metodoDcl);
			metodos.put(metodo.getNombre(), metodo);
		}
	}
	
	public Metodo getMetodo(String nombre) {
		return metodos.get(nombre);
	}

	public HashMap<String, Metodo> getMetodos() {
		return metodos;
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
