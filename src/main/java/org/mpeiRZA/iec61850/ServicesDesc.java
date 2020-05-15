package org.mpeiRZA.iec61850;

import Services.*;

import javax.xml.bind.annotation.XmlElement;

public class ServicesDesc {
    private  String DynAssociation = "IAll services for dynamic building of associations. These are capabilities without\n" +
            "attributes.\n" +
            "The max attribute indicates the maximum guarantied number of dynamic associations\n" +
            "which are possible";
    private String SettingGroups ="Setting group services belong to the setting group control block. If this control block is\n" +
            "available, then the setting group service SelectActiveSG for activating a setting group\n" +
            "is also available. The capability of online editing (IEC 61850-7-2 services\n" +
            "SelectEditSG, ConfirmEditSGValues, SetSGValues) is decided with the SGEdit\n" +
            "element. The capability to configure the (number of) setting groups by SCL can be also\n" +
            "available (ConfSG). These are options without attributes";
    private String GetDirectory ="Services for reading the contents of a server, i.e. the LD and LN directories (all LDs,\n" +
            "LNs and DATA of the LNs). This is an option without attributes.\n" +
            "Includes the IEC 61850-7-2 services GetServerDirectory, GetLogicalDeviceDirectory,\n" +
            "GetLogicalNodeDirectory";
    private String GetDataObjectDefinition="Service to retrieve the complete list of all DA definitions of the referenced data that are\n" +
            "visible and thus accessible to the requesting client by the referenced LN. It is a service\n" +
            "without attributes. Refers to IEC 61850-7-2 service GetDataDefinition";
    private String DataObjectDirectory="Service to get the DATA defined in a LN. It is a service without attributes. Refers to\n" +
            "IEC 61850-7-2 service GetDataDirectory";
    private String GetDataSetValue="Service to retrieve all values of data referenced by the members of the data set. It is a\n" +
            "service without attributes. Refers to IEC 61850-7-2 service GetDataSetValues";
    private String SetDataSetValue="Service to write all values of data referenced by the members of the data set. It is a\n" +
            "service without attributes. Refers to IEC 61850-7-2 service SetDataSetValues";
    private String DataSetDirectory="Service to retrieve FCD/FCDA of all members referenced in the data set. It is a service\n" +
            "without attributes. Refers to IEC 61850-7-2 service GetDataSetDirectory";
    private String ConfDataSet="f ConfDataSet is not specified, then the default value of its max attribute is equal to\n" +
            "the number of preconfigured data sets, and they may only be modified. If it is specified,\n" +
            "it is possible to configure new data sets up to the defined max, or modify existing ones\n" +
            "at configuration time via SCL.\n" +
            "The attribute meaning is:\n" +
            "max – the maximum number of data sets\n" +
            "maxAttributes – the maximum number of attributes allowed in a data set (an FCDA can\n" +
            "contain several attributes)\n" +
            "modify – TRUE means that preconfigured data sets may be modified; default: true";
    private String DynDataSet="Services to dynamically create and delete data sets. Refers to IEC 61850-7-2 services\n" +
            "CreateDataSet and DeleteDataSet.\n" +
            "The attribute meaning is:\n" +
            "max – the maximum number of dynamically creatable data sets (including eventually\n" +
            "predefined data sets)\n" +
            "maxAttributes – the maximum number of attributes allowed in a data set (an FCDA can\n" +
            "contain several attributes)";
    private String ReadWrite="Basic data read and write facility; includes the IEC 61850-7-2 services GetData,\n" +
            "SetData, and the Operate service, if appropriate data exist. It is a capability without\n" +
            "attributes.";
    private String TimerActivatedControl="This element specifies that timer activated control services are supported. All other\n" +
            "control-related services are specified directly at a DO with the ctlModel attribute. It is a\n" +
            "service without attributes.";
    private String ConfReportControl="Capability of static (by configuration via SCL) creation of report control blocks.\n" +
            "The attribute’s meaning is:\n" +
            "max – the maximum number of instantiable report control blocks. If this is equal to the\n" +
            "number of preconfigured instances, then no new instances can be created. If it is\n" +
            "higher than the number of preconfigured instances, the project engineer is allowed to\n" +
            "create more instances, even for new types, up to this limit.\n" +
            "bufMode – unbuffered, buffered, both; the buffer mode allowed to configure for new\n" +
            "control block types.\n" +
            "bufConf – boolean. TRUE means, the buffered attribute of preconfigured report control\n" +
            "blocks can be changed via SCL";
    private String GetCBValues="Read values of control blocks. It is a service without attributes";
    private String ConfLogControl="Capability of static (by configuration via SCL) creation of log control blocks.\n" +
            "The attribute’s meaning is:\n" +
            "max – maximum number of instantiable log control blocks";
    private String ReportSettings="The report control block attributes for which online setting is possible with services\n" +
            "SetURCBValues respective SetBRCBValues:\n" +
            "The attribute’s meaning is:\n" +
            "cbName – control block name\n" +
            "datSet – data set reference\n" +
            "rptID – report identifier\n" +
            "optFields – optional fields to include in report\n" +
            "bufTime – buffer time\n" +
            "trgOps – trigger options enable\n" +
            "intgPd – integrity period\n" +
            "resvTms – if true, the ResvTms attribute exists at all buffered control blocks. In this\n" +
            "case, if the BRCB instance is allocated to a client, it should be configured as -1\n" +
            "(reserved), else as 0 (free).";
    private String LogSettings="The log control block attributes for which online setting is possible with service\n" +
            "SetLCBValues:\n" +
            "The attribute’s meaning is:\n" +
            "cbName – control block name\n" +
            "datSet – data set reference\n" +
            "logEna – log enable\n" +
            "trgOps – trigger options\n" +
            "intgPd – integrity period";
    private String GSESettings="The GSE control block attributes for which online setting is possible with service\n" +
            "SetGsCBValues respective SetGoCBValues:\n" +
            "The attribute’s meaning is:\n" +
            "cbName – control block name\n" +
            "datSet – data set reference\n" +
            "appID – application identifier\n" +
            "dataLabel – value for the object reference if the corresponding element ist being sent\n" +
            "(applies only to GSSE control blocks)";
    private String SMVSettings="The SMV control block attributes for which online setting is possible with service\n" +
            "SetMSVCBValues respective SetUSVCBValues:\n" +
            "The attribute’s meaning is:\n" +
            "cbName – control block name\n" +
            "datSet – data set reference\n" +
            "svID – sample value identifier\n" +
            "optFields – optional fields to include in sample value message\n" +
            "smpRate – sample rate per period is supported\n" +
            "samplesPerSec - samples per second resp. seconds per samples are supported\n" +
            "SMVSettings allows the following (sub-)elements:\n" +
            "SmpRate – defines the implemented sample rate(s) per period\n" +
            "SamplesPerSec – defines the implemented sample rate(s) per second\n" +
            "SecPerSamples – defines the implemented seconds between samples\n" +
            "If no appropriate elements are defined, the sample rate per period or per second as\n" +
            "defined by above attributes is assumed to be freely settable";
    private String GSEDir="GSE directory services according to IEC 61850-7-2. This capability has no attributes.";
    private String GOOSE="This element shows that the IED can be a GOOSE server or client according to\n" +
            "IEC 61850-7-2.\n" +
            "The attributes meaning is:\n" +
            "max = maximum number of GOOSE control blocks, which are configurable for\n" +
            "publishing (max=0 means the device is only a GOOSE client)";
    private String GSSE="This element shows that the IED can be a binary data GSSE server or client according\n" +
            "to IEC 61850-7-2.\n" +
            "The attributes meaning is:\n" +
            "max – maximum number of GSSE control blocks, which are configurable. Max=0\n" +
            "means only GSSE client.";
    private String FileHandling="All file handling services; without attributes";
    private String ConfLNs="Describes what can be configured for LNs defined in an ICD file\n" +
            "The attribute meanings are:\n" +
            "fixPrefix – if false, prefixes can be set/changed\n" +
            "fixLnInst – if false. LN instance numbers can be changed";
    private String ClientServices="Indicates which general service classes this IED can use as a client: goose, gsse,\n" +
            "sampled values (sv), unbuffered reporting (unbufReport), buffered reporting\n" +
            "(bufReport), reading logs (readLog). Default (missing element): supported client\n" +
            "services not known (except possibly from GOOSE/GSSE elements) – look into PICS.\n" +
            "Required for 2007 version. A pure client shall set at least one of the options to true.\n" +
            "Default for a missing attribute: false";

