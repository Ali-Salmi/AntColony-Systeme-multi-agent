package UI;

import algorithm.AntColony;
import algorithm.TSP;
import agents.AlgorithmAgent;
import agents.AntAgent;
import agents.IntermediaireAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Ui extends JFrame implements Runnable {

  private static final long serialVersionUID = 0x00010006L;
  private boolean      isprog  = false;
  private JScrollPane  scroll  = null;
  private UiPanel panel   = null;
  private JTextField   stat    = null;
  private JDialog      runopt  = null;
  private TSP tsp     = null;
  private JDialog      randtsp = null;
  private JDialog      antcol  = null;
  private Timer        timer   = null;
  private int          cnt     = -1;
  private AntAgent antAgent=new AntAgent("Ant");
  private IntermediaireAgent intermediaireAgent=new IntermediaireAgent("Intermediaire");
  private AlgorithmAgent algorithmAgent=new AlgorithmAgent("Algorithm");

  //deploy agents
  public void startContainer(String name,String className) throws Exception {
    Runtime runtime=Runtime.instance();
    ProfileImpl profile=new ProfileImpl();
    profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
    AgentContainer agentContainer=runtime.createAgentContainer(profile);
    AgentController agentController=agentContainer
            .createNewAgent(name,className,new Object[] {this});
    agentController.start();
  }


  private void genTSP (int vertcnt, long seed)
  {
    if (this.cnt >= 0) return;
    Random rand = (seed > 0) ? new Random(seed) : new Random();
    this.tsp = new TSP(vertcnt, rand);
    this.tsp.transform(10.0, 0, 0);
    this.panel.setTSP(Ui.this.tsp);
    this.repaint();
  }  /* genTSP() */

  private JDialog createRandTSP ()
  {
    final JDialog      dlg  = new JDialog(this, "Générer les sources de nouriture aléatoirement...");
    GridBagLayout      g    = new GridBagLayout();
    GridBagConstraints lc   = new GridBagConstraints();
    GridBagConstraints rc   = new GridBagConstraints();
    JPanel             grid = new JPanel(g);
    JPanel             bbar;
    JLabel             lbl;
    JButton            btn;
    grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    lc.fill= rc.fill= GridBagConstraints.BOTH;
    rc.gridwidth = GridBagConstraints.REMAINDER;
    lbl = new JLabel("Nombre de sources de nouriture:");
    g.setConstraints(lbl, lc);     grid.add(lbl);
    final JSpinner vertcnt = new JSpinner(
            new SpinnerNumberModel(30, 5, 1000, 1));
    g.setConstraints(vertcnt, rc); grid.add(vertcnt);

    bbar = new JPanel(new GridLayout(1, 2, 5, 5));
    bbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
    btn = new JButton("Ok"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        dlg.setVisible(false);
        Ui.this.genTSP(((Integer)vertcnt.getValue()).intValue(),
                0);
      } } );
    btn = new JButton("Appliquer"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        Ui.this.genTSP(((Integer)vertcnt.getValue()).intValue(),
                0);
      } } );
    btn = new JButton("Annuler"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        dlg.setVisible(false); } } );

    dlg.getContentPane().add(grid, BorderLayout.CENTER);
    dlg.getContentPane().add(bbar, BorderLayout.SOUTH);
    dlg.setLocationRelativeTo(this);
    dlg.setLocation(740, 440);
    dlg.pack();
    return dlg;
  }  /* createRandTSP() */

  private JDialog createAnts ()
  {
    final JDialog      dlg  = new JDialog(this, "Créer une colonie de fourmis...");
    GridBagLayout      g    = new GridBagLayout();
    GridBagConstraints lc   = new GridBagConstraints();
    GridBagConstraints rc   = new GridBagConstraints();
    JPanel             grid = new JPanel(g);
    JPanel             bbar;
    JLabel             lbl;
    JButton            btn;

    grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    lc.fill      =
            rc.fill      = GridBagConstraints.BOTH;
    rc.gridwidth = GridBagConstraints.REMAINDER;

    lbl = new JLabel("Nombre de fourmis:");
    g.setConstraints(lbl, lc);     grid.add(lbl);
    final JSpinner antcnt = new JSpinner(
            new SpinnerNumberModel(30, 1, 999999, 1));
    g.setConstraints(antcnt, rc);  grid.add(antcnt);

    bbar = new JPanel(new GridLayout(1, 2, 5, 5));
    bbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
    btn = new JButton("Ok"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        dlg.setVisible(false);
        int s = 0;
        Ui.this.panel.initAnts(
                ((Integer)antcnt.getValue()).intValue(),
                Double.parseDouble("0"),
                (s != 0) ? new Random(s) : new Random());
        Ui.this.panel.setParams(
                Double.parseDouble("0.2"),
                Double.parseDouble("1"),
                Double.parseDouble("1"),
                Double.parseDouble("0.1"),
                Double.parseDouble("1"),
                Double.parseDouble("0.1"));
      } } );
    btn = new JButton("Appliquer"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        int s = 0;
        Ui.this.panel.initAnts(
                ((Integer)antcnt.getValue()).intValue(),
                Double.parseDouble("0"),
                (s != 0) ? new Random(s) : new Random());
        Ui.this.panel.setParams(
                Double.parseDouble("0.2"),
                Double.parseDouble("1"),
                Double.parseDouble("1"),
                Double.parseDouble("0.1"),
                Double.parseDouble("1"),
                Double.parseDouble("0.1"));
      } } );
    btn = new JButton("Annuler"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        dlg.setVisible(false); } } );
    dlg.getContentPane().add(grid, BorderLayout.CENTER);
    dlg.getContentPane().add(bbar, BorderLayout.SOUTH);
    dlg.setLocationRelativeTo(this);
    dlg.setLocation(740, 440);
    dlg.pack();
    return dlg;
  }  /* createAnts() */

  public void runAnts(int epochs, int delay)
  {
    if (this.cnt >= 0) {
      this.timer.stop(); this.cnt = -1;
      return; }
    AntColony ants = this.panel.getAnts();
    if (ants == null) return;
    if (delay <= 0) {
      while (--epochs >= 0)
        this.panel.runAnts();
      this.panel.repaint();
      this.stat.setText("epoch: " +ants.getEpoch());
      return;
    }
    this.cnt   = epochs;
    this.timer = new Timer(delay, new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        if (--Ui.this.cnt < 0) {
          algorithmAgent.sendMessage("Fin de l'algorithme",ACLMessage.INFORM);
          Ui.this.timer.stop();

          return; }
        Ui.this.panel.runAnts();
        Ui.this.panel.repaint();
        AntColony ants = Ui.this.panel.getAnts();
        Ui.this.stat.setText("epoch: " +ants.getEpoch());
      } } );
    this.timer.start();
  }  /* runAnts() */

  public void setAntAgent(AntAgent antAgent) {
    this.antAgent = antAgent;
  }

  public void setIntermediaireAgent(IntermediaireAgent intermediaireAgent) {this.intermediaireAgent = intermediaireAgent;}

  public void setAlgorithmAgent(AlgorithmAgent algorithmAgent) {this.algorithmAgent = algorithmAgent;}

  private JDialog createRunOpt ()
  {
    final JDialog      dlg  = new JDialog(this, "Démarrer l'optimization...");
    GridBagLayout      g    = new GridBagLayout();
    GridBagConstraints lc   = new GridBagConstraints();
    GridBagConstraints rc   = new GridBagConstraints();
    JPanel             grid = new JPanel(g);
    JPanel             bbar;
    JLabel             lbl;
    JButton            btn;

    grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    lc.fill      =
            rc.fill      = GridBagConstraints.BOTH;
    rc.weightx   = 1.0;
    lc.weightx   = 0.0;
    lc.weighty   = 0.0;
    rc.weighty   = 0.0;
    lc.ipadx     = 10;
    lc.ipady     = 10;
    rc.gridwidth = GridBagConstraints.REMAINDER;

    lbl = new JLabel("Nombre des epochs:");
    g.setConstraints(lbl, lc);     grid.add(lbl);
    final JSpinner epochs = new JSpinner(
            new SpinnerNumberModel(5000, 1, 999999, 1));
    g.setConstraints(epochs, rc);  grid.add(epochs);
    bbar = new JPanel(new GridLayout(1, 2, 5, 5));
    bbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
    btn = new JButton("Ok"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        dlg.setVisible(false);
        //ACODemo.this.runAnts(((Integer)epochs.getValue()).intValue(), 100);
        antAgent.sendMessage(((Integer)epochs.getValue()).intValue());
        intermediaireAgent.sendMessage("Démarrer l'algorithme avec"+((Integer)epochs.getValue()).intValue()+" epochs","Algorithm",ACLMessage.REQUEST);
        //algorithmAgent.sendMessage("Algorithme est démarré",ACLMessage.AGREE);
        algorithmAgent.runAlgorithm(((Integer)epochs.getValue()).intValue(),100);
      } } );
    btn = new JButton("Annuler"); bbar.add(btn);
    btn.addActionListener(new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        dlg.setVisible(false); } } );

    dlg.getContentPane().add(grid, BorderLayout.CENTER);
    dlg.getContentPane().add(bbar, BorderLayout.SOUTH);
    dlg.setLocationRelativeTo(this);
    dlg.setLocation(740, 440);
    dlg.pack();
    return dlg;
  }  /* createRunOpt() */

  public void run ()
  {
    try {
      startContainer(this.antAgent.getNomAgent(),"agents.AntAgent");
      startContainer(this.intermediaireAgent.getNomAgent(),"agents.IntermediaireAgent");
      startContainer(this.algorithmAgent.getNomAgent(),"agents.AlgorithmAgent");
    } catch (Exception e) {
      e.printStackTrace();
    }
    JMenuBar  mbar;
    JMenu     menu;
    JMenuItem item;

    this.getContentPane().setLayout(new BorderLayout());

    mbar = new JMenuBar();
    this.getContentPane().add(mbar, BorderLayout.NORTH);

    menu = mbar.add(new JMenu("Actions"));
    item = menu.add(new JMenuItem("Générer les sources de nouriture..."));
    item.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        if (Ui.this.randtsp == null)
          Ui.this.randtsp = createRandTSP();
        Ui.this.randtsp.setVisible(true);
      } } );
    item = menu.add(new JMenuItem("Créer une colonie de fourmis..."));
    item.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        if (Ui.this.antcol == null)
          Ui.this.antcol = createAnts();
        Ui.this.antcol.setVisible(true);
      } } );
    item = menu.add(new JMenuItem("Démarer l'optimization..."));
    item.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        Ui.this.runopt = createRunOpt();
        Ui.this.runopt.setVisible(true);
      } } );

    menu.addSeparator();
    item = menu.add(new JMenuItem("Repaint"));
    item.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        Ui.this.panel.repaint(); } } );

    this.panel = new UiPanel();
    this.panel.setLayout(new BorderLayout());
    this.panel.setPreferredSize(new Dimension(800, 656));
    this.scroll = new JScrollPane(this.panel);
    this.getContentPane().add(this.scroll, BorderLayout.CENTER);

    this.stat = new JTextField("");
    this.stat.setEditable(false);
    this.getContentPane().add(this.stat, BorderLayout.SOUTH);

    this.setTitle("Algorithme de Colonie des fourmis");
    this.setDefaultCloseOperation(this.isprog
            ? JFrame.EXIT_ON_CLOSE : JFrame.HIDE_ON_CLOSE);
    this.setLocation(500, 170);
    this.setResizable(false);
    this.pack();
    if (this.isprog) this.setVisible(true);
    this.stat.setText("");
  }  /* run() */
  public Ui(boolean isProg)
  { this.isprog = isProg;
    try { EventQueue.invokeAndWait(this); } catch (Exception e) {} }
  public Ui()
  { this.isprog = false;
    try { EventQueue.invokeAndWait(this); } catch (Exception e) {} }
  public Ui(String title)
  { this(false); this.setTitle(title); }
  public static void main (String[] args)
  {
    Ui v = new Ui(true);
  }
}  /* class ACODemo */
