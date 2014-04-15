
package org.apache.beehive.netui.test.databinding.beans;

import java.io.Serializable;

public class b40682
    implements java.io.Serializable
{
    public static class Report
        implements Serializable
    {
        private int _number;
        private String _url;
        private String _text;
        private Company[] _companies;

        public int getNumber()
        {
            return _number;
        }

        public void setNumber(int number)
        {
            _number = number;
        }

        public String getUrl()
        {
            return _url;
        }

        public void setUrl(String url)
        {
            _url = url;
        }

        public String getText()
        {
            return _text;
        }

        public void setText(String text)
        {
            _text = text;
        }

        public Company[] getCompanies()
        {
            return _companies;
        }

        public void setCompanies(Company[] companies)
        {
            _companies = companies;
        }
    }

    public static class Company
        implements Serializable
    {
        private String _symbol;
        private String _status;

        public Company() {}

        public Company(String symbol, String status)
        {
            _symbol = symbol;
            _status = status;
        }

        public String getStatus()
        {
            return _status;
        }

        public void setStatus(String status)
        {
            _status = status;
        }
        
        public String getSymbol()
        {
            return _symbol;
        }

        public void setSymbol(String symbol)
        {
            _symbol = symbol;
        }
    }

}
