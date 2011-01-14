package densityarraytopng;

/**
 *
 * @author Michael Lawrence Evans :: michael@longliveman.com
 */

import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Main {
    BufferedReader reader = null;

    int nrows;
    int ncols;
    int ndata;

    double[] vals;

	double min;
	double max;

	BufferedImage image;
	int[] LUT = new int[257];

    public static void main(String[] args) {
        Main a2p = new Main();
        a2p.run(args);
    }

    public void run(String[] args) {
        File inDir = new File("input/Zoom13/");
        File inFile = inDir.listFiles()[0];
        File outFile = new File("output/Zoom13/newdata4.png");

        nrows = 0;
        ncols = 0;
        ndata = 0;

        try {
            reader = new BufferedReader(new FileReader(inFile));

        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        String s = null;

        try {
        	s = getString();
			nrows = readInt(s);
        	s = getString();
			ncols = readInt(s);
        } catch (IOException e){
            System.out.println("Error");
        }

		vals = new double[nrows * ncols];

        s = getString();

        while (s != null) {

            try {
            	vals[ndata] = readDouble(s);
            } catch (IOException e){
                System.out.println("Error");
            }

            ndata += 1;

            s = getString();
        }

		if (ndata != (nrows * ncols)) {
            System.out.println("ndata incorrect!");
            System.exit(1);
		}

		image = new BufferedImage(nrows, ncols,
                                          BufferedImage.TYPE_INT_ARGB);

        convert();

        try {
            ImageIO.write(image, "png", outFile);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

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
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
       if (line == null) {
           s2 = null;
           return null;
       }

        strs = line.split(" ");
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

    private int readInt(String s) throws IOException{
        int i = Integer.parseInt(s);

        return i;
    }

    private void convert() {
		int count = 0;

		computeMinMax();
		if (min != 0.0) {
            System.out.println("min != 0.0");
		}

		computeLUT();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				double v = vals[count];

                                if (v <= 0.25){
                                    vals[count] = 0.0;
                                } else if (v <= 1){
                                    vals[count] = 1.0;
                                } else if (v <= 2){
                                    vals[count] = 2.0;
                                } else if (v <= 3){
                                    vals[count] = 3.0;
                                } else if (v <= 4){
                                    vals[count] = 4.0;
                                }else if (v <= 5){
                                    vals[count] = 5.0;
                                } else if (v <= 6){
                                    vals[count] = 6.0;
                                } else if (v <= 7){
                                    vals[count] = 7.0;
                                } else if (v <= 8){
                                    vals[count]= 8.0;
                                } else if (v <= 9){
                                    vals[count] = 9.0;
                                } else {
                                    vals[count] = 10.0;
                                }
                                int k = (int) vals[count]; //used to be v

				image.setRGB(j, i, LUT[k]);
				count = count + 1;
			}
		}

    }

    private void computeMinMax() {

        min = vals[0];
        max = vals[0];
        for (int i = 1; i < ndata; i++) {
            if (vals[i] <= min) {
                min = vals[i];
            }
            if (vals[i] >= max) {
                max = vals[i];
            }
        }
		System.out.println("min: " + min);
		System.out.println("max: " + max);
	}

    private void computeLUT() {
		int c = 0xFFFFFFFF;
		for (int i = 0; i < 10; i++) {
			LUT[i] = c;
			c = c - 0x00010101;
		}
                /*
		LUT[0] = 0xFFFFFFFF;
                LUT[1] = 0xFFe6e6e6; //converted to hexadecimal
                LUT[2] = 0xFFCCCCCC;
                LUT[3] = 0xFFb3b3b3;
                LUT[4] = 0xFF999999;
                LUT[5] = 0xFF808080;
                LUT[6] = 0xFF666666;
                LUT[7] = 0xFF4d4d4d;
                LUT[8] = 0xFF333333;
                LUT[9] = 0xFF1a1a1a;
                LUT[10] = 0xFF000000;
                */

                LUT[0] = 0xFFe7f3fe;

                LUT[1] = 0xFFdbeaf2; //converted to hexadecimal

                LUT[2] = 0xFFb9d7e7;

                LUT[3] = 0xFF9ac5dd;

                LUT[4] = 0xFF79b2d2;

                LUT[5] = 0xFF59a0c7;

                LUT[6] = 0xFF3d8bb7;

                LUT[7] = 0xFF327397;

                LUT[8] = 0xFF275875;

                LUT[9] = 0xFF1c4055;

                LUT[10] = 0xFF122a37;

                /*
                1. fcfdfe --> 16580094 --> sat 9

2. dbeaf2 --> 14412530

3. b9d7e7 --> 12179431

4. 9ac5dd --> 10143197

5. 79b2d2 --> 7975634

6. 59a0c7 --> 5873863

7. 3d8bb7 --> 4033463

8. 327397 --> 3306391

9. 275875 --> 2578549

10. 1c4055 --> 1851477

11. 122a37 --> 1190455
                */
	}

}
