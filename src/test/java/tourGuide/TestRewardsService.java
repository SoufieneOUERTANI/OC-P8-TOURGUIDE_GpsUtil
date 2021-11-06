package tourGuide;


import java.util.Date;
import java.util.List;
import java.util.UUID;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tourGuide.webClient.GpsUtilWebClient;
import gpsUtil.location.Attraction;
import user.VisitedLocation;
import rewardCentral.RewardCentral;
import helper.InternalTestHelper;
import rewardCentral.RewardsService;
import user.User;
import user.UserReward;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardCentral());

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilWebClient, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilWebClient.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		Assertions.assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardCentral());
		Attraction attraction = gpsUtilWebClient.getAttractions().get(0);
		Assertions.assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	//@Ignore // Needs fixed - can throw ConcurrentModificationException
	//@Test
	public void nearAllAttractions() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilWebClient, rewardsService);
		
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		Assertions.assertEquals(gpsUtilWebClient.getAttractions().size(), userRewards.size());
	}
	
}
