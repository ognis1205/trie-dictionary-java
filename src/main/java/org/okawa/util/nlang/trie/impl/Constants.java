package org.okawa.util.nlang.trie.impl;

/**
 * 各種定数が定義されるクラス。
 */
final class Constants {
    /**
     * Double-Array Base配列に関する定数
     */
    public static class DABase {
	/** BASEノード初期値 */
	public static final int INIT_VALUE = Integer.MIN_VALUE;
	/** BASEノードに格納されるIDのエンコード・デコード (自己逆関数) */
	public static int ID(int id) {
	    return id * -1 - 1;
	}
    }

    /**
     * Double-Array Check配列に関する定数
     */
    public static class DACheck {
	/** 終端を表す文字定数 */
	public static final char TERM_CODE = 0;
	/** 未使用ノードに付与される定数 */
	public static final char EMPTY_CODE = 1;
	/** 使用可能な文字コードの最大値 (多バイト長コードも1バイトずつ処理) */
	public static final char LIMIT_CODE = 0xFFFF;
    }
}
