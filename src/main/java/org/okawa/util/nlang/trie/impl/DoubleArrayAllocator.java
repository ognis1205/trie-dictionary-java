package org.okawa.util.nlang.trie.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Double-Array構築のためのアロケータ。
 */
final class DoubleArrayAllocator {
    /**
     * ノード番号管理用
     */
    private static class LinkedNode {
	// 弟ノード
	public int prev;
	// 兄ノード
	public int next;
	// コンストラクタ
	public LinkedNode(int prev, int next) {
	    this.prev = prev;
	    this.next = next;
	}
    }

    /** 仮想メモリの伸張率 */
    private static final int ALLOC_RATIO = 2;
    /**  番兵ノード番号 */
    private static final int SENTINEL = 0;
    /** 仮想メモリ管理 番地管理 */
    private ArrayList<LinkedNode> linkedNodes = new ArrayList<LinkedNode>();
    /** 仮想メモリ管理 使用フラグ */
    private BitSet bitSet = new BitSet();

    /**
     * 使用する番地一覧の拡張
     *
     * @param hint 確保する領域を明示する場合のヒント
     */
    private void resize(int hint) {
	final int newSize = Math.max(hint, this.linkedNodes.size() * DoubleArrayAllocator.ALLOC_RATIO);
	if (this.linkedNodes.size() == 0) {
	    this.linkedNodes.add(new DoubleArrayAllocator.LinkedNode(-1, 1));
	}
	this.linkedNodes.get(this.linkedNodes.size() - 1).next = this.linkedNodes.size();
	for (int i = this.linkedNodes.size(); i < newSize; i++) {
	    this.linkedNodes.add(new DoubleArrayAllocator.LinkedNode(i - 1, i + 1));
	}
	this.linkedNodes.get(newSize - 1).next = DoubleArrayAllocator.SENTINEL;
    }

    /**
     * 番地をDouble-Arrayの番地として使用
     *
     * @param node 使用する番地
     */
    private void assign(int node) {
	while (node >= this.linkedNodes.size() - 1) {
	    this.resize(0);
	}
	// 双方向リストから該当する番地を抜き出す。
	// nextが番兵の場合は使用済みノードと見なす。
	this.linkedNodes.get(this.linkedNodes.get(node).prev).next = this.linkedNodes.get(node).next;
	this.linkedNodes.get(this.linkedNodes.get(node).next).prev = this.linkedNodes.get(node).prev;
	this.linkedNodes.get(node).next = DoubleArrayAllocator.SENTINEL;
    }

    /**
     * 与えられた文字コード一覧に対して番地が使用可能の場合true
     *
     * @param codes 文字コード一覧
     * @param candidate 番地候補
     */
    private boolean isAssignable(List<Character> codes, int candidate) {
	for (Character c : codes) {
	    // 番兵がnextとなる場合は使用済み
	    if (candidate + c < this.linkedNodes.size()
		&& this.linkedNodes.get(candidate + c).next == DoubleArrayAllocator.SENTINEL) {
		return false;
	    }
	}
	return true;
    }

    /**
     * 割当可能なBASEノード番地を返す (AOE論文参照)
     *
     * @param codes 文字コード一覧
     * @return 割当可能な番地
     */
    public int xCheck(List<Character> codes) {
	// 本アロケータは最低でもLIMIT_CODE以上の初期メモリ領域を要する
	if (this.linkedNodes.size() < Constants.DACheck.LIMIT_CODE) {
	    this.resize(Constants.DACheck.LIMIT_CODE * 2);
	}
	// 初期値として文字コードの終端を使用
	for (int curr = this.linkedNodes.get(Constants.DACheck.LIMIT_CODE).next; ; curr = this.linkedNodes.get(curr).next) {
	    final int candidate = curr - codes.get(0);
	    if (this.bitSet.get(candidate) == false && this.isAssignable(codes, candidate)) {
		// この番地を使用済みとしてマーク
		this.bitSet.flip(candidate);
		// エッジの先となる番地を全てリンクから外す
		for (Character c : codes) {
		    this.assign(candidate + c);
		}
		return candidate;
	    }
	}
    }
}
