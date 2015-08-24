package org.okawa.util.nlang.trie.impl;

/**
 * 文字列を扱うためのストリーム。
 */
final class StringStream implements Comparable<StringStream> {
    /** 実データ */
    private final CharSequence sequence;
    /** 参照中のインデックス */
    private int curr;

    /**
     * CharSequenceのサブクラスからインスタンス化
     *
     * @param sequence 実データ
     */
    public StringStream(CharSequence sequence) {
	this.sequence = sequence;
	this.curr = 0;
    }

    /**
     * CharSequenceのサブクラスからインデックスを明示してインスタンス化
     *
     * @param sequence 実データ
     * @param index 参照するインデックス
     */
    public StringStream(CharSequence sequence, int index) {
	this.sequence = sequence;
	this.curr = index;
    }

    /**
     * Comparableインターフェース実装 (Stringクラスへ委譲)
     *
     * @param stream 右辺値。接尾辞によって比較。
     */
    @Override
	public int compareTo(StringStream stream) {
	return this.rest().compareTo(stream.rest());
    }

    /**
     * 引数で与えられる接頭辞を持つか
     *
     * @param prefix 接頭辞
     * @param begin 接頭辞のどこから判別するか
     * @param length 接頭辞の長さ
     */
    public boolean startsWith(CharSequence prefix, int begin, int length) {
	if (this.sequence.length() - this.curr < length) {
	    return false;
	}
	for (int i = 0; i < length; i++) {
	    if (this.sequence.charAt(this.curr + i) != prefix.charAt(begin + i)) {
		return false;
	    }
	}
	return true;
    }

    /**
     * 未処理の部分文字列を返す。
     */
    public String rest() {
	return this.sequence.subSequence(this.curr, this.sequence.length()).toString();
    }

    /**
     * 未処理部分からオフセット分ずらした部分文字列を返す。
     *
     * @param offset 未処理部分からどれだけずらすか
     */
    public String rest(int offset) {
	int begin = this.curr + offset;
	if (0 <= begin && begin < this.sequence.length()) {
	    return this.sequence.subSequence(begin, this.sequence.length()).toString();
	}
	return this.rest();
    }

    /**
     * 文字を一文字読み込む
     */
    public char read() {
	return this.eos() ? Constants.DACheck.TERM_CODE : this.sequence.charAt(this.curr++);
    }

    /**
     * 文字列の終端に達した場合true
     */
    public boolean eos() {
	return this.curr == this.sequence.length();
    }
}
