/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<String> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.cmbMese.getValue() == null || this.txtMinuti.getText() == null) {
    		this.txtResult.setText("Prima indicare il mese e i minuti minimi di gioco");
    		return;
    	}
    	
    	List<Adiacenza> res = model.getMassimi();
    	if(res != null) {
    		this.txtResult.appendText("Connessioni massime: \n");
    		for(Adiacenza a: res) {
    			this.txtResult.appendText(a + "\n");
    		}
    	}else {
    		this.txtResult.appendText("Prima crea il grafo");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String mese = this.cmbMese.getValue();
    	if(mese == null) {
    		this.txtResult.setText("Devi selezionare un mese");
    		return;
    	}
    	
    	int month = getMese(mese);
    	
    	int min;
    	try{
    		min = Integer.parseInt(this.txtMinuti.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Indicare il minimo di minuti giocati che deve essere un numero positivo");
    		return;
    	}
    	
    	this.model.creaGrafo(month, min);
    	this.txtResult.appendText("GRAFO CREATO\n");
    	this.txtResult.appendText("#vertici: " + this.model.getNVertici() + "\n");
    	this.txtResult.appendText("#archi: " + this.model.getNArchi() + "\n");
    	
    	if(model.getVertici() != null) {
    		this.cmbM1.getItems().addAll(model.getVertici());
    		this.cmbM2.getItems().addAll(model.getVertici());
    	}
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbMese.getItems().addAll("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");
  
    }
    
    public int getMese(String mese) {
    	
    	switch(mese) {
    	case "Gennaio":
    		return 1;
    		
    	case "Febbraio":
    		return 2;
    	
    	case "Marzo":
    		return 3;
    	
    	case "Aprile":
    		return 4;
    		
    	case "Maggio":
    		return 5;
    		
    	case "Giugno":
    		return 6;
    		
    	case "Luglio":
    		return 7;
    		
    	case "Agosto":
    		return 8;
    		
    	case "Settembre":
    		return 9;
    		
    	case "Ottobre":
    		return 10;
    		
    	case "Novembre":
    		return 11;
    		
    	case "Dicembre":
    		return 12;
    		
    	default: return 0;
    	}
    	
    }
}
