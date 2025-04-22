package btssio.sport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLVoirCompetitiondeSportif {

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
    private TableView<Personne> TableViewSVP;
    @FXML
    private TableView<Competition> TableViewSVC;
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

    public SQLVoirCompetitiondeSportif() throws SQLException {
    }

    public void initialize() {
        reset();

        // rempli le textfill avec les valeurs de la ligne sélectionné
        TableViewSVP.setRowFactory(tv -> {
            TableRow<Personne> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    Personne clickedRow = row.getItem();
                    EModifier(clickedRow);
                }
            });
            return row ;
        });
    }

    // voir les competitions de la bdd
    public void voirCompetition() {
        // Établir une connexion à la base de données
        try {
            observableListVC.clear();
            observableListVP.clear();
            pre = con.prepareStatement(
                        "SELECT competition.ID_COMPET, NOM_COMPET, personne.ID_CAND, NOM_CAND, PRENOM_CAND  " +
                            "FROM personne " +
                            "JOIN appartient_a ON personne.ID_CAND = appartient_a.ID_CAND " +
                            "JOIN equipe ON appartient_a.ID_EQUIPE = equipe.ID_EQUIPE " +
                            "JOIN participes_a ON equipe.ID_EQUIPE = participes_a.ID_EQUIPE " +
                            "JOIN competition ON participes_a.ID_COMPET = competition.ID_COMPET " +
                            "WHERE appartient_a.ID_CAND = ?"
            );
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            rs = pre.executeQuery();
            while (rs.next()){
                observableListVC.add(new Competition(rs.getInt("ID_COMPET"), rs.getString("NOM_COMPET")));
                observableListVP.add(new Personne(rs.getInt("ID_CAND"),rs.getString("NOM_CAND"), rs.getString("PRENOM_CAND")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idCColumn.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomCColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));
        idPColumn.setCellValueFactory(new PropertyValueFactory<>("ID_CAND"));
        nomPColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_CAND"));
        prenomPColumn.setCellValueFactory(new PropertyValueFactory<>("PRENOM_CAND"));

        TableViewSVC.setItems(observableListVC);
        TableViewSVP.setItems(observableListVP);
    }

    // rempli le textfill avec les valeurs de la ligne sélectionné
    public void EModifier(Personne personne) {
        try {
            idTFM.setText(String.valueOf(personne.getID_CAND()));
            voirCompetition();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // effaces les textfill
    public void reset () {
        // Établir une connexion à la base de données
        try {
            observableListVP.clear();
            observableListVC.clear();
            pre = con.prepareStatement("SELECT ID_CAND, NOM_CAND, PRENOM_CAND FROM personne");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListVP.add(new Personne(rs.getInt("ID_CAND"),rs.getString("NOM_CAND"), rs.getString("PRENOM_CAND")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idPColumn.setCellValueFactory(new PropertyValueFactory<>("ID_CAND"));
        nomPColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_CAND"));
        prenomPColumn.setCellValueFactory(new PropertyValueFactory<>("PRENOM_CAND"));

        TableViewSVP.setItems(observableListVP);
        TableViewSVC.setItems(observableListVC);
        idTFM.clear();
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
