package net.coderodde.datamining.lottery;

/**
 * This class specifies the lottery game configuration.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jan 18, 2020)
 * @since 1.6 (Jan 18, 2020)
 */
public class LotteryConfiguration {

    /**
     * The maximum ball integer value.
     */
    private final int maximumNumberValue;

    /**
     * The length of each lottery row.
     */
    private final int lotteryRowLength;

    /**
     * Construct a new lottery configuration.
     * 
     * @param maximumNumberValue the maximum ball integer value.
     * @param lotteryRowLength   the lottery row length.
     */
    public LotteryConfiguration(final int maximumNumberValue,
                                final int lotteryRowLength) {
        checkArgs(maximumNumberValue, lotteryRowLength);
        this.maximumNumberValue = maximumNumberValue;
        this.lotteryRowLength   = lotteryRowLength;
    }

    public int getMaximumNumberValue() {
        return this.maximumNumberValue;
    }

    public int getLotteryRowLength() {
        return this.lotteryRowLength;
    }

    private static void checkArgs(int maximumNumber, int numberCount) {
        if (maximumNumber < 1) {
            throw new IllegalArgumentException(
                    "maximumNumber(" + maximumNumber + ") < 1");
        }

        if (numberCount < 1) {
            throw new IllegalArgumentException(
                    "numberCount(" + numberCount + ") < 1");
        }

        if (numberCount > maximumNumber) {
            throw new IllegalArgumentException(
                    "numberCount(" + numberCount + ") > " + 
                    "maximumNumber(" + maximumNumber + ")");
        }
    }
}
