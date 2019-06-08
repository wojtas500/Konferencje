package konferencje;

public class Uzytkownicy {
    public String imie;
    public String nazwisko;
    public String email;
    private String pesel;
    private String adres;
    private String login;
    private String haslo;

    public Uzytkownicy() {
    }

    public String getPesel() {
        return pesel;
    }

    public String getAdres() {
        return adres;
    }

    public String getLogin() {
        return login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
    
}
