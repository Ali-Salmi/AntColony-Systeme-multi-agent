package agents;

import UI.Ui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class AlgorithmAgent extends GuiAgent {
    private Ui acoDemo;

    @Override
    protected void setup() {
        if(getArguments().length==1){
            acoDemo= (Ui) getArguments()[0];
            acoDemo.setAlgorithmAgent(this);
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

    public void sendMessage(String textContent,int ACLMessageNum){
        ACLMessage msg=new ACLMessage(ACLMessageNum);
        msg.setContent(textContent);
        msg.addReceiver(new AID("Intermediaire",AID.ISLOCALNAME));
        send(msg);
    }
    public void runAlgorithm(int epochs,int delai){
        acoDemo.runAnts(epochs,delai );
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {}
}
