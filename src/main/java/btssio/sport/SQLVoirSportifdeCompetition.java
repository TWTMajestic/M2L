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


public class SQLVoirSportifdeCompetition {

    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Personne> observableListVP = FXCollections.observableArrayList();
    private ObservableList<Competition> observableListVC = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField idTFM;
    // Tableview
    @FXML
    private TableView<Personne> TableViewVP;
    @FXML
    private TableView<Competition> TableViewVC;
    @FXML
    private TableColumn<Personne, Integer> idPColumn;
    @FXML
    private TableColumn<Personne, String> nomPColumn;
    @FXML
    private TableColumn<Personne, String> prenomPColumn;
    @FXML
    private TableColumn<Competition, String> idCColumn;
    @FXML
    private TableColumn<Competition, String> nomCColumn;

    public SQLVoirSportifdeCompetition() throws SQLException {
    }

    public void initialize() {
        reset();

        // rempli le textfill avec les valeurs de la ligne sélectionné
        TableViewVC.setRowFactory(tv -> {
            TableRow<Competition> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    Competition clickedRow = row.getItem();
                    EModifier(clickedRow);
                }
            });
            return row ;
        });
    }

    // Fonction pour voir les sportifs d'une compétition
    public void voir() {
        // Établir une connexion à la base de données
        try {
            observableListVC.clear();
            observableListVP.clear();
            pre = con.prepareStatement("SELECT competition.ID_COMPET, NOM_COMPET, personne.ID_CAND, NOM_CAND,PRENOM_CAND FROM competition,equipe,personne,participes_a,appartient_a WHERE equipe.ID_EQUIPE = participes_a.ID_EQUIPE AND competition.ID_COMPET = participes_a.ID_COMPET AND equipe.ID_EQUIPE = appartient_a.ID_EQUIPE AND personne.ID_CAND = appartient_a.ID_CAND AND competition.ID_COMPET = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            rs = pre.executeQuery();
            while (rs.next()){
                observableListVP.add(new Personne(rs.getInt("ID_CAND"), rs.getString("NOM_CAND"), rs.getString("PRENOM_CAND")));
                observableListVC.add(new Competition(rs.getInt("ID_COMPET"),rs.getString("NOM_COMPET")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idPColumn.setCellValueFactory(new PropertyValueFactory<>("ID_CAND"));
        nomPColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_CAND"));
        prenomPColumn.setCellValueFactory(new PropertyValueFactory<>("PRENOM_CAND"));
        idCColumn.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomCColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));

        TableViewVC.setItems(observableListVC);
        TableViewVP.setItems(observableListVP);
    }

    // rempli le textfill avec les valeurs de la ligne sélectionné
    public void EModifier(Competition competition) {
        try {
            idTFM.setText(String.valueOf(competition.getID_COMPET()));
            voir();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Fonction pour effacer les textfill
    public void reset () {
        try {
            observableListVC.clear();
            observableListVP.clear();
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_COMPET, NOM_COMPET FROM competition");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListVC.add(new Competition(rs.getInt("ID_COMPET"),rs.getString("NOM_COMPET")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idCColumn.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomCColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));

        TableViewVC.setItems(observableListVC);
        TableViewVP.setItems(observableListVP);
        idTFM.clear();
    }

    // retourner à la page d'accueil
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
    protected void ReturnBoutonSEquipe() throws IOException {
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
