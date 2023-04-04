package models.file;

import java.sql.*;

public interface FileInputDBInterface {
    public void parseFromLine(String inputLine);
    public void insertToDB(Connection conn);
}
