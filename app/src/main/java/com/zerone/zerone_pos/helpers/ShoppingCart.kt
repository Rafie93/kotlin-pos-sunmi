package com.zerone.zerone_pos.helpers
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.zerone.zerone_pos.ui.transaction.CartItem
import io.paperdb.Paper


class ShoppingCart {

    companion object {
        fun addItem(cartItem: CartItem) {
            val cart = ShoppingCart.getCart()
            val targetItem = cart.singleOrNull { it.product?.id == cartItem.product?.id}
            if (targetItem == null) {
                cart.add(cartItem)
            } else {
                if (cartItem.priceItem != targetItem.priceItem){
                    // cartItem.price += cartItem.price!!
                    cart.add(cartItem)
                }else{
                    targetItem.price+=cartItem.priceItem!!
                    targetItem.quantity++
                }
            }
            ShoppingCart.saveCart(cart)
        }
        fun addItem2(cartItem: CartItem) {
            val cart = ShoppingCart.getCart()
            val targetItem = cart.singleOrNull { it.product?.id == cartItem.product?.id}
            if (targetItem == null) {
                cart.add(cartItem)
            } else {
                if (cartItem.priceItem != targetItem.priceItem){
                    // cartItem.price += cartItem.price!!
                    cart.add(cartItem)
                }else{
                    targetItem.price+=cartItem.priceItem!!
                    targetItem.quantity++
                }
            }
            ShoppingCart.saveCart(cart)
        }
        fun removeItem(cartItem: CartItem, context: Context) {
            val cart = ShoppingCart.getCart()
            val targetItem = cart.singleOrNull { it.product?.id == cartItem.product?.id }
            if (targetItem != null) {
                Log.d("TARGETITEM",targetItem.toString())
                if (targetItem.quantity > 0) {

                    if (targetItem.quantity==0){
                        cart.remove(targetItem)
                    }else{
                        targetItem.price-= cartItem.priceItem!!
                        targetItem.quantity--
                    }
                } else {
                    cart.remove(targetItem)
                }
            }
            ShoppingCart.saveCart(cart)
        }
        fun removeAll( context: Context) {
            val cart = ShoppingCart.getCart()
            cart.removeAll(cart)
            ShoppingCart.saveCart(cart)
        }
        fun saveCart(cart: MutableList<CartItem>) {
            Paper.book().write("cart", cart)
        }
        fun getCart(): MutableList<CartItem> {
            return Paper.book().read("cart", mutableListOf())
        }
        fun getCartLength():Int{
            var cartSize = 0
            ShoppingCart.getCart().forEach {
                cartSize++
            }

            return cartSize
        }
        fun getShoppingCartSize(): Int {

            var cartSize = 0
            ShoppingCart.getCart().forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
    }

}