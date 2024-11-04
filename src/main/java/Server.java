import com.example.soap.PersonServiceImpl;
import jakarta.xml.ws.Endpoint;

import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws SQLException {
        String url= "http://localhost:7070/";
        Endpoint.publish(url, new PersonServiceImpl());
    }
}
