/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Federico
 */
public class AnalisisDeMetodo {

	private List<String> clases = new ArrayList<>();
	private List<String> nombreClases = new ArrayList<>();
	private List<Metodo> metodos = new ArrayList<>();
	private List<String> nombreMetodos = new ArrayList<>();
	private Integer[] fanIn;

	private List<String> posiblesIniciosDeMetodo = Arrays.asList("public", "private", "protected", "default");

	public List<String> encontrarClases(File fuente) throws IOException {
		Pattern p = Pattern.compile("(\\s\\w+)");
		Matcher m;
		Scanner sc = new Scanner(new FileReader(fuente));
		String linea, copia, clase = "";
		boolean esComentario = false, claseEncontrada = false, nuevaClase;
		clases.clear();
		nombreClases.clear();
		while (sc.hasNextLine()) {
			nuevaClase = false;
			copia = linea = sc.nextLine();
			if (esComentario) { // la peor forma posible de encontrar comentarios e ignorarlos
				if (linea.contains(Constantes.CIERRE_DE_COMENTARIO)) {
					esComentario = false;
					linea = linea.substring(linea.indexOf(Constantes.CIERRE_DE_COMENTARIO));
				} else
					continue;
			}
			if (linea.contains(Constantes.COMENTARIO_UNA_LINEA))
				linea = linea.substring(0, linea.indexOf(Constantes.COMENTARIO_UNA_LINEA));

			if (linea.contains(Constantes.APERTURA_DE_COMENTARIO)) {
				linea = linea.substring(0, linea.indexOf(Constantes.APERTURA_DE_COMENTARIO));
				esComentario = true;
			}
			m = p.matcher(linea.substring(linea.indexOf(" class ") + 1));
			if (linea.contains(" class ") && m.find()) {
				nombreClases.add(m.group().trim());
				if (claseEncontrada)
					nuevaClase = true;
				claseEncontrada = true;
			}
			if (nuevaClase) {
				clases.add(clase);
				clase = new String();
				nuevaClase = false;
			}
			if (claseEncontrada)
				clase = clase.concat(copia + Constantes.SALTO_DE_LINEA);
		}
		clases.add(clase);
		sc.close();

		return nombreClases;
	}

	public List<String> encontrarMetodos(int index) {
		String clase = clases.get(index);
		String metodo = null;
		String linea, copia;
		Scanner sc = new Scanner(clase);
		Pattern p = Pattern.compile("\\w+[(]");
		Matcher m;
		boolean esComentario = false;
		metodos.clear();
		nombreMetodos.clear();

		while (sc.hasNextLine()) {
			copia = linea = sc.nextLine();
			if (esComentario) {// igual que en el otro metodo
				if (linea.contains(Constantes.CIERRE_DE_COMENTARIO)) {
					esComentario = false;
					linea = linea.substring(linea.indexOf(Constantes.CIERRE_DE_COMENTARIO));
				} else
					continue;
			}
			if (linea.contains(Constantes.COMENTARIO_UNA_LINEA))
				linea = linea.substring(0, linea.indexOf(Constantes.COMENTARIO_UNA_LINEA));

			if (linea.contains(Constantes.APERTURA_DE_COMENTARIO)) {
				linea = linea.substring(0, linea.indexOf(Constantes.APERTURA_DE_COMENTARIO));
				esComentario = true;
			}
			m = p.matcher(linea);
			linea = linea.trim();
			if (esMetodo(linea) && m.find() && !linea.endsWith(Constantes.PUNTO_Y_COMA)) {
				nombreMetodos.add(obtenerNombreDeMetodo(m));
				if (metodo != null)
					metodos.add(new Metodo(metodo));
				metodo = "";
			}
			if (metodo != null) {
				metodo = metodo.concat(copia + Constantes.SALTO_DE_LINEA);
			}

		}
		metodos.add(new Metodo(metodo));
		sc.close();
		calcularFanIn();
		return nombreMetodos;
	}

	private String obtenerNombreDeMetodo(Matcher m) {
		return m.group().substring(0, m.group().length() - 1);
	}

	private boolean esMetodo(String linea) {
		return posiblesIniciosDeMetodo.stream().anyMatch(linea::contains);
	}

	public Metodo getMetodo(int index) {
		return metodos.get(index);
	}

	private void calcularFanIn() {
		int i, j;
		fanIn = new Integer[nombreMetodos.size()];
		for (i = 0; i < fanIn.length; i++) {
			fanIn[i] = 0;
			String nombreMetodo = nombreMetodos.get(i);
			for (j = 0; j < fanIn.length; j++) {
				if (j != i) {
					Scanner sc = new Scanner(metodos.get(j).getCodigo());
					String linea;
					while (sc.hasNextLine()) {
						linea = sc.nextLine();
						if (linea.contains(nombreMetodo))
							fanIn[i]++;
					}
					sc.close();
				}
			}
		}

	}

	public String getFanIn(int index) {
		return Integer.toString(fanIn[index]);
	}

	public String getFanOut(int index) {
		int contador = 0;
		Scanner sc = new Scanner(metodos.get(index).getCodigo());
		String linea;
		while (sc.hasNextLine()) {
			linea = sc.nextLine();
			for (String nombre : nombreMetodos) {
				if (linea.contains(nombre) && nombre != nombreMetodos.get(index))
					contador++;
			}
		}
		sc.close();
		return Integer.toString(contador);
	}
	

}
