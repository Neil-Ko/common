package com.coolsen.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Created by YeKai on 2019/1/12
 */
public class MathUtils {

    /**
     * double 精准的加法
     */
    public static double addDobule(double p1, double p2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(p1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(p2));
        return b1.add(b2).doubleValue();
    }

    /**
     * String 类型 精准加法
     */
    public static double addString(String p1, String p2) {
        BigDecimal b1 = new BigDecimal(p1);
        BigDecimal b2 = new BigDecimal(p2);
        return b1.add(b2).doubleValue();
    }

    /**
     * Double减法
     */
    public static double substract(double p1, double p2) {
        BigDecimal b1 = new BigDecimal(Double.toString(p1));
        BigDecimal b2 = new BigDecimal(Double.toString(p2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * String 减法
     */
    /**
     * String 类型 精准加法
     */
    public static double subString(String p1, String p2) {
        BigDecimal b1 = new BigDecimal(p1);
        BigDecimal b2 = new BigDecimal(p2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 两数相除 保留n位小数
     */
    public static String division(int dividend, int divisor, int scale) {
        BigDecimal b1 = new BigDecimal(dividend);
        BigDecimal b2 = new BigDecimal(divisor);
        return round(Long.valueOf(dividend), Long.valueOf(divisor), scale);
    }

    /**
     * long类型计算
     *
     * @param dividend
     * @param divisor
     * @param scale
     * @return
     */
    public static String round(long dividend, long divisor, int scale) {
        String pattern = "0.";
        for (int i = 0; i < scale; i++) {
            pattern += "0";
        }
        float num = (float) dividend / divisor;
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(num);
    }

    /**
     * double 转Long (四舍五入)
     *
     * @param number
     * @return
     */
    public static Long double2Long(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Long.valueOf(bd.toString());
    }

    /**
     * 元转分
     *
     * @param price
     * @return
     */
    public static long changeY2F(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        price = Double.valueOf(df.format(price));
        long money = (long) (price * 100);
        return money;
    }

    /**
     * 分转元，转换为bigDecimal在toString
     *
     * @return
     */
    public static String changeF2Y(long price) {
        return BigDecimal.valueOf(price).divide(new BigDecimal(100)).toString();
    }

    /**
     * 根据费率计算（取整）
     *
     * @param rate 费率(百分比)
     * @param fee  金额
     * @return 总金额
     */
    public static long rateFee(Double rate, Long fee) {
        if (Objects.isNull(rate) || Objects.isNull(fee)) {
            return 0;
        }
        //小于0 直接返回0
        if (rate < 0 || fee < 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(fee);
        BigDecimal b2 = new BigDecimal(rate);
        if (Objects.equals(rate, 0.00)) {
            return 0;
        }
        BigDecimal b3 = new BigDecimal(100);
        BigDecimal bb = b1.multiply(b2).divide(b3, 0, BigDecimal.ROUND_HALF_UP);
        return bb.longValue();
    }

}