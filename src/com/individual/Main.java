package com.individual;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

class Individual {
    MenuEngine menuEngine;
    workWithFile fileAct;
    JFrame frame, frameFind, frameEdit;
    JTable tableExcel;
    JTextArea number, name;
    JScrollPane panelForExcel;
    JButton finder, write, add, findRace, findName, find, edit;
    static JPanel panel;
    JMenuBar menuBar, mb;
    public JTextField[] textField;
    JTabbedPane choosePanel;
    JMenuItem saveFile, saveFileAs, openFile, exit;
    JMenu reports, admin, fileMenu;

    Individual() throws IOException {
        fileAct = new workWithFile(this);
        frame = new JFrame("Система управления");
        frameFind = new JFrame("Поиск");
        frameEdit = new JFrame("Редактирование");
        panel = new JPanel();
        menuBar = new JMenuBar();
        number = new JTextArea();
        name = new JTextArea();
        finder = new JButton();
        write = new JButton("Записать");
        add = new JButton("Добавить");
        saveFile = new JMenuItem("Сохранить файл");
        KeyStroke keyStrokeSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        saveFile.setAccelerator(keyStrokeSave);
        saveFile.add(new JSeparator(SwingConstants.HORIZONTAL));
        saveFileAs = new JMenuItem("Сохранить как");
        openFile = new JMenuItem("Открыть файл");
        KeyStroke keyStrokeOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        openFile.setAccelerator(keyStrokeOpen);
        find = new JButton("Поиск");
        edit = new JButton("Редактирование");
        reports = new JMenu("Просмотр отчётов");
        admin = new JMenu("Администрирование");
        exit = new JMenuItem("Выход");
        fileMenu = new JMenu("Файл");
        findRace = new JButton("Найти");
        findName = new JButton("Найти");
        mb = new JMenuBar();
        menuEngine = new MenuEngine(this, fileAct);
        textField = new JTextField[6];
        choosePanel  = new JTabbedPane();
        menuBar.add(fileMenu);
        menuBar.add(reports);
        menuBar.add(admin);
        for (JTextField text : textField) text = new JTextField(15);
        tableExcel = new JTable(15, 6) {
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        panelForExcel = new JScrollPane(tableExcel);
        panelForExcel.setOpaque(true);
        panelForExcel.setPreferredSize(new Dimension(550, 150));
        panelForExcel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelForExcel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelForExcel.setVisible(true);
        menuBar.add(exit);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveFileAs);
        openFile.addActionListener(menuEngine);
        saveFile.addActionListener(menuEngine);
        saveFileAs.addActionListener(menuEngine);
        finder.addActionListener(menuEngine);
        write.addActionListener(menuEngine);
        add.addActionListener(menuEngine);
        exit.addActionListener(menuEngine);
        edit.addActionListener(menuEngine);
        find.addActionListener(menuEngine);
        findRace.addActionListener(menuEngine);
        findName.addActionListener(menuEngine);
        panel.add(panelForExcel);
        panel.add(find, "North");
        panel.add(edit);
        createTabbedPane();
    }

    public void createTabbedPane() {
        JLabel numberField = new JLabel("Введите название страны");
        JLabel nameField = new JLabel("Введите название спорта");
        number.setPreferredSize(new Dimension(70, 190));
        name.setPreferredSize(new Dimension(70, 190));
        frameFind.setSize(350, 300);
        frameFind.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frameFind.setResizable(false);
        frameFind.setVisible(false);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(numberField, "North");
        panel1.add(number, "Center");
        panel1.add(findRace, "South");
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.add(nameField, "North");
        panel2.add(name, "Center");
        panel2.add(findName, "South");
        choosePanel.add(panel1, "Название страны");
        choosePanel.add(panel2, "Название спорта");
        frameFind.add(choosePanel);
    }

    public void createFindWindow() {
        String[] items = {"Номер1", "Номер2", "Номер3"};
        String[] labelText = {"Название страны", "Название спорта", "Кол-во медалей"};
        JPanel panel = new JPanel();
        frameEdit = new JFrame("Редактирование");
        frameEdit.add(panel);
        frameEdit.setSize(350, 300);
        frameEdit.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frameEdit.setResizable(false);
        frameEdit.setVisible(true);
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        JLabel[] labels = new JLabel[6];
        int pad = 25;
        panel.add(finder);
        layout.putConstraint(SpringLayout.NORTH, finder, 25, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, finder, 290, SpringLayout.WEST, panel);
        finder.setPreferredSize(new Dimension(20, 20));
        for (int i = 0; i < textField.length; i++) {
            labels[i] = new JLabel(labelText[i]);
            textField[i] = new JTextField(15);
            labels[i].setVisible(true);
            textField[i].setVisible(true);
            panel.add(labels[i]);
            panel.add(textField[i]);
            layout.putConstraint(SpringLayout.WEST, labels[i], 10, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.NORTH, labels[i], pad, SpringLayout.NORTH, panel);
            layout.putConstraint(SpringLayout.NORTH, textField[i], pad, SpringLayout.NORTH, panel);
            pad += 30;
            layout.putConstraint(SpringLayout.WEST, textField[i], 130, SpringLayout.WEST, panel);
        }
        panel.add(write);
        panel.add(add);
        layout.putConstraint(SpringLayout.WEST, write, 30, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, write, -55, SpringLayout.SOUTH, panel);
        layout.putConstraint(SpringLayout.WEST, add, 130, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, add, -55, SpringLayout.SOUTH, panel);
        //panel.add(add, SpringLayout.NORTH);
    }

    private void run() throws IOException {
        frame.setVisible(true);
        panel.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("MenuBar.background", Color.lightGray);// цвет для верхней панели
        frame.setJMenuBar(mb);
        frame.setJMenuBar(menuBar);
        frame.setSize(620, 350);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fileAct.parseTXTFile();
    }

   /* private void choose() throws IOException, InvalidFormatException {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialog = JOptionPane.showConfirmDialog(null, "Необходимо выбрать базу данных!", "Выбор", dialogButton);
        if (dialog == JOptionPane.YES_OPTION) {
            fileAct.getPathFile(2);
            if (fileAct.getFileFormat() != null) {
                run();
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка. Файл не выбран", "Ошибка", 0);
                choose();
            }
        } else {
            frame.dispose();
        }

    }

    */

    public static void main(final String[] args) throws IOException, InvalidFormatException {
        Individual ind = new Individual();
        ind.run();

    }
}
// h
