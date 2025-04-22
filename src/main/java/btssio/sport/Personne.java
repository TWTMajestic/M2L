package btssio.sport;

import java.time.LocalDate;
import java.util.Date;

public class Personne {
    // Def variables
    private int ID_CAND;
    private String NOM_CAND;
    private String PRENOM_CAND;
    private String SEXE_CAND;
    private LocalDate DATEN_CAND;

    // Constructor
    public Personne(int ID_CAND, String NOM_CAND, String PRENOM_CAND, String SEXE_CAND, LocalDate DATEN_CAND) {
        this.ID_CAND = ID_CAND;
        this.NOM_CAND = NOM_CAND;
        this.PRENOM_CAND = PRENOM_CAND;
        this.SEXE_CAND = SEXE_CAND;
        this.DATEN_CAND = DATEN_CAND;
    }

    public Personne(int ID_CAND, String NOM_CAND, String PRENOM_CAND) {
        this.ID_CAND = ID_CAND;
        this.NOM_CAND = NOM_CAND;
        this.PRENOM_CAND = PRENOM_CAND;
    }

    // Getters and Setters
    public int getID_CAND() {
        return ID_CAND;
    }
    public void setID_CAND(int ID_CAND) {
        this.ID_CAND = ID_CAND;
    }
    public String getNOM_CAND() {
        return NOM_CAND;
    }
    public void setNOM_CAND(String NOM_CAND) {
        this.NOM_CAND = NOM_CAND;
    }
    public String getPRENOM_CAND() {
        return PRENOM_CAND;
    }
    public void setPRENOM_CAND(String PRENOM_CAND) {
        this.PRENOM_CAND = PRENOM_CAND;
    }
    public String getSEXE_CAND() {
        return SEXE_CAND;
    }
    public void setSEXE_CAND(String SEXE_CAND) {
        this.SEXE_CAND = SEXE_CAND;
    }
    public LocalDate getDATEN_CAND() {
        return DATEN_CAND;
    }
    public void setDATEN_CAND(LocalDate DATEN_CAND) {
        this.DATEN_CAND = DATEN_CAND;
    }
}