package org.okawa.util.nlang.trie.impl;

import java.util.ArrayList;
import java.util.List;
import org.okawa.util.nlang.trie.Trie;
import org.okawa.util.nlang.trie.TrieBuilder;

/**
 * Double-Arrayビルダー
 */
public final class DoubleArrayBuilder implements TrieBuilder {
    /** キーワード一覧 */
    private final List<StringStream> keys;
    /** BASE配列 */
    private final DynamicArrayList<Integer> base = new DynamicArrayList<Integer>();
    /** CHECK配列 */
    private final DynamicArrayList<Character> check = new DynamicArrayList<Character>();
    /** TAIL配列 接尾辞開始位置配列 */
    private final ArrayList<Integer> begins = new ArrayList<Integer>();
    /** TAIL配列 接尾辞長 */
    private final ArrayList<Integer> lengths = new ArrayList<Integer>();
    /** TAIL配列 */
    private final StringBuilder tail = new StringBuilder();

    /**
     * キーワード一覧からビルダーをインスタンス化
     *
     * @param keys キーワード一覧
     * @param sorted trueの場合はソートされたキーワードを使用するものとして処理
     */
    private DoubleArrayBuilder(List<? extends Trie.Entry> keys, boolean sorted) {
	this.keys = new ArrayList<StringStream>(keys.size());
	// ソート & ユニーク
	// 本来であればRaddixSort使用したい
	if (!sorted) {
	    java.util.Collections.sort(keys);
	}
	String prev = null;
	String keyString = null;
	for (Trie.Entry key : keys) {
	    keyString = key.getKey();
	    if (!keyString.equals(prev)) {
		this.keys.add(new StringStream(prev = keyString));
	    }
	}
    }

    /**
     * ビルダーからTrieをインスタンス化
     *
     * @param keys キーワード一覧
     * @param sorted trueの場合はソートされたキーワードを使用するものとして処理
     */
    public static DoubleArray build(List<? extends Trie.Entry> keys, boolean sorted, Callback func) {
	DoubleArrayBuilder builder = new DoubleArrayBuilder(keys, sorted);
	// 0 : begin
	// builder.keys.size() : end
	// 0 : rootIndex
	builder.build(new DoubleArrayAllocator(), 0, builder.keys.size(), 0, func);
	return new DoubleArray(builder);
    }

    /**
     * 構築実処理
     *
     * @param allocator 使用する仮想メモリアロケータ
     * @param begin 使用キーワード開始インデックス
     * @param end 使用キーワード終了インデックス
     * @param 根ノードに割り振られた番地
     */
    private void build(DoubleArrayAllocator allocator, int begin, int end, int rootIndex, Callback func) {
	// 残るは接尾辞のみ
	// endとbeginの差が1の場合は共通の接頭辞を持つキーが存在しない、すなわちTAIL配列に格納
	if (end - begin == 1) {
	    this.insertTail(keys.get(begin), rootIndex, func);
	    return;
	}

	// 各接頭文字に対する終了位置
	final List<Integer> ends = new ArrayList<Integer>();
	// 文字コードリスト
	final List<Character> codes = new ArrayList<Character>();
	// 前回処理した文字コード
	char prev = Constants.DACheck.EMPTY_CODE;

	// 根ノードから伸びるエッジ(文字)を収集
	for (int i = begin; i < end; i++) {
	    char curr = keys.get(i).read();
	    if (prev != curr) {
		codes.add(prev = curr);
		ends.add(i);
	    }
	}
	ends.add(end);

	// 根ノードから派生するノードに対して再帰的に構築
	final int xNode = allocator.xCheck(codes);
	for (int i = 0; i< codes.size(); i++) {
	    this.build(allocator, ends.get(i), ends.get(i + 1), this.setNode(codes.get(i), rootIndex, xNode), func);
	}
    }

    /**
     * ノードをセット
     *
     * @param code 文字コード
     * @param parentIndex 親ノードインデックス
     * @param xNode xCheckで付与される番地
     * @return 新たに付与されたノード番地
     */
    private int setNode(char code, int parentIndex, int xNode) {
	// アサインされた文字コードに対応した子ノード
	final int childNode = xNode + code;
	// 親ノードにはxCheckで付与される番地を振る
	this.base.set(parentIndex, xNode, Constants.DABase.INIT_VALUE);
	// 子ノードとの接続情報をセット
	this.check.set(childNode, code, Constants.DACheck.EMPTY_CODE);
	// 子ノードを返り値とする
	return childNode;
    }

    /**
     * TAIL配列に接尾辞を格納
     *
     * @param key 格納する接尾辞を持つキーワード
     * @param nodeIndex 付随するノードインデックス
     * @param func キー登録時のコールバック関数
     */
    private void insertTail(StringStream key, int nodeIndex, Callback func) {
	String suffix = key.rest(-1);
	// 0-startのインデックスなので、以下のようなIDの割り振りはAllocationに相当することに注意
	int id = Constants.DABase.ID(this.begins.size());
	// 処理時のBASE配列のサイズをIDとして使用
	this.base.set(nodeIndex, id, Constants.DABase.INIT_VALUE);
	// TAILオフセット (開始位置)
	this.begins.add(this.tail.length());
	// TAILオフセット (終了位置)
	this.lengths.add(suffix.length());
	// TAIL配列
	this.tail.append(suffix);
	// コールバック関数実行
	func.apply(Constants.DABase.ID(id));
    }

    /** DoubleArray構築時に使用 */
    public int getKeySetSize() {
	return this.keys.size();
    }

    /** DoubleArray構築時に使用 */
    public DynamicArrayList<Integer> getBase() {
	return this.base;
    }

    /** DoubleArray構築時に使用 */
    public DynamicArrayList<Character> getCheck() {
	return this.check;
    }

    /** DoubleArray構築時に使用 */
    public List<Integer> getBegins() {
	return this.begins;
    }

    /** DoubleArray構築時に使用 */
    public List<Integer> getLengths() {
	return this.lengths;
    }

    /** DoubleArray構築時に使用 */
    public StringBuilder getTail() {
	return this.tail;
    }
}
