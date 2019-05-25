package GUI;

import SQLhandling.QueryHandler;
import SQLhandling.Selector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

class MyAccountPanel
{
    private JFrame frame;

    private JLabel currLogin, currName, currSurname, currEmail, currPesel, currStreetAddress, currPostCode, currTown;
    private JTextField newLogin, newName, newSurname, newEmail, newPesel, newStreetAddress, newPostCode, newTown;
    private JPasswordField newPassword, newPasswordRepeat, oldPasswordField;
    private JButton changeDataButton;
    private JPasswordField confirmDelete;

    private MessageDigest digest;

    KeyListener newLoginListener, newNameListener, newSurnameListener, newEmailListener, newPeselListener,
                newStreetAddressListener, newPostCodeListener, newTownListener;

    private final Logger logger = LogManager.getLogger(MainWindow.class);

    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};

    MyAccountPanel(JFrame frame, JLabel currLogin, JLabel currName, JLabel currSurname, JLabel currEmail, JLabel currPesel, JLabel currStreetAddress, JLabel currPostCode, JLabel currTown,
                          JTextField newLogin, JTextField newName, JTextField newSurname, JTextField newEmail, JTextField newPesel, JTextField newStreetAddress, JTextField newPostCode,
                          JTextField newTown, JPasswordField newPassword, JPasswordField newPasswordRepeat, JPasswordField oldPasswordField, JButton changeDataButton, JPasswordField confirmDelete)
    {
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        this.frame = frame;

        this.currLogin = currLogin;
        this.currName = currName;
        this.currSurname = currSurname;
        this.currEmail = currEmail;
        this.currPesel = currPesel;
        this.currStreetAddress = currStreetAddress;
        this.currPostCode = currPostCode;
        this.currTown = currTown;
        this.newLogin = newLogin;
        this.newName = newName;
        this.newSurname = newSurname;
        this.newEmail = newEmail;
        this.newPesel = newPesel;
        this.newStreetAddress = newStreetAddress;
        this.newPostCode = newPostCode;
        this.newTown = newTown;
        this.newPassword = newPassword;
        this.newPasswordRepeat = newPasswordRepeat;
        this.oldPasswordField = oldPasswordField;
        this.changeDataButton = changeDataButton;
        this.confirmDelete = confirmDelete;

        this.newLoginListener = new RegexKeyListener(RegexContainer.loginRegEx, newLogin);
        this.newNameListener = new RegexKeyListener(RegexContainer.nameRegEx, newName);
        this.newSurnameListener = new RegexKeyListener(RegexContainer.nameRegEx, newSurname);
        this.newEmailListener = new RegexKeyListener(RegexContainer.emailRegEx, newEmail);
        this.newPeselListener = new RegexKeyListener(RegexContainer.peselRegEx, newPesel);
        this.newStreetAddressListener = new RegexKeyListener(RegexContainer.streetAdressRegEx, newStreetAddress);
        this.newPostCodeListener = new RegexKeyListener(RegexContainer.postCodeRegEx, newPostCode);
        this.newTownListener = new RegexKeyListener(RegexContainer.townRegEx, newTown);
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

    private class RegexKeyListener implements KeyListener
    {
        private JTextField textField;
        private Pattern pattern;

        RegexKeyListener(Pattern pattern, JTextField textField)
        {
            this.pattern = pattern;
            this.textField = textField;
        }

        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e)
        {
            if(pattern.matcher(textField.getText()).matches())
                textField.setForeground(Color.black);
            else
                textField.setForeground(Color.red);
            CheckEnableChangeData();
        }
    }

    KeyListener newPasswordListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e)
        {
            if(RegexContainer.passwordRegEx.matcher(String.valueOf(newPassword.getPassword())).matches())
                newPassword.setForeground(Color.black);
            else
                newPassword.setForeground(Color.red);

            if(String.valueOf(newPassword.getPassword()).equals(String.valueOf(newPasswordRepeat.getPassword())))
                newPasswordRepeat.setForeground(Color.black);
            else
                newPasswordRepeat.setForeground(Color.red);

            CheckEnableChangeData();
        }
    };

    KeyListener newPasswordRepeatListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e)
        {
            if(String.valueOf(newPassword.getPassword()).equals(String.valueOf(newPasswordRepeat.getPassword())))
                newPasswordRepeat.setForeground(Color.black);
            else
                newPasswordRepeat.setForeground(Color.red);
            CheckEnableChangeData();
        }
    };

    ActionListener changeDataButtonListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String password = String.valueOf(oldPasswordField.getPassword());
            String SHApassword = String.format("%032x", new BigInteger(1, digest.digest(password.getBytes(StandardCharsets.UTF_8))));

            if(!SHApassword.equalsIgnoreCase(CurrentUser.getPasswordHash()))
            {
                JOptionPane.showMessageDialog(null, "Hasło nieprawidłowe", "Błąd", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            else
            {
                ArrayList<ArrayList<String>> Emails = Selector.select("SELECT email FROM sysuser");
                ArrayList<ArrayList<String>> Logins = Selector.select("SELECT login FROM sysuser");
                ArrayList<ArrayList<String>> Pesels = Selector.select("SELECT pesel FROM sysuser");

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
                            QueryHandler.execute("UPDATE sysuser SET login = '" + newLogin.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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
                        QueryHandler.execute("UPDATE sysuser SET name = '" + newName.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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
                        QueryHandler.execute("UPDATE sysuser SET surname = '" + newSurname.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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
                            QueryHandler.execute("UPDATE sysuser SET email = '" + newEmail.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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
                                QueryHandler.execute("UPDATE sysuser SET pesel = '" + newPesel.getText() + "' WHERE userID = '" + CurrentUser.getID()+ "';");
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
                        QueryHandler.execute("UPDATE sysuser SET streetAddress = '" + newStreetAddress.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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
                        QueryHandler.execute("UPDATE sysuser SET PostCode = '" + newPostCode.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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
                        QueryHandler.execute("UPDATE sysuser SET town = '" + newTown.getText() + "' WHERE userID = '" + CurrentUser.getID() + "';");
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

                            QueryHandler.execute("UPDATE sysuser SET passwd = '" + SHAnewPass + "' WHERE userID = '" + CurrentUser.getID() + "';");
                            CurrentUser.setPasswordHash(SHAnewPass);
                            logger.trace(CurrentUser.getLogin() + " changed password");
                            message+="Hasło zostało zmienione!\n\n";
                            newPassword.setText("");
                            newPasswordRepeat.setText("");
                        }
                    }
                }

                ArrayList<ArrayList<String>> result = Selector.select("SELECT * FROM SysUser WHERE userID = '" + CurrentUser.getID() + "';");
                CurrentUser.setValues(result.get(0));

                currLogin.setText(CurrentUser.getLogin());
                currName.setText(CurrentUser.getName());
                currSurname.setText(CurrentUser.getSurname());
                currEmail.setText(CurrentUser.getEmail());

                if(CurrentUser.getPesel()==null)
                    currPesel.setText("(brak)");
                else
                    currPesel.setText(CurrentUser.getPesel());

                if(CurrentUser.getStreetAdress()==null)
                    currStreetAddress.setText("(brak)");
                else
                    currStreetAddress.setText(CurrentUser.getStreetAdress());

                if(CurrentUser.getPostCode()==null)
                    currPostCode.setText("(brak)");
                else
                    currPostCode.setText(CurrentUser.getPostCode());

                if(CurrentUser.getTown()==null)
                    currTown.setText("(brak)");
                else
                    currTown.setText(CurrentUser.getTown());

                JOptionPane.showMessageDialog(frame, message, "Wynik operacji", JOptionPane.PLAIN_MESSAGE);
                oldPasswordField.setText("");
            }
        }
    };

    ActionListener removeAccountButtonListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String Password = String.valueOf(confirmDelete.getPassword());
            String SHApassword = String.format("%032x", new BigInteger(1, digest.digest(Password.getBytes(StandardCharsets.UTF_8))));

            if(!SHApassword.equalsIgnoreCase(CurrentUser.getPasswordHash()))
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
                    QueryHandler.execute("DELETE FROM sysuser where userID='"+ CurrentUser.getID() +"';");
                    CurrentUser.setValues(null);

                    new LoginForm(frame);
                    frame.dispose();
                    JOptionPane.showMessageDialog(frame, "Twoje konto zostało usunięte.", "Operacja przebiegła pomyślnie", JOptionPane.PLAIN_MESSAGE);
                }
                else confirmDelete.setText("");
            }
        }
    };
}
