package ma.ofppt.tdi201.quizly.Model;

public class User {
    public String userName;
    private String password;
    private String email;
    private String Prenom;
    private String Filiere;
    public User() {
    }

    public User(String userName, String password, String email, String prenom, String filiere) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        Prenom = prenom;
        Filiere = filiere;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
