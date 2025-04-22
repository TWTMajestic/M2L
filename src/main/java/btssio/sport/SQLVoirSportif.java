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
import java.sql.*;


public class SQLVoirSportif {

    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Personne> observableListV = FXCollections.observableArrayList();
    @FXML
    private TableView<Personne> personTableView;
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

    public SQLVoirSportif() throws SQLException {
    }

    public void initialize() {
        // Établir une connexion à la base de données et d'afficher les sportifs
        try {
            pre = con.prepareStatement("SELECT ID_CAND, NOM_CAND, PRENOM_CAND, SEXE_CAND, DATEN_CAND FROM personne");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListV.add(new Personne(rs.getInt("ID_CAND"),rs.getString("NOM_CAND"), rs.getString("PRENOM_CAND"), rs.getString("SEXE_CAND"), rs.getDate("DATEN_CAND").toLocalDate()));
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

        personTableView.setItems(observableListV);

        personTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifie s'il s'agit d'un double-clic
                Personne personne = personTableView.getSelectionModel().getSelectedItem();
                if (personne != null) {
                    openModificationPersonne(personne);
                }
            }
        });

    }

    // Ouvre la nouvelle fenêtre pour éditer les données du sportif sélectionné
    private void openModificationPersonne(Personne personne) {
        try {
            // Charger le fichier FXML de la nouvelle fenêtre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/btssio/sport/Personne/subPage/EditSportif.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur associé
            SQLEditSportif controller = loader.getController();

            // passe les données vers la fenêtre d'édition
            controller.SModifier(personne);

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

    // Bouton pour retourner à la page d'accueil
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

    // Bouton pour retourner a la page de gestion de personne
    @FXML
    protected void ReturnBoutonPersonne() throws IOException {
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
