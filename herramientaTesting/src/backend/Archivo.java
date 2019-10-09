package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.Scanner;

public class Archivo {

	private int lineasTotales = -1;
	private double porcentajeComentarios = -1;
	private CompilationUnit arbol;
	private String nombre;
	private String codigo;
	private ArrayList<String> lineas;
	private int cantComentarios;
	
	
	public Archivo(File archivo) throws IOException, ParseProblemException {
		//Abrir archivo, crear arbol, lo que haga falta
		this.nombre = archivo.getName();
		this.arbol = JavaParser.parse(archivo);
        this.codigo = new String(Files.readAllBytes(archivo.toPath()));
        
		lineas = new ArrayList<String>();
		calcularLineas(archivo);
		calcularLineasTotales();
		
		this.cantComentarios = 0;
		int i=0;
		while(i<this.lineas.size()) {
			lineas.set(i, lineas.get(i).replaceAll("\"(?:[^\"\\\\]|\\\\.)*\"", "plainText"));
			i++;
		}
		contarComentarios();
		calcularPorcentajeComentarios();
	}

	private void calcularLineas(File archivo) throws FileNotFoundException {
		Scanner scanner =new Scanner(archivo);
		while(scanner.hasNextLine()) {
			this.lineas.add(scanner.nextLine());
		}
		int i=0;
		while(i<this.lineas.size()) {
			this.lineas.set(i, lineas.get(i).trim());
			if(this.lineas.get(i).equals("")) {
				this.lineas.remove(i);
			}
			else
				i++;
		}
		scanner.close();
	}
	
	public int getLineasTotales() {
		return lineasTotales;
	}

	private void calcularLineasTotales() {
		this.lineasTotales=this.lineas.size();
	}

	public double getPorcentajeComentarios() {
		return porcentajeComentarios;
	}
	
	
	
	private void contarComentarios() {
		int i=0;
		while(i<this.lineas.size()) {
			if(this.lineas.get(i).contains("//")) 
				this.cantComentarios++;
				else if(this.lineas.get(i).contains("/*")) {
					this.cantComentarios++;
					while(!this.lineas.get(i).contains("*/")) {
						this.cantComentarios++;
						i++;
					}
				}
			i++;
			}
		}
	
	
	private void calcularPorcentajeComentarios() {
		this.porcentajeComentarios=this.cantComentarios/(float)this.lineasTotales;
	}

	public CompilationUnit getArbol() {
		return arbol;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
