package org.okawa.util.nlang.trie;

/**
 * Trie検索インターフェース
 */
public interface TrieSearcher {
    /**
     * 共通接頭辞検索でキーが見つかった場合に呼び出されるメソッド
     */
    public static interface Callback {
	/**
	 * 実際に呼び出される処理
	 *
	 * @param begin 入力テキストの検索開始位置
	 * @param offset 一致した部分文字列の終端
	 * @param 一致した文字列のID
	 */
	public void apply(int begin, int offset, int id);
    }

    /**
     * キーが登録されているか
     *
     * @param key 対象とするキー
     * @return キーが存在する場合はそのID、それ意外は-1
     */
    public int membership(CharSequence key);

    /**
     * 共通接頭辞検索
     *
     * @param query 問い合わせる文字列
     * @param begin 検索開始位置
     * @param func コールバック関数
     */
    public void eachCommonPrefix(CharSequence query, int begin, Callback func);
}
