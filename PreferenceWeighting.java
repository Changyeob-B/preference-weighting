package custom;

import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EncodedValueLookup;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.util.EdgeIteratorState;

public class PreferenceWeighting implements Weighting {

    private static final double WALKING_SPEED_MPS = 1.4;

    private final DecimalEncodedValue lengthEnc;
    private final DecimalEncodedValue slopeEnc;
    private final DecimalEncodedValue widthEnc;
    private final DecimalEncodedValue poiCountEnc;
    private final DecimalEncodedValue forestOverlapEnc;

    private final BooleanEncodedValue streetlightsEnc;
    private final BooleanEncodedValue trafficlightsEnc;
    private final BooleanEncodedValue speedingEnc;

    private final PreferenceProfile profile;
    private final PreferenceCoefficients coeffs;

    public PreferenceWeighting(EncodedValueLookup lookup, PreferenceProfile profile) {
        this.lengthEnc = lookup.getDecimalEncodedValue("length");
        this.slopeEnc = lookup.getDecimalEncodedValue("slope_forward_percent");
        this.widthEnc = lookup.getDecimalEncodedValue("width");
        this.poiCountEnc = lookup.getDecimalEncodedValue("poi_count");
        this.forestOverlapEnc = lookup.getDecimalEncodedValue("forest_overlap_ratio");

        this.streetlightsEnc = lookup.getBooleanEncodedValue("streetlights");
        this.trafficlightsEnc = lookup.getBooleanEncodedValue("trafficlights");
        this.speedingEnc = lookup.getBooleanEncodedValue("speeding");

        this.profile = profile;
        this.coeffs = PreferenceCoefficients.of(profile);
    }

    @Override
    public double calcMinWeightPerDistance() {
        return 0.5;
    }

    @Override
    public double calcEdgeWeight(EdgeIteratorState edgeState, boolean reverse) {
        double length = getLength(edgeState, reverse);

        if (length <= 0 || !Double.isFinite(length)) {
            return Double.POSITIVE_INFINITY;
        }

        double gradient = reverse ? edgeState.getReverse(slopeEnc) : edgeState.get(slopeEnc);
        double width = reverse ? edgeState.getReverse(widthEnc) : edgeState.get(widthEnc);
        double poiCount = reverse ? edgeState.getReverse(poiCountEnc) : edgeState.get(poiCountEnc);
        double tree = reverse ? edgeState.getReverse(forestOverlapEnc) : edgeState.get(forestOverlapEnc);

        double streetlights = (reverse ? edgeState.getReverse(streetlightsEnc) : edgeState.get(streetlightsEnc)) ? 1.0 : 0.0;
        double trafficlights = (reverse ? edgeState.getReverse(trafficlightsEnc) : edgeState.get(trafficlightsEnc)) ? 1.0 : 0.0;
        double speeding = (reverse ? edgeState.getReverse(speedingEnc) : edgeState.get(speedingEnc)) ? 1.0 : 0.0;
        double vodComponent = coeffs.gradient * gradient
                + coeffs.width * width
                + coeffs.tree * tree
                + coeffs.poi * poiCount
                + coeffs.speeding * speeding
                + coeffs.streetlights * streetlights
                + coeffs.trafficlights * trafficlights;
        double vodDistanceRatio = vodComponent / length;
        double costMultiplier = 0.5 + sigmoid(vodDistanceRatio);

        return length * costMultiplier;
    }

    @Override
    public long calcEdgeMillis(EdgeIteratorState edgeState, boolean reverse) {
        double length = getLength(edgeState, reverse);

        if (length <= 0 || !Double.isFinite(length)) {
            return 0L;
        }

        return Math.round((length / WALKING_SPEED_MPS) * 1000.0);
    }

    @Override
    public double calcTurnWeight(int inEdge, int viaNode, int outEdge) {
        return 0.0;
    }

    @Override
    public long calcTurnMillis(int inEdge, int viaNode, int outEdge) {
        return 0L;
    }

    @Override
    public boolean hasTurnCosts() {
        return false;
    }

    @Override
    public String getName() {
        return "preference_" + profile.getKey();
    }

    private double getLength(EdgeIteratorState edgeState, boolean reverse) {
        double length = reverse ? edgeState.getReverse(lengthEnc) : edgeState.get(lengthEnc);

        if (length <= 0 || !Double.isFinite(length)) {
            length = edgeState.getDistance();
        }

        return length;
    }

    private static double sigmoid(double x) {

        if (x >= 0) {
            double exp = Math.exp(-x);
            return 1.0 / (1.0 + exp);
        } else {
            double exp = Math.exp(x);
            return exp / (1.0 + exp);
        }
    }
}