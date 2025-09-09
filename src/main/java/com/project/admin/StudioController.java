package com.project.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class StudioController implements Initializable {

    // Table & columns
    @FXML private TableView<StudioVM> tblStudios;
    @FXML private TableColumn<StudioVM, String> colLogo;
    @FXML private TableColumn<StudioVM, String> colId;
    @FXML private TableColumn<StudioVM, String> colName;
    @FXML private TableColumn<StudioVM, String> colBestAnimes;

    // Form fields
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtLogo;
    @FXML private TextField txtBestAnimes;

    // Search & status
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    // Backing list
    private final ObservableList<StudioVM> data = FXCollections.observableArrayList();
    private FilteredList<StudioVM> filtered;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Value factories
        colId.setCellValueFactory(new PropertyValueFactory<>("studioId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("studioName"));
        colBestAnimes.setCellValueFactory(new PropertyValueFactory<>("bestAnimes"));

        // Logo column: value + cell for ImageView
        colLogo.setCellValueFactory(new PropertyValueFactory<>("logoUrl"));
        colLogo.setCellFactory(col -> new TableCell<StudioVM, String>() {
            private final ImageView iv = new ImageView();
            {
                iv.setFitWidth(50);
                iv.setFitHeight(50);
                iv.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String src, boolean empty) {
                super.updateItem(src, empty);
                if (empty || src == null || src.isBlank()) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                try {
                    Path file = Path.of(System.getProperty("user.dir"), "images", src);
                    iv.setImage(new Image(file.toUri().toString(), true));
                    setGraphic(iv);
                    setText(null);
                } catch (Exception e) {
                    setGraphic(null);
                    setText(src); // fallback
                }
            }
        });

        // Filter + sort
        filtered = new FilteredList<>(data, s -> true);
        SortedList<StudioVM> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tblStudios.comparatorProperty());
        tblStudios.setItems(sorted);

        // Fill form when selecting a row
        tblStudios.getSelectionModel().selectedItemProperty().addListener((obs, old, s) -> {
            if (s != null) {
                txtId.setText(s.getStudioId());
                txtName.setText(s.getStudioName());
                txtLogo.setText(s.getLogoUrl());
                txtBestAnimes.setText(s.getBestAnimes());
            }
        });

        // Optional: auto-resize columns nicely
        tblStudios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // (Optional) seed demo
        // data.add(new StudioVM("1", "Ufotable", "ufotable.png", "Demon Slayer"));
    }

    // ===== Actions =====

    @FXML
    private void onSearch() {
        String kw = searchField.getText();
        if (kw == null) kw = "";
        final String key = kw.toLowerCase().trim();

        filtered.setPredicate(s ->
            key.isEmpty() || s.getStudioName().toLowerCase().contains(key)
        );
        statusLabel.setText(key.isEmpty() ? "" : "Found: " + tblStudios.getItems().size());
    }

    @FXML
    private void onAdd() {
        StudioVM vm = fromForm();
        if (vm == null) return;

        // simple unique check by ID
        boolean exists = data.stream().anyMatch(s -> s.getStudioId().equals(vm.getStudioId()));
        if (exists) {
            statusLabel.setText("Studio ID already exists.");
            return;
        }

        data.add(vm);
        tblStudios.getSelectionModel().select(vm);
        statusLabel.setText("Added.");
    }

    @FXML
    private void onUpdate() {
        StudioVM sel = tblStudios.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a row to update."); return; }

        StudioVM vm = fromForm();
        if (vm == null) return;

        // keep ID of selected row; allow editing ID if bạn muốn
        sel.setStudioId(vm.getStudioId());
        sel.setStudioName(vm.getStudioName());
        sel.setLogoUrl(vm.getLogoUrl());
        sel.setBestAnimes(vm.getBestAnimes());

        tblStudios.refresh();
        statusLabel.setText("Studio updated.");
    }

    @FXML
    private void onDelete() {
        StudioVM sel = tblStudios.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a row to delete."); return; }
        data.remove(sel);
        onClear();
        statusLabel.setText("Deleted.");
    }

    @FXML
    private void onClear() {
        txtId.clear();
        txtName.clear();
        txtLogo.clear();
        txtBestAnimes.clear();
        tblStudios.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    @FXML
    private void onChooseLogo() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fc.showOpenDialog(txtLogo.getScene().getWindow());
        if (file != null) {
            try {
                Path dest = Path.of(System.getProperty("user.dir"), "images", file.getName());
                Files.createDirectories(dest.getParent());
                Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                txtLogo.setText(file.getName()); // chỉ lưu tên file
                statusLabel.setText("Logo selected: " + file.getName());
            } catch (Exception e) {
                statusLabel.setText("Error saving logo: " + e.getMessage());
            }
        }
    }

    // ===== Helpers =====

    private StudioVM fromForm() {
        String id   = safe(txtId.getText());
        String name = safe(txtName.getText());
        String logo = safe(txtLogo.getText());
        String best = safe(txtBestAnimes.getText());

        if (id.isBlank())   { statusLabel.setText("Studio ID is required."); return null; }
        if (name.isBlank()) { statusLabel.setText("Name is required."); return null; }

        return new StudioVM(id, name, logo, best);
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    // ===== ViewModel =====
    public static class StudioVM {
    private final SimpleStringProperty studioId = new SimpleStringProperty();
    private final SimpleStringProperty studioName = new SimpleStringProperty();
    private final SimpleStringProperty logoUrl = new SimpleStringProperty();
    private final SimpleStringProperty bestAnimes = new SimpleStringProperty();

    public StudioVM(String id, String name, String logo, String best) {
        setStudioId(id);
        setStudioName(name);
        setLogoUrl(logo);
        setBestAnimes(best);
    }

    public String getStudioId() { return studioId.get(); }
    public void setStudioId(String v) { studioId.set(v); }

    public String getStudioName() { return studioName.get(); }
    public void setStudioName(String v) { studioName.set(v); }

    public String getLogoUrl() { return logoUrl.get(); }
    public void setLogoUrl(String v) { logoUrl.set(v); }

    public String getBestAnimes() { return bestAnimes.get(); }
    public void setBestAnimes(String v) { bestAnimes.set(v); }
}

}