package org.mpeiRZA.Controllers;

import Services.SCL;
import Services.TExtRef;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.util.converter.NumberStringConverter;
import org.mpeiRZA.Controllers.mService.TabViewMService;
import org.mpeiRZA.Controllers.mService.XMLInOutMService;
import org.mpeiRZA.iec61850.*;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class DataSetsController {

    private static DataSetsController self;
    public SCL SCLFile;
    public Button backButton;
    public TabViewMService tabService;
    public TableView table;
    public TilePane buttonContainer;
    public XMLInOutMService XMLController;
    public BorderPane infoContainer;
    public Label topLabel;
    private ObservableList<TExtRef> ExtRefList;
    private List<TExtRef> extRef;
    private List<TExtRef> buffer;
    private boolean FlagInputs = false;

    public void setXMLController(XMLInOutMService XMLController) {
        this.XMLController = XMLController;
    }

    public DataSetsController() {
        self = this;
        buffer = new ArrayList<>();
    }



    public void setTabService(TabViewMService tabService) {
        self.tabService = tabService;
    }
    public void createContentForGoose(int tabIndex){
        SCLFile = XMLController.getSCLfile();
        buttonContainer.setVgap(10);
        MenuButton menuButton= new MenuButton("Inputs");
        menuButton.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        buttonContainer.getChildren().add(menuButton);
        String str = null;
        for ( LD i:IEDExtractor.extractIEDList(SCLFile).get(tabIndex).getLogicalDeviceList()) {
            for (DS j:i.getGooseInputDS()) {
                for (DO n:j.getDataObject()) {
                    str = "\\("+n.getDataObjectName()+"\\)";
                    MenuItem button  = new MenuItem( n.toString().replaceAll(str,"")+"_"+ n.getDataObjectName());
                    button.setId(n.getDataObjectName());
                    button.setOnAction(actionEvent -> updateTableWithDataforInput(actionEvent,tabIndex));
                    menuButton.getItems().add(button);
                }
            }
            for (DS j:i.getGooseOutputDS()) {
                menuButton = new MenuButton("Output__"+j.getName());
                menuButton.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                buttonContainer.getChildren().add(menuButton);
                for (DO n:j.getDataObject()) {
                    MenuItem button  = new MenuItem(n.toString().replaceAll(str,"")+"_"+ n.getDataObjectName());
                    button.setId(n.toString().replaceAll("("+n.getDataObjectName()+")",""));
                    button.setOnAction(actionEvent -> updateTableWithDataForDataSets(actionEvent,tabIndex));
                    menuButton.getItems().add(button);
                }
            }
        }

    }

    public void createContentForMMS(int tabIndex){
        SCLFile = XMLController.getSCLfile();
        buttonContainer.setVgap(10);
        for ( LD i:IEDExtractor.extractIEDList(SCLFile).get(tabIndex).getLogicalDeviceList()) {
            for (DS j:i.getMmsOutputDS()) {
                MenuButton menuButton= new MenuButton(j.getName());
                menuButton.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                buttonContainer.getChildren().add(menuButton);
                for (DO n:j.getDataObject()) {
                    MenuItem button  = new MenuItem(n.toString());
                    button.setOnAction(actionEvent -> updateTableWithDataForDataSets(actionEvent,tabIndex));
                    menuButton.getItems().add(button);
                }
            }
        }

    }

    public void goBack(ActionEvent actionEvent) {
        tabService.showTabs();
    }
    private void createTableView(ActionEvent e, int tabIndex) {
        MenuItem menuItem = (MenuItem) e.getTarget();
        table.setEditable(true);
        SCLFile = XMLController.getSCLfile();

        TableColumn iedName = new TableColumn("iedName");
        iedName.setMinWidth(100);
        iedName.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("iedName"));
        iedName.setCellFactory(TextFieldTableCell.forTableColumn());
        iedName.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn ldInst = new TableColumn("ldInst");
        ldInst.setMinWidth(100);
        ldInst.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("ldInst"));
        ldInst.setCellFactory(TextFieldTableCell.forTableColumn());
        ldInst.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn prefix = new TableColumn("prefix");
        prefix.setMinWidth(100);
        prefix.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("prefix"));
        prefix.setCellFactory(TextFieldTableCell.forTableColumn());
        prefix.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn lnClass = new TableColumn("lnClass");
        lnClass.setMinWidth(100);
        ObservableList<String> options = FXCollections.observableArrayList();
        for (LNClass i:LNClass.values()){
            options.add(i.toString());
        }
        lnClass.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("lnClass"));
        lnClass.setCellFactory(ComboBoxTableCell.forTableColumn(options));

        TableColumn lnInst = new TableColumn("lnInst");
        lnInst.setMinWidth(100);
        lnInst.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("lnInst"));
        lnInst.setCellFactory(TextFieldTableCell.forTableColumn());
        lnInst.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn doName = new TableColumn("doName");
        doName.setMinWidth(100);
        doName.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("doName"));
        doName.setCellFactory(TextFieldTableCell.forTableColumn());
        doName.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn daName = new TableColumn("daName");
        daName.setMinWidth(100);
        daName.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("daName"));
        daName.setCellFactory(TextFieldTableCell.forTableColumn());
        daName.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn intAddr = new TableColumn("intAddr");
        intAddr.setMinWidth(100);
        intAddr.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("intAddr"));
        intAddr.setCellFactory(TextFieldTableCell.forTableColumn());
        intAddr.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn desc = new TableColumn("desc");
        desc.setMinWidth(100);
        desc.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("desc"));
        desc.setCellFactory(TextFieldTableCell.forTableColumn());
        desc.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn serviceType = new TableColumn("serviceType");
        serviceType.setMinWidth(100);
        options = FXCollections.observableArrayList();
        for (Field i:ServicesDesc.class.getDeclaredFields()){
            options.add(i.getName());
        }
        serviceType.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("serviceType"));
        serviceType.setCellFactory(ComboBoxTableCell.forTableColumn(options));

        TableColumn srcLDInst = new TableColumn("srcLDInst");
        srcLDInst.setMinWidth(100);
        srcLDInst.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("srcLDInst"));
        srcLDInst.setCellFactory(TextFieldTableCell.forTableColumn());
        srcLDInst.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn srcPrefix = new TableColumn("srcPrefix");
        srcPrefix.setMinWidth(100);
        srcPrefix.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("srcPrefix"));
        srcPrefix.setCellFactory(TextFieldTableCell.forTableColumn());
        srcPrefix.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn srcLNClass = new TableColumn("srcLNClass");
        srcLNClass.setMinWidth(100);
        srcLNClass.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("srcLNClass"));
        srcLNClass.setCellFactory(TextFieldTableCell.forTableColumn());
        srcLNClass.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn srcLNInst = new TableColumn("srcLNInst");
        srcLNInst.setMinWidth(100);
        srcLNInst.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("srcLNInst"));
        srcLNInst.setCellFactory(TextFieldTableCell.forTableColumn());
        srcLNInst.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));

        TableColumn srcCBName = new TableColumn("srcCBName");
        srcCBName.setMinWidth(100);
        srcCBName.setCellValueFactory(new PropertyValueFactory<TExtRef, String>("srcCBName"));
        srcCBName.setCellFactory(TextFieldTableCell.forTableColumn());
        srcCBName.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Parameter, String>>) t -> updateCell(t,tabIndex));
        table.getColumns().addAll(iedName, ldInst, prefix,lnClass,lnInst,doName,daName,intAddr,desc,serviceType,srcLDInst,srcPrefix,srcLNClass,srcLNInst,srcCBName);
    }

    private void updateTableWithDataforInput(ActionEvent e, int tabIndex) {
        MenuItem menuItem = (MenuItem) e.getTarget();
        ExtRefList = FXCollections.observableArrayList();
        extRef = SCLFile.getIED().get(tabIndex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN0().getInputs().getExtRef();
        System.out.println("#############");
        for (TExtRef j : extRef) {
            System.out.println(j.getDoName() + " = " + menuItem.getId());
            System.out.println(j.getDaName()+ "=" + menuItem.getText().replaceAll(" _"+menuItem.getId(),""));
            System.out.println("-----------------");
            if (j.getDoName().equals(menuItem.getId()) && (j.getDaName().equals(menuItem.getText().replaceAll(" _"+menuItem.getId(),"")))) {
                ExtRefList.add(j);
            }
        }

        if (!FlagInputs) {
            table.setItems(ExtRefList);
            createTableView(e, tabIndex);
            FlagInputs = true;
        }
        else {
            table.getItems().clear();
            for (TExtRef i:buffer) {
                ExtRefList.add(i);
            }
            table.setItems(ExtRefList);
        }
        buffer.add(ExtRefList.get(0));
    }

    private void updateTableWithDataForDataSets(ActionEvent e,int tabIndex){

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
//        updateTablewithData(IEDindex);

    }

}
