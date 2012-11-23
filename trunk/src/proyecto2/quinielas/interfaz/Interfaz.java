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
	
	/** LEER DESDE FICHERO **/
	public static int jornada_max_actual_primera = 7; // jornada de la temporada actual a la que se puede acceder
	public static int jornada_max_actual_segunda = 13;
	public final static int num_temporada_inicial = 2000;
	public final static int num_temporada_actual = 2012;
	
	/** Número de jornadas totales para primera y segunda división */
	public final static int NUM_JOR_PRIMERA = 38;
	public final static int NUM_JOR_SEGUNDA = 42;
	public final static int PRIMERA_DIVISION = 0;
	public final static int SEGUNDA_DIVISION = 1;
	
	// Elementos de la barra de información que hay que modificar
	private JTextField jplog_log;
	private JLabel jlab_jornada;
	private int num_jornada_primera;
	private int num_jornada_segunda;
	private int num_temporada;
	
	// Elementos a leer y modificar
	private JProgressBar jplog_barra;
	JComboBox[] comboBox_jornada;
	JRadioButton rbutt_uno;
	JRadioButton rbutt_var;
	
	//botones
	private JButton jplog_actualizar;
	
	//Tabla
	private DefaultTableModel modelo;
	private ArrayList<String> equipos;
	
	public Interfaz()
	{
		//DATOS POR DEFECTO, INICIALIZACION
		num_jornada_primera = 1; //jornada
		num_jornada_segunda = 1;
		num_temporada = 2012;
		equipos = new ArrayList<String>();
		//por defecto
		for (int i = 0; i < NUM_EQU; i++)
			equipos.add("EquipoLocal"+(i+1)+","+"EquipoVisitante"+(i+1));
		
		jlab_jornada = new JLabel();
		comboBox_jornada = new JComboBox[2];
		
		// Configuración de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas");
		// Falta poner icono :D
		this.setSize(500, 600);
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
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.add(getPanelInformacion(), BorderLayout.NORTH);
		panelPrincipal.add(getTabla(), BorderLayout.CENTER);
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
		// TODO cambiar esto para referirse fácilmente al límite de elementos de cada JComboBox
		
		JLabel labelJor2 = new JLabel("Jornada Segunda División: ");
		p.add(labelJor2);
		
		p.add(getDesplegableJornada(SEGUNDA_DIVISION));
		
		return p;
	}
	
	private JComboBox getDesplegableTemporada()
	{
		int num_temporadas = num_temporada_actual - num_temporada_inicial + 1;
		String[] nom_temp = new String[num_temporadas];
		for (int i = 0; i <num_temporadas; i++)
			nom_temp[i] = (num_temporada_inicial+i)+"-"+(num_temporada_inicial+(i+1));
		
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
			case PRIMERA_DIVISION: n_max = NUM_JOR_PRIMERA; break;
			case SEGUNDA_DIVISION: n_max = NUM_JOR_SEGUNDA; break;
		}
		
		// Actualizamos el valor si es la temporada actual:
		// Como máximo se muestra la jornada actual + 1
		if (num_temporada == num_temporada_actual)
		{
			switch (division)
			{
				case PRIMERA_DIVISION: n_max = jornada_max_actual_primera; break;
				case SEGUNDA_DIVISION: n_max = jornada_max_actual_segunda; break;
			}
		}
		comboBox_jornada[division] = new JComboBox();	
		
		comboBox_jornada[division].addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		actualizarJornada(e); //TODO actualizarJornada para JComboBox Primera o JComboBox segunda
		    	      }
		      });
		setNumElemComboBoxJornada(n_max,division);	//TODO este método debería estar mejor en actualizarJornada(e);
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
	private JTable getTabla() {
		modelo = new DefaultTableModel();
		JTable t = new JTable(modelo);
		modelo.addColumn("Equipo Local");
		modelo.addColumn("Equipo Visitante");
		modelo.addColumn("Resultado");
		modelo.addColumn("Similitud");
		leerATabla(equipos);
		return t;
	}
	
	private void leerATabla(ArrayList<String> equipos2) {
		
		String str;
		String[] res;
		int num_max = NUM_EQU;
		
		// Borramos la tabla actual
		borrarTabla();
		// Cargamos la nueva tabla desde la estructura
		
		if (modo_tabla == 0)
			num_max = 1;
		
		for (int i = 0; i < equipos2.size() && i < num_max; i++)
		{
			str = equipos2.get(i);
			res = str.split(",");
			modelo.addRow(res);
		}
		
	}
	
	private void borrarTabla()
	{
		modelo.setRowCount(0);
	}
	
	private void deTablaAEstructura()
	{
		String str="";
		equipos = new ArrayList<String>();
		for (int i = 0; i < modelo.getRowCount(); i++)
		{
			for (int j = 0; j < 2; j++)
			{
				str = str+modelo.getValueAt(i,j);
				if (j == 0)
					str = str+",";
			}
			
			equipos.add(str);	
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
		jplog_barra = new JProgressBar();
		jplog_barra.setValue(0);
		p.add(jplog_barra);
		
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
		setJornadaAño(num_jornada_primera, num_jornada_segunda,num_temporada);
		p.add(jlab_jornada);
		return p;
	}
	
	/** FUNCIONES PARA COMUNICARSE CON EL RESTO DEL PROGRAMA **/
	
	public ArrayList<String> getPartidos()
	{
		return equipos;
	}
	
	
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
		deTablaAEstructura();
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
	    		num_jornada_primera = n_jornada;
	    	else
	    		num_jornada_segunda = n_jornada;
	    		
	    }
	    setJornadaAño(num_jornada_primera,num_jornada_segunda,num_temporada);
	}
	
	public void actualizarTemporada(ActionEvent e)
	{	
		@SuppressWarnings("rawtypes")
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    // parseamos la entrada
	    num_temporada = Integer.parseInt(newSelection.substring(0, 4));
	    // actualizamos la temporada (por defecto a 1)
	    
		if (num_temporada == num_temporada_actual)
		{
			setNumElemComboBoxJornada(jornada_max_actual_primera,PRIMERA_DIVISION);
			setNumElemComboBoxJornada(jornada_max_actual_segunda,SEGUNDA_DIVISION);
		}
		else
		{
			setNumElemComboBoxJornada(NUM_JOR_PRIMERA,PRIMERA_DIVISION);
			setNumElemComboBoxJornada(NUM_JOR_SEGUNDA,SEGUNDA_DIVISION);
		}
	    num_jornada_primera = 1;
	    num_jornada_segunda = 1;
	    setJornadaAño(num_jornada_primera, num_jornada_segunda,num_temporada);
	}

	public void actualizarListaPartidos()
	{
		if (rbutt_uno.isSelected())
			modo_tabla = 0; // Modo de un sólo partido
		else
			modo_tabla = 1; // Modo de 15 partidos
		leerATabla(equipos);
	}
	
	
	public static void main(String[] args)
	{
		JOptionPane.showMessageDialog(null, "ACTUALIZANDO");
		Interfaz i = new Interfaz();
	}
	

}
