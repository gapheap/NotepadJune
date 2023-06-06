package com.stone.win;

/**
 * 编码
 * 
 * @author: stone
 * @date 2023-6-6 13:41:57
 */
public enum CommonEncodingEnum {
    ISO("ISO-8859-1"), UTF8("UTF-8"), GB2312("GB2312"), BIG5("BIG5");

    private String charset;

    CommonEncodingEnum(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
