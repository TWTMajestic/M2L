package btssio.sport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLVoirCompetition {

    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Competition> observableListV = FXCollections.observableArrayList();
    @FXML
    private TableView<Competition> competTableView;
    @FXML
    private TableColumn<Competition, Integer> idColumnV;
    @FXML
    private TableColumn<Competition, String> nomColumnV;

    public void initialize() {
        // Établir une connexion à la base de données et affiche toutes les compétitions de la BDD
        try {
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_COMPET, NOM_COMPET FROM competition");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListV.add(new Competition(rs.getInt("ID_COMPET"),rs.getString("NOM_COMPET")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumnV.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomColumnV.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));

        competTableView.setItems(observableListV);

        competTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifie s'il s'agit d'un double-clic
                Competition compet = competTableView.getSelectionModel().getSelectedItem();
                if (compet != null) {
                    // Ouvre la nouvelle fenêtre pour éditer les données du véhicule sélectionné
                    openModificationCompet(compet);
                }
            }
        });


    }

    // Fonction pour ouvrir la fenêtre d'édition des compétitions
    private void openModificationCompet(Competition compet) {
        try {
            // Charger le fichier FXML de la nouvelle fenêtre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/btssio/sport/Competition/subPage/EditerCompetition.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur associé
            SQLEditCompetition controller = loader.getController();

            // passe les données vers la fenêtre d'édition
            controller.CModifier(compet);

            // Obtenir la fenêtre principale
            Stage stage = HelloApplication.getPrimaryStage();

            // Créer une nouvelle scène et la définir pour la fenêtre principale
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Bouton pour retourner à l'accueil
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
    protected void ReturnBoutonCompetition() throws IOException {
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
