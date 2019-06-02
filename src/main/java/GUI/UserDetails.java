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

public class UserDetails
{
    private MainWindow MainInstance;
    private JFrame frame;
    private JFrame parentFrame;
    private ArrayList<String> ManagedUserValues;
    private ManageUsersPanel ManageUsersPanelInstance;

    private int articlesAsAuthorCount;
    private int articlesAsRedactorCount;
    private int reviewsCount;
    private int pathsCount;

    private JCheckBox authorCheckBox;
    private JCheckBox adminCheckBox;
    private JCheckBox redactorCheckBox;
    private JCheckBox reviewerCheckBox;
    private JLabel userLogin;
    private JLabel userTown;
    private JLabel userPostcode;
    private JLabel userStreetAdress;
    private JLabel userPesel;
    private JLabel userEmail;
    private JLabel userSurname;
    private JLabel userName;
    private JButton saveChangesButton;
    private JButton deleteUserAccountButton;
    private JButton changeUserPasswordButton;
    private JPasswordField newPasswordField;
    private JPanel UserDetailsPanel;
    private JPasswordField newPasswordRepeatField;
    private JLabel warningsLabel;
    private Font labelfont;
    private MessageDigest digest;

    private final Logger logger = LogManager.getLogger(UserDetails.class);
    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};

    private void checkFunctionChange()
    {
        if(authorCheckBox.isSelected() == ManagedUserValues.get(10).equals("1") && reviewerCheckBox.isSelected() == ManagedUserValues.get(11).equals("1")
                && redactorCheckBox.isSelected() == ManagedUserValues.get(12).equals("1") && adminCheckBox.isSelected() == ManagedUserValues.get(13).equals("1"))
            saveChangesButton.setEnabled(false);
        else
            saveChangesButton.setEnabled(true);
    }

    public UserDetails(JFrame ParentFrame, ArrayList<String> managedUserValues, MainWindow mainInstance, final ManageUsersPanel manageUsersPanelInstance)
    {
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        MainInstance = mainInstance;
        ManageUsersPanelInstance = manageUsersPanelInstance;
        ManagedUserValues = managedUserValues;

        ArrayList<ArrayList<String>> articlesAsAuthor = Selector.select("SELECT COUNT(articleID) FROM article where AuthorID = " + ManagedUserValues.get(0) + ";");
        ArrayList<ArrayList<String>> articlesAsRedactor = Selector.select("SELECT COUNT(articleID) FROM article where RedactorID = " + ManagedUserValues.get(0) + ";");
        final ArrayList<ArrayList<String>> reviews = Selector.select("SELECT COUNT(reviewID) FROM review where ReviewerID = " + ManagedUserValues.get(0) + ";");
        ArrayList<ArrayList<String>> paths = Selector.select("SELECT COUNT(pathID) FROM path where redactorID = " + ManagedUserValues.get(0) + ";");

        articlesAsAuthorCount = Integer.parseInt(articlesAsAuthor.get(0).get(0));
        articlesAsRedactorCount = Integer.parseInt(articlesAsRedactor.get(0).get(0));
        reviewsCount = Integer.parseInt(reviews.get(0).get(0));
        pathsCount = Integer.parseInt(paths.get(0).get(0));

        labelfont = userLogin.getFont();   // get standard fon for labels
        parentFrame = ParentFrame;
        frame = new JFrame("Zarządzanie użytkownikiem");
        frame.setContentPane(this.UserDetailsPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(parentFrame);
        parentFrame.setEnabled(false);

        userLogin.setText(ManagedUserValues.get(1));
        userName.setText(ManagedUserValues.get(3));
        userSurname.setText(ManagedUserValues.get(4));
        userEmail.setText(ManagedUserValues.get(5));
        userPesel.setText(ManagedUserValues.get(6));
        userStreetAdress.setText(ManagedUserValues.get(7));
        userPostcode.setText(ManagedUserValues.get(8));
        userTown.setText(ManagedUserValues.get(9));

        authorCheckBox.setSelected(ManagedUserValues.get(10).equals("1"));
        reviewerCheckBox.setSelected(ManagedUserValues.get(11).equals("1"));
        redactorCheckBox.setSelected(ManagedUserValues.get(12).equals("1"));
        adminCheckBox.setSelected(ManagedUserValues.get(13).equals("1"));

        newPasswordField.setToolTipText("Hasło musi zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę i mieć 8-25 znaków.");
        newPasswordRepeatField.setToolTipText("Wpisz hasło identyczne z tym w polu \"Hasło\"");

        String warning = "";
        if(articlesAsAuthorCount!=0)    warning += "Do konta jest przypisanych " + articlesAsAuthorCount + " artykułów jako autor\n";
        if(articlesAsRedactorCount!=0)  warning += "Do konta jest przypisanych " + articlesAsRedactorCount + " artykułów jako redaktor\n";
        if(reviewsCount!=0)             warning += "Do konta jest przypisanych " + reviewsCount + " recenzji\n";
        if(pathsCount!=0)               warning += "Do konta jest przypisanych " + pathsCount + " ścieżek\n";

        if(!warning.equals(""))
        {
            // make text multi line using html
            warning = warning.replaceAll("\n", "<br>");
            warning = "<html>" + warning + "</html>";
            warningsLabel.setText(warning);
        }

        if(ManagedUserValues.get(1).equals("admin"))
        {
            adminCheckBox.setEnabled(false);
            adminCheckBox.setToolTipText("Ta funkcja nie może zostać zmieniona dla konta\"admin\"");
            deleteUserAccountButton.setEnabled(false);
            adminCheckBox.setToolTipText("Konto \"admin\" nie może zostać usunięte!");
            warningsLabel.setText("Konto \"admin\" nie może zostać usunięte!");
        }

        frame.addWindowListener(new WindowAdapter()     // closing window - activating main window again
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                frame.dispose();
                parentFrame.setEnabled(true);
                parentFrame.setVisible(true);
            }
        });

        // change user functions

        authorCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                checkFunctionChange();
                if(authorCheckBox.isSelected() == ManagedUserValues.get(10).equals("1"))
                    authorCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                else
                    authorCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() | Font.BOLD));

            }
        });

        reviewerCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                checkFunctionChange();
                if(reviewerCheckBox.isSelected() == ManagedUserValues.get(11).equals("1"))
                    reviewerCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                else
                    reviewerCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() | Font.BOLD));
            }
        });

        redactorCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                checkFunctionChange();
                if(redactorCheckBox.isSelected() == ManagedUserValues.get(12).equals("1"))
                    redactorCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                else
                    redactorCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() | Font.BOLD));
            }
        });

        adminCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                checkFunctionChange();
                if(adminCheckBox.isSelected() == ManagedUserValues.get(13).equals("1"))
                    adminCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                else
                    adminCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() | Font.BOLD));
            }
        });

        saveChangesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String warning = "";
                if(!authorCheckBox.isSelected() && articlesAsAuthorCount!=0)       warning += "Do konta jest przypisanych " + articlesAsAuthorCount + " artykułów jako autor\n";
                if(!redactorCheckBox.isSelected() && articlesAsRedactorCount!=0)   warning += "Do konta jest przypisanych " + articlesAsRedactorCount + " artykułów jako redaktor\n";
                if(!reviewerCheckBox.isSelected() && reviewsCount!=0)              warning += "Do konta jest przypisanych " + reviewsCount + " recenzji\n";
                if(!redactorCheckBox.isSelected() && pathsCount!=0)                warning += "Do konta jest przypisanych " + pathsCount + " ścieżek\n";
                if(!warning.equals(""))
                    warning = "\n\nUwaga:\n" + warning;

                if(JOptionPane.showOptionDialog(frame, "Zmienić funkcje użytkownika " + ManagedUserValues.get(1) + "?" + warning, "Potwierdź operację",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, confirmOptions, confirmOptions[1])==JOptionPane.YES_OPTION)
                {
                    String isAuthor, isReviewer, isRedactor, isAdmin;
                    isAuthor  =  (authorCheckBox.isSelected() ? "1" : "0");
                    isReviewer = (reviewerCheckBox.isSelected() ? "1" : "0");
                    isRedactor = (redactorCheckBox.isSelected() ? "1" : "0");
                    isAdmin =    (adminCheckBox.isSelected() ? "1" : "0");

                    QueryHandler.execute("UPDATE sysuser SET" +
                            " isAuthor = " + isAuthor +
                            ", isReviewer = " + isReviewer +
                            ", isRedactor = " + isRedactor +
                            ", isAdmin = " + isAdmin +
                            " WHERE login = '" + ManagedUserValues.get(1) + "';");

                    ManagedUserValues = Selector.select("Select * FROM sysuser WHERE login = '" + ManagedUserValues.get(1) + "';").get(0);
                    checkFunctionChange();
                    authorCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                    reviewerCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                    redactorCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));
                    adminCheckBox.setFont(labelfont.deriveFont(labelfont.getStyle() & ~Font.BOLD));

                    //creating log
                    String functionset = "";
                    if (authorCheckBox.isSelected()) functionset += "author, ";
                    if (reviewerCheckBox.isSelected()) functionset += "reviewer, ";
                    if (redactorCheckBox.isSelected()) functionset += "redactor, ";
                    if (adminCheckBox.isSelected()) functionset += "admin, ";
                    functionset = functionset.replaceAll(", $", "");
                    if (functionset.equals("")) functionset = "none";

                    logger.trace("Admin " + CurrentUser.getLogin() + " changed " + ManagedUserValues.get(1) + " functions to: (" + functionset + ")");

                    if(CurrentUser.getLogin().equals(ManagedUserValues.get(1))) // update enabled cards if admin changed his own functions
                    {
                        ArrayList<ArrayList<String>> result = Selector.select("SELECT * FROM SysUser " + "WHERE login = '" + CurrentUser.getLogin() + "';");
                        CurrentUser.setValues(result.get(0));
                        MainInstance.updateAvailableCards(4);
                        if(!CurrentUser.isAdmin())
                            MainInstance.changeActiveCard(0);
                        ManageUsersPanelInstance.UpdateUsersTable();
                    }
                }
            }
        });

        // delete account

        deleteUserAccountButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String warning = "";
                if(articlesAsAuthorCount!=0)    warning += "Do konta jest przypisanych " + articlesAsAuthorCount + " artykułów jako autor\n";
                if(articlesAsRedactorCount!=0)  warning += "Do konta jest przypisanych " + articlesAsRedactorCount + " artykułów jako redaktor\n";
                if(reviewsCount!=0)             warning += "Do konta jest przypisanych " + reviewsCount + " recenzji\n";
                if(pathsCount!=0)               warning += "Do konta jest przypisanych " + pathsCount + " ścieżek\n";
                if(CurrentUser.getLogin().equals(ManagedUserValues.get(1))) warning += "Czy chcesz usunąc swoje konto?\n";
                if(!warning.equals(""))
                    warning = "\n\nUsunięcie konta może sprawić problemy:\n" + warning;

                if(JOptionPane.showOptionDialog(frame, "Usunąć konto? Tej operacji NIE MOŻNA COFNĄĆ!"+warning, "Potwierdź operację",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, confirmOptions, confirmOptions[1])==JOptionPane.YES_OPTION)
                {
                    logger.trace("Admin " + CurrentUser.getLogin() + " removed account of user " + ManagedUserValues.get(1));
                    QueryHandler.execute("DELETE FROM sysuser where login ='"+ ManagedUserValues.get(1) +"';");

                    if(!CurrentUser.getLogin().equals(ManagedUserValues.get(1)))    // deleted other user account
                    {
                        JOptionPane.showMessageDialog(frame, "Konto użytkownika " + ManagedUserValues.get(1) + "zostało usunięte", "Operacja przebiegła pomyślnie", JOptionPane.PLAIN_MESSAGE);
                        frame.dispose();
                        ManageUsersPanelInstance.UpdateUsersTable();
                        parentFrame.setEnabled(true);
                        parentFrame.setVisible(true);
                    }
                    else    // deleted their own account
                    {
                        CurrentUser.setValues(null);
                        new LoginForm(parentFrame);
                        parentFrame.dispose();
                        frame.dispose();
                        JOptionPane.showMessageDialog(frame, "Twoje konto zostało usunięte.", "Operacja przebiegła pomyślnie", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });

        newPasswordField.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(RegexContainer.passwordRegEx.matcher(String.valueOf(newPasswordField.getPassword())).matches())
                    newPasswordField.setForeground(Color.black);
                else
                    newPasswordField.setForeground(Color.red);

                if(String.valueOf(newPasswordField.getPassword()).equals(String.valueOf(newPasswordRepeatField.getPassword())))
                    newPasswordRepeatField.setForeground(Color.black);
                else
                    newPasswordRepeatField.setForeground(Color.red);

                changeUserPasswordButton.setEnabled(!String.valueOf(newPasswordField.getPassword()).isEmpty() || !String.valueOf(newPasswordRepeatField.getPassword()).isEmpty());
            }
        });

        newPasswordRepeatField.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(String.valueOf(newPasswordField.getPassword()).equals(String.valueOf(newPasswordRepeatField.getPassword())))
                    newPasswordRepeatField.setForeground(Color.black);
                else
                    newPasswordRepeatField.setForeground(Color.red);

                changeUserPasswordButton.setEnabled(!String.valueOf(newPasswordField.getPassword()).isEmpty() || !String.valueOf(newPasswordRepeatField.getPassword()).isEmpty());
            }
        });

        changeUserPasswordButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!RegexContainer.passwordRegEx.matcher(String.valueOf(newPasswordField.getPassword())).matches())
                {
                    String message ="Hasło nieprawidłowe!\n" +
                            "Hasło musi:\n" +
                            "- zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę\n" +
                            "- mieć 8-25 znaków.\n\n";

                    JOptionPane.showMessageDialog(frame, message, "Błąd", JOptionPane.PLAIN_MESSAGE);
                }
                else
                {
                    if (!String.valueOf(newPasswordField.getPassword()).equals(String.valueOf(newPasswordRepeatField.getPassword())))    // check repeat password
                        JOptionPane.showMessageDialog(frame, "Hasła się nie zgadzają!\n", "Błąd", JOptionPane.PLAIN_MESSAGE);
                    else
                    {
                        String newPass = String.valueOf(newPasswordField.getPassword());
                        String SHAnewPass = String.format("%032x", new BigInteger(1, digest.digest(newPass.getBytes(StandardCharsets.UTF_8))));

                        QueryHandler.execute("UPDATE sysuser SET passwd = '" + SHAnewPass + "' WHERE login = '" + ManagedUserValues.get(1) + "';");
                        if(CurrentUser.getLogin().equals(ManagedUserValues.get(1)))
                            CurrentUser.setPasswordHash(SHAnewPass);
                        logger.trace("Admin " + CurrentUser.getLogin() + " changed password of " + ManagedUserValues.get(1));

                        JOptionPane.showMessageDialog(frame, "Hasło użytkownika " + ManagedUserValues.get(1) + "zostało zmienione" , "Operacja pomyślna", JOptionPane.PLAIN_MESSAGE);
                        newPasswordField.setText("");
                        newPasswordRepeatField.setText("");
                        changeUserPasswordButton.setEnabled(false);
                    }
                }
            }
        });
    }
}
