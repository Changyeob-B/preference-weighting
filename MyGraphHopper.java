package custom;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.WeightingFactory;

public class MyGraphHopper extends GraphHopper {

    @Override
    protected WeightingFactory createWeightingFactory() {
        return new PreferenceWeightingFactory(
                getBaseGraph(),
                getEncodingManager(),
                getEncodingManager()
        );
    }
}
