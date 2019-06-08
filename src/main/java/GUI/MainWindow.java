package GUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class MainWindow
{
    private JFrame frame;
    private MainWindow mainInstance = this;
    private JPanel MainPanel, SidePanel, Cards, MyAccountPanel, MyReviewsPanel, RedactedArticlesPanel, MyArticlesPanel, ManageArticlesPanel, ManageUsersPanel;
    private JButton myAccountButton, myArticlesButton, myReviewsButton, redactedArticlesButton, manageUsersButton, manageArticlesButton, wylogujButton;
    private JSeparator Separator;
    private final CardLayout cards = (CardLayout) (Cards.getLayout());

    //My Account
    private JLabel currLogin, currName, currSurname, currEmail, currPesel, currStreetAddress, currPostCode, currTown;
    private JTextField newLogin, newName, newSurname, newEmail, newPesel, newStreetAddress, newPostCode, newTown;
    private JPasswordField newPassword, newPasswordRepeat, oldPasswordField;
    private JButton changeDataButton;

    private JPasswordField confirmDelete;
    private JButton removeAccountButton;
    private JLabel warningsLabel;

    //Manage Users
    private JTable UsersTable;
    private JTextField UsersSearchField;
    private JPanel WelcomeScreen;
    private DefaultTableModel UsersTableModel;

    private JTable ReviewTable;
    private JTextField ReviewSearchField;
    private DefaultTableModel ReviewTableModel;

    private JTable ArticleTable;
    private JTextField ArticleSearchField;
    private DefaultTableModel ArticleTableModel;

    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};
    private final Logger logger = LogManager.getLogger(MainWindow.class);

    void updateAvailableCards(int currentcard)
    {
        myAccountButton.setEnabled(0!=currentcard);
        myArticlesButton.setEnabled(1!=currentcard && CurrentUser.isAuthor());
        myReviewsButton.setEnabled(2!=currentcard && CurrentUser.isReviewer());
        redactedArticlesButton.setEnabled(3!=currentcard && CurrentUser.isRedactor());
        manageUsersButton.setEnabled(4!=currentcard && CurrentUser.isAdmin());
        manageArticlesButton.setEnabled(5!=currentcard && CurrentUser.isAdmin());
    }

    void changeActiveCard(int card)     //  0-5 (so far)
    {
        myAccountButton.setEnabled(0!=card);
        myArticlesButton.setEnabled(1!=card && CurrentUser.isAuthor());
        myReviewsButton.setEnabled(2!=card && CurrentUser.isReviewer());
        redactedArticlesButton.setEnabled(3!=card && CurrentUser.isRedactor());
        manageUsersButton.setEnabled(4!=card && CurrentUser.isAdmin());
        manageArticlesButton.setEnabled(5!=card && CurrentUser.isAdmin());

        myAccountButton.setBackground( 0==card ? Color.WHITE : null );
        myArticlesButton.setBackground( 1==card ? Color.WHITE : null );
        myReviewsButton.setBackground( 2==card ? Color.WHITE: null );
        redactedArticlesButton.setBackground( 3==card ? Color.WHITE: null );
        manageUsersButton.setBackground( 4==card ? Color.WHITE: null );
        manageArticlesButton.setBackground( 5==card ? Color.WHITE: null );

        if(card==0) frame.setTitle("Moje konto");
        if(card==1) frame.setTitle("Moje artykuły");
        if(card==2) frame.setTitle("Moje recenzje");
        if(card==3) frame.setTitle("Redagowane artykuły");
        if(card==4) frame.setTitle("Zarządzaj użytkownikami");
        if(card==2) frame.setTitle("Moje recenzje");

        if(card==0) cards.show(Cards, "MyAccountPanel");
        if(card==1) cards.show(Cards, "MyArticles");
        if(card==2) cards.show(Cards, "MyReviews");
        if(card==3) cards.show(Cards, "RedactedArticles");
        if(card==4) cards.show(Cards, "ManageUsers");
        if(card==5) cards.show(Cards, "ManageArticles");
    }

    public MainWindow(JFrame parentFrame)
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

        Cards.add(WelcomeScreen, "WelcomeScreen");
        Cards.add(MyAccountPanel, "MyAccountPanel");
        Cards.add(MyArticlesPanel, "MyArticles");
        Cards.add(MyReviewsPanel, "MyReviews");
        Cards.add(RedactedArticlesPanel, "RedactedArticles");
        Cards.add(ManageUsersPanel, "ManageUsers");
        Cards.add(ManageArticlesPanel, "ManageArticles");

        // initial card
        cards.show(Cards, "WelcomeScreen");
        myAccountButton.setEnabled(true);
        myAccountButton.setEnabled(true);
        myArticlesButton.setEnabled(CurrentUser.isAuthor());
        myReviewsButton.setEnabled(CurrentUser.isReviewer());
        redactedArticlesButton.setEnabled(CurrentUser.isRedactor());
        manageUsersButton.setEnabled(CurrentUser.isAdmin());
        manageArticlesButton.setEnabled(CurrentUser.isAdmin());

        myAccountButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(0);
            }
        });

        myArticlesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(1);
            }
        });

        myReviewsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(2);
            }
        });

        redactedArticlesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeActiveCard(3);
            }
        });

        manageUsersButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(4);
            }
        });

        manageArticlesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                changeActiveCard(5);
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
                    new LoginForm(frame);
                    frame.dispose();
                }
            }
        });

        // MOJE KONTO

        MyAccountPanel myAccountPanel = new MyAccountPanel(frame, currLogin, currName, currSurname, currEmail, currPesel, currStreetAddress, currPostCode, currTown,
                newLogin, newName, newSurname, newEmail, newPesel, newStreetAddress, newPostCode,
                newTown, newPassword, newPasswordRepeat, oldPasswordField, changeDataButton,
                confirmDelete, removeAccountButton, warningsLabel);
        
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

        // MOJE ARTYKUŁY - DO POPRAWY
        /*  ArticlePanel articlePanel = new ArticlePanel(ArticleTable, ArticleSearchField, ArticleTableModel, frame, mainInstance);
        ArticlePanel.UpdateUsersTable();
        ArticleSearchField.addKeyListener(ArticlePanel.ArticleSearchFieldListener); */
            //TODO

        // MOJE RECENZJE - DO POPRAWY!!!
        /*ReviewPanel reviewPanel = new ReviewPanel(ReviewTable, ReviewSearchField, ReviewTableModel, frame, mainInstance);
        reviewPanel.UpdateReviewTable();
        ReviewSearchField.addKeyListener(reviewPanel.ReviewSearchFieldListener);*/
        //ReviewTable.addMouseListener(reviewPanel.ReviewTableListener);
            // TODO

        // REDAGOWANIE ARTYKUŁY

            // TODO

        // ZARZĄDZANIE UŻYKOWNIKAMI
        ManageUsersPanel manageUsersPanel = new ManageUsersPanel(UsersTable, UsersSearchField, UsersTableModel, frame, mainInstance);
        manageUsersPanel.UpdateUsersTable();
        UsersSearchField.addKeyListener(manageUsersPanel.UsersSearchFieldListener);
        UsersTable.addMouseListener(manageUsersPanel.UsersTableListener);

        // ZARZĄDZANIE ARTYKUŁAMI

            // TODO
    }

    private void createUIComponents()
    {
        UsersTable = new JTable();
        String[] UsersTableColumns = {"Nazwa użytkownika", "Autor", "Recenzent", "Redaktor", "Administrator"};
        UsersTable.setModel(new DefaultTableModel(UsersTableColumns,0));
        UsersTableModel = (DefaultTableModel) UsersTable.getModel();
        UsersTable.setDefaultEditor(Object.class, null);
        UsersTable.setAutoCreateRowSorter(true);

        ReviewTable = new JTable();
        String[] ReviewTableColumns = {"Autor recenzji", "Tytuł recenzji"};
        ReviewTable.setModel(new DefaultTableModel(ReviewTableColumns,0));
        ReviewTableModel = (DefaultTableModel) ReviewTable.getModel();
        ReviewTable.setDefaultEditor(Object.class, null);
        ReviewTable.setAutoCreateRowSorter(true);

        ArticleTable = new JTable();
        String[] ArticleTableColumns = {"Autor artykułu", "Tytuł artykułu"};
        ArticleTable.setModel(new DefaultTableModel(ArticleTableColumns,0));
        ArticleTableModel = (DefaultTableModel) ArticleTable.getModel();
        ArticleTable.setDefaultEditor(Object.class, null);
        ArticleTable.setAutoCreateRowSorter(true);
}
}
