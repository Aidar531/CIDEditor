package org.mpeiRZA.Controllers;

import Services.SCL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.mpeiRZA.Controllers.mService.TabViewMService;
import org.mpeiRZA.Controllers.mService.TreeViewMService;
import org.mpeiRZA.Controllers.mService.XMLInOutMService;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainPageController  implements Initializable{

    public TextFlow myText;
    public MenuItem OpenTheFile;
    private final Desktop desktop = Desktop.getDesktop();
    final FileChooser fileChooser = new FileChooser();
    public File file;
    @FXML public TreeView<String> treeView;
    private static MainPageController self;
    public TabPane TabView;
    private TreeViewMService TreeService;
    private TabViewMService TabService;
    private SCL SCLfile;
    public XMLInOutMService XMlController;




    public MainPageController() {
        self = this;
        SCLfile = new SCL();
    }

    public static void writeLog(String msg) {
        Text txt = new Text("\n" + msg);
        self.myText.getChildren().add(txt);
    }


    public void OpenFile(ActionEvent actionEvent) {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            XMlController = new XMLInOutMService();
            TreeService = new TreeViewMService(treeView,file,XMlController,TabView);
            TreeService.createTreeStructure();
            TabService = new TabViewMService(TabView,file, XMlController);
            TabService.showTabs();
            myText.getChildren().add(new Text("\nОткрыт файл: " + file.getName()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text txt = new Text("Программа запущена");
        self.myText.getChildren().add(txt);
        // Временный запуск нужног файла
        file = new File("/home/aidar/Desktop/MasterStudying#2/Kursach/CIDEditor/Project.cid");
        if (file != null) {
            XMlController = new XMLInOutMService();
            TreeService = new TreeViewMService(treeView, file, XMlController, TabView);
            TreeService.createTreeStructure();
            TabService = new TabViewMService(TabView, file, XMlController);
            TabService.showTabs();
            myText.getChildren().add(new Text("\nОткрыт файл: " + file.getName()));
        }
    }

    public void SaveTheFile(ActionEvent actionEvent) throws JAXBException {
        MainPageController.writeLog("Изменения сохранены");
        XMlController.SaveChanges();
    }
}
