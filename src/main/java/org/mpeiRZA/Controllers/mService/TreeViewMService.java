package org.mpeiRZA.Controllers.mService;

import Services.SCL;
import Services.TIED;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import org.mpeiRZA.Controllers.MainPageController;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

public class TreeViewMService {

    public TreeView treeView = null;
    public File file = null;
    private SCL SCLfile = null;
    ArrayList<IED> ieds = null;
    JAXBContext jaxbContext = null;
    Marshaller marshaller = null;

    public TreeViewMService(TreeView treeView, File file) {
        this.treeView = treeView;
        this.file = file;
    }

    public void SaveChanges() throws JAXBException {
        marshaller.marshal(SCLfile, new File("NewProject1.cid"));
    }

    public void createTreeStructure() {
        TreeItem<String> rootTree = new TreeItem<>(file.getName());
        TreeItem<String> IEDs = new TreeItem<>("IEDs");
        TreeItem<String> Services = new TreeItem<>("Services");
        TreeItem<String> AccessPoint = new TreeItem<>("AccessPoint");
        try {
            jaxbContext = JAXBContext.newInstance(SCL.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SCLfile = (SCL) jaxbUnmarshaller.unmarshal(file);
            ieds = IEDExtractor.extractIEDList(SCLfile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        for (IED i : ieds) {
            IEDs.getChildren().add(new TreeItem<>(i.getName()));

        }

        rootTree.getChildren().add(IEDs);
        treeView.setRoot(rootTree);
        treeView.setEditable(true);
        treeView.setCellFactory(TextFieldTreeCell.forTreeView());
        treeView.setOnEditCommit(event -> editCommit((TreeView.EditEvent) event));

    }
//    TODO  Испарвить ошибку бага с маршалингом, нкоторые поля форматируются неправильно

    private void editCommit(TreeView.EditEvent event)
    {
        for (TIED i:SCLfile.getIED()) {
            if (i.getName().equals(event.getOldValue().toString())) {
                i.setName(event.getNewValue().toString());
            }
        }
        try {
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        MainPageController.writeLog(event.getTreeItem().getValue() + " changed." +
                " old = " + event.getOldValue() +
                ", new = " + event.getNewValue());
    }
}
