package GUI;

import SQLhandling.Selector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class ManageUsersPanel
{
    private JTable UsersTable;
    private JTextField UsersSearchField;
    private DefaultTableModel UsersTableModel;
    private JFrame MainWindowFrame;
    private MainWindow MainInstance;
    private ManageUsersPanel ManageUsersPanelInstance = this;

    ManageUsersPanel(JTable usersTable, JTextField usersSearchField, DefaultTableModel usersTableModel, JFrame frame, MainWindow mainInstance)
    {
        UsersTable = usersTable;
        UsersSearchField = usersSearchField;
        UsersSearchField.addKeyListener(UsersSearchFieldListener);
        UsersTableModel = usersTableModel;
        MainWindowFrame = frame;
        MainInstance = mainInstance;
    }

    KeyListener UsersSearchFieldListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e)
        {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) UsersTable.getModel());
            sorter.setRowFilter(RowFilter.regexFilter(UsersSearchField.getText()));
            UsersTable.setRowSorter(sorter);
        }
    };

    MouseAdapter UsersTableListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent evt)
        {
            int row = UsersTable.convertRowIndexToModel(UsersTable.rowAtPoint(evt.getPoint()));
            ArrayList<String> ManagedUserValues = Selector.select("Select * FROM sysuser LIMIT " + row +  ", 1;").get(0);

            new UserDetails(MainWindowFrame, ManagedUserValues, MainInstance, ManageUsersPanelInstance);
        }
    };

    void UpdateUsersTable()
    {
        UsersTableModel.setNumRows(0);

        ArrayList<ArrayList<String>> UsersList = Selector.select("SELECT login, isAuthor, isReviewer, isRedactor, isAdmin, userID FROM SysUser");
        for(ArrayList<String> iter : UsersList)
            UsersTableModel.addRow(new Vector<String>(iter));
    }
}
