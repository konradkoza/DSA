package p.lodz.dsa;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private final DSA dsa = new DSA();

    private byte[] message;

    private BigInteger[] signature;

    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField fileNameSignatureText;

    @FXML
    private TextField fileNameText;

    @FXML
    private TextField fileNameToSaveSignatureText;

    @FXML
    private TextField fileNameToSaveText;

    @FXML
    private RadioButton fileRadioBox;

    @FXML
    private Button generateKeyButton;

    @FXML
    private TextField keyGText;

    @FXML
    private TextField keyPText;

    @FXML
    private TextField keyQText;

    @FXML
    private TextField keyXText;

    @FXML
    private TextField keyYText;

    @FXML
    private Button readPrivateKeyFromFileButton;

    @FXML
    private Button readPublicKeyFromFileButton;

    @FXML
    private Button readSignatureTextFileButton;

    @FXML
    private Button readTextFileButton;

    @FXML
    private Button saveKeyToFileButton;

    @FXML
    private Button saveSignatureTextToFIleButton;

    @FXML
    private Button saveTextToFIleButton;

    @FXML
    private Button signButton;

    @FXML
    private RadioButton textRadioBox;

    @FXML
    private TextArea textSignature;

    @FXML
    private TextArea textToSign;

    @FXML
    private Button verifyButton;

    @FXML
    void handleRadioBoxFile(ActionEvent event) {
        if(fileRadioBox.isSelected()){
            textRadioBox.selectedProperty().set(false);
        }
    }

    @FXML
    void handleRadioBoxText(ActionEvent event) {
        if(textRadioBox.isSelected()){
            fileRadioBox.selectedProperty().set(false);
        }
    }

    @FXML
    void onActionGenerateKey(ActionEvent event) {
        dsa.generateKey();
        keyQText.setText(dsa.getQ().toString(16));
        keyGText.setText(dsa.getG().toString(16));
        keyYText.setText(dsa.getY().toString(16));
        keyXText.setText(dsa.getX().toString(16));
        keyPText.setText(dsa.getP().toString(16));

    }

    @FXML
    void onActionReadPrivateKeyFromFile(ActionEvent event) {
        try {
            fileChooser.getExtensionFilters().setAll(
                    new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
            fileChooser.setTitle("Wybierz klucz prywatny");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {

                try {
                    keyXText.setText(new String(Helper.readFile(file.getAbsolutePath())));
                } catch (FileException e) {
                    AlertWindow.messageWindow("Błąd pliku", "Błąd podczas wczytywania pliku", Alert.AlertType.WARNING);
                }
            }

        } catch(NumberFormatException  e){
            AlertWindow.messageWindow("Błąd klucza", "Błąd przy wczytywaniu klucza", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onActionReadPublicKeyFromFile(ActionEvent event) {
        try {
            fileChooser.setTitle("Wybierz dane publiczne");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                String tab[] = new String[0];
                try {
                    tab = (new String(Helper.readFile(file.getAbsolutePath()))).split("\n");
                    keyQText.setText(tab[0]);
                    keyGText.setText(tab[1]);
                    keyYText.setText(tab[2]);
                    keyPText.setText(tab[3]);
                } catch (FileException e) {
                    throw new RuntimeException(e);
                }

                dsa.setQ(new BigInteger(keyQText.getText(), 16));
                dsa.setG(new BigInteger(keyGText.getText(), 16));
                dsa.setY(new BigInteger(keyYText.getText(), 16));
                dsa.setP(new BigInteger(keyPText.getText(), 16));
            }
        } catch(NumberFormatException  e){
            AlertWindow.messageWindow("Błąd klucza", "Błąd przy wczytywaniu klucza", Alert.AlertType.WARNING);
        }
    }


    @FXML
    void onActionReadSignatureTextFileButton(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            try {
                fileNameSignatureText.setText(file.getAbsolutePath());
                signature=Helper.readBigIntArrayFromFile(file.getAbsolutePath());
                String tekst = signature[0].toString(16)
                        + "\n"
                        + signature[1].toString(16);
                textSignature.setText(tekst);

            } catch (Exception e) {
                e.printStackTrace();
                AlertWindow.messageWindow("Błąd pliku", "Błąd podczas wczytywania pliku", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    void onActionReadTextFileButton(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            fileNameText.setText(file.getAbsolutePath());
            try {
                message = Helper.readFile(file.getAbsolutePath());
                textToSign.setText(new String(message));
            } catch (FileException e) {
                AlertWindow.messageWindow("Błąd pliku", "Błąd podczas wczytywania pliku", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    void onActionSaveKeyToFile(ActionEvent event) {
        try{

            fileChooser.getExtensionFilters().setAll(
                    new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
            fileChooser.setTitle("Zapisz klucz klucz prywatny");
            File file = fileChooser.showSaveDialog(null);
            if(file != null) {


                try {
                    Helper.saveFile(keyXText.getText().getBytes(), file.getAbsolutePath());
                }catch(Exception e) {
                    AlertWindow.messageWindow("Błąd pliku", "Błąd podczas zapisywania klucza prywatnego do pliku", Alert.AlertType.WARNING);
                }
            }

            fileChooser.getExtensionFilters().setAll(
                    new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
            fileChooser.setTitle("Zapisz dane publiczne");
            file = fileChooser.showSaveDialog(null);
            if(file != null)
            {

                try
                { String spom=keyQText.getText()+'\n'+keyGText.getText()+'\n'+keyYText.getText()+'\n'+keyPText.getText();
                    Helper.saveFile(spom.getBytes(), file.getAbsolutePath());
                }catch(Exception e){
                    AlertWindow.messageWindow("Błąd zapisu", "Błąd przy zapisywaniu danych publicznych do pliku", Alert.AlertType.WARNING);
                }
            }
        } catch(NumberFormatException  e1){
            AlertWindow.messageWindow("Błąd zapisu", "Błąd przy zapisywaniu klucza", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void onActionSaveSignatureText(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null) {
            try {
            fileNameToSaveSignatureText.setText(file.getAbsolutePath());
            if(fileRadioBox.isSelected()){
                if (textSignature != null){
                    Helper.saveBigIntsWithNewlineToFile(signature,file.getAbsolutePath());
                } else {
                    AlertWindow.messageWindow("Błąd zapisu", "Brak danych do zapisania w pliku", Alert.AlertType.WARNING);
                }

            } else {
                if(!textSignature.getText().isEmpty()){
                    Helper.saveFile(textSignature.getText().getBytes(),file.getAbsolutePath());
                } else {
                    AlertWindow.messageWindow("Błąd zapisu", "Brak danych do zapisania w pliku", Alert.AlertType.WARNING);
                }
            }
            } catch (FileException e) {
                AlertWindow.messageWindow("Błąd pliku", "Błąd podczas zapisywania pliku", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    void onActionSaveTextFile(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            fileNameToSaveText.setText(file.getAbsolutePath());
            try {
                if(fileRadioBox.isSelected()){
                    if (message != null){
                        Helper.saveFile(message,file.getAbsolutePath());
                    } else if(!textToSign.getText().isEmpty()) {
                        Helper.saveFile(textToSign.getText().getBytes(),file.getAbsolutePath());
                    } else {
                        AlertWindow.messageWindow("Błąd zapisu", "Brak danych do zapisania w pliku", Alert.AlertType.WARNING);
                    }

                } else {
                    if(!textToSign.getText().isEmpty()){
                        Helper.saveFile(textToSign.getText().getBytes(),file.getAbsolutePath());
                    } else {
                        AlertWindow.messageWindow("Błąd zapisu", "Brak danych do zapisania w pliku", Alert.AlertType.WARNING);
                    }
                }

            } catch (FileException e) {
                AlertWindow.messageWindow("Błąd pliku", "Błąd podczas zapisywania pliku", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    void onActionSignButton(ActionEvent event) {
        try {
            if(fileRadioBox.isSelected()) {
                if (message != null) {
                    if(dsa.keysWereGenerated()) {
                        signature = dsa.sign(message);

                        String sign = Helper.bytesToHex(signature[0].toByteArray()) +
                                "\n" +
                                Helper.bytesToHex(signature[1].toByteArray());

                        textSignature.setText(sign);
                    } else {
                        AlertWindow.messageWindow("Błąd podpisu", "Brak kluczy", Alert.AlertType.ERROR);
                    }

                } else {
                    AlertWindow.messageWindow("Błąd podpisu", "Brak pliku do podpisania", Alert.AlertType.WARNING);
                }
            } else if(!textToSign.getText().isEmpty()) {
                signature = dsa.sign(textToSign.getText().getBytes());
                String sign = Helper.bytesToHex(signature[0].toByteArray()) +
                        "\n" +
                        Helper.bytesToHex(signature[1].toByteArray());

                textSignature.setText(sign);

            } else {
                AlertWindow.messageWindow("Błąd podpisu", "Brak tekstu do podpisania", Alert.AlertType.WARNING);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            AlertWindow.messageWindow("Błąd podpisu", "Błąd podczas generowania podpisu", Alert.AlertType.WARNING);
        }

    }

    @FXML
    void onActionVerifyButton(ActionEvent event) {
        try{

            if(fileRadioBox.isSelected()) {
                if(message != null && signature != null && dsa.keysForVerificationAvailable()) {

                    if (dsa.verify(message, signature)) {
                        AlertWindow.messageWindow("Podpis jest zgodny.", "Weryfikacja się powiodła", Alert.AlertType.INFORMATION);
                    } else {
                        AlertWindow.messageWindow("Podpis nie jest zgodny.", "Weryfikacja się nie powiodła", Alert.AlertType.ERROR);
                    }
                } else {
                    AlertWindow.messageWindow("Brak informacji.", "Podano za mało informacji", Alert.AlertType.ERROR);
                }
            }
            else {
                if(!textToSign.getText().isEmpty() && !textSignature.getText().isEmpty() && dsa.keysForVerificationAvailable()) {
                    String[] sign = textSignature.getText().split("\n");
                    BigInteger[] signBigInt = new BigInteger[2];
                    signBigInt[0] = new BigInteger(1, Helper.hexToBytes(sign[0]));
                    signBigInt[1] = new BigInteger(1, Helper.hexToBytes(sign[1]));
                    if (dsa.verify(textToSign.getText().getBytes(), signBigInt)) {
                        AlertWindow.messageWindow("Podpis jest zgodny.", "Weryfikacja się powiodła", Alert.AlertType.INFORMATION);
                    } else {
                        AlertWindow.messageWindow("Podpis nie jest zgodny.", "Weryfikacja się nie powiodła", Alert.AlertType.ERROR);
                    }
                } else {
                    AlertWindow.messageWindow("Brak informacji.", "Podano za mało informacji", Alert.AlertType.ERROR);
                }

            }

        }  catch(NumberFormatException | NoSuchAlgorithmException | InvalidKeyException e) {
            AlertWindow.messageWindow("Problem z kluczem.","Nie można przeprowadzić weryfikacji",  Alert.AlertType.ERROR );
        } catch (HexStringException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        dsa.generateKey();
//        keyQText.setText(dsa.getQ().toString(16));
//        keyGText.setText(dsa.getG().toString(16));
//        keyYText.setText(dsa.getY().toString(16));
//        keyXText.setText(dsa.getX().toString(16));
//        keyPText.setText(dsa.getP().toString(16));


        textSignature.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^([A-Fa-f0-9]+\n?[A-Fa-f0-9]*){1}$") || newValue.equals(""))){
                    textSignature.setText(oldValue);
                }
            }
        });

        keyQText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^[A-Fa-f0-9]+$") || newValue.equals(""))){
                    keyQText.setText(oldValue);
                }
            }
        });

        keyGText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^[A-Fa-f0-9]+$") || newValue.equals(""))){
                    keyQText.setText(oldValue);
                }
            }
        });

        keyYText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^[A-Fa-f0-9]+$") || newValue.equals(""))){
                    keyQText.setText(oldValue);
                }
            }
        });
        keyXText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^[A-Fa-f0-9]+$") || newValue.equals(""))){
                    keyQText.setText(oldValue);
                }
            }
        });
        keyPText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^[A-Fa-f0-9]+$") || newValue.equals(""))){
                    keyQText.setText(oldValue);
                }
            }
        });



    }
}
