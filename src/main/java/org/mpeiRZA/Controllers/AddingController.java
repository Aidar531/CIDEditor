package org.mpeiRZA.Controllers;

import Services.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import org.mpeiRZA.Controllers.mService.TreeViewMService;
import org.mpeiRZA.Controllers.mService.XMLInOutMService;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.LD;
import org.mpeiRZA.iec61850.LN;
import org.mpeiRZA.iec61850.LNClass;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddingController implements Initializable {
    public TilePane tilePane;
    private ObservableList<LN> LNInfo;
    private XMLInOutMService XMLController;
    private static AddingController self;
    private SCL SCLFile;


    public void setTreeViewMService(TreeViewMService treeViewMService) {
        this.treeViewMService = treeViewMService;
    }

    private TreeViewMService treeViewMService;

    public void setIEDindex(int IEDindex) {
        this.IEDindex = IEDindex;
    }

    private int IEDindex;

    public AddingController() {
        self = this;
    }

    public void setLNInfo(ObservableList<LN> LNInfo) {
        this.LNInfo = LNInfo;
    }

    public void setXMLController(XMLInOutMService XMLController) {
        this.XMLController = XMLController;
        this.SCLFile = XMLController.getSCLfile();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (LNClass i:LNClass.values()){
            options.add(i.toString());
        }
        final ComboBox lnClass = new ComboBox(options);
        lnClass.setPromptText("lnClass");

//        final TextField lnClass = new TextField();
//        lnClass.setPromptText("lnClass");
        final TextField inst = new TextField();
        inst.setPromptText("inst");
        final TextField lnType = new TextField();
        lnType.setPromptText("lnType");
        final TextField desc = new TextField();
        desc.setPromptText("description");
        final TextField prefix = new TextField();
        prefix.setPromptText("prefix");
        lnClass.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lnType.setText(lnClass.getSelectionModel().getSelectedItem().toString());
        });

        final Button addButton = new Button("Добавить");
        tilePane.setPadding(new Insets(20, 10, 20, 0));
        tilePane.setHgap(10.0);
        tilePane.setVgap(8.0);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.getChildren().addAll(lnClass,inst,lnType,desc,prefix,addButton);

        addButton.setOnAction(e -> {
            LN LNinst = new LN();
            LNinst.setInst(Long.parseLong(inst.getText()));
            LNinst.setClassType(lnClass.getSelectionModel().getSelectedItem().toString());
            LNinst.setPrefix(prefix.getText());
            LNinst.setDescription(desc.getText());
            LNinst.setName(lnType.getText());
            LNInfo.add(LNinst);
            TLN newLN = new TLN();
            newLN.setInst(Long.parseLong(inst.getText()));
            newLN.setPrefix(prefix.getText());
            newLN.setDesc(desc.getText());
            newLN.setLnType(LNinst.getName());
            List<String> list = new ArrayList<>();
            list.add(lnClass.getSelectionModel().getSelectedItem().toString());
            newLN.setLnClass(list);

            SCLFile.getIED().get(IEDindex).getAccessPoint().get(0).getServer().getLDevice().get(0).getLN().add(newLN);
            treeViewMService.createTreeStructure();
            inst.clear();
//            lnClass.clear();
            prefix.clear();
            desc.clear();
            lnType.clear();
        });

    }
}
