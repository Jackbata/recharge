package com.up.lhm.recharge.data;

/**
 * @author lianghaimiao
 * @date 2019/1/22
 * @function 金额信息
 */

public class AmountRespone {
    /**
     * 普通金额标签
     */
    public final static int NORMAL_NUM = 0;
    /**
     * 其他金额标签
     */
    public final static int OTHER_NUM = 1;
    /**
     * 固定金额
     */
    public String amount;
    /**
     * 类型标签 普通金额标签:0 其他金额标签:1
     */
    public int itemType;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    /**
     * 是否默认选中
     */
    public boolean isSelect;

}
