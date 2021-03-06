package net.coderodde.datamining.lottery;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class implements a single lottery row.
 * 
 * @author Rodion "rodde" Efremove
 * @version 1.61 (Apr 27, 2020) ~ removed manual sorting.
 * @version 1.6 (Apr 18, 2020) ~ initial version.
 * @since 1.6 (Apr 18, 2020)
 */
public class LotteryRow {

    /**
     * The configuration object.
     */
    private final LotteryConfiguration lotteryConfiguration;

    /**
     * The actual lottery numbers.
     */
    private final int[] lotteryNumbers;

    /**
     * Stores the index of the internal storage array at which the next lottery
     * number will be inserted.
     */
    private int size = 0;

    /**
     * Constructs an empty lottery row with given configuration.
     * 
     * @param lotteryConfiguration the lottery row configuration.
     */
    public LotteryRow(LotteryConfiguration lotteryConfiguration) {
        this.lotteryConfiguration = 
                Objects.requireNonNull(lotteryConfiguration);

        this.lotteryNumbers =
                new int[lotteryConfiguration.getLotteryRowLength()];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;

        for (final int number : this.lotteryNumbers) {
            if (isFirst) {
                isFirst = false;
                stringBuilder.append(number);
            } else {
                stringBuilder.append(",").append(number);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Appends a number to the tail of this lottery row.
     * 
     * @param number the number to append.
     */
    public void appendNumber(int number) {
        checkNumber(number);
        checkHasSpaceForNewNumber();
        lotteryNumbers[size++] = number;
        Arrays.sort(lotteryNumbers, 0, size);
    }

    /**
     * Returns the <code>index</code>th number.
     * 
     * @param index the index of the desired number.
     * @return the <code>index</code>th number.
     */
    public int getNumber(int index) {
        checkIndex(index);
        return lotteryNumbers[index];
    }
    
    /**
     * Returns the configuration object of this row.
     * 
     * @return the configuration object.
     */
    public LotteryConfiguration getLotteryConfiguration() {
        return lotteryConfiguration;
    }

    /**
     * Checks that there is more space for lottery numbers in this row.
     */
    private void checkHasSpaceForNewNumber() {
        if (size == lotteryNumbers.length) {
            throw new IllegalStateException(
                    "The lottery row cannot accommodate more numbers.");
        }
    }

    /**
     * Checks that the input number is within the lottery number range.
     * 
     * @param number the number to check.
     */
    private void checkNumber(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("number(" + number + ") < 1");
        }

        if (number > this.lotteryConfiguration.getMaximumNumberValue()) {
            throw new IllegalArgumentException(
                "number (" + number + ") > " +
                "this.lotteryConfiguration.getMaximumNumberValue()[" +
                this.lotteryConfiguration.getMaximumNumberValue() + "]");
        }
    }

    /**
     * Checks that the index is withing the range <code>[0, n)</code>.
     * 
     * @param index the index to check.
     */
    private void checkIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index(" + index + ") < 0");
        }

        if (index >= this.size) {
            throw new IllegalArgumentException(
                    "index(" + index + ") >= this.index(" + this.size + ")");
        }
    }
}
