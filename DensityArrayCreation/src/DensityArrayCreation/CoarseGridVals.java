package densityarraycreation;

public class CoarseGridVals {

    int ndata;
    int gridCols;
    int gridRows;
    double minX;
    double maxX;
    double minY;
    double maxY;
    double incX;
    double incY;
    double radius;

    double[] X;
    double[] Y;

    double[] gridVals = new double[67 * 67];

    public CoarseGridVals(int ndata, double maxX, double maxY,
						  double[] X, double[] Y) {
        this.ndata = ndata;
		this.minX = 0.0;
		this.maxX = maxX;
		this.minY = 0.0;
		this.maxY = maxY;
		this.X = X;
		this.Y = Y;
		this.gridCols = 65;
		this.gridRows = 65;
    }

    public double[] createCoarseGrid() {
		int count = 67 * 67;

		for (int i = 0; i < count; i++) {
			gridVals[i] = 0.0;
		}
		incX = (maxX - minX) / ((double) (gridCols - 1));
		incY = incX;
		radius = 2*incX;

        calculateGrid();
		return gridVals;

    }

    private double distance(double x1, double y1, double x2, double y2){
        double d;

        d = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
        return  Math.sqrt(d);
    }

    public void calculateGrid() {
        int k;
        int index;
        double x, y;
        double d;
        double maxVal = 0.0;
		int countX, countY;

		countY = gridRows;
        y = minY;
		while (countY > 0) {
			index = ((gridRows - countY + 1) * 67) + 1;
			countX = gridCols;
            x = minX;
			while (countX > 0) {
                gridVals[index] = 0.0;
                for (k = 0; k < ndata; k++){
                    d = distance(x, y, X[k], Y[k]);
                    if (d <= radius){
                        gridVals[index] = gridVals[index] + 1.0;
                        if (gridVals[index] > maxVal){
                            maxVal = gridVals[index];
                        }
                    }
                }
				countX = countX - 1;
                x = x + incX;
				index = index + 1;
            }
			countY = countY - 1;
            y = y + incY;
        }
        System.out.println("gridCols: " + gridCols);
        System.out.println("gridRows: " + gridRows);
        System.out.println("maxVal: " + maxVal);
        for(k = 0; k < 67 * 67; k++){
            gridVals[k] = (gridVals[k] / maxVal) * 1023.0;
        }
		double invLog2 = 1.0 / (Math.log(2.0));
		double minVal;
        gridVals[0] = (double) (invLog2 * Math.log1p(gridVals[0]));
		minVal = gridVals[0];
		maxVal = gridVals[0];
        for(k = 1; k < 67 * 67; k++){
            gridVals[k] = (double) (invLog2 * Math.log1p(gridVals[k]));
       		if (gridVals[k] > maxVal){
            	maxVal = gridVals[k];
            }
       		if (gridVals[k] < minVal){
            	minVal = gridVals[k];
            }
        }
        System.out.println("minVal: " + minVal);
        System.out.println("maxVal: " + maxVal);

    }

}
