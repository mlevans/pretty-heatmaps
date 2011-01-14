package densityarraycreation;

/**
 *
 * @author Michael Lawrence Evans :: michael@codeforamerica.org
 */
public class Main {

    public static void main(String[] args) {
        LatLon2XY latlon2xyinstance = new LatLon2XY();
        latlon2xyinstance.createXYdata();

        CoarseGridVals coarsegridvalsinstance = new CoarseGridVals(latlon2xyinstance.ndata, latlon2xyinstance.normXmax, latlon2xyinstance.normYmax, latlon2xyinstance.x, latlon2xyinstance.y);
        double[] gridvalsarray = coarsegridvalsinstance.createCoarseGrid();
        
        HeatMapVals heatmapvalsinstance = new HeatMapVals();
        heatmapvalsinstance.generate(gridvalsarray);
    }

}
