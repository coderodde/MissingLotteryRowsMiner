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
    
    public static void main(String[] args) {
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
    
    private static void oldMain() {
        
        LotteryConfiguration lotteryConfiguration = 
                new LotteryConfiguration(10, 3);
        
        MissingLotteryRowsGenerator tree = 
                new MissingLotteryRowsGenerator(lotteryConfiguration);
        
        LotteryRow lotteryRow1 = new LotteryRow(lotteryConfiguration);
        LotteryRow lotteryRow2 = new LotteryRow(lotteryConfiguration);
        LotteryRow lotteryRow3 = new LotteryRow(lotteryConfiguration);
        
        lotteryRow1.appendNumber(3);
        lotteryRow1.appendNumber(6);
        lotteryRow1.appendNumber(7);
        
        lotteryRow2.appendNumber(3);
        lotteryRow2.appendNumber(6);
        lotteryRow2.appendNumber(9);
        
        lotteryRow3.appendNumber(2);
        lotteryRow3.appendNumber(5);
        lotteryRow3.appendNumber(6);
        
        tree.addLotteryRow(lotteryRow1);
        tree.addLotteryRow(lotteryRow2);
        tree.addLotteryRow(lotteryRow3);
        
        System.out.println("fd");
        
        tree.computeMissingLotteryRows();
    }
}
