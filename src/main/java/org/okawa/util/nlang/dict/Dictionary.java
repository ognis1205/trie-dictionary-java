package org.okawa.util.nlang.dict;

import java.util.List;
import org.okawa.util.nlang.trie.Trie;
import org.okawa.util.nlang.trie.TrieSearcher;
import org.okawa.util.nlang.trie.impl.DoubleArray;
import org.okawa.util.nlang.trie.impl.DoubleArrayBuilder;
import org.okawa.util.nlang.trie.impl.DoubleArraySearcher;

/**
 * 辞書実装クラス
 * Trie実装(委譲)クラス
 */
public class Dictionary implements Trie {
    /** Trie検索クラスを辞書の索引として使用 */
    private TrieSearcher index;
    /** 各インデックスに紐づくデータ */
    private LexemeArray lexemes;

    /**
     * 語彙素から辞書を生成する
     *
     * @param lexemes 語彙素配列
     * @param sorted trueの場合、語彙素は整列されているものとしてデータを構築
     */
    public Dictionary(List<? extends Lexeme> lexemes, boolean sorted) {
	// 元データとなる語彙素配列準備
	LexemeArrayBuilder lexemeArrayBuilder = new LexemeArrayBuilder(lexemes);
	// Double-Array生成
	DoubleArray doubleArray = DoubleArrayBuilder.build(lexemeArrayBuilder.getTrieEntryList(), sorted, lexemeArrayBuilder);
	// Lexeme-Array生成
	this.lexemes = LexemeArrayBuilder.build(lexemeArrayBuilder);
	// 索引生成
	this.index = new DoubleArraySearcher(doubleArray);
    }

    /**
     * Double-ArrayとLexeme-Arrayから辞書をインスタンス化
     *
     * @param index 索引となるTrie木
     * @param lexemes 翻訳データ
     */
    public Dictionary(DoubleArray index, LexemeArray lexemes) {
	// 索引登録
	this.index = new DoubleArraySearcher(index);
	// 翻訳後データ登録
	this.lexemes = lexemes;
    }

    /**
     * キーが登録されているかの問い合わせ
     *
     * @param key 検索対象となるキー
     * @return キーが存在する場合はそのID、それ意外の場合は-1
     */
    @Override
    public int membership(CharSequence key) {
	return this.index.membership(key);
    }

    /**
     * 共通接頭辞検索
     *
     * 通常は登録されているキーの中で最長のキーを返すメソッドに以下の名称を使用するが、今回は
     * 共通接頭辞検索の名称として使用
     *
     * @param query 問い合わせ対象となるクエリ
     * @param begin 問い合わせ時、クエリ開始位置
     * @param func コールバック関数
     */
    @Override
    public void prefix(CharSequence query, int begin, TrieSearcher.Callback func) {
	this.index.eachCommonPrefix(query, begin, func);
    }

    /**
     * キーワード番号で登録されている訳語を返す
     *
     * @param id キーワード登録番号
     * @return 翻訳後の単語
     */
    public String getTranslation(int id) {
	return this.lexemes.get(id);
    }
}
