import java.util.Random;

public class PMXCrossover {
    static int[][] pmxCrossover(int[] parent1, int[] parent2) {
        int[] replacement1 = new int[parent1.length+1];
        int[] replacement2 = new int[parent1.length+1];
        int i, n1, m1, n2, m2;
        int swap;

        int[] offSpring1 = new int[parent1.length];
        int [] offSpring2 = new int[parent1.length];

        int[][] offSprings = new int[2][];

        Random rand = new Random();

        int cuttingPoint1 = rand.nextInt(parent1.length);
        int cuttingPoint2 = rand.nextInt(parent1.length);

        while (cuttingPoint1 == cuttingPoint2) {
            cuttingPoint2 = rand.nextInt(parent1.length);
        }

        if (cuttingPoint1 > cuttingPoint2) {
            swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        }

        for (i=0; i < parent1.length+1; i++) {
            replacement1[i] = -1;
            replacement2[i] = -1;
        }

        for (i=cuttingPoint1; i <= cuttingPoint2; i++) {
            offSpring1[i] = parent2[i];
            offSpring2[i] = parent1[i];
            replacement1[parent2[i]] = parent1[i];
            replacement2[parent1[i]] = parent2[i];
        }

        // fill in remaining slots with replacements
        for (i = 0; i < parent1.length; i++) {
            if ((i < cuttingPoint1) || (i > cuttingPoint2)) {
                n1 = parent1[i];
                m1 = replacement1[n1];
                n2 = parent2[i];
                m2 = replacement2[n2];
                while (m1 != -1) {
                    n1 = m1;

                    m1 = replacement1[m1];

                }
                while (m2 != -1) {
                    n2 = m2;

                    m2 = replacement2[m2];

                }
                offSpring1[i] = n1;
                offSpring2[i] = n2;
            }
        }

        offSprings[0] = offSpring1;
        offSprings[1] = offSpring2;

        return offSprings;
    }
}
