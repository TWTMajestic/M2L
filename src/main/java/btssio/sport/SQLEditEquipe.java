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

public class SQLEditEquipe {
    Connection con = DataBaseConnection.getConnection();
    private PreparedStatement pre;
    private ResultSet rs;
    private ObservableList<Equipe> observableListM = FXCollections.observableArrayList();
    // Textfill
    @FXML
    private TextField idTFM;
    @FXML
    private TextField nomTFM;
    // Tableview
    @FXML
    private TableView<Equipe> TableViewSME;
    @FXML
    private TableColumn<Equipe, Integer> idColumn;
    @FXML
    private TableColumn<Equipe, String> nomColumn;

    public SQLEditEquipe() throws SQLException {
    }

    public void initialize() {
        voir();

        // rempli le textfill avec les valeurs de la ligne sélectionnée
        TableViewSME.setRowFactory(tv -> {
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

    // voir les equipes de la bdd
    public void voir() {
        // Établir une connexion à la base de données
        try {
            observableListM.clear();
            pre = con.prepareStatement("SELECT ID_EQUIPE, NOM_EQUIPE FROM equipe");
            rs = pre.executeQuery();
            while (rs.next()){
                observableListM.add(new Equipe(rs.getInt("ID_EQUIPE"),rs.getString("NOM_EQUIPE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // met les valeurs dans le tableau
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID_EQUIPE"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_EQUIPE"));

        TableViewSME.setItems(observableListM);

    }

    // Ajouter une équipe
    public void ajouter() {
        try {
            Connection con = DataBaseConnection.getConnection();
            pre = con.prepareStatement("INSERT INTO equipe (NOM_EQUIPE) VALUES (?)");
            pre.setString(1, nomTFM.getText());
            pre.executeUpdate();
            effacer();
            voir();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Supprimer une équipe avec son ID
    public void Supprimer() {
        try {
            pre = con.prepareStatement("DELETE FROM participes_a WHERE ID_EQUIPE = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            pre = con.prepareStatement("DELETE FROM appartient_a WHERE ID_EQUIPE = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            pre = con.prepareStatement("DELETE FROM equipe WHERE ID_EQUIPE = ?");
            pre.setInt(1, Integer.parseInt(idTFM.getText()));
            pre.executeUpdate();
            voir();
            effacer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Effacer les textfill
    public void effacer () {
        idTFM.clear();
        nomTFM.clear();
    }

    // Modifier une équipe grâce à son ID
    public void Modifier() {
        try {
            pre = con.prepareStatement("UPDATE equipe SET NOM_EQUIPE = ? WHERE ID_EQUIPE = ?");
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
    public void EModifier(Equipe equipe) {
        try {
            idTFM.setText(String.valueOf(equipe.getID_EQUIPE()));
            nomTFM.setText(equipe.getNOM_EQUIPE());

        } catch (Exception e) {
            throw new RuntimeException(e);
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

    // Bouton pour retourner à la page de gestion d'équipe
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
