# Java Trie-Tree Based Dictionary Implementation

This little library enables fast, simple and yet universal dictionary implementation
for Java based on trie-tree, e.g., first you must implement interface called "Lexeme",

    package foo.bar

    import org.okawa.util.nlang.dict.Lexeme;

    public class Pair implement Lexeme {
        @Override
        public String getKey() {
            // returns dictionary key here.
        }

        @Override
        public String getValue() {
            // returns dictionary value here.
        }
    }

then you can use dictionary implementation as follows:

    import java.util.*;
    import org.okawa.util.nlang.dict.Dictionary;
    import foo.bar.Pair;

    public public static void main(String[] args) {
        List<Pair> words = new ArrayList<Pair>();
        :
        :
        :
        Dictionary dictionary = new Dictionary(words, true);
    }

That's all. Now you can use trie tree based dictionary with useful callback function APIs.