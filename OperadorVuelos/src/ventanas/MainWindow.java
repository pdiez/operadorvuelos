package ventanas;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXAutoCompletePopup;

import application.Aerolinea;
import application.Aeropuerto;
import application.Ciudad;
import application.Donut;
import application.Ruta;
import application.ToolTipDefaultsFixer;
import application.Vuelo;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class MainWindow implements Initializable {
	private static final String MAINFXML = "MainWindow.fxml";
	private static final String CITYEDITPO = "CityEditWindow.fxml";
	private static final String CITYADDPO = "CityAddWindow.fxml";
	private static final String AEROLADDPO = "AeroLAddWindow.fxml";
	private static final String AEROLEDITPO = "AeroLEditWindow.fxml";
	private static final String AEROPADDPO = "AeroPAddWindow.fxml";
	private static final String AEROPEDITPO = "AeroPEditWindow.fxml";
	private static final String VUELOADDPO = "VueloAddWindow.fxml";
	private static final String VUELOEDITPO = "VueloEditWindow.fxml";
	private Parent parent;
    private Scene scene;
    private Stage stage;
    
    private ObservableList<Ciudad> ciudadesData = FXCollections.observableArrayList();
    private ObservableList<Aeropuerto> aeropuertosData = FXCollections.observableArrayList();
    private ObservableList<Aerolinea> aerolineasData = FXCollections.observableArrayList();
    private ObservableList<Ruta> rutasData = FXCollections.observableArrayList();
    private ObservableList<Vuelo> vuelosData = FXCollections.observableArrayList();
    
    @FXML
    private Label lblCiudadesDisponibles, lblCiudadesActivas, lblAerolineasDisponibles, lblAerolineasActivas, lblAeropuertosDisponibles, lblAeropuertosActivos;
    
    @FXML
    private Label labelMatches;
    
    @FXML
    private Pane shadowPane, aeroPChart, aeroLChart, cityChart;
    
    @FXML
    private CustomTextField fieldSearch, fieldSearchRutas; 
    
    @FXML
    private AnchorPane mainAnchor;
    
    @FXML
    private JFXButton exitButton ,cityEdit, cityAdd, aeroLAdd, aeroLEdit, aeroPAdd, aeroPEdit, btnNuevoVuelo, btnNuevaRuta;
    
	@FXML
    private JFXComboBox<Aeropuerto> cboRutaOrigen, cboRutaDestino;
    
    @FXML
    private JFXListView<Ruta> lstRutas;
    
    @FXML
    private Button clearSearch, clearSearchRutas;
    
    @FXML
    private void exitApp() {
    	Platform.exit();
    }
    
    @FXML
    private TableView<Vuelo> tablaVuelos;
    
    @FXML
    private GridPane grdCityEdit;
    
    public MainWindow() {
    	 rellenaDatosSample();
    	 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAINFXML));
		 fxmlLoader.setController(this);
	     try {
	      	 parent = (Parent) fxmlLoader.load();
	         scene = new Scene(parent);
	         scene.getStylesheets().add(getClass().getResource("/assets/app.css").toExternalForm());
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
    }
    

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.mainAnchor.getStyleClass().add("mainPane");		
		this.shadowPane.getStyleClass().add("shadowPane");
		ToolTipDefaultsFixer.setTooltipTimers(300, 5000, 300);
		cityEdit.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(CITYEDITPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Editar ciudades");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				JFXComboBox<Ciudad> cboCityEdit = (JFXComboBox) p.lookup("#cboCityEdit");
				cboCityEdit.setEditable(false);
			
				JFXTextField txtCityEdit = (JFXTextField) p.lookup("#txtCityEdit");
				JFXCheckBox chkCityEdit = (JFXCheckBox) p.lookup("#chkCityEdit");
				cboCityEdit.getItems().addAll(ciudadesData.sorted());
				cboCityEdit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ciudad>() {
					@Override
					public void changed(ObservableValue<? extends Ciudad> observable, Ciudad oldValue,
							Ciudad newValue) {
						if (newValue == null) {
							txtCityEdit.setText(null);
						} else {
							txtCityEdit.setText(newValue.getNombre());
							chkCityEdit.setSelected(newValue.isActivo());
						}
					
						
					}});
				
				JFXButton poCityEditClose = (JFXButton) p.lookup("#poCityEditClose");
				JFXButton poCityEditGuardar = (JFXButton) p.lookup("#poCityEditGuardar");
				Label poCityEditLabel = (Label) p.lookup("#poCityEditLabel");
				poCityEditGuardar.setOnAction(x -> {
					if (!txtCityEdit.getText().equals("") ) {
						Ciudad c = (Ciudad) cboCityEdit.getSelectionModel().getSelectedItem();
						c.setActivo(chkCityEdit.isSelected());
						c.setNombre(txtCityEdit.getText());
						cboCityEdit.getItems().clear();
						cboCityEdit.getItems().addAll(ciudadesData.sorted());
						poCityEditLabel.setText("Se ha modificado la ciudad.");
						parpadeoLabel(poCityEditLabel);
						
						
						actualizaDashboard();
					}
				});
				poCityEditClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(cityEdit);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
			
		cityAdd.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(CITYADDPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Añadir una ciudad");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				JFXTextField txtCityAdd = (JFXTextField) p.lookup("#txtCityAdd");
				JFXCheckBox chkCityAdd = (JFXCheckBox) p.lookup("#chkCityAdd");
				JFXButton poCityAddClose = (JFXButton) p.lookup("#poCityAddClose");
				JFXButton poCityAddGuardar = (JFXButton) p.lookup("#poCityAddGuardar");
				Label poCityAddLabel = (Label) p.lookup("#poCityAddLabel");
				poCityAddGuardar.setOnAction(x -> {
					if (!txtCityAdd.getText().equals("") ) {
						Ciudad c = new Ciudad(txtCityAdd.getText(), chkCityAdd.isSelected());
						ciudadesData.add(c);
						poCityAddLabel.setText("Se ha añadido la ciudad.");
						parpadeoLabel(poCityAddLabel);
						
						actualizaDashboard();
					}
				});
				poCityAddClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(cityAdd);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});	
		
		aeroLAdd.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(AEROLADDPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Añadir una aerolinea");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				JFXTextField txtAeroLAdd = (JFXTextField) p.lookup("#txtAeroLAdd");
				JFXCheckBox chkAeroLAdd = (JFXCheckBox) p.lookup("#chkAeroLAdd");
				JFXButton poAeroLAddClose = (JFXButton) p.lookup("#poAeroLAddClose");
				JFXButton poAeroLAddGuardar = (JFXButton) p.lookup("#poAeroLAddGuardar");
				Label poAeroLAddLabel = (Label) p.lookup("#poAeroLAddLabel");
				poAeroLAddGuardar.setOnAction(x -> {
					if (!txtAeroLAdd.getText().equals("") ) {
						Aerolinea a = new Aerolinea(txtAeroLAdd.getText(), chkAeroLAdd.isSelected());
						aerolineasData.add(a);
						poAeroLAddLabel.setText("Se ha añadido la aerolinea.");
						parpadeoLabel(poAeroLAddLabel);
						actualizaDashboard();
					}
				});
				poAeroLAddClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(aeroLAdd);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});	
		
		aeroLEdit.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(AEROLEDITPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Editar aerolineas");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				JFXComboBox<Aerolinea> cboAeroLEdit = (JFXComboBox) p.lookup("#cboAeroLEdit");
				cboAeroLEdit.setEditable(false);
			
				JFXTextField txtAeroLEdit = (JFXTextField) p.lookup("#txtAeroLEdit");
				JFXCheckBox chkAeroLEdit = (JFXCheckBox) p.lookup("#chkAeroLEdit");
				cboAeroLEdit.getItems().addAll(aerolineasData.sorted());
				cboAeroLEdit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Aerolinea>() {
					@Override
					public void changed(ObservableValue<? extends Aerolinea> observable, Aerolinea oldValue,
							Aerolinea newValue) {
						if (newValue == null) {
							txtAeroLEdit.setText(null);
						} else {
							txtAeroLEdit.setText(newValue.getNombre());
							chkAeroLEdit.setSelected(newValue.isActivo());
						}
					
						
					}});
				
				JFXButton poAeroLEditClose = (JFXButton) p.lookup("#poAeroLEditClose");
				JFXButton poAeroLEditGuardar = (JFXButton) p.lookup("#poAeroLEditGuardar");
				Label poAeroLEditLabel = (Label) p.lookup("#poAeroLEditLabel");
				poAeroLEditGuardar.setOnAction(x -> {
					if (!txtAeroLEdit.getText().equals("") ) {
						Aerolinea a = (Aerolinea) cboAeroLEdit.getSelectionModel().getSelectedItem();
						a.setActivo(chkAeroLEdit.isSelected());
						a.setNombre(txtAeroLEdit.getText());
						cboAeroLEdit.getItems().clear();
						cboAeroLEdit.getItems().addAll(aerolineasData.sorted());
						poAeroLEditLabel.setText("Se ha modificado la aerolinea.");
						parpadeoLabel(poAeroLEditLabel);						
						actualizaDashboard();
					}
				});
				poAeroLEditClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(aeroLEdit);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		aeroPEdit.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(AEROPEDITPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Editar aeropuertos");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				po.setAutoFix(true);
				JFXComboBox<Aeropuerto> cboAeroPEdit = (JFXComboBox) p.lookup("#cboAeroPEdit");
				cboAeroPEdit.setEditable(false);
				JFXComboBox<Ciudad> cboAeroPEditC = (JFXComboBox) p.lookup("#cboAeroPEditC");
				cboAeroPEditC.setEditable(false);
				cboAeroPEditC.getItems().addAll(ciudadesData.sorted());
				JFXTextField txtAeroPEdit = (JFXTextField) p.lookup("#txtAeroPEdit");
				JFXCheckBox chkAeroPEdit = (JFXCheckBox) p.lookup("#chkAeroPEdit");
				cboAeroPEdit.getItems().addAll(aeropuertosData.sorted());
				cboAeroPEdit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Aeropuerto>() {
					@Override
					public void changed(ObservableValue<? extends Aeropuerto> observable, Aeropuerto oldValue,
							Aeropuerto newValue) {
						if (newValue == null) {
							txtAeroPEdit.setText(null);
						} else {
							txtAeroPEdit.setText(newValue.getNombre());
							chkAeroPEdit.setSelected(newValue.isActivo());
							cboAeroPEditC.getSelectionModel().select(newValue.getCiudad());
						}
					
						
					}});
				
				JFXButton poAeroPEditClose = (JFXButton) p.lookup("#poAeroPEditClose");
				JFXButton poAeroPEditGuardar = (JFXButton) p.lookup("#poAeroPEditGuardar");
				Label poAeroPEditLabel = (Label) p.lookup("#poAeroPEditLabel");
				poAeroPEditGuardar.setOnAction(x -> {
					if (!txtAeroPEdit.getText().equals("") ) {
						Aeropuerto a = (Aeropuerto) cboAeroPEdit.getSelectionModel().getSelectedItem();
						a.setActivo(chkAeroPEdit.isSelected());
						a.setNombre(txtAeroPEdit.getText());
						a.setCiudad(cboAeroPEditC.getSelectionModel().getSelectedItem());
						cboAeroPEdit.getItems().clear();
						cboAeroPEdit.getItems().addAll(aeropuertosData.sorted());
						poAeroPEditLabel.setText("Se ha modificado el aeropuerto.");
						parpadeoLabel(poAeroPEditLabel);						
						actualizaDashboard();
						rellenaRutas();
					}
				});
				poAeroPEditClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(aeroPEdit);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		aeroPAdd.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(AEROPADDPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Añadir un aeropuerto");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				JFXComboBox<Ciudad> cboAeroPAddC = (JFXComboBox) p.lookup("#cboAeroPAddC");
				cboAeroPAddC.setEditable(false);
				cboAeroPAddC.getItems().addAll(ciudadesData.sorted());
				JFXTextField txtAeroPAdd = (JFXTextField) p.lookup("#txtAeroPAdd");
				JFXCheckBox chkAeroPAdd = (JFXCheckBox) p.lookup("#chkAeroPAdd");
				JFXButton poAeroPAddClose = (JFXButton) p.lookup("#poAeroPAddClose");
				JFXButton poAeroPAddGuardar = (JFXButton) p.lookup("#poAeroPAddGuardar");
				Label poAeroPAddLabel = (Label) p.lookup("#poAeroPAddLabel");
				poAeroPAddGuardar.setOnAction(x -> {
					if (!txtAeroPAdd.getText().equals("") && cboAeroPAddC.getSelectionModel().getSelectedItem() != null ) {
						Aeropuerto a = new Aeropuerto(txtAeroPAdd.getText(), cboAeroPAddC.getSelectionModel().getSelectedItem(), chkAeroPAdd.isSelected());
						aeropuertosData.add(a);
						poAeroPAddLabel.setText("Se ha añadido el aeropuerto.");
						parpadeoLabel(poAeroPAddLabel);
						rellenaRutas();
						pintaVuelos();
					}
				});
				poAeroPAddClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(aeroPEdit);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		btnNuevoVuelo.setOnAction(e -> {
			Pane p;
			try {
				p = FXMLLoader.load(getClass().getResource(VUELOADDPO));
				PopOver po = new PopOver();
				po.setContentNode(p);
				po.setTitle("Añadir un vuelo");
				po.setAnimated(true);
				po.setCloseButtonEnabled(true);
				JFXComboBox<Ruta> cboVueloAddRuta = (JFXComboBox) p.lookup("#cboVueloAddRuta");
				cboVueloAddRuta.setEditable(false);
				cboVueloAddRuta.getItems().addAll(rutasData.sorted());
				
				JFXComboBox<Aerolinea> cboVueloAddAero = (JFXComboBox) p.lookup("#cboVueloAddAero");
				cboVueloAddAero.setEditable(false);
				cboVueloAddAero.getItems().addAll(aerolineasData.sorted());
				JFXTimePicker tpVueloAdd = (JFXTimePicker) p.lookup("#tpVueloAdd");
				tpVueloAdd.set24HourView(true);
				JFXCheckBox chkVueloAdd = (JFXCheckBox) p.lookup("#chkVueloAdd");
				JFXButton poVueloAddClose = (JFXButton) p.lookup("#poVueloAddClose");
				JFXButton poVueloAddGuardar = (JFXButton) p.lookup("#poVueloAddGuardar");
				Label poVueloAddLabel = (Label) p.lookup("#poVueloAddLabel");
				poVueloAddGuardar.setOnAction(x -> {
					if (cboVueloAddRuta.getSelectionModel().getSelectedItem() == null) {
						poVueloAddLabel.setText("Debe seleccionar una ruta.");
						parpadeoLabel(poVueloAddLabel);
					} else if (cboVueloAddAero.getSelectionModel().getSelectedItem() == null) {
						poVueloAddLabel.setText("Debe seleccionar una aerolinea.");
						parpadeoLabel(poVueloAddLabel);
					} else if (tpVueloAdd.getValue() == null) {
						poVueloAddLabel.setText("Debe seleccionar una hora de salida.");
						parpadeoLabel(poVueloAddLabel);
					} else {
						Vuelo v = new Vuelo(cboVueloAddRuta.getSelectionModel().getSelectedItem(), tpVueloAdd.getValue().getHour(), 
								tpVueloAdd.getValue().getMinute(), cboVueloAddAero.getSelectionModel().getSelectedItem(), 
								chkVueloAdd.isSelected());
						boolean vueloAddExiste = false;
						for (Vuelo vtemp : vuelosData) {
							if (vtemp.getRuta() == v.getRuta() && vtemp.getAerolinea() == v.getAerolinea() && vtemp.getHoraSalida() == v.getHoraSalida() && vtemp.getMinutoSalida() == v.getMinutoSalida()) {
								vueloAddExiste = true;
							}
						}
						if(vueloAddExiste) {
							poVueloAddLabel.setText("El vuelo indicado ya existe.");
						} else {
							vuelosData.add(v);
							poVueloAddLabel.setText("Se ha añadido el vuelo.");
							parpadeoLabel(poVueloAddLabel);
							pintaVuelos();
						}
					}
				});
				poVueloAddClose.setOnAction(x -> {
					po.hide();
					
				});
				po.show(btnNuevoVuelo);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});	
		
		btnNuevaRuta.setOnAction(e -> {
			if (cboRutaOrigen.getSelectionModel().getSelectedItem() != null && cboRutaDestino.getSelectionModel().getSelectedItem() != null) {
				Ruta r = new Ruta(cboRutaOrigen.getSelectionModel().getSelectedItem(), cboRutaDestino.getSelectionModel().getSelectedItem());
				rutasData.add(r);
				rellenaRutas();
			}
		});
		clearSearchRutas.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fieldSearchRutas.setText(null);
			}
		});
		actualizaDashboard();
		rellenaRutas();
		

	}
	
	private void rellenaRutas() {
		lstRutas.getItems().clear();
		cboRutaOrigen.getItems().clear();
		cboRutaDestino.getItems().clear();
		cboRutaOrigen.getItems().addAll(aeropuertosData.sorted());
		cboRutaDestino.getItems().addAll(aeropuertosData.sorted());
		
		cboRutaOrigen.setConverter(new StringConverter<Aeropuerto>() {

			@Override
			public String toString(Aeropuerto object) {
				if (object != null) {return object.getNombre();}
				else { return null; }
			}

			@Override
			public Aeropuerto fromString(String string) {
				try {
					return cboRutaOrigen.getSelectionModel().getSelectedItem();
				} catch (Exception e) {
					return null;
				}
				
				
			}
			
		});
		
		cboRutaDestino.setConverter(new StringConverter<Aeropuerto>() {

			@Override
			public String toString(Aeropuerto object) {
				if (object != null) {return object.getNombre();}
				else { return null; }
			}

			@Override
			public Aeropuerto fromString(String string) {
				try {
					return cboRutaDestino.getSelectionModel().getSelectedItem();
				} catch (Exception e) {
					return null;
				}
				
				
			}
			
		});
		
		JFXAutoCompletePopup<Aeropuerto> autoCompleteAeropuertosO = new JFXAutoCompletePopup<Aeropuerto>();
		autoCompleteAeropuertosO.getSuggestions().addAll(aeropuertosData.sorted());
		
		autoCompleteAeropuertosO.setSelectionHandler(e -> {
			cboRutaOrigen.getSelectionModel().select(e.getObject());
		});
		
		TextField editorO = cboRutaOrigen.getEditor();
		editorO.addEventHandler(KeyEvent.ANY, e -> {
			autoCompleteAeropuertosO.filter(item -> {
				Aeropuerto a = (Aeropuerto) item;
				if (a.getNombre().toLowerCase().contains(editorO.getText().toLowerCase())) { return true; } else {return false;}	
			});
			if (autoCompleteAeropuertosO.getFilteredSuggestions().isEmpty() || cboRutaOrigen.showingProperty().get()) {
				autoCompleteAeropuertosO.hide();
		    } 
		    else {
		    	autoCompleteAeropuertosO.show(editorO);
		    }
		});
		
		JFXAutoCompletePopup<Aeropuerto> autoCompleteAeropuertosD = new JFXAutoCompletePopup<>();
		autoCompleteAeropuertosD.getSuggestions().addAll(aeropuertosData.sorted());
		
		autoCompleteAeropuertosD.setSelectionHandler(e -> {
			cboRutaDestino.getSelectionModel().select(e.getObject());
		});
		
		TextField editorD = cboRutaDestino.getEditor();
		editorD.addEventHandler(KeyEvent.ANY, e -> {
			autoCompleteAeropuertosD.filter(item -> {
				Aeropuerto a = (Aeropuerto) item;
				if (a.getNombre().toLowerCase().contains(editorD.getText().toLowerCase())) { return true; } else {return false;}	
			});
			if (autoCompleteAeropuertosD.getFilteredSuggestions().isEmpty() || cboRutaDestino.showingProperty().get()) {
				autoCompleteAeropuertosD.hide();
		    } 
		    else {
		    	autoCompleteAeropuertosD.show(editorD);
		    }
		});
		
		lstRutas.setCellFactory(new Callback<ListView<Ruta>, ListCell<Ruta>>() {

			@Override
			public ListCell<Ruta> call(ListView<Ruta> param) {
				return new JFXListCell<Ruta>() {
					@SuppressWarnings({ "rawtypes", "static-access" })
					@Override
					public void updateItem(Ruta item,  boolean empty) {
						super.updateItem(item, empty);
						if(item!=null && !empty) {
							final GlyphIcon upIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
						            .glyph(FontAwesomeIcon.PLANE)
						            .size("20px")
						            .build();
							upIcon.setStyle("-fx-fill: black;");
							final GlyphIcon downIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
						            .glyph(FontAwesomeIcon.PLANE)
						            .size("20px")
						            .build();
							downIcon.setStyle("-fx-fill: black;");
							Rotate rotate = new Rotate(90, 10.0,-5.0);
							downIcon.getTransforms().add(rotate);
							HBox con = new HBox();
							HBox co = new HBox();
							JFXCheckBox lstRutasChk = new JFXCheckBox();
							lstRutasChk.setSelected(item.isActivo());
							lstRutasChk.setOnAction(e -> {
								item.setActivo(lstRutasChk.isSelected());
								tablaVuelos.refresh();
							});
							con.getChildren().add(lstRutasChk);
							co.setSpacing(10.0);
							co.getChildren().add(upIcon);
							co.getChildren().add(new Label(item.getOrigen().getNombre()));
							co.setAlignment(Pos.CENTER_LEFT);
							HBox cd = new HBox();
							cd.setSpacing(10.0);
							cd.getChildren().add(new Label(item.getDestino().getNombre()));
							cd.getChildren().add(downIcon);
							cd.setAlignment(Pos.CENTER_RIGHT);
							con.getChildren().addAll(co,cd);
							con.setHgrow(co, Priority.ALWAYS);
							con.setHgrow(cd, Priority.ALWAYS);
							setGraphic(con);
							setText(null);
						}
					}
				};
			}
			
		});
		FilteredList<Ruta> filtroRutas = new FilteredList<>(rutasData.sorted(), e -> true);
		fieldSearchRutas.setOnKeyReleased(e -> {
			fieldSearchRutas.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filtroRutas.setPredicate((Predicate<? super Ruta>) ruta -> {
					if(newValue == null || newValue.isEmpty()) {
						return true;
					}
					String lower =  newValue.toLowerCase();
					if(ruta.getOrigen().getNombre().toLowerCase().contains(lower)) {
						return true;
					} else if (ruta.getDestino().getNombre().toLowerCase().contains(lower)) {
						return true;
					}  else {
					return false;
					}
					
				});
			});
			SortedList<Ruta> sortedData = new SortedList<>(filtroRutas);
			lstRutas.setItems(sortedData.sorted());
		});
		
		clearSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fieldSearch.setText(null);
				labelMatches.setText(vuelosData.size() + " vuelos encontrados.");
			}
		});
		lstRutas.getItems().addAll(rutasData.sorted());
		
	}


	private void actualizaDashboard() {
		
		int ciudadesActivas = 0;
		int ciudadesTotales = 0;
		for (Ciudad c : ciudadesData) {
			if (c.isActivo()) { ciudadesActivas++; }
			ciudadesTotales++;
		}
		
		lblCiudadesDisponibles.setText(Integer.toString(ciudadesTotales));
		lblCiudadesActivas.setText(Integer.toString(ciudadesActivas));
		
		
		int aerolineasActivas = 0;
		int aerolineasTotales = 0;
		for (Aerolinea a : aerolineasData) {
			if (a.isActivo()) { aerolineasActivas++; }
			aerolineasTotales++;
		}
		
		lblAerolineasDisponibles.setText(Integer.toString(aerolineasTotales));
		lblAerolineasActivas.setText(Integer.toString(aerolineasActivas));
		
		
		int aeropuertosActivos = 0;
		int aeropuertosTotales = 0;
		for (Aeropuerto a : aeropuertosData) {
			if (a.isActivo()) { aeropuertosActivos++; }
			aeropuertosTotales++;
		}
		
		lblAeropuertosDisponibles.setText(Integer.toString(aeropuertosTotales));
		lblAeropuertosActivos.setText(Integer.toString(aeropuertosActivos));
		
		ObservableList<PieChart.Data> aerolineasChart = FXCollections.observableArrayList(
                new PieChart.Data("Activos", aerolineasActivas ),
                new PieChart.Data("Inactivos", aerolineasTotales - aerolineasActivas));
		Donut aeroLChart = new Donut(aerolineasChart);
		//aeroChart.setPrefSize(40, 40);
		aeroLChart.setMinSize(160, 160);
		aeroLChart.setMaxSize(160, 160);
		aeroLChart.setLegendVisible(false);
		aeroLChart.setLabelsVisible(false);
		this.aeroLChart.getChildren().add(aeroLChart);
		//Grafico aeropuertos
		ObservableList<PieChart.Data> aeropuertosChart = FXCollections.observableArrayList(
                new PieChart.Data("Activos", aeropuertosActivos ),
                new PieChart.Data("Inactivos", aeropuertosTotales - aeropuertosActivos));
		Donut aeroPChart = new Donut(aeropuertosChart);
		//aeroChart.setPrefSize(40, 40);
		aeroPChart.setMinSize(160, 160);
		aeroPChart.setMaxSize(160, 160);
		aeroPChart.setLegendVisible(false);
		aeroPChart.setLabelsVisible(false);
		this.aeroPChart.getChildren().add(aeroPChart);
		//Grafico ciudades
		ObservableList<PieChart.Data> ciudadesSChart = FXCollections.observableArrayList(
                new PieChart.Data("Activos", ciudadesActivas ),
                new PieChart.Data("Inactivos", ciudadesTotales - ciudadesActivas));
		Donut cityChart = new Donut(ciudadesSChart);
		//aeroChart.setPrefSize(40, 40);
		cityChart.setMinSize(160, 160);
		cityChart.setMaxSize(160, 160);
		cityChart.setLegendVisible(false);
		cityChart.setLabelsVisible(false);
		this.cityChart.getChildren().add(cityChart);
		
		pintaVuelos();

	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void pintaVuelos() {
		tablaVuelos.setEditable(false);
		tablaVuelos.getColumns().clear();
		tablaVuelos.getSelectionModel().cellSelectionEnabledProperty().set(false);
		
		//origen del vuelo
		TableColumn tV1 = new TableColumn("Origen");
		
		tV1.setCellValueFactory(new PropertyValueFactory<Vuelo, Ruta>("ruta"));
		tV1.setCellFactory(col -> new TableCell<Vuelo, Ruta>() {
			@Override
			protected void updateItem(Ruta r, boolean empty) {
				super.updateItem(r, empty);
				if (empty) {
					setText(null);
				} else {
					setText(r.getOrigen().getNombre() + " ("+ r.getOrigen().getNombreCiudad() + ")");
				}
			}
		});
		tV1.prefWidthProperty().set(150.0);
		
		
		//destino del vuelo
		TableColumn tV2 = new TableColumn("Destino");
		
		tV2.setCellValueFactory(new PropertyValueFactory<Vuelo, Ruta>("ruta"));
		tV2.setCellFactory(col -> new TableCell<Vuelo, Ruta>() {
			@Override
			protected void updateItem(Ruta r, boolean empty) {
				super.updateItem(r, empty);
				if (empty) {
					setText(null);
				} else {
					setText(r.getDestino().getNombre() + " ("+ r.getDestino().getNombreCiudad() + ")");
				}
			}
		});
		tV2.prefWidthProperty().set(150.0);
		
		// hora de salida
		TableColumn tV3 = new TableColumn("Salida");
		tV3.setCellValueFactory(new PropertyValueFactory<Vuelo, Ruta>("salidaString"));
		tV3.prefWidthProperty().set(80.0);
		
		//Aerolinea del vuelo
		TableColumn tV4 = new TableColumn("Aerolinea");
		
		tV4.setCellValueFactory(new PropertyValueFactory<Vuelo, Aerolinea>("aerolinea"));
		tV4.setCellFactory(col -> new TableCell<Vuelo, Aerolinea>() {
			@Override
			protected void updateItem(Aerolinea a, boolean empty) {
				super.updateItem(a, empty);
				if (empty) {
					setText(null);
				} else {
					setText(a.getNombre());
				}
			}
		});
		tV4.prefWidthProperty().set(150.0);
		
		//estado del vuelo
		TableColumn tV5 = new TableColumn("Estado");
		
		tV5.setCellValueFactory(new PropertyValueFactory<Vuelo, Boolean>("activo"));
		tV5.setCellFactory(col -> new TableCell<Vuelo, Boolean>() {
			@Override
			protected void updateItem(Boolean a, boolean empty) {
				super.updateItem(a, empty);
				if (empty) {
					setText(null);
				} else {
					if(a) {
						setText("Activo");
						setStyle("-fx-text-fill: black");
					} else {
						setText("Inactivo");
						setStyle("-fx-text-fill: red");
					}
					
				}
			}
		});
		tV5.prefWidthProperty().set(80.0);
		
		//Incidencias
		TableColumn tV6 = new TableColumn();
		final GlyphIcon xIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
	            .glyph(FontAwesomeIcon.INFO_CIRCLE)
	            .size("1em")
	            .build();
		xIcon.setStyle("-fx-fill: white");
		tV6.setGraphic(xIcon);
		tV6.setCellValueFactory(new PropertyValueFactory<Vuelo, Boolean>("activo"));
		tV6.setCellFactory(col -> new TableCell<Vuelo, Boolean>() {
			@Override
			protected void updateItem(Boolean a, boolean empty) {
				super.updateItem(a, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Vuelo v = getTableView().getItems().get(getIndex());
					final GlyphIcon wIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
				            .glyph(FontAwesomeIcon.INFO_CIRCLE)
				            .size("1em")
				            .build();
					wIcon.setStyle("-fx-fill: orange");
					boolean info = false;
					String infotext = "";
					if(!v.getAerolinea().isActivo()) {
						info = true;
						infotext += "Esta aerolinea no está activa.\n";
					}
					if (!v.getRuta().getOrigen().isActivo()) {
						info = true;
						infotext += "El aeropuerto de origen no está activo.\n";
					} 
					if (!v.getRuta().getDestino().isActivo()) {
						info = true;
						infotext += "El aeropuerto de destino no está activo.\n";
					} 
					if (!v.getRuta().getOrigen().getCiudad().isActivo()) {
						info = true;
						infotext += "La ciudad del aeropuerto de origen no está activa.\n";
					} 
					if (!v.getRuta().getDestino().getCiudad().isActivo()) {
						info = true;
						infotext += "La ciudad del aeropuerto de destino no está activa.\n";
					} 
					if (!v.getRuta().isActivo()) {
						info = true;
						infotext += "Esta ruta no está activa.\n";
					}
					if (info) {
						setGraphic(wIcon);
						setTooltip(new Tooltip(infotext));
					} else {
						setGraphic(null);
						setTooltip(null);
					}
					
				}
			}
		});
		tV6.prefWidthProperty().set(20.0);
		
		//Botones
		TableColumn tV7 = new TableColumn("");
		
		tV7.setCellValueFactory(new PropertyValueFactory<Vuelo, Boolean>("activo"));
		tV7.setCellFactory(col -> new TableCell<Vuelo, Boolean>() {
			JFXButton bEdit = new JFXButton();
			JFXButton bEliminar = new JFXButton();
			HBox hb = new HBox(bEdit, bEliminar);
	
			@Override
			protected void updateItem(Boolean a, boolean empty) {
				super.updateItem(a, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					final GlyphIcon aIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
				            .glyph(FontAwesomeIcon.EDIT)
				            .size("1em")
				            .build();
					aIcon.setStyle("-fx-fill: #ee8a10");
					bEdit.setText("Editar");
					bEdit.setStyle("-fx-background-color: transparent;");
					bEdit.setTextFill(Color.web("#ee8a10"));
					bEdit.setGraphic(aIcon);
					
					bEdit.setOnAction(e -> {
						Pane p;
						try {
							p = FXMLLoader.load(getClass().getResource(VUELOEDITPO));
							PopOver po = new PopOver();
							po.setContentNode(p);
							po.setTitle("Editar vuelo");
							po.setAnimated(true);
							po.setCloseButtonEnabled(true);
							JFXComboBox<Ruta> cboVueloEditRuta = (JFXComboBox) p.lookup("#cboVueloEditRuta");
							cboVueloEditRuta.setEditable(false);
							cboVueloEditRuta.getItems().addAll(rutasData.sorted());
							cboVueloEditRuta.getSelectionModel().select(getTableView().getItems().get(getIndex()).getRuta());
							
							JFXComboBox<Aerolinea> cboVueloEditAero = (JFXComboBox) p.lookup("#cboVueloEditAero");
							cboVueloEditAero.setEditable(false);
							cboVueloEditAero.getItems().addAll(aerolineasData.sorted());
							cboVueloEditAero.getSelectionModel().select(getTableView().getItems().get(getIndex()).getAerolinea());
							
							JFXTimePicker tpVueloEdit = (JFXTimePicker) p.lookup("#tpVueloEdit");
							tpVueloEdit.setValue(LocalTime.of(getTableView().getItems().get(getIndex()).getHoraSalida(), getTableView().getItems().get(getIndex()).getMinutoSalida()));
							tpVueloEdit.set24HourView(true);
							JFXCheckBox chkVueloEdit = (JFXCheckBox) p.lookup("#chkVueloEdit");
							chkVueloEdit.setSelected(getTableView().getItems().get(getIndex()).isActivo());
							
							JFXButton poVueloEditClose = (JFXButton) p.lookup("#poVueloEditClose");
							JFXButton poVueloEditGuardar = (JFXButton) p.lookup("#poVueloEditGuardar");
							Label poVueloEditLabel = (Label) p.lookup("#poVueloEditLabel");
							poVueloEditGuardar.setOnAction(x -> {
								if (cboVueloEditRuta.getSelectionModel().getSelectedItem() == null) {
									poVueloEditLabel.setText("Debe seleccionar una ruta.");
									parpadeoLabel(poVueloEditLabel);
								} else if (cboVueloEditAero.getSelectionModel().getSelectedItem() == null) {
									poVueloEditLabel.setText("Debe seleccionar una aerolinea.");
									parpadeoLabel(poVueloEditLabel);
								} else if (tpVueloEdit.getValue() == null) {
									poVueloEditLabel.setText("Debe seleccionar una hora de salida.");
									parpadeoLabel(poVueloEditLabel);
								} else {
									Vuelo v = getTableView().getItems().get(getIndex());
									boolean vueloEditExiste = false;
									for (Vuelo vtemp : vuelosData) {
										if (vtemp != v) {
											if (vtemp.getRuta() == cboVueloEditRuta.getSelectionModel().getSelectedItem()
													&& vtemp.getAerolinea() == cboVueloEditAero.getSelectionModel().getSelectedItem()
													&& vtemp.getHoraSalida() == tpVueloEdit.getValue().getHour()
													&& vtemp.getMinutoSalida() == tpVueloEdit.getValue().getMinute()) {
												vueloEditExiste = true;
											}
										}
									}
									if(vueloEditExiste) {
										poVueloEditLabel.setText("Ya existe un vuelo con esos parámetros.");
									} else {
										v.setActivo(chkVueloEdit.isSelected());
										v.setRuta(cboVueloEditRuta.getSelectionModel().getSelectedItem());
										v.setAerolinea(cboVueloEditAero.getSelectionModel().getSelectedItem());
										v.setHoraSalida(tpVueloEdit.getValue().getHour());
										v.setMinutoSalida(tpVueloEdit.getValue().getMinute());
										tablaVuelos.refresh();
										tablaVuelos.getSelectionModel().select(v);
										po.hide();
									}
								}
							});
							poVueloEditClose.setOnAction(x -> {
								po.hide();
								
							});
							po.show(bEdit);
							
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
					});	
					
					final GlyphIcon bIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
				            .glyph(FontAwesomeIcon.REMOVE)
				            .size("1em")
				            .build();
					bIcon.setStyle("-fx-fill: #bf1515");
					bEliminar.setText("Editar");
					bEliminar.setStyle("-fx-background-color: transparent;");
					bEliminar.setTextFill(Color.web("#bf1515"));
					
					bEliminar.setGraphic(bIcon);
					bEliminar.setText("Eliminar");
					bEliminar.setOnAction(e -> {
						final Stage dialog = new Stage(StageStyle.TRANSPARENT);
					    dialog.initModality(Modality.WINDOW_MODAL);
					   
					    dialog.initOwner(stage);
					    stage.getScene().getRoot().setEffect(new GaussianBlur());
				        Label l = new Label();
				        
				        l.setText("¿Está seguro de querer eliminar este vuelo?");
				        JFXButton by = new JFXButton("Sí");
				        JFXButton bn = new JFXButton("No");
				        final GlyphIcon bnIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
					            .glyph(FontAwesomeIcon.REMOVE)
					            .size("20px")
					            .build();
						bnIcon.setStyle("-fx-fill: #bf1515");
						bn.setTextFill(Color.web("#bf1515"));
						bn.setStyle("-fx-background-color: transparent;");
				        bn.setGraphic(bnIcon);
				        final GlyphIcon byIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
					            .glyph(FontAwesomeIcon.CHECK)
					            .size("20px")
					            .build();
						byIcon.setStyle("-fx-fill: green");
						by.setTextFill(Color.GREEN);
						by.setStyle("-fx-background-color: transparent;");
				        by.setGraphic(byIcon);
				        
				        HBox xd = new HBox(by,bn);
				        xd.alignmentProperty().set(Pos.CENTER);
				        VBox vd = new VBox(l,xd);
				        vd.getStyleClass().add("modal-dialog");
				        Scene ds = new Scene(vd, 500,200);
				        ds.getStylesheets().add(getClass().getResource("/assets/app.css").toExternalForm());
				        dialog.setScene(ds);
				       
				        dialog.show();
				        ds.getRoot().requestFocus();
					    by.setOnAction(new EventHandler<ActionEvent>() {
				            @Override public void handle(ActionEvent actionEvent) {
				            	Vuelo v = getTableView().getItems().get(getIndex());
					            vuelosData.remove(v);
					            stage.getScene().getRoot().setEffect(null);
					            dialog.close();
					            pintaVuelos();
				            }
					     });
					     bn.setOnAction(new EventHandler<ActionEvent>() {
					            @Override public void handle(ActionEvent actionEvent) {
					              stage.getScene().getRoot().setEffect(null);
					              dialog.close();
					            }
					     });
					});
					
					hb.setAlignment(Pos.CENTER);
					hb.setPadding(new Insets(0,10,0,10));
					hb.setSpacing(10);
					setGraphic(hb);
				}
			}
		});
		tV7.prefWidthProperty().set(170.0);
		
		
		FilteredList<Vuelo> filtroVuelos = new FilteredList<>(vuelosData.sorted(), e -> true);
		tablaVuelos.getColumns().addAll(tV1, tV2, tV3, tV4, tV5, tV6, tV7);
		SortedList<Vuelo> sortedData = new SortedList<>(filtroVuelos);
		sortedData.comparatorProperty().bind(tablaVuelos.comparatorProperty());
		labelMatches.setText(sortedData.size() + " vuelos encontrados.");
		tablaVuelos.setItems(sortedData);
		
		
		fieldSearch.setOnKeyReleased(e -> {
			fieldSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filtroVuelos.setPredicate((Predicate<? super Vuelo>) vuelo -> {
					if(newValue == null || newValue.isEmpty()) {
						return true;
					}
					String lower =  newValue.toLowerCase();
					if(vuelo.getAerolinea().getNombre().toLowerCase().contains(lower)) {
						return true;
					} else if (vuelo.getRuta().getOrigen().getNombre().toLowerCase().contains(lower)) {
						return true;
					} else if (vuelo.getRuta().getDestino().getNombre().toLowerCase().contains(lower)) {
						return true;
					} else if (vuelo.getRuta().getOrigen().getNombreCiudad().toLowerCase().contains(lower)) {
						return true;
					} else if (vuelo.getRuta().getDestino().getNombreCiudad().toLowerCase().contains(lower)) {
						return  true;
					} else if (vuelo.getSalidaString().contains(lower)) {
						return  true;
					} else {
					return false;
					}
					
				});
			});
			//SortedList<Vuelo> sortedData = new SortedList<>(filtroVuelos);
			//sortedData.comparatorProperty().bind(tablaVuelos.comparatorProperty());
			labelMatches.setText(sortedData.size() + " vuelos encontrados.");
			//tablaVuelos.setItems(sortedData);
		});
		
		clearSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fieldSearch.setText(null);
				labelMatches.setText(vuelosData.size() + " vuelos encontrados.");
			}
		});
	}


	private void parpadeoLabel(Label lbl) {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), lbl);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.setCycleCount(1);
		fadeTransition.play();
	}


	public void show(Stage stage) {
		this.stage = stage;
		//stage.initStyle(StageStyle.DECORATED.UNDECORATED);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        scene.getRoot().requestFocus();
	}

	
	private void rellenaDatosSample() {
		ciudadesData.add(new Ciudad("Madrid"));
		ciudadesData.add(new Ciudad("Barcelona"));
		ciudadesData.add(new Ciudad("Nueva York"));
		ciudadesData.add(new Ciudad("Paris"));
		ciudadesData.add(new Ciudad("Los Angeles"));
		ciudadesData.add(new Ciudad("Estocolmo"));
		ciudadesData.add(new Ciudad("Melbourne"));
		ciudadesData.add(new Ciudad("Oslo", false));
		ciudadesData.add(new Ciudad("Frankfurt", false));
		ciudadesData.add(new Ciudad("Alicante", false));
		
		aeropuertosData.add(new Aeropuerto("Barajas", ciudadesData.get(0)));
		aeropuertosData.add(new Aeropuerto("El Prat", ciudadesData.get(1)));
		aeropuertosData.add(new Aeropuerto("JFK", ciudadesData.get(2)));
		aeropuertosData.add(new Aeropuerto("LaGuardia", ciudadesData.get(2)));
		aeropuertosData.add(new Aeropuerto("Charles de Gaulle", ciudadesData.get(3)));
		aeropuertosData.add(new Aeropuerto("Orly", ciudadesData.get(3)));
		aeropuertosData.add(new Aeropuerto("LAX", ciudadesData.get(4)));
		aeropuertosData.add(new Aeropuerto("Long Beach", ciudadesData.get(4)));
		aeropuertosData.add(new Aeropuerto("Arlanda Airport", ciudadesData.get(5)));
		aeropuertosData.add(new Aeropuerto("Bromma", ciudadesData.get(5)));
		aeropuertosData.add(new Aeropuerto("Melbourne Airport", ciudadesData.get(6)));
		aeropuertosData.add(new Aeropuerto("Moorabbin", ciudadesData.get(6)));
		aeropuertosData.add(new Aeropuerto("Frankfurt Airport", ciudadesData.get(7), false));
		aeropuertosData.add(new Aeropuerto("Alicante-Elche", ciudadesData.get(8), false));
		
		aerolineasData.add(new Aerolinea("Iberia"));
		aerolineasData.add(new Aerolinea("British Airways"));
		aerolineasData.add(new Aerolinea("Air Asia", false));
		aerolineasData.add(new Aerolinea("Air Italy", false));
		aerolineasData.add(new Aerolinea("Austrian Airlines"));
		aerolineasData.add(new Aerolinea("Corsair", false));
		aerolineasData.add(new Aerolinea("Finnair"));
		aerolineasData.add(new Aerolinea("JetStar Asia", false));
		aerolineasData.add(new Aerolinea("Loganair", false));
		aerolineasData.add(new Aerolinea("Nord Wind"));
		aerolineasData.add(new Aerolinea("Qatar Airways"));
		aerolineasData.add(new Aerolinea("Easyjet"));
		
		rutasData.add(new Ruta(aeropuertosData.get(0), aeropuertosData.get(1)));
		rutasData.add(new Ruta(aeropuertosData.get(0), aeropuertosData.get(2)));
		rutasData.add(new Ruta(aeropuertosData.get(2), aeropuertosData.get(0)));
		rutasData.add(new Ruta(aeropuertosData.get(2), aeropuertosData.get(4)));
		rutasData.add(new Ruta(aeropuertosData.get(2), aeropuertosData.get(6)));
		
		vuelosData.add(new Vuelo(rutasData.get(0), 10, 30, aerolineasData.get(1)));
		vuelosData.add(new Vuelo(rutasData.get(1), 11, 30, aerolineasData.get(0)));
		vuelosData.add(new Vuelo(rutasData.get(2), 12, 30, aerolineasData.get(0)));
		
		vuelosData.add(new Vuelo(rutasData.get(0), 3, 45, aerolineasData.get(0)));
		vuelosData.add(new Vuelo(rutasData.get(1), 9, 00, aerolineasData.get(2)));
		vuelosData.add(new Vuelo(rutasData.get(2), 15, 30, aerolineasData.get(3)));
		vuelosData.add(new Vuelo(rutasData.get(0), 21, 00, aerolineasData.get(4)));
		vuelosData.add(new Vuelo(rutasData.get(1), 22, 00, aerolineasData.get(5)));
		vuelosData.add(new Vuelo(rutasData.get(2), 23, 30, aerolineasData.get(1)));
	}
}
