package org.mpeiRZA.Controllers.mService;

import Services.SCL;
import Services.TIED;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.text.Font;
import org.mpeiRZA.Controllers.MainPageController;
import org.mpeiRZA.iec61850.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class TreeViewMService {

    public TreeView treeView = null;
    public File file = null;
    private SCL SCLfile = null;
    public XMLInOutMService XMLController;
    TreeItem<String> rootTree;
    ArrayList<IED> ieds = null;
    TabPane tabView;

    public TreeViewMService(TreeView treeView, File file, XMLInOutMService XMLController,TabPane tabView) {
        this.tabView = tabView;
        this.XMLController = XMLController;
        this.treeView = treeView;
        this.file = file;
        SCLfile = XMLController.loadSClFile(file);
        XMLController.setSCLfile(SCLfile);
    }


    public void createTreeStructure() {
        rootTree = new TreeItem<>(file.getName());
        TreeItem<String> IEDs = new TreeItem<>("IEDs");
        IEDs.setExpanded(true);
        ieds = IEDExtractor.extractIEDList(SCLfile);

        for (IED i : ieds) {
            TreeItem<String> ied = new TreeItem<>(i.getName());
            for (LD j:i.getLogicalDeviceList()) {
                TreeItem<String> LD = new TreeItem<>(j.getName());

                TreeItem<String> DSs = new TreeItem<String>("Signals");
                TreeItem<String> GOOSE = new TreeItem<String>("GOOSE");
                for (DS n:j.getGooseInputDS()) {
                    TreeItem<String> inputs = new TreeItem<String>("Inputs");
                    for (DO m:n.getDataObject()) {
                        TreeItem<String> DOins = new TreeItem<String>(m.getDataObjectName());
                        inputs.getChildren().add(DOins);
                    }
                    GOOSE.getChildren().add(inputs);
                }
                TreeItem<String> outputs = new TreeItem<String>("Outputs");
                for (DS n:j.getGooseOutputDS()) {
                    TreeItem<String> signals = new TreeItem<String>(n.getDatSetName());
                    for (DO m:n.getDataObject()) {
                        TreeItem<String> DOins = new TreeItem<String>(m.getDataObjectName());
                        signals.getChildren().add(DOins);
                    }
                   outputs.getChildren().add(signals);
                }
                GOOSE.getChildren().add(outputs);
                DSs.getChildren().add(GOOSE);

                TreeItem<String> MMS = new TreeItem<String>("MMS");
                for (DS n:j.getMmsOutputDS()) {
                    TreeItem<String> signals = new TreeItem<String>(n.getDatSetName());
                    for (DO m:n.getDataObject()) {
                        TreeItem<String> DOins = new TreeItem<String>(m.getDataObjectName());
                        signals.getChildren().add(DOins);
                    }
                    MMS.getChildren().add(signals);
                }
                DSs.getChildren().add(MMS);
                LD.getChildren().add(DSs);

                TreeItem<String> LNs = new TreeItem<String>("LogicalNodes");
                for (LN n:j.getLogicalNodeList()) {
                    TreeItem<String> LN = new TreeItem<>(n.getName());
                    LNs.getChildren().add(LN);
                }
                LD.getChildren().add(LNs);
                ied.getChildren().add(LD);
            }
            IEDs.getChildren().add(ied);

        }
        rootTree.getChildren().add(IEDs);
        rootTree.setExpanded(true);
        treeView.setRoot(rootTree);
        treeView.setEditable(true);
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    TreeItem trITM = (TreeItem) newValue;
                    if (trITM != null) {
                    if (trITM.isLeaf()) {
                        openChousenElement(trITM);
                    } }});
        treeView.setCellFactory(TextFieldTreeCell.forTreeView());
//        treeView.setOnMouseEntered(mouseEvent -> chooseTabView(mouseEvent));
//        treeView.setOnMouseClicked(mouseEvent -> chooseTabView(mouseEvent));
        treeView.setOnEditCommit(event -> editCommit((TreeView.EditEvent) event));

    }

//    TODO  Испарвить баг с маршалингом, которые поля форматируются неправильно

    private void openChousenElement(TreeItem leaf) {
        final Label label = new Label(leaf.toString());
        label.setFont(new Font("Arial", 20));
        TableView newTable = new TableView();
        int count=0;
        TreeItem root = leaf.getParent();

//TODO Здесь ищу имя корневого элемента - костыль, нужно сделать более универсальный поиск IEDа
        while ( Optional.ofNullable(root.getParent()).isPresent()) {
            if (root.getValue().toString().contains("TestIED")) break;
            root = root.getParent();
        }
        for (IED i:ieds) {
            if (i.getName().equals(root.getValue().toString())) break;
            count+=1;
        }

        tabView.getSelectionModel().select(count);
        System.out.println("Листок - " + leaf.getValue().toString());

    }

    private void editCommit(TreeView.EditEvent event)
    {
        for (TIED i:SCLfile.getIED()) {
            if (i.getName().equals(event.getOldValue().toString())) {
                i.setName(event.getNewValue().toString());
            }
        }

        MainPageController.writeLog(event.getTreeItem().getValue() + " Измененен на " + event.getNewValue());
    }
}
