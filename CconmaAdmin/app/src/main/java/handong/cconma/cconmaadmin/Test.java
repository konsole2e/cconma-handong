package handong.cconma.cconmaadmin;

import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Seoyul on 2015-07-10.
 */
public class Test extends AsyncTask<String, Void, String> {

    private URL url;
    private XmlPullParserFactory factory;
    private XmlPullParser xpp;
    private int eventType;
    private String tag;
    private String result = null;

    protected String doInBackground(String... strings) {

        Random rand = new Random(16);
        ArrayList<String> hour = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> wfKor = new ArrayList<String>();

        try{

            url = new URL("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=73&gridy=116");

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(url.openStream(), "utf-8");

            eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){

                if(eventType == XmlPullParser.START_TAG){

                    tag = xpp.getName();

                }else if(eventType == XmlPullParser.TEXT){

                    if(tag.equals("hour"))
                        hour.add(xpp.getText());
                    else if(tag.equals("temp"))
                        temp.add(xpp.getText());
                    else if(tag.equals("wfKor"))
                        wfKor.add(xpp.getText());

                }else if(eventType == XmlPullParser.END_TAG){

                    tag = "";

                }

                eventType = xpp.next();

            }

        }catch (Exception e) {

            e.printStackTrace();

        }

        //int pick = rand.nextInt();
        result = "hour : " + hour.get(1) + " temp : " + temp.get(1) + " wfKor : " + wfKor.get(1);

        return result;

    }

    public String getWeather(){

        this.execute();

        while(result == null);

        return result;

    }

}
