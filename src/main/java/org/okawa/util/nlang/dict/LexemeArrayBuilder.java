package org.okawa.util.nlang.dict;

import java.util.ArrayList;
import java.util.List;
import org.okawa.util.nlang.trie.Trie;
import org.okawa.util.nlang.trie.TrieBuilder;

/**
 * Lexicon-Arrayビルダー
 */
final class LexemeArrayBuilder implements TrieBuilder.Callback {
    /** 元データとなる語彙素配列 */
    private final List<? extends Lexeme> lexemes;
    /** DATA配列 開始位置配列 */
    private final ArrayList<Integer> begins = new ArrayList<Integer>();
    /** DATA配列 データ長 */
    private final ArrayList<Integer> lengths = new ArrayList<Integer>();
    /** TAIL配列 */
    private final StringBuffer data = new StringBuffer();


    /**
     * 語彙素配列からインスタンス化[
     *
     * @param lexemes 辞書を作る際に必要となる語彙素
     */
    public LexemeArrayBuilder(List<? extends Lexeme> lexemes) {
	this.lexemes = lexemes;
    }

    /**
     * ビルダーからTrieをインスタンス化
     *
     * @param keys キーワード一覧
     * @param sorted trueの場合はソートされたキーワードを使用するものとして処理
     */
    public static LexemeArray build(LexemeArrayBuilder builder) {
	return new LexemeArray(builder);
    }

    /**
     * Trieビルダーがキーワードの終端に達する度に呼び出される処理
     *
     * @param 付与されるキーのID
     */
    @Override
    public void apply(int id) {
	// 処理対象となる語彙素の取得
	Lexeme lexeme = this.lexemes.get(id);
	if (lexeme != null) {
	    String value = lexeme.getValue();
	    // データ開始位置を更新
	    //this.begins.add(id);
	    this.begins.add(data.length());
	    // データ終了位置を更新
	    this.lengths.add(value.length());
	    // 実データ登録
	    this.data.append(value);
	}
    }

    /** Trie木構築時にTrie木のEntryとして語彙素の配列を返す */
    public List<? extends Trie.Entry> getTrieEntryList() {
	return this.lexemes;
    }

    /** LexiconArray構築時に使用 */
    public List<Integer> getBegins() {
	return this.begins;
    }

    /** LexiconArray構築時に使用 */
    public List<Integer> getLengths() {
	return this.lengths;
    }

    /** LexiconArray構築時に使用 */
    public String getData() {
	return data.toString();
    }
}
