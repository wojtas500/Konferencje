package GUI;

import SQLhandling.Selector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

public class ArticlePanel
{
    private JTable ArticleTable;
    private JTextField ArticleSearchField;
    private DefaultTableModel ArticleTableModel;
    private JFrame MainWindowFrame;
    private MainWindow MainInstance;
    private ArticlePanel ArticlePanelInstance = this;

    ArticlePanel(JTable articleTable, JTextField articleSearchField, DefaultTableModel articleTableModel, JFrame frame, MainWindow mainInstance)
    {
        ArticleTable = articleTable;
        ArticleSearchField = articleSearchField;
        ArticleSearchField.addKeyListener(ArticleSearchFieldListener);
        ArticleTableModel = articleTableModel;
        MainWindowFrame = frame;
        MainInstance = mainInstance;
    }

    KeyListener ArticleSearchFieldListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e)
        {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) ArticleTable.getModel());
            sorter.setRowFilter(RowFilter.regexFilter(ArticleSearchField.getText()));
            ArticleTable.setRowSorter(sorter);
        }
    };
/*
    MouseAdapter ArticleTableListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent evt)
        {
            int row = ArticleTable.convertRowIndexToModel(ArticleTable.rowAtPoint(evt.getPoint()));
            ArrayList<String> ArticleValues = Selector.select("Select * FROM Article LIMIT " + row +  ", 1;").get(0);

        }
    };*/

    void UpdateReviewTable()
    {
        ArticleTableModel.setNumRows(0);

        ArrayList<ArrayList<String>> ArticleList = Selector.select("SELECT AuthorID, title, articleID FROM Article");
        for(ArrayList<String> iter : ArticleList)
            ArticleTableModel.addRow(new Vector<String>(iter));
    }
}
