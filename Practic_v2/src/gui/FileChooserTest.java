package gui;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class FileChooserTest extends JFrame{
    public JLabel label;
    public File file;

    FileChooserTest(){
        //super("Тестовое окно");
        JFileChooser fileopen = new JFileChooser();
        label = new JLabel("Выбранный файл");
        label.setAlignmentX(CENTER_ALIGNMENT);
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = fileopen.getSelectedFile();
            label.setText(file.getName());
            //System.out.println(file.getName());
        }
        setSize(360, 110);
        setVisible(true);
    }

    public String readUsingFiles(String fileName) throws IOException {
        setVisible(false);
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

}
