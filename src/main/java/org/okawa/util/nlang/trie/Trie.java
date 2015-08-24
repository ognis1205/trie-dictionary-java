package org.okawa.util.nlang.trie;

import org.okawa.util.nlang.trie.impl.DoubleArraySearcher;

/**
 * Trieインターフェース
 * 本実装ではTrie木の実装としてDouble-Arrayを採用する。
 * また本案件ではDictionaryクラスでこのインターフェースを実装し、内部のDouble-Array関連クラス
 * に委譲する形で実装する。
 */
public interface Trie {
    /**
     * Trie木に格納されているデータを保持するためのインターフェース
     */
    public static interface Entry extends Comparable<Entry> {
	/** キーの取得 */
	public String getKey();
	/** バリューの取得 */
	public String getValue();
    }

    /**
     * キーが登録されているかの問い合わせ
     *
     * @param key 検索対象となるキー
     * @return キーが存在する場合はそのID、それ意外の場合は-1
     */
    public int membership(CharSequence key);

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
    public void prefix(CharSequence query, int begin, DoubleArraySearcher.Callback func);
}
