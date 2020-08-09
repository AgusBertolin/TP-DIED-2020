package paneles;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dominio.Insumo;
import dominio.Planta;
import dominio.Planta.TipoPlanta;
import dominio.Ruta;
import dominio.Stock;
import servicios.InsumoService;
import servicios.PlantaService;
import servicios.RutaService;

@SuppressWarnings("serial")
public class PanelPlanta extends JPanel {

	private DefaultTableModel modeloPlanta;
	private JTable tablaPlanta;
	private JButton borrar;
	private JButton crear;
	private JButton modificar;
	private JButton actualizarStock;
	private JButton agregarRuta;
	private PlantaService service = new PlantaService();

	
	public PanelPlanta() {
		inicializar();
	}
	
	private void inicializar() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel panelTitulo = new JPanel(new GridBagLayout());

		//Crear tabla
		tablaPlanta = new JTable(modeloPlanta);
		add(tablaPlanta);
		actualizarTabla();
		tablaPlanta.setDefaultEditor(Object.class, null);

		//Crear botones

		//Boton BORRAR
		borrar = new JButton("Borrar");
		borrar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(tablaPlanta.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(new JFrame(), "No se ha seleccionado una planta", "Informacion", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					int res = JOptionPane.showConfirmDialog(getParent(), "¿Desea borrar esta planta?", "Advertencia", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (res == 0) {
						service.borrarPlanta((Integer) modeloPlanta.getValueAt(tablaPlanta.getSelectedRow(), 0));
						actualizarTabla();
					}
				}
				
			}
		});
		
		
		//Boton AÑADIR
		crear = new JButton("Añadir");
		crear.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					crearPlanta();
			}
			
		});
	
		//Boton Modificar
		modificar = new JButton("Modificar");
		modificar.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(tablaPlanta.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(new JFrame(), "No se ha seleccionado una planta", "Informacion", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					Planta p = service.buscarPlanta(Integer.valueOf(modeloPlanta.getValueAt(tablaPlanta.getSelectedRow(), 0).toString()));
					modificarPlanta(p);					
				}
				
			}
			
		});
		
		//Boton agregar Stock
		actualizarStock = new JButton("Agregar Insumo");
		actualizarStock.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(tablaPlanta.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(new JFrame(), "No se ha seleccionado una planta", "Informacion", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					Planta p = service.buscarPlanta(Integer.valueOf(modeloPlanta.getValueAt(tablaPlanta.getSelectedRow(), 0).toString()));
					agregarInsumo(p);					
				}
				
			}
			
		});

		//Boton agregar Ruta
		agregarRuta = new JButton("Agregar Ruta");
		agregarRuta.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				List<Planta> aux = service.buscarTodos();
				
				if(aux.size() < 2) {
						JOptionPane.showMessageDialog(new JFrame(), "No hay Plantas suficientes", "Informacion", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						agregarRuta(aux);
					}
				}
			
		});
		
		JScrollPane tableSP = new JScrollPane(tablaPlanta);
		tableSP.setPreferredSize(new Dimension(1200, 500));
		 
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.anchor = GridBagConstraints.CENTER; 
	    constraints.insets = new Insets(10, 10, 10, 10);
	          
	    constraints.gridx = 2;
	    constraints.gridy = 0;     
	    JLabel titulos = new JLabel("Control de Plantas");
	    titulos.setFont(new Font("Panel Plantas", Font.TYPE1_FONT, 30));
	    titulos.setSize(new Dimension(300, 400));
	    panelTitulo.add(titulos, constraints);
	    
	    // add components to the panel
	    constraints.gridx = 0;
	    constraints.gridy = 1;     
	    panelTitulo.add(borrar, constraints);
	  
	    constraints.gridx = 1;
	    panelTitulo.add(modificar, constraints);
	          
	    constraints.gridx = 2;    
	    panelTitulo.add(crear, constraints);
	          
	    constraints.gridx = 3;
	    panelTitulo.add(actualizarStock, constraints);
	    
	    constraints.gridx = 4;
	    panelTitulo.add(agregarRuta, constraints);
	    
	    JPanel panelTabla = new JPanel(new GridBagLayout());
	    GridBagConstraints constraintsTabla = new GridBagConstraints();
	    constraintsTabla.anchor = GridBagConstraints.BOTH;
	    constraintsTabla.insets = new Insets(0, 0, 0, 5);
	    constraintsTabla.gridy = 2;
	    constraintsTabla.gridx = 1;
	    constraintsTabla.gridwidth = 8;
	    constraintsTabla.anchor = GridBagConstraints.CENTER;
	    panelTabla.add(tableSP, constraintsTabla);
	    
	    add(panelTitulo);
	    add(panelTabla);
	    
	}
	
	public void crearPlanta() {
		final JDialog dialogo = new JDialog(new JFrame(), "Crear planta", Dialog.ModalityType.DOCUMENT_MODAL); 
		dialogo.setResizable(false);
		dialogo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panelCrear = new JPanel();
        panelCrear.setLayout(new BoxLayout(panelCrear, BoxLayout.PAGE_AXIS)); //VERRRRRRRRRRRRRRR
        JButton botonAñadir = new JButton("Guardar");
        
        //Etiquetas
        JLabel nombreEtiqueta = new JLabel("Nombre: ");
        JLabel tipoEtiqueta = new JLabel("Tipo: ");
        
    	//Campos
        final JTextField textoNombre = new JTextField(30);
        final JComboBox<TipoPlanta> tipoComboBox = new JComboBox<TipoPlanta>(); 
        tipoComboBox.setModel(new DefaultComboBoxModel<TipoPlanta>(TipoPlanta.values()));
        
        //Panel del mensaje
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
          
        constraints.gridx = 0;
        constraints.gridy = 1;     
        newPanel.add(nombreEtiqueta, constraints);
  
        constraints.gridx = 1;
        newPanel.add(textoNombre, constraints);
          
        constraints.gridx = 0;
        constraints.gridy = 2;     
        newPanel.add(tipoEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(tipoComboBox, constraints);
         
        panelCrear.add(newPanel);
        
        //Crear Planta
        ActionListener al =  new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if(textoNombre.getText().length() == 0 ) {
					JOptionPane.showMessageDialog(getParent(), "Error: El nombre esta vacío", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					boolean todoOk = true;
					switch (TipoPlanta.valueOf(tipoComboBox.getSelectedItem().toString())) {
					case Acopio_Final:
						List<Planta> aux = service.buscarTodos();
						int i = 0;
						while(todoOk && !aux.get(i).equals(null)) {
							if(aux.get(i).getTipoPlanta() == TipoPlanta.Acopio_Final) todoOk = false;
							i++;
						}
						break;
					case Acopio_Puerto:
						List<Planta> aux2 = service.buscarTodos();
						int j = 0;
						while(todoOk && !aux2.get(j).equals(null)) {
							if(aux2.get(j).getTipoPlanta() == TipoPlanta.Acopio_Puerto) todoOk = false;
							j++;
						}
						break;
					case Produccion:
						break;
					default:
						break;
					
					}
					if(!todoOk) {
						JOptionPane.showMessageDialog(getParent(), "Error: Solo puede haber una planta de ese tipo", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						Planta p = new Planta();
						
						p.setNombre(textoNombre.getText());
						p.setTipoPlanta(TipoPlanta.valueOf(tipoComboBox.getSelectedItem().toString()));
					
						service.crearPlanta(p);
						actualizarTabla();
						dialogo.dispose();
					}
				}
			}
        	
        };
        botonAñadir.addActionListener(al); 
  
        JPanel panelBoton = new JPanel();
        panelBoton.setSize(new Dimension(400, 120));
        panelBoton.setLayout(new BorderLayout(0,0));
        panelBoton.add(botonAñadir, BorderLayout.CENTER); 
  
        panelCrear.add(panelBoton);
        dialogo.add(panelCrear);  
        dialogo.setSize(400, 300); 
        dialogo.setVisible(true); 
	}
	
	public void modificarPlanta(final Planta p) {
        
		final JDialog dialogo = new JDialog(new JFrame(), "Modificar planta", Dialog.ModalityType.DOCUMENT_MODAL); 
		dialogo.setResizable(false);
		dialogo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panelModificar = new JPanel();
        panelModificar.setLayout(new BoxLayout(panelModificar, BoxLayout.PAGE_AXIS));
        
      //Etiquetas
        JLabel nombreEtiqueta = new JLabel("Nombre: ");
        JLabel tipoEtiqueta = new JLabel("Tipo: ");
        
    	//Campos
        final JTextField textoNombre = new JTextField(p.getNombre(), 30);
        final JComboBox<TipoPlanta> tipoComboBox = new JComboBox<TipoPlanta>(); 
        tipoComboBox.setModel(new DefaultComboBoxModel<TipoPlanta>(TipoPlanta.values()));
        tipoComboBox.setSelectedItem(p.getTipoPlanta());
        
        //Boton       
        JButton botonGuardar = new JButton("Guardar cambios");
         
        //Se agregan los componentes en el panel
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
 
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
          
        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;     
        newPanel.add(nombreEtiqueta, constraints);
  
        constraints.gridx = 1;
        newPanel.add(textoNombre, constraints);
          
        constraints.gridx = 0;
        constraints.gridy = 1;     
        newPanel.add(tipoEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(tipoComboBox, constraints);
                  
        panelModificar.add(newPanel);
  
        //Creamos el boton
        ActionListener al =  new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if(		textoNombre.getText().length() == 0 ) {
					JOptionPane.showMessageDialog(getParent(), "Error: Ingrese un nombre", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					p.setNombre(textoNombre.getText());
					p.setTipoPlanta(TipoPlanta.valueOf(tipoComboBox.getSelectedItem().toString()));
					service.crearPlanta(p);
					actualizarTabla();
					dialogo.dispose();
				}
			}
        	
        };
        botonGuardar.addActionListener(al); 

        //Añadimos es boton al panel
        JPanel panelBoton = new JPanel();
        panelBoton.setSize(new Dimension(400, 100));
        panelBoton.setLayout(new BorderLayout(0,0));
        panelBoton.add(botonGuardar, BorderLayout.CENTER);
        
        panelModificar.add(panelBoton);
        dialogo.add(panelModificar); 
        dialogo.setSize(420, 300); 
        dialogo.setVisible(true); 

	}
	
	public void agregarInsumo(final Planta p) {
		
		final JDialog dialogo = new JDialog(new JFrame(), "Agregar Insumo", Dialog.ModalityType.DOCUMENT_MODAL); 
		dialogo.setResizable(false);
		dialogo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panelAgregarI = new JPanel();
        panelAgregarI.setLayout(new BoxLayout(panelAgregarI, BoxLayout.PAGE_AXIS)); //VERRRRRRRRRRRRRRR
        JButton botonAñadir = new JButton("Guardar");
        
        //Etiquetas
        JLabel insumoEtiqueta = new JLabel("Insumo: ");
        JLabel cantidadEtiqueta = new JLabel("Cantidad: ");
        JLabel ptoPedidoEtiqueta = new JLabel("Punto de Pedido: ");
        
    	//Campos
        final JComboBox<String> insumoComboBox = new JComboBox<String>(); 
        final JTextField textoCantidad = new JTextField(16);
        final JTextField textoPtoPedido = new JTextField(16);
        
        //Obtener nombres Insumos
        InsumoService is = new InsumoService();
        final List<Insumo> insumos = is.buscarTodos();
        for(Insumo i: insumos) {
        	insumoComboBox.addItem(i.getDescripcion());
        }
       
        //Panel del mensaje
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
          
        constraints.gridx = 0;
        constraints.gridy = 1;     
        newPanel.add(insumoEtiqueta, constraints);
  
        constraints.gridx = 1;
        newPanel.add(insumoComboBox, constraints);
          
        constraints.gridx = 0;
        constraints.gridy = 2;     
        newPanel.add(cantidadEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(textoCantidad, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3;     
        newPanel.add(ptoPedidoEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(textoPtoPedido, constraints);
         
        panelAgregarI.add(newPanel);
        
        //Crear Planta
        ActionListener al =  new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if(		textoCantidad.getText().length() == 0 ||
						textoPtoPedido.getText().length() == 0) {
					JOptionPane.showMessageDialog(getParent(), "Error: Uno de los campos se encuentra vacío", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					Stock s = new Stock();
					String d = insumoComboBox.getSelectedItem().toString();
					boolean encontrado = false;
					int j = 0;
					while(!encontrado) {
						if(insumos.get(j).getDescripcion().equals(d)) {
							s.setInsumo(insumos.get(j));
							encontrado = true;
						}
						j++;
					}
					s.setCantidad(Integer.valueOf(textoCantidad.getText()));
					s.setPuntoDeReposicion(Integer.valueOf(textoPtoPedido.getText()));
					p.agregarStock(s);
					service.crearPlanta(p);
					//actualizarTabla();
					dialogo.dispose();
					
				}
			}
        	
        };
        botonAñadir.addActionListener(al); 
  
        JPanel panelBoton = new JPanel();
        panelBoton.setSize(new Dimension(400, 120));
        panelBoton.setLayout(new BorderLayout(0,0));
        panelBoton.add(botonAñadir, BorderLayout.CENTER); 
  
        panelAgregarI.add(panelBoton);
        dialogo.add(panelAgregarI);  
        dialogo.setSize(400, 300); 
        dialogo.setVisible(true); 
	}
	
	public void agregarRuta(final List<Planta> plantas) {
		final JDialog dialogo = new JDialog(new JFrame(), "Agregar Ruta", Dialog.ModalityType.DOCUMENT_MODAL); 
		dialogo.setResizable(false);
		dialogo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panelAgregarI = new JPanel();
        panelAgregarI.setLayout(new BoxLayout(panelAgregarI, BoxLayout.PAGE_AXIS)); //VERRRRRRRRRRRRRRR
        JButton botonAñadir = new JButton("Guardar");
        
        //Etiquetas
        JLabel plantaOEtiqueta = new JLabel("Planta de origen: ");
        JLabel plantaDEtiqueta = new JLabel("Planta destino: ");
        JLabel distanciaEtiqueta = new JLabel("Distancia (Km): ");
        JLabel duracionEtiqueta = new JLabel("Duracion (Horas): ");
        JLabel pesoEtiqueta = new JLabel("Peso maximo transportable (Kg): ");
        
    	//Campos
        final JComboBox<String> plantaOComboBox = new JComboBox<String>();
        final JComboBox<String> plantaDComboBox = new JComboBox<String>();
        final JTextField textoDistancia = new JTextField(16);
        final JTextField textoDuracion = new JTextField(16);
        final JTextField textoPeso = new JTextField(16);
        
        //Obtener nombres Plantas
        for(Planta p: plantas) {
        	switch (p.getTipoPlanta()) {
			case Acopio_Final:
				plantaDComboBox.addItem(p.getNombre());
				break;
			case Acopio_Puerto:
				plantaOComboBox.addItem(p.getNombre());
				break;
			case Produccion:
	        	plantaOComboBox.addItem(p.getNombre());
	        	plantaDComboBox.addItem(p.getNombre());
				break;
			default:
				break;
        	
        	}
        }
       
        //Panel del mensaje
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
          
        constraints.gridx = 0;
        constraints.gridy = 1;     
        newPanel.add(plantaOEtiqueta, constraints);
  
        constraints.gridx = 1;
        newPanel.add(plantaOComboBox, constraints);
          
        constraints.gridx = 0;
        constraints.gridy = 2;     
        newPanel.add(plantaDEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(plantaDComboBox, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3;     
        newPanel.add(distanciaEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(textoDistancia, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 4;     
        newPanel.add(duracionEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(textoDuracion, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 5;     
        newPanel.add(pesoEtiqueta, constraints);
          
        constraints.gridx = 1;
        newPanel.add(textoPeso, constraints);
        
        panelAgregarI.add(newPanel);
        
        //Crear Ruta
        ActionListener al =  new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				if(		textoDistancia.getText().length() == 0 ||
						textoDuracion.getText().length() == 0 ||
						textoPeso.getText().length() == 0) {
					JOptionPane.showMessageDialog(getParent(), "Error: Uno de los campos se encuentra vacío", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					if(plantaOComboBox.getSelectedItem().toString() == plantaDComboBox.getSelectedItem().toString()) {
						JOptionPane.showMessageDialog(getParent(), "Error: No se puede crear una ruta desde una planta hacia si misma", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						Planta po = null;
						Planta pd = null;
						int i = 0;
						while(po == null || pd == null) {
							if(plantas.get(i).getNombre() == plantaOComboBox.getSelectedItem().toString()) {
								po = plantas.get(i);
							}
							else {
								if(plantas.get(i).getNombre() == plantaDComboBox.getSelectedItem().toString()) {
									pd = plantas.get(i);
								}
							}
							i++;
						}
						
						Ruta r = new Ruta();
						r.setOrigen(po);
						r.setDestino(pd);
						r.setDistanciaKM(Double.valueOf(textoDistancia.getText()));
						r.setDuracion(Integer.valueOf(textoDuracion.getText()));
						r.setPesoMaxPorDia(Double.valueOf(textoPeso.getText()));
						
						RutaService rs = new RutaService();
						rs.saveOrUpdate(r);
						//actualizarTabla();
						dialogo.dispose();
					}
				}
			}
        	
        };
        botonAñadir.addActionListener(al);   
        botonAñadir.setPreferredSize(new Dimension(100, 100));
        
        JPanel panelBoton = new JPanel();
        panelBoton.setSize(new Dimension(400, 40));
        panelBoton.setLayout(new BorderLayout(0,0));
        panelBoton.add(botonAñadir, BorderLayout.CENTER); 
  
        panelAgregarI.add(panelBoton);
        dialogo.add(panelAgregarI);  
        dialogo.setSize(400, 300); 
        dialogo.setVisible(true); 
	}
	
	public void actualizarTabla() {
		//crear modelo tabla
				String[] columnas = {"Id", "Nombre", "Tipo"};
				modeloPlanta = new DefaultTableModel(columnas, 0);
				
				//buscar datos de la db
				List<Planta> plantas = service.buscarTodos();
				
			
				for(Planta p: plantas) {
					Integer id = p.getId();
					String nombre = p.getNombre();
					String tipoPlanta = p.getTipoPlanta().toString();
					Object[] renglon = {id, nombre, tipoPlanta};
					modeloPlanta.addRow(renglon);
				}

				//actualizar modelo
				tablaPlanta.setModel(modeloPlanta);

	}
	
}