//Dovid Korn

import java.util.Set;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper implements Runnable {

    public static Set<String> emails = Collections.synchronizedSet(new HashSet<String>());

    public static List<String> linksToVisit = Collections.synchronizedList(new LinkedList<>());
    public static Set<String> linksVisited = Collections.synchronizedSet(new HashSet<String>());

    private static Map<String, Integer> limiter = Collections.synchronizedMap(new HashMap<String, Integer>());
    static final int limitLink = 15;

    boolean badLink = false;

    private String link;

    public Scraper(String link){
        this.link = link;
    }



    void pullEmails(String text){
        String regex = "[a-zA-Z0-9\\.\\-\\_]+@[a-zA-Z]+[\\.]{1}[a-zA-Z]{2,4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            emails.add(canonicalFormatizer(matcher.group()));
        }

    }

    void pullLinks(Document doc){


        Elements scrapedUrls = doc.select("a[href]");
        for (Element tag_a : scrapedUrls) {
            String str = tag_a.attr("abs:href");
            if (!str.isEmpty()){
                URL url = null;
                try {
                    url = new URL(str);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (!linksVisited.contains(str) && withinLimit(str)){ //we check if 1] we already used that link, 2] if its kosher, 3] if within limit
                    System.out.println(str);
                    linksToVisit.add(str);
                }
            }
        }
    }

    Document URLtoDocument(String urlAsStr){

        Document doc = null;
        try {
            doc = Jsoup.connect(urlAsStr).ignoreContentType(true).get();
        } catch (IOException e) {
            e.printStackTrace();
            badLink = true;
        }

        return doc;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Document doc = URLtoDocument(this.link);
        if (badLink){return;}
        pullLinks(doc);
        pullEmails(doc.text());
    }
    private String canonicalFormatizer(String email){
        return email.toLowerCase();
    }

    boolean withinLimit(String str){

        String root = rootGetter(str);

        if  (root == null){return false;}

        Integer linkCount = limiter.get(root);
        if  (linkCount == null){
            limiter.put(root, 1);
            return true;
        }

        if (linkCount <= limitLink){
            limiter.put(root, linkCount+1);
            return true;
        } else {
            //limiter.put(root, linkCount+1);
            return false;
        }

    }

    String rootGetter(String str){ // I would have used getAuthority() but that was slowing things down a ton. No clue as to how or why

        try {
            str = str.substring(8);
            str = str.substring(0, str.indexOf('/'));
        } catch (Exception e){
            return null;
        }
        return str;
    }
/*
    String authorityGetter(String urlAsStr){
        String authority = null; // host name

        try {
            URL mainURL = new URL(urlAsStr);
            authority = mainURL.getAuthority();
        } catch (Exception e){}

        return authority;
    }

 */
}
