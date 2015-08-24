package org.okawa.util.nlang.trie.impl;

import java.util.List;


/**
 * Trie木実装 Double-Array
 */
public final class DoubleArray {
    /** 格納されているキーワード数 */
    public int keySetSize;
    /** BASE配列 */
    public final DynamicArrayList<Integer> base;
    /** CHECK配列 */
    public final DynamicArrayList<Character> check;
    /** TAIL配列 各接尾辞開始位置 */
    public final List<Integer> begins;
    /** TAIL配列 各接尾辞長 */
    public final List<Integer> lengths;
    /** TAIL配列 */
    public StringBuilder tail;

    /**
     * ビルダーからインスタンス化
     */
    public DoubleArray(DoubleArrayBuilder builder) {
	// 登録されているキーの濃度をセット
	this.keySetSize = builder.getKeySetSize();
	// BASE配列のセット
	this.base = builder.getBase();
	// CHECK配列のセット
	this.check = builder.getCheck();
	// TAIL配列開始位置格納配列をセット
	this.begins = builder.getBegins();
	// TAIL配列データ長格納配列をセット
	this.lengths = builder.getLengths();
	// TAIL配列実データをセット
	this.tail = builder.getTail();
    }
}
