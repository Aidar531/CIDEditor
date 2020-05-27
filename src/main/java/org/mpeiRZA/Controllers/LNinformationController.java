package org.mpeiRZA.Controllers;


import Services.SCL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mpeiRZA.App;
import org.mpeiRZA.Controllers.mService.TabViewMService;
import org.mpeiRZA.Controllers.mService.XMLInOutMService;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;
import org.mpeiRZA.iec61850.LD;
import org.mpeiRZA.iec61850.LN;

import java.io.IOException;
import java.util.List;

public class LNinformationController {

    private static LNinformationController self;
    public TableView table;
    public SCL SCLFile;
    public XMLInOutMService XMLController;
    public Button backButton;
    public TabViewMService tabService;
    private List<IED> ieds;
    private static Scene scene;
    private ObservableList<LN> LNodes;
    private int tabIndex;

    public LNinformationController() {
        self = this;
    }

    public void setSCLFile(SCL SCLFile) {
        self.SCLFile = SCLFile;
    }

    public void setXMLController(XMLInOutMService XMLController) {
        self.XMLController = XMLController;
    }

    public void setTabService(TabViewMService tabService) {
        self.tabService = tabService;
    }

    public void updateTablewithData(int tabIndex) {
        SCLFile = XMLController.getSCLfile();
        this.tabIndex = tabIndex;
        TableColumn lnClass = new TableColumn("lnClass");
        lnClass.setMinWidth(100);
        lnClass.setCellValueFactory(new PropertyValueFactory<LN, String>("classType"));
        TableColumn inst = new TableColumn("inst");
        inst.setMinWidth(100);
        inst.setCellValueFactory(new PropertyValueFactory<LN, String>("inst"));
        TableColumn lnType = new TableColumn("lnType");
        lnType.setMinWidth(100);
        lnType.setCellValueFactory(new PropertyValueFactory<LN, String>("name"));
        TableColumn desc = new TableColumn("desc");
        desc.setMinWidth(100);
        desc.setCellValueFactory(new PropertyValueFactory<LN, String>("description"));
        TableColumn prefix = new TableColumn("prefix");
        prefix.setMinWidth(100);
        prefix.setCellValueFactory(new PropertyValueFactory<LN, String>("prefix"));
        LNodes = FXCollections.observableArrayList();
        ieds = IEDExtractor.extractIEDList(SCLFile);
        for (LD j : ieds.get(tabIndex).getLogicalDeviceList()) {
            for (LN n : j.getLogicalNodeList()) {
                LNodes.add(n);
            }
        }
        table.setItems(LNodes);
        table.getColumns().addAll(lnClass, inst, lnType,desc,prefix);

    }

    public void goBack(ActionEvent actionEvent) {
        tabService.showTabs();
    }

    public void addElement(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Pages/addingPromt.fxml"));
        AnchorPane info = null;
        try {
            info = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(info, 600, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        AddingController addController = loader.getController();
        addController.setTreeViewMService(tabService.getTreeViewMService());
        addController.setIEDindex(tabIndex);
        addController.setXMLController(XMLController);
        addController.setLNInfo(LNodes);

    }
}
