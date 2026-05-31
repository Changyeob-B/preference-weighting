# Incorporating Route Choice Modelling Results into a Preference-Based Pedestrian Routing Tool

This repository contains a custom GraphHopper components to implement preference based routing tool.
The main purpose is to incorporate the findings from route choice modelling into an existing routing engine, so that such preferences can be reflected from the route-planning stage and implemented as a practical tool.
These components were implemented based on GraphHopper version 11, and a compatible Maven environment is required for installation and build.

# Usage
Prepare the original network data in OSM format with the required attributes stored as OSM tags.
GraphHopperManaged is a class that creates MyGraphHopper and registers MyImportRegistry when the GraphHopper server starts, enabling the custom import logic to be used.
\graphhopper\web-bundle\src\main\java\com\graphhopper\http
MyGraphHopper creates a custom weighting factory so that GraphHopper can use the preference-based routing weights.
MyImportRegistry registers custom OSM tag attributes  as encoded values.
PreferenceWeightingFactory creates the appropriate PreferenceWeighting according to the selected routing profile.
\graphhopper\core\src\main\java\custom
