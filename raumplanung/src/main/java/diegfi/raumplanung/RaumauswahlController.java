package diegfi.raumplanung;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class RaumauswahlController {

	@FXML
    private DatePicker datePickerVon;

    @FXML
    private DatePicker datePickerBis;
    
    @FXML
    private Button buttonSuchen;
    
    @FXML
    private void initialize() 
    {
       	System.out.println("RaumauswahlController.initialize()");
    }

}
