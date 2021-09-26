package controllers;

import application.App;
import exceptions.InvalidPublicKeyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TransactionController {

    @FXML
    private TextField textFieldSender, textFieldReceiver, textFieldAmount;

    @FXML
    protected void send(ActionEvent actionEvent) {
        try {
            BlockchainController.node.newTransaction(textFieldSender.getText().trim(), textFieldReceiver.getText().trim(), Double.parseDouble(textFieldAmount.getText().trim()));
            App.showInfoDialog("Transaction successfully saved", BlockchainController.node.getMemPool().get(BlockchainController.node.getMemPool().size() - 1).toString());
            BlockchainController.newContractStage.close();
        } catch (InvalidPublicKeyException | NumberFormatException exception) {
            App.showErrorDialog(exception.getMessage());
        }
    }
}





