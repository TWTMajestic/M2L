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

public class SQLAjouterSportifaEquipe {
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Equipe> observableListE = FXCollections.observableArrayList();
    private ObservableList<Personne> observableListP = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField IDTFE;
    @FXML
    private TextField IDTFP;
    // Tableview
    @FXML
    private TableView<Equipe> TableViewE;
    @FXML
    private TableView<Personne> TableViewP;
    @FXML
    private TableColumn<Equipe, Integer> idColumnE;
    @FXML
    private TableColumn<Equipe, String> nomColumnE;
    @FXML
    private TableColumn<Personne, Integer> idColumnP;
    @FXML
    private TableColumn<Personne, String> nomColumnP;
    @FXML
    private TableColumn<Personne, Integer> prenomColumnP;

    public void initialize() {
        voir();

        // récupère les valeurs d'une ligne pour la mettre dans le textfield ID Sportif
        TableViewP.setRowFactory(tv -> {
            TableRow<Personne> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    Personne clickedRow = row.getItem();
                    // Appeler votre fonction ici
                    SModifier(clickedRow);
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

    // Permet de voir les équipes et les sportifs dans le tableau
    public void voir() {
        voirE();
        voirP();
    }
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
    public void voirP() {
        // Établir une connexion à la base de données
        try {
            observableListP.clear();
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_CAND, NOM_CAND, PRENOM_CAND FROM personne");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListP.add(new Personne(rs.getInt("ID_CAND"),rs.getString("NOM_CAND"),rs.getString("PRENOM_CAND")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumnP.setCellValueFactory(new PropertyValueFactory<>("ID_CAND"));
        nomColumnP.setCellValueFactory(new PropertyValueFactory<>("NOM_CAND"));
        prenomColumnP.setCellValueFactory(new PropertyValueFactory<>("PRENOM_CAND"));

        TableViewP.setItems(observableListP);
    }

    // Permet d'effacer les textfield
    public void effacer () {
        IDTFE.clear();
        IDTFP.clear();
    }

    // Permet d'ajouter un sportif à une équipe
    public void ajouter() {
        try {
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("INSERT INTO appartient_a VALUES (?,?)");
            pre.setInt(1, Integer.parseInt(IDTFP.getText()));
            pre.setInt(2, Integer.parseInt(IDTFE.getText()));
            pre.executeUpdate();
            effacer();
            voir();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Sportif déjà dans l'équipe");
            alert.setContentText("Le sportif que vous essayez d'ajouter est déjà dans l'équipe.");
            alert.showAndWait();

        }
    }

    // Permet de modifier le textfield ID Equipe
    public void EModifier(Equipe equipe) {
        try {
            IDTFE.setText(String.valueOf(equipe.getID_EQUIPE()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Permet de modifier le textfield ID Sportif
    public void SModifier(Personne personne) {
        try {
            IDTFP.setText(String.valueOf(personne.getID_CAND()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
