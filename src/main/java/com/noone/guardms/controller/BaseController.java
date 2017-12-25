package com.noone.guardms.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noone.guardms.common.basemodel.ClientRequest;


public class BaseController {
	
	
	protected final Logger logger;
    private final ObjectMapper mapper;

    public BaseController()
    {
        logger = Logger.getLogger(this.getClass());
        mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
    }
	
	protected void traceGetIntoMethod(String methodName)
    {
        logger.info("Get into " + methodName);
    }

    protected void traceGetOffFromMethod(String methodName)
    {
        logger.info("Get off from " + methodName);
    }
	
    
    protected <T> T parseOmniRequestData(ClientRequest<T> req, Class<T> clazz)
    {
        String json = null;
        T pojo = null;
        try
        {
            json = mapper.writeValueAsString(req.getData());
            pojo = mapper.readValue(json, clazz);
        }
        catch (Exception e)
        {
            logger.error("json parse error :" + e.getMessage(), e);
            return null;
        }
        if (json == null)
        {
            logger.error("json string is null. clazz=" + clazz.getSimpleName());
            return null;
        }

        if (pojo == null)
        {
            logger.info("json String:" + json);
            logger.error("pojo is null." + clazz.getSimpleName());
            return null;
        }

        return pojo;
    }

    protected void refreshTheSessionId(HttpServletRequest request)
    {
        request.getSession().invalidate();
        request.getSession(true);
    }

