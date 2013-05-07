package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;


/**
 * NOTA: Hemos cogido la clase tal cual para poder adaptarla a nuestras necesidades
 * Podriamos haber extendido de ella, pero hemos preferido "trastear" un poco con ella
 * para ver como podiamos manipularla
 */
/**
 * Shows concepts, defined and inferred instances and allows to select one.
 * @author Juan Ant. Recio García
 */
public class ArbolPersonalizado extends JPanel  implements TreeSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private JTree ontologyTree;
	private DefaultMutableTreeNode root;
	private JCheckBox inferredInstancesCB;
	private JButton update;
	private java.util.ArrayList<String> _instances = new java.util.ArrayList<String>();
	private OntoBridge ob;
	// Esto es nuestro - Elegimos la raiz de la que queremos partir
	private String raiz;
	
	private static int maxdepth = 20; //Constant to avoid cycles;
	private static Icon CONCEPT  = new javax.swing.ImageIcon(ArbolPersonalizado.class.getResource("/es/ucm/fdi/gaia/ontobridge/test/gui/class-orange.gif"));      
	private static Icon INSTANCE = new javax.swing.ImageIcon(ArbolPersonalizado.class.getResource("/es/ucm/fdi/gaia/ontobridge/test/gui/instance.gif"));      
	
	/**
	 * Constructor
	 */
	public ArbolPersonalizado(OntoBridge ob, boolean inferenceEnabled, String raiz) {
		super();
		createComponents();
		this.inferredInstancesCB.setSelected(inferenceEnabled);
		this.ob = ob;
		// Ponemos la raiz
		this.raiz = raiz;
		readOntology();
	}
	
	protected void createComponents(){
		JScrollPane scrPnl;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		
		//set border and layout
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder, "Ontology Structure");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		setBorder(compoundBorder);
		
		//set Ontology
		root= new DefaultMutableTreeNode("Thing");

		ontologyTree = new JTree(root);
		ontologyTree.setCellRenderer(new MyRenderer());
		ontologyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		ontologyTree.addTreeSelectionListener(this);
        
		scrPnl = new JScrollPane(ontologyTree);
        scrPnl.setViewportView(ontologyTree);
		
		setLayout(new BorderLayout());
		add(scrPnl,BorderLayout.CENTER);
		
		JPanel options = new JPanel();
		inferredInstancesCB = new JCheckBox("Inferred Instances");
		update = new JButton("Update");
		options.setLayout(new BorderLayout());
		options.add(inferredInstancesCB, BorderLayout.WEST);
		options.add(update, BorderLayout.EAST);
		add(options, BorderLayout.SOUTH);
		
		
		update.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				readOntology();	
			}
		});
		

	}

	String selectedInstance = null;
	
    public void valueChanged(TreeSelectionEvent event) {
    		Object node = ontologyTree.getLastSelectedPathComponent();
    		if(node == null)
    			return;
    		String selected = node.toString();
    		if(ob.existsClass(selected))
    			ontologyTree.getSelectionModel().clearSelection();
    		else
    			selectedInstance = selected;
      }
	
    
    public String getSelectedInstance()
    {
    	return selectedInstance;
    }
    
	/**
	 * Read the ontology classes.
	 * 
	 */
	protected void readOntology() {
		try 
		{
			root.removeAllChildren();
			Iterator<String> rc = ob.listRootClasses();
			String aux;
			while(rc.hasNext())
			{
				aux = rc.next();
				// Evitamos pasar la URI completa mirando solo el final de la cadena
				if (aux.endsWith(raiz)) {
					DefaultMutableTreeNode node = createNode(aux, ob, 0);
					root.add(node);
					break;
				}
			}
			ontologyTree.setModel(new DefaultTreeModel(root));
	        ontologyTree.expandRow(0);
	        
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
		}
	}

	
	private DefaultMutableTreeNode createNode(String nodeName, OntoBridge ob, int depth)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(ob.getShortName(nodeName));
		if(depth > maxdepth)
			return node;
		
			Iterator<String> subClasses = ob.listSubClasses(nodeName, true);
			while(subClasses.hasNext())
			{
				String subClassName = ob.getShortName(subClasses.next());
				if(!subClassName.equals("owl:Nothing"))
					node.add(createNode(subClassName, ob, depth+1));
			}
			Iterator<String> instances;
			if(this.inferredInstancesCB.isSelected())
				instances = ob.listInstances(nodeName);
			else
				instances = ob.listDeclaredInstances(nodeName);
			
			while(instances.hasNext())
			{
				String instanceName = ob.getShortName(instances.next());
				node.add(new DefaultMutableTreeNode(instanceName));
				_instances.add(instanceName);
			}	
		return node;
	}


	class MyRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;
		
		public MyRenderer() {
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

				super.getTreeCellRendererComponent(tree, value, sel, expanded,
												   leaf, row, hasFocus);
				
				try {
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
					if(_instances.contains( dmtn.getUserObject() ))
						setIcon(INSTANCE);
					else
						setIcon(CONCEPT);
				} catch (Exception e) {
					org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
				}
			
			    return this;
		}
	}
}
