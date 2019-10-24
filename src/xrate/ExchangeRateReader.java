package xrate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Provide access to basic currency exchange rate services.
 */
public class ExchangeRateReader {

    private String accessKey;
    private String urlString;
    private URL url;

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     */
    public ExchangeRateReader(String baseURL) throws IOException {
        /*
         * DON'T DO MUCH HERE!
         * People often try to do a lot here, but the action is actually in
         * the two methods below. All you need to do here is store the
         * provided `baseURL` in a field so it will be accessible later.
         */
        urlString = baseURL;
        url = new URL(urlString);
        // TODO Your code here

        // Reads the access keys from `etc/access_keys.properties`
        readAccessKeys();
    }

    /**
     * This reads the `fixer_io` access key from `etc/access_keys.properties`
     * and assigns it to the field `accessKey`.
     *
     * @throws IOException if there is a problem reading the properties file
     */
    private void readAccessKeys() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            // Don't change this filename unless you know what you're doing.
            // It's crucial that we don't commit the file that contains the
            // (private) access keys. This file is listed in `.gitignore` so
            // it's safe to put keys there as we won't accidentally commit them.
            in = new FileInputStream("etc/access_keys.properties");
        } catch (FileNotFoundException e) {
            /*
             * If this error gets generated, make sure that you have the desired
             * properties file in your project's `etc` directory. You may need
             * to rename the file ending in `.sample` by removing that suffix.
             */
            System.err.println("Couldn't open etc/access_keys.properties; have you renamed the sample file?");
            throw(e);
        }
        properties.load(in);
        // This assumes we're using Fixer.io and that the desired access key is
        // in the properties file in the key labelled `fixer_io`.
        accessKey = properties.getProperty("fixer_io");
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException if there are problems reading from the server
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        // TODO Your code here

        String strmonth;

        if (Integer.toString(month).length() == 1) {
            strmonth = "0" + month;
        } else {
            strmonth = Integer.toString(month);
        }

        String strday;

        if (Integer.toString(day).length() == 1) {
            strday = "0" + day;
        } else {
            strday = Integer.toString(day);
        }


        String tempurlstring = (urlString + year + "-" + strmonth + "-" + strday + "?access_key=" + accessKey);
        JsonObject rateJSON = returnJSON(tempurlstring);

        JsonObject rates = rateJSON.getAsJsonObject("rates");
        float rate = rates.get(currencyCode).getAsFloat();
        return rate;
    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException if there are problems reading from the server
     */
    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException {
        // TODO Your code here

        String strmonth;

        if (Integer.toString(month).length() == 1) {
            strmonth = "0" + month;
        } else {
            strmonth = Integer.toString(month);
        }

        String strday;

        if (Integer.toString(day).length() == 1) {
            strday = "0" + day;
        } else {
            strday = Integer.toString(day);
        }




        String tempurlstring = (urlString + year + "-" + strmonth + "-" + strday + "?access_key=" + accessKey);
        JsonObject rateJSON = returnJSON(tempurlstring);


        JsonObject rates = rateJSON.getAsJsonObject("rates");
        float rate1 = rates.get(fromCurrency).getAsFloat();
        float rate2 = rates.get(toCurrency).getAsFloat();
        float ratefinal = rate1 / rate2;
        return ratefinal;
    }

    public JsonObject returnJSON(String tempurlstring) throws IOException{
        URL tempurl = new URL(tempurlstring);
        InputStream inputStream = tempurl.openStream();

        InputStreamReader jsonreader = new InputStreamReader(inputStream);


        JsonObject rateJSON = new JsonParser().parse(jsonreader).getAsJsonObject();
        return rateJSON;
    }

}