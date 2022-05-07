package algorithm;

import java.util.Random;

public class TSP {

  private static final int BLKSIZE = 16;
  protected int        size;
  protected double[]   xs, ys;
  protected double[][] dists;
  protected boolean    sym;
  protected boolean    euclid;
  protected int[]      tour;
  private   double     bbx, bby;
  private   double     bbw, bbh;
  private   boolean    valid;

  public TSP (int size)
  {
    this.size   = 0;
    this.xs     = new double[size];
    this.ys     = new double[size];
    this.dists  = null;
    this.euclid = true;
    this.sym    = true;
    this.tour   = null;
    this.valid  = false;
  }  /* TSP() */

  public TSP (int size, Random rand)
  {
    this(size);
    this.randomize(rand);
    this.makeDists(true);
  }  /* TSP() */

  private void resize (int size)
  {
    int      k;
    double[] v;

    k = this.size;
    if (size < 0) size = k +((k < BLKSIZE) ? BLKSIZE : (k >> 1));
    if (size < k) k = this.size = size;
    System.arraycopy(this.xs, 0, v = new double[size], 0, k);
    this.xs = v;
    System.arraycopy(this.ys, 0, v = new double[size], 0, k);
    this.ys = v;
  }  /* resize() */

  public int add (double x, double y)
  {
    if (this.size >= this.xs.length)
      this.resize(-1);
    this.valid = false;
    this.xs[this.size] = x;
    this.ys[this.size] = y;
    return this.size++;
  }  /* add() */

  public void randomize (Random rand)
  {
    for (int i = this.size = this.xs.length; --i >= 0; ) {
      this.xs[i] = rand.nextDouble();
      this.ys[i] = rand.nextDouble();
    }
    this.valid = false;
  }  /* randomize() */

  public int    size   ()      { return this.size; }
  public double getX   (int i) { return this.xs[i]; }
  public double getY   (int i) { return this.ys[i]; }

  public void transform (double scale, double xoff, double yoff)
  {                             /* --- transform vertex coordinates */
    for (int i = this.size; --i >= 0; ) {
      this.xs[i] = this.xs[i] *scale +xoff;
      this.ys[i] = this.ys[i] *scale +yoff;
    }                           /* traverse and transform vertices */
    this.valid = false;         /* bounding box is no longer valid */
  }  /* transform() */

  private void bbox ()
  {                             /* --- compute bounding box */
    int    i;                   /* loop variable */
    double x, y;                /* coordinates of a vertex */
    double xmax, ymax;          /* maximal x- and y-coordinates */

    this.bbx = Double.MAX_VALUE; xmax = -Double.MAX_VALUE;
    this.bby = Double.MAX_VALUE; ymax = -Double.MAX_VALUE;
    for (i = this.xs.length; --i >= 0; ) {
      x = this.xs[i];          /* traverse the vertices */
      y = this.ys[i];          /* of the problem */
      if (x < this.bbx) this.bbx = x;
      if (x > xmax)     xmax     = x;
      if (y < this.bby) this.bby = y;
      if (y > ymax)     ymax     = y;
    }                           /* find minimum and maximum coords. */
    this.bbw = xmax -this.bbx;  /* compute the width and height */
    this.bbh = ymax -this.bby;  /* of the bounding box */
    this.valid = true;          /* the bounding box is now valid */
  }  /* bbox() */

  public double getX ()
  { if (!this.valid) this.bbox(); return this.bbx; }

  public double getY ()
  { if (!this.valid) this.bbox(); return this.bby; }

  public double getWidth ()
  { if (!this.valid) this.bbox(); return this.bbw; }

  public double getHeight ()
  { if (!this.valid) this.bbox(); return this.bbh; }

  public void makeDists (boolean calc)
  {                             /* --- calculate distance matrix */
    int      i, k;              /* loop variables */
    double   dx, dy;            /* coordinate-wise distances */
    double[] v;                 /* buffer for reallocation */

    if (this.size < this.xs.length)
      this.resize(this.size);   /* shrink the coord. vectors if poss. */
    this.dists = new double[this.size][this.size];
    if (!calc) return;          /* create the distance matrix */
    for (i = this.size; --i >= 0; ) {
      this.dists[i][i] = 0;     /* set diagonal elements to zero */
      for (k = i; --k >= 0; ) { /* traverse the off-diagonal elements */
        dx = this.xs[i] -this.xs[k];
        dy = this.ys[i] -this.ys[k];
        this.dists[i][k] = this.dists[k][i] = Math.sqrt(dx*dx +dy*dy);
      }                         /* compute pairwise vertex distances */
    }                           /* (Euclidian distances, symmetric) */
    this.euclid = this.sym = true;
  }  /* makeDists() */

  public boolean isSymmetric ()
  { return this.sym; }

  public double  getDist     (int i, int j)
  { return this.dists[i][j]; }
}  /* class TSP */
