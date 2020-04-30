package net.coderodde.datamining.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a data mining algorithm for selecting all possible 
 * lottery rows that do not appear in the given data set. This version differs
 * from {@link net.coderodde.datamining.lottery.MissingLotteryRowsGenerator} in
 * that respect that TreeMaps are changed to lighter unbalanced binary search
 * trees.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Apr 28, 2020) ~ renamed the class.
 * @since 1.6 (Apr 20, 2020)
 */
public final class MissingLotteryRowsGenerator {
    
    private static final class RadixTreeNode {
        RadixTreeNode[] children;
    }
    
    private final RadixTreeNode root;
    private final LotteryConfiguration lotteryConfiguration;

    /**
     * Implements the main constructor.
     * 
     * @param lotteryConfiguration the lottery configuration object.
     * @param root the root node of the radix tree.
     */
    private MissingLotteryRowsGenerator(
            LotteryConfiguration lotteryConfiguration,
            RadixTreeNode root) {

        this.lotteryConfiguration =
                Objects.requireNonNull(
                        lotteryConfiguration, 
                        "lotteryConfiguration == null");

        this.root = Objects.requireNonNull(root, "The root node is null.");
    }

    /**
     * Constructs a missing rows generator with given lottery configuration.
     * 
     * @param lotteryConfiguration the lottery configuration.
     */
    public MissingLotteryRowsGenerator(
            LotteryConfiguration lotteryConfiguration) {
        this(lotteryConfiguration, new RadixTreeNode());
    }

    /**
     * Adds a list of lottery rows to this generator.
     * 
     * @param lotteryRows the lottery rows to add one by one.
     * @return this generator for chaining.
     */
    public MissingLotteryRowsGenerator
        addLotteryRows(List<LotteryRow> lotteryRows) {

        for (LotteryRow lotteryRow : lotteryRows) {
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
        addLotteryRow(LotteryRow lotteryRow) {

        Objects.requireNonNull(lotteryRow, "lotteryRow == null");
        checkLotteryRow(lotteryRow);
        RadixTreeNode node = root;
        int maximumValue = lotteryConfiguration.getMaximumNumberValue();

        for (int i = 0, sz = lotteryConfiguration.getLotteryRowLength(); 
                i < sz; 
                i++) {
            RadixTreeNode nextNode;
            int number = lotteryRow.getNumber(i);
            
            if (node.children == null) {
                node.children = new RadixTreeNode[maximumValue];
            }
            
            if (node.children[number - 1] == null) {
                RadixTreeNode tmp = new RadixTreeNode();
                nextNode = tmp;
                node.children[number - 1] = tmp;
                
                if (i < sz - 1) {
                    nextNode.children = new RadixTreeNode[maximumValue];
                }
            } else {
                nextNode = node.children[number - 1];
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
        int[] numbers = getInitialNumbers();

        do {
            LotteryRow lotteryRow = convertNumbersToLotteryRow(numbers);

            if (!treeContains(lotteryRow)) {
                lotteryRows.add(lotteryRow);
            }

        } while (increment(numbers));

        return lotteryRows;
    }

    private boolean treeContains(LotteryRow lotteryRow) {
        RadixTreeNode node = root;

        for (int i = 0, sz = lotteryConfiguration.getLotteryRowLength(); 
                i < sz;
                i++) {
            
            int number = lotteryRow.getNumber(i);
            RadixTreeNode nextNode = node.children[number - 1];

            if (nextNode == null) {
                return false;
            }

            node = nextNode;
        }

        return true;
    }

    private boolean increment(final int[] numbers) {
        int maximumNumber = lotteryConfiguration.getMaximumNumberValue();
        int lotteryRowLength = lotteryConfiguration.getLotteryRowLength();

        for (int i = lotteryRowLength - 1, j = 0; 
                i >= 0; 
                i--, j++) {
            
            if (numbers[i] < maximumNumber - j) {
                numbers[i]++;

                for (int k = i + 1; k < lotteryRowLength; k++) {
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
    private LotteryRow convertNumbersToLotteryRow(int[] numbers) {
        LotteryRow lotteryRow = new LotteryRow(this.lotteryConfiguration);

        for (int number : numbers) {
            lotteryRow.appendNumber(number);
        }

        return lotteryRow;
    }

    private int[] getInitialNumbers() {
        int lotteryRowLength = lotteryConfiguration.getLotteryRowLength();
        int[] numbers = new int[lotteryRowLength];

        for (int i = 0, number = 1; i < lotteryRowLength; i++, number++) {
            numbers[i] = number;
        }

        return numbers;
    }

    private void checkLotteryRow(final LotteryRow lotteryRow) {
        if (lotteryRow.getLotteryConfiguration().getLotteryRowLength()
                != lotteryConfiguration.getLotteryRowLength()) {
            throw new IllegalArgumentException(
                    "Wrong length of a row (" +
                            lotteryRow.getLotteryConfiguration()
                                       .getLotteryRowLength() + 
                            ", must be exactly " + 
                            this.lotteryConfiguration.getLotteryRowLength() + 
                            ".");
        }
    }
}
