package pantalla;

import backend.Controlador;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

import java.text.DecimalFormat;


public class PantallaPrincipal extends JFrame {

	private JPanel contentPane;
	private Controlador elControlador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrincipal frame = new PantallaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PantallaPrincipal() {
		
		setTitle("Herramienta de Testing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1039, 584);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1021, 26);
		contentPane.add(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		DecimalFormat formato = new DecimalFormat("#0.00"); //Para limitar los doubles a 2 decimales
		
		//Labels con resultados
		
		JLabel locsLabel = new JLabel("[Cant Lineas]");
		locsLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		locsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		locsLabel.setBounds(294, 103, 212, 30);
		contentPane.add(locsLabel);
		
		JLabel porcentajeComentLabel = new JLabel("[Cant Lineas]%");
		porcentajeComentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		porcentajeComentLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		porcentajeComentLabel.setBounds(294, 172, 212, 30);
		contentPane.add(porcentajeComentLabel);
		
		JLabel lblfanOut = new JLabel("[FAN OUT]");
		lblfanOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblfanOut.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lblfanOut.setBounds(10, 504, 176, 30);
		contentPane.add(lblfanOut);
		
		JLabel lblfanIn = new JLabel("[FAN IN]");
		lblfanIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblfanIn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lblfanIn.setBounds(10, 440, 176, 30);
		contentPane.add(lblfanIn);
		
		JLabel lbllong = new JLabel("[LONG]");
		lbllong.setHorizontalAlignment(SwingConstants.CENTER);
		lbllong.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lbllong.setBounds(401, 440, 176, 30);
		contentPane.add(lbllong);
		
		JLabel lblvol = new JLabel("[VOL]");
		lblvol.setHorizontalAlignment(SwingConstants.CENTER);
		lblvol.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lblvol.setBounds(401, 504, 176, 30);
		contentPane.add(lblvol);
		
		JLabel lblvg = new JLabel("[V(G)]");
		lblvg.setHorizontalAlignment(SwingConstants.CENTER);
		lblvg.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lblvg.setBounds(200, 463, 189, 30);
		contentPane.add(lblvg);
		
		//Listas
		JList<String> listMetodos = new JList<String>(new DefaultListModel<String>());
		contentPane.add(listMetodos);
		JList<String> listClases = new JList<String>(new DefaultListModel<String>());
		contentPane.add(listClases);
		JList<String> listArchivos = new JList<String>(new DefaultListModel<String>());
		JTextArea codigoArea=new JTextArea();
		
		//ScrollPanes
		JScrollPane metScroll = new JScrollPane(listMetodos);
		metScroll.setBounds(294, 267, 283, 135);
		contentPane.add(metScroll);
		JScrollPane claScroll = new JScrollPane(listClases);
		claScroll.setBounds(10, 267, 272, 135);
		contentPane.add(claScroll);
		JScrollPane filScroll = new JScrollPane(listArchivos);
		filScroll.setBounds(10, 82, 272, 120);
		contentPane.add(filScroll);
		JScrollPane codScrollPane = new JScrollPane(codigoArea);		 
	    codScrollPane.setBounds(608, 59, 401, 465); 
	    contentPane.add(codScrollPane);
		
		//Lista de metodos
		listMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMetodos.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Que hacer cuando clickean un metodo
				
				//Si la seleccion todavia esta cambiando, la ignoro
				//Solo cuando quedo quieta ejecuto
				if(!e.getValueIsAdjusting()) {
					//Si no hay nada seleccionado, no hacer nada
					if(listMetodos.isSelectionEmpty()) {
						codigoArea.setText("");
						return;
					}
					
					//Si hay algo seleccionado, buscar datos de ese metodo
					String clase = listClases.getSelectedValue();
					String metodo = listMetodos.getSelectedValue();
					elControlador.procesarMetodo(clase, metodo);
					
					//Rellenar labels
					lblfanOut.setText(Integer.toString(elControlador.traerFanOut(clase, metodo)));
					lblfanIn.setText(Integer.toString(elControlador.traerFanIn(clase, metodo)));
					lbllong.setText(Integer.toString(elControlador.traerLongitud(clase, metodo)));
					lblvol.setText(formato.format(elControlador.traerVolumen(clase, metodo)));
					lblvg.setText(Integer.toString(elControlador.traerVg(clase, metodo)));
					codigoArea.setText(elControlador.traerCod(clase, metodo));
				}
			}
		});
		listMetodos.setBounds(593, 106, 369, 135);
		listMetodos.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		//Lista de clases
		listClases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listClases.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Que hacer cuando clickean una clase
				
