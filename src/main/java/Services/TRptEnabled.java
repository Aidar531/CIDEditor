
package Services;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRptEnabled", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "clientLN"
})
public class TRptEnabled
    extends TUnNaming
{
    @XmlAttribute(name = "max")
    protected Long max;

    @XmlElement(name = "ClientLN", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TClientLN> clientLN;

    public List<TClientLN> getClientLN() {
        if (clientLN == null) {
            clientLN = new ArrayList<TClientLN>();
        }
        return this.clientLN;
    }

    public long getMax() {
        if (max == null) {
            return  1L;
        } else {
            return max;
        }
    }

    public void setMax(Long value) {
        this.max = value;
    }

}
