package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import nz.ac.auckland.se206.App;
import java.io.IOException;

public class LockController {

  @FXML
  private Spinner<Integer> spinner1;

  @FXML
  private Spinner<Integer> spinner2;

  @FXML
  private Spinner<Integer> spinner3;

  @FXML
  public void initialize() {
    SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);                                                                                                       // value
    SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
    SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);

    spinner1.setValueFactory(valueFactory1);
    spinner2.setValueFactory(valueFactory2);
    spinner3.setValueFactory(valueFactory3);
  }

  @FXML
  private void checkLock() throws IOException {
    int value1 = spinner1.getValue();
    int value2 = spinner2.getValue();
    int value3 = spinner3.getValue();

    if (value1 == 2 && value2 == 0 && value3 == 6) {
      System.out.println("Lock opened!");
      App.setRoot("crime");
    }
  }
}
