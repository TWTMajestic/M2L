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


public class SQLVoirCompetitiondeEquipe {

    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Equipe> observableListVE = FXCollections.observableArrayList();
    private ObservableList<Competition> observableListVC = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField idTFM;
    // Tableview
    @FXML
    private TableView<Equipe> TableViewVE;
    @FXML
    private TableView<Competition> TableViewVC;
    @FXML
    private TableColumn<Equipe, Integer> idEColumn;
    @FXML
    private TableColumn<Equipe, String> nomEColumn;
    @FXML
    private TableColumn<Competition, String> idCColumn;
    @FXML
    private TableColumn<Competition, String> nomCColumn;

    public SQLVoirCompetitiondeEquipe() throws SQLException {
    }

    public void initialize() {
        reset();

        // remplie le textfill avec les valeurs de la ligne sélectionné
        TableViewVE.setRowFactory(tv -> {
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

    // voir les compétitions d'une équipe de la bdd
    public void voir() {
        // Établir une connexion à la base de données
        try {
            observableListVC.clear();
            observableListVE.clear();
            pre = con.prepareStatement(
                        "SELECT competition.ID_COMPET, NOM_COMPET, equipe.ID_EQUIPE, NOM_EQUIPE " +
                            "FROM equipe " +
                            "JOIN participes_a ON equipe.ID_EQUIPE = participes_a.ID_EQUIPE " +
                            "JOIN competition ON competition.ID_COMPET = participes_a.ID_COMPET " +
                            "WHERE participes_a.ID_EQUIPE = ?"
            );
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            rs = pre.executeQuery();
            while (rs.next()){
                observableListVE.add(new Equipe(rs.getInt("ID_EQUIPE"), rs.getString("NOM_EQUIPE")));
                observableListVC.add(new Competition(rs.getInt("ID_COMPET"),rs.getString("NOM_COMPET")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idEColumn.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomEColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));
        idCColumn.setCellValueFactory(new PropertyValueFactory<>("ID_COMPET"));
        nomCColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_COMPET"));

        TableViewVC.setItems(observableListVC);
        TableViewVE.setItems(observableListVE);
    }

    // remplie le textfill avec les valeurs de la ligne sélectionné
    public void EModifier(Equipe equipe) {
        try {
            idTFM.setText(String.valueOf(equipe.getID_EQUIPE()));
            voir();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Fonction pour effacer les textfill et revenir à l'état de base des tableView
    public void reset () {
        try {
            observableListVC.clear();
            observableListVE.clear();
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_EQUIPE, NOM_EQUIPE FROM equipe");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListVE.add(new Equipe(rs.getInt("ID_EQUIPE"),rs.getString("NOM_EQUIPE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idEColumn.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomEColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));

        TableViewVC.setItems(observableListVC);
        TableViewVE.setItems(observableListVE);
        idTFM.clear();
        idTFM.clear();
    }

    // Bouton pour revenir à la page d'accueil
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

    // Bouton pour revenir à la page de gestion d'équipe
    @FXML
    protected void ReturnBoutonSEquipe() throws IOException {
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
