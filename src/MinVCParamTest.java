import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Derrick Lockwood
 * @created 4/18/17.
 */
@RunWith(Parameterized.class)
public class MinVCParamTest {

    private final int[][] M;
    private final ArrayList<Integer> expected;

    public MinVCParamTest(int[][] M, ArrayList<Integer> expected) {
        this.M = M;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection params() {
        return Arrays.asList(new Object[]{
                new int[][]{
                        {3, 1, 2},
                        {8, 4, 5},
                        {6, 7, 9}
                },
                new ArrayList<>(Arrays.asList(0, 1, 1, 1, 2, 0))
        }, new Object[]{
                new int[][]{
                        {1, 8, 3},
                        {4, 2 ,2},
                        {1, 5, 3}
                },
                new ArrayList<>(Arrays.asList(0, 0, 1, 1, 2, 0))
        }, new Object[]{
                new int[][]{
                        {7, 5, 11, 4},
                        {1, 2, 4, 8},
                        {10, 9, 3, 5},
                        {7, 5, 4, 8}
                },
                new ArrayList<>(Arrays.asList(0,1,1,1,2,2,3,2))
        }, new Object[] {
                new int[][]{
                        {1, 2, 4},
                        {7, 12, 3},
                        {0, 1, 4},
                        {3, 4, 3}
                },
                new ArrayList<>(Arrays.asList(0,1,1,2,2,1,3,2))
        });
    }

    @Test
    public void testMinVC() {
        Assert.assertEquals(expected, DynamicProgramming.minCostVC(M));
    }

}