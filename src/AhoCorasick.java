import java.util.*;

public class AhoCorasick {
    static class Node {
        Map<Character, Node> next = new HashMap<>();
        Node fail;
        List<String> out = new ArrayList<>();
    }

    Node root = new Node();

    void addPattern(String p) {
        if (p == null || p.isEmpty()) return;
        Node v = root;
        for (char c : p.toCharArray()) {
            v = v.next.computeIfAbsent(c, k -> new Node());
        }
        v.out.add(p);
    }

    void build() {
        Queue<Node> q = new ArrayDeque<>();
        root.fail = root;
        for (Node child : root.next.values()) {
            child.fail = root;
            q.add(child);
        }

        while (!q.isEmpty()) {
            Node v = q.poll();
            for (var e : v.next.entrySet()) {
                char c = e.getKey();
                Node u = e.getValue();
                Node f = v.fail;

                while (f != root && !f.next.containsKey(c)) {
                    f = f.fail;
                }
                if (f.next.containsKey(c)) {
                    u.fail = f.next.get(c);
                } else {
                    u.fail = root;
                }
                u.out.addAll(u.fail.out);
                q.add(u);
            }
        }
    }

    List<Match> search(String text) {
        List<Match> found = new ArrayList<>();
        Node v = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (v != root && !v.next.containsKey(c)) {
                v = v.fail;
            }
            v = v.next.getOrDefault(c, root);
            for (String pattern : v.out) {
                found.add(new Match(pattern, i - pattern.length() + 1));
            }
        }
        return found;
    }
}