package agents;

import UI.Ui;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import javax.swing.*;

public class AntAgent extends GuiAgent {
    private transient Ui ui;

    private String nomAgent;
    public AntAgent(){}
    public AntAgent(String Nom) {
        this.nomAgent = Nom;
    }
    public String getNomAgent() {
        return nomAgent;
    }

    @Override
    protected void setup() {
        if(getArguments().length==1){
            ui= (Ui) getArguments()[0];
            ui.setAntAgent(this);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg=receive();
                if(msg!=null){
                    JOptionPane.showMessageDialog(ui,
                            "Fin de l'algorithme",
                            "Le plus court chemin",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else block();
            }
        });
        addBehaviour(parallelBehaviour);
    }

    public void sendMessage(int epoch){
        int epochs=this.saisirEpoch(epoch);
        ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
        msg.setContent("donner moi le plus court chemin avec "+epochs+" epoch");
        msg.addReceiver(new AID("Intermediaire",AID.ISLOCALNAME));
        send(msg);
    }
    public int saisirEpoch(int epocks){
        return epocks;
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {}
}
