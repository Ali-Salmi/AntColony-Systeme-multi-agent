package agents;

import UI.Ui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class IntermediaireAgent extends GuiAgent {
    private Ui ui;
    private String nomAgent;

    public IntermediaireAgent(){}
    public IntermediaireAgent(String Nom) {
        this.nomAgent = Nom;
    }
    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    @Override
    protected void setup() {
        if(getArguments().length==1){
            ui= (Ui) getArguments()[0];
            ui.setIntermediaireAgent(this);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg=receive();
                if(msg!=null){
                    System.out.println("sender: "+ msg.getSender().getName());
                    System.out.println("content: "+ msg.getContent());
                }
                else block();
            }
        });
        addBehaviour(parallelBehaviour);
    }
    public void sendMessage(String msgContent,String sendTo,int numberOfAclMessage){
        ACLMessage msg=new ACLMessage(numberOfAclMessage);
        msg.setContent(msgContent);
        msg.addReceiver(new AID(sendTo,AID.ISLOCALNAME));
        send(msg);
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {}
}
