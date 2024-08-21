/*
 * @Date: 2024-08-06 14:08:43
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-13 00:58:47
 * @FilePath: \Guli\cart\src\main\java\com\atguigu\gulimall\cart\controller\CartController.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/currentUserCartItems")
    public List<CartItemVo> getCurrentCartItems() {
        return cartService.getUserCartItems();
    }

    @GetMapping("/cart.html")
    public String cartListPage(Model model) {
        CartVo cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    @GetMapping("/addCartItem")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
            RedirectAttributes attributes) {
        cartService.addToCart(skuId, num);
        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        CartItemVo cartItem = cartService.getCartItem(skuId);
        if (cartItem != null) {
            model.addAttribute("cartItem", cartItem);
            return "success";
        } else {
            return "redirect:http://item.gulimall.com/" + skuId + ".html";
        }

    }

    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.countItem(skuId, num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @GetMapping("/checkCart")
    public String checkCart(@RequestParam("isChecked") Integer isChecked, @RequestParam("skuId") Long skuId) {
        cartService.checkCart(isChecked, skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @GetMapping("/deleteItem")
    public String deleteCartItem(@RequestParam("skuId") Long skuId, RedirectAttributes attributes) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

}
