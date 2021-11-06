package rewardCentral;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import tourGuide.webClient.GpsUtilWebClient;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import user.VisitedLocation;
import user.User;
import user.UserReward;

@Service
public class RewardsService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	static private final int attractionProximityRange = 200;
	private final GpsUtilWebClient gpsUtilWebClient;
	private final RewardCentral rewardsCentral;

	public RewardsService(GpsUtilWebClient gpsUtilWebClient, RewardCentral rewardCentral) {
		this.gpsUtilWebClient = gpsUtilWebClient;
		this.rewardsCentral = rewardCentral;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	// Soussou
	public void calculateRewards(User user) {
		final List<Attraction> attractions = GpsUtilWebClient.getAttractions();
		CopyOnWriteArrayList <VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());
		for(VisitedLocation visitedLocation : userLocations) {
			attractions.parallelStream().forEach(attractionStream->
			{
				if(user.getUserRewards().parallelStream().filter(r -> r.attraction.attractionName.equals(attractionStream.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attractionStream)) {
						user.addUserReward(new UserReward(visitedLocation, attractionStream, getRewardPoints(attractionStream, user)));
					}
				}
			});
		}

	}
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
