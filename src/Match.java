public class Match {
    final String pattern;
    final int index;

    Match(String pattern, int index) {
        this.pattern = pattern;
        this.index = index;
    }

    @Override
    public String toString() {
        return "(" + pattern + " @ " + index + ")";
    }
}