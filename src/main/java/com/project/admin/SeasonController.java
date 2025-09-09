package com.project.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SeasonController implements Initializable {

    @FXML
    private TableView<SeasonVM> tblSeasons;
    @FXML
    private TableColumn<SeasonVM, String> colName;
    @FXML
    private TableColumn<SeasonVM, String> colYear;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtYear;
    @FXML
    private TextField searchField;
    @FXML
    private Label statusLabel;

    private final ObservableList<SeasonVM> data = FXCollections.observableArrayList();
    private FilteredList<SeasonVM> filtered;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colName.setCellValueFactory(c -> c.getValue().seasonNameProperty());
        colYear.setCellValueFactory(c -> c.getValue().seasonYearProperty());

        filtered = new FilteredList<>(data, s -> true);
        SortedList<SeasonVM> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tblSeasons.comparatorProperty());
        tblSeasons.setItems(sorted);

        tblSeasons.getSelectionModel().selectedItemProperty().addListener((obs, old, s) -> {
            if (s != null) {
                txtName.setText(s.getSeasonName());
                txtYear.setText(s.getSeasonYear());
            }
        });

        tblSeasons.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void onSearch() {
        String kw = searchField.getText();
        if (kw == null)
            kw = "";
        final String key = kw.toLowerCase().trim();

        filtered.setPredicate(s -> key.isEmpty() ||
                s.getSeasonName().toLowerCase().contains(key) ||
                s.getSeasonYear().toLowerCase().contains(key));
        statusLabel.setText(key.isEmpty() ? "" : "Found: " + tblSeasons.getItems().size());
    }

    @FXML
    private void onAdd() {
        SeasonVM vm = fromForm();
        if (vm == null)
            return;

        boolean exists = data.stream().anyMatch(s -> s.getSeasonName().equalsIgnoreCase(vm.getSeasonName()) &&
                s.getSeasonYear().equalsIgnoreCase(vm.getSeasonYear()));
        if (exists) {
            statusLabel.setText("Season already exists.");
            return;
        }

        data.add(vm);
        tblSeasons.getSelectionModel().select(vm);
        statusLabel.setText("Added.");
    }

    @FXML
    private void onUpdate() {
        SeasonVM sel = tblSeasons.getSelectionModel().getSelectedItem();
        if (sel == null) {
            statusLabel.setText("Select a row to update.");
            return;
        }

        SeasonVM vm = fromForm();
        if (vm == null)
            return;

        sel.setSeasonName(vm.getSeasonName());
        sel.setSeasonYear(vm.getSeasonYear());

        tblSeasons.refresh();
        statusLabel.setText("Updated.");
    }

    @FXML
    private void onDelete() {
        SeasonVM sel = tblSeasons.getSelectionModel().getSelectedItem();
        if (sel == null) {
            statusLabel.setText("Select a row to delete.");
            return;
        }
        data.remove(sel);
        onClear();
        statusLabel.setText("Deleted.");
    }

    @FXML
    private void onClear() {
        txtName.clear();
        txtYear.clear();
        tblSeasons.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    private SeasonVM fromForm() {
        String name = safe(txtName.getText());
        String year = safe(txtYear.getText());
        if (name.isBlank() || year.isBlank()) {
            statusLabel.setText("Both name and year are required.");
            return null;
        }
        return new SeasonVM(name, year);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    // ===== ViewModel =====
    public static class SeasonVM {
        private final SimpleStringProperty seasonName = new SimpleStringProperty();
        private final SimpleStringProperty seasonYear = new SimpleStringProperty();

        public SeasonVM(String name, String year) {
            setSeasonName(name);
            setSeasonYear(year);
        }

        public String getSeasonName() {
            return seasonName.get();
        }

        public void setSeasonName(String v) {
            seasonName.set(v);
        }

        public SimpleStringProperty seasonNameProperty() {
            return seasonName;
        }

        public String getSeasonYear() {
            return seasonYear.get();
        }

        public void setSeasonYear(String v) {
            seasonYear.set(v);
        }

        public SimpleStringProperty seasonYearProperty() {
            return seasonYear;
        }
    }
}