    public String getDynAssociation() {
        return DynAssociation;
    }

    public String getSettingGroups() {
        return SettingGroups;
    }

    public String getGetDirectory() {
        return GetDirectory;
    }

    public String getGetDataObjectDefinition() {
        return GetDataObjectDefinition;
    }

    public String getDataObjectDirectory() {
        return DataObjectDirectory;
    }

    public String getGetDataSetValue() {
        return GetDataSetValue;
    }

    public String getSetDataSetValue() {
        return SetDataSetValue;
    }

    public String getDataSetDirectory() {
        return DataSetDirectory;
    }

    public String getConfDataSet() {
        return ConfDataSet;
    }

    public String getDynDataSet() {
        return DynDataSet;
    }

    public String getReadWrite() {
        return ReadWrite;
    }

    public String getTimerActivatedControl() {
        return TimerActivatedControl;
    }

    public String getConfReportControl() {
        return ConfReportControl;
    }

    public String getGetCBValues() {
        return GetCBValues;
    }

    public String getConfLogControl() {
        return ConfLogControl;
    }

    public String getReportSettings() {
        return ReportSettings;
    }

    public String getLogSettings() {
        return LogSettings;
    }

    public String getGSESettings() {
        return GSESettings;
    }

    public String getSMVSettings() {
        return SMVSettings;
    }

    public String getGSEDir() {
        return GSEDir;
    }

    public String getGOOSE() {
        return GOOSE;
    }

    public String getGSSE() {
        return GSSE;
    }

    public String getFileHandling() {
        return FileHandling;
    }

    public String getConfLNs() {
        return ConfLNs;
    }

    public String getClientServices() {
        return ClientServices;
    }
}
