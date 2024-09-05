package com.atguigu.gulimall.common.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartItemVo {
    private Long skuId;
	
    //是否选中
    private Boolean check = true;

    //标题
    private String title;
	
    //图片
    private String image;

    //商品套餐属性
    private List<String> skuAttrValues;

    //价格
    private BigDecimal price;

    //数量
    private Integer count;

    //总价
    private BigDecimal totalPrice;
    
      /**
     * 当前购物车项总价等于单价x数量
     * @return
     */
    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(count));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
