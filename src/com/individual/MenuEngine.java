package com.individual;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;


public class MenuEngine implements ActionListener {
    Individual parent;
    workWithFile fileAct;

    public MenuEngine(Individual parent, workWithFile fileAct) {
        this.parent = parent;
        this.fileAct = fileAct;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        try {
            if (src instanceof JMenuItem) {
                src = (JMenuItem) e.getSource();
                if (parent.openFile.equals(src)) {
                    fileAct.getPathFile(2);
                } else if (parent.saveFile.equals(src)) {
                    if (fileAct.getFileFormat() != null) fileAct.getPathFile(3);
                } else if (parent.saveFileAs.equals(src)) {
                    if (fileAct.getFileFormat() != null) fileAct.getPathFile(4);
                } else if (parent.exit.equals(src)) {
                    parent.frame.dispose();
                }

            } else if (src instanceof JButton) {
                src = (JButton) e.getSource();
                if (fileAct.getData() != null) {
                    if (parent.edit.equals(src)) {
                        fileAct.Action(1);
                    } else if (parent.finder.equals(src)) {
                        fileAct.Action(2);
                    } else if (parent.write.equals(src)) {
                        fileAct.Action(3);
                    } else if (parent.add.equals(src)) {
                        fileAct.Action(4);
                    } else if (parent.find.equals(src)) {
                        fileAct.Action(5);
                    } else if (parent.findRace.equals(src)) {
                        fileAct.Action(6);
                    } else if (parent.findName.equals(src)) {
                        fileAct.Action(7);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Необходимо открыть базу данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException | InvalidFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка в работе программы", "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
    }
}
// h

