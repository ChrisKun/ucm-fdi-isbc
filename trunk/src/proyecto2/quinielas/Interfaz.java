import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableModel;


public class Interfaz extends JFrame{
	
	//constantes (para recorrer la tabla)
	public final int L = 0;
	public final int V = 1;
	public final int R = 2;
	public final int NUM_EQU = 22;
	
	// Elementos de la barra de información que hay que modificar
	private int jp_info_jo;
	private String jp_info_año;
	
	// Elementos a leer y modificar
	JTextField jtxt_jornada;
	
	//botones
	JButton jp_info_butt_ok;
	JButton jp_info_butt_ult;
	
	//Tabla
	JTable jp_tab_tabla_resul;
	String[][] datos_tabla;
	
	public Interfaz()
	{
		//DATOS POR DEFECTO
		jp_info_jo = 0; //jornada (a la hora de ponerlo en pantalla)
		jp_info_año = "2012-2013"; //año (a la hora de ponerlo en pantalla)
		datos_tabla = new String[NUM_EQU][3];
		// Configuración de la ventana
		this.setVisible(true);
		this.setTitle("Quinielas");
		// Falta poner icono :D
		this.setSize(500, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.add(getPanelPrincipal());

	}
	
	public JPanel getPanelPrincipal()
	{
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.add(getPanelInfo(),BorderLayout.NORTH);
		panelPrincipal.add(getPanelTabla(),BorderLayout.CENTER);
		//panelPrincipal.add(getPanelLogBoton(), JPanel.BOTTOM_ALIGNMENT);
		return panelPrincipal;
	}
	
	private JPanel getPanelTabla() {
		JPanel panelTabla = new JPanel();
		// Añadimos el elemento de la tabla en el centro
		jp_tab_tabla_resul = new JTable(getDatosTabla(), getColumnasTabla()); 
		panelTabla.add(jp_tab_tabla_resul, BorderLayout.CENTER);
		//ajustes de la tabla
		jp_tab_tabla_resul.setVisible(true);
		/** FALTA TABLA NO EDITABLE DESDE INTERFAZ **/
		/** FALTA CENTRAR ELEMENTOS COLUMNAS **/
		//jp_tab_tabla_resul.s
		return panelTabla;
	}
	
	
	//SE NECESITARÁ MODIFICAR
	private Object[][] getDatosTabla() {
		String dato;
		for (int i = 0; i < NUM_EQU; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				switch (j)
				{
					case L: dato = "Equipo"+i; break;
					case V: dato = "Equipo"+i; break;
					default: dato = "X";
				}
				datos_tabla[i][j] = dato;
			}
		}
		return datos_tabla;
	}
	
	private Object[] getColumnasTabla(){
		String[] nombreColumnas = {"Equipo Local","Equipo Visitante","Resultado"};
		return nombreColumnas;
		
	}

	// Panel con información sobre la jornada a mostrar y la selección de jornada
	private JPanel getPanelInfo()
	{
		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new BorderLayout());
		// Ahora ya añadimos los elementos al panel de información
		// 1. Vamos a poner un texto que indique la jornada que se muestra y el año
		JLabel jlab_jornada = new JLabel();
		// Font("Titulo fuente", "Atributos", "Tamaño"
		jlab_jornada.setFont(new Font("Verdana", Font.BOLD, 18));
		jlab_jornada.setText("Año Actual: "+jp_info_año+"; Jornada: "+jp_info_jo);
		panelInfo.add(jlab_jornada, BorderLayout.NORTH);
		
		// Ahora la opción para introducir jornada, confirmar y última jornada
		JPanel panelInfo2 = new JPanel();
		panelInfo2.setLayout(new FlowLayout());
		
		// Texto informativo
		JLabel jlab_texto_intro = new JLabel();
		jlab_texto_intro.setText("Por favor, introduce una jornada o pulsa ´última´ :");
		panelInfo2.add(jlab_texto_intro);
		
		// Area para introducir texto
		jtxt_jornada = new JTextField();
		/**FALTA AÑADIR UNA DIMENSIÓN TOPE**/
		jtxt_jornada.setText("AQUI");
		panelInfo2.add(jtxt_jornada);
		
		// Botón de confirmar
		jp_info_butt_ok = new JButton("Confirmar");
		// Añadimos el Listener
			/**FALTA**/
		panelInfo2.add(jp_info_butt_ok);
		
		// Botón de última jornada
		jp_info_butt_ult = new JButton("Última");
		panelInfo2.add(jp_info_butt_ult);
		
		// Añadimos este subpanel y devolvemos el panel completo de información
		panelInfo.add(panelInfo2, BorderLayout.CENTER);
		return panelInfo;
	}
	
	
	public static void main(String[] args)
	{
		Interfaz i = new Interfaz();
	}

}
