import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

    private String envVar = System.getenv("DBCONTAINERNAME");

    private String mongodbUri;

    public MongoDBConfig() {
        if (!envVar.isEmpty()) {
            mongodbUri = envVar;
        }
        System.setProperty("mongodb.uri", mongodbUri);
    }
}
