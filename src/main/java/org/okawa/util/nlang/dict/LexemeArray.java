package org.okawa.util.nlang.dict;

import java.util.List;

/**
 * 辞書データクラス
 * キーIDに対して翻訳語をシーケンシャルに格納 (省メモリ)
 */
final class LexemeArray {
    /** DATA配列 開始位置配列 */
    private final List<Integer> begins;
    /** DATA配列 データ長 */
    private final List<Integer> lengths;
    /** DATA配列 */
    private final String data;

    /**
     * ビルダーからインスタンス化
     */
    public LexemeArray(LexemeArrayBuilder builder) {
	// DATA配列開始位置セット
	this.begins = builder.getBegins();
	// DATA長配列セット
	this.lengths = builder.getLengths();
	// 実データ配列セット
	this.data = builder.getData();
    }

    /**
     * IDで指定される番地のデータを返す
     */
    public String get(int id) {
	// データ開始位置
	int begin = begins.get(id);
	// データ長
	int length = lengths.get(id);
	// データを返す
	return data.substring(begin, begin + length);
    }
}
