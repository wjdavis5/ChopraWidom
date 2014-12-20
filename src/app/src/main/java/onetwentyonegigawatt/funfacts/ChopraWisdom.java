package onetwentyonegigawatt.funfacts;


import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by William.Davis on 12/19/2014.
 */
public class ChopraWisdom{

    public String BaseUrl;
    public static String SplitLeft = "<meta property=\"og:description\" content=\"'";
    public static String SplitRight = "' www.wisdomofchopra.com";
    public ChopraWisdom(String baseUrl)
    {
        BaseUrl = baseUrl;
    }


    public String GetQuote() throws IOException {

        String quote = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(BaseUrl);
        HttpResponse response = null;
        response = httpClient.execute(httpGet, localContext);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        response.getEntity().getContent()
                )

        );

        StringBuilder total = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            total.append(line);
        }
        String[] x = total.toString().split(SplitLeft);
        String[] y = x[1].split(SplitRight);
        return y[0];
    }

}
