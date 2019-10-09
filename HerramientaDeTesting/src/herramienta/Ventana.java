package herramienta;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Ventana {
	
	private JFrame frmHerramientaDeTesting;
	private JMenuBar menuBar;
	private JMenu mnAnalizar;
	private JMenuItem mntmAbrir;
	private JMenuItem mntmSalir;
	private JPanel panel2;
	private JPanel PanelAnalisis;
	private JLabel lblCantidadDeLneas;
	private JLabel nroLineas;
	private JLabel nroLineasComentadas;
	private JLabel lblCantidadComentarios;
	private JLabel porcentajeComentarios;
	private JLabel lblPorcentajeDeLneas;
	private JLabel nroComplejidad;
	private JLabel lblComplejidadCiclomtica;
	private JLabel lblFanIn;
	private JLabel nroFanIn;
	private JLabel nroFanOut;
	private JLabel lblFanOut;
	private JLabel lblLongitudDeHalstead;
	private JLabel nroLongitud;
	private JLabel lblVolumenDeHalstead;
	private JLabel nroVolumen;
	//private JPanel PanelHalstead;
	private JPanel PanelFuente;
	private JTextPane textoFuente;
	private JTextPane textoReco;
	private JPanel PanelReco;
	private JTabbedPane tabbedPane;
	private JLabel lblOperadoresUnicos;
	private JLabel lblCantOperad;
	private JLabel lblCantOperand;
	private JLabel lblOperandosUnicos;
	private JLabel lblArchivo;
	private JLabel lblClase;
	private JLabel lblMtodo;
	private JButton btnVerOperadoresY;
	private DefaultTableModel datosOperad;
	private DefaultTableModel datosOperand;
	private JComboBox<String> listaMetodos;
	private JComboBox<String> listaClases;
	private JComboBox<String> listaArchivos;	
	private List<File> listaCodigosFuente = new ArrayList<>();
	private List<String> clasesArchivo;
	private List<String> metodosArchivo;
	private File fuenteAct;
	private Object[][] operandos;
	private Object[][] operadores;
	private AnalisisDeMetodo analizador = new AnalisisDeMetodo();
	
	private FilenameFilter fuenteJava = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".java");
		}
	};
	private JTable tablaOperadores;
	private JTable tablaOperandos;

	/**
	 * Create the application.
	 */
	public Ventana() {
		initialize();
	}

	/**
	 * Launch the application.
	 *
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana window = new Ventana();
					window.frmHerramientaDeTesting.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmHerramientaDeTesting = new JFrame();
		frmHerramientaDeTesting.setResizable(false);
		frmHerramientaDeTesting.setTitle("Herramienta de Testing");
		frmHerramientaDeTesting.setBounds(100, 100, 800, 600);
		frmHerramientaDeTesting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHerramientaDeTesting.getContentPane().setBackground(new Color(255, 204, 153));
		((JComponent) frmHerramientaDeTesting.getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		frmHerramientaDeTesting.getContentPane().setLayout(null);
		
		Image icon = new ImageIcon(getClass().getResource("./tuerca.png")).getImage();
		frmHerramientaDeTesting.setIconImage(icon);
		
		menuBar = new JMenuBar();
		frmHerramientaDeTesting.setJMenuBar(menuBar);
		
		mnAnalizar = new JMenu("Analizar");
		menuBar.add(mnAnalizar);
		
		mntmAbrir = new JMenuItem("Abrir..");
		mnAnalizar.add(mntmAbrir);
		
		mntmSalir = new JMenuItem("Salir");
		mnAnalizar.add(mntmSalir);

		panel2 = new JPanel();
		panel2.setBounds(10, 56, 774, 504);
		panel2.setBackground(new Color(255, 204, 153));
		panel2.setBorder(new EmptyBorder(5, 5, 5, 5));
		frmHerramientaDeTesting.getContentPane().add(panel2);
		panel2.setLayout(null);

		PanelAnalisis = new JPanel();
		PanelAnalisis.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"An\u00E1lisis del M\u00E9todo", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12), new Color(0, 0, 0)));
		PanelAnalisis.setBackground(new Color(255, 204, 153));
		PanelAnalisis.setBounds(555, 11, 209, 482);
		panel2.add(PanelAnalisis);
		PanelAnalisis.setLayout(null);

		lblCantidadDeLneas = new JLabel("Cantidad de l\u00EDneas de c\u00F3digo:");
		lblCantidadDeLneas.setHorizontalAlignment(SwingConstants.CENTER);
		lblCantidadDeLneas.setBounds(10, 22, 189, 20);
		PanelAnalisis.add(lblCantidadDeLneas);

		nroLineas = new JLabel("");
		nroLineas.setHorizontalAlignment(SwingConstants.CENTER);
		nroLineas.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroLineas.setBounds(10, 43, 189, 25);
		PanelAnalisis.add(nroLineas);

		nroLineasComentadas = new JLabel("");
		nroLineasComentadas.setHorizontalAlignment(SwingConstants.CENTER);
		nroLineasComentadas.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroLineasComentadas.setBounds(10, 100, 189, 25);
		PanelAnalisis.add(nroLineasComentadas);

		lblCantidadComentarios = new JLabel("Cantidad de l\u00EDneas con comentarios:");
		lblCantidadComentarios.setHorizontalAlignment(SwingConstants.CENTER);
		lblCantidadComentarios.setBounds(10, 79, 189, 20);
		PanelAnalisis.add(lblCantidadComentarios);

		porcentajeComentarios = new JLabel("");
		porcentajeComentarios.setHorizontalAlignment(SwingConstants.CENTER);
		porcentajeComentarios.setFont(new Font("Tahoma", Font.BOLD, 15));
		porcentajeComentarios.setBounds(10, 157, 189, 25);
		PanelAnalisis.add(porcentajeComentarios);

		lblPorcentajeDeLneas = new JLabel("Porcentaje de l\u00EDneas comentadas:");
		lblPorcentajeDeLneas.setHorizontalAlignment(SwingConstants.CENTER);
		lblPorcentajeDeLneas.setBounds(10, 136, 189, 20);
		PanelAnalisis.add(lblPorcentajeDeLneas);

		nroComplejidad = new JLabel("");
		nroComplejidad.setHorizontalAlignment(SwingConstants.CENTER);
		nroComplejidad.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroComplejidad.setBounds(10, 214, 189, 25);
		PanelAnalisis.add(nroComplejidad);

		lblComplejidadCiclomtica = new JLabel("Complejidad Ciclom\u00E1tica:");
		lblComplejidadCiclomtica.setHorizontalAlignment(SwingConstants.CENTER);
		lblComplejidadCiclomtica.setBounds(10, 193, 189, 20);
		PanelAnalisis.add(lblComplejidadCiclomtica);

		lblFanIn = new JLabel("Fan In:");
		lblFanIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanIn.setBounds(10, 244, 94, 20);
		PanelAnalisis.add(lblFanIn);

		nroFanIn = new JLabel("");
		nroFanIn.setHorizontalAlignment(SwingConstants.CENTER);
		nroFanIn.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroFanIn.setBounds(10, 275, 94, 25);
		PanelAnalisis.add(nroFanIn);

		nroFanOut = new JLabel("");
		nroFanOut.setHorizontalAlignment(SwingConstants.CENTER);
		nroFanOut.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroFanOut.setBounds(105, 275, 94, 25);
		PanelAnalisis.add(nroFanOut);

		lblFanOut = new JLabel("Fan Out:");
		lblFanOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanOut.setBounds(105, 244, 94, 20);
		PanelAnalisis.add(lblFanOut);

		lblLongitudDeHalstead = new JLabel("Longitud de Halstead:");
		lblLongitudDeHalstead.setHorizontalAlignment(SwingConstants.CENTER);
		lblLongitudDeHalstead.setBounds(10, 311, 189, 20);
		PanelAnalisis.add(lblLongitudDeHalstead);

		nroLongitud = new JLabel("");
		nroLongitud.setHorizontalAlignment(SwingConstants.CENTER);
		nroLongitud.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroLongitud.setBounds(10, 342, 189, 25);
		PanelAnalisis.add(nroLongitud);

		lblVolumenDeHalstead = new JLabel("Volumen de Halstead:");
		lblVolumenDeHalstead.setHorizontalAlignment(SwingConstants.CENTER);
		lblVolumenDeHalstead.setBounds(10, 378, 189, 20);
		PanelAnalisis.add(lblVolumenDeHalstead);

		nroVolumen = new JLabel("");
		nroVolumen.setHorizontalAlignment(SwingConstants.CENTER);
		nroVolumen.setFont(new Font("Tahoma", Font.BOLD, 15));
		nroVolumen.setBounds(10, 409, 189, 25);
		PanelAnalisis.add(nroVolumen);

		//PanelHalstead = new JPanel();
		//PanelHalstead.setBounds(10, 11, 535, 482);
		//PanelHalstead.setLayout(null);

		PanelFuente = new JPanel();
	
		PanelFuente.setBounds(10, 11, 535, 482);
		
		PanelFuente.setLayout(null);
		textoFuente = new JTextPane();
		textoFuente.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textoFuente.setEditable(false);
		textoFuente.setBounds(0, 11, 513, 438);
		
		JScrollPane sp = new JScrollPane(textoFuente);
		sp.setBounds(0, -1, 530, 454);
		sp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setViewportBorder(null);
		sp.setBorder(null);
		PanelFuente.add(sp);
		
		PanelReco = new JPanel();
		PanelReco.setBounds(10, 11, 535, 482);//11
		PanelReco.setLayout(null);
		textoReco = new JTextPane();
		textoReco.setEditable(false);
		textoReco.setBounds(0, 11, 513, 438);//11
		JScrollPane sp2 = new JScrollPane(textoReco);
		sp2.setBounds(0, -1, 530, 454);
		sp2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp2.setViewportBorder(null);
		sp2.setBorder(null);
		PanelReco.add(sp2);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 535, 482);
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 11));
		tabbedPane.addTab("CÓDIGO FUENTE", PanelFuente);
		//tabbedPane.addTab("Halstead", PanelHalstead);
		tabbedPane.addTab("RECOMENDACIONES", PanelReco);
		tabbedPane.setEnabledAt(1, false);

		/*lblOperadoresUnicos = new JLabel("Operadores Unicos:");
		lblOperadoresUnicos.setBounds(10, 11, 95, 14);
		PanelHalstead.add(lblOperadoresUnicos);*/

		/*lblCantOperad = new JLabel("");
		lblCantOperad.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCantOperad.setBounds(115, 11, 46, 14);
		PanelHalstead.add(lblCantOperad);*/

		/*lblCantOperand = new JLabel("");
		lblCantOperand.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCantOperand.setBounds(115, 36, 46, 14);
		PanelHalstead.add(lblCantOperand);*/

		/*lblOperandosUnicos = new JLabel("Operandos Unicos:");
		lblOperandosUnicos.setBounds(10, 36, 91, 14);
		PanelHalstead.add(lblOperandosUnicos);*/

		/*btnVerOperadoresY = new JButton("Ver operadores y operandos considerados");
		btnVerOperadoresY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog popup = new JDialog(frmHerramientaDeTesting, "Operadores y Operandos", true);
				JLabel l1 = new JLabel(
						"<html><b>Se considera Operador:</b><br>while if for foreach case default continue goto catch<br>? ! = * / + - < >++ -- == && <= >= || !=<br><br><b>Se considera Operando:</b><br>Literales y variables de los siguientes tipos:<br>int string float char double Integer String Character Double bool Boolean File");
				popup.setSize(320, 200);
				popup.setResizable(false);
				popup.getContentPane().add(l1);
				popup.pack();
				popup.setLocationRelativeTo(frmHerramientaDeTesting);
				popup.setVisible(true);

			}
		});
		btnVerOperadoresY.setBounds(264, 7, 235, 23);
		PanelHalstead.add(btnVerOperadoresY);*/
		
		
		/*datosOperad = new DefaultTableModel(operadores,new String[] {"Operadores","Cantidad"});
		tablaOperadores = new JTable(datosOperad);
		tablaOperadores.setEnabled(false);
		tablaOperadores.setBounds(10, 61, 510, 190);
		
		datosOperand = new DefaultTableModel(operandos,new String[] {"Operandos","Cantidad"});
		tablaOperandos = new JTable(datosOperand);
		tablaOperandos.setEnabled(false);
		tablaOperandos.setBounds(10, 262, 510, 190);
		
		JScrollPane spTabla1 = new JScrollPane(tablaOperadores);
		JScrollPane spTabla2 = new JScrollPane(tablaOperandos);
		spTabla1.setBounds(10, 61, 510, 190);
		spTabla1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spTabla1.setViewportBorder(null);
		spTabla1.setBorder(null);
		spTabla2.setBounds(10, 262, 510, 190);
		spTabla2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spTabla2.setViewportBorder(null);
		spTabla2.setBorder(null);
		
		PanelHalstead.add(spTabla1);
		PanelHalstead.add(spTabla2);*/
		
		panel2.add(tabbedPane);

		lblArchivo = new JLabel("Lista de Archivos");
		lblArchivo.setBounds(20, 11, 120, 14);
		lblArchivo.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmHerramientaDeTesting.getContentPane().add(lblArchivo);

		lblClase = new JLabel("Lista de Clases");
		lblClase.setBounds(210, 11, 120, 14);
		lblClase.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmHerramientaDeTesting.getContentPane().add(lblClase);

		lblMtodo = new JLabel("Lista de Metodos");
		lblMtodo.setBounds(400, 11, 120, 14);
		lblMtodo.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmHerramientaDeTesting.getContentPane().add(lblMtodo);
		
		
		listaMetodos = new JComboBox<String>();
		listaMetodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listaMetodos.getSelectedIndex() == -1)
					return;
				
				String recomendaciones = "";
				Metodo metodo = analizador.getMetodo(listaMetodos.getSelectedIndex());
				operadores = metodo.datosOperador();
				operandos = metodo.datosOperando();
				/*datosOperad.setDataVector(operadores,new String[] {"Operadores","Cantidad"});
				datosOperand.setDataVector(operandos,new String[] {"Operandos","Cantidad"});
				tablaOperadores.setEnabled(true);
				tablaOperandos.setEnabled(true);	
				lblCantOperad.setText(Integer.toString(tablaOperadores.getRowCount()));
				lblCantOperand.setText(Integer.toString(tablaOperandos.getRowCount()));*/
				textoFuente.setContentType("text/html");
				textoFuente.setText(
						"<html><pre><code>" + metodo.getCodigo() + "</code></pre></html>");
				nroLineas.setText(metodo.getLineas());
				nroLineasComentadas.setText(metodo.getComentarios());
				porcentajeComentarios.setText(metodo.getPorcentaje());
				if(metodo.getPorcentajeD()<30)
					recomendaciones += Constantes.RECO_COMENTARIOS;
					
				nroComplejidad.setText(metodo.getComplejidad());
				if(metodo.getComplejidadI()>10)
					recomendaciones += Constantes.RECO_CC;
				nroFanIn.setText(analizador.getFanIn(listaMetodos.getSelectedIndex()));
				nroFanOut.setText(analizador.getFanOut(listaMetodos.getSelectedIndex()));
				if(Integer.parseInt(analizador.getFanOut(listaMetodos.getSelectedIndex()))>(metodosArchivo.size()/2))
					recomendaciones += Constantes.RECO_FANOUT;
				nroLongitud.setText(metodo.longitudHalstead());
				if(metodo.calcularLongitud()/2>=Integer.parseInt(metodo.getLineas()))
					recomendaciones+= Constantes.RECO_HALSTEAD;
				nroVolumen.setText(metodo.volumenHalstead());
				textoReco.setText(recomendaciones);
				if(textoReco.getText() == "") {
					tabbedPane.setEnabledAt(2, false);
				}
				else
					tabbedPane.setEnabledAt(1, true);
				
			}
		});
		listaMetodos.setMaximumRowCount(8);
		listaMetodos.setBounds(400, 25, 155, 20);
		listaMetodos.setEnabled(false);
		listaMetodos.setEditable(false);
		frmHerramientaDeTesting.getContentPane().add(listaMetodos);

		listaClases = new JComboBox<String>();
		listaClases.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listaClases.getSelectedIndex() == -1)
					return;
				listaMetodos.removeAllItems();
				metodosArchivo = analizador.encontrarMetodos(listaClases.getSelectedIndex());
				for (String string : metodosArchivo) {
					listaMetodos.addItem(string);
				}
				listaMetodos.setEnabled(true);
			}
		});
		listaClases.setMaximumRowCount(8);
		listaClases.setEnabled(false);
		listaClases.setEditable(false);
		listaClases.setBounds(210, 25, 155, 20);
		frmHerramientaDeTesting.getContentPane().add(listaClases);

		listaArchivos = new JComboBox<String>();
		listaArchivos.setMaximumRowCount(8);
		listaArchivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listaArchivos.getSelectedIndex() == -1)
					return;
				fuenteAct = listaCodigosFuente.get(listaArchivos.getSelectedIndex());
				listaClases.removeAllItems();
				try {
					clasesArchivo = analizador.encontrarClases(fuenteAct);
					for (String string : clasesArchivo) {
						listaClases.addItem(string);
					}
					listaClases.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		listaArchivos.setBounds(20, 25, 155, 20);
		listaArchivos.setEnabled(false);
		listaArchivos.setEditable(false);

		frmHerramientaDeTesting.getContentPane().add(listaArchivos);

		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cargarArchivos();

			}
		});
		
		mntmSalir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});

	}
	
	public void cargarArchivos(){
		
		JFileChooser archivos = new JFileChooser();
		archivos.setCurrentDirectory(null);
		archivos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		archivos.setDialogTitle("Seleccione carpeta del código fuente");
		archivos.setAcceptAllFileFilterUsed(false);
		int ret = archivos.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			listaArchivos.removeAllItems();
			listaArchivos.setEnabled(false);
			listaMetodos.removeAllItems();
			listaMetodos.setEnabled(false);
			listaClases.removeAllItems();
			listaClases.setEnabled(false);
			//datosOperad.setDataVector(null,new String[] {"Operadores","Cantidad"});
			//datosOperand.setDataVector(null,new String[] {"Operandos","Cantidad"});
			textoFuente.setText("");;
			tabbedPane.setEnabledAt(1, false);
			nroComplejidad.setText("");
			nroFanIn.setText("");
			nroFanOut.setText("");
			nroLineas.setText("");
			nroLineasComentadas.setText("");
			nroLongitud.setText("");
			nroVolumen.setText("");
			porcentajeComentarios.setText("");
			//lblCantOperad.setText("");
			//lblCantOperand.setText("");
			
			listaCodigosFuente.clear();
			frmHerramientaDeTesting
					.setTitle("Herramienta de Testing - " + archivos.getSelectedFile().getPath());

			for (File f : archivos.getSelectedFile().listFiles(fuenteJava)) {
				listaCodigosFuente.add(f);
				listaArchivos.addItem(f.getName());
				
			}
			if(!listaCodigosFuente.isEmpty())
			listaArchivos.setEnabled(true);
			else {
				JOptionPane.showMessageDialog(frmHerramientaDeTesting,
					    "La carpeta seleccionada no contiene archivos .java",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
