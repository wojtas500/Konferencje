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
import java.util.ArrayList;

public class MainWindow
{
    private JFrame frame;

    private JPanel MyAccountPanel;
    private JPanel MyReviewsPanel;
    private JPanel MyArticlesPanel;
    private JPanel ManageUsersPanel;

    private JButton mojeKontoButton;
    private JButton mojeArtykułyButton;
    private JButton mojeRecenzjeButton;
    private JButton zarządzajUzytkownikamiButton;
    private JButton wylogujButton;
    private JPanel SidePanel;
    private JPanel Cards;
    private JSeparator Separator;

    private JPanel MainPanel;

    MyAccountPanel myAccountPanel;  // MyAccount controls

    private JLabel currLogin;
    private JLabel currName;
    private JLabel currSurname;
    private JLabel currEmail;
    private JLabel currPesel;
    private JLabel currStreetAddress;
    private JLabel currPostCode;
    private JLabel currTown;

    private JTextField newLogin;
    private JTextField newName;
    private JTextField newSurname;
    private JTextField newEmail;
    private JTextField newPesel;
    private JTextField newStreetAddress;
    private JTextField newPostCode;
    private JTextField newTown;
    private JPasswordField newPassword;
    private JPasswordField newPasswordRepeat;
    private JPasswordField oldPasswordField;
    private JButton changeDataButton;

    private JLabel warningsLabel;
    private JButton removeAccountButton;
    private JPasswordField confirmDelete;

    private final Color defaultTextColor = newLogin.getForeground();

    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};

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

            warningsLabel.setText(Warning);
            removeAccountButton.setEnabled(false);
            confirmDelete.setEnabled(false);
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

        if(card==0) cards.show(Cards, "MyAccountPanel");
        if(card==1) cards.show(Cards, "MyArticles");
        if(card==2) cards.show(Cards, "MyReviews");
        if(card==3) cards.show(Cards, "ManageUsers");
    }

    public MainWindow()
    {
        myAccountPanel = new MyAccountPanel(frame, currLogin, currName, currSurname, currEmail, currPesel, currStreetAddress, currPostCode, currTown,
                newLogin, newName, newSurname, newEmail, newPesel, newStreetAddress, newPostCode,
                newTown, newPassword, newPasswordRepeat, oldPasswordField, changeDataButton, warningsLabel,
                removeAccountButton, confirmDelete, defaultTextColor);

        Cards.add(MyAccountPanel, "MyAccountPanel");
        Cards.add(MyArticlesPanel, "MyArticles");
        Cards.add(MyReviewsPanel, "MyReviews");
        Cards.add(ManageUsersPanel, "ManageUsers");

        final CardLayout cards = (CardLayout) (Cards.getLayout());

        // initial card
        cards.show(Cards, "MyAccountPanel");
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
                frame.setSize(1000, 700);
                System.out.println(frame.toString());
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
                    new LoginForm().createWindow(frame);
                    frame.dispose();
                }
            }
        });

        // MOJE KONTO

        CheckDeletion();
        currLogin.setText(CurrentUser.getLogin());
        currName.setText(CurrentUser.getName());
        currSurname.setText(CurrentUser.getSurname());
        currEmail.setText(CurrentUser.getEmail());
        if(CurrentUser.getPesel()!=null)        currPesel.setText(CurrentUser.getPesel());
        if(CurrentUser.getStreetAdress()!=null) currStreetAddress.setText(CurrentUser.getStreetAdress());
        if(CurrentUser.getPostCode()!=null)     currPostCode.setText(CurrentUser.getPostCode());
        if(CurrentUser.getTown()!=null)         currTown.setText(CurrentUser.getTown());

        if(CurrentUser.getLogin().equals("admin"))
        {
            newLogin.setEnabled(false);
            newLogin.setToolTipText("Login dla tego konta nie może zostać zmieniony");
            removeAccountButton.setEnabled(false);
            confirmDelete.setEnabled(false);
            warningsLabel.setText("Konto \"admin\" nie może zostać usunięte!");
        }
        else    newLogin.setToolTipText("Login musi składać się z cyfr oraz małych i wielkich liter i mieć 8-25 znaków.");

        newPassword.setToolTipText("Hasło musi zawierać co najmniej jedną cyfrę, jedną wielką i jedną małą literę i mieć 8-25 znaków.");
        newPasswordRepeat.setToolTipText("Wpisz hasło identyczne z tym w polu \"Hasło\"");
        newPostCode.setToolTipText("dd-ddd gdzie \"d\" oznacza cyfrę");

        newLogin.addKeyListener(myAccountPanel.newLoginListener);
        newName.addKeyListener(myAccountPanel.newNameListener);
        newSurname.addKeyListener(myAccountPanel.newSurnameListener);
        newEmail.addKeyListener(myAccountPanel.newEmailListener);
        newPesel.addKeyListener(myAccountPanel.newPeselListener);
        newStreetAddress.addKeyListener(myAccountPanel.newStreetAddressListener);
        newPostCode.addKeyListener(myAccountPanel.newPostCodeListener);
        newTown.addKeyListener(myAccountPanel.newTownListener);
        newPassword.addKeyListener(myAccountPanel.newPasswordListener);
        newPasswordRepeat.addKeyListener(myAccountPanel.newPasswordRepeatListener);
        changeDataButton.addActionListener(myAccountPanel.changeDataButtonListener);
        removeAccountButton.addActionListener(myAccountPanel.removeAccountButtonListener);

        // MOJE ARTYKUŁY - przesłane i redagowane

            //TODO

        // MOJE RECENZJE

            // TODO

        // ZARZĄDZANIE UŻYKOWNIKAMI

            // TODO
    }

    public void createWindow(JFrame parentFrame)
    {
        frame = new JFrame("Moje konto");
        frame.setContentPane(this.MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 700);
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
    }
}
