package org.mpeiRZA.Controllers;

import Services.SCL;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.mpeiRZA.Controllers.mService.TabViewMService;
import org.mpeiRZA.Controllers.mService.XMLInOutMService;

public class DataSetsController {

    private static DataSetsController self;
    public SCL SCLFile;
    public Button backButton;
    public TabViewMService tabService;


    public DataSetsController() {
        self = this;
    }

    public void setSCLFile(SCL SCLFile) {
        self.SCLFile = SCLFile;
    }


    public void setTabService(TabViewMService tabService) {
        self.tabService = tabService;
    }

    public void goBack(ActionEvent actionEvent) {
        tabService.showTabs();
    }
}
