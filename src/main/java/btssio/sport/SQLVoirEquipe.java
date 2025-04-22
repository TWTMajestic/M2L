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

public class SQLVoirEquipe {


    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Equipe> observableListV = FXCollections.observableArrayList();
    @FXML
    private TableView<Equipe> equiptableView;
    @FXML
    private TableColumn<Equipe, Integer> idColumnV;
    @FXML
    private TableColumn<Equipe, String> nomColumnV;

    public void initialize() {
        // Établir une connexion à la base de données et affiche les données des équipes
        try {
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_EQUIPE, NOM_EQUIPE FROM equipe");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListV.add(new Equipe(rs.getInt("ID_EQUIPE"),rs.getString("NOM_EQUIPE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumnV.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomColumnV.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));

        equiptableView.setItems(observableListV);

        equiptableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifie s'il s'agit d'un double-clic
                Equipe equipe = equiptableView.getSelectionModel().getSelectedItem();
                if (equipe != null) {
                    // Ouvre la nouvelle fenêtre pour éditer les données du véhicule sélectionné
                    openModificationEquipe(equipe);
                }
            }
        });

    }

    // Ouvre la nouvelle fenêtre pour éditer les données de l'équipe sélectionnée
    private void openModificationEquipe(Equipe equipe) {
        try {
            // Charger le fichier FXML de la nouvelle fenêtre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/btssio/sport/Equipe/subPage/EditerEquipe.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur associé
            SQLEditEquipe controller = loader.getController();

            // passe les données vers la fenêtre d'édition
            controller.EModifier(equipe);

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

    // retourne à la page de gestion d'équipe
    @FXML
    protected void ReturnBoutonEquipe() throws IOException {
        // Charger le fichier FXML pour la nouvelle scène
        Parent Sc1 = FXMLLoader.load(getClass().getResource("/btssio/sport/Equipe/Equipe.fxml"));

        // Obtenir la fenêtre principale
        Stage stage = HelloApplication.getPrimaryStage();

        // Créer une nouvelle scène et la définir pour la fenêtre principale
        Scene scene = new Scene(Sc1);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}
