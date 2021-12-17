package com.individual;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class workWithFile {
    Individual parent;
    private String[][] data;
    private String fileFormat;
    private String buffer = "";
    private File file;
    private int counter = 0;

    workWithFile(Individual parent) {
        this.parent = parent;

    }

    public String[][] getData() {
        return this.data;
    }

    private ArrayList getRow(String value) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int j = 0; j < data.length; j++) {
            for (int i = 0; i <= 1; i++) {
                if (data[j][i] != null) {
                    if (data[j][i].equals(value)) {
                        indexes.add(j);
                    }
                }
            }
        }
        return indexes;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    private void writeXLSXFile() {
        try {
            FileInputStream file = new FileInputStream(String.valueOf(this.file));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= getRowNull(); i++) {
                XSSFRow sheetrow = sheet.createRow(i);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = sheetrow.createCell(j);
                    String value = data[i - 1][j] != null ? data[i - 1][j] : " ";
                    cell.setCellValue(value);
                }
            }
            file.close();
            FileOutputStream outputStream = new FileOutputStream(String.valueOf(this.file));
            workbook.write(outputStream);
            outputStream.close();
            errorMessage("Успех", "Файл успешно сохранен", 1);
        } catch (IOException ex) {
            ex.printStackTrace();
            errorMessage("Ошибка", "Файл не сохранен", 0);
        }
    }

    private void writeTXTFile(File newFile) {
        try {
            FileWriter fileWriter = new FileWriter(newFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf(String.valueOf(this.file));
            printWriter.close();
            errorMessage("Сохранен", "Файл успешно сохранен", 1);

        } catch (Exception e) {
            errorMessage("Ошибка", "Файл не сохранен", 0);

        }
    }

    public void parseTXTFile() throws IOException {
        File f = new File(System.getProperty("user.dir") + "/settings.ini");
        if (f.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/settings.ini"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            this.file = new File(String.valueOf(stringBuilder));
            fileFormat = "xlsx";
            parseXLSXFile();
        }
    }

    private void saveFile() throws IOException, InvalidFormatException {
        if ("xlsx".equals(fileFormat)) {
            writeXLSXFile();
        } else {
            errorMessage("Ошибка", "Неожиданный формат: " + fileFormat, 0);
        }
    }

    private void createFile(String pathOfFile) throws IOException {
        File newFile = new File(pathOfFile);
        newFile.getParentFile().mkdirs();
        if (!newFile.exists() && newFile.getName().length() != 0) {
            try {
                String[] parts = newFile.getName().split("\\.");
                fileFormat = parts[parts.length - 1];
                Paths.get(pathOfFile);
                file = new File(String.valueOf(newFile));

                if (parts[parts.length - 1].equals("xlsx") || parts[parts.length - 1].equals("csv")) {
                    parent.panelForExcel.setVisible(true);
                    Individual.panel.updateUI();
                }
            } catch (InvalidPathException ex) {
                errorMessage("Ошибка", "Некорректное название файла", 0);
            }
        } else {
            errorMessage("Ошибка", "Файл с таким названием уже существует", 0);
        }

    }

    private void inputNameWindow(String pathofFile, Integer typeOfOperation) {
        JButton ok = new JButton("OK");
        JPanel panel = new JPanel();
        JTextField inputName = new JTextField();
        JFrame frame = new JFrame("Название файла");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        inputName.setPreferredSize(new Dimension(100, 30));
        panel.add(inputName, "south");
        panel.add(ok);
        ok.addActionListener(e -> {
            String nameFile = inputName.getText() + '.' + "xlsx";
            try {
                createFile(pathofFile + '\\' + nameFile);
                if (typeOfOperation == 4) saveFile();
                frame.dispose();

            } catch (IOException | InvalidFormatException ex) {
                ex.printStackTrace();
            }
        });

    }

    private void findInWrite() {
        if (!buffer.equals(parent.textField[0].getText())) counter = 0;
        buffer = parent.textField[0].getText();
        ArrayList<Integer> indexes = getRow(parent.textField[0].getText());
        if (indexes.size() != 0) {
            for (int i = 0; i < parent.textField.length; i++) {
                parent.textField[i].setText(data[indexes.get(counter)][i]);
            }
            counter++;
            if (counter == indexes.size()) counter = 0;
        }

    }

    private void reWrite() {
        ArrayList<Integer> indexes = getRow(parent.textField[0].getText());
        if (counter - 1 < 0) counter = indexes.size() - 1;
        for (int i = 0; i < parent.textField.length; i++) {
            data[indexes.get(counter - 1)][i] = parent.textField[i].getText();
        }
    }

    private int getRowNull() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == null) return i;
            }
        }
        return 0;
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) parent.tableExcel.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt(" ", i, j);
            }
        }
        model.fireTableDataChanged();
    }

    private void setTable(Integer action) {
        clearTable();
        String[] tags = {"Название страны", "Фигурное катание", "Биатлон", "Бобслей", "Лыжи", "Коньки"};
        DefaultTableModel model = (DefaultTableModel) parent.tableExcel.getModel();
        String value = action == 6 ? parent.number.getText() : parent.name.getText();
        ArrayList<Integer> indexes = getRow(value);
        for (int i = 1; i < indexes.size() + 1; i++) {
            for (int j = 0; j < 6; j++) {
                model.setValueAt(tags[j], 0, j);
                model.setValueAt(data[indexes.get(i - 1)][j], i, j);
            }
        }
        parent.tableExcel.repaint();
        parent.frameFind.dispose();
    }

    private void add() {
        int row = getRowNull();
        for (int j = 0; j < data[row].length; j++) {
            data[row][j] = String.valueOf(parent.textField[j].getText());
            System.out.println("add)");
        }
    }

    private void errorMessage(String title, String message, Integer code) {
        JOptionPane.showMessageDialog(null, message, title, code);
    }

    private void parseXLSXFile() {
        try {
            Workbook workbook = WorkbookFactory.create(file);
            TableModel tb = (TableModel) parent.tableExcel.getModel();
            String[] tags = {"Название страны", "Фигурное катание", "Биатлон", "Бобслей", "Лыжи", "Коньки"};
            Sheet sheet = workbook.getSheetAt(0);
            int numberRows = sheet.getLastRowNum();
            this.data = new String[numberRows + 7][6];
            for (int i = 1; i <= numberRows; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < 6; j++) {
                    Cell cell = row.getCell(j);
                    String value = cell != null ? cell.toString() : " ";
                    tb.setValueAt(tags[i - 1], 0, j);tb.setValueAt(value, i, j);
                    data[i - 1][j] = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        parent.panelForExcel.setVisible(true);
        Individual.panel.updateUI();
    }

    private void openFile(JFileChooser fileChooser) throws IOException {
        Individual.panel.updateUI();
        fileChooser.setDialogTitle("Выбор файла");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX", "xlsx");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String[] parts = String.valueOf(fileChooser.getSelectedFile()).split("\\.");
            file = new File(String.valueOf(fileChooser.getSelectedFile()));
            fileFormat = parts[parts.length - 1];
            if (fileFormat.equals("xlsx")) {
                parseXLSXFile();
                int option = JOptionPane.showConfirmDialog(null, "Сохранить директорию базы данных?", "Настройки", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    File newFile = new File(System.getProperty("user.dir") + "/settings.ini");
                    newFile.getParentFile().mkdirs();
                    newFile.createNewFile();
                    writeTXTFile(newFile);
                }
            } else {
                errorMessage("Ошибка", "Неизвестный формат " + fileFormat, 0);
            }
        }
    }

    private void chooseDirectory(JFileChooser fileChooser, Integer typeOfOperation) {
        parent.panelForExcel.setVisible(false);
        Individual.panel.updateUI();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Выбор директории файла");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inputNameWindow(fileChooser.getSelectedFile().getAbsolutePath(), typeOfOperation);
        }
    }

    public void getPathFile(Integer typeofOperation) throws IOException, InvalidFormatException { // получаем ссылку на файл
        JFileChooser fileChooser = new JFileChooser();
        switch (typeofOperation) {
            case 1, 4 -> chooseDirectory(fileChooser, typeofOperation);
            case 2 -> openFile(fileChooser);
            case 3 -> saveFile();
        }
    }

    public void Action(Integer typeOfOperation) throws IOException {
        switch (typeOfOperation) {
            case 1 -> parent.createFindWindow();
            case 2 -> findInWrite();
            case 3 -> reWrite();
            case 4 -> add();
            case 5 -> parent.frameFind.setVisible(true);
            case 6 -> setTable(6);
            case 7 -> setTable(7);
        }
    }
}