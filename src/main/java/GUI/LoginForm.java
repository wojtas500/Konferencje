package GUI;

import SQLhandling.DBConnection;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginForm
{
    static JFrame frame;

    private JPanel MainPanel, Cards;
    private CardLayout cards;

    private JPanel StartScreen;
    private JLabel ImageConference;
    private JButton logInButton, signInButton, ExitButton;

    private JPanel LogIn;
    private JLabel ImageKey;
    private JTextField LogInEmail;
    private JPasswordField LogInPassword;
    private JButton BackButton, LogInButton;

    private JPanel SignIn;
    private JLabel ImageAdd;
    private JTextField SignUpName, SignUpSurname, SignUpEmail, SignUpLogin,
                       SignUpPesel, SignUpStreetAddress, SignUpPostCode, SignUpTown;
    private JPasswordField SignUpPassword, SignUpRepeatPassword;
    private JButton BackButton1, SignUpButton;

    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};

    private static final int WindowWidth = 350;
    private static final int StartScreenHeight = 405;
    private static final int LogInHeight = 405;
    private static final int SignInHeight = 610;
    private final Color defaultTextColor = SignUpLogin.getForeground();

    MessageDigest digest = null;
    final Logger logger = LogManager.getLogger(LoginForm.class);

    private class RegexKeyListener implements KeyListener // broken
    {
        private JTextField textField;
        private Pattern pattern;

        private RegexKeyListener(Pattern pattern, JTextField textField)
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
            if(pattern.matcher(textField.getText()).matches())  // error:  textField is null despite setting
                textField.setForeground(Color.black);
            else
                textField.setForeground(Color.red);
        }
    }

    public LoginForm(JFrame parentFrame)
    {
        frame = new JFrame("Zaloguj lub zarejestruj się");
        frame.setContentPane(this.MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(WindowWidth, StartScreenHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(parentFrame);

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

        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        Cards.add(StartScreen, "StartScreen");
        Cards.add(LogIn, "LogIn");
        Cards.add(SignIn, "SignIn");

        cards = (CardLayout) (Cards.getLayout());
        cards.show(Cards, "StartScreen");

        try
        {
            DBConnection.GetInstance().getConnection();
        }
        catch (SQLException e)
        {
            logger.error("Couldn't get connection to database");
            logger.trace("Application closed with exit code 1");

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Nie można połaczyć z bazą danych", "Błąd", JOptionPane.PLAIN_MESSAGE);
            System.exit(1);
        }

        logger.trace("Connected to Database");

        //// StartScreen ////

        logInButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.setSize(WindowWidth, LogInHeight);
                frame.setTitle("Zaloguj się");
                cards.show(Cards, "LogIn");
                LogInEmail.requestFocus();
            }
        });

        signInButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.setSize(WindowWidth, SignInHeight);
                frame.setTitle("Zarejestruj się");
                cards.show(Cards, "SignIn");
                SignUpEmail.requestFocus();
            }
        });

        ExitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(JOptionPane.showOptionDialog(frame, "Czy chcesz wyjść?", "Potwierdź operację",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, confirmOptions, confirmOptions[1])==JOptionPane.YES_OPTION)
                {
                    logger.trace("Application closed with exit code 0");
                    System.exit(0);
                }
            }
        });

        //// LogIn ////

        LogInButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String login = LogInEmail.getText();
                String password = String.valueOf(LogInPassword.getPassword());
                String SHApassword = String.format("%032x", new BigInteger(1, digest.digest(password.getBytes(StandardCharsets.UTF_8))));

                ArrayList<ArrayList<String>> result = Selector.select
                ("SELECT * FROM SysUser " + "WHERE login = '" + login + "' and passwd = '" + SHApassword + "';");

                if (result.size() == 1)
                {
                    CurrentUser.setValues(result.get(0));
                    logger.trace("User " + CurrentUser.getLogin() + " logged");

                    new MainWindow(frame);//.createWindow(frame);
                    frame.dispose();
                }
                else
                {
                    if(Selector.select("SELECT * FROM SysUser WHERE login = '" + login + "';").size()==0)
                    {
                        JOptionPane.showMessageDialog(frame, "Niewłaściwy login!", "Błąd", JOptionPane.PLAIN_MESSAGE);
                        logger.warn("Incorrect email provided when attempting to login: " + login);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(frame, "Niewłaściwe hasło!", "Błąd", JOptionPane.PLAIN_MESSAGE);
                        logger.warn("Incorrect password provided when attempting to login as: " + login);
                    }
                }
            }
        });

        ActionListener BackButtonListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.setSize(WindowWidth, StartScreenHeight);
                frame.setTitle("Zaloguj lub zarejestruj się");
                cards.show(Cards, "StartScreen");
                logInButton.requestFocus();
            }
        };

        BackButton.addActionListener(BackButtonListener);

        // SignIn //

        SignUpLogin.setToolTipText("Login musi składać się z cyfr oraz małych i wielkich liter i mieć 8-25 znaków.");
        SignUpPassword.setToolTipText("Hasło musi zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę i mieć 8-25 znaków.");
        SignUpRepeatPassword.setToolTipText("Wpisz hasło identyczne z tym w polu \"Hasło\"");
        SignUpPostCode.setToolTipText("dd-ddd gdzie \"d\" oznacza cyfrę");

        SignUpLogin.addKeyListener(new RegexKeyListener(RegexContainer.loginRegEx, SignUpLogin));
        SignUpName.addKeyListener(new RegexKeyListener(RegexContainer.nameRegEx, SignUpName));
        SignUpSurname.addKeyListener(new RegexKeyListener(RegexContainer.nameRegEx, SignUpSurname));
        SignUpEmail.addKeyListener(new RegexKeyListener(RegexContainer.emailRegEx, SignUpEmail));
        SignUpPesel.addKeyListener(new RegexKeyListener(RegexContainer.peselRegEx, SignUpPesel));
        SignUpStreetAddress.addKeyListener(new RegexKeyListener(RegexContainer.streetAdressRegEx, SignUpStreetAddress));
        SignUpPostCode.addKeyListener(new RegexKeyListener(RegexContainer.postCodeRegEx, SignUpPostCode));
        SignUpTown.addKeyListener(new RegexKeyListener(RegexContainer.townRegEx, SignUpTown));

        SignUpPassword.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.passwordRegEx.matcher(String.valueOf(SignUpPassword.getPassword())).matches())
                    SignUpPassword.setForeground(defaultTextColor);
                else
                    SignUpPassword.setForeground(Color.red);

                if(String.valueOf(SignUpPassword.getPassword()).equals(String.valueOf(SignUpRepeatPassword.getPassword())))
                    SignUpRepeatPassword.setForeground(defaultTextColor);
                else
                    SignUpRepeatPassword.setForeground(Color.red);
            }
        });

        SignUpRepeatPassword.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(String.valueOf(SignUpPassword.getPassword()).equals(String.valueOf(SignUpRepeatPassword.getPassword())))
                    SignUpRepeatPassword.setForeground(defaultTextColor);
                else
                    SignUpRepeatPassword.setForeground(Color.red);
            }
        });

        SignUpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<ArrayList<String>> Emails = Selector.select("SELECT email FROM sysuser");
                ArrayList<ArrayList<String>> Logins = Selector.select("SELECT login FROM sysuser");
                ArrayList<ArrayList<String>> Pesels = Selector.select("SELECT pesel FROM sysuser");

                boolean errors = false;
                String errormessage = "";

                if(!RegexContainer.loginRegEx.matcher(SignUpLogin.getText()).matches())        // check login
                {
                    errors = true;
                    errormessage+="Login nieprawidłowy!\n" +
                        "Login musi:\n" +
                        "- składać się z cyfr oraz małych i wielkich liter\n" +
                        "- mieć 8-25 znaków.\n\n";
                }
                else
                {
                    for(ArrayList<String> iter : Logins)
                    {
                        String Login = SignUpLogin.getText();
                        if(iter.get(0).equalsIgnoreCase(Login))
                        {
                            errors = true;
                            errormessage+="Login jest już zajęty!\n\n";
                            break;
                        }
                    }
                }

                if(!RegexContainer.passwordRegEx.matcher(String.valueOf(SignUpPassword.getPassword())).matches())      // check password
                {
                    errors = true;
                    errormessage+="Hasło nieprawidłowe!\n" +
                            "Hasło musi:\n" +
                            "- zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę\n" +
                            "- mieć 8-25 znaków.\n\n";
                }
                else
                {
                    if (!String.valueOf(SignUpPassword.getPassword()).equals(String.valueOf(SignUpRepeatPassword.getPassword())))    // check repeat password
                    {
                        errors = true;
                        errormessage += "Hasła się nie zgadzają!\n\n";
                    }
                }

                if(!RegexContainer.nameRegEx.matcher(SignUpName.getText()).matches())      // check name
                {
                    errors = true;
                    errormessage+="Imię jest nieprawidłowe!\n\n";
                }

                if(!RegexContainer.nameRegEx.matcher(SignUpSurname.getText()).matches())   // check surname
                {
                    errors = true;
                    errormessage+="Nazwisko jest nieprawidłowe!\n\n";
                }

                if(!RegexContainer.emailRegEx.matcher(SignUpEmail.getText()).matches())    // check email
                {
                    errors = true;
                    errormessage+="Email jest nieprawidłowy!\n\n";
                }
                else
                {
                    for(ArrayList<String> iter : Emails)
                    {
                        String Email = SignUpEmail.getText();
                        if (iter.get(0).equalsIgnoreCase(Email))
                        {
                            errors = true;
                            errormessage+="Email jest już zajęty!\n\n";
                            break;
                        }
                    }
                }

                if(SignUpPesel.getText().length()!=0 && !RegexContainer.peselRegEx.matcher(SignUpPesel.getText()).matches())   // check pesel
                {
                    errors = true;
                    errormessage+="Pesel jest nieprawidłowy!\n\n";
                }
                else
                {
                    if(SignUpPesel.getText().length()!=0)
                    {
                        for (ArrayList<String> iter : Pesels)
                        {
                            String Pesel = SignUpPesel.getText();
                            if (iter.get(0)!=null && iter.get(0).equals(Pesel))
                            {
                                errors = true;
                                errormessage+="Pesel jest już zajęty!\n\n";
                                break;
                            }
                        }
                    }
                }

                if(SignUpStreetAddress.getText().length()!=0  && !RegexContainer.streetAdressRegEx.matcher(SignUpStreetAddress.getText()).matches())
                {
                    errors = true;
                    errormessage+="Adres jest nieprawidłowy!\n\n";
                }

                if(SignUpPostCode.getText().length()!=0 && !RegexContainer.postCodeRegEx.matcher(SignUpPostCode.getText()).matches())
                {
                    errors = true;
                    errormessage+="Kod pocztowy jest nieprawidłowy!\n\n";
                }

                if(SignUpTown.getText().length()!=0     && !RegexContainer.townRegEx.matcher(SignUpTown.getText()) .matches())
                {
                    errors = true;
                    errormessage+="Miejscowość jest nieprawidłowa!\n\n";
                }

                if(errors)
                {
                    JOptionPane.showMessageDialog(frame, errormessage, "Błąd", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                else
                {
                    String login = SignUpLogin.getText();
                    String password = String.valueOf(SignUpPassword.getPassword());
                    String SHApassword = String.format("%032x", new BigInteger(1, digest.digest(password.getBytes(StandardCharsets.UTF_8))));

                    String name = SignUpName.getText();
                    String surname = SignUpSurname.getText();
                    String email = SignUpEmail.getText();

                    QueryHandler.execute("INSERT INTO SysUser (login, passwd, name, surname, email) VALUES " +
                            "('" + login + "', '" + SHApassword + "', '" + name + "', '" + surname + "', '" + email + "');");

                    if(SignUpPesel.getText().length()!=0)
                        QueryHandler.execute("UPDATE SysUser SET pesel = " + SignUpPesel.getText() + " WHERE login = '" + login + "';");

                    if(SignUpStreetAddress.getText().length()!=0)
                        QueryHandler.execute("UPDATE SysUser SET streetaddress = '" + SignUpStreetAddress.getText() + "' WHERE login = '" + login + "';");

                    if(SignUpPostCode.getText().length()!=0)
                        QueryHandler.execute("UPDATE SysUser SET PostCode = '" + SignUpPostCode.getText() + "' WHERE login = '" + login + "';");

                    if(SignUpTown.getText().length()!=0)
                        QueryHandler.execute("UPDATE SysUser SET town = '" + SignUpTown.getText() + "' WHERE login = '" + login + "';");

                    JOptionPane.showMessageDialog(frame, "Dodano nowego użytownika!", "Operacja Pomyślna", JOptionPane.PLAIN_MESSAGE);
                    logger.trace("Created new user " + login);

                    SignUpLogin.setText("");
                    SignUpPassword.setText("");
                    SignUpRepeatPassword.setText("");
                    SignUpName.setText("");
                    SignUpSurname.setText("");
                    SignUpEmail.setText("");
                    SignUpPesel.setText("");
                    SignUpStreetAddress.setText("");
                    SignUpStreetAddress.setText("");

                    frame.setSize(WindowWidth, StartScreenHeight);
                    frame.setTitle("Zaloguj lub zarejestruj się");
                    cards.show(Cards, "StartScreen");
                    logInButton.requestFocus();
                }
            }
        });

        BackButton1.addActionListener(BackButtonListener);
    }

    private void createUIComponents()
    {
        ImageConference = new JLabel(new ImageIcon("Conference.png"));
        ImageKey = new JLabel(new ImageIcon("Key.png"));
        ImageAdd = new JLabel(new ImageIcon("Add.png"));
    }
}
