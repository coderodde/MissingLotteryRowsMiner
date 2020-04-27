package net.coderodde.datamining.lottery;

import java.util.Arrays;
import java.util.List;

/**
 * This class demonstrates the functionality of the missing lottery row data
 * mining algorithm.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Apr 25, 2020)
 * @since 1.6 (Apr 25, 2020)
 */
public class Demo {
    
    private static final int LOTTERY_ROW_LENGTH = 7;
    private static final int LOTTERY_MAXIMUM_NUMBER = 40;
    private static final int LOTTERY_ROWS = 15_000_000;
    
    public static void main(String[] args) {
        smallDemo();
        
        final long seed = System.currentTimeMillis();
        final LotteryConfiguration lotteryConfiguration = 
                new LotteryConfiguration(LOTTERY_MAXIMUM_NUMBER,
                                         LOTTERY_ROW_LENGTH);
        
        System.out.println("Seed = " + seed);
        
        final List<LotteryRow> data = benchmarkAndObtainData(seed);
        benchmark(lotteryConfiguration, data);
    }
    
    private static List<LotteryRow> benchmarkAndObtainData(final long seed) {
        final LotteryConfiguration lotteryConfiguration = 
                new LotteryConfiguration(LOTTERY_MAXIMUM_NUMBER,    
                                         LOTTERY_ROW_LENGTH);
        
        // Warmup run:
        new LotteryRowGenerator(lotteryConfiguration, seed)
                .generateLotteryRows(LOTTERY_ROWS);
        
        long startTime = System.nanoTime();
        
        // Data generation: 
        final List<LotteryRow> data =
                new LotteryRowGenerator(lotteryConfiguration)
                        .generateLotteryRows(LOTTERY_ROWS);
        
        long endTime = System.nanoTime();
        
        System.out.println(
                "Data generated in " + 
                        ((endTime - startTime) / 1_000_000L) + 
                        " milliseconds.");
        
        return data;
    }
    
    private static void benchmark(
            final LotteryConfiguration lotteryConfiguration,
            final List<LotteryRow> data) {
        
        final long startTime = System.nanoTime();
        
        final List<LotteryRow> missingLotteryRows = 
            new MissingLotteryRowsGenerator(lotteryConfiguration)
                .addLotteryRows(data)
                .computeMissingLotteryRows();
        
        final long endTime = System.nanoTime();
        
        System.out.println(
                "Duration: " 
                        + ((endTime - startTime) / 1_000_000L) 
                        + " milliseconds.");
        
        System.out.println(
                "Missing lottery rows: " + missingLotteryRows.size());
    }
    
    private static void smallDemo() {
        LotteryConfiguration lotteryConfiguration = 
                new LotteryConfiguration(5, 3);
        
        LotteryRow lotteryRow1 = new LotteryRow(lotteryConfiguration); // 1, 2, 4
        LotteryRow lotteryRow2 = new LotteryRow(lotteryConfiguration); // 2, 4, 5
        LotteryRow lotteryRow3 = new LotteryRow(lotteryConfiguration); // 1, 3, 5
        LotteryRow lotteryRow4 = new LotteryRow(lotteryConfiguration); // 3, 4, 5
        
        lotteryRow1.appendNumber(1);
        lotteryRow1.appendNumber(4);
        lotteryRow1.appendNumber(2);
        
        lotteryRow2.appendNumber(4);
        lotteryRow2.appendNumber(5);
        lotteryRow2.appendNumber(2);
        
        lotteryRow3.appendNumber(1);
        lotteryRow3.appendNumber(3);
        lotteryRow3.appendNumber(5);
        
        lotteryRow4.appendNumber(3);
        lotteryRow4.appendNumber(4);
        lotteryRow4.appendNumber(5);
        
        List<LotteryRow> drawnLotteryRows = Arrays.asList(lotteryRow1,
                                                          lotteryRow2,
                                                          lotteryRow3,
                                                          lotteryRow4);
        
        List<LotteryRow> missingLotteryRows = 
                new MissingLotteryRowsGenerator(lotteryConfiguration)
                        .addLotteryRows(drawnLotteryRows)//
                        .computeMissingLotteryRows();
        
        System.out.println(missingLotteryRows);
    }
}
