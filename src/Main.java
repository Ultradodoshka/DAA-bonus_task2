import java.util.*;

public class Main {
    private static String generateRandomText(int length, Random rnd) {
        String alphabet = "abcd";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private static List<String> generatePatterns(int count, int minLen, int maxLen, Random rnd) {
        String alphabet = "abcd";
        Set<String> uniquePatterns = new HashSet<>();
        while (uniquePatterns.size() < count) {
            int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
            }
            uniquePatterns.add(sb.toString());
        }
        return new ArrayList<>(uniquePatterns);
    }

    public static void main(String[] args) {
        int[] patternCounts = {10, 100, 1000};
        int[] textLengths = {1000, 10000, 100000};
        int runs = 5;
        Random rnd = new Random(42);

        AhoCorasick warmupAC = new AhoCorasick();
        warmupAC.addPattern("abc");
        warmupAC.build();
        warmupAC.search("abcdefabc");
        KMP.searchMultiple("abcdefabc", List.of("abc"));

        System.out.printf("%-10s | %-12s | %-18s | %-18s | %-10s%n",
                "Patterns", "Text Length", "Aho-Corasick (ms)", "KMP Multi (ms)", "Matches");
        System.out.println("-".repeat(78));

        for (int pCount : patternCounts) {
            List<String> patterns = generatePatterns(pCount, 4, 10, rnd);

            for (int tLen : textLengths) {
                String text = generateRandomText(tLen, rnd);

                long totalAcTime = 0;
                long totalKmpTime = 0;
                int matchesCount = 0;

                for (int r = 0; r < runs; r++) {
                    long startAc = System.nanoTime();
                    AhoCorasick ac = new AhoCorasick();
                    for (String p : patterns) {
                        ac.addPattern(p);
                    }
                    ac.build();
                    List<Match> acMatches = ac.search(text);
                    long endAc = System.nanoTime();
                    totalAcTime += (endAc - startAc);

                    long startKmp = System.nanoTime();
                    List<Match> kmpMatches = KMP.searchMultiple(text, patterns);
                    long endKmp = System.nanoTime();
                    totalKmpTime += (endKmp - startKmp);

                    if (acMatches.size() != kmpMatches.size()) {
                        throw new IllegalStateException("Mismatch in results!");
                    }
                    matchesCount = acMatches.size();
                }

                double avgAcMs = (totalAcTime / (double) runs) / 1_000_000.0;
                double avgKmpMs = (totalKmpTime / (double) runs) / 1_000_000.0;

                System.out.printf("%-10d | %-12d | %-18.4f | %-18.4f | %-10d%n",
                        pCount, tLen, avgAcMs, avgKmpMs, matchesCount);
            }
        }
    }
}
