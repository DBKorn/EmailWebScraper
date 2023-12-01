import sun.nio.ch.ThreadPool;

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
public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int EMAIL_MAX_COUNT = 10_000;
        String touro  = "https://www.touro.edu/";
        SQLDBConnection sq = new SQLDBConnection();

        ExecutorService pool = Executors.newFixedThreadPool(60);

        Scraper.linksToVisit.add(touro);
        String link;

        while ( Scraper.emails.size() <= EMAIL_MAX_COUNT){
            if (!Scraper.linksToVisit.isEmpty()) {
                link = Scraper.linksToVisit.remove(0);

                Scraper.linksVisited.add(link);
                pool.execute(new Scraper(link));

                System.out.println(Scraper.emails.size() + " -------------------------------- " + Scraper.linksToVisit.size());
            } else {
                Thread.sleep(1000);
            }
        }
        pool.shutdownNow();

/*      // this block of code is so I can look thru all the emails myself and see if anything might be off
        System.out.println("00000000000000000000000000000000000000000000");

        ArrayList<String> check = new ArrayList<>(Scraper.emails);

        for (int i = 0; i < check.size(); i++) {
            System.out.println(i + " ================= " + check.get(i));
        }
*/
        System.out.println(Scraper.emails.size() + " --------------------------------------------------------------------------------");
        sq.insertSetToDB(Scraper.emails);

    }
}
