package org.mpeiRZA.Controllers.mService;

import Services.SCL;
import Services.TIED;
import Services.TLDevice;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.control.skin.TreeCellSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.mpeiRZA.Controllers.MainPageController;
import org.mpeiRZA.iec61850.*;
import org.w3c.dom.Text;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
    }


    public void createTreeStructure() {
        rootTree = new TreeItem<>(file.getName());
        TreeItem<String> IEDs = new TreeItem<>("IEDs");
        IEDs.setExpanded(true);
        SCLfile = XMLController.getSClFile(file);
        XMLController.setSCLfile(SCLfile);
        ieds = IEDExtractor.extractIEDList(SCLfile);

        for (IED i : ieds) {
            TreeItem<String> ied = new TreeItem<>(i.getName());
            for (LD j:i.getLogicalDeviceList()) {
                TreeItem<String> LD = new TreeItem<>(j.getName());

                TreeItem<String> DSs = new TreeItem<String>("DataSets");

                for (DS n:j.getGooseInputDS()) {
                    TreeItem<String> inputs = new TreeItem<String>("Inputs");
                    for (DO m:n.getDataObject()) {
                        TreeItem<String> DOins = new TreeItem<String>(m.getDataObjectName());
                        inputs.getChildren().add(DOins);
                    }
                    DSs.getChildren().add(inputs);
                }
                for (DS n:j.getGooseOutputDS()) {
                    TreeItem<String> signals = new TreeItem<String>(n.getDatSetName());
                    for (DO m:n.getDataObject()) {
                        TreeItem<String> DOins = new TreeItem<String>(m.getDataObjectName());
                        signals.getChildren().add(DOins);
                    }
                    DSs.getChildren().add(signals);
                }
                for (DS n:j.getMmsOutputDS()) {
                    TreeItem<String> signals = new TreeItem<String>(n.getDatSetName());
                    for (DO m:n.getDataObject()) {
                        TreeItem<String> DOins = new TreeItem<String>(m.getDataObjectName());
                        signals.getChildren().add(DOins);
                    }
                    DSs.getChildren().add(signals);
                }
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
                    if (trITM.isLeaf()) {
                        openChousenElement(trITM);
                    } });
//        treeView.setCellFactory(TextFieldTreeCell.forTreeView());
//        treeView.setOnMouseEntered(mouseEvent -> chooseTabView(mouseEvent));
//        treeView.setOnMouseClicked(mouseEvent -> chooseTabView(mouseEvent));
//        treeView.setOnEditCommit(event -> editCommit((TreeView.EditEvent) event));

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

        MainPageController.writeLog(event.getTreeItem().getValue() + " changed." +
                " old = " + event.getOldValue() +
                ", new = " + event.getNewValue());
    }
}
