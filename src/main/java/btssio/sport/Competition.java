package btssio.sport;

public class Competition {
    // Def variables
    private int ID_COMPET;
    private String NOM_COMPET;

    // Getters and Setters
    public int getID_COMPET() {
        return ID_COMPET;
    }
    public void setID_COMPET(int ID_COMPET) {
        this.ID_COMPET = ID_COMPET;
    }
    public String getNOM_COMPET() {
        return NOM_COMPET;
    }
    public void setNOM_COMPET(String NOM_COMPET) {
        this.NOM_COMPET = NOM_COMPET;
    }

    // Constructor
    public Competition(int ID_COMPET, String NOM_COMPET) {
        this.ID_COMPET = ID_COMPET;
        this.NOM_COMPET = NOM_COMPET;
    }


}
