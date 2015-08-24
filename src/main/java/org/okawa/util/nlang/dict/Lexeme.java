package org.okawa.util.nlang.dict;

import org.okawa.util.nlang.trie.Trie;

/**
 * 辞書語彙クラス
 * 辞書を定義するテーブルのDTOはこれを継承することでTrie辞書を実装する
 */
public interface Lexeme extends Trie.Entry {
    /* 単なるラッパーのため何も実装しない */
}
