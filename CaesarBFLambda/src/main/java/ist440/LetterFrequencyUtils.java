package ist440;

/**
 * Utility class to perform frequency operations on text
 */
public class LetterFrequencyUtils {

    public static int ALPHABET_COUNT = 26;

    /* The index of coincidence bounds text should have if it is in the english language */
    public static double ENG_IC_LOWER_BOUND = 0.061;
    public static double ENG_IC_UPPER_BOUND = 0.071;
    public static double ENG_IC = 0.0686;

    /*
     * Frequencies of characters in the english alphabet
     */
    public static double A = 8.167;
    public static double B = 1.492;
    public static double C = 2.782;
    public static double D = 4.253;
    public static double E = 12.702;
    public static double F = 2.228;
    public static double G = 2.015;
    public static double H = 6.094;
    public static double I = 6.966;
    public static double J = 0.153;
    public static double K = 0.772;
    public static double L = 4.025;
    public static double M = 2.406;
    public static double N = 6.749;
    public static double O = 7.507;
    public static double P = 1.929;
    public static double Q = 0.095;
    public static double R = 5.987;
    public static double S = 6.327;
    public static double T = 9.056;
    public static double U = 2.758;
    public static double V = 0.978;
    public static double W = 2.360;
    public static double X = 0.150;
    public static double Y = 1.974;
    public static double Z = 0.074;

    public static double[] FREQUENCIES = {
            A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    };

    public static int[] countCharacters(String message) {
        message = message.replaceAll("[^a-zA-Z]", "").toUpperCase();
        int[] counts = new int[ALPHABET_COUNT];
        for (char c : message.toCharArray())
            counts[c - 'A']++;
        return counts;
    }

    /**
     * calculates the expected number of times each character in the alphabet should occur based on the values in {@link LetterFrequencyUtils#FREQUENCIES}
     *
     * @param length the length of the ciphertext
     * @return the expected number of total of occurrences for each character
     */
    public static double[] expectedCharacterCounts(int length) {
        double[] expected = new double[ALPHABET_COUNT];
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            // length * P(i)
            expected[i] = (length * (FREQUENCIES[i] / 100));
        }
        return expected;
    }

    /**
     * Determines whether the passed index of coincidence is within a tolerance value of the Index of Coincidence of the
     * english language
     * <p>
     * The passed index of coincidence must be greater than {@link LetterFrequencyUtils#ENG_IC_LOWER_BOUND} and lower than
     * {@link LetterFrequencyUtils#ENG_IC_UPPER_BOUND} to be considered "close" to the english language.
     */
    public static boolean closeToEng(double indexOfCoincidence) {
        return ENG_IC_LOWER_BOUND < indexOfCoincidence && indexOfCoincidence < ENG_IC_UPPER_BOUND;
    }

    /**
     * Index of Coincidence is {@code (Σ Fi * (Fi - 1))/N * (N - 1)}, where {@code Fi} is the frequency of the {@code ith} character
     * of the alphabet in the ciphertext and {@code N} is the length of the input.
     *
     * @param text the text to analyse
     * @return index of coincidence
     */
    public static double indexOfCoincidence(String text) {

        // ignore anything other than the alphabet
        text = text.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (text.length() < 1) return -1;
        // get the probability of each character occurring in the text
        int[] counts = LetterFrequencyUtils.countCharacters(text);

        double sum = 0.0;
        // sum of Fi * (Fi - 1)
        for (int i = 0; i < LetterFrequencyUtils.ALPHABET_COUNT; i++) {
            double fi = counts[i];
            if (fi > 0.0)
                sum += fi * (fi - 1.0);
        }
        // divide by N * (N - 1)
        return sum / (text.length() * (text.length() - 1));
    }

    /**
     * Compares frequencies of characters in the {@code ciphertext} with those expected in the english language using
     * a chi-squared test.
     * <p>
     * chiSq = {@code Sum of ((Ci - Ei)^2)/Ei}
     * <p>
     * where {@code Ci} is the observed number of occurrences of the ith letter of the alphabet in the ciphertext,
     * and {@code Ei} is the expected number of occurrences of the ith letter of the alphabet in an english string
     * with the length of {@code ciphertext}, based on {@link LetterFrequencyUtils#expectedCharacterCounts(int)}
     *
     * @param ciphertext the ciphertext to analyse
     * @return a fitness value indicating whether the ciphertext is english
     */
    public static double chiSquareAgainstEnglish(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        // an array containing the total number of times each character occurred in the ciphertext
        int[] characterCounts = LetterFrequencyUtils.countCharacters(ciphertext);
        // an array containing the expected number of times each character should occur in text the length of the
        // ciphertext
        double[] expectedCharacterCounts = LetterFrequencyUtils.expectedCharacterCounts(ciphertext.length());

        double fitness = 0.0;
        for (int i = 0; i < LetterFrequencyUtils.ALPHABET_COUNT; i++) {
            //((Ci - Ei)^2)/Ei
            fitness += Math.pow(characterCounts[i] - expectedCharacterCounts[i], 2) / expectedCharacterCounts[i];
        }
        return fitness;
    }
}