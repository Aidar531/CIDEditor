package org.mpeiRZA.Controllers;

import Services.SCL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.mpeiRZA.Controllers.mService.TreeViewMService;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
    TreeViewMService TreeService;

    public MainPageController() {
        self = this;
    }

    public static void writeLog(String msg) {
        Text txt = new Text("\n" + msg);
        self.myText.getChildren().add(txt);
    }


    public void OpenFile(ActionEvent actionEvent) {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {

            myText.getChildren().add(new Text("\nОткрыт файл: " + file.getName()));
            TreeService = new TreeViewMService(treeView,file);
            TreeService.createTreeStructure();
//            TreeItem<String> Languages = new TreeItem<String>("languages");
//            TreeItem<String> germanics = new TreeItem<>("Geramnic");
//            germanics.getChildren().add(new TreeItem<String>("German"));
//            germanics.getChildren().add(new TreeItem<String>("English"));
//
//            TreeItem<String> romans = new TreeItem<String>("Roman");
//            romans.getChildren().add(new TreeItem<String>("French"));
//            romans.getChildren().add(new TreeItem<String>("Spanish"));
//            romans.getChildren().add(new TreeItem<String>("Italian"));
//
//            Languages.getChildren().add(germanics);
//            Languages.getChildren().add(romans);

        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text txt = new Text("Программа запущена");
        self.myText.getChildren().add(txt);
    }

    public void SaveTheFile(ActionEvent actionEvent) throws JAXBException {
        MainPageController.writeLog("Изменения сохранены");
        TreeService.SaveChanges();
    }
}