				//Si la seleccion todavia esta cambiando, la ignoro
				//Solo cuando quedo quieta ejecuto
				if(!e.getValueIsAdjusting()) {
					//Primero, limpiar lista de metodos
					listMetodos.clearSelection();
					((DefaultListModel<String>) listMetodos.getModel()).removeAllElements();
					
					//Si lo que paso es que saque la seleccion, listo
					if(listClases.isSelectionEmpty())
						return;
					
					//Sino, listar los metodos de la clase seleccionada
					for(String nombreMetodo : elControlador.traerMetodosDeClase(listClases.getSelectedValue()))
						((DefaultListModel<String>) listMetodos.getModel()).addElement(nombreMetodo);
				}
			}
		});
		listClases.setBounds(1, 1, 202, 56);
		listClases.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		//Lista de archivos
		listArchivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listArchivos.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Que hacer cuando clickean un archivo
				
				//Si la seleccion todavia esta cambiando, la ignoro
				//Solo cuando quedo quieta ejecuto
				if(!e.getValueIsAdjusting()) {
					//Si no hay nada seleccionado, no hacer nada
					if(listArchivos.isSelectionEmpty())
						return;
					
					//Mostrar datos de ese archivo
					locsLabel.setText( Integer.toString(
							elControlador.traerLineasArch(
									listArchivos.getSelectedValue() )
							));
					
					porcentajeComentLabel.setText(formato.format(
							elControlador.traerPorcentajeComent(
								listArchivos.getSelectedValue()
							) * 100) + "%" );

				}
			}
		});
		listArchivos.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listArchivos.setBounds(6, 77, 362, 130);
		
		JMenuItem mntmAbrir = new JMenuItem("Abrir directorio");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser js = new JFileChooser();
				js.setDialogTitle("Abrir directorio");
				js.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = js.showOpenDialog(null);
				if (result==JFileChooser.APPROVE_OPTION) {
					//Proceso el nuevo directorio
					elControlador = new Controlador();
					elControlador.procesar(js.getSelectedFile());
					
					//Reseteo labels
					locsLabel.setText("[Cant Lineas]");
					porcentajeComentLabel.setText("[Cant Lineas]%");
					lblfanOut.setText("[FAN OUT]");
					lblfanIn.setText("[FAN IN]");
					lbllong.setText("[LONG]");
					lblvol.setText("[LONG]");
					
					//Reseteo listas
					((DefaultListModel<String>) listArchivos.getModel()).removeAllElements();
					listArchivos.clearSelection();
					((DefaultListModel<String>) listClases.getModel()).removeAllElements();
					listClases.clearSelection();
					((DefaultListModel<String>) listMetodos.getModel()).removeAllElements();
					listMetodos.clearSelection();
					
					//Relleno listas
					for(String arch : elControlador.traerArchivos())
						((DefaultListModel<String>) listArchivos.getModel()).addElement(arch);
					for(String clase : elControlador.traerClases())
						((DefaultListModel<String>) listClases.getModel()).addElement(clase);
				}
			}

		});
		
		mnArchivo.add(mntmAbrir);
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnArchivo.add(mntmSalir);
		
		
		JLabel lblNewLabel = new JLabel("Seleccione un archivo de la lista:");
		lblNewLabel.setForeground(new Color(178, 34, 34));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(6, 55, 276, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblSeleccioneUnaClase = new JLabel("Seleccione una clase de la lista:");
		lblSeleccioneUnaClase.setForeground(new Color(178, 34, 34));
		lblSeleccioneUnaClase.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSeleccioneUnaClase.setBounds(10, 245, 362, 16);
		contentPane.add(lblSeleccioneUnaClase);
		
		JLabel lblSeleccioneUnMetodo = new JLabel("Seleccione un metodo de la lista:");
		lblSeleccioneUnMetodo.setForeground(new Color(178, 34, 34));
		lblSeleccioneUnMetodo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSeleccioneUnMetodo.setBounds(294, 245, 476, 16);
		contentPane.add(lblSeleccioneUnMetodo);
		
		JLabel lblLineasDeCdigo = new JLabel("Lineas de c\u00F3digo del archivo:");
		lblLineasDeCdigo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLineasDeCdigo.setForeground(new Color(178, 34, 34));
		lblLineasDeCdigo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLineasDeCdigo.setBounds(294, 75, 283, 16);
		contentPane.add(lblLineasDeCdigo);
		
		JTextArea txtrPorcentajeDeLineas = new JTextArea();		 
	    txtrPorcentajeDeLineas.setLineWrap(true);	 
	    txtrPorcentajeDeLineas.setWrapStyleWord(true);	 
	    txtrPorcentajeDeLineas.setText("Porcentaje de lineas de c\u00F3digo del archivo comentadas:");	 
	    txtrPorcentajeDeLineas.setBackground(SystemColor.menu);	 
	    txtrPorcentajeDeLineas.setForeground(new Color(178, 34, 34));	 
	    txtrPorcentajeDeLineas.setFont(new Font("Tahoma", Font.BOLD, 13));
	    txtrPorcentajeDeLineas.setBounds(304, 130, 273, 44);
	    contentPane.add(txtrPorcentajeDeLineas);
		
		JLabel lblFanOut = new JLabel("Fan Out:");
		lblFanOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanOut.setForeground(new Color(178, 34, 34));
		lblFanOut.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblFanOut.setBounds(10, 477, 176, 16);
		contentPane.add(lblFanOut);
		
		JLabel lblLongitudDeHalstead = new JLabel("Longitud de Halstead:");
		lblLongitudDeHalstead.setHorizontalAlignment(SwingConstants.CENTER);
		lblLongitudDeHalstead.setForeground(new Color(178, 34, 34));
		lblLongitudDeHalstead.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLongitudDeHalstead.setBounds(401, 415, 176, 16);
		contentPane.add(lblLongitudDeHalstead);
		
		JLabel lblFanIn = new JLabel("Fan In:");
		lblFanIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanIn.setForeground(new Color(178, 34, 34));
		lblFanIn.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblFanIn.setBounds(10, 413, 176, 16);
		contentPane.add(lblFanIn);
		
		JLabel lblVolumenDeHalstead = new JLabel("Volumen de Halstead:");
		lblVolumenDeHalstead.setHorizontalAlignment(SwingConstants.CENTER);
		lblVolumenDeHalstead.setForeground(new Color(178, 34, 34));
		lblVolumenDeHalstead.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblVolumenDeHalstead.setBounds(401, 477, 176, 16);
		contentPane.add(lblVolumenDeHalstead);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 213, 571, 2);
		contentPane.add(separator);
		
		JLabel lblNewLabel_2 = new JLabel("Archivos .java");
		lblNewLabel_2.setForeground(new Color(0, 0, 128));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lblNewLabel_2.setBounds(10, 33, 760, 26);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblClasesYMetodos = new JLabel("Clases y metodos");
		lblClasesYMetodos.setHorizontalAlignment(SwingConstants.CENTER);
		lblClasesYMetodos.setForeground(new Color(0, 0, 128));
		lblClasesYMetodos.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lblClasesYMetodos.setBounds(10, 221, 760, 26);
		contentPane.add(lblClasesYMetodos);
		
		JLabel lblComplejidadCiclomatica = new JLabel("Complejidad Ciclomatica:");
		lblComplejidadCiclomatica.setHorizontalAlignment(SwingConstants.CENTER);
		lblComplejidadCiclomatica.setForeground(new Color(178, 34, 34));
		lblComplejidadCiclomatica.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblComplejidadCiclomatica.setBounds(207, 449, 195, 16);
		contentPane.add(lblComplejidadCiclomatica);
		
	}
}
