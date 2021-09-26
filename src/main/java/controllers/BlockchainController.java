package controllers;

import application.App;
import domain.Node;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class BlockchainController {

    public static Node node;
    private int blockCounter;
    public static Stage newContractStage;
    private Scene transactionScene;
    private Scene smartContractScene;
    private static Scene miningScene;


    @FXML
    private Label lblGenesisBlock;

    @FXML
    private FlowPane blockFlow;

    @FXML
    private MenuBar menuBar;

    public void initialize() {
        node = new Node();
        newContractStage = new Stage();
        blockCounter = 2;
        menuBar.setFocusTraversable(true);

        lblGenesisBlock.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Alert blockInfo = new Alert(Alert.AlertType.INFORMATION);
                blockInfo.setTitle("Block Information");
                blockInfo.setHeaderText("GENESIS BLOCK");
                blockInfo.setContentText(node.getBlockChain().get(0).toString());
                blockInfo.show();
            }
        });
    }

    @FXML
    protected void mine(ActionEvent event) {

        if (node.getMemPool().size() > 0) {

            try {
                miningScene = new Scene(FXMLLoader.load(App.class.getResource("/view/mine.fxml")), 800, 600);
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }

            final KeyFrame kf1 = new KeyFrame(Duration.seconds(0), e -> App.getMainStage().setScene(miningScene));
            final KeyFrame kf2 = new KeyFrame(Duration.seconds(12), e -> App.getMainStage().setScene(App.getBlockchainScene()));
            final Timeline timeline = new Timeline(kf1, kf2);
            Platform.runLater(timeline::play);

            node.newBlock();

            Image image = new Image("/images/block.gif");
            ImageView imgView = new ImageView();
            imgView.setImage(image);
            imgView.setFitWidth(150);
            imgView.setPreserveRatio(true);

            Label infoBlock = new Label("Block " + blockCounter);
            infoBlock.setTextFill(Paint.valueOf("#4682B4"));
            infoBlock.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

            VBox vbox = new VBox();
            vbox.setPadding(new Insets(75, 0, 0, 50));
            vbox.getChildren().add(infoBlock);

            StackPane blockView = new StackPane();
            blockView.getChildren().add(imgView);
            blockView.getChildren().add(vbox);

            blockFlow.getChildren().add(blockView);

            blockCounter++;

            infoBlock.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Alert blockInfo = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
                    blockInfo.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    blockInfo.setTitle("Block Information");
                    blockInfo.setHeaderText(infoBlock.getText());
                    int blockIndexInList = Integer.parseInt(infoBlock.getText().substring(6)) - 1;
                    blockInfo.setContentText(node.getBlockChain().get(blockIndexInList).toString());
                    blockInfo.show();
                }
            });

        } else {
            Alert emptyMempool = new Alert(Alert.AlertType.ERROR);
            emptyMempool.setTitle("Error");
            emptyMempool.setHeaderText("Mempool is empty");
            emptyMempool.show();
        }
    }

    @FXML
    public void newTransaction(ActionEvent actionEvent) {

        try {
            transactionScene = new Scene(FXMLLoader.load(App.class.getResource("/view/transaction.fxml")), 400, 375);
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        App.showStage(newContractStage, transactionScene, "New Transaction");
    }

    @FXML
    public void showTransactionList(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Enter the Blocknumber ...");
        dialog.setTitle("Show transaction list");
        dialog.setHeaderText("Of which Block do you want to see transactions?");
        dialog.setContentText("Block: ");
        Optional<String> input = dialog.showAndWait();
        if (input.isPresent()) {
            try {
                int block = Integer.parseInt(input.get().toString());
                Alert blockInfo = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
                blockInfo.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                blockInfo.setTitle("Transactions of Block " + block);
                blockInfo.setHeaderText("Transaction List Block " + block);
                blockInfo.setContentText(node.getBlockChain().get(block - 1).transactionsAsStringList());
                blockInfo.show();
            } catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException exception) {
                App.showErrorDialog("Invalid Input!");
            }
        }
    }

    @FXML
    public void newSmartContract(ActionEvent actionEvent) {

        SmartContractController smartContractController = new SmartContractController();

        try {
            smartContractScene = new Scene(FXMLLoader.load(App.class.getResource("/view/smartContract.fxml")), 400, 375);
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        App.showStage(newContractStage, smartContractScene, "New Smart Contract");
    }

    public void showSmartContractList(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Enter the Blocknumber ...");
        dialog.setTitle("Show Smart Contract list");
        dialog.setHeaderText("Of which Block do you want to see smart contracts?");
        dialog.setContentText("Block: ");
        Optional<String> input = dialog.showAndWait();
        if (input.isPresent()) {
            try {
                int block = Integer.parseInt(input.get().toString());
                Alert blockInfo = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
                blockInfo.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                blockInfo.setTitle("Smart Contracts of Block " + block);
                blockInfo.setHeaderText("Smart Contract List Block " + block);
                blockInfo.setContentText(node.getBlockChain().get(block - 1).smartContractsAsStringList());
                blockInfo.show();
            } catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException exception) {
                App.showErrorDialog("Invalid Input!");
            }
        }
    }

    public void showMempool(ActionEvent actionEvent) {
        if (node.getMemPool().size() > 0) {
            Alert blockInfo = new Alert(Alert.AlertType.INFORMATION, "Content here", ButtonType.OK);
            blockInfo.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            blockInfo.setTitle("Mempool");
            blockInfo.setHeaderText("MEMPOOL");
            blockInfo.setContentText(node.showMempool());
            blockInfo.show();
        } else {
            App.showErrorDialog("Mempool is empty!");
        }
    }

}
