import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import utils.Position;
import utils.Scenario;

public class
App {

    public static void main(String[] args) throws StaleProxyException {
        Properties properties = new Properties();
        properties.setProperty(Profile.GUI, Boolean.TRUE.toString());
        AgentContainer mainContainer = Runtime.instance().createMainContainer(new ProfileImpl(properties));
        Position []positions = new Position[3];
        positions[0] = new Position(0,0);
        positions[1] = new Position(50,50);
        positions[2] = new Position(50,0);
        Factory factory = new Factory(new Scenario(100, 100,positions), mainContainer);

    }
}