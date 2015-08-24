package org.okawa.util.nlang.trie.impl;

import java.util.ArrayList;

/**
 * 範囲外のアクセスに対して自動的にリストの拡張が行われる。
 */
final class DynamicArrayList<E> extends ArrayList<E> {

    /**
     * 範囲外アクセスがあった場合は自動的にリストが拡張されデフォルト値がセットされる。
     *
     * @param index リストのインデックス
     * @param defaultValue 要素のデフォルト値
     */
    public E get(int index, E defaultValue) {
	try {
	    return super.get(index);
	} catch (IndexOutOfBoundsException ex) {
	    for (int i = super.size(); i <= index * 2; i++) {
		super.add(defaultValue);
	    }
	    return super.get(index);
	}
    }

    /**
     * 範囲外アクセスがあった場合は自動的にリストが拡張され、対象の要素意外にはデフォル
     * ト値がセットされる。
     *
     * @param index リストのインデックス
     * @param element 格納する要素
     * @param defaultValue 要素のデフォルト値
     * @return インデックスの位置に前回格納されていた値
     */
    public E set(int index, E element, E defaultValue) {
	try {
	    return super.set(index, element);
	} catch (IndexOutOfBoundsException ex) {
	    for (int i = super.size(); i <= index * 2; i++) {
		super.add(defaultValue);
	    }
	    return super.set(index, element);
	}
    }
}
