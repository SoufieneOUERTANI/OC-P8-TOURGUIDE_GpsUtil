package tourGuide;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import gpsUtil.location.NearbyAttraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tourGuide.webClient.GpsUtilWebClient;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import rewardCentral.RewardCentral;
import user.VisitedLocation;
import rewardCentral.RewardsService;
import helper.InternalTestHelper;
import tracker.Tracker;
import user.User;
import user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService implements ITourGuideService {
	private static final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtilWebClient gpsUtilWebClient;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;
	
	public TourGuideService(GpsUtilWebClient gpsUtilWebClient, RewardsService rewardsService) {
		this.gpsUtilWebClient = gpsUtilWebClient;
		this.rewardsService = rewardsService;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}


	@Override
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	@Override
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}

	@Override
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	@Override
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	@Override
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	@Override
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	@Override
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtilWebClient.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<Attraction> getAttractions() {
		List<Attraction> attractions = gpsUtilWebClient.getAttractions();
		return attractions;
	}

	@Override
	public List<NearbyAttraction> getNearByAttractions(User user) {
		VisitedLocation visitedLocation = getUserLocation(user);
		List<Attraction> nearbyAttractions = GpsUtilWebClient.getAttractions();

		nearbyAttractions.sort((attraction1, attraction2) -> {
			if((rewardsService.getDistance(attraction1, visitedLocation.location) -
					rewardsService.getDistance(attraction2, visitedLocation.location)) == 0)
				return 0;
			if((rewardsService.getDistance(attraction1, visitedLocation.location) -
					rewardsService.getDistance(attraction2, visitedLocation.location)) > 0)
				return 1;
			return -1;
		});

		return nearbyAttractions.subList(0,5).parallelStream().map(
				nearbyAttraction ->
						new NearbyAttraction(
								nearbyAttraction.attractionName,
								new Location(nearbyAttraction.latitude,nearbyAttraction.longitude),
								visitedLocation.location,
								rewardsService.getDistance(nearbyAttraction, visitedLocation.location),
								new RewardCentral().getAttractionRewardPoints(nearbyAttraction.attractionId,user.getUserId())
						)
		).collect(Collectors.toList());
	}


	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}
