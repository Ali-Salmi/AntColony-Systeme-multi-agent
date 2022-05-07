import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class mainContainer {
    public static void main(String[] args) throws Exception {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.GUI, "true");
        AgentContainer agentContainer = runtime.createMainContainer(profile);
        agentContainer.start();
    }
}
