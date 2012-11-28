package proyecto2.quinielas.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import proyecto2.quinielas.Config;
import proyecto2.quinielas.Principal;
import proyecto2.quinielas.cbr.Prediccion;
import proyecto2.quinielas.cbr.Quinielas;

import junit.awtui.ProgressBar;


public class Interfaz extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Constantes */
	public final static int LOCAL = 0;
	public final static int VISITANTE = 1;
	public final static int RESULTADO = 2;
	public final static int NUM_EQU = 15;
	
	/** Configuracion de pantalla */
	public final static int W = 800;
	public final static int H = 700;
	
	/** Número de jornadas totales para primera y segunda división */
	public final static int PRIMERA_DIVISION = 0;
	public final static int SEGUNDA_DIVISION = 1;
	
	/** Modo de la tabla (0) para un partido y (1) para todos	 */
	private static int modo_tabla = 0; // modo de un partido (0) o modo de una jornada con los 15 partidos(1) a rellenar
	private static int modo_partido = 0;
	private static int num_partidos_primera = 10;
	
	
	/** Elementos del panel superior de información que tienen que modificarse */
	private JLabel jlab_jornada;
	JComboBox[] comboBox_jornada; // tiene guardado los 2 desplegables de las jornadas
	JRadioButton rbutt_uno; // seleccion de un partido
	JRadioButton rbutt_var; // seleccion de varios partidos
	
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
	
	/** Datos de configuración */
	private Config conf;
	
	// Datos de la tabla
	//private String[][] datosTabla; 
	
	// Datos de los pesos
	private double[] datosPesos;
	private JTextField[] campoPesos;
	private JSlider[] sliderPesos; // para poder escucharlos
	
	//Quinielas
	Quinielas q;
	
	public Interfaz(double[] ds, Config c, Quinielas q)
	{
		// Inicialización
		jlab_jornada = new JLabel();
		comboBox_jornada = new JComboBox[2];
		setDatosPesos(ds);
		campoPesos = new JTextField[datosPesos.length];
		sliderPesos = new JSlider[datosPesos.length];
		conf = c;
		this.q = q;
		
		// Configuración de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas"); //TODO Poner icono?
		this.setSize(W, H);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true); // TODO Poner a falso
		this.setContentPane((getPanelPrincipal()));
		this.setJMenuBar(getMenuPrincipal());
		this.validate();
		//inicializarCamposPesos(campoPesos);

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
		panelPrincipal.add(getPanelLogYPesos(), BorderLayout.SOUTH);
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
		panelTabla = new JPanel();
		panelTabla.setLayout(new BorderLayout());
		
		JPanel p = new JPanel();
		
		int numF = NUM_EQU+1;
		//panelTabla = new JPanel();
		if (modo == 0) // un solo partido
			numF = 2;
		//panelTabla.setLayout(new GridLayout(numF,4));
		p.setLayout(new GridLayout(numF,4));
		// Añadimos los elementos de la primera fila 
		JLabel j = new JLabel("Equipo Local");
		//panelTabla.add(j);
		p.add(j);
		JLabel h = new JLabel("Equipo Visitante");
		p.add(h);
		//panelTabla.add(p);
		JLabel s = new JLabel("Resultado");
		p.add(s);
		//panelTabla.add(s);
		JLabel o = new JLabel("Confianza");
		p.add(o);
		//panelTabla.add(o);
		
		// añadimos los equipos correspondientes
		//inicializarDatosPartidos(numF-1);
		inicializarDesplegablesEquipoTabla(numF-1);
		inicializarResultados(numF-1);
		inicializarConfianza(numF-1);
		
		for (int i = 0; i < numF-1; i++)
		{
			//panelTabla.add(comboBoxLocales[i]);
			//panelTabla.add(comboBoxVisitantes[i]);
			//panelTabla.add(resultados[i]);
			//panelTabla.add(confianza[i]);
			p.add(comboBoxLocales[i]);
			p.add(comboBoxVisitantes[i]);
			p.add(resultados[i]);
			p.add(confianza[i]);
		}
		
		panelTabla.add(p,BorderLayout.CENTER);
		
		//Y Ahora, si esta el modo 0, añadimos un panel inferior con la información de primera o segunda division
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
		
		// Creamos la relación lógica entre los botones
		bg.add(rbutt_primera);
		bg.add(rbutt_segunda);
		
		// Añadimos los botones al panel
		p.add(rbutt_primera);
		p.add(rbutt_segunda);
		
		return p;
	}
	
	/** Incializa la tabla de datos intermedia, que será modificada por la interfaz y
	 *  por el resto del programa
	 */
	
	//private void inicializarDatosPartidos(int numFilas)
	//{
	//	datosTabla = new String[numFilas][4];
		// TODO SET DATOS POR DEFECTO leer de la estructura pasada por parametro con equipos temporada = 2012 y jornadas 1 y 1
		// Datos por defecto para resultado y confianza a "-1"
	//}
	
	private void inicializarDesplegablesEquipoTabla(int numFilas)
	{
		// TODO Falta hacer la opción de un solo partido que sea de primera o de segunda division
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
		
			// Rellenamos para los equipos de primera divisón
		for (int i = 0; i < numEquipos; i++)
		{
			ind = 0;
			lineaClasificacion = clasificacion[n][ind][i];
			
			while (lineaClasificacion == null) // Sólo en caso de que no haya informacion en la temporada 0
			{
				lineaClasificacion = clasificacion[n][ind][i];
				ind++;
			}
		
			eq[i] = (lineaClasificacion.split(",")[0]);
		}
		
		
		// Ahora con la lista de todos los equipos, creamos el JComboBox
		comboBoxLocales[num] = new JComboBox(eq);
		
		 //Y le añadimos el listener para cuando el usuario modifique algo se reinicie la confinanza
		comboBoxLocales[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		reiniciarConfianzaYResultado();
		    	      }
		});
		comboBoxVisitantes[num] = new JComboBox(eq);
		
		comboBoxVisitantes[num].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		reiniciarConfianzaYResultado();
		    	      }
		});
		// TODO Hacer un metodo setDatoTablaVisitantes para modificar los datos de los visitantes
	}
	
	protected void reiniciarConfianzaYResultado() {
		for (int i = 0; i < resultados.length; i++)
		{
			resultados[i].setText("N/D");
			resultados[i].repaint();
			confianza[i].setValue(0);
			confianza[i].setString("N/D");
			confianza[i].repaint();
		}
		
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
	/** TODO RETOCAR MÉTODO: AHORA ESTÁ CHAPUZA E INCOMPLETO **/
	private JPanel getPanelLogYPesos()
	{
		// Definición del panel
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		p.add(getPanelCompletoPesos(), BorderLayout.CENTER);
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
		p.setLayout(new GridLayout(4,4));
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
		JPanel subPanel = new JPanel(); // subPanel con la información del peso y el JSlider
		panelPesos.setLayout(new BorderLayout());
		subPanel.setLayout(new GridLayout(1,2));
		// Añadimos la etiqueta con el nombre del peso
		panelPesos.add(getJLabelNombrePeso(quePeso),BorderLayout.NORTH);
		
		// Añadimos el JSlider con los datos correspondientes
		sliderPesos[quePeso] = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int)(datosPesos[quePeso]*1000));
		sliderPesos[quePeso].setPaintLabels(true); // Pintar las etiquetas
		sliderPesos[quePeso].setPaintTrack(true); // Pintar por donde va el Slider
		// Asociamos los valores 0 y 1 a los valores 0 y 1000 de la JSlider para darle más precisión
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
		
		subPanel.add(sliderPesos[quePeso]); //añadimos el Slider
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
			case Principal.LOCAL: str = "Local"; break;
			case Principal.VISITANTE: str = "Visitante"; break;
			case Principal.PUNTOSLOCAL: str = "Puntos Local"; break;
			case Principal.PGLOCAL: str = "P. Ganados Local"; break;
			case Principal.PELOCAL: str = "P. Empatados Local"; break;
			case Principal.PPLOCAL: str = "P. Perdidos Local"; break;
			case Principal.PUNTOSVISITANTE: str = "Puntos Visitante"; break;
			case Principal.PGVISITANTE: str = "P. Ganados Visitante"; break;
			case Principal.PEVISITANTE: str = "P. Empatados Visitante"; break;
			case Principal.PPVISITANTE: str = "P. Perdidos Visitante"; break;
			case Principal.POSLOCAL: str = "Posición Local"; break;
			case Principal.POSVISITANTE: str = "Posición VIsitante"; break;
			case Principal.GFAVORLOCAL: str = "Goles a Favor Local"; break;
			case Principal.GCONTRALOCAL: str = "Goles en Contra Local"; break;
			case Principal.GFAVORVISITANTE: str = "Goles a Favor Visitante"; break;
			case Principal.GCONTRAVISITANTE: str = "Goles en Contra Visitante"; break;
			default: str = "Default";
			
		}
		
		j = new JLabel(str);
		return j;
		
	}

	/** Panel con la información de la temporada y las jornadas */
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
		if (año == 0)
			año = 2000;
		jlab_jornada.setFont(new Font("Verdana", Font.BOLD, 22));
		jlab_jornada.setText("Temporada: "+año+"-"+(año+1)+" ; Jornada 1ºDiv : "+jornada_p+" ; Jornada 2ºDiv : "+jornada_s);
	}

	// Métodos Listener	
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
	    actualizarListaPartidos();
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
	}
	
	public void actualizarEquiposPrimeraOSegunda()
	{
		panelPrincipal.remove(panelTabla);
		if (rbutt_primera.isSelected())
			modo_partido = 0; // Modo primera division
		else
			modo_partido = 1; // Modo segunda Division
		panelPrincipal.add(getJPanelTabla(modo_tabla), BorderLayout.CENTER);
		panelPrincipal.validate();
	}
	
	public void accionesBotones(ActionEvent e)
	{
		JButton b = (JButton)e.getSource();
		// Para ver el modo en el que estamos
		
		if (b.getText().equals("Consultar"))
		{
			ArrayList<String> partidosPrimera = new ArrayList<String>();
			ArrayList<String> partidosSegunda = new ArrayList<String>();
			ArrayList<Prediccion> respuestaPrimera = null;
			ArrayList<Prediccion> respuestaSegunda = null;
			
			if (modo_tabla == 0) // un solo partido
			{
				if (modo_partido == 0) // de primera division
				{
					partidosPrimera.add(comboBoxLocales[0].getSelectedItem()+","+comboBoxVisitantes[0].getSelectedItem());
					respuestaPrimera = q.querysCBR(partidosPrimera,conf.getSeleccionTemporada(),conf.getSeleccionJornadaPrimera(),datosPesos,conf.getClasificacionesPrimera(),conf.getClasificacionesSegunda());
				}
				else // de segunda división
				{
					partidosSegunda.add(comboBoxLocales[0].getSelectedItem()+","+comboBoxVisitantes[0].getSelectedItem());
					respuestaSegunda = q.querysCBR(partidosSegunda,conf.getSeleccionTemporada(),conf.getSeleccionJornadaPrimera(),datosPesos,conf.getClasificacionesPrimera(),conf.getClasificacionesSegunda());
				}
			}
			else // todos los partidos -> 10 de primera y 5 de segunda
			{
				for (int i = 0; i < NUM_EQU; i++)
				{
					if (i < num_partidos_primera)
						partidosPrimera.add(comboBoxLocales[i].getSelectedItem()+","+comboBoxVisitantes[i].getSelectedItem());
					else
						partidosSegunda.add(comboBoxLocales[i].getSelectedItem()+","+comboBoxVisitantes[i].getSelectedItem());
				}
				respuestaPrimera = q.querysCBR(partidosPrimera,conf.getSeleccionTemporada(),conf.getSeleccionJornadaPrimera(),datosPesos,conf.getClasificacionesPrimera(),conf.getClasificacionesSegunda());
				respuestaSegunda = q.querysCBR(partidosSegunda,conf.getSeleccionTemporada(),conf.getSeleccionJornadaPrimera(),datosPesos,conf.getClasificacionesPrimera(),conf.getClasificacionesSegunda());
			}
			
			actualizarDatos(respuestaPrimera, respuestaSegunda);
			
		}
		else //en caso de que sea el botón default
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
			
			for (int i = 0; i < datosPesos.length; i++)
			{
				campoPesos[i].setText(""+datosPesos[i]);
				sliderPesos[i].setValue((int)(datosPesos[i]*1000));
			}
		}
	}

	public double[] getDatosPesos() {
		return datosPesos;
	}

	public void setDatosPesos(double[] ds) {
		this.datosPesos = ds;
	}

	public static void mensajeEspera() {
		JOptionPane.showMessageDialog(null , "Actualizando, por favor, espere");
		
	}
	
	/** Actualiza los campos resultado y confianza de la interfaz */
	public void actualizarDatos(ArrayList<Prediccion> respuestaPrimera,ArrayList<Prediccion> respuestaSegunda)
	{
		if (modo_tabla == 0) // un solo partido
		{
			if (modo_partido == 0) // de primera division
			{
				resultados[0].setText(""+respuestaPrimera.get(0).getResultado()); 
				confianza[0].setValue((int) (respuestaPrimera.get(0).getConfianza()*100));
				confianza[0].setString((""+respuestaPrimera.get(0).getConfianza()*100).substring(0, 5)+"%");
			}
			else // de segunda división
			{
				resultados[0].setText(""+respuestaSegunda.get(0).getResultado()); 
				confianza[0].setValue((int) (respuestaSegunda.get(0).getConfianza()*100));
				confianza[0].setString((""+respuestaSegunda.get(0).getConfianza()*100).substring(0, 5)+"%");
			}
		}
		else // todos los partidos -> 10 de primera y 5 de segunda
		{
			for (int i = 0; i < NUM_EQU; i++)
			{
				if (i < num_partidos_primera)
				{
					resultados[i].setText(""+respuestaPrimera.get(i).getResultado()); 
					confianza[i].setValue((int) (respuestaPrimera.get(i).getConfianza()*100));
					confianza[i].setString((""+respuestaPrimera.get(i).getConfianza()*100).substring(0, 5)+"%");
				}
				else
				{
					resultados[i].setText(""+respuestaSegunda.get(i).getResultado()); 
					confianza[i].setValue((int) (respuestaSegunda.get(i).getConfianza()*100));
					confianza[i].setString((""+respuestaSegunda.get(i).getConfianza()*100).substring(0, 5)+"%");
				}
			}
		}
	}
	
	

}
