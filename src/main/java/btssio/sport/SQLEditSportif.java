package btssio.sport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLEditSportif {

    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Personne> observableListM = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField idTFM;
    @FXML
    private TextField nomTFM;
    @FXML
    private TextField prenomTFM;
    @FXML
    public DatePicker dateNaissanceTFM;
    @FXML
    public ChoiceBox SexeTFM;
    // Tableview
    @FXML
    private TableView<Personne> TableViewSMP;
    @FXML
    private TableColumn<Personne, Integer> idColumn;
    @FXML
    private TableColumn<Personne, String> nomColumn;
    @FXML
    private TableColumn<Personne, String> prenomColumn;
    @FXML
    private TableColumn<Personne, String> sexeColumn;
    @FXML
    private TableColumn<Personne, String> dateNaissanceColumn;

    public SQLEditSportif() throws SQLException {
    }

    public void initialize() {
        voir();

        // remplie le textfill avec les valeurs de la ligne sélectionné
        TableViewSMP.setRowFactory(tv -> {
            TableRow<Personne> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()==MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    Personne clickedRow = row.getItem();
                    SModifier(clickedRow);
                }
            });
            return row ;
        });
    }

    // voir les sportifs de la bdd
    public void voir() {
        // Établir une connexion à la base de données
        try {
            observableListM.clear();
            pre = con.prepareStatement("SELECT ID_CAND, NOM_CAND, PRENOM_CAND, SEXE_CAND, DATEN_CAND FROM personne");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListM.add(new Personne(rs.getInt("ID_CAND"),rs.getString("NOM_CAND"), rs.getString("PRENOM_CAND"), rs.getString("SEXE_CAND"), rs.getDate("DATEN_CAND").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID_CAND"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_CAND"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("PRENOM_CAND"));
        sexeColumn.setCellValueFactory(new PropertyValueFactory<>("SEXE_CAND"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("DATEN_CAND"));

        TableViewSMP.setItems(observableListM);

    }

    // Fonction pour effacer les textfill
    public void effacer () {
        idTFM.clear();
        nomTFM.clear();
        prenomTFM.clear();
        dateNaissanceTFM.getEditor().clear();
    }

    // Ajouter un sportif
    public void ajouter() {
        try {
            pre = con.prepareStatement("INSERT INTO personne (NOM_CAND, PRENOM_CAND, SEXE_CAND, DATEN_CAND) VALUES (?, ?, ?, ?)");
            pre.setString(1, nomTFM.getText());
            pre.setString(2, prenomTFM.getText());
            pre.setString(3, SexeTFM.getValue().toString());
            pre.setDate(4, java.sql.Date.valueOf(dateNaissanceTFM.getValue()));
            pre.executeUpdate();
            effacer();
            voir();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Supprimer un sportif avec son ID
    public void Supprimer() {
        try {
            pre = con.prepareStatement("DELETE FROM participe_a WHERE ID_CAND = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            pre = con.prepareStatement("DELETE FROM appartient_a WHERE ID_CAND = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            pre = con.prepareStatement("DELETE FROM personne WHERE ID_CAND = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            voir();
            effacer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Modifier un sportif avec son ID
    public void Modifier() {
        try {
            pre = con.prepareStatement("UPDATE personne SET NOM_CAND = ?, PRENOM_CAND = ?, SEXE_CAND = ?, DATEN_CAND = ? WHERE ID_CAND = ?");
            pre.setString(1, nomTFM.getText());
            pre.setString(2, prenomTFM.getText());
            pre.setString(3, SexeTFM.getValue().toString());
            pre.setDate(4, java.sql.Date.valueOf(dateNaissanceTFM.getValue()));
            pre.setInt(5, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            voir();
            effacer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Rempli les textfill avec les valeurs de la ligne sélectionnée
    public void SModifier(Personne personne) {
        try {
            idTFM.setText(String.valueOf(personne.getID_CAND()));
            nomTFM.setText(personne.getNOM_CAND());
            prenomTFM.setText(personne.getPRENOM_CAND());
            SexeTFM.setValue(personne.getSEXE_CAND());
            // Conversion et assignation de la date de naissance
            dateNaissanceTFM.setValue(personne.getDATEN_CAND());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // retourne à la page d'accueil
    @FXML
    protected void HomeButton() throws IOException {
        // Charger le fichier FXML pour la nouvelle scène
        Parent Sc1 = FXMLLoader.load(getClass().getResource("/btssio/sport/Accueil.fxml"));

        // Obtenir la fenêtre principale
        Stage stage = HelloApplication.getPrimaryStage();

        // Créer une nouvelle scène et la définir pour la fenêtre principale
        Scene scene = new Scene(Sc1);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    // retourne a la page de gestion de sportif
    @FXML
    protected void ReturnBoutonSPersonne() throws IOException {
        // Charger le fichier FXML pour la nouvelle scène
        Parent Sc1 = FXMLLoader.load(getClass().getResource("/btssio/sport/Personne/Sportif.fxml"));

        // Obtenir la fenêtre principale
        Stage stage = HelloApplication.getPrimaryStage();

        // Créer une nouvelle scène et la définir pour la fenêtre principale
        Scene scene = new Scene(Sc1);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}
