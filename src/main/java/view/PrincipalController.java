package view;

import java.net.URL;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Moeda;

public class PrincipalController implements Initializable {
        private List<Moeda> moedas = new ArrayList<>();
 private boolean ehDe = true;
    private double fator = 1;

    private final char separadorDecimal
            = new DecimalFormatSymbols(Locale.getDefault(Locale.Category.FORMAT)).getDecimalSeparator();
    private final NumberFormat nf
            = NumberFormat.getInstance(Locale.getDefault());
    @FXML
    private ComboBox cmbMoedas;

    @FXML
    private TextField txtFldValorDe;

    @FXML
    private TextField txtFldValorPara;

    @FXML
    private Label lblUnDe;

    @FXML
    private Label lblUnPara;

    @FXML
    private ImageView imgSetas;

    @FXML
    private void btnFecharClick(Event event) {
        System.exit(0);
    }

    private String convertido(double val) {
        double v;
        if (ehDe) {
            v = val * fator;
        } else {
            v = val / fator;
        }
        return nf.format(v);
    }

    private void atualizaTxtFld() {
        if (ehDe) {
            try {
                txtFldValorPara.setText(
                        convertido(nf.parse(txtFldValorDe.getText())
                                .doubleValue()));
            } catch (ParseException ex) {
                txtFldValorPara.setText("");
            }
        } else {
            try {
                txtFldValorDe.setText(
                        convertido(nf.parse(txtFldValorPara.getText())
                                .doubleValue()));
            } catch (ParseException ex) {
                txtFldValorDe.setText("");
            }
        }
    }

    private final ChangeListener<? super String> listenerDe
            = (observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\" + separadorDecimal + "\\d*)?")
                && !newValue.isEmpty()) {
                    txtFldValorDe.setText(oldValue);
                } else {
                    atualizaTxtFld();
                }
            };

    private final ChangeListener<? super String> listenerPara
            = (observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\" + separadorDecimal + "\\d*)?")
                && !newValue.isEmpty()) {
                    txtFldValorPara.setText(oldValue);
                } else {
                    atualizaTxtFld();
                }
            };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        moedas.add(new Moeda( "Euro", "€","Real", "R$", 4.09));
        moedas.add(new Moeda("Dolar", "U$","Real", "R$",  3.32));
        moedas.add(new Moeda("Real", "R$", "Iene", "¥", 31.59));
        moedas.add(new Moeda("Libra esterlina", "£","Real", "R$", 4.68));
        moedas.add(new Moeda("Real", "R$", "Peso argentino", "$", 6.10));
        moedas.add(new Moeda("Real", "R$", "Peso chileno", "$", 184));
        moedas.add(new Moeda("Real", "R$", "Peso mexicano", "$", 5.63));
        moedas.add(new Moeda("Real", "R$", "Peso uruguaio", "$", 8.59));
        
                cmbMoedas.setItems(FXCollections.observableList(moedas));
                
//Listener COmbo
        cmbMoedas.valueProperty().addListener(
                new ChangeListener<Moeda>() {

            @Override
            public void changed(ObservableValue<? extends Moeda> observable, Moeda oldValue, Moeda newValue) {
                lblUnDe.setText(newValue.getSiglaDe());
                lblUnPara.setText(newValue.getSiglaPara());
                fator = newValue.getFator();
                atualizaTxtFld();
            }
        });
        
//Listener txtFld-COmbo
        txtFldValorDe.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        ehDe = true;
                        imgSetas.setImage(new Image("/image/next.png"));
                        txtFldValorDe.textProperty().addListener(listenerDe);
                    } else {
                        txtFldValorDe.textProperty().removeListener(listenerDe);

                    }

                });
        txtFldValorPara.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        ehDe = false;
                        imgSetas.setImage(new Image("/image/back.png"));
                        txtFldValorPara.textProperty().addListener(listenerPara);
                    } else {
                        txtFldValorPara.textProperty().removeListener(listenerPara);

                    }

                });
        
    }    
};

