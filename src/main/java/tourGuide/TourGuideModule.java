package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tourGuide.webClient.GpsUtilWebClient;
import rewardCentral.RewardCentral;
import rewardCentral.RewardsService;

@Configuration
public class TourGuideModule {
	
	@Bean
	public GpsUtilWebClient getGpsUtil() {
		return new GpsUtilWebClient();
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
