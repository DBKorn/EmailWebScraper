import junit.framework.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLDBConnectionTest extends TestCase {

    SQLDBConnection sq = new SQLDBConnection();


    public void testInsertListToDB() throws ClassNotFoundException {
        Set<String> emailsToAdd = new HashSet<>();
        emailsToAdd.add("shprintza1");
        emailsToAdd.add("shprintza2");
        emailsToAdd.add("shprintza3");
        emailsToAdd.add("shprintza4");

        sq.insertSetToDB(emailsToAdd);


        sq.qry();
    }

    public void testQ() throws ClassNotFoundException {
        sq.qry();
    }
}