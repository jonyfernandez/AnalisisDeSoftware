/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.nonNull;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Federico
 */
public class Metodo {

	private final int NOT_FOUND = -1;
	private String codigo;
	private int nodosPredicados;
	private int cantLineas;
	private int lineasComentadas;
	private HashMap<String, Integer> operadoresHalstead;
	private HashMap<String, Integer> operandosHalstead;
	ArrayList <String> variables = new ArrayList<String>();	

	private String palabrasClave = "while|if|for|foreach|case|default|continue|goto|&&|catch|\\||\\?";

	public Metodo(String str) {
		codigo = str;
		nodosPredicados = cantLineas = lineasComentadas = 0;
		this.analizar();
	}

	private void analizar() {
		if (nonNull(codigo)) {
			Scanner sc = new Scanner(codigo);
			String linea;
			List<String> codigoEditado = new ArrayList<>();
			operadoresHalstead = new HashMap<String, Integer>();
			operandosHalstead = new HashMap<String, Integer>();
			// aplicados.
			while (sc.hasNextLine()) {
				linea = sc.nextLine();
				if (linea.contains(Constantes.COMENTARIO_UNA_LINEA) || linea.contains(Constantes.APERTURA_DE_COMENTARIO)
						|| linea.contains(Constantes.CIERRE_DE_COMENTARIO)) {
					lineasComentadas++;
				}

				encontrarOperadores(removerComentarios(linea));
				encontrarOperandos(removerComentarios(linea));
				linea = contarYResaltarPredicados(linea, removerComentarios(linea));

				codigoEditado.add(linea);
				if (!linea.trim().isEmpty())
					cantLineas++;

			}
			cantLineas--;
			this.codigo = String.join(Constantes.SALTO_DE_LINEA, codigoEditado);
			sc.close();
		}
	}

	private String removerComentarios(String linea) {
		int indiceComentario = linea.indexOf(Constantes.COMENTARIO_UNA_LINEA);
		if (indiceComentario != -1) {
			linea = linea.substring(0, indiceComentario);
		}

		int indiceApertura = linea.indexOf(Constantes.APERTURA_DE_COMENTARIO);
		int indiceCerrado = linea.indexOf(Constantes.CIERRE_DE_COMENTARIO);
		if (indiceApertura != NOT_FOUND || indiceCerrado != NOT_FOUND) {
			if (indiceApertura != NOT_FOUND && indiceCerrado != NOT_FOUND) {
				String comentario = linea.substring(indiceApertura, indiceCerrado);
				linea = linea.replace(comentario, "");
			} else if (indiceApertura != NOT_FOUND) {
				linea = linea.substring(0, indiceApertura);
			} else {
				linea = linea.substring(indiceCerrado);
			}
		}

		return linea;
	}

	private String contarYResaltarPredicados(String original, String analizar) {
		String regex = "((" + palabrasClave + ")(?=[\\s\\(]))";
		Pattern p = Pattern.compile(regex); // encuentra cualquier palabra de la lista separada por OR |
		Matcher m = p.matcher(analizar); // solo si esta seguida de un espacio \\s o un parentesis abierto \\(
		while (m.find()) // https://www.freeformatter.com/java-regex-tester.html
			nodosPredicados++;
		original = original.replaceAll(regex, "<b><font color=\"red\">$1</b></font>");// $1 es el elemento encontrado
		return original;
	}

	public String getLineas() {
		return Integer.toString(cantLineas);
	}

	public String getComentarios() {
		return Integer.toString(lineasComentadas);
	}

	public Double getPorcentajeD() {
		return ((double) (lineasComentadas * 100) / cantLineas);
	}
	public String getPorcentaje() {
		return String.format("%.2f", ((double) (lineasComentadas * 100) / cantLineas)) + "%";
	}

	public String getComplejidad() {
		return Integer.toString(nodosPredicados + 1);
	}
	public int getComplejidadI() {
		return nodosPredicados + 1;
	}

	public String getCodigo() {
		return codigo;
	}

