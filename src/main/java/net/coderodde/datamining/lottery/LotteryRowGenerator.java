package net.coderodde.datamining.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * This class implements a facility for creating random lottery rows.
 * 
 * @author Rodion "rodde" Efremov 
 * @version 1.6 (Apr 18, 2020) 
 * @since 1.6 (Apr 18, 2020)
 */
public final class LotteryRowGenerator {
    
    /**
     * The lottery configuration object.
     */
    private final LotteryConfiguration lotteryConfiguration;
    
    /**
     * The random number generator.
     */
    private final Random random;
    
    /**
     * The storage array for.
     */
    private final int[] numbers;
    
    /**
     * Constructs a {@code LotteryRowGenerator} with a given configuration.
     * 
     * @param lotteryConfiguration the lottery configuration object.
     */
    public LotteryRowGenerator(LotteryConfiguration lotteryConfiguration) {
        this(lotteryConfiguration, new Random());
    }
    
    /**
     * Constructs a {@code LotteryRowGenerator} with a given configuration and
     * a seed value.
     * 
     * @param lotteryConfiguration the lottery configuration object.
     * @param seed the seed value.
     */
    public LotteryRowGenerator(LotteryConfiguration lotteryConfiguration,
                               long seed) {
        this(lotteryConfiguration, new Random(seed));
    }
    
    /**
     * Constructs a {@code LotteryRowGenerator} with a given configuration and
     * a random number generator.
     * 
     * @param lotteryConfiguration the lottery configuration object.
     * @param random the random number generator.
     */
    public LotteryRowGenerator(LotteryConfiguration lotteryConfiguration,
                               Random random) {
        this.random = Objects.requireNonNull(random, 
                                             "The input Random is null.");
        this.lotteryConfiguration =
                Objects.requireNonNull(
                        lotteryConfiguration,
                        "The input LotteryConfiguration is null.");
        
        this.numbers = new int[lotteryConfiguration.getLotteryRowLength()];
    }
    
    /**
     * Generates and returns a list of random lottery rows.
     * 
     * @param numberOfLotteryRows the requested number of lottery rows.
     * @return a list of random rows.
     */
    public List<LotteryRow> 
        generateLotteryRows(int numberOfLotteryRows) {
        List<LotteryRow> rows = new ArrayList<>(numberOfLotteryRows);
        
        for (int i = 0; i < numberOfLotteryRows; i++) {
            rows.add(generateRow());
        }
        
        return rows;
    }
        
    private LotteryRow generateRow() {
        LotteryRow lotteryRow = new LotteryRow(this.lotteryConfiguration);
        shuffleInternalNumbers();
        loadLotteryRow(lotteryRow);
        return lotteryRow;
    }
    
    private void shuffleInternalNumbers() {
        for (int i = 0, n = lotteryConfiguration.getLotteryRowLength();
                i < n; 
                i++) {
            int i2 = getRandomIndex();
            swap(i, i2);
        }
    }
    
    public void loadLotteryRow(LotteryRow lotteryRow) {
        for (int i = 0, n = this.lotteryConfiguration.getLotteryRowLength();
                i < n;
                i++) {
            lotteryRow.appendNumber(this.numbers[i]);
        }
    }
    
    private int getRandomIndex() {
        return this.random.nextInt(
                this.lotteryConfiguration.getMaximumNumberValue());
    }
    
    private void swap(int index1, int index2) {
        int tmp = this.numbers[index1];
        this.numbers[index1] = this.numbers[index2];
        this.numbers[index2] = tmp;
    }
}
