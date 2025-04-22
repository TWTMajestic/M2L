package btssio.sport;

public class Equipe {
    // Def variables
    private int ID_EQUIPE;
    private String NOM_EQUIPE;

    // Getters and Setters
    public String getNOM_EQUIPE() {
        return NOM_EQUIPE;
    }
    public void setNOM_EQUIPE(String NOM_EQUIPE) {
        this.NOM_EQUIPE = NOM_EQUIPE;
    }
    public int getID_EQUIPE() {
        return ID_EQUIPE;
    }
    public void setID_EQUIPE(int ID_EQUIPE) {
        this.ID_EQUIPE = ID_EQUIPE;
    }

    // Constructor
    public Equipe(int ID_EQUIPE, String NOM_EQUIPE) {
        this.ID_EQUIPE = ID_EQUIPE;
        this.NOM_EQUIPE = NOM_EQUIPE;
    }
}
