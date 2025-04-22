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


public class SQLVoirSportifdeEquipe {

    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Personne> observableListVP = FXCollections.observableArrayList();
    private ObservableList<Equipe> observableListVE = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField idTFM;
    // Tableview
    @FXML
    private TableView<Personne> TableViewSVP;
    @FXML
    private TableView<Equipe> TableViewSVE;
    @FXML
    private TableColumn<Personne, Integer> idPColumn;
    @FXML
    private TableColumn<Personne, String> nomPColumn;
    @FXML
    private TableColumn<Personne, String> prenomPColumn;
    @FXML
    private TableColumn<Equipe, String> idEColumn;
    @FXML
    private TableColumn<Equipe, String> nomEColumn;

    public SQLVoirSportifdeEquipe() throws SQLException {
    }
    public void initialize(){
        reset();

        TableViewSVE.setRowFactory(tv -> {
            TableRow<Equipe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    Equipe clickedRow = row.getItem();
                    // Appeler votre fonction ici
                    EModifier(clickedRow);
                }
            });
            return row ;
        });
    }

    public void voirEquipe() {
        // Établir une connexion à la base de données
        try {
            observableListVE.clear();
            observableListVP.clear();
            pre = con.prepareStatement("SELECT equipe.ID_EQUIPE, NOM_EQUIPE, personne.ID_CAND, NOM_CAND, PRENOM_CAND FROM equipe,personne,appartient_a WHERE personne.ID_CAND = appartient_a.ID_CAND AND equipe.ID_EQUIPE = appartient_a.ID_EQUIPE AND equipe.ID_EQUIPE = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            rs = pre.executeQuery();
            // créer les personne avec les valeur de la bdd
            while (rs.next()){
                observableListVE.add(new Equipe(rs.getInt("ID_EQUIPE"), rs.getString("NOM_EQUIPE")));
                observableListVP.add(new Personne(rs.getInt("ID_CAND"),rs.getString("NOM_CAND"), rs.getString("PRENOM_CAND")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeur dans le tableau
        idEColumn.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomEColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));
        idPColumn.setCellValueFactory(new PropertyValueFactory<>("ID_CAND"));
        nomPColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_CAND"));
        prenomPColumn.setCellValueFactory(new PropertyValueFactory<>("PRENOM_CAND"));

        TableViewSVE.setItems(observableListVE);
        TableViewSVP.setItems(observableListVP);
    }

    public void EModifier(Equipe equipe) {
        try {
            idTFM.setText(String.valueOf(equipe.getID_EQUIPE()));
            voirEquipe();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void reset () {
        try {
            observableListVP.clear();
            observableListVE.clear();
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("SELECT ID_EQUIPE, NOM_EQUIPE FROM equipe");
            rs = pre.executeQuery();
            // créer les personne avec les valeur de la bdd
            while (rs.next()){
                observableListVE.add(new Equipe(rs.getInt("ID_EQUIPE"),rs.getString("NOM_EQUIPE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeur dans le tableau
        idEColumn.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomEColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));

        TableViewSVP.setItems(observableListVP);
        TableViewSVE.setItems(observableListVE);
        idTFM.clear();
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
