package org.mpeiRZA.Controllers.mService;

import Services.SCL;
import Services.TServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.mpeiRZA.App;
import org.mpeiRZA.Controllers.DataSetsController;
import org.mpeiRZA.Controllers.LNinformationController;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;
import org.mpeiRZA.iec61850.LD;
import org.mpeiRZA.iec61850.ServicesDesc;
import javax.xml.bind.annotation.XmlElement;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TabViewMService {
    private File file;
    public TabPane TabView;
    private ArrayList<IED> ieds;
    private SCL SCLFile = null;
    public XMLInOutMService XMLController;
    public List<Tab> listOfIed = new ArrayList<>();

    public TreeViewMService getTreeViewMService() {
        return treeViewMService;
    }

    public TreeViewMService treeViewMService;


    public TabViewMService(TabPane tabView, File file, XMLInOutMService XMLController, TreeViewMService treeViewMService) {
        this.XMLController = XMLController;
        this.file = file;
        this.TabView = tabView;
        this.treeViewMService = treeViewMService;
    }

    public void showTabs() {

        if (TabView.getTabs().isEmpty()) {
            createTabContent();
        } else {
            removeTabs();
            createTabContent();
        }
//        хотел сделать чтобы переключалось при нажатии сверху на IED, пока что то не получилось
//        for (Tab i:listOfIed) {
//            i.setOnSelectionChanged(event ->  {removeTabs();createTabContent();});
//        }
    }

    public void removeTabs() {
        TabView.getTabs().clear();
    }

    private void createTabContent() {

        SCLFile = XMLController.loadSClFile(file);
        ieds = IEDExtractor.extractIEDList(SCLFile);
        ServicesDesc servdesc = new ServicesDesc();
        Method method = null;

        for (IED i : ieds) {

            List<String> listOfServices = getServicesList(i.getServices());

            for (LD j : i.getLogicalDeviceList()) {

                VBox vbox1 = new VBox();
                vbox1.setAlignment(Pos.CENTER_LEFT);
                vbox1.setSpacing(10);
                Label Title = new Label("Доступные сервисы");
                Title.setFont(new Font("Serif", 25));
                vbox1.getChildren().add(Title);

                ObservableList<Label> labelsOfServices = FXCollections.observableArrayList();

                vbox1.setStyle("-fx-border-color: #000000");
                for (String s : listOfServices) {
                    try {
                        method = servdesc.getClass().getMethod("get" + s);
                    } catch (NoSuchMethodException ignored) {
                    }
                    Label serv = new Label(s);
                    serv.setFont(new Font("Arial", 14));
                    labelsOfServices.add(serv);
                    try {
                        serv.setTooltip(new Tooltip((String) method.invoke(servdesc)));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                ListView<Label> list = new ListView(labelsOfServices);
                vbox1.getChildren().add(list);
                Button button1 = new Button("Доступные Logical Nodes");
                Button button2 = new Button("Сигналы GOOSE");
                Button button3 = new Button("Сигналы MMS");
                button1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button1.setOnMouseClicked(mouseEvent -> {
                    try {
                        openNewPage(mouseEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                button2.setOnMouseClicked(mouseEvent -> {
                    try {
                        openNewPageWithDataSets(mouseEvent,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                button3.setOnMouseClicked(mouseEvent -> {
                    try {
                        openNewPageWithDataSets(mouseEvent,2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                TilePane tileButtons = new TilePane(Orientation.VERTICAL);
                tileButtons.setPadding(new Insets(20, 10, 20, 0));
                tileButtons.setHgap(10.0);
                tileButtons.setVgap(8.0);
                tileButtons.setAlignment(Pos.CENTER);
                tileButtons.getChildren().addAll(button1, button2,button3);

                HBox hbox = new HBox(20, vbox1, tileButtons);
                Tab tab = new Tab(i.getName(), new ScrollPane(hbox));
                listOfIed.add(tab);
                TabView.getTabs().add(tab);
            }
        }
    }

    private void openNewPageWithDataSets(MouseEvent mouseEvent,int regim) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Pages/DataSets.fxml"));
        AnchorPane info = loader.load();
        TabView.getTabs().get(TabView.getSelectionModel().getSelectedIndex()).setContent(info);
        DataSetsController DataSetsInfo  = loader.getController();
        DataSetsInfo.setTabService(this);
        DataSetsInfo.setXMLController(XMLController);
        if (regim == 1) DataSetsInfo.createContentForGoose(TabView.getSelectionModel().getSelectedIndex());
        else if (regim == 2) DataSetsInfo.createContentForMMS(TabView.getSelectionModel().getSelectedIndex());
    }

    private void openNewPage(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Pages/LDinformation.fxml"));
        AnchorPane info = loader.load();
        TabView.getTabs().get(TabView.getSelectionModel().getSelectedIndex()).setContent(info);
        LNinformationController LdInfo  = loader.getController();
        LdInfo.setTabService(this);
        LdInfo.setSCLFile(SCLFile);
        LdInfo.setXMLController(XMLController);
        LdInfo.updateTablewithData(TabView.getSelectionModel().getSelectedIndex());
    }

    private List<String> getServicesList(TServices Services) {
        List<String> ListOfServices = new ArrayList<>();
        Field[] names = Services.getClass().getDeclaredFields();
        for (Field n : names) {
            n.setAccessible(true);
            XmlElement a = n.getAnnotation(XmlElement.class);
            String nameValue = a.name();
            Method method = null;
            try {
                method = Services.getClass().getMethod("get" + nameValue);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                assert method != null;
                if (method.invoke(Services) != null) {
                    ListOfServices.add(a.name());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return ListOfServices;

    }
}
