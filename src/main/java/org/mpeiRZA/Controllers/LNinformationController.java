package org.mpeiRZA.Controllers;


import Services.SCL;
import Services.TLN;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.mpeiRZA.App;
import org.mpeiRZA.Controllers.mService.TabViewMService;
import org.mpeiRZA.Controllers.mService.XMLInOutMService;
import org.mpeiRZA.iec61850.*;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
        table.setEditable(true);
        SCLFile = XMLController.getSCLfile();
        this.tabIndex = tabIndex;
        TableColumn lnClass = new TableColumn("lnClass");
        lnClass.setMinWidth(100);
        ObservableList<String> options = FXCollections.observableArrayList();
        for (LNClass i:LNClass.values()){
            options.add(i.toString());
        }
        lnClass.setCellValueFactory(new PropertyValueFactory<LN, String>("classType"));
        lnClass.setCellFactory(ComboBoxTableCell.forTableColumn(options));
        lnClass.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));
        TableColumn inst = new TableColumn("inst");
        inst.setMinWidth(100);
        inst.setCellValueFactory(new PropertyValueFactory<LN, Long>("inst"));
        inst.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        inst.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));
        TableColumn lnType = new TableColumn("lnType");
        lnType.setMinWidth(100);
        lnType.setCellValueFactory(new PropertyValueFactory<LN, String>("name"));
        lnType.setCellFactory(TextFieldTableCell.forTableColumn());
        lnType.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));
        TableColumn desc = new TableColumn("desc");
        desc.setMinWidth(100);
        desc.setCellValueFactory(new PropertyValueFactory<LN, String>("description"));
        desc.setCellFactory(TextFieldTableCell.forTableColumn());
        desc.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));
        TableColumn prefix = new TableColumn("prefix");
        prefix.setMinWidth(100);
        prefix.setCellValueFactory(new PropertyValueFactory<LN, String>("prefix"));
        prefix.setCellFactory(TextFieldTableCell.forTableColumn());
        prefix.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));
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
    private void updateCell(TableColumn.CellEditEvent e, int IEDindex) {
        switch (e.getTableColumn().getText()) {
            case ("lnClass"):
                SCLFile.getIED().get(IEDindex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN().get(e.getTablePosition().getRow()).getLnClass().set(0,e.getNewValue().toString());
                break;
            case ("inst"):
                SCLFile.getIED().get(IEDindex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN().get(e.getTablePosition().getRow()).setInst(Long.parseLong(e.getNewValue().toString()));
                break;
            case ("prefix"):
                SCLFile.getIED().get(IEDindex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN().get(e.getTablePosition().getRow()).setPrefix(e.getNewValue().toString());
                break;
            case ("lnType"):
                SCLFile.getIED().get(IEDindex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN().get(e.getTablePosition().getRow()).setLnType(e.getNewValue().toString());
                break;
            case ("desc"):
                SCLFile.getIED().get(IEDindex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN().get(e.getTablePosition().getRow()).setDesc(e.getNewValue().toString());
                break;
        }
        MainPageController.writeLog("Изменения внесены в поле " + e.getTableColumn().getText() + " на " + e.getTablePosition().getRow() + " строке");
        table.getColumns().clear();
        updateTablewithData(tabIndex);

    }
}
