package com.noone.guardms.common.basemodel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Constant
{

    final public static String DEFAULT_PASSWORD = "asdasd123";
    final public static Long VIRTUAL_WAREHOUSE_ID = -1l;
    final public static String SESSIONUSER = "viewSessionUser";
    final public static String SYSTEM_ACCOUNT = "system.account";
    final public static String STRING_FORMAT_PLAN_CALC_PARAM_NAME = "format.plan.calcParamName";
    final public static String STRING_FORMAT_PLAN_CALC_RATE_NAME = "format.plan.calcRateName";
    final public static Long SystemOperatorId = -99999L;
    final public static String DefaultLanguage = "en-us";

    final public static String DEFAULT_PHONE_NUMBER = "000-000-0000";

    final public static int MicrosecondsInSec = 1000;
    final public static int SecondsInMinute = 60;
    final public static int MinutesInHour = 60;
    final public static int HoursInDay = 24;
    final public static int MicrosecondsInHour = 1000 * 60 * 60;
    final public static String DateTimeFormatString = "yyyy-MM-dd HH:mm:ss";
    final public static SimpleDateFormat DateTimeFormat = new SimpleDateFormat(DateTimeFormatString);
    final public static String DateTimeENUSFormatString = "HH:mm:ss MMM dd, yyyy z";
    final public static SimpleDateFormat DateTimeENUSFormat = new SimpleDateFormat(DateTimeENUSFormatString, Locale.US);
    final public static SimpleDateFormat DateTimeFormatForFileName = new SimpleDateFormat("yyyyMMddHHmmss");
    final public static SimpleDateFormat DateTimeFormatWithMicroSecondForFileName = new SimpleDateFormat(
            "yyyyMMddHHmmssSSS");
    final public static String DateFormatString = "yyyy-MM-dd";
    final public static SimpleDateFormat DateFormat = new SimpleDateFormat(DateFormatString);
    final public static String DateFormatSelectString = "yyyy/MM/dd";
    final public static SimpleDateFormat DateFormatSelect = new SimpleDateFormat(DateFormatSelectString);
    final public static String DateHMFormatString = "yyyy-MM-dd HH:mm";
    final public static SimpleDateFormat DateHMFormat = new SimpleDateFormat(DateHMFormatString);
    final public static SimpleDateFormat DateFormatForFile = new SimpleDateFormat("yyyyMMdd");

    final public static String ShortDateFormatString = "MMM-dd-yy";
    final public static SimpleDateFormat ShortDateFormat = new SimpleDateFormat(ShortDateFormatString);
    // final private static Logger logger = Logger.getLogger(Constant.class);
    //
    // private static String ContextPath="";

    public static String ApplicationName = "OmniSelling";
    public static String ApplicationVersion = "0.5a";
    public static String TsvSuffix = ".tsv";
    public static String CsvSuffix = ".csv";
    public static String TxtSuffix = ".txt";
    public static String xlsSuffix = ".xls";
    public static String xlsxSuffix = ".xlsx";
    public static final double KG_PER_LB = 2.20462;
    public static final double KG_PER_OZS = 35.27396;
    public static final double INCH_PER_CM = 0.393701;

    public static String ApplicationContextPath;
    public static String ENCODING = "GBK";
    public final static String UploadPath = "/resources/upload/";
    public final static String DownloadPath = "/tmp/omni/result/";
    public final static String FBA_PACKAGELABELS_NAME = "fbalabels";
    public final static String FBA_FILE_TYPE_ZIP = ".zip";
    public final static String FBA_FILE_TYPE_PDF = ".pdf";

    public final static String AMAZON_REPORT_IMPORT_FILE_PATH = "C:/fire/amazonreport/";
    public final static String AMAZON_PRODUCT_PATH = "C:/fire/product/";
    public final static String AMAZON_REPORT_EXPORT_FILE_PATH = "C:/fire/amazonreport/export";
    public final static String AMAZON_REPORT_EXPORT_PRODUCT_DATA_UPLOAD = "C:/fire/sku/data/upload/";
    public final static String AMAZON_REPORT_EXPORT_PRODUCT_DATA_DOWNLOAD = "C:/fire/sku/data/download/";
    public final static String AMAZON_PRODUCT_UPLOADPATH = "C:/fire/upload/product/";

    public final static String FINANCIAL_REPORT_UPLOADPATH = "C:/fire/upload/financialreport/";
    public final static String INVENTORY_REPORT_UPLOADPATH = "C:/fire/upload/inventoryreport/";

    public final static Integer exportMaxLimit = 6000;
    public final static Integer exportDefaultLimit = 30;

    public final static String ENUMRATION_PREFIX = "Enum.";
    public static final String WAREHOUSE_ISSUE_TYPE = "WRHSISSUE";
    
    public final static String DASHBOARD_SPLIT = "|";
}
