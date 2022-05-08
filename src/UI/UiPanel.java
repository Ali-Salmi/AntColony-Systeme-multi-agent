package UI;

import algorithm.AntColony;
import algorithm.ACP;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

class Edge implements Comparable<Edge> {

  protected int i, j,c;
  public Edge () {}

  public Edge (int i, int j, int c)
  { this.i = i; this.j = j; this.c = c; }

  public int compareTo (Edge obj)
  {
    if (this.c < obj.c) return -1;
    if (this.c > obj.c) return 1;
    return 0;
  }  /* compareTo() */
}  /* class Edge() */

public class UiPanel extends JPanel {

  private static final long serialVersionUID = 0x00010000L;
  private ACP acp;
  private AntColony antc;
  private double    xoff, yoff;
  private double    scale;
  private int[]     xs, ys;
  private Edge[]    edges;
  private Color[]   cols;
  private Stroke    thick;
  private Stroke    thin;

  public UiPanel()
  {
    this.acp   = null;
    this.antc  = null;
    this.xoff  = this.yoff = 0;
    this.scale = 64.0;
    this.cols  = new Color[256];
    for (int i = 256; --i >= 0; )
      this.cols[255-i] = new Color(i/255.0F, i/255.0F, i/255.0F);
    this.thick = new BasicStroke(7.0F);
    this.thin  = new BasicStroke(2.0F);
  }  /* ACOPanel() */

  public AntColony getAnts () { return this.antc; }

  public void setTSP (ACP tsp)
  {
    this.antc  = null;
    this.edges = null;
    this.acp   = tsp;
    if (this.acp == null)
      this.setPreferredSize(new Dimension(656, 656));
    else {
      this.xoff = tsp.getX();
      this.yoff = tsp.getY();
      this.setScale(64.0);
      int n = this.acp.size();
      this.edges = new Edge[n = (n *(n-1)) >> 1];
      while (--n >= 0) this.edges[n] = new Edge();
    }
    this.revalidate();
    this.repaint();
  }  /* setTSP() */

  public void setScale (double scale)
  {
    int       i, n;
    Dimension d;
    int       w, h;

    this.scale = scale;
    d = new Dimension();
    d.width  = (int)(this.acp.getWidth()  *scale +16.5);
    d.height = (int)(this.acp.getHeight() *scale +16.5);
    this.setPreferredSize(d);
    w = 8; h = d.height -8;
    n = this.acp.size();
    this.xs = new int[n];
    this.ys = new int[n];
    for (i = n; --i >= 0; ) {
      this.xs[i] = (int)(w +scale *(this.acp.getX(i) -this.xoff) +0.5);
      this.ys[i] = (int)(h -scale *(this.acp.getY(i) -this.yoff) +0.5);
    }
  }  /* setScale() */

  public void initAnts (int antcnt, double phero, Random rand)
  {
    if (this.acp == null) return;
    this.antc = new AntColony(this.acp, antcnt, rand);
    this.antc.init(phero);
    this.repaint();
  }  /* initAnts() */

  public void setParams (double exploit, double alpha, double beta,
                         double trail, double elite, double evap)
  {
    if (this.antc == null) return;
    this.antc.setExploit(exploit);
    this.antc.setAlpha(alpha);
    this.antc.setBeta(beta);
    this.antc.setTrail(trail);
    this.antc.setElite(elite);
    this.antc.setEvap(evap);
  }  /* setParams() */


  public void runAnts ()
  {
    if (this.antc == null) return;
    this.antc.runAllAnts();
    this.repaint();
  }  /* runAnts() */

  public void paint (Graphics g)
  {
    int       i, j, k, n;
    Dimension d;
    int       w, h;
    int       x, y;
    double    trl, avg, max;
    int[]     tour;
    Edge      e;

    d = this.getSize();
    w = d.width; h = d.height;
    d = this.getPreferredSize();
    if (d.width  > w) w = d.width;
    if (d.height > h) h = d.height;
    g.setColor(Color.white);
    g.fillRect(0, 0, w, h);
    if (this.acp == null)
      return;
    n = this.acp.size();
    w = 8; h = d.height -8;

    if (this.antc != null) {
      avg = this.antc.getTrailAvg();
      max = this.antc.getTrailMax();
      if (max < 2*avg) max = 2*avg;
      max = 255.0/max;
      for (k = 0, i = n; --i >= 0; ) {
        for (j = i; --j >= 0; ) {
          e   = this.edges[k++];
          trl = this.antc.getTrail(e.i = i, e.j = j);
          e.c = (int)(max*trl);
          if (e.c > 255) e.c = 255;
        }
      }
      Arrays.sort(this.edges, 0, k);
      ((Graphics2D)g).setStroke(this.thick);
      for (i = 0; i < k; i++) {
        e = this.edges[i];
        g.setColor(this.cols[e.c]);
        g.drawLine(this.xs[e.i],this.ys[e.i],this.xs[e.j],this.ys[e.j]);
      }
      ((Graphics2D)g).setStroke(this.thin);
      g.setColor(Color.red);
      tour = this.antc.getBestTour();
      i    = tour[0];
      for (k = n; --k >= 0; ) {
        j = i; i = tour[k];
        g.drawLine(this.xs[i], this.ys[i], this.xs[j], this.ys[j]);
      }
    }
    for (i = n; --i >= 0; ) {
      x = this.xs[i]; y = this.ys[i];
      g.setColor(Color.black);
      g.fillOval(x-4, y-4, 9, 9);
      g.setColor(Color.yellow);
      g.fillOval(x-3, y-3, 7, 7);
    }
  }  /* paint() */


}  /* class ACOPanel */
