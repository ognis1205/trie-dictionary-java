package org.okawa.util.nlang.trie;

/**
 * Trie構築インターフェース
 * 本実装ではTrie木の実装としてDouble-Arrayを採用する。
 */
public interface TrieBuilder {
    /**
     * キー登録時に呼び出されるコールバック関数
     * 辞書構築時のキーに対するバリューの登録などに使用される
     */
    public static interface Callback {
	/**
	 * 実際に呼び出される処理
	 *
	 * @param 付与されるキーのID
	 */
	public void apply(int id);
    }
}
