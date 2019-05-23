package GUI;

import SQLhandling.QueryHandler;
import SQLhandling.Selector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainWindow
{
    static JFrame frame;

    private JButton mojeKontoButton;
    private JButton mojeArtykułyButton;
    private JButton mojeRecenzjeButton;
    private JButton zarządzajUzytkownikamiButton;
    private JButton wylogujButton;
    private JPanel SidePanel;
    private JPanel Cards;
    private JSeparator Separator;

    private JPanel MyAccountPanel;
    private JPanel MyReviewsPanel;
    private JPanel MyArticlesPanel;
    private JPanel ManageUsersPanel;
    private JPanel MainPanel;
    private JLabel CurrName;
    private JLabel CurrSurname;
    private JLabel CurrEmail;
    private JButton usuńKontoButton;
    private JPasswordField ConfirmDelete;
    private JLabel CurrLogin;
    private JTextField newLogin;
    private JTextField newEmail;
    private JTextField newSurname;
    private JTextField newName;
    private JLabel CurrPesel;
    private JLabel CurrStreetAddress;
    private JLabel CurrPostCode;
    private JLabel CurrTown;
    private JTextField newPesel;
    private JTextField newStreetAddress;
    private JTextField newPostCode;
    private JTextField newTown;
    private JPasswordField oldPasswordField;
    private JButton changeDataButton;
    private JPasswordField newPassword;
    private JPasswordField newPasswordRepeat;
    private JLabel WarningsLabel;

    private final Color defaultTextColor = newLogin.getForeground();

    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};

    private MessageDigest digest = null;
    private MainWindow mainInstance = this;

    private final Logger logger = LogManager.getLogger(MainWindow.class);
    private final Selector selector = new Selector();
    private final QueryHandler queryHandler = new QueryHandler();

    private void CheckDeletion()
    {
        ArrayList<ArrayList<String>> articlesAsAuthor = selector.select("SELECT COUNT(articleID) FROM article where AuthorID = " + CurrentUser.getID() + ";");
        ArrayList<ArrayList<String>> articlesAsRedactor = selector.select("SELECT COUNT(articleID) FROM article where RedactorID = " + CurrentUser.getID() + ";");
        ArrayList<ArrayList<String>> reviews = selector.select("SELECT COUNT(reviewID) FROM review where ReviewerID = " + CurrentUser.getID() + ";");
        ArrayList<ArrayList<String>> paths = selector.select("SELECT COUNT(pathID) FROM path where redactorID = " + CurrentUser.getID() + ";");

        int articlesAsAuthorCount = Integer.parseInt(articlesAsAuthor.get(0).get(0));
        int articlesAsRedactorCount = Integer.parseInt(articlesAsRedactor.get(0).get(0));
        int reviewsCount = Integer.parseInt(reviews.get(0).get(0));
        int pathsCount = Integer.parseInt(paths.get(0).get(0));

        if(articlesAsAuthorCount!=0 || articlesAsRedactorCount!=0 || reviewsCount!=0 || pathsCount!=0)
        {
            String Warning = "Do konta przypisanych jest ";

            if(articlesAsAuthorCount!=0 || articlesAsRedactorCount!=0 ) Warning+= (articlesAsAuthorCount + articlesAsRedactorCount) + " artykułów, ";
            if(reviewsCount!=0)     Warning+= reviewsCount + " recenzji, ";
            if(pathsCount!=0)       Warning+= pathsCount + " ścieżek, ";

            Warning = Warning.replaceAll(", $", "");

            WarningsLabel.setText(Warning);
            usuńKontoButton.setEnabled(false);
            ConfirmDelete.setEnabled(false);
        }
    }

    private void CheckEnableChangeData()
    {
        if(newLogin.getText().isEmpty() && newName.getText().isEmpty() && newSurname.getText().isEmpty()
        && newEmail.getText().isEmpty() && newPesel.getText().isEmpty() && newStreetAddress.getText().isEmpty()
        && newPostCode.getText().isEmpty() && newTown.getText().isEmpty() && String.valueOf(newPassword.getPassword()).isEmpty()
        && String.valueOf(newPasswordRepeat.getPassword()).isEmpty())
        {
            oldPasswordField.setEnabled(false);
            oldPasswordField.setText("");
            changeDataButton.setEnabled(false);
        }
        else
        {
            oldPasswordField.setEnabled(true);
            changeDataButton.setEnabled(true);
        }
    }

    private void changeActiveCard(int card, CardLayout cards)     //  0-3 (so far)
    {
        mojeKontoButton.setEnabled(0!=card);
        mojeArtykułyButton.setEnabled(1!=card);
        mojeRecenzjeButton.setEnabled(2!=card);
        zarządzajUzytkownikamiButton.setEnabled(3!=card);

        mojeKontoButton.setBackground( 0==card ? Color.WHITE : null );
        mojeArtykułyButton.setBackground( 1==card ? Color.WHITE : null );
        mojeRecenzjeButton.setBackground( 2==card ? Color.WHITE: null );
        zarządzajUzytkownikamiButton.setBackground( 3==card ? Color.WHITE: null );

        if(card==0) frame.setTitle("Moje konto");
        if(card==1) frame.setTitle("Moje artykuły");
        if(card==2) frame.setTitle("Moje recenzje");
        if(card==3) frame.setTitle("Zarządzaj użytkownikami");

        if(card==0) cards.show(Cards, "MyAccount");
        if(card==1) cards.show(Cards, "MyArticles");
        if(card==2) cards.show(Cards, "MyReviews");
        if(card==3) cards.show(Cards, "ManageUsers");
    }

    public MainWindow()
    {
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        Cards.add(MyAccountPanel, "MyAccount");
        Cards.add(MyArticlesPanel, "MyArticles");
        Cards.add(MyReviewsPanel, "MyReviews");
        Cards.add(ManageUsersPanel, "ManageUsers");

        final CardLayout cards = (CardLayout) (Cards.getLayout());

        // initial card
        cards.show(Cards, "MyAccount");
        mojeKontoButton.setEnabled(false);
        mojeKontoButton.setBackground(Color.WHITE);

        mojeKontoButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(0, cards);
            }
        });

        mojeArtykułyButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(1, cards);
            }
        });

        mojeRecenzjeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(2, cards);
            }
        });

        zarządzajUzytkownikamiButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(3, cards);
            }
        });

        wylogujButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(JOptionPane.showOptionDialog(frame, "Czy chcesz się wylogować?", "Potwierdź operację",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, confirmOptions, confirmOptions[1])==JOptionPane.YES_OPTION)
                {
                    logger.trace(CurrentUser.getLogin() + " logged out");
                    CurrentUser.setValues(null);
                    new LoginForm().createWindow();
                    frame.dispose();
                }
            }
        });

        // MOJE KONTO

        CheckDeletion();
        CurrLogin.setText(CurrentUser.getLogin());
        CurrName.setText(CurrentUser.getName());
        CurrSurname.setText(CurrentUser.getSurname());
        CurrEmail.setText(CurrentUser.getEmail());
        if(CurrentUser.getPesel()!=null)        CurrPesel.setText(CurrentUser.getPesel());
        if(CurrentUser.getStreetAdress()!=null) CurrStreetAddress.setText(CurrentUser.getStreetAdress());
        if(CurrentUser.getPostCode()!=null)     CurrPostCode.setText(CurrentUser.getPostCode());
        if(CurrentUser.getTown()!=null)         CurrTown.setText(CurrentUser.getTown());

        if(CurrentUser.getLogin().equals("admin"))
        {
            newLogin.setEnabled(false);
            newLogin.setToolTipText("Login dla tego konta nie może zostać zmieniony");
            usuńKontoButton.setEnabled(false);
            ConfirmDelete.setEnabled(false);
            WarningsLabel.setText("Konto \"admin\" nie może zostać usunięte!");
        }
        else    newLogin.setToolTipText("Login musi składać się z cyfr oraz małych i wielkich liter i mieć 8-25 znaków.");

        newPassword.setToolTipText("Hasło musi zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę i mieć 8-25 znaków.");
        newPasswordRepeat.setToolTipText("Wpisz hasło identyczne z tym w polu \"Hasło\"");
        newPostCode.setToolTipText("dd-ddd gdzie \"d\" oznacza cyfrę");

        newLogin.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.loginRegEx.matcher(newLogin.getText()).matches())
                    newLogin.setForeground(defaultTextColor);
                else
                    newLogin.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newName.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.nameRegEx.matcher(newName.getText()).matches())
                    newName.setForeground(defaultTextColor);
                else
                    newName.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newSurname.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.nameRegEx.matcher(newSurname.getText()).matches())
                    newSurname.setForeground(defaultTextColor);
                else
                    newSurname.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newEmail.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.emailRegEx.matcher(newEmail.getText()).matches())
                    newEmail.setForeground(defaultTextColor);
                else
                    newEmail.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newPesel.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.peselRegEx.matcher(newPesel.getText()).matches())
                    newPesel.setForeground(defaultTextColor);
                else
                    newPesel.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newStreetAddress.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.streetAdressRegEx.matcher(newStreetAddress.getText()).matches())
                    newStreetAddress.setForeground(defaultTextColor);
                else
                    newStreetAddress.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newPostCode.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.postCodeRegEx.matcher(newPostCode.getText()).matches())
                    newPostCode.setForeground(defaultTextColor);
                else
                    newPostCode.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newTown.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.townRegEx.matcher(newTown.getText()).matches())
                    newTown.setForeground(defaultTextColor);
                else
                    newTown.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newPassword.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.passwordRegEx.matcher(String.valueOf(newPassword.getPassword())).matches())
                    newPassword.setForeground(defaultTextColor);
                else
                    newPassword.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        newPasswordRepeat.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(String.valueOf(newPassword.getPassword()).equals(String.valueOf(newPasswordRepeat.getPassword())))
                    newPasswordRepeat.setForeground(defaultTextColor);
                else
                    newPasswordRepeat.setForeground(Color.red);
                CheckEnableChangeData();
            }
        });

        changeDataButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String password = String.valueOf(oldPasswordField.getPassword());
                String SHApassword = String.format("%032x", new BigInteger(1, digest.digest(password.getBytes(StandardCharsets.UTF_8))));

                if(!SHApassword.equals(CurrentUser.getPasswordHash()))
                {
                    JOptionPane.showMessageDialog(frame, "Hasło nieprawidłowe", "Błąd", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                else
                {
                    ArrayList<ArrayList<String>> Emails = selector.select("SELECT email FROM sysuser");
                    ArrayList<ArrayList<String>> Logins = selector.select("SELECT login FROM sysuser");
                    ArrayList<ArrayList<String>> Pesels = selector.select("SELECT pesel FROM sysuser");

                    String message = "";

                    if(!newLogin.getText().isEmpty())       // change login
                    {
                        if(!RegexContainer.loginRegEx.matcher(newLogin.getText()).matches())
                        {
                            message+="<html><font color=#ff0000>Login nieprawidłowy!</font>\n" +
                                    "<html><font color=#ff0000>Login musi:</font>\n" +
                                    "<html><font color=#ff0000>- składać się z cyfr oraz małych i wielkich liter</font>\n" +
                                    "<html><font color=#ff0000>- mieć 8-25 znaków.</font>\n\n";
                        }
                        else
                        {
                            boolean repeated = false;
                            for(ArrayList<String> iter : Logins)
                            {
                                String Login = newLogin.getText();
                                if(iter.get(0).equalsIgnoreCase(Login))
                                {
                                    repeated=true;
                                    message+="<html><font color=#ff0000>Login jest już zajęty!</font>\n\n";
                                    break;
                                }
                            }

                            if(!repeated)
                            {
                                String oldLogin = CurrentUser.getLogin();
                                queryHandler.execute("UPDATE sysuser SET login = '" + newLogin.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
                                CurrentUser.setLogin(newLogin.getText());
                                logger.trace(oldLogin + " changed login to " + CurrentUser.getLogin());
                                message+="Login został zmieniony!\n\n";
                                newLogin.setText("");
                            }
                        }
                    }

                    if(!newName.getText().isEmpty())        // change name
                    {
                        if(!RegexContainer.nameRegEx.matcher(newName.getText()).matches())
                            message+="<html><font color=#ff0000>Imię jest nieprawidłowe!</font>\n\n";
                        else
                        {
                            queryHandler.execute("UPDATE sysuser SET name = '" + newName.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
                            logger.trace(CurrentUser.getLogin() + " changed name");
                            message+="Imię zostało zmienione!\n\n";
                            newName.setText("");
                        }
                    }

                    if(!newSurname.getText().isEmpty())     // change surname
                    {
                        if(!RegexContainer.nameRegEx.matcher(newSurname.getText()).matches())
                            message+="<html><font color=#ff0000>Nazwisko jest nieprawidłowe!</font>\n\n";
                        else
                        {
                            queryHandler.execute("UPDATE sysuser SET surname = '" + newSurname.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
                            logger.trace(CurrentUser.getLogin() + " changed surname");
                            message+="Nazwisko zostało zmienione!\n\n";
                            newSurname.setText("");
                        }
                    }

                    if(!newEmail.getText().isEmpty())       // change email
                    {
                        if(!RegexContainer.emailRegEx.matcher(newEmail.getText()).matches())
                            message+="<html><font color=#ff0000>Email jest nieprawidłowy!</font>\n\n";
                        else
                        {
                            boolean repeated=false;
                            for(ArrayList<String> iter : Emails)
                            {
                                String Email = newEmail.getText();
                                if (iter.get(0).equalsIgnoreCase(Email))
                                {
                                    repeated=true;
                                    message+="<html><font color=#ff0000>Email jest już zajęty!</font>\n\n";
                                    break;
                                }
                            }

                            if(!repeated)
                            {
                                queryHandler.execute("UPDATE sysuser SET email = '" + newEmail.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
                                logger.trace(CurrentUser.getLogin() + " changed email");
                                message+="email został zmieniony!\n\n";
                                newEmail.setText("");
                            }
                        }
                    }

                    if(!newPesel.getText().isEmpty())       // change pesel
                    {
                        if(newPesel.getText().length()!=0 && !RegexContainer.peselRegEx.matcher(newPesel.getText()).matches())
                            message+="<html><font color=#ff0000>Pesel jest nieprawidłowy!</font>\n\n";
                        else
                        {
                            boolean repeated=false;
                            if(newPesel.getText().length()!=0)
                            {
                                for (ArrayList<String> iter : Pesels)
                                {
                                    String Pesel = newPesel.getText();
                                    if (iter.get(0)!=null && iter.get(0).equals(Pesel))
                                    {
                                        repeated=true;
                                        message+="<html><font color=#ff0000>Pesel jest już zajęty!</font>\n\n";
                                        break;
                                    }
                                }

                                if(!repeated)
                                {
                                    queryHandler.execute("UPDATE sysuser SET pesel = '" + newPesel.getText() + "' WHERE userID = '" + CurrentUser.getID()+ "';");
                                    logger.trace(CurrentUser.getLogin() + " changed pesel");
                                    message+="pesel został zmieniony!\n\n";
                                    newPesel.setText("");
                                }
                            }
                        }
                    }

                    if(!newStreetAddress.getText().isEmpty())     // change streetAdress
                    {
                        if(!RegexContainer.streetAdressRegEx.matcher(newStreetAddress.getText()).matches())
                            message+="<html><font color=#ff0000>Adres jest nieprawidłowy!</font>\n\n";
                        else
                        {
                            queryHandler.execute("UPDATE sysuser SET streetAddress = '" + newStreetAddress.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
                            logger.trace(CurrentUser.getLogin() + " changed streetAdress");
                            message+="Adres został zmieniony!\n\n";
                            newStreetAddress.setText("");
                        }
                    }

                    if(!newPostCode.getText().isEmpty())     // change PostCode
                    {
                        if(!RegexContainer.postCodeRegEx.matcher(newPostCode.getText()).matches())
                            message+="<html><font color=#ff0000>Kod pocztowy jest nieprawidłowy!</font>\n\n";
                        else
                        {
                            queryHandler.execute("UPDATE sysuser SET PostCode = '" + newPostCode.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");

                            System.out.println("UPDATE sysuser SET PostCode = '" + newPostCode.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");  //!

                            logger.trace(CurrentUser.getLogin() + " changed streetAdress");
                            message+="Kod pocztowy zmieniony!\n\n";
                            newPostCode.setText("");
                        }
                    }

                    if(!newTown.getText().isEmpty())     // change town
                    {
                        if(!RegexContainer.townRegEx.matcher(newTown.getText()).matches())
                            message+="<html><font color=#ff0000>Miejscowość jest nieprawidłowa!</font>\n\n";
                        else
                        {
                            queryHandler.execute("UPDATE sysuser SET town = '" + newTown.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
                            logger.trace(CurrentUser.getLogin() + " changed town");
                            message+="Miejscowość została zmieniona!\n\n";
                            newTown.setText("");
                        }
                    }

                    if(!String.valueOf(newPassword.getPassword()).isEmpty() || !String.valueOf(newPasswordRepeat.getPassword()).isEmpty())        // check password
                    {
                        if(!RegexContainer.passwordRegEx.matcher(String.valueOf(newPassword.getPassword())).matches())
                        {
                            message+="<html><font color=#ff0000>Hasło nieprawidłowe!</font>\n" +
                                    "<html><font color=#ff0000>Hasło musi:</font>\n" +
                                    "<html><font color=#ff0000>- zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę</font>\n" +
                                    "<html><font color=#ff0000>- mieć 8-25 znaków.</font>\n\n";
                        }
                        else
                        {
                            if (!String.valueOf(newPassword.getPassword()).equals(String.valueOf(newPasswordRepeat.getPassword())))    // check repeat password
                                message += "<html><font color=#ff0000>Hasła się nie zgadzają!</font>\n\n";
                            else
                            {
                                String newPass = String.valueOf(newPassword.getPassword());
                                String SHAnewPass = String.format("%032x", new BigInteger(1, digest.digest(newPass.getBytes(StandardCharsets.UTF_8))));

                                queryHandler.execute("UPDATE sysuser SET passwd = '" + SHAnewPass + "' WHERE userID = '" + CurrentUser.getID() + "';");
                                CurrentUser.setPasswordHash(SHAnewPass);
                                logger.trace(CurrentUser.getLogin() + " changed password");
                                message+="Hasło zostało zmienione!\n\n";
                                newPassword.setText("");
                                newPasswordRepeat.setText("");
                            }
                        }
                    }

                    ArrayList<ArrayList<String>> result = selector.select("SELECT * FROM SysUser WHERE userID = '" + CurrentUser.getID() + "';");
                    CurrentUser.setValues(result.get(0));

                    CurrLogin.setText(CurrentUser.getLogin());
                    CurrName.setText(CurrentUser.getName());
                    CurrSurname.setText(CurrentUser.getSurname());
                    CurrEmail.setText(CurrentUser.getEmail());

                    if(CurrentUser.getPesel()==null)
                        CurrPesel.setText("(brak)");
                    else
                        CurrPesel.setText(CurrentUser.getPesel());

                    if(CurrentUser.getStreetAdress()==null)
                        CurrStreetAddress.setText("(brak)");
                    else
                        CurrStreetAddress.setText(CurrentUser.getStreetAdress());

                    if(CurrentUser.getPostCode()==null)
                        CurrPostCode.setText("(brak)");
                    else
                        CurrPostCode.setText(CurrentUser.getPostCode());

                    if(CurrentUser.getTown()==null)
                        CurrTown.setText("(brak)");
                    else
                        CurrTown.setText(CurrentUser.getTown());

                    JOptionPane.showMessageDialog(frame, message, "Wynik operacji", JOptionPane.PLAIN_MESSAGE);
                    oldPasswordField.setText("");
                }
            }
        });

        usuńKontoButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String Password = String.valueOf(ConfirmDelete.getPassword());
                String SHApassword = String.format("%032x", new BigInteger(1, digest.digest(Password.getBytes(StandardCharsets.UTF_8))));

                if(!SHApassword.equals(CurrentUser.getPasswordHash()))
                {
                    JOptionPane.showMessageDialog(frame, "Hasło nieprawidłowe", "Błąd", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                else
                {
                    if(JOptionPane.showOptionDialog(frame, "Usunąć konto? Tej operacji NIE MOŻNA COFNĄĆ!", "Potwierdź operację",
                       JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, confirmOptions, confirmOptions[1])==JOptionPane.YES_OPTION)
                    {
                        logger.trace("User " + CurrentUser.getLogin() + " removed account");
                        queryHandler.execute("DELETE FROM sysuser where userID='"+ CurrentUser.getID() +"';");
                        CurrentUser.setValues(null);

                        new LoginForm().createWindow();
                        frame.dispose();

                        JOptionPane.showMessageDialog(frame, "Twoje konto zostało usunięte.", "Operacja przebiegła pomyślnie", JOptionPane.PLAIN_MESSAGE);
                    }
                    else ConfirmDelete.setText("");
                }
            }
        });

        // MOJE ARTYKUŁY - przesłane i redagowane

            //TODO

        // MOJE RECENZJE

            // TODO

        // ZARZĄDZANIE UŻYKOWNIKAMI

            // TODO
    }

    public void createWindow()
    {
        frame = new JFrame("Moje konto");
        frame.setContentPane(this.MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                if(JOptionPane.showOptionDialog(frame, "Czy chcesz wyjść?", "Potwierdź operację",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, confirmOptions, confirmOptions[1])==JOptionPane.YES_OPTION)
                {
                    logger.trace("Application closed with exit code 0");
                    System.exit(0);
                }
            }
        });
    }
}
