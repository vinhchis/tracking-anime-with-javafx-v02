package com.project.admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.entity.Account;

import javafx.beans.property.ReadOnlyObjectWrapper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccountController implements Initializable{
    @FXML
    private TableView<Account> accTableView;

    @FXML
    private Label adminCountLabel;

    @FXML
    private Label userCountLabel;

    @FXML
    private Label totalCountLabel;

    private AccountViewModel accountViewModel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.accountViewModel = new AccountViewModel();

        // setting table view
        accTableView.setId("acc-table");
        accTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        TableColumn<Account, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Account, String> usernameColumn = new TableColumn<>("USERNAME");
        TableColumn<Account, String> roleColumn = new TableColumn<>("ROLE");
        TableColumn<Account, Button> actionColumn = new TableColumn<>("ACTION");

        idColumn.setCellValueFactory(cell -> {
            return new ReadOnlyObjectWrapper<>(cell.getValue().getId());
        });
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        actionColumn.setCellValueFactory(cell -> {
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px;");
            deleteButton.setOnAction(event -> {
                accountViewModel.deleteAccount(cell.getValue());
                // update count labels
            });
            return new ReadOnlyObjectWrapper<>(deleteButton);
        });


        accTableView.getColumns().addAll(idColumn,usernameColumn, roleColumn, actionColumn);


        // bind
        accTableView.setItems(accountViewModel.getAccounts());

        accountViewModel.selectedAccountProperty().bind(accTableView.getSelectionModel().selectedItemProperty());

        accountViewModel.getAdminCount().bind(adminCountLabel.textProperty());
        accountViewModel.getUserCount().bind(userCountLabel.textProperty());
        accountViewModel.getTotalCount().bind(totalCountLabel.textProperty());

    }



}
