package proyecto2.quinielas.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import jcolibri.exception.ExecutionException;

import proyecto2.quinielas.Principal;
import proyecto2.quinielas.cbr.Prediccion;
import proyecto2.quinielas.cbr.Quinielas;
import proyecto2.quinielas.cbr.ValidacionCruzada;
import proyecto2.quinielas.datosWeb.Clasificacion;
import proyecto2.quinielas.datosWeb.ParserWeb;

import junit.awtui.ProgressBar;


public class Interfaz extends JFrame{
	
	/**
	 * TODO eleccion de votacion
	 */
	private static final long serialVersionUID = 1L;

	/* Constantes */
	public final static int LOCAL = 0;
	public final static int VISITANTE = 1;
	public final static int RESULTADO = 2;
	public final static int NUM_EQU = 15;
	public final static int MODOUNPARTIDO = 0;
	
	/* Configuracion de pantalla */
	public final static int W = 840;
	public final static int H = 600;
	
	/* Número de jornadas totales para primera y segunda división */
	public final static int PRIMERA_DIVISION = 0; 
	public final static int SEGUNDA_DIVISION = 1;
	
	/* Modo de la tabla (0) para un partido y (1) para todos	 */
	private static int modo_tabla = 0; // modo de un partido (0) o modo de una jornada con los 15 partidos(1) a rellenar
	private static int modo_partido = 0;
	private static int num_partidos_primera = 10;
	
	/* Elementos para la seleccion de temporada y jornadas */
	 private int selTemporada;
	 private int selJornadaPrimera;
	 private int selJornadaSegunda;
	 private boolean tipoVotacionPonderada;
	
	
	/** Elementos del panel superior de información que tienen que modificarse */
	private JLabel jlab_jornada;
	JComboBox[] comboBox_jornada; // tiene guardado los 2 desplegables de las jornadas
	JRadioButton rbutt_uno; // seleccion de un partido
	JRadioButton rbutt_var; // seleccion de varios partidos
	JComboBox desplegableNumPartidosPrimera;
	
	/** Elementos del panel que contiene la tabla de los partidos que tienen que modificarse*/
	private JPanel panelTabla;
	private JComboBox[] comboBoxLocales;
	private JComboBox[] comboBoxVisitantes;
	private JTextField[] resultados;
	private JProgressBar[] confianza;
	JRadioButton rbutt_primera; // selección primera división
	JRadioButton rbutt_segunda; // selección segunda división
	
	/** Panel principal (se modifica cuando se cambia de tabla */
	private JPanel panelPrincipal;
	
	/** Elementos del menu que se modifican */
	JRadioButtonMenuItem itemMBasica;
	JRadioButtonMenuItem itemMPonderada; 
	
	// Datos de la tabla
	ArrayList<Prediccion> respuestaPrimera;
	ArrayList<Prediccion> respuestaSegunda;
	
	// Datos de los pesos
	private double[] datosPesos;
	private JTextField[] campoPesos;
	private JSlider[] sliderPesos; // para poder escucharlos
	
	//Quinielas
	private Quinielas q;
	
	//Información del parser
	ParserWeb parser;
	
	//Interfaz
	private Interfaz interfaz;
	
	/**
	 * CONSTRUCTORA
	 * @param ds lista de pesos
	 * @param q quinielas con información para consultar
	 * @param p información recolectada por el ParserWeb
	 */
	public Interfaz(double[] ds,Quinielas q,ParserWeb p)
	{
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		 int width = pantalla.width;
		 this.setBounds(width/2 - W/2,0, W, H);
		// Inicialización
		jlab_jornada = new JLabel();
		comboBox_jornada = new JComboBox[2];
		setDatosPesos(ds);
		campoPesos = new JTextField[datosPesos.length];
		sliderPesos = new JSlider[datosPesos.length];
		this.q = q;
		parser = p;
		interfaz = this;
		
		selTemporada = ParserWeb.TEMPORADAINICIAL;
		selJornadaPrimera = 1;
		selJornadaSegunda = 1;
		tipoVotacionPonderada = false;
		
		// Configuración de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas");
		this.setSize(W, H);
		

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setContentPane((getPanelPrincipal()));
		this.setJMenuBar(getMenuPrincipal());
		this.validate();

	}

	/****** METODOS PARA CONSTRUIR EL MENU DE LA INTERFAZ ******/
	
	/**
	 * Método que obtiene la barra del menú principal, que se va construyendo con los distintos menus
	 * @return JMenuBar
	 */
	private JMenuBar getMenuPrincipal()
	{
		ListenerMenu listener = new ListenerMenu();
		// Creamos un unico listener para todos los elementos del menú
		JMenuBar barraMenuPrincipal = new JMenuBar();
		// Menu Archivo: Sobre, Salir
		barraMenuPrincipal.add(getMenuArchivo(listener));
		// Menu Eficiencia: Opciones para medir la eficiencia
		barraMenuPrincipal.add(getMenuEficiencia(listener));
		// Menu Configuración: Con dos submenus: uno para la media y otro para la validacion
		barraMenuPrincipal.add(getMenuConfiguracion());
		return barraMenuPrincipal;
	}
	
	/**
	 * Método que obtiene el menú Archivo, con las opciones de Sobre (sobre la aplicación) y Salir, que sale de la
	 * aplicación
	 * @return JMenu Archivo
	 */
	private JMenu getMenuArchivo(ListenerMenu l)
	{
		JMenu menu = new JMenu("Archivo");
		// Elementos del menú Archivo:
			// Item Menu
			JMenuItem itemSobre = new JMenuItem("Sobre");
			// Listener del item: Muestra información sobre el programa
			itemSobre.addActionListener(l);
			// Item Salir
			JMenuItem itemSalir = new JMenuItem("Salir");
			// Listener del item: Sale del programa
			itemSalir.addActionListener(l);
		// Por último, los añadimos al menú
		menu.add(itemSobre);
		menu.add(itemSalir);
		return menu;
	}
	
