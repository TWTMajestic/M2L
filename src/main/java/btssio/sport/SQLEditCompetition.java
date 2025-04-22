package btssio.sport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLEditCompetition {
    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Competition> observableListC = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField idTFM;
    @FXML
    private TextField nomTFM;
    // Tableview
    @FXML
    private TableView<Competition> TableViewSMC;
    @FXML
    private TableColumn<Competition, Integer> idColumn;
    @FXML
    private TableColumn<Competition, String> nomColumn;

    public SQLEditCompetition() throws SQLException {
    }

    public void initialize() {
        voir();

        // remplie le textfill avec les valeurs de la ligne sélectionnée
        TableViewSMC.setRowFactory(tv -> {
            TableRow<Competition> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    Competition clickedRow = row.getItem();
                    CModifier(clickedRow);
                }
            });
            return row ;
        });
    }

    // Fonction pour voir les valeurs de la bdd
    public void voir() {
        // Établir une connexion à la base de données
        try {
            observableListC.clear();
            pre = con.prepareStatement("SELECT ID_COMPET, NOM_COMPET FROM competition");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListC.add(new Competition(rs.getInt("ID_COMPET"),rs.getString("NOM_COMPET")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));

        TableViewSMC.setItems(observableListC);

    }

    // Fonction pour effacer les textfill
    public void effacer () {
        idTFM.clear();
        nomTFM.clear();
    }

    // ajoute une compétition à la BDD
    public void ajouter() {
        try {
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("INSERT INTO competition (NOM_COMPET) VALUES (?)");
            pre.setString(1, nomTFM.getText());
            pre.executeUpdate();
            effacer();
            voir();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Supprime une compétition de la BDD grace a son ID
    public void Supprimer() {
        try {
            pre = con.prepareStatement("DELETE FROM participes_a WHERE ID_COMPET = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            pre = con.prepareStatement("DELETE FROM participe_a WHERE ID_COMPET = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            pre = con.prepareStatement("DELETE FROM competition WHERE ID_COMPET = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            voir();
            effacer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Modifie une compétition de la BDD grace à son ID
    public void Modifier() {
        try {
            pre = con.prepareStatement("UPDATE competition SET NOM_COMPET = ? WHERE ID_COMPET = ?");
            pre.setString(1, nomTFM.getText());
            pre.setInt(2, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            voir();
            effacer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Rempli les textfill avec les valeurs de la ligne sélectionnée
    public void CModifier(Competition competition) {
        try {
            idTFM.setText(String.valueOf(competition.getID_COMPET()));
            nomTFM.setText(competition.getNOM_COMPET());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Bouton pour retourner a l'accueil
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

    // Bouton pour retourner a la page de gestion de compétition
    @FXML
    protected void ReturnBoutonCompet() throws IOException {
        // Charger le fichier FXML pour la nouvelle scène
        Parent Sc1 = FXMLLoader.load(getClass().getResource("/btssio/sport/Competition/Competition.fxml"));

        // Obtenir la fenêtre principale
        Stage stage = HelloApplication.getPrimaryStage();

        // Créer une nouvelle scène et la définir pour la fenêtre principale
        Scene scene = new Scene(Sc1);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}
