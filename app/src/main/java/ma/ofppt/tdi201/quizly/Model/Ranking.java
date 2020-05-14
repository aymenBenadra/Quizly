package ma.ofppt.tdi201.quizly.Model;

public class Ranking {
    private String userName;
    private String Prenom;
    private String Filiere;
    private String score;


    public Ranking(){

    }

    public Ranking(String userName, String prenom, String filiere, String score) {
        this.userName = userName;
        Prenom = prenom;
        Filiere = filiere;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getFiliere() {
        return Filiere;
    }

    public void setFiliere(String filiere) {
        Filiere = filiere;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
