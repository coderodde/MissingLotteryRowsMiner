package net.coderodde.datamining.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class implements a tree data structure for infering missing lottery 
 * rows.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.61 (Apr 27, 2020) ~ renamed the class.
 * @version 1.6 (Apr 20, 2020) ~ initial version.
 * @since 1.6 (Apr 20, 2020)
 */
public final class MissingLotteryRowsGenerator {
    
    /**
     * This inner class implements an integer tree node type.
     */
    private static final class IntegerTreeNode {
        
        /**
         * Children map.
         */
        private SortedMap<Integer, IntegerTreeNode> children;
        
        /**
         * Returns the textual representation of this 
         * {@linkplain net.coderodde.datamining.lottery.LotteryRowsIntegerTree.IntegerTreeNode}.
         * 
         * @return the textual representation of this tree node.
         */
        @Override
        public String toString() {
            return "{children: " + children.toString() + "}";
        }
    }
    
    /**
     * The root node of this integer tree.
     */
    private IntegerTreeNode root = new IntegerTreeNode();
    
    /**
     * The lottery configuration.
     */
    private final LotteryConfiguration lotteryConfiguration;
    
    /**
     * Caches the length of the lottery rows.
     */
    private final int length;
    
    /**
     * Implements the main constructor.
     * 
     * @param lotteryConfiguration the lottery configuration object.
     * @param root the root node of the radix tree.
     */
    private MissingLotteryRowsGenerator(
            final LotteryConfiguration lotteryConfiguration,
            final IntegerTreeNode root) {
        
        this.lotteryConfiguration =
                Objects.requireNonNull(
                        lotteryConfiguration, 
                        "lotteryConfiguration == null");
        
        this.root = Objects.requireNonNull(root, "The root node is null.");
        this.length = this.lotteryConfiguration.getLotteryRowLength();
    }
    
    /**
     * Constructs a missing rows generator with given lottery configuration.
     * 
     * @param lotteryConfiguration the lottery configuration.
     */
    public MissingLotteryRowsGenerator(
            final LotteryConfiguration lotteryConfiguration) {
        this(lotteryConfiguration, new IntegerTreeNode());
    }
    
    /**
     * Adds a list of lottery rows to this generator.
     * 
     * @param lotteryRows the lottery rows to add one by one.
     * @return this generator for chaining.
     */
    public MissingLotteryRowsGenerator 
        addLotteryRows(final List<LotteryRow> lotteryRows) {
        
        for (final LotteryRow lotteryRow : lotteryRows) {
            addLotteryRow(lotteryRow);
        }
        
        return this;
    }
    
    /**
     * Adds a single lottery row to this generator.
     * 
     * @param lotteryRow the lottery row to add.
     * @return this generator for chaining.
     */
    public MissingLotteryRowsGenerator 
        addLotteryRow(final LotteryRow lotteryRow) {
            
        Objects.requireNonNull(lotteryRow, "lotteryRow == null");
        checkLotteryRow(lotteryRow);
        IntegerTreeNode node = root;
        
        for (int i = 0, sz = this.length; i < sz; i++) {
            final IntegerTreeNode nextNode;
            final int number = lotteryRow.getNumber(i);
            
            if (node.children == null) {
                node.children = new TreeMap<>();
            }
            
            if (!node.children.containsKey(number)) {
                node.children.put(number, nextNode = new IntegerTreeNode());
                
                if (i < sz - 1) {
                    nextNode.children = new TreeMap<>();
                }
            } else {
                nextNode = node.children.get(number);
            }
            
            node = nextNode;
        }
        
        return this;
    }
    
    /**
     * Computes and returns all the <i>missing</i> lottery rows. A lottery row 
     * is <i>missing</i> if and only if it was not drawn in the population of
     * players.
     * 
     * @return the list of missing lottery rows.
     */
    public List<LotteryRow> computeMissingLotteryRows() {
        List<LotteryRow> lotteryRows = new ArrayList<>();
        final int[] numbers = getInitialNumbers();
        
        do {
            LotteryRow lotteryRow = convertNumbersToLotteryRow(numbers);
            
            if (!treeContains(lotteryRow)) {
                lotteryRows.add(lotteryRow);
            }
            
        } while (increment(numbers));
        
        return lotteryRows;
    }
    
    private boolean treeContains(final LotteryRow lotteryRow) {
        IntegerTreeNode node = root;
        
        for (int i = 0; i < this.length; i++) {
            final int number = lotteryRow.getNumber(i);
            final IntegerTreeNode nextNode = node.children.get(number);
            
            if (nextNode == null) {
                return false;
            }
            
            node = nextNode;
        }
        
        return true;
    }
    
    private boolean increment(final int[] numbers) {
        final int maximumNumber =
                this.lotteryConfiguration.getMaximumNumberValue();
        
        for (int i = this.length - 1, j = 0; i >= 0; i--, j++) {
            if (numbers[i] < maximumNumber - j) {
                numbers[i]++;
                
                for (int k = i + 1; k < this.length; k++) {
                    numbers[k] = numbers[k - 1] + 1;
                }
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Converts a number integer array into a 
     * {@link net.coderodde.datamining.lottery.LotteryRow}.
     * @param numbers the raw number array in ascending order.
     * @return the lottery row with exactly the same numbers as in 
     * {@code numbers}.
     */
    private LotteryRow convertNumbersToLotteryRow(final int[] numbers) {
        LotteryRow lotteryRow = new LotteryRow(this.lotteryConfiguration);
        
        for (final int number : numbers) {
            lotteryRow.appendNumber(number);
        }
        
        return lotteryRow;
    }
    
    private int[] getInitialNumbers() {
        final int[] numbers = new int[this.length];
        
        for (int i = 0, number = 1; i < this.length; i++, number++) {
            numbers[i] = number;
        }
        
        return numbers;
    }
    
    private void checkLotteryRow(final LotteryRow lotteryRow) {
        if (lotteryRow.size() != this.length) {
            throw new IllegalArgumentException(
                    "Wrong length of a row (" + lotteryRow.size() + ", " +
                            "must be exactly " + 
                            this.lotteryConfiguration.getLotteryRowLength() + 
                            ".");
        }
    }
}
