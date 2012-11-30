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

import proyecto2.quinielas.Config;
import proyecto2.quinielas.Principal;
import proyecto2.quinielas.cbr.Prediccion;
import proyecto2.quinielas.cbr.Quinielas;
import proyecto2.quinielas.cbr.ValidacionCruzada;

import junit.awtui.ProgressBar;


public class Interfaz extends JFrame{
	
	/**
	 * TODO eleccion de votacion
	 */
	private static final long serialVersionUID = 1L;

	/** Constantes */
	public final static int LOCAL = 0;
	public final static int VISITANTE = 1;
	public final static int RESULTADO = 2;
	public final static int NUM_EQU = 15;
	public final static int MODOUNPARTIDO = 0;
	
	/** Configuracion de pantalla */
	public final static int W = 840;
	public final static int H = 600;
	
	/** N�mero de jornadas totales para primera y segunda divisi�n */
	public final static int PRIMERA_DIVISION = 0;
	public final static int SEGUNDA_DIVISION = 1;
	
	/** Modo de la tabla (0) para un partido y (1) para todos	 */
	private static int modo_tabla = 0; // modo de un partido (0) o modo de una jornada con los 15 partidos(1) a rellenar
	private static int modo_partido = 0;
	private static int num_partidos_primera = 10;
	
	
	/** Elementos del panel superior de informaci�n que tienen que modificarse */
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
	JRadioButton rbutt_primera; // selecci�n primera divisi�n
	JRadioButton rbutt_segunda; // selecci�n segunda divisi�n
	
	/** Panel principal (se modifica cuando se cambia de tabla */
	private JPanel panelPrincipal;
	
	/** Datos de configuraci�n */
	private Config conf;
	
	// Datos de la tabla
	ArrayList<Prediccion> respuestaPrimera;
	ArrayList<Prediccion> respuestaSegunda;
	
	// Datos de los pesos
	private double[] datosPesos;
	private JTextField[] campoPesos;
	private JSlider[] sliderPesos; // para poder escucharlos
	
	//Quinielas
	private Quinielas q;
	