    protected String getRequestHostIpAdress(HttpServletRequest request)
    {
        String ipaddress = request.getHeader("X-Forwarded-For");

        if (logger.isInfoEnabled())
        {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ipaddress=" + ipaddress);
        }

        if (ipaddress == null || ipaddress.length() == 0 || "unknown".equalsIgnoreCase(ipaddress))
        {
            if (ipaddress == null || ipaddress.length() == 0 || "unknown".equalsIgnoreCase(ipaddress))
            {
                ipaddress = request.getHeader("Proxy-Client-ipaddress");
                if (logger.isInfoEnabled())
                {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-ipaddress - String ipaddress="
                            + ipaddress);
                }
            }
            if (ipaddress == null || ipaddress.length() == 0 || "unknown".equalsIgnoreCase(ipaddress))
            {
                ipaddress = request.getHeader("WL-Proxy-Client-ipaddress");
                if (logger.isInfoEnabled())
                {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-ipaddress - String ipaddress="
                            + ipaddress);
                }
            }
            if (ipaddress == null || ipaddress.length() == 0 || "unknown".equalsIgnoreCase(ipaddress))
            {
                ipaddress = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled())
                {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ipaddress=" + ipaddress);
                }
            }
            if (ipaddress == null || ipaddress.length() == 0 || "unknown".equalsIgnoreCase(ipaddress))
            {
                ipaddress = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled())
                {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ipaddress="
                            + ipaddress);
                }
            }
            if (ipaddress == null || ipaddress.length() == 0 || "unknown".equalsIgnoreCase(ipaddress))
            {
                ipaddress = request.getRemoteAddr();
                if (logger.isInfoEnabled())
                {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ipaddress=" + ipaddress);
                }
            }
        }
        else if (ipaddress.length() > 15)
        {
            String[] ips = ipaddress.split(",");
            for (int index = 0; index < ips.length; index++)
            {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp)))
                {
                    ipaddress = strIp;
                    break;
                }
            }
        }

        logger.trace("Request from: " + ipaddress);
        return ipaddress;
    }

    protected void sendCsvFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (fileName.toLowerCase().endsWith(".csv"))
        {
            sendFileToPage(filePath, fileName, "text/csv", resp);
        }
        else if (fileName.toLowerCase().endsWith(".xls"))
        {
            sendFileToPage(filePath, fileName, "application/vnd.ms-excel", resp);
        }
        else if (fileName.toLowerCase().endsWith(".xlsx"))
        {
            sendFileToPage(filePath, fileName, "application/x-excel", resp);
        }
    }

    protected void sendPlainTextFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".txt"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "text/plain", resp);
    }

    protected void sendXmlFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".xml"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "application/xml", resp);
    }

    protected void sendPdfFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".pdf"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "application/pdf", resp);
    }
    
    protected void sendGifFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".gif"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "image/gif", resp);
    }
    
    protected void sendJpegFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".jpeg"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "image/jpeg", resp);
    }

    protected void sendPngFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".png"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "image/png", resp);
    }

    protected void sendHtmlFileToPage(String filePath, String fileName, HttpServletResponse resp)
    {
        if (!fileName.toLowerCase().endsWith(".html"))
        {
            return;
        }
        sendFileToPage(filePath, fileName, "text/plain", resp);
    }

    protected void sendFileToPage(HttpServletResponse resp, String fileName, byte[] fileContent)
    {
        ServletOutputStream sOutputStream = null;
        try
        {
            sOutputStream = resp.getOutputStream();
        }
        catch (Exception e)
        {
            sOutputStream = null;
            logger.error("sendFileToPage IO Exception :" + e.getMessage(), e);
        }
        if (sOutputStream == null)
        {
            return;
        }
        MimetypesFileTypeMap mftm = new MimetypesFileTypeMap();
        String contentType = mftm.getContentType(fileName);
        if (fileName.toLowerCase().endsWith(".pdf"))
        {
            contentType = "application/pdf";
            resp.setHeader("Content-Disposition", "inline");
        }
        if (fileName.toLowerCase().endsWith(".txt"))
        {
            contentType = "text/plain";
            resp.setHeader("Content-Disposition", "inline");
        }
        else if (fileName.toLowerCase().endsWith(".rtf"))
        {
            contentType = "application/msword";
            resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        else
        {
            resp.setHeader("Content-Disposition", "inline");
        }
        resp.setContentType(contentType);
        resp.setHeader("Content-Length", "" + fileContent.length);

        try
        {
            sOutputStream.write(fileContent, 0, fileContent.length);
            sOutputStream.flush();
            sOutputStream.close();
            resp.flushBuffer();
        }
        catch (Exception e)
        {
            logger.error("sendFileToPage IO Exception :" + e.getMessage(), e);
        }
        sOutputStream = null;
    }

    private void sendFileToPage(String filePath, String fileName, String contentType, HttpServletResponse resp)
    {
        String wholeName = filePath + fileName;
        logger.trace("Will download file: " + wholeName);
        File file = new File(wholeName);
        ServletOutputStream sOutputStream = null;
        try
        {
            sOutputStream = resp.getOutputStream();
        }
        catch (Exception e)
        {
            sOutputStream = null;
            logger.error("sendFileToPage IO Exception :" + e.getMessage(), e);
        }
        if (sOutputStream == null)
        {
            return;
        }
        
        resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
        resp.setHeader("Content-Disposition", "inline; filename=" + fileName);
        resp.setHeader("Content-Length", String.valueOf(file.length()));

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (Exception e)
        {
            fis = null;
            logger.error("file not found error :" + e.getMessage(), e);
        }
        if (fis == null)
        {
            return;
        }
        BufferedInputStream bufis = new BufferedInputStream(fis);
        byte[] cacheBuf = new byte[1024];
        long readLength = 0l;

        try
        {
            while (readLength < file.length())
            {
                int j = bufis.read(cacheBuf, 0, 1024);
                readLength += j;
                sOutputStream.write(cacheBuf, 0, j);
            }
            sOutputStream.flush();
            sOutputStream.close();
            resp.flushBuffer();
            bufis.close();
            fis.close();
        }
        catch (Exception e)
        {
            logger.error("sendFileToPage IO Exception :" + e.getMessage(), e);
        }
        sOutputStream = null;
    }
    
    
    
    
    
}