	private void encontrarOperadores(String linea) {
		String regexOpCaracter = "((?<!(" + Constantes.OPERADORES_CARACTER + "))(" + Constantes.OPERADORES_CARACTER
				+ ")(?!(" + Constantes.OPERADORES_CARACTER + ")))";
		// cuenta un operador de la lista de operadores_caracter si no tiene otro de la
		// misma lista detras, delante o ambas.
		// evita contar por ejemplo, un + 3 veces cuando tengo el operador + y el
		// operador ++ (dos op. distintos)
		String regexOpPalabra = "((" + Constantes.OPERADORES_PALABRA + ")(?=[\\s\\(]))";
		String regexOpCompuestos = "(" + Constantes.OPERADORES_COMPUESTOS + ")";
		Pattern p = Pattern.compile(regexOpCaracter);
		Matcher m = p.matcher(linea);
		while (m.find()) {
			if (operadoresHalstead.containsKey(m.group()) && m.group() != null) {
				operadoresHalstead.put(m.group(), operadoresHalstead.get(m.group()) + 1);
			} else
				operadoresHalstead.put(m.group(), 1);
		}
		p = Pattern.compile(regexOpPalabra);
		m = p.matcher(linea);
		while (m.find()) {
			if (operadoresHalstead.containsKey(m.group()) && m.group() != null) {
				operadoresHalstead.put(m.group(), operadoresHalstead.get(m.group()) + 1);
			} else
				operadoresHalstead.put(m.group(), 1);
		}
		p = Pattern.compile(regexOpCompuestos);
		m = p.matcher(linea);
		while (m.find()) {
			if (operadoresHalstead.containsKey(m.group()) && m.group() != null) {
				operadoresHalstead.put(m.group(), operadoresHalstead.get(m.group()) + 1);
			} else
				operadoresHalstead.put(m.group(), 1);
		}
	}

	private void encontrarOperandos(String linea) {
		String regexLiterales = "(\\d+)";
		String regexVariables = "((?<=" + Constantes.TIPOS_DE_DATO + ")(?:\\s+)(\\w+)(?:\\s*)(?=\\=|;))";// busca el
																										// nombre de la
																										// variable
																										// entre un tipo
																										// de dato y un
																										// igual o punto y coma
		Pattern p = Pattern.compile(regexLiterales);
		Matcher m = p.matcher(linea);
		while (m.find()) {
			if (operandosHalstead.containsKey(m.group()) && m.group() != null) {
				operandosHalstead.put(m.group(), operandosHalstead.get(m.group()) + 1);
			} else
				operandosHalstead.put(m.group(), 1);
		}

		for (String var : variables) {// encontrar las variables en el codigo luego de que fueron declaradas.
			p = Pattern.compile(("(?<!(\"))(\\b" + var + "\\b)(?!(\"))"));
			m= p.matcher(linea);
			while(m.find())
					operandosHalstead.put(var, operandosHalstead.get(var) + 1);
			}
		p = Pattern.compile(regexVariables);
		m = p.matcher(linea);// no supe como hacer que no capture los espacios, asi que uso trim
		while (m.find()) {
				operandosHalstead.put(m.group().trim(), 1);
				variables.add(m.group().trim());
			}
		}
	
	public int calcularLongitud() {
		int longitud = 0;
		for (Integer dato : operadoresHalstead.values())
			longitud += dato;
		for (Integer dato : operandosHalstead.values())
			longitud+=dato;
		return longitud;
	}
	public String longitudHalstead() {
		
		return Integer.toString(calcularLongitud());
	}
	
	public String volumenHalstead() {
		int n = operandosHalstead.keySet().size()+operadoresHalstead.keySet().size();
		return n==0?"0":String.format("%.2f", calcularLongitud()*(Math.log(n)/Math.log(2)));
	}
	public Object[][] datosOperador(){
		Object[][] datos = new Object[operadoresHalstead.keySet().size()][2];
		int cont = 0;
		for (Map.Entry<String, Integer> op : operadoresHalstead.entrySet()) {
			datos[cont][0]= op.getKey();
			datos[cont][1]= op.getValue();
			cont++;
		}
		return datos;
	}
	public Object[][] datosOperando(){
		Object[][] datos = new Object[operandosHalstead.keySet().size()][2];
		int cont = 0;
		for (Map.Entry<String, Integer> op : operandosHalstead.entrySet()) {
			datos[cont][0]= op.getKey();
			datos[cont][1]= op.getValue();
			cont++;
		}
		return datos;
	}
	// para testear

	public String getOperadores() {
		String ret = "\n";
		for (Map.Entry<String, Integer> op : operadoresHalstead.entrySet()) {
			ret += op.getKey() + " " + op.getValue() + "\n";
		}
		return ret;
	}
	public String getOperandos() {
		String ret = "\n";
		for (Map.Entry<String, Integer> op : operandosHalstead.entrySet()) {
			ret += op.getKey() + " " + op.getValue() + "\n";
		}
		return ret;
	}
}
