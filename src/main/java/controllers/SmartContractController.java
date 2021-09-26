package controllers;

import application.App;
import exceptions.InvalidCodeException;
import exceptions.InvalidPublicKeyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.python.core.PyException;

public class SmartContractController {

    @FXML
    private TextField textFieldSender, textFieldCode;

    @FXML
    protected void send(ActionEvent actionEvent) {
        try {
            BlockchainController.node.newSmartContract(textFieldSender.getText().trim(), textFieldCode.getText());
            App.showInfoDialog("Smart Contract successfully saved", BlockchainController.node.getMemPool().get(BlockchainController.node.getMemPool().size() - 1).toString());
            BlockchainController.newContractStage.close();
        } catch (InvalidPublicKeyException invalidPublicKey) {
            App.showErrorDialog(invalidPublicKey.getMessage());
        } catch (PyException | InvalidCodeException exception) {
            App.showErrorDialog("Invalid Code! Please insert a valid Python-Code! ");
        }
    }
}