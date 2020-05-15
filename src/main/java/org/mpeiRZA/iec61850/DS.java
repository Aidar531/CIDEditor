package org.mpeiRZA.iec61850;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description DataSet
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class DS {

    private String name;

    public String getName() {
        return name;
    }

    private String description;
    private DSType type = DSType.Node;

    public void setDataObject(ArrayList<DO> dataObject) {
        this.dataObject = dataObject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatSetName(String datSetName) {
        this.datSetName = datSetName;
    }

    public ArrayList<DO> getDataObject() {
        return dataObject;
    }

    public String getDatSetName() {
        return datSetName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(DSType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String datSetName; // Имя из списка всех датасетов
    private String id; // appID - для Goose, rptID - для MMS

    private double layoutX = -1, layoutY = -1; // Координаты

    private int intgPd, bufTime; // для MMS

    @XmlElement(name = "DO")
    private ArrayList<DO> dataObject = new ArrayList<>();

    public String toString(){ return String.format("%s (%s)", name, type.toString()); }

    @XmlTransient
    private String ID;
    public DS(){ ID = UUID.randomUUID().toString(); }
}
