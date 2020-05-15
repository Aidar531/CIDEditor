package org.mpeiRZA.Controllers.mService;

import Services.SCL;
import Services.TServices;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;
import org.mpeiRZA.iec61850.LD;
import org.mpeiRZA.iec61850.ServicesDesc;

import javax.xml.bind.annotation.XmlElement;
import java.io.File;
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

    public TabViewMService(TabPane tabView, File file, XMLInOutMService XMLController) {
        this.XMLController = XMLController;
        this.file = file;
        TabView = tabView;
    }

    public void showTabs() {
        if (TabView.getTabs().isEmpty()) {
            createTabContent();
        } else {
            removeTabs();
            createTabContent();
        }
    }

    public void removeTabs() {
        TabView.getTabs().clear();
    }

    private void createTabContent() {
        SCLFile = XMLController.getSClFile(file);
        ieds = IEDExtractor.extractIEDList(SCLFile);
        ServicesDesc servdesc = new ServicesDesc();
        Method method = null;

        for (IED i : ieds) {
            List<String> listOfServices = getServicesList(i.getServices());
//            listOfServices.forEach(System.out::println);
            for (LD j : i.getLogicalDeviceList()) {
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER_LEFT);
                vbox.setSpacing(10);
                Label Title = new Label("Доступные сервисы");
                Title.setFont(new Font("Serif",25));
                vbox.getChildren().add(Title);
                for (String s:listOfServices) {
                    try {method = servdesc.getClass().getMethod("get" + s); } catch (NoSuchMethodException ignored) {}
                    Label serv = new Label(s);
                    serv.setFont(new Font("Arial",14));
                    try { serv.setTooltip(new Tooltip((String) method.invoke(servdesc)));} catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}
                    vbox.getChildren().add(serv);
                }
                TabView.getTabs().add(new Tab(i.getName(), new ScrollPane(vbox)));
            }
        }
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
