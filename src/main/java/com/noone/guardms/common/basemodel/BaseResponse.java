package com.noone.guardms.common.basemodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.noone.guardms.common.basemodel.ErrorInfo.SEVERITY;


public abstract class BaseResponse<T>
{
    private List<ErrorInfo> errors;
    private T data;

    /**
     * Check if the response has data, if it's a collection make sure it has element
     */
    public boolean hasData()
    {
        if (data == null)
        {
            return false;
        }
         
        if (data instanceof Collection){
            Collection<?> list = (Collection<?>) data;
            return list.size()>0;
        }
        return true;
    }
    
    /**
     * Check if the response has any errors
     */    
    public boolean hasError()
    {
        if (errors != null && errors.size() > 0)
        {
            for (ErrorInfo error : errors)
            {
                if (error.getSeverity() == SEVERITY.ERROR)
                {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Check if the response has any Warnings or Errors
     */ 
    public boolean hasWarning()
    {
        if (errors != null && errors.size() > 0)
        {
            for (ErrorInfo error : errors)
            {
                if (error.getSeverity() == SEVERITY.WARN || error.getSeverity() == SEVERITY.ERROR)
                {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Check if the response has any Status: (INFO, WARNING, ERROR)
     */ 
    public boolean hasStatus()
    {
        if (errors != null && errors.size() > 0)
        {
            return true;
        }
        return false;
    }
    
    public void addError(String errorInfo)
    {
        if (errors == null)
        {
            errors = new ArrayList<ErrorInfo>();
        }
        ErrorInfo error = new ErrorInfo(errorInfo);
        errors.add(error);
    }

    public void addError(ErrorInfo error)
    {
        if (errors == null)
        {
            errors = new ArrayList<ErrorInfo>();
        }
        errors.add(error);
    }

    public void addErrors(List<ErrorInfo> errors)
    {
        if (errors == null || errors.size() == 0)
        {
            return;
        }
        if (this.errors == null)
        {
            this.errors = new ArrayList<ErrorInfo>();
        }
        this.errors.addAll(errors);
    }

    public List<ErrorInfo> getErrors()
    {
        return errors;
    }

    public String getErrorsToString()
    {
        String res = "";
        if (errors != null)
        {
            res += "[";
            for (int i = 0; i < errors.size(); i++)
            {
                ErrorInfo err = errors.get(i);
                if (i == 0)
                {
                    res += err.toString();
                }
                else
                {
                    res += "," + err.toString();
                }
            }
            res += "]";
        }
        return res;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        String res = "";
        if (data != null)
        {
            res += "data=" + data.toString();
        }
        res += " errors=" + getErrorsToString();

        return this.getClass().getSimpleName() + " [" + res + "]";

    }

}
