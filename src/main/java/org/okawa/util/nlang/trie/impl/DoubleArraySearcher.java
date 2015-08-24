package org.okawa.util.nlang.trie.impl;

import org.okawa.util.nlang.trie.TrieSearcher;


/**
 * Trieからキーワードを検索
 */
public final class DoubleArraySearcher implements TrieSearcher {

    /** Double-Array実データ */
    private final DoubleArray doubleArray;

    /**
     * DoubleArrayを引数にインスタンス化
     *
     * @param doubleArray セットするDouble-Array
     */
    public DoubleArraySearcher(DoubleArray doubleArray) {
	this.doubleArray = doubleArray;
    }

    /**
     * 格納されているキーワード数
     */
    public int size() {
	return this.doubleArray.keySetSize;
    }

    /**
     * TAIL配列と接尾辞のマッチングを取る
     *
     * @param key 対象とするキー
     * @param node ノード番地
     */
    private boolean matchTail(StringStream key, int node) {
	// node番地は負値となっているものと想定
	final int id = Constants.DABase.ID(node);
	// TAIL開始インデックス
	final int begin = this.doubleArray.begins.get(id);
	// TAIL 終了オフセット
	final int offset = this.doubleArray.lengths.get(id);
	// TAILに登録されている接尾辞
	final String suffix = this.doubleArray.tail.substring(begin, begin + offset);
	// 実際の比較処理
	return key.rest().equals(suffix);
    }

    /**
     * 引数で与えられるキーがTAIL配列の要素を接頭辞として持っている場合にコールバック関数を実行する
     *
     * @param key 対象となるキー
     * @param node 現在処理中のノード番地
     * @param begin 現在処理中のクエリ開始位置
     * @param offset 接頭辞として含んでいた場合のクエリ終端位置
     * @param func コールバック関数
     */
    private void applyIfKeyIncludesSuffix(StringStream key, int node, int begin, int offset, Callback func) {
	// 番地からインデックスへ変換
	final int id = Constants.DABase.ID(node);
	// 接尾辞開始インデックス
	final int suffixBegin = this.doubleArray.begins.get(id);
	// 接尾辞長
	final int suffixOffset = this.doubleArray.lengths.get(id);
	// startsWithメソッドはカレントインデックスから処理される事に注意
	if (key.startsWith(this.doubleArray.tail, suffixBegin, suffixOffset)) {
	    func.apply(begin, offset, id);
	}
    }

    /**
     * キーが登録されているか
     *
     * @param key 対象とするキー
     * @return キーが存在する場合はそのID、それ意外は-1
     */
    @Override
    public int membership(CharSequence key) {
	// 根ノードをセット
	int node = this.doubleArray.base.get(0);
	// StringStreamを利用して検索
	StringStream keyStream = new StringStream(key);
	// 各エッジに対して検証
	for (char code = keyStream.read(); ; code = keyStream.read()) {
	    // 子ノード
	    final int index = node + code;
	    // 格納されている番地オフセット
	    node = this.doubleArray.base.get(index);

	    // 下記条件が接続条件
	    if (this.doubleArray.check.get(index) == code) {
		//if (this.doubleArray.check.get(0) == code) {
		if (node >= 0) { // nodeオフセットが非負の場合は終端ではない
		    continue;
		} else if (keyStream.eos() || this.matchTail(keyStream, node)) { // 接尾辞が登録されている場合
		    return Constants.DABase.ID(node);
		} else { // それ以外の場合はヒットしていない
		    return -1;
		}
	    }
	}
    }

    /**
     * 共通接頭辞検索
     *
     * @param query 問い合わせる文字列
     * @param begin 検索開始位置
     * @param func コールバック関数
     */
    @Override
    public void eachCommonPrefix(CharSequence query, int begin, Callback func) {
	// 根ノードをセット
	int node = this.doubleArray.base.get(0);
	// マッチのとれた終端
	int offset = 0;
	// クエリをストリームへ
	StringStream queryStream = new StringStream(query, begin);

	for (char code = queryStream.read(); ; code = queryStream.read(), offset++) {
	    // 現在のノードに終端ノードがぶらさがっているか
	    final int terminalIndex = node + Constants.DACheck.TERM_CODE;
	    if (this.doubleArray.check.get(terminalIndex) == Constants.DACheck.TERM_CODE) {
		func.apply(begin, offset, Constants.DABase.ID(this.doubleArray.base.get(terminalIndex)));
		if (code == Constants.DACheck.TERM_CODE) {
		    return;
		}
	    }
	    // 番兵ノードであり、TAILに接尾辞が登録されている
	    final int index = node + code;
	    node = this.doubleArray.base.get(index);
	    // 接続条件が充たされる場合
	    if (this.doubleArray.check.get(index) == code) {
		if (node >= 0) { // node番地が非負の場合は終端ではない
		    continue;
		} else { // それ以外は現在のノードに付随する接尾辞を接頭辞として含んでいるか検証
		    final int suffixOffset = this.doubleArray.lengths.get(node);
		    this.applyIfKeyIncludesSuffix(queryStream, node, begin, suffixOffset, func);
		}
	    } else {
		return;
	    }
	}
    }
}