	private JMenu getMenuEficiencia(ListenerMenu l)
	{
		JMenu menu = new JMenu("Eficiencia");
		// Elementos del menú Eficiencia
			//Item HoldOutEvaluation
			JMenuItem itemHoldOut = new JMenuItem("Hold Out Evaluation"); 
			itemHoldOut.addActionListener(l);
			//Item LeaveOneOutEvaluation
			JMenuItem itemLeaveOne = new JMenuItem("Leave One Out Evaluation");
			itemLeaveOne.addActionListener(l);
			//Item SameSplitEvaluation
			JMenuItem itemSameSplit = new JMenuItem("Same Split Evaluation");
			itemSameSplit.addActionListener(l);
			//Item N-Fold
			JMenuItem itemNFold = new JMenuItem("N-Fold Evaluation");
			itemNFold.addActionListener(l);
		menu.add(itemHoldOut);
		menu.add(itemLeaveOne);
		menu.add(itemSameSplit);			
		menu.add(itemNFold);
		return menu;
	}
	
	private JMenu getMenuConfiguracion()
	{
		JMenu menu = new JMenu("Configuración");
		JMenu subMenu = new JMenu("Tipo de votación");
		//Items del menú
			itemMBasica = new JRadioButtonMenuItem("Básica");
			itemMBasica.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	  tipoVotacionPonderada = false;
			    	  }
			      });
			itemMPonderada = new JRadioButtonMenuItem("Ponderada");
			itemMPonderada.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	  tipoVotacionPonderada = true;
			    	  }
			      });
		//Ahora creamos el grupo de botones para que no haya dos seleccionados al mismo tiempo
			ButtonGroup group = new ButtonGroup();
			group.add(itemMBasica);
			group.add(itemMPonderada);
		// Por defecto seleccionado
		itemMBasica.setSelected(true);
		subMenu.add(itemMBasica);
		subMenu.add(itemMPonderada);
		menu.add(subMenu);
		return menu;
	}
	
	/****** METODOS PARA CONSTRUIR LOS PANELES DE LA INTERFAZ Y SUS ELEMENTOS ******/
	
	/**
	 * Se encargar de devolver el panel Principal que se construye a través de tres subpaneles: El subpanel del título,
	 * el subpanel con la tabla de equipos y resultados y por último, el subpanel con los pesos y los botones auxiliares.
	 * @return JPanel
	 */
	private JPanel getPanelPrincipal()
	{
		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.add(getPanelInformacion(), BorderLayout.NORTH);
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.add(getPanelBotonesYPesos(), BorderLayout.SOUTH);
		panelPrincipal.validate();
		return panelPrincipal;
	}
	

	/**
	 * Se encarga de devolver el subpanel superior con el título, y la selección de temporada y jornadas.
	 * @return JPanel
	 */
	private JPanel getPanelInformacion()
	{
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3,1));
		p.add(getJLabelTemporadaJornada()); 	//En la parte superior
		p.add(getSeleccionTemporadaJornada()); 	//En la parte de media
		p.add(getSeleccionUnoVariosPartidos()); //En la parte inferior
		return p;
	}
	
	/** Se encarga de devolver un subpanel (contenido en el subpanel del título) que permite seleccionar entre
	 * uno o varios partidos, actualizando los equipos.
	 * @return JPanel
	 */
	private JPanel getSeleccionUnoVariosPartidos()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel label = new JLabel("Rellenar: ");
		p.add(label);
		// Botones de selección
		ButtonGroup bg = new ButtonGroup(); 	//grupo de botones: solo seleccionable 1 al mismo tiempo
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
		
		// añadimos un JLabel para el numero de partidos
		JLabel jl = new JLabel("Partidos 1ºD: ");
		// Añadimos un comboBox con el número de partidos de primera y de segunda
		String[] num_par = new String[NUM_EQU-3];
		for (int i = 0; i < NUM_EQU-3; i++)
			num_par[i] = ""+(i+2);
		desplegableNumPartidosPrimera = new JComboBox(num_par);
		desplegableNumPartidosPrimera.setSelectedIndex(8);
		desplegableNumPartidosPrimera.setEnabled(false);
		desplegableNumPartidosPrimera.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  actualizarNumeroPartidos(e);
		    	  }
		      });
		p.add(jl);
		p.add(desplegableNumPartidosPrimera);
		
		return p;
	}
	
	/** 
	 *  Subpanel que está contenido en el subpanel de título y se encarga de construir la parte donde se permite
	 *  seleccionar con desplegables la temporada y las jornadas
	 * @return JPanel
	 */
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
	
	/**
	 * Obtiene la información sobre las temporadas y construye un desplegable que se añadirá al subpanel
	 * de título
	 * @return JComboBox
	 */
	private JComboBox getDesplegableTemporada()
	{
		int num_temporadas = parser.getUltimaTemporada() - ParserWeb.TEMPORADAINICIAL + 1;
		String[] nom_temp = new String[num_temporadas]; // Elementos que contendra el JComboBox
		for (int i = 0; i <num_temporadas; i++)
			nom_temp[i] = (ParserWeb.TEMPORADAINICIAL+i)+"-"+(ParserWeb.TEMPORADAINICIAL+(i+1)); // Formato "2000-2001" para temporada 2000
		
		JComboBox b = new JComboBox(nom_temp);
		b.addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		actualizarTemporada(e);
		    	      }
		      });
		return b;
	}
	
	/**
	 * Devuelve en un JComboBox las jornadas que se pueden seleccionar de primera o de segunda (teniendo en cuenta
	 * que si es la temporada actual, actualizar las jornadas máximas
	 * @param division (PRIMERA_DIVISION o SEGUNDA_DIVISION)
	 * @return JComboBox
	 */
	private JComboBox getDesplegableJornada(int division)
	{
		int n_max = 0; //número máximo de la jornada
		/* Comprobamos si se ha seleccionado la temporada actual para restringir el número
		   de jornadas */
		if (selTemporada == parser.getUltimaTemporada()) {
			switch (division){
				case PRIMERA_DIVISION: n_max = parser.getUltimaJornadaPrimera(); break;
				case SEGUNDA_DIVISION: n_max = parser.getUltimaJornadaSegunda(); break;
			}
		}
		else{
			switch (division){
			case PRIMERA_DIVISION: n_max = Principal.JORNADASPRIMERA; break;
			case SEGUNDA_DIVISION: n_max = Principal.JORNADASSEGUNDA; break;
			}
		}
		/* Creamos el Desplegable de la temporada correspondiente */
		comboBox_jornada[division] = new JComboBox();	
		
		comboBox_jornada[division].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		actualizarJornada(e); 
		    	      }
		      });
		/* Ponemos el número de elementos que tendrá */
		setNumElemComboBoxJornada(n_max,division);	
		return comboBox_jornada[division];
	}
	
	/**
	 * Construcción de la tabla que permite introducir al usuario los equipos de la query. También tiene
	 * dos columnas adicionales donde se muestra el resultado de la solución y la medida de 
	 * confianza.
	 * @param modo Modo de la tabla (0 = 1 partido, 1 = Todos los partidos [15])
	 * @return JPanel donde esta construida la tabla
	 */
	private JPanel getJPanelTabla(int modo)
	{
		int numF;  // Numero de filas que tendra nuestra "tabla" (por defecto 16)
		// Si el modo es el de un solo partido, serán 2 filas, una para la cabecera y otra para el contenido
		switch (modo){
			case MODOUNPARTIDO: numF = 2; break;
			default: numF = NUM_EQU+1; break;
		}
		// Creamos y configuramos el panel
		panelTabla = new JPanel();
		panelTabla.setLayout(new BorderLayout());
		// Creamos Subpaneles para la cabecera y el resto de elementos
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(numF,4));
		// Añadimos los elementos de la primera fila en el subpanel de cabecera 
		JLabel j; // Etiqueta auxiliar para completar la cabecera
		String str = "default"; // Auxiliar para añadir el nombre
		for (int i = 0; i < 4; i++)
		{
			switch(i)
			{
				case 0: str = "Equipo Local"; break;
				case 1: str = "Equipo Visitante"; break;
				case 2: str = "Resultado"; break;
				case 3: str = "Confianza"; break;
			}
			j = new JLabel(str, JLabel.CENTER);
			j.setFont(new Font(j.getFont().getFontName(), Font.BOLD, 18));
			p.add(j);
			
		}
		// Ahora creamos el resto de elementos que tendra la tabla
		inicializarDesplegablesEquipoTabla(numF-1);
		inicializarResultados(numF-1);
		inicializarConfianza(numF-1);
		// Añadimos los elementos creados anteriormente a la tabla
		for (int i = 0; i < numF-1; i++)
		{
			p.add(comboBoxLocales[i]);
			p.add(comboBoxVisitantes[i]);
			p.add(resultados[i]);
			p.add(confianza[i]);
		}
		// Añadimos este panel con la tabla al subpanel que contiene la tabla y otra información
		panelTabla.add(p,BorderLayout.CENTER);
		
		//Y Ahora, si esta el modo 0, añadimos un panel inferior con la información de primera o segunda division
		if (modo == 0)
			panelTabla.add(getPanelPrimeraOSegunda(), BorderLayout.NORTH);
		return panelTabla;
	}
	
	/**
	 * Subpanel que contiene los elementos que permiten seleccionar entre un partido de primera o un partido de segunda
	 * división. NOTA: Sólo está disponible si el modo de la tabla es de un partido.
	 * @return JPanel
	 */
	private JPanel getPanelPrimeraOSegunda()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel label = new JLabel("Partido: ");
		p.add(label);
		// Botones de selección
		ButtonGroup bg = new ButtonGroup(); //grupo de botones: solo seleccionable 1
		rbutt_primera = new JRadioButton("Primera División");
		rbutt_segunda = new JRadioButton("Segunda División");
		
		rbutt_primera.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  actualizarEquiposPrimeraOSegunda();
		    	  }
		      });
		
		rbutt_segunda.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  actualizarEquiposPrimeraOSegunda();
		    	  }
		      });
		if (modo_partido == 0)
			rbutt_primera.setSelected(true);
		else
			rbutt_segunda.setSelected(true);
		/* Creamos la relación lógica entre los botones */
		bg.add(rbutt_primera);
		bg.add(rbutt_segunda);
		/* Añadimos los botones al panel */
		p.add(rbutt_primera);
		p.add(rbutt_segunda);
		return p;
	}
	
	/**
	 * Rellena un ComboBox con los equipos que juegan en esa temporada y division 
	 * @param num comboBox que rellenamos
	 * @param p información recogida por el parser web
	 * @param primeraDivision (TRUE = primera división, FALSE = segunda división)
	 */
	private void setEquiposComboBox(int num, ParserWeb p, boolean primeraDivision)
	{
		int jor; // auxiliar para recorrer las jornadas
		ArrayList<Clasificacion> listaClasificacion = null;
		// Leer de la tabla de datos
		HashMap<String, ArrayList<Clasificacion>> clasificacion; // Almacenamiento temporal para el acceso a datos
		String[] eq; // Elementos posibles en el ComboBox
		int numEquipos; // Numero de equipos que hay (varian entre primera y segunda division)
		
		// Datos para segunda division (Así inicializamos las variables)
		numEquipos = Principal.NUMEROEQUIPOSSEGUNDA;
		clasificacion = parser.getClasPorJornSeg();
		
		// Datos para primera division
		if (primeraDivision)
		{
			numEquipos = Principal.NUMEROEQUIPOSPRIMERA;
			clasificacion = parser.getClasPorJornPrim();
		}
		/* Creamos el array de String que contrendra el nombre de los equipos, que luego servirá para construir
		   el desplegable con los equipos */
		eq = new String[numEquipos];
		
		// Rellenamos ahora ese array de String intermedio
		for (int i = 0; i < numEquipos; i++)
		{
			jor = 1;
			listaClasificacion = clasificacion.get("A"+selTemporada+"J"+jor);
			
			while (listaClasificacion == null) // Sólo en caso de que no haya informacion en la jornada 1
			{
				listaClasificacion = clasificacion.get("A"+selTemporada+"J"+jor);
				jor++;
			}	
			eq[i] = listaClasificacion.get(i).getEq();
		}
		/* Ahora con la lista de todos los equipos, creamos el JComboBox */
		comboBoxLocales[num] = new JComboBox(eq);
		/* Selecciona un equipo aleatorio */
		comboBoxLocales[num].setSelectedIndex((int) (Math.random() * eq.length));
		/* Y le añadimos el listener para cuando el usuario modifique algo se reinicie la confianza */
		comboBoxLocales[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		reiniciarConfianzaYResultado();
		    	      }
		});
		/* Hacemos lo mismo con los desplegables de los equipos visitantes */
		comboBoxVisitantes[num] = new JComboBox(eq);
		/* Selecciona un equipo aleatorio */
		comboBoxVisitantes[num].setSelectedIndex((int) (Math.random() * eq.length));
		/* Y le añadimos el listener para cuando el usuario modifique algo se reinicie la confianza */
		comboBoxVisitantes[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		reiniciarConfianzaYResultado();
		    	      }
		});
		/* Ahora ponemos un color distinto para los ComboBox (diferenciamos los de primera division y los de segunda) */
		if (primeraDivision)
		{
			comboBoxLocales[num].setBackground(Color.WHITE);
			comboBoxVisitantes[num].setBackground(Color.WHITE);
		}
		else
		{
			comboBoxLocales[num].setBackground(Color.LIGHT_GRAY);
			comboBoxVisitantes[num].setBackground(Color.LIGHT_GRAY);
		}
	}

	/**
	 * Método que devuelve un panel que contiene el subpanel con los pesos modificables y otro subpanel con
	 * los botones para la consulta y reiniciar pesos.
	 * @return JPanel
	 */
	private JPanel getPanelBotonesYPesos()
	{
		// Definición del panel
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		// Subpanel con pesos
		p.add(getPanelCompletoPesos(), BorderLayout.CENTER); 
		// Subpanel con botones
		p.add(getSubPanelBotones(), BorderLayout.SOUTH); 
		return p;
	}
	
	/**
	 * Subpanel que completa el panel de pesos y botones auxiliares y que contiene los botones.
	 * @return JPanel
	 */
	private JPanel getSubPanelBotones()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		// Boton de Consultar
		JButton botonConsulta = new JButton("Consultar");
		botonConsulta.addActionListener(new ActionListener() {
				public void actionPerformed( ActionEvent evt ) {
				    	accionesBotones(evt);
				    	   }
				});
		p.add(botonConsulta);
		
		//Boton de Poner pesos a default
		JButton botonDefault = new JButton("Reiniciar Pesos");
		botonDefault.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
			    	accionesBotones(evt);
			    	   }
			});
		p.add(botonDefault);
		return p;
	}
	
	/**
	 * Devuelve el subpanel de pesos para incluirlo en el subpanel de pesos y botones auxiliares. Este subpanel de pesos
	 * está distribuido con una rejilla y en cada celda habrá otro subpanel.
	 * @return JPanel
	 */
	private JPanel getPanelCompletoPesos()
	{
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,9)); 
		for (int i = 0; i < campoPesos.length; i++)
		{
			p.add(getPanelPeso(i));
		}
		return p;
	}
	
	/**
	 * Devuelve un subpanel que contiene un nombre del peso, un slider para modificar el peso y un campo
	 * de texto no modificable que muestra el valor
	 * @param quePeso de que tipo de peso queremos obtener el subpanel
	 * @return JPanel
	 */
	private JPanel getPanelPeso(int quePeso)
	{
		/* Creamos el panel */
		JPanel panelPesos = new JPanel();
		JPanel subPanel = new JPanel(); //subPanel con la información del peso y el JSlider
		panelPesos.setLayout(new BorderLayout());
		subPanel.setLayout(new GridLayout(1,2));
		/* Añadimos la etiqueta con el nombre del peso */
		panelPesos.add(getJLabelNombrePeso(quePeso),BorderLayout.NORTH);
		
		/* Añadimos el JSlider con los datos correspondientes */
		sliderPesos[quePeso] = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int)(datosPesos[quePeso]*1000));
		sliderPesos[quePeso].setPaintLabels(true); 	// Pintar las etiquetas
		sliderPesos[quePeso].setPaintTrack(true); 	// Pintar por donde va el Slider
		/* Asociamos los valores 0 y 1 a los valores 0 y 1000 de la JSlider para darle más precisión */
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 1000 ), new JLabel("1") );
		sliderPesos[quePeso].setLabelTable( labelTable );
		/* Listener */
		sliderPesos[quePeso].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				  JSlider source = (JSlider)e.getSource();
				  double d;
				  // Buscamos el Slider que ha producido el evento
				  for (int i = 0; i < campoPesos.length; i++)
				  {
					  if (source == sliderPesos[i])
					  {
						  d = source.getValue();
						  datosPesos[i] = d/1000;
						  campoPesos[i].setText(""+datosPesos[i]);
					  }
				  }
			}});
		/* Añadimos el Slider */
		subPanel.add(sliderPesos[quePeso]);
		/* Creamos los campos que contienen el valor númerico del peso */
		campoPesos[quePeso] = new JTextField(""+datosPesos[quePeso]); // Datos iniciales
		campoPesos[quePeso].setEditable(false);
		subPanel.add(campoPesos[quePeso]);
		panelPesos.add(subPanel,BorderLayout.CENTER);
		return panelPesos;
	}
	
	/**
	 * Método auxiliar que devuelve la etiqueta asociada a un tipo de peso
	 * @param quePeso tipo de peso del que queremos la etiqueta (TEMPORADA, LOCAL, etc)
	 * @return JLabel
	 */
	private JLabel getJLabelNombrePeso(int quePeso)
	{
		String str = "Default";
		JLabel j;
		
		switch(quePeso)
		{
			case Principal.TEMPORADA: str = "Temporada"; break;
			case Principal.LOCAL: str = "Local (L)"; break;
			case Principal.VISITANTE: str = "Visitante (V)"; break;
			case Principal.PUNTOSLOCAL: str = "Puntos L"; break;
			case Principal.PGLOCAL: str = "Par. Gan. L"; break;
			case Principal.PELOCAL: str = "Par. Emp. L"; break;
			case Principal.PPLOCAL: str = "Par. Per. L"; break;
			case Principal.PUNTOSVISITANTE: str = "Puntos V"; break;
			case Principal.PGVISITANTE: str = "Par. Gan. V"; break;
			case Principal.PEVISITANTE: str = "Par. Emp. V"; break;
			case Principal.PPVISITANTE: str = "Par. Per. V"; break;
			case Principal.POSLOCAL: str = "Posición L"; break;
			case Principal.POSVISITANTE: str = "Posición V"; break;
			case Principal.GFAVORLOCAL: str = "Gol. Favor L"; break;
			case Principal.GCONTRALOCAL: str = "Gol. Contra L"; break;
			case Principal.GFAVORVISITANTE: str = "Gol. Favor V"; break;
			case Principal.GCONTRAVISITANTE: str = "Gol. Contra V"; break;
			default: str = "Default";
			
		}
		
		j = new JLabel(str,JLabel.CENTER);
		return j;
		
	}

	/**
	 * Subpanel con la información de la temporada y las jornadas
	 * @return JPanel
	 */
	private JPanel getJLabelTemporadaJornada()
	{

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		setJornadaAño(selJornadaPrimera, selJornadaSegunda,selTemporada);
		p.add(jlab_jornada);
		return p;
	}
	
	/****** MÉTODOS AUXILIARES ******/
	
	/** 
	 * Inicializa los campos de la medida de confianza
	 * @param numFilas número de filas que depende del número de partidos
	 */
	private void inicializarConfianza(int numFilas)
	{
		// Creamos el número de campos que va a tener (1 o 15)
		confianza = new JProgressBar[numFilas];
		
		for (int i = 0; i < numFilas; i++)
		{
			confianza[i] = new JProgressBar();
			// Se muestre el texto encima de la barra
			confianza[i].setStringPainted(true);
			// Como al inicio no hay datos, ponemos un "No disponible"
			confianza[i].setString("N/D");
		}
	}
	
	/** 
	 * Inicializa los campos de texto de los resultados
	 * @param numFilas número de filas que depende del número de partidos
	 */
	private void inicializarResultados(int numFilas)
	{
		resultados = new JTextField[numFilas];
		
		for (int i = 0; i < numFilas; i++)
		{
			resultados[i] = new JTextField("N/D");
			resultados[i].setEditable(false);
			// Centramos el texto del JTextField
			resultados[i].setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
	

	/**
	 * Crea los desplegables con los equipos disponibles teniendo en cuenta si son partidos
	 * de primera o de segunda y si el modo de la tabla es de un partido o de varios
	 * @param numFilas numero de filas de la tabla que depende del numero de partidos que haya
	 */
	private void inicializarDesplegablesEquipoTabla(int numFilas)
	{
		comboBoxLocales = new JComboBox[numFilas];
		comboBoxVisitantes = new JComboBox[numFilas];
		
		if (numFilas < NUM_EQU) // Modo de un solo partido
			setEquiposComboBox(0,parser,(modo_partido == 0));
		else //modo de todos los partidos
		{
			for (int i = 0; i < numFilas; i++) 
			{
				/* Tenemos en cuenta el número de partidos que se muestran de primera y de segunda */
				setEquiposComboBox(i,parser,(i<num_partidos_primera));
			}
		}
	}
	
	
	/**
	 * Selecciona el número de elementos que tendra el desplegable de la jornada correspondiente 
	 * (de primera o segunda división)
	 * @param n número de elementos máximos que puede tener una jornada
	 * @param division
	 */
	private void setNumElemComboBoxJornada(int n,int division)
	{
		/* Eliminamos todos los elementos que tuviese el desplegable y metemos hasta los que
		 * indica el parámetro n */
		comboBox_jornada[division].removeAllItems();
		for (int i = 0; i < n; i++)
		{
			comboBox_jornada[division].addItem(""+(i+1));
		}
	}
	
	/****** MÉTODOS QUE IMPLEMENTAN LAS ACCIONES DE LOS LISTENER ******/
	
	/**
	 * MÉTODO LISTENER
	 * Actualiza la información de Temporada y Jornadas del título
	 * @param jornada_p Jornada de primera división
	 * @param jornada_s Jornada de segunda división
	 * @param año Año de la temporada
	 */
	private void setJornadaAño(int jornada_p, int jornada_s, int año)
	{
		// Font("Titulo fuente", "Atributos", "Tamaño")
		jlab_jornada.setFont(new Font("Verdana", Font.BOLD, 22));
		jlab_jornada.setText("Temporada: "+año+"-"+(año+1)+" ; Jornada 1ºDiv : "+jornada_p+" ; Jornada 2ºDiv : "+jornada_s);
	}

	/**
	 * MÉTODO LISTENER
	 * Actualiza la jornada actual seleccionada en el ComboBox de jornada correspondiente
	 * @param e
	 */
	private void actualizarJornada(ActionEvent e)
	{
		int n_jornada = 1;
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    if (newSelection != null)
	    {
	    	n_jornada = Integer.parseInt(newSelection);
	    	// Comprobamos que el ComboBox modificado es el de primera o el de segunda division
	    	if (cb == comboBox_jornada[PRIMERA_DIVISION])
	    		selJornadaPrimera = n_jornada;
	    	else
	    		selJornadaSegunda = n_jornada;
	    		
	    }
	    // Actualizamos la información del título
	    setJornadaAño(selJornadaPrimera,selJornadaSegunda,selTemporada);
	}
	
	/**
	 * MÉTODO LISTENER
	 *  Se encarga de leer la modificacion del ComboBox de la temporada y actualizar los valores y jornadas
	 * @param e
	 */
	private void actualizarTemporada(ActionEvent e)
	{	
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    // Nos quedamos solo con los 4 primeros digitos de la temporada (las temporadas las almacenamos como enteros) 
	    selTemporada = Integer.parseInt(newSelection.substring(0, 4));
	    /* Comprobamos si es la temporada actual, para actualizar los valores de los ComboBox de jornadas a su
	       valor máximo correspondiente (el de la jornada actual para ambos) */
		if (selTemporada == parser.getUltimaTemporada())
		{
			setNumElemComboBoxJornada(parser.getUltimaJornadaPrimera(),PRIMERA_DIVISION);
			setNumElemComboBoxJornada(parser.getUltimaJornadaSegunda(),SEGUNDA_DIVISION);
		}
		else
		{
			setNumElemComboBoxJornada(Principal.JORNADASPRIMERA,PRIMERA_DIVISION);
			setNumElemComboBoxJornada(Principal.JORNADASSEGUNDA,SEGUNDA_DIVISION);
		}
		// Ponemos a 1 la seleccion de jornadas para que no haya problemas
		selJornadaPrimera = 1;
		selJornadaSegunda = 1;
		// Actualizamos el título
	    setJornadaAño(selJornadaPrimera, selJornadaSegunda, selTemporada);
	    // Actualizamos la lista de partidos y sus equipos
	    actualizarListaPartidos();
	}

	/**
	 * MÉTODO LISTENER
	 * Actualiza la lista de partidos dependiendo del modo seleccionado (uno o más partidos)
	 */
	private void actualizarListaPartidos()
	{
		panelPrincipal.remove(panelTabla);
		if (rbutt_uno.isSelected())
		{
			modo_tabla = 0; // Modo de un sólo partido
			desplegableNumPartidosPrimera.setEnabled(false);
		}
		else
		{
			modo_tabla = 1; // Modo de 15 partidos
			desplegableNumPartidosPrimera.setEnabled(true);
		}
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.validate();
	}
	
	/**
	 * Actualiza los equipos en modo un partido para primera o segunda división
	 */
	private void actualizarEquiposPrimeraOSegunda()
	{
		panelPrincipal.remove(panelTabla);
		if (rbutt_primera.isSelected())
			modo_partido = 0; // Modo primera division
		else
			modo_partido = 1; // Modo segunda Division
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.validate();
	}
	
	/**
	 * Actualiza el número de partidos de primera división para el modo de todos los partidos
	 * @param e
	 */
	private void  actualizarNumeroPartidos(ActionEvent e)
	{
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    num_partidos_primera = Integer.parseInt(newSelection);
	    actualizarEquiposPrimeraOSegunda();
	}
	
	/** Ejecuta las acciones correspondientes a la consulta, como preparar los datos para pasárselos a la consulta, 
	 * ejecutar la consulta y mostrar una barra de progreso de la consulta. Finalmente, actualiza la informacion y 
	 * la muestra en pantalla.
	 */
	private void accionBotonConsulta()
	{
		int numMax = 0; // numero de partidos a consultar
		ArrayList<String> partidosPrimera = null;
		ArrayList<String> partidosSegunda = null;
			
		if (modo_tabla == 0) // un solo partido
		{
			if (modo_partido == 0) // de primera division
			{
				partidosPrimera = new ArrayList<String>();
				partidosPrimera.add(comboBoxLocales[0].getSelectedItem()+","+comboBoxVisitantes[0].getSelectedItem());
			}
			else // de segunda división
			{
				partidosSegunda = new ArrayList<String>();
				partidosSegunda.add(comboBoxLocales[0].getSelectedItem()+","+comboBoxVisitantes[0].getSelectedItem());
			}
		}
		else // todos los partidos -> 10 de primera y 5 de segunda
		{
			partidosPrimera = new ArrayList<String>();
			partidosSegunda = new ArrayList<String>();
				
			for (int i = 0; i < NUM_EQU; i++)
			{
				if (i < num_partidos_primera)
						partidosPrimera.add(comboBoxLocales[i].getSelectedItem()+","+comboBoxVisitantes[i].getSelectedItem());
				else
					partidosSegunda.add(comboBoxLocales[i].getSelectedItem()+","+comboBoxVisitantes[i].getSelectedItem());
			}
		}	
			// Creamos el nuevo hilo con los partidos y lo ejecutamos
		ThreadConsulta m = new ThreadConsulta(partidosPrimera,partidosSegunda);
		m.start();
		// Mientras, creamos una ventana con una barra de progreso para indicarnos el porcentaje de la consulta
		if (partidosPrimera != null)
			numMax = numMax + partidosPrimera.size();
		if (partidosSegunda != null)
			numMax = numMax + partidosSegunda.size();
		interfaz.setEnabled(false);
		Principal.setBarra(new BarraProgreso(0,numMax));
	}
	
	/** Restablece los valores de los pesos a default*/
	private void accionPesosDefault()
	{
		datosPesos[Principal.TEMPORADA] = Principal.PESOTEMPORADA;
		datosPesos[Principal.LOCAL] = Principal.PESOLOCAL;
		datosPesos[Principal.VISITANTE] = Principal.PESOVISITANTE;
		datosPesos[Principal.PUNTOSLOCAL] = Principal.PESOPUNTOSLOCAL;
		datosPesos[Principal.PGLOCAL] = Principal.PESOPGLOCAL;
		datosPesos[Principal.PELOCAL] = Principal.PESOPELOCAL;
		datosPesos[Principal.PPLOCAL] = Principal.PESOPPLOCAL;
		datosPesos[Principal.PUNTOSVISITANTE] = Principal.PESOPUNTOSVISITANTE;
		datosPesos[Principal.PGVISITANTE] = Principal.PESOPGVISITANTE;
		datosPesos[Principal.PEVISITANTE] = Principal.PESOPEVISITANTE;
		datosPesos[Principal.PPVISITANTE] = Principal.PESOPPVISITANTE;
		datosPesos[Principal.POSLOCAL] = Principal.PESOPOSLOCAL;
		datosPesos[Principal.POSVISITANTE] = Principal.PESOPOSVISITANTE;
		datosPesos[Principal.GFAVORLOCAL] = Principal.PESOGFAVORLOCAL;
		datosPesos[Principal.GCONTRALOCAL] = Principal.PESOGCONTRALOCAL;
		datosPesos[Principal.GFAVORVISITANTE] = Principal.PESOGFAVORVISITANTE;
		datosPesos[Principal.GCONTRAVISITANTE] = Principal.PESOGCONTRAVISITANTE;
		
		for (int i = 0; i < datosPesos.length; i++){
			campoPesos[i].setText(""+datosPesos[i]);
			sliderPesos[i].setValue((int)(datosPesos[i]*1000));
		}
	}
	
	private void accionesBotones(ActionEvent e){
		// Comprobamos que boton se ha pulsado
		JButton b = (JButton)e.getSource();
		// Ejecutamos la accion correspondiente dependiendo del boton
		if (b.getText().equals("Consultar"))
			accionBotonConsulta();
		else //en caso de que sea el botón de reiniciar pesos
			accionPesosDefault();
		
	}

	public double[] getDatosPesos() {
		return datosPesos;
	}

	private void setDatosPesos(double[] ds) {
		this.datosPesos = ds;
	}
	
	/** Actualiza los campos resultado y confianza de la interfaz */
	private void actualizarDatos(ArrayList<Prediccion> respuestaPrimera,ArrayList<Prediccion> respuestaSegunda)
	{
		if (modo_tabla == 0) // un solo partido
		{
			if (modo_partido == 0) // de primera division
			{
				resultados[0].setText(""+respuestaPrimera.get(0).getResultado()); 
				confianza[0].setValue((int) (respuestaPrimera.get(0).getConfianza()*100));
				if (tipoVotacionPonderada)
					confianza[0].setString((""+respuestaPrimera.get(0).getConfianza()*100).substring(0,5)+"%");
				else
					confianza[0].setString((""+respuestaPrimera.get(0).getConfianza()*100)+"%");
			}
			else // de segunda división
			{
				resultados[0].setText(""+respuestaSegunda.get(0).getResultado()); 
				confianza[0].setValue((int) (respuestaSegunda.get(0).getConfianza()*100));
				if (tipoVotacionPonderada)
					confianza[0].setString((""+respuestaSegunda.get(0).getConfianza()*100).substring(0,5)+"%");
				else
					confianza[0].setString((""+respuestaSegunda.get(0).getConfianza()*100)+"%");
				
			}
		}
		else // todos los partidos -> 10 de primera y 5 de segunda
		{
			for (int i = 0; i < num_partidos_primera; i++){
				resultados[i].setText(""+respuestaPrimera.get(i).getResultado()); 
				confianza[i].setValue((int) (respuestaPrimera.get(i).getConfianza()*100));
				if (tipoVotacionPonderada)
				{
					String str = ""+respuestaPrimera.get(i).getConfianza()*100;
					if (str.length()>5)
						str = str.substring(0,5);
					str = str+"%";
					confianza[i].setString(str);
				}
				else
					confianza[i].setString((""+respuestaPrimera.get(i).getConfianza()*100)+"%");
			}
			for (int i = 0; i < NUM_EQU - num_partidos_primera; i++){
				resultados[i+num_partidos_primera].setText(""+respuestaSegunda.get(i).getResultado()); 
				confianza[i+num_partidos_primera].setValue((int) (respuestaSegunda.get(i).getConfianza()*100));
				if (tipoVotacionPonderada)
				{
					String str = ""+respuestaSegunda.get(i).getConfianza()*100;
					if (str.length()>5)
						str = str.substring(0,5);
					str = str+"%";
					confianza[i+num_partidos_primera].setString(str);
				}
				else
					confianza[i+num_partidos_primera].setString((""+respuestaSegunda.get(i).getConfianza()*100)+"%");
			}
		}
	}
	
	/** Reinicia los valores de los campos de resultados y confianza*/
	private void reiniciarConfianzaYResultado() {
		for (int i = 0; i < resultados.length; i++){
			resultados[i].setText("N/D");
			resultados[i].repaint();
			confianza[i].setValue(0);
			confianza[i].setString("N/D");
			confianza[i].repaint();
		}
	}
	
	/****** CLASES INTERNAS PARA EJECUTAR OTROS HILOS ******/
	
	/** Clase interna para ejecutar el hilo de la consulta*/
	class ThreadConsulta extends Thread {
		private ArrayList<String> partidosPrimera;
		private ArrayList<String> partidosSegunda;
		
		public ThreadConsulta(ArrayList<String> pP, ArrayList<String> pS){
			partidosPrimera = pP;
			partidosSegunda = pS;
		}
		
		public void run() {
			if (partidosPrimera != null){
				try {
					respuestaPrimera = q.querysCBR(partidosPrimera,selTemporada,selJornadaPrimera,datosPesos, PRIMERA_DIVISION+1, tipoVotacionPonderada);
				} catch (ExecutionException e) {
					JOptionPane.showMessageDialog(null, "Ha habido un error ejecutando los casos", "Atención", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			if (partidosSegunda != null){
				try {
					respuestaSegunda = q.querysCBR(partidosSegunda,selTemporada,selJornadaSegunda,datosPesos, SEGUNDA_DIVISION+1, tipoVotacionPonderada);
				} catch (ExecutionException e) {
					JOptionPane.showMessageDialog(null, "Ha habido un error ejecutando los casos", "Atención", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			interfaz.setEnabled(true);
			Principal.getBarra().cerrarVentana();
			actualizarDatos(respuestaPrimera, respuestaSegunda);
		}
	}
	
	/** Clase interna para ejecutar el hilo de eficiencia */
	class ThreadEficiencia extends Thread{
		/** Constantes para el tipo de evaluacion de eficiencia */
		public static final int M_LEAVEONEOUT_EV = 0;
		public static final int M_HOLDOUT_EV = 1;
		public static final int M_SAMESPLIT = 2;
		public static final int M_NFOLD = 3;
		/** Atributos privados para la consulta de eficiencia */
		private ValidacionCruzada vc;
		private int modo;
		private int nCasos;
		private int nVueltas;
		
		public ThreadEficiencia(int modo, int numeroCasos, int numeroVueltas){
			this.modo = modo;
			this.nCasos = numeroCasos;
			this.nVueltas = numeroVueltas;
			vc = new ValidacionCruzada();
		}
		
		public void run(){
			switch (modo){
				case M_LEAVEONEOUT_EV: vc.LeaveOneOutEvaluation(datosPesos, tipoVotacionPonderada); break;
			 	case M_HOLDOUT_EV: vc.HoldOutEvaluation(datosPesos, nCasos, nVueltas, tipoVotacionPonderada); break;
			 	case M_SAMESPLIT: vc.SameSplitEvaluation(datosPesos, nCasos,tipoVotacionPonderada); break;
			 	case M_NFOLD: vc.NFoldEvaluation(datosPesos, nCasos, nVueltas, tipoVotacionPonderada); break;			 	
			}
			interfaz.setEnabled(true);
			if (modo == M_NFOLD)
				Principal.getBarra().cerrarVentana();
		}
		
	}
	
	/****** CLASE INTERNA PARA LISTENER DE MENÚ ******/
	
	/**
	 *  Clase interna para unificar todas las acciones del menú 
	 */
	class ListenerMenu implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			JMenuItem it = (JMenuItem) arg0.getSource();
			String str = it.getText();
			ThreadEficiencia te;
			int numCasos = 1;
			int numVueltas = 15;
			
			if (str.equals("Sobre"))
			{
				JOptionPane.showMessageDialog(null, "Quinielas V.01 \n Álvaro Pérez \n Xavier Gallofré \n Raúl Bueno", "Sobre...", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(str.equals("Salir"))
			{
				System.exit(0);
			}
			else if(str.equals("Hold Out Evaluation"))
			{
				str = JOptionPane.showInputDialog(null, "Introduce número de casos (se hará el valor absoluto)", "Hold Out Evaluation: número de casos",JOptionPane.QUESTION_MESSAGE);
				if (str == null) // Si se hace cancelar, no se hace nada
					return;
				try{
					numCasos = Integer.parseInt(str);
				}
				catch(Exception e){
					numCasos = 1;
					JOptionPane.showMessageDialog(null, "No has introducido un valor correcto, se asignará el valor por defecto");
				}
				if (numCasos < 0)
					numCasos = -numCasos;
				
				str = JOptionPane.showInputDialog(null, "Introduce número de vueltas (se hará el valor absoluto)", "Hold Out Evaluation: número de vueltas",JOptionPane.QUESTION_MESSAGE);
				
				if (str == null)
					return;
				try{
					numVueltas = Integer.parseInt(str);
				}
				catch(Exception e){
					numVueltas = 15;
					JOptionPane.showMessageDialog(null, "No has introducido un valor correcto, se asignará el valor por defecto");
				}
				if (numVueltas < 0)
					numVueltas = -numVueltas;
				interfaz.setEnabled(false);
				//Ejecutamos en un hilo a parte con los datos recibidos por el usuario
				te = new ThreadEficiencia(ThreadEficiencia.M_HOLDOUT_EV, numCasos, numVueltas);
				te.start();
			}
			else if(str.equals("Leave One Out Evaluation"))
			{
				interfaz.setEnabled(false);
				te = new ThreadEficiencia(ThreadEficiencia.M_LEAVEONEOUT_EV, numCasos, numVueltas);
				te.start();
			}
			else if(str.equals("Same Split Evaluation"))
			{
				str = JOptionPane.showInputDialog(null, "Introduce número de casos (se hará el valor absoluto)", "Same Split Evaluation: número de casos",JOptionPane.QUESTION_MESSAGE);
				
				if (str == null)
					return;
				try{
					numCasos = Integer.parseInt(str);
				}
				catch(Exception e){
					numCasos = 1;
					JOptionPane.showMessageDialog(null, "No has introducido un valor correcto, se asignará el valor por defecto");
				}
				if (numCasos < 0)
					numCasos = -numCasos;
				
				interfaz.setEnabled(false);
				te = new ThreadEficiencia(ThreadEficiencia.M_SAMESPLIT, numCasos, numVueltas);
				te.start();				
			}
			else if(str.equals("N-Fold Evaluation"))
			{
				str = JOptionPane.showInputDialog(null, "Introduce número de conjuntos (se hará el valor absoluto)", "N-Fold Evaluation: número de conjuntos",JOptionPane.QUESTION_MESSAGE);
				
				if (str == null)
					return;
				try{
					numCasos = Integer.parseInt(str);
				}
				catch(Exception e){
					numCasos = 1;
					JOptionPane.showMessageDialog(null, "No has introducido un valor correcto, se asignará el valor por defecto");
				}
				if (numCasos < 0)
					numCasos = -numCasos;
				
				str = JOptionPane.showInputDialog(null, "Introduce número de vueltas (se hará el valor absoluto)", "N-Fold Evaluation: número de vueltas",JOptionPane.QUESTION_MESSAGE);
				
				if (str == null)
					return;
				try{
					numVueltas = Integer.parseInt(str);
				}
				catch(Exception e){
					numVueltas = 15;
					JOptionPane.showMessageDialog(null, "No has introducido un valor correcto, se asignará el valor por defecto");
				}
				if (numVueltas < 0)
					numVueltas = -numVueltas;
				interfaz.setEnabled(false);
				Principal.setBarra(new BarraProgreso(BarraProgreso.MODONFOLDEVALUATOR,0));
				te = new ThreadEficiencia(ThreadEficiencia.M_NFOLD, numCasos, numVueltas);
				te.start();
			}
			
		}
	}
}
