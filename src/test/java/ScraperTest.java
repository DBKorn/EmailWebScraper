//Dovid Korn
import junit.framework.TestCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScraperTest extends TestCase {
    String touro = "https://www.touro.edu/";
    Scraper s = new Scraper(touro);



    public void testPullLinks() {
        assertEquals(true, Scraper.linksToVisit.size() == 0);

        Document d = s.URLtoDocument(touro);
        s.pullLinks(d);
        assertEquals(true, Scraper.linksToVisit.size() > 0);
    }

    public void testPullEmails() {
        assertEquals(true, Scraper.emails.size() == 0);
        Document d = s.URLtoDocument("https://www.touro.edu/directory/");
        s.pullEmails(d.text());
        assertEquals(true, Scraper.emails.size() > 0);
    }

    public void testWithinLimit(){

        String a = "aaaaaaaaaaaaaaaaaaaaaaaaaaa/";
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            al.add(a);
        }


        for (int i = 0; i < al.size(); i++) {

            if (i > Scraper.limitLink){
                assertEquals(false, s.withinLimit(al.get(i)));
                continue; //just wanted to try out continue keyword
            }

            assertEquals(true, s.withinLimit(al.get(i)));
        }
    }

    public void testRootGetter(){
        Document d = s.URLtoDocument(touro);
        s.pullLinks(d);

        for (String i : Scraper.linksToVisit) {
            System.out.println(s.rootGetter(i));
        }



        //System.out.println(s.rootGetter("https://www.touro.edu/about/careers-at-touro/"));
    }

}