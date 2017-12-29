package com.credithc.skyeye.util;


/**
 * @Description: 二元组（工具类）
 * @Author ：dongbin
 * @Date ：2017/6/8
 */
public class TwoTuple<F, S> {
    /**
     * 第一元素
     */
    private final F first;
    /**
     * 第二元素
     */
    private final S second;

    /**
     * @param first 第一个对象
     * @param second 第二个对象
     */
    public TwoTuple(F first, S second) {
        super();
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