	//Interfaz
	private Interfaz interfaz;
	
	
	public Interfaz(double[] ds, Config c, Quinielas q)
	{
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		 int width = pantalla.width;
		 this.setBounds(width/2 - W/2,0, W, H);
		// Inicializaci�n
		jlab_jornada = new JLabel();
		comboBox_jornada = new JComboBox[2];
		setDatosPesos(ds);
		campoPesos = new JTextField[datosPesos.length];
		sliderPesos = new JSlider[datosPesos.length];
		conf = c;
		this.q = q;
		interfaz = this;
		
		// Configuraci�n de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas"); //TODO Poner icono?
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
		JMenuBar barraMenuPrincipal = new JMenuBar();
		// Menu archivo
		JMenu menuArchivo = new JMenu("Archivo");
		barraMenuPrincipal.add(menuArchivo);
			// Elementos del menu archivo
			JMenuItem it_salir = new JMenuItem("Salir");
			menuArchivo.add(it_salir);
		
		// Menu eficiencia
		JMenu menuEficiencia = new JMenu("Eficiencia");
			// Elementos del menu eficiencia
			JMenuItem itNFold = new JMenuItem("N-fold");
			// A�adimos el listener al item correspondiente
			itNFold.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg) {
					accionesMenu(arg);
				}

			});
			menuEficiencia.add(itNFold);
			
			JMenuItem itHoldOut = new JMenuItem("Hold-Out");
			itHoldOut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg) {
					accionesMenu(arg);
				}

			});
			menuEficiencia.add(itHoldOut);
			
			JMenuItem itLeaveOneOut = new JMenuItem("Leave-One-Out");
			menuEficiencia.add(itLeaveOneOut);
		barraMenuPrincipal.add(menuEficiencia);
		// Menu sobre
		JMenu menuSobre = new JMenu("Sobre");
		barraMenuPrincipal.add(menuSobre);
		
		return barraMenuPrincipal;
	}
	
	private JPanel getPanelPrincipal()
	{
		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.add(getPanelInformacion(), BorderLayout.NORTH);
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.add(getPanelLogYPesos(), BorderLayout.SOUTH);
		panelPrincipal.validate();
		return panelPrincipal;
	}
	
	/** PANEL INFORMACI�N:
	 * Muestra la temporada actual y la jornada.
	 * A trav�s de un desplegable permite seleccionar la temporada y la jornada a consultar.
	 * A trav�s de un RadioButton permite seleccionar un partido o una jornada a rellenar.
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
	
	// Subpanel del panel informaci�n para seleccionar uno o la jornada completa para rellenar
	private JPanel getSeleccionUnoVariosPartidos()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel label = new JLabel("Rellenar: ");
		p.add(label);
		// Botones de selecci�n
		ButtonGroup bg = new ButtonGroup(); //grupo de botones: solo seleccionable 1
		rbutt_uno = new JRadioButton("S�lo un partido");
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
		
		// Creamos la relaci�n l�gica entre los botones
		bg.add(rbutt_uno);
		bg.add(rbutt_var);
		
		// A�adimos los botones al panel
		p.add(rbutt_uno);
		p.add(rbutt_var);
		
		// a�adimos un JLabel para el numero de partidos
		JLabel jl = new JLabel("Partidos 1�D: ");
		// A�adimos un comboBox con el n�mero de partidos de primera y de segunda
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
	
	// Subpanel de Temporada y Jornada con ComboBox desplegables
	private JPanel getSeleccionTemporadaJornada()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		JLabel labelTemp = new JLabel("Temporada: ");
		p.add(labelTemp);
		p.add(getDesplegableTemporada());
		
		JLabel labelJor = new JLabel("Jornada Primera Divisi�n: ");
		p.add(labelJor);
		
		p.add(getDesplegableJornada(PRIMERA_DIVISION)); 
		
		JLabel labelJor2 = new JLabel("Jornada Segunda Divisi�n: ");
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
		int n_max = 0; //n�mero m�ximo de la jornada
		switch (division)
		{
			case PRIMERA_DIVISION: n_max = conf.JORNADASPRIMERA; break;
			case SEGUNDA_DIVISION: n_max = conf.JORNADASSEGUNDA; break;
		}
		
		// Actualizamos el valor si es la temporada actual:
		// Como m�ximo se muestra la jornada actual + 1
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
	
	/** Selecciona el n�mero de elementos que tendra el desplegable de la jornada */
	private void setNumElemComboBoxJornada(int n,int division)
	{
		comboBox_jornada[division].removeAllItems();
		for (int i = 0; i < n; i++)
		{
			comboBox_jornada[division].addItem(""+(i+1));
		}
	}
	
	/**
	 * Tabla que permite introducir al usuario los equipos de la query. Tambi�n tiene
	 * dos columnas adicionales donde se muestra el resultado de la soluci�n y la medida de 
	 * confianza.
	 */
	private JPanel getJPanelTabla(int modo)
	{
		// Numero de filas que tendra nuestra "tabla" (por defecto 16)
		int numF; 
		// Si el modo es el de un solo partido, ser�n 2
		switch (modo)
		{
			case MODOUNPARTIDO: numF = 2; break;
			default: numF = NUM_EQU+1; break;
		}
		// Creamos y configuramos el panel
		panelTabla = new JPanel();
		panelTabla.setLayout(new BorderLayout());
		// Creamos Subpaneles para la cabecera y el resto de elementos
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(numF,4));
		// A�adimos los elementos de la primera fila en el subpanel de cabecera 
		JLabel j; // Etiqueta auxiliar para completar la cabecera
		String str = "default"; // Auxiliar para a�adir el nombre
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
		inicializarDesplegablesEquipoTabla(numF-1);
		inicializarResultados(numF-1);
		inicializarConfianza(numF-1);
		
		for (int i = 0; i < numF-1; i++)
		{
			p.add(comboBoxLocales[i]);
			p.add(comboBoxVisitantes[i]);
			p.add(resultados[i]);
			p.add(confianza[i]);
		}
		
		panelTabla.add(p,BorderLayout.CENTER);
		
		//Y Ahora, si esta el modo 0, a�adimos un panel inferior con la informaci�n de primera o segunda division
		if (modo == 0)
			panelTabla.add(getPanelPrimeraOSegunda(), BorderLayout.NORTH);
		return panelTabla;
	}
	
	private JPanel getPanelPrimeraOSegunda()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel label = new JLabel("Partido: ");
		p.add(label);
		// Botones de selecci�n
		ButtonGroup bg = new ButtonGroup(); //grupo de botones: solo seleccionable 1
		rbutt_primera = new JRadioButton("Primera Divisi�n");
		rbutt_segunda = new JRadioButton("Segunda Divisi�n");
		
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
		
		// Creamos la relaci�n l�gica entre los botones
		bg.add(rbutt_primera);
		bg.add(rbutt_segunda);
		
		// A�adimos los botones al panel
		p.add(rbutt_primera);
		p.add(rbutt_segunda);
		
		return p;
	}
	
	/** Crea los ComboBox con los equipos disponibles teniendo en cuenta si son partidos
	 * de primera o de segunda y si el modo de la tabla es de un partido o de varios
	 */
	private void inicializarDesplegablesEquipoTabla(int numFilas)
	{
		comboBoxLocales = new JComboBox[numFilas];
		comboBoxVisitantes = new JComboBox[numFilas];
		
		if (numFilas < NUM_EQU) // Modo de un solo partido
			setEquiposComboBox(0,conf,(modo_partido == 0));
		else //modo de todos los partidos
		{
			for (int i = 0; i < numFilas; i++)
			{
				setEquiposComboBox(i,conf,(i<num_partidos_primera));
			}
		}
	}
	
	/** Rellena un ComboBox con los equipos que juegan en esa temporada y division*/ 
	private void setEquiposComboBox(int num, Config c, boolean primeraDivision)
	{
		int ind; // auxiliar para recorrer las jornadas
		int n = c.getSeleccionTemporada();
		
		if (c.getSeleccionTemporada()>=2000)
			n = n - 2000;
		
		String lineaClasificacion = null;
		// Leer de la tabla de datos
		String[][][] clasificacion; // Almacenamiento temporal para el acceso a datos
		String[] eq; // Elementos posibles en el ComboBox
		
		// Datos para segunda division
		int numEquipos = c.NUMEROEQUIPOSSEGUNDA;
		clasificacion = c.getClasificacionesSegunda();
		
		// Datos para primera division
		if (primeraDivision)
		{
			numEquipos = c.NUMEROEQUIPOSPRIMERA;
			clasificacion = c.getClasificacionesPrimera();
		}
		
		eq = new String[numEquipos];
		
		// Rellenamos para los equipos de primera divis�n
		for (int i = 0; i < numEquipos; i++)
		{
			ind = 0;
			lineaClasificacion = clasificacion[n][ind][i];
			
			while (lineaClasificacion == null) // S�lo en caso de que no haya informacion en la temporada 0
			{
				lineaClasificacion = clasificacion[n][ind][i];
				ind++;
			}
		
			eq[i] = (lineaClasificacion.split(",")[0]);
		}
		
		
		// Ahora con la lista de todos los equipos, creamos el JComboBox
		comboBoxLocales[num] = new JComboBox(eq);
		
		// Selecciona un equipo aleatorio
		comboBoxLocales[num].setSelectedIndex((int) (Math.random() * eq.length));
		
		 //Y le a�adimos el listener para cuando el usuario modifique algo se reinicie la confinanza
		comboBoxLocales[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		reiniciarConfianzaYResultado();
		    	      }
		});
		comboBoxVisitantes[num] = new JComboBox(eq);
		// Selecciona un equipo aleatorio
		comboBoxVisitantes[num].setSelectedIndex((int) (Math.random() * eq.length));
		
		comboBoxVisitantes[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		reiniciarConfianzaYResultado();
		    	      }
		});
		// Ahora ponemos un color distinto para los ComboBox (diferenciamos los de primera division y los de segunda
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

	/** Inicializa los campos de texto de los resultados **/
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
	
	/** Inicializa los campos de la medida de confianza (JProgressBar)*/
	private void inicializarConfianza(int numFilas)
	{
		// Creamos el n�mero de campos que va a tener (1 o 15)
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
	
	/** Metodo que devuelve el JPanel donde estan los pesos para modificar
	 * y tambi�n los botones de Consulta y Poner Pesos por defecto
	 */
	private JPanel getPanelLogYPesos()
	{
		// Definici�n del panel
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		// Subpanel con pesos
		p.add(getPanelCompletoPesos(), BorderLayout.CENTER); 
		// Subpanel con botones
		p.add(getSubPanelBotones(), BorderLayout.SOUTH); 
		return p;
	}
	
	/** SUBPANEL con los botones de consultar y poner pesos a default **/
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
	
	/** Creamos el panel completo de los pesos, que tendra un Grid Layout, en donde cada celda habra
	 * una etiqueta con el nombre del peso, un Slider y un campo no editable que muestre el valor del peso
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
	
	private JPanel getPanelPeso(int quePeso)
	{
		// Creamos el panel
		JPanel panelPesos = new JPanel();
		JPanel subPanel = new JPanel(); // subPanel con la informaci�n del peso y el JSlider
		panelPesos.setLayout(new BorderLayout());
		subPanel.setLayout(new GridLayout(1,2));
		// A�adimos la etiqueta con el nombre del peso
		panelPesos.add(getJLabelNombrePeso(quePeso),BorderLayout.NORTH);
		
		// A�adimos el JSlider con los datos correspondientes
		sliderPesos[quePeso] = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int)(datosPesos[quePeso]*1000));
		sliderPesos[quePeso].setPaintLabels(true); // Pintar las etiquetas
		sliderPesos[quePeso].setPaintTrack(true); // Pintar por donde va el Slider
		// Asociamos los valores 0 y 1 a los valores 0 y 1000 de la JSlider para darle m�s precisi�n
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 1000 ), new JLabel("1") );
		sliderPesos[quePeso].setLabelTable( labelTable );
		// Espaciados
		//sliderPesos[quePeso].setMaximumSize(new Dimension(10,5));
		// Listener
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
		
		subPanel.add(sliderPesos[quePeso]); //a�adimos el Slider
		// Creamos los campos que contienen el valor numerico del peso
		campoPesos[quePeso] = new JTextField(""+datosPesos[quePeso]); // Datos iniciales
		campoPesos[quePeso].setEditable(false);
		subPanel.add(campoPesos[quePeso]);
		panelPesos.add(subPanel,BorderLayout.CENTER);
		return panelPesos;
		
	}
	
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
			case Principal.POSLOCAL: str = "Posici�n L"; break;
			case Principal.POSVISITANTE: str = "Posici�n V"; break;
			case Principal.GFAVORLOCAL: str = "Gol. Favor L"; break;
			case Principal.GCONTRALOCAL: str = "Gol. Contra L"; break;
			case Principal.GFAVORVISITANTE: str = "Gol. Favor V"; break;
			case Principal.GCONTRAVISITANTE: str = "Gol. Contra V"; break;
			default: str = "Default";
			
		}
		
		j = new JLabel(str,JLabel.CENTER);
		return j;
		
	}

	/** Panel con la informaci�n de la temporada y las jornadas */
	private JPanel getJLabelTemporadaJornada()
	{

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		setJornadaA�o(conf.getSeleccionJornadaPrimera(), conf.getSeleccionJornadaSegunda(),conf.getSeleccionTemporada());
		p.add(jlab_jornada);
		return p;
	}
	
	/** METODOS QUE EDITAN LA INFORMACION DE LA INTERFAZ (Y ACCIONES DE LISTENERS) 
	 * @param arg **/ 
	
	private void accionesMenu(ActionEvent arg)
	{
		JMenuItem it = (JMenuItem) arg.getSource();
		String nomb = it.getText();
		int numCasos = 0;
		int numCasosDefecto = 15;
		int numVueltas = 0;
		int numVueltasDefecto = 1;
		String str = null;
		if (nomb == "LeaveOneOut")
		{
			
		}
		else if (nomb == "Hold-Out") //TODO
		{
			ValidacionCruzada vc = new ValidacionCruzada();
			str = JOptionPane.showInputDialog(null, "N�mero de casos (si no introduce algo aceptable se usar� el valor por defecto ("+numCasosDefecto+")", "Introduce el n�mero de casos",JOptionPane.QUESTION_MESSAGE);
			if (str != null)
			{
				try{
				numCasos = Integer.parseInt(str);
				}
				catch (Exception e)
				{
					numCasos = numCasosDefecto;
				}
				if (numCasos < 0)
					numCasos = numCasosDefecto;
			}
			str = JOptionPane.showInputDialog(null, "Introducir numero de vueltas (por defecto 1)", ""+numVueltasDefecto, JOptionPane.QUESTION_MESSAGE);
			
			if (str != null)
			{
				numVueltas = Integer.parseInt(str);
				if (numVueltas < 0)
					numVueltas = numVueltasDefecto;
			}
			// TODO Hacerlo en otro hilo a parte...
			vc.HoldOutEvaluation(datosPesos, numCasos, numVueltas);
		}
		else if (nomb == "Leave-One-Out")
		{
			
		}
		else //accion por defecto -> Salir
		{
			
		}
	}
	
	/** Actualiza la informaci�n de Temporada y Jornadas del t�tulo*/
	private void setJornadaA�o(int jornada_p, int jornada_s, int a�o)
	{
		// Font("Titulo fuente", "Atributos", "Tama�o")
		jlab_jornada.setFont(new Font("Verdana", Font.BOLD, 22));
		jlab_jornada.setText("Temporada: "+a�o+"-"+(a�o+1)+" ; Jornada 1�Div : "+jornada_p+" ; Jornada 2�Div : "+jornada_s);
	}

	/** Actualiza la jornada actual seleccionada en el ComboBox de jornada correspondiente**/
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
	    		conf.setSeleccionJornadaPrimera(n_jornada);
	    	else
	    		conf.setSeleccionJornadaSegunda(n_jornada);
	    		
	    }
	    // Actualizamos la informaci�n del t�tulo
	    setJornadaA�o(conf.getSeleccionJornadaPrimera(),conf.getSeleccionJornadaSegunda(),conf.getSeleccionTemporada());
	}
	
	/** Se encarga de leer la modificacion del ComboBox de la temporada y actualizar el valor correspondiente*/
	private void actualizarTemporada(ActionEvent e)
	{	
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    // Nos quedamos solo con los 4 primeros digitos de la temporada (las temporadas las almacenamos como enteros) 
	    conf.setSeleccionTemporada(Integer.parseInt(newSelection.substring(0, 4)));
	    // Comprobamos si es la temporada actual, para actualizar los valores de los ComboBox de jornadas a su
	    // valor m�ximo correspondiente (el de la jornada actual para ambos)
		if (conf.getSeleccionTemporada().equals(conf.getUltimaTemporada()))
		{
			setNumElemComboBoxJornada(conf.getUltimaJornadaPrimera(),PRIMERA_DIVISION);
			setNumElemComboBoxJornada(conf.getUltimaJornadaSegunda(),SEGUNDA_DIVISION);
		}
		else
		{
			setNumElemComboBoxJornada(Config.JORNADASPRIMERA,PRIMERA_DIVISION);
			setNumElemComboBoxJornada(Config.JORNADASSEGUNDA,SEGUNDA_DIVISION);
		}
		// Ponemos a 1 la seleccion de jornadas para que no haya problemas
		conf.setSeleccionJornadaPrimera(1);
		conf.setSeleccionJornadaSegunda(1);
		// Actualizamos el t�tulo
	    setJornadaA�o(conf.getSeleccionJornadaPrimera(), conf.getSeleccionJornadaSegunda(),conf.getSeleccionTemporada());
	    // Actualizamos la lista de partidos y sus equipos
	    actualizarListaPartidos();
	}

	private void actualizarListaPartidos()
	{
		panelPrincipal.remove(panelTabla);
		if (rbutt_uno.isSelected())
		{
			modo_tabla = 0; // Modo de un s�lo partido
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
	
	private void  actualizarNumeroPartidos(ActionEvent e)
	{
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    num_partidos_primera = Integer.parseInt(newSelection);
	    actualizarEquiposPrimeraOSegunda();
	}
	
	/** Ejecuta las acciones correspondientes a la consulta, como preparar los datos para pas�rselos a la consulta, 
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
			else // de segunda divisi�n
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
		Principal.setBarra(new BarraProgreso(1,numMax));
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
		else //en caso de que sea el bot�n de reiniciar pesos
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
				confianza[0].setString((""+respuestaPrimera.get(0).getConfianza()*100).substring(0, 5)+"%");
			}
			else // de segunda divisi�n
			{
				resultados[0].setText(""+respuestaSegunda.get(0).getResultado()); 
				confianza[0].setValue((int) (respuestaSegunda.get(0).getConfianza()*100));
				confianza[0].setString((""+respuestaSegunda.get(0).getConfianza()*100).substring(0, 5)+"%");
			}
		}
		else // todos los partidos -> 10 de primera y 5 de segunda
		{
			for (int i = 0; i < num_partidos_primera; i++){
				resultados[i].setText(""+respuestaPrimera.get(i).getResultado()); 
				confianza[i].setValue((int) (respuestaPrimera.get(i).getConfianza()*100));
				confianza[i].setString((""+respuestaPrimera.get(i).getConfianza()*100).substring(0, 5)+"%");
			}
			for (int i = 0; i < NUM_EQU - num_partidos_primera; i++){
				resultados[i+num_partidos_primera].setText(""+respuestaSegunda.get(i).getResultado()); 
				confianza[i+num_partidos_primera].setValue((int) (respuestaSegunda.get(i).getConfianza()*100));
				confianza[i+num_partidos_primera].setString((""+respuestaSegunda.get(i).getConfianza()*100).substring(0, 5)+"%");
			}
		}
	}
	
	/** Reinicia los valores de los campos de resultados y confianza*/
	private void reiniciarConfianzaYResultado() 
	{
		for (int i = 0; i < resultados.length; i++){
			resultados[i].setText("N/D");
			resultados[i].repaint();
			confianza[i].setValue(0);
			confianza[i].setString("N/D");
			confianza[i].repaint();
		}
		
	}
	
	/** Clase interna para ejecutar el hilo de la consulta*/
	class ThreadConsulta extends Thread 
	{
		private ArrayList<String> partidosPrimera;
		private ArrayList<String> partidosSegunda;
		
		public ThreadConsulta(ArrayList<String> pP, ArrayList<String> pS){
			partidosPrimera = pP;
			partidosSegunda = pS;
		}
		
		public void run() 
		{
			if (partidosPrimera != null)
				respuestaPrimera = q.querysCBR(partidosPrimera,conf.getSeleccionTemporada(),conf.getSeleccionJornadaPrimera(),datosPesos,conf.getClasificacionesPrimera());
			if (partidosSegunda != null)
				respuestaSegunda = q.querysCBR(partidosSegunda,conf.getSeleccionTemporada(),conf.getSeleccionJornadaSegunda(),datosPesos,conf.getClasificacionesSegunda());
			interfaz.setEnabled(true);
			Principal.getBarra().cerrarVentana();
			actualizarDatos(respuestaPrimera, respuestaSegunda);
		}
	}
	
	/** Clase interna para ejecutar el hilo de eficiencia */
	class ThreadEficiencia extends Thread
	{
		/** Constantes para el tipo de evaluacion de eficiencia */
		public static final int M_LEAVEONEOUT_EV = 0;
		public static final int M_HOLDOUT_EV = 1;
		/** Atributos privados para la consulta de eficiencia */
		private ValidacionCruzada vc;
		private int modo;
		private int nCasos;
		private int nVueltas;
		
		public ThreadEficiencia(int modo, int numeroCasos, int numeroVueltas)
		{
			this.modo = modo;
			vc = new ValidacionCruzada();
		}
		
		public void run()
		{
			switch (modo)
			{
			 	case M_HOLDOUT_EV: vc.HoldOutEvaluation(datosPesos, nCasos, nVueltas); break;
			}
		}
		
	}
	
	

}
