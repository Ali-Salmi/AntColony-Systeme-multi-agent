package agents;

import UI.Ui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class AntAgent extends GuiAgent {
    private transient Ui acoDemo;

    @Override
    protected void setup() {
        if(getArguments().length==1){
            acoDemo= (Ui) getArguments()[0];
            acoDemo.setAntAgent(this);
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

    public void sendMessage(int epoch){
        ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setContent("donner moi le plus court chemin avec "+Integer.toString(epoch)+" epoch");
        msg.addReceiver(new AID("Intermediaire",AID.ISLOCALNAME));
        send(msg);
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {}
}
