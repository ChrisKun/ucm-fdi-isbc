package proyecto2.quinielas.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import proyecto2.quinielas.Config;

import junit.awtui.ProgressBar;


public class Interfaz extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//constantes (para recorrer la tabla)
	public final static int LOCAL = 0;
	public final static int VISITANTE = 1;
	public final static int RESULTADO = 2;
	public final static int NUM_EQU = 15;
	
	// Información que podría inferirse de los archivos de texto
	
	
	public static int modo_tabla = 0; // modo de un partido (0) o modo de una jornada con los 15 partidos(1) a rellenar
	
	/** Configuracion de pantalla */
	public final static int W = 800;
	public final static int H = 600;
	
	/** Número de jornadas totales para primera y segunda división */
	public final static int PRIMERA_DIVISION = 0;
	public final static int SEGUNDA_DIVISION = 1;
	
	// Elementos de la barra de información que hay que modificar
	private JLabel jlab_jornada;
	JComboBox[] comboBox_jornada; // tiene guardado los 2 desplegables de las jornadas
	

	JRadioButton rbutt_uno; // seleccion de un partido
	JRadioButton rbutt_var; // seleccion de varios partidos
	// Elementos en la tabla que hay que modificar
	private JPanel panelTabla;
	private JComboBox[] comboBoxLocales;
	private JComboBox[] comboBoxVisitantes;
	private JTextField[] resultados;
	private JProgressBar[] confianza;
	
	// Panel principal que hay que modificar al cambiar de modo de tabla
	private JPanel panelPrincipal;
	
	// Datos de la tabla
	private String[][] datosTabla; 
	private double[] datosPesos;
	
	// Datos de configuración
	private Config conf;
	
	
	//botones
	private JButton jplog_actualizar;
	
	//private DefaultTableModel modelo;
	//private ArrayList<String> equipos;
	

	
	public Interfaz(double[] ds, Config c)
	{
		//DATOS POR DEFECTO, INICIALIZACION
		
		jlab_jornada = new JLabel();
		comboBox_jornada = new JComboBox[2];
		setDatosPesos(ds);
		conf = c;
		
		
		
		// Configuración de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas");
		// Falta poner icono :D
		this.setSize(W, H);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setContentPane((getPanelPrincipal()));
		this.setJMenuBar(getMenuPrincipal());
		this.validate();

	}
	
	private JMenuBar getMenuPrincipal()
	{
		/** FALTA ACTION LISTENER Y COMPLETAR MENU **/
		JMenuBar menuPrincipal = new JMenuBar();
		// Menu archivo
		JMenu m_arch = new JMenu("Archivo");
		menuPrincipal.add(m_arch);
			// Elementos del menu archivo
			JMenuItem it_salir = new JMenuItem("Salir");
			m_arch.add(it_salir);
			
		// Menu sobre
		JMenu m_sobr = new JMenu("Sobre");
		menuPrincipal.add(m_sobr);
		
		return menuPrincipal;
	}
	
	private JPanel getPanelPrincipal()
	{
		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.add(getPanelInformacion(), BorderLayout.NORTH);
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.add(getPanelLog(), BorderLayout.SOUTH);
		panelPrincipal.validate();
		return panelPrincipal;
	}
	
	/** PANEL INFORMACIÓN:
	 * Muestra la temporada actual y la jornada.
	 * A través de un desplegable permite seleccionar la temporada y la jornada a consultar.
	 * A través de un RadioButton permite seleccionar un partido o una jornada a rellenar.
	**/
	private JPanel getPanelInformacion()
	{
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3,1));
		p.add(getJLabelTemporadaJornada()); //En la parte superior
		p.add(getSeleccionTemporadaJornada()); //En la parte de media
		p.add(getSeleccionUnoVariosPartidos()); //En la parte inferior
		return p;
	}
	
	// Subpanel del panel información para seleccionar uno o la jornada completa para rellenar
	private JPanel getSeleccionUnoVariosPartidos()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel label = new JLabel("Rellenar: ");
		p.add(label);
		// Botones de selección
		ButtonGroup bg = new ButtonGroup(); //grupo de botones: solo seleccionable 1
		rbutt_uno = new JRadioButton("Sólo un partido");
		rbutt_var = new JRadioButton("Todos los partidos");
		
		rbutt_uno.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  actualizarListaPartidos();
		    	  }
		      });
		
		rbutt_var.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  actualizarListaPartidos();
		    	  }
		      });
		
		rbutt_uno.setSelected(true);
		
		// Creamos la relación lógica entre los botones
		bg.add(rbutt_uno);
		bg.add(rbutt_var);
		
		// Añadimos los botones al panel
		p.add(rbutt_uno);
		p.add(rbutt_var);
		
		return p;
	}
	
	// Subpanel de Temporada y Jornada con ComboBox desplegables
	private JPanel getSeleccionTemporadaJornada()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		JLabel labelTemp = new JLabel("Temporada: ");
		p.add(labelTemp);
		p.add(getDesplegableTemporada());
		
		JLabel labelJor = new JLabel("Jornada Primera División: ");
		p.add(labelJor);
		
		p.add(getDesplegableJornada(PRIMERA_DIVISION)); 
		
		JLabel labelJor2 = new JLabel("Jornada Segunda División: ");
		p.add(labelJor2);
		
		p.add(getDesplegableJornada(SEGUNDA_DIVISION));
		
		return p;
	}
	
	private JComboBox getDesplegableTemporada()
	{
		int num_temporadas = conf.getUltimaTemporada() - 2000 + 1;
		String[] nom_temp = new String[num_temporadas];
		for (int i = 0; i <num_temporadas; i++)
			nom_temp[i] = (2000+i)+"-"+(2000+(i+1));
		
		JComboBox b = new JComboBox(nom_temp);
		b.addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		actualizarTemporada(e);
		    	      }
		      });
		return b;
	}
	
	private JComboBox getDesplegableJornada(int division)
	{
		int n_max = 0; //número máximo de la jornada
		switch (division)
		{
			case PRIMERA_DIVISION: n_max = conf.JORNADASPRIMERA; break;
			case SEGUNDA_DIVISION: n_max = conf.JORNADASSEGUNDA; break;
		}
		
		// Actualizamos el valor si es la temporada actual:
		// Como máximo se muestra la jornada actual + 1
		if (conf.getSeleccionTemporada() == conf.getUltimaTemporada())
		{
			switch (division)
			{
				case PRIMERA_DIVISION: n_max = conf.getUltimaJornadaPrimera(); break;
				case SEGUNDA_DIVISION: n_max = conf.getUltimaJornadaSegunda(); break;
			}
		}
		comboBox_jornada[division] = new JComboBox();	
		
		comboBox_jornada[division].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		actualizarJornada(e); 
		    	      }
		      });
		setNumElemComboBoxJornada(n_max,division);	
		return comboBox_jornada[division];
	}
	
	/** Selecciona el número de elementos que tendra el desplegable de la jornada */
	private void setNumElemComboBoxJornada(int n,int division)
	{
		comboBox_jornada[division].removeAllItems();
		for (int i = 0; i < n; i++)
		{
			comboBox_jornada[division].addItem(""+(i+1));
		}
	}
	
	/**
	 * Tabla que permite introducir al usuario los equipos de la query. También tiene
	 * dos columnas adicionales donde se muestra el resultado de la solución y la medida de 
	 * confianza.
	 */
	
	private JPanel getJPanelTabla(int modo)
	{
		int numF = NUM_EQU+1;
		panelTabla = new JPanel();
		if (modo == 0) // un solo partido
			numF = 2;
		panelTabla.setLayout(new GridLayout(numF,4));
		// Añadimos los elementos de la primera fila 
		JLabel j = new JLabel("Equipo Local");
		panelTabla.add(j);
		JLabel p = new JLabel("Equipo Visitante");
		panelTabla.add(p);
		JLabel s = new JLabel("Resultado");
		panelTabla.add(s);
		JLabel o = new JLabel("Confianza");
		panelTabla.add(o);
		
		// añadimos los equipos correspondientes
		inicializarDatosPartidos(numF-1);
		inicializarDesplegablesEquipoTabla(numF-1);
		inicializarResultados(numF-1);
		inicializarConfianza(numF-1);
		
		for (int i = 0; i < numF-1; i++)
		{
			panelTabla.add(comboBoxLocales[i]);
			panelTabla.add(comboBoxVisitantes[i]);
			panelTabla.add(resultados[i]);
			panelTabla.add(confianza[i]);
		}
		
		return panelTabla;
	}
	
	/** Incializa la tabla de datos intermedia, que será modificada por la interfaz y
	 *  por el resto del programa
	 */
	
	private void inicializarDatosPartidos(int numFilas)
	{
		datosTabla = new String[numFilas][4];
		// TODO SET DATOS POR DEFECTO leer de la estructura pasada por parametro con equipos temporada = 2012 y jornadas 1 y 1
		// Datos por defecto para resultado y confianza a "-1"
	}
	
	private void inicializarDesplegablesEquipoTabla(int numFilas)
	{
		
		comboBoxLocales = new JComboBox[numFilas];
		comboBoxVisitantes = new JComboBox[numFilas];
		
		for (int i = 0; i < numFilas; i++)
		{
			setEquiposComboBox(i,conf.getSeleccionTemporada(), conf.getSeleccionJornadaPrimera(), conf.getSeleccionJornadaSegunda());
		}
	}
	
	private void setEquiposComboBox(int num, int num_temporada2, int num_jornada_primera2, int num_jornada_segunda2)
	{
		// TODO Leer de la tabla de datos
		int numE = 20;// DEFAULT
		String[] eq = new String[numE];
		for (int i = 0; i < numE; i++)
		{
			eq[i] = "Equipo "+i;
		}
		comboBoxLocales[num] = new JComboBox(eq);
		
		comboBoxLocales[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		setDatoTablaLocales(e);
		    	      }
		});
		comboBoxVisitantes[num] = new JComboBox(eq);
		// TODO Hacer un metodo setDatoTablaVisitantes para modificar los datos de los visitantes
	}
	
	private void inicializarResultados(int numFilas)
	{
		// TODO leer de la tabla de datos
		resultados = new JTextField[numFilas];
		
		for (int i = 0; i < numFilas; i++)
		{
			resultados[i] = new JTextField("N/D");
			resultados[i].setEditable(false);
			resultados[i].setAlignmentX(CENTER_ALIGNMENT);
		}
	}
	
	private void inicializarConfianza(int numFilas)
	{
		// Leer de la tabla de datos
		confianza = new JProgressBar[numFilas];
		
		for (int i = 0; i < numFilas; i++)
		{
			confianza[i] = new JProgressBar();
			//confianza[i].setValue(35);
			confianza[i].setStringPainted(true);
			confianza[i].setString("N/D");
		//	confianza[i].setString(confianza[i].getValue()+"%");
		}
	}
	


	
	// Panel donde está el log para cargar resultados, una barra para cargar y los botones
	/** RETOCAR MÉTODO: AHORA ESTÁ CHAPUZA **/
	private JPanel getPanelLog()
	{
		// Definición del panel
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());

		
		// LOG : información

		// Barra de progreso
		//jplog_barra = new JProgressBar();
		//jplog_barra.setValue(0);
		//p.add(jplog_barra);
		
		// Boton de Actualizar
		jplog_actualizar = new JButton("Consultar");
		jplog_actualizar.addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent evt ) {
		    		actualizarEstructura();
		    	      }
		      });
		p.add(jplog_actualizar);
		return p;
	}
	
	private JPanel getJLabelTemporadaJornada()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		setJornadaAño(conf.getSeleccionJornadaPrimera(), conf.getSeleccionJornadaSegunda(),conf.getSeleccionTemporada());
		p.add(jlab_jornada);
		return p;
	}
	
	/** FUNCIONES PARA COMUNICARSE CON EL RESTO DEL PROGRAMA **/
	// Crear método que cree un arrayList con la estructura "Equipo0,Equipo1" de todos los
	// partidos que se van a jugar...

	
	/** FUNCIONES PARA EDITAR INFORMACIÓN DE LA INTERFAZ**/
	// EDITAR JORNADA Y AÑO
	public void setJornadaAño(int jornada_p, int jornada_s, int año)
	{
		// Font("Titulo fuente", "Atributos", "Tamaño"
		jlab_jornada.setFont(new Font("Verdana", Font.BOLD, 22));
		jlab_jornada.setText("Temporada: "+año+"-"+(año+1)+" ; Jornada 1ºDiv : "+jornada_p+" ; Jornada 2ºDiv : "+jornada_s);
	}

	// Métodos Listener
	public void actualizarEstructura()
	{
		//deTablaAEstructura();
	}
	
	public void actualizarJornada(ActionEvent e)
	{
		int n_jornada = 1;
		@SuppressWarnings("rawtypes")
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    if (newSelection != null)
	    {
	    	n_jornada = Integer.parseInt(newSelection);
	    	if (cb == comboBox_jornada[PRIMERA_DIVISION])
	    		conf.setSeleccionJornadaPrimera(n_jornada);
	    	else
	    		conf.setSeleccionJornadaSegunda(n_jornada);
	    		
	    }
	    setJornadaAño(conf.getSeleccionJornadaPrimera(),conf.getSeleccionJornadaSegunda(),conf.getSeleccionTemporada());
	    // TODO ACTUALIZAR COMBOBOX DE EQUIPOS A SELECCIONAR
	}
	
	public void actualizarTemporada(ActionEvent e)
	{	
		@SuppressWarnings("rawtypes")
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    // Nos quedamos solo con los 4 primeros digitos de la temporada 
	    conf.setSeleccionTemporada(Integer.parseInt(newSelection.substring(0, 4)));
	    
	    
		if (conf.getSeleccionTemporada().equals(conf.getUltimaTemporada()))
		{
			setNumElemComboBoxJornada(conf.getUltimaJornadaPrimera(),PRIMERA_DIVISION);
			setNumElemComboBoxJornada(conf.getUltimaJornadaSegunda(),SEGUNDA_DIVISION);
		}
		else
		{
			setNumElemComboBoxJornada(conf.JORNADASPRIMERA,PRIMERA_DIVISION);
			setNumElemComboBoxJornada(conf.JORNADASSEGUNDA,SEGUNDA_DIVISION);
		}
		conf.setSeleccionJornadaPrimera(1);
		conf.setSeleccionJornadaSegunda(1);
	    setJornadaAño(conf.getSeleccionJornadaPrimera(), conf.getSeleccionJornadaSegunda(),conf.getSeleccionTemporada());
	 // TODO ACTUALIZAR COMBOBOX DE EQUIPOS A SELECCIONAR
	}

	public void actualizarListaPartidos()
	{
		panelPrincipal.remove(panelTabla);
		if (rbutt_uno.isSelected())
			modo_tabla = 0; // Modo de un sólo partido
		else
			modo_tabla = 1; // Modo de 15 partidos
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.validate();
		// TODO ACTUALIZAR COMBOBOX DE EQUIPOS A SELECCIONAR
	}
	
	private void setDatoTablaLocales(ActionEvent e)
	{
		//Hay que identificar que comboBox es el que ha sido seleccionado
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    for (int i = 0; i < 1; i++)
	    	if (comboBoxLocales[i] == cb) //tenemos el comboBox que buscabamos
	    		datosTabla [i][LOCAL] = newSelection;
	}

	public double[] getDatosPesos() {
		return datosPesos;
	}

	public void setDatosPesos(double[] ds) {
		this.datosPesos = ds;
	}
	
	

}
