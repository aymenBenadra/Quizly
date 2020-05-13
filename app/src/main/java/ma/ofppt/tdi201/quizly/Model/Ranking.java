package ma.ofppt.tdi201.quizly.Model;

public class Ranking {
    private String userName;
    private String Prenom;
    private String score;

    public Ranking(){

    }

    public Ranking(String userName, String prenom, String score) {
        this.userName = userName;
        this.Prenom = prenom;
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
        this.Prenom = prenom;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
