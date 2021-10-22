package user;

import gpsUtil.location.Attraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserReward {

	public final VisitedLocation visitedLocation;
	public final Attraction attraction;
	private int rewardPoints;

}
