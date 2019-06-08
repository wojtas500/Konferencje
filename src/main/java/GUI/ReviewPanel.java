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

public class ReviewPanel
{
    private JTable ReviewTable;
    private JTextField ReviewSearchField;
    private DefaultTableModel ReviewTableModel;
    private JFrame MainWindowFrame;
    private MainWindow MainInstance;
    private ReviewPanel ReviewPanelInstance = this;

    ReviewPanel(JTable reviewTable, JTextField reviewSearchField, DefaultTableModel reviewTableModel, JFrame frame, MainWindow mainInstance)
    {
        ReviewTable = reviewTable;
        ReviewSearchField = reviewSearchField;
        ReviewSearchField.addKeyListener(ReviewSearchFieldListener);
        ReviewTableModel = reviewTableModel;
        MainWindowFrame = frame;
        MainInstance = mainInstance;
    }

    KeyListener ReviewSearchFieldListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e)
        {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) ReviewTable.getModel());
            sorter.setRowFilter(RowFilter.regexFilter(ReviewSearchField.getText()));
            ReviewTable.setRowSorter(sorter);
        }
    };
/*
    MouseAdapter ReviewTableListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent evt)
        {
            int row = ReviewTable.convertRowIndexToModel(ReviewTable.rowAtPoint(evt.getPoint()));
            ArrayList<String> ReviewValues = Selector.select("Select * FROM Review LIMIT " + row +  ", 1;").get(0);

        }
    };*/

    void UpdateReviewTable()
    {
        ReviewTableModel.setNumRows(0);

        ArrayList<ArrayList<String>> ReviewList = Selector.select("SELECT ReviewerID, title, reviewID FROM Review");
        for(ArrayList<String> iter : ReviewList)
            ReviewTableModel.addRow(new Vector<String>(iter));
    }
}
