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

public class SQLAjouterEquipeaCompetition {
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Equipe> observableListE = FXCollections.observableArrayList();
    private ObservableList<Competition> observableListC = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField IDTFE;
    @FXML
    private TextField IDTFC;
    // Tableview
    @FXML
    private TableView<Equipe> TableViewE;
    @FXML
    private TableView<Competition> TableViewC;
    @FXML
    private TableColumn<Equipe, Integer> idColumnE;
    @FXML
    private TableColumn<Equipe, String> nomColumnE;
    @FXML
    private TableColumn<Competition, Integer> idColumnC;
    @FXML
    private TableColumn<Competition, String> nomColumnC;

    public void initialize() {
        voir();

        // récupère les valeurs d'une ligne pour la mettre dans le textfield ID Competition
        TableViewC.setRowFactory(tv -> {
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

        // récupère les valeurs d'une ligne pour la mettre dans le textfield ID Equipe
        TableViewE.setRowFactory(tv -> {
            TableRow<Equipe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    Equipe clickedRow = row.getItem();
                    EModifier(clickedRow);
                }
            });
            return row ;
        });
    }

    // Fonction permettant de voir les équipes et les compétitions dans le tableau
    public void voir() {
        voirE();
        voirC();
    }

    // Fonction permettant de voir les équipes dans le tableau
    public void voirE() {
        // Établir une connexion à la base de données
        try {
            observableListE.clear();
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_EQUIPE, NOM_EQUIPE FROM equipe");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListE.add(new Equipe(rs.getInt("ID_EQUIPE"),rs.getString("NOM_EQUIPE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumnE.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomColumnE.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));

        TableViewE.setItems(observableListE);
    }

    // Fonction permettant de voir les compétitions dans le tableau
    public void voirC() {
        // Établir une connexion à la base de données
        try {
            observableListC.clear();
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_COMPET,NOM_COMPET FROM competition");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListC.add(new Competition(rs.getInt("ID_COMPET"),rs.getString("NOM_COMPET")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumnC.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomColumnC.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));

        TableViewC.setItems(observableListC);
    }

    // Fonction permetant de mettre la valeur récupérée quand on clique sur une ligne du tableau dans le textfield ID Equipe
    public void EModifier(Equipe equipe) {
        try {
            IDTFE.setText(String.valueOf(equipe.getID_EQUIPE()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Fonction permetant de mettre la valeur récupérée quand on clique sur une ligne du tableau dans le textfield ID Competition
    public void CModifier(Competition competition) {
        try {
            IDTFC.setText(String.valueOf(competition.getID_COMPET()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // efface les valeurs dans les textfield
    public void effacer () {
        IDTFE.clear();
        IDTFC.clear();
    }

    // Ajoute une équipe à une compétition avec les ID de l'équipe et de la compétition
    public void ajouter() {
        try {
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("INSERT INTO participes_a VALUES (?,?)");
            pre.setInt(2, Integer.parseInt(IDTFC.getText()));
            pre.setInt(1, Integer.parseInt(IDTFE.getText()));
            pre.executeUpdate();
            effacer();
            voir();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Equipe déjà dans la compétition");
            alert.setContentText("L'équipe que vous essayez d'ajouter est déjà dans la compétition.");
            alert.showAndWait();
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

    // Bouton pour retourner à la page de gestion des compétitions
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
