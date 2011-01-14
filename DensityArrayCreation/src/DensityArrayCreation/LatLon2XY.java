package densityarraycreation;

import java.io.*;

public class LatLon2XY {
   BufferedReader reader = null;

   public int ndata;

   public double[] x = new double[17500];
   public double[] y = new double[17500];
   double[] lat = new double[17500];
   double[] lon = new double[17500];

       /*
       // Zoom Level 13 -- 1024 x 1024
       // top left X,Y for SF area map is 1308,3165 at this Zoom Level
       // all lat/lon data to be converted is assumed to lie within a
       // 4x4 grid of 256x256 tiles from top left X,Y (i.e. 1024x1024)
       int zoom = 13;
       double xTileLeft = 1308.0;
       double yTileTop = 3165.0;
       double normXmin = 0.0;
       double normXmax = 1024.0;
       double normYmin = 0.0;
       double normYmax = 1024.0;
       */

       // Zoom Level 14 -- 2048 x 2048
       // top left X,Y for SF area map is 1308*2,3165*2 at this Zoom Level
       // all lat/lon data to be converted is assumed to lie within an
       // 8x8 grid of 256x256 tiles from top left X,Y (i.e. 2048x2048)
       int zoom = 14;
       double xTileLeft = 1308.0 * 2.0;
       double yTileTop = 3165.0 * 2.0;
       public double normXmin = 0.0;
       public double normXmax = 1024.0 * 2.0;
       public double normYmin = 0.0;
       public double normYmax = 1024.0 * 2.0;

   public LatLon2XY() {
   }

   public void createXYdata() {
       File inDir = new File("input");
       File inFile = inDir.listFiles()[0];

       ndata = 0;

       try {
           reader = new BufferedReader(new FileReader(inFile));

       } catch (IOException e) {
           System.err.println(e);
           System.exit(1);
       }

       String s = null;

       s = getString();

       while (s != null) {

           try {
                               // System.out.println(s);
               lat[ndata] = readDouble(s);
                               s = getString();
                               // System.out.println(s);
                               lon[ndata] = readDouble(s);
                               s = getString(); // ignore 3rd field
                               // System.out.println(s);
           } catch (IOException e){
               System.out.println("Error");
           }

           ndata += 1;

           s = getString();
       }

       convert();

   }

   String line = null;
   String[] strs = null;
   int strindx = -1;
   String s0 = null;
   String s1 = null;
   String s2 = null;

   public String getString1() {

       String s = null;
       s0 = s1;
       s1 = s2;

       if (strindx >= 0){
           s = strs[strindx];
           strindx = strindx + 1;

           if (strindx >= strs.length){
               strindx = -1;
           }
           s2 = s;
           return s;
       }

       if (line == null){
           try{
               line = reader.readLine();
           } catch(IOException e){
               System.err.println(e);
           }
       }
      if (line == null){
          s2 = null;
          return null;
      }

       strs = line.split(",");
       line = null;

       s = strs[0];

       if (strs.length == 1){
           strindx = -1;
       } else {
           strindx = 1;
       }
       s2 = s;
       return s;
   }

   public String getString() {
               String s = getString1();
               while (s != null && (s.length() == 0)) {
                       s = getString1();
               }
               return s;
       }

   private double readDouble(String s) throws IOException{
       double d = Double.parseDouble(s);

       return d;
   }

   private void convert() {
               double n = 1.0;
               for (int i = 0; i < zoom; i++) {
                       n = n * 2.0;
               }
               for (int i = 0; i < ndata; i++) {
                       double latrad, tmp;
                       x[i] = ((lon[i] + 180.0) / 360.0) * n; // 0 <= x <= n
                       latrad = Math.toRadians((double) lat[i]);
                       tmp =  Math.log(Math.tan(latrad) + (1.0 / Math.cos(latrad)));
                                  // -PI < tmp < PI for latitude range of interest
                       tmp = tmp / Math.PI; // -1 < tmp < 1, increasing south to north
                       tmp = (1.0 - tmp) / 2.0; // 0 < tmp < 1, increasing north to south
                       y[i] = tmp * n; // 0 <= y <= n
                       x[i] = (x[i] - xTileLeft) * 256.0; // 0 <= x < normXmax
                       y[i] = (y[i] - yTileTop) * 256.0; // 0 <= y < normYmax
               }
               computeMinMax();

   }

   private void computeMinMax() {

       double minX = x[0];
       double maxX = x[0];
       double minY = y[0];
       double maxY = y[0];
       for (int i = 1; i < ndata; i++) {
           if (x[i] <= minX) {
               minX = x[i];
           }
           if (x[i] >= maxX) {
               maxX = x[i];
           }
           if (y[i] <= minY) {
               minY = y[i];
           }
           if (y[i] >= maxY) {
               maxY = y[i];
           }
       }
               System.out.println("minX: " + minX);
               System.out.println("maxX: " + maxX);
               System.out.println("minY: " + minY);
               System.out.println("maxY: " + maxY);
       }

}

