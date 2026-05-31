# Incorporating Route Choice Modelling Results into a Preference-Based Pedestrian Routing Tool

This repository contains a custom GraphHopper components to implement preference based routing tool.  
The main purpose is to incorporate the findings from route choice modelling into an existing routing engine, so that such preferences can be reflected from the route-planning stage and implemented as a practical tool.  
In this implementation, a Value-of-Distance-based cost conversion method is used, and a logistic transformation is applied to adjust the edge cost.  
These components were implemented based on GraphHopper version 11, and a compatible Maven environment is required for installation and build.

# Usage
Prepare the original network data in OSM format with the required attributes stored as OSM tags.  
As basic guide from the study, the attributes are assumed inclduing, width, poi_count, slope_forward_percent, forest_overlap_ratio, length, streetlights, trafficlights and speeding.

**GraphHopperManaged** is a class that creates MyGraphHopper and registers MyImportRegistry when the GraphHopper server starts, enabling the custom import logic to be used.  
*\graphhopper\web-bundle\src\main\java\com\graphhopper\http*

OSMCustomAttributesParser reads custom OSM way tags, and stores them as GraphHopper encoded values for both travel directions.  
*\graphhopper\core\src\main\java\com\graphhopper\routing\util\parsers*

**MyGraphHopper** creates a custom weighting factory so that GraphHopper can use the preference-based routing weights.  
**MyImportRegistry** registers custom OSM tag attributes  as encoded values.  
**PreferenceWeightingFactory** creates the appropriate PreferenceWeighting according to the selected routing profile.  
**PreferenceProfile** defines the preference profiles used to select them.  
**PreferenceCoefficients** stores the coefficient values used by each preference profile to calculate the routing cost.  
**PreferenceWeighting** calculates the final cost based on lengths and profile coefficients.  
*\graphhopper\core\src\main\java\custom*

After all files are placed in the directories, build the project using Maven.  
*mvn clean package*  
Then, place the OSM network data, the GraphHopper configuration file(yml), and the profile folder in the path required by the GraphHopper project structure.  
After these files are prepared, run the GraphHopper server.
