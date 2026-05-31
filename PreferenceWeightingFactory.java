package custom;

import com.graphhopper.config.Profile;
import com.graphhopper.routing.DefaultWeightingFactory;
import com.graphhopper.routing.ev.EncodedValueLookup;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.util.PMap;

public class PreferenceWeightingFactory extends DefaultWeightingFactory {

    private final EncodedValueLookup lookup;

    public PreferenceWeightingFactory(BaseGraph baseGraph,
                                      EncodingManager encodingManager,
                                      EncodedValueLookup lookup) {
        super(baseGraph, encodingManager);
        this.lookup = lookup;
    }

    @Override
    public Weighting createWeighting(Profile profile, PMap hints, boolean disableTurnCosts) {
        String profileName = profile.getName();

        if ("foot_none".equals(profileName)) {
            return new PreferenceWeighting(lookup, PreferenceProfile.NONE);
        } else if ("foot_meta".equals(profileName)) {
            return new PreferenceWeighting(lookup, PreferenceProfile.META);
        } else if ("foot_path".equals(profileName)) {
            return new PreferenceWeighting(lookup, PreferenceProfile.PATH);
        } else if ("foot_recursive".equals(profileName)) {
            return new PreferenceWeighting(lookup, PreferenceProfile.RECURSIVE);
        }

        return super.createWeighting(profile, hints, disableTurnCosts);
    }
}