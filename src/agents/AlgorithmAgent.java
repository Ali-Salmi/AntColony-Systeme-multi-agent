package agents;

import UI.Ui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class AlgorithmAgent extends GuiAgent {
    private Ui ui;
    private String nomAgent;
    public AlgorithmAgent(){}
    public AlgorithmAgent(String Nom) {
        this.nomAgent = Nom;
    }

    public String getNomAgent() {
        return this.nomAgent;
    }

    public String getAgentName(String s){
        String name = "";
        for(int i=0;i<s.length();i++){
            if( Character.compare(s.charAt(i), '@') != 0){
                name+=s.charAt(i);
            }
            else return name;
        }
        return name;
    }
    @Override
    protected void setup() {
        if(getArguments().length==1){
            ui= (Ui) getArguments()[0];
            ui.setAlgorithmAgent(this);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg=receive();
                if(msg!=null){
                    if(getAgentName(msg.getSender().getName()).equals("Intermediaire")){
                        ACLMessage aclMessage=msg.createReply();
                        aclMessage.setContent("Algorithme est démarré");
                        aclMessage.setPerformative(ACLMessage.AGREE);
                        send(aclMessage);
                    }
                }
                else block();
            }
        });
        addBehaviour(parallelBehaviour);
    }

    public void sendMessage(String textContent,int ACLMessageNum){
        ACLMessage msg=new ACLMessage(ACLMessageNum);
        msg.setContent(textContent);
        msg.addReceiver(new AID("Intermediaire",AID.ISLOCALNAME));
        send(msg);
    }
    public void runAlgorithm(int epochs,int delai){
        ui.runAnts(epochs,delai );
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {}
}
