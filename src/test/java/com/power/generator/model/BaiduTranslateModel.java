package com.power.generator.model;

/**
 * 百度翻译数据模型
 * @author sunyu
 */
public class BaiduTranslateModel {

    private String dst;//翻译输出
    private String src;//翻译源

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
