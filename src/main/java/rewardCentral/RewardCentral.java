//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package rewardCentral;

import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class RewardCentral {

    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        try {
            TimeUnit.MILLISECONDS.sleep((long)ThreadLocalRandom.current().nextInt(1, 10));
        } catch (InterruptedException var4) {
        }

        int randomInt = ThreadLocalRandom.current().nextInt(1, 1000);
        return randomInt;
    }
}
