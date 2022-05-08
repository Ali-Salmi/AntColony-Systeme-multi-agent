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
    public ACLMessage msg1;
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
            ui.setIntermediaireAgent(this);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg=receive();
                if(msg!=null){
                    if(getAgentName(msg.getSender().getName()).equals("Ant")){
                        msg1=msg;
                        System.out.println(getAgentName(msg.getSender().getName()));
                        msg1.setPerformative(ACLMessage.INFORM);
                    }
                }
                else block();
                if(msg!=null){
                    if(msg.getContent().equals("Fin de l'algorithme")){
                        ACLMessage aclMessage=msg1.createReply();
                        aclMessage.setContent("le plus court chemin est prêt..!");
                        send(aclMessage);
                    }
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
