package proyecto2.quinielas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableModel;


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
	public final static int NUM_JOR_PRIMERA = 38;
	public final static int NUM_JOR_SEGUNDA = 42;
	
	// Elementos de la barra de información que hay que modificar
	//private int jp_info_jo;
	//private String jp_info_año;
	private JTextField jplog_log;
	private JLabel jlab_jornada;
	private int num_jornada;
	
	// Elementos a leer y modificar
	private JTextField jtxt_jornada;
	private JProgressBar jplog_barra;
	
	//botones
	private JButton jp_info_butt_ok;
	private JButton jp_info_butt_ult;
	private JButton jplog_actualizar;
	
	//Tabla
	JTable jp_tab_tabla_resul;
	String[][] datos_tabla;
	String[] nombreColumnas;
	
	public Interfaz()
	{
		//DATOS POR DEFECTO, INICIALIZACION
		num_jornada = 1; //jornada
		//jp_info_año = "2012-2013"; //año (a la hora de ponerlo en pantalla)
		datos_tabla = new String[NUM_EQU][3];
		jlab_jornada = new JLabel();
		// Configuración de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas");
		// Falta poner icono :D
		this.setSize(500, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.add(getPanelPrincipal());
		this.setJMenuBar(getMenuPrincipal());

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
		panelPrincipal.add(getInfoAñoJornada(),BorderLayout.NORTH);
		panelPrincipal.add(getTabla());
		panelPrincipal.add(getPanelLog(), BorderLayout.SOUTH);
		panelPrincipal.validate();
		return panelPrincipal;
	}
	
	// Panel que contiene la tabla con los equipos y los resultados
	private JTable getTabla() {
		// Añadimos el elemento de la tabla en el centro
		jp_tab_tabla_resul = new JTable(getDatosDefecto(), getColumnasTabla());
		//ajustes de la tabla
	//	setTablaNoEditable();
		jp_tab_tabla_resul.setVisible(true);
		jp_tab_tabla_resul.setEnabled(false); //No se permite editar la tabla
		jp_tab_tabla_resul.setRowHeight(25);
		/** FALTA CENTRAR ELEMENTOS COLUMNAS **/
		return jp_tab_tabla_resul;
	}
	
	
	// Panel donde está el log para cargar resultados, una barra para cargar y los botones
	/** RETOCAR MÉTODO: AHORA ESTÁ CHAPUZA **/
	private JPanel getPanelLog()
	{
		// Definición del panel
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());

		
		// LOG : información
		jplog_log = new JTextField();
		jplog_log.setEditable(false);
		jplog_log.setSize(10, 200);
		p.add(jplog_log);

		// Barra de progreso
		jplog_barra = new JProgressBar();
		jplog_barra.setValue(0);
		p.add(jplog_barra);
		
		// Boton de Actualizar
		jplog_actualizar = new JButton("Actualizar Datos");
		jplog_actualizar.addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent evt ) {
		    		actualizarBaseDatos();
		    	      }
		      });
		p.add(jplog_actualizar);
		
		//Selección de jornada
		JLabel j = new JLabel("Jornada: ");
		// Lista donde aparecen todas las jornadas
		String[] nom_jor = new String[NUM_JOR_PRIMERA];
		for (int i = 0; i <NUM_JOR_PRIMERA; i++)
			nom_jor[i]=""+(i+1);
		JComboBox b = new JComboBox(nom_jor);
		b.addActionListener(new ActionListener() {
		      public void actionPerformed( ActionEvent e) {
		    		actualizarJornada(e);
		    	      }
		      });
		p.add(j);
		p.add(b);
		
		
		return p;
	}
	
	
	//SE NECESITARÁ MODIFICAR
	private Object[][] getDatosDefecto() {
		String dato;
		for (int i = 0; i < NUM_EQU; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				switch (j)
				{
					case LOCAL: dato = "Equipo"+i; break;
					case VISITANTE: dato = "Equipo"+i; break;
					default: dato = "X";
				}
				setCeldaTabla(i,j,dato);
			}
		}
		return datos_tabla;
	}
	
	private Object[] getColumnasTabla(){
		nombreColumnas = new String[3];
		nombreColumnas[LOCAL]="Equipo Local";
		nombreColumnas[VISITANTE]="Equipo Visitante";
		nombreColumnas[RESULTADO]="Resultado";
		return nombreColumnas;
		
	}
	
	private JLabel getInfoAñoJornada()
	{
		setJornadaAño(num_jornada,"2012-2013");
		return jlab_jornada;
	}

	// Panel con información sobre la jornada a mostrar y la selección de jornada
	private JPanel getPanelInfoOUTDATED()
	{
		// Area para introducir texto
		jtxt_jornada = new JTextField();
		/**FALTA AÑADIR UNA DIMENSIÓN TOPE**/
		jtxt_jornada.setText("AQUI");
		//panelInfo2.add(jtxt_jornada);
		
		// Botón de confirmar
		jp_info_butt_ok = new JButton("Confirmar");
		// Añadimos el Listener
			/**FALTA**/
	//	panelInfo2.add(jp_info_butt_ok);
		
		// Botón de última jornada
		jp_info_butt_ult = new JButton("Última");
		//panelInfo2.add(jp_info_butt_ult);
		
		// Añadimos este subpanel y devolvemos el panel completo de información
		//panelInfo.add(panelInfo2, BorderLayout.CENTER);
		//return panelInfo;
		return null;
	}
	
	/** FUNCIONES PARA EDITAR INFORMACIÓN DE LA INTERFAZ**/
	// EDITAR JORNADA Y AÑO
	public void setJornadaAño(int jornada, String año)
	{
		// Font("Titulo fuente", "Atributos", "Tamaño"
		jlab_jornada.setFont(new Font("Verdana", Font.BOLD, 22));
		jlab_jornada.setText("Año Actual: "+año+"; Jornada: "+jornada);
	}
	//EDITAR CELDA DE LA TABLA
	public void setCeldaTabla(int fila, int columna, String dato)
	{
		datos_tabla[fila][columna] = dato;
		if (jp_tab_tabla_resul != null)
			jp_tab_tabla_resul.repaint();
		
	}
	
	// Métodos Listener
	public void actualizarBaseDatos()
	{
		//setCeldaTabla(1,1,"HOLA");
		//setJornadaAño(2,"2012-2013");

	}
	
	public void actualizarJornada(ActionEvent e)
	{
		@SuppressWarnings("rawtypes")
		JComboBox cb = (JComboBox)e.getSource();
	    String newSelection = (String)cb.getSelectedItem();
	    num_jornada = Integer.parseInt(newSelection);
	    setJornadaAño(num_jornada,"2012-2013");
	}
	
	
	public static void main(String[] args)
	{
		Interfaz i = new Interfaz();
	}
	
	

}
