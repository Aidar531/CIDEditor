package org.mpeiRZA.Controllers.mService;

import Services.SCL;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import javafx.scene.control.TreeItem;
import org.mpeiRZA.Controllers.MainPageController;
import org.mpeiRZA.iec61850.IED;
import org.mpeiRZA.iec61850.IEDExtractor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLInOutMService {
    private Marshaller marshaller;
    private JAXBContext jaxbContext;

    public SCL getSCLfile() {
        return SCLfile;
    }

    private SCL SCLfile = null;

    public void setSCLfile(SCL SCLfile) {
        this.SCLfile = SCLfile;
    }

    public SCL loadSClFile(File file) {
        SCL SCLfile = null;
        try {
            jaxbContext = JAXBContext.newInstance(SCL.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SCLfile = (SCL) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return SCLfile;
    }

    public void SaveChanges() throws JAXBException {
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(SCLfile, new File("NewProject1.cid"));
    }
}
