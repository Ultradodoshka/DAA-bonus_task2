import java.util.ArrayList;
import java.util.List;

public class KMP {
    private static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }


    static void searchSingle(String text, String pattern, List<Match> results) {
        if (pattern == null || pattern.isEmpty()) return;
        int n = text.length();
        int m = pattern.length();
        int[] lps = computeLPS(pattern);

        int i = 0;
        int j = 0;
        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == m) {
                results.add(new Match(pattern, i - j));
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
    }

    static List<Match> searchMultiple(String text, List<String> patterns) {
        List<Match> results = new ArrayList<>();
        for (String p : patterns) {
            searchSingle(text, p, results);
        }
        return results;
    }
}