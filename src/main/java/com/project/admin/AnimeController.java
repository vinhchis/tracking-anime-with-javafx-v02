package com.project.admin;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class AnimeController implements Initializable {

    // ===== TABLE =====
    @FXML private TableView<AnimeVM> tbl;
    @FXML private TableColumn<AnimeVM, String> colPoster, colTitle, colSeason, colType, colStatus;
    // Cột “Tags” cũ → thành nút Details
    @FXML private TableColumn<AnimeVM, AnimeVM> colDetails;

    // ===== FORM =====
    @FXML private TextField txtPoster, txtTitle, txtStudio, txtEpisodes, searchField;
    @FXML private TextArea  txtIntroduction;
    @FXML private ComboBox<String> cbSeason, cbType, cbStatus;
    @FXML private Label statusLabel;

    // ===== DATA =====
    private final ObservableList<AnimeVM> data = FXCollections.observableArrayList();
    private FilteredList<AnimeVM> filtered;

    private static final String IMAGES_DIR = "images";

    // ==========================================================
    // Init
    // ==========================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Combos
        cbSeason.setItems(FXCollections.observableArrayList("Winter","Spring","Summer","Autumn","—"));
        cbType.setItems(FXCollections.observableArrayList("Series","Movies","OVA","ONA"));
        cbStatus.setItems(FXCollections.observableArrayList("Airing","Finished","Cancelled","Paused"));

        // Bản đồ cột
        colTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        colSeason.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSeason()));
        colType.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        // Poster -> ImageView
        colPoster.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPoster()));
        colPoster.setCellFactory(col -> new TableCell<AnimeVM, String>() {
            private final ImageView iv = new ImageView();
            { iv.setFitWidth(50); iv.setFitHeight(70); iv.setPreserveRatio(true); }
            @Override protected void updateItem(String src, boolean empty) {
                super.updateItem(src, empty);
                if (empty || src == null || src.trim().isEmpty()) { setGraphic(null); return; }
                try {
                    String uri = Paths.get(System.getProperty("user.dir"), src).toUri().toString();
                    iv.setImage(new Image(uri, true));
                    setGraphic(iv);
                } catch (Exception ignore) { setGraphic(null); }
            }
        });

        // Cột nút Details (hiện Introduction)
        colDetails.setText("Details");
        colDetails.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colDetails.setCellFactory(col -> new TableCell<AnimeVM, AnimeVM>() {
            private final Button btn = new Button("Details");
            @Override protected void updateItem(AnimeVM vm, boolean empty) {
                super.updateItem(vm, empty);
                if (empty || vm == null) { setGraphic(null); return; }
                btn.setOnAction(e -> showIntroductionDialog(vm));
                setGraphic(btn);
            }
        });

        // Chọn row → đổ form
        tbl.getSelectionModel().selectedItemProperty().addListener((o, oldV, v) -> fillForm(v));

        // Seed demo
        seedSample();

        // Filtered + Sorted (không đổi tbl.setItems qua lại)
        filtered = new FilteredList<>(data, x -> true);
        SortedList<AnimeVM> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tbl.comparatorProperty());
        tbl.setItems(sorted);
    }

    // ==========================================================
    // Upload poster
    // ==========================================================
    @FXML
    private void onChoosePoster() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Poster Image");
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.gif")
        );
        File sel = fc.showOpenDialog(tbl.getScene().getWindow());
        if (sel == null) return;

        try {
            Path dir = ensureImagesDir();
            String safe = sel.getName().replaceAll("[^a-zA-Z0-9._-]", "_");
            String newName = System.currentTimeMillis() + "_" + safe;
            Path dest = dir.resolve(newName);
            Files.copy(sel.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

            txtPoster.setText(IMAGES_DIR + "/" + newName); // relative path
            statusLabel.setText("Poster uploaded: " + txtPoster.getText());
        } catch (Exception ex) {
            statusLabel.setText("Upload failed: " + ex.getMessage());
        }
    }
    private Path ensureImagesDir() throws Exception {
        Path dir = Paths.get(System.getProperty("user.dir"), IMAGES_DIR);
        Files.createDirectories(dir);
        return dir;
    }

    // ==========================================================
    // CRUD (in-memory)
    // ==========================================================
    @FXML private void onAdd() {
        AnimeVM vm = fromForm();
        if (vm == null) return;
        data.add(vm);
        tbl.getSelectionModel().select(vm);
        statusLabel.setText("Added: " + vm.getTitle());
    }

    @FXML private void onUpdate() {
        AnimeVM sel = tbl.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a row to update."); return; }
        AnimeVM vm = fromForm();
        if (vm == null) return;

        sel.setPoster(vm.getPoster());
        sel.setTitle(vm.getTitle());
        sel.setSeason(vm.getSeason());
        sel.setType(vm.getType());
        sel.setStatus(vm.getStatus());
        sel.setStudio(vm.getStudio());
        sel.setEpisodes(vm.getEpisodes());
        sel.setIntroduction(vm.getIntroduction());

        tbl.refresh();
        statusLabel.setText("Updated: " + sel.getTitle());
    }

    @FXML private void onDelete() {
        AnimeVM sel = tbl.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a row to delete."); return; }
        data.remove(sel);
        onClear();
        statusLabel.setText("Deleted.");
    }

    @FXML private void onClear() {
        txtPoster.clear(); txtTitle.clear(); txtStudio.clear(); txtEpisodes.clear();
        txtIntroduction.clear();
        cbSeason.getSelectionModel().clearSelection();
        cbType.getSelectionModel().clearSelection();
        cbStatus.getSelectionModel().clearSelection();
        tbl.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    // ==========================================================
    // Search (đổi predicate)
    // ==========================================================
    @FXML
    private void onSearch() {
        String kw = (searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase());
        filtered.setPredicate(vm -> {
            if (kw.isEmpty()) return true;
            return contains(vm.getTitle(), kw)
                || contains(vm.getSeason(), kw)
                || contains(vm.getType(), kw)
                || contains(vm.getStatus(), kw)
                || contains(vm.getStudio(), kw);
        });
        statusLabel.setText(kw.isEmpty() ? "" : "Found " + tbl.getItems().size() + " item(s).");
    }
    private boolean contains(String s, String kw){ return s != null && s.toLowerCase().contains(kw); }

    // ==========================================================
    // Details dialog
    // ==========================================================
    private void showIntroductionDialog(AnimeVM vm) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Introduction");
        dialog.setHeaderText(vm.getTitle());
        String intro = vm.getIntroduction() == null ? "" : vm.getIntroduction();

        TextArea ta = new TextArea(intro);
        ta.setEditable(false); ta.setWrapText(true); ta.setPrefRowCount(12);

        dialog.getDialogPane().setContent(new VBox(6,
            new Label("Studio: " + (vm.getStudio()==null?"":vm.getStudio())),
            new Label("Episodes: " + (vm.getEpisodes()==null? "" : vm.getEpisodes())),
            ta
        ));
        dialog.getDialogPane().setMinWidth(520);
        dialog.showAndWait();
    }

    // ==========================================================
    // Helpers
    // ==========================================================
    private void fillForm(AnimeVM a) {
        if (a == null) return;
        txtPoster.setText(a.getPoster());
        txtTitle.setText(a.getTitle());
        cbSeason.getSelectionModel().select(a.getSeason());
        cbType.getSelectionModel().select(a.getType());
        cbStatus.getSelectionModel().select(a.getStatus());
        txtStudio.setText(a.getStudio());
        txtEpisodes.setText(a.getEpisodes()==null? "" : String.valueOf(a.getEpisodes()));
        txtIntroduction.setText(a.getIntroduction());
    }

    private AnimeVM fromForm() {
        String title = txtTitle.getText();
        if (title == null || title.trim().isEmpty()) {
            statusLabel.setText("Title is required.");
            return null;
        }
        Integer eps = parseInt(txtEpisodes.getText());
        return new AnimeVM(
            txtPoster.getText(),
            title,
            sel(cbSeason),
            sel(cbType),
            sel(cbStatus),
            txtStudio.getText(),
            eps,
            txtIntroduction.getText()
        );
    }
    private Integer parseInt(String s){
        try { return (s==null || s.trim().isEmpty()) ? null : Integer.parseInt(s.trim()); }
        catch (Exception e) { statusLabel.setText("Episodes is not a number."); return null; }
    }
    private String sel(ComboBox<String> cb) {
        return cb.getSelectionModel().getSelectedItem() == null ? "" : cb.getSelectionModel().getSelectedItem();
    }

    private void seedSample() {
        data.setAll(
            new AnimeVM("", "One Piece", "Winter", "Series", "Airing",
                    "Toei Animation", 1000,
                    "Luffy dreams to become the Pirate King."),
            new AnimeVM("", "Solo Leveling", "Winter", "Series", "Finished",
                    "A-1 Pictures", 12,
                    "Sung Jinwoo gains a leveling system."),
            new AnimeVM("", "Kaiju No. 8", "Spring", "Series", "Airing",
                    "Production I.G", 24,
                    "Kafka can transform into a kaiju."),
            new AnimeVM("", "Black Clover", "Autumn", "Series", "Cancelled",
                    "Pierrot", 170,
                    "Asta aims to be Wizard King.")
        );
    }

    // ==========================================================
    // ViewModel
    // ==========================================================
    public static class AnimeVM {
        private final SimpleStringProperty poster, title, season, type, status, studio, introduction;
        private final SimpleIntegerProperty episodes;

        public AnimeVM(String poster, String title, String season, String type, String status,
                       String studio, Integer episodes, String introduction) {
            this.poster = new SimpleStringProperty(poster);
            this.title  = new SimpleStringProperty(title);
            this.season = new SimpleStringProperty(season);
            this.type   = new SimpleStringProperty(type);
            this.status = new SimpleStringProperty(status);
            this.studio = new SimpleStringProperty(studio);
            this.episodes = new SimpleIntegerProperty(episodes == null ? 0 : episodes);
            this.introduction = new SimpleStringProperty(introduction);
        }

        public String getPoster(){ return poster.get(); }     public void setPoster(String v){ poster.set(v); }
        public String getTitle(){ return title.get(); }       public void setTitle(String v){ title.set(v); }
        public String getSeason(){ return season.get(); }     public void setSeason(String v){ season.set(v); }
        public String getType(){ return type.get(); }         public void setType(String v){ type.set(v); }
        public String getStatus(){ return status.get(); }     public void setStatus(String v){ status.set(v); }
        public String getStudio(){ return studio.get(); }     public void setStudio(String v){ studio.set(v); }
        public Integer getEpisodes(){ return episodes.get()==0? null : episodes.get(); }
        public void setEpisodes(Integer v){ episodes.set(v==null?0:v); }
        public String getIntroduction(){ return introduction.get(); }
        public void setIntroduction(String v){ introduction.set(v); }
    }
}