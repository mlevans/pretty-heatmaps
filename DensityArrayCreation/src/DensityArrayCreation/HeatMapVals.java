package densityarraycreation;

import java.io.*;

public class HeatMapVals {
    BufferedWriter writer = null;
	int outputLineValCount;

	int zoomLevel;
        int gridCols;
        int gridRows;
	double fineSamplesPerCoarseInterval;
	int[] coarseSampleInterval;
	double[] interpCoord;
	Bicubic[][] bcinterp;
    double[] coarseGrid;


    public HeatMapVals() {
		zoomLevel = 13; // 1024x1024
		gridCols = 1024;
		gridRows = 1024;
		initHelperArrays();
    }

	private void initHelperArrays() {
		coarseSampleInterval = new int[gridRows];
		interpCoord = new double[gridRows];
		fineSamplesPerCoarseInterval = (double) (gridRows / 64);

		double sampleCoord = 0.5;

		for (int i = 0; i < gridRows; i++) {
			coarseSampleInterval[i] = (int)(sampleCoord /fineSamplesPerCoarseInterval);
			interpCoord[i] = (sampleCoord - ((double)(coarseSampleInterval[i]) * fineSamplesPerCoarseInterval)) / fineSamplesPerCoarseInterval;
			sampleCoord = sampleCoord + 1.0;
		}

		bcinterp = new Bicubic[64][64];
	}

	private Bicubic getBCI(int r, int c) {
		// check if non-null and return
		Bicubic bci = bcinterp[r][c];
		if (bci != null) {
			return bci;
		}

		// free interpolator refs for older rows that will no longer be needed
		if ((r > 0) && (c == 0) && (bcinterp[r-1][0] != null)) {
			for (int i = 0; i < 64; i++) {
				bcinterp[r-1][i] = null;
			}
		}

		// create new bci
		double[][] coeffs = new double[4][4];
		for (int i = 0; i < 4; i++) {
			int k = ((r + i) * 67) + c;
			for (int j = 0; j < 4; j++) {
				coeffs[i][j] = coarseGrid[k + j];
			}
		}
		bci = new Bicubic(coeffs);
		return bci;
	}

    public void generate(double[] coarseGrid) {
		this.coarseGrid = coarseGrid;

		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				bcinterp[i][j] = null;
			}
		}


        File outFile = new File("output/data.txt");

        try {
			String s;
            writer = new BufferedWriter(new FileWriter(outFile));
			s = Integer.toString(gridRows);
			writer.write(s, 0, s.length());
            writer.write(" ", 0, 1);
			s = Integer.toString(gridCols);
			writer.write(s, 0, s.length());
           	writer.newLine();
			outputLineValCount = 0;
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

		int r, c;
		double x, y, val;
		Bicubic bci;
        for (int i = 0; i < gridRows; i++) {
			r = coarseSampleInterval[i];
			y = interpCoord[i];
			for (int j = 0; j < gridCols; j++) {
				c = coarseSampleInterval[j];
				x = interpCoord[j];
				bci = getBCI(r, c);
				val = bci.eval(y, x);
				// clamp to input range
				if (val < 0.0) {
					val = 0.0;
				} else if (val > 10.0) {
					val = 10.0;
				}
				outputVal(val);
			}
		}

        try {
			if (outputLineValCount != 0) {
				writer.newLine();
			}
            writer.close();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

	private void outputVal(double val) {
        try {
			String s = Double.toString(val);
            writer.write(s, 0, s.length());
			if (outputLineValCount < 4) {
            	writer.write(" ", 0, 1);
				outputLineValCount += 1;
			} else {
				writer.newLine();
				outputLineValCount = 0;
			}
        } catch (IOException e) {
            System.out.println("Error");
        }
	}


}
