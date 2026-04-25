package com.example.fastmart.root;

import android.app.Application;

import com.example.fastmart.R;
import com.example.fastmart.model.CartItem;
import com.example.fastmart.model.Product;

import java.util.ArrayList;

public class MyApplication extends Application {

    // global product list shared across all fragments
    public ArrayList<Product> masterProductList;

    // global cart list replacing CartManager
    public ArrayList<CartItem> cartList;

    @Override
    public void onCreate() {
        super.onCreate();

        cartList = new ArrayList<>();
        masterProductList = new ArrayList<>();
        initProducts();
    }

    private void initProducts() {
        // cycling through 4 base products to create 25 unique items
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$320.99", "$399.99",
                "Model: WH-1000XM5, Black", "Industry leading noise cancellation with LDAC support", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$349.99", "$429.99",
                "Model: WH-1000XM4, Black", "30 hour battery with quick charge", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$349.99", "$429.99",
                "Model: WH-1000XM4, Beige", "Multipoint connection, speak to chat", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wired RODE PodMic", "$108.20", "$159.99",
                "Model: WH-1000XM7, Beige", "Dynamic microphone with internal shock mount", R.drawable.podemic));

        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$310.00", "$390.00",
                "Model: WH-1000XM5, Midnight Blue", "Hi-Res audio certified headphones", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$299.99", "$379.99",
                "Model: WH-1000XM4, Graphite", "Adaptive sound control technology", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$339.99", "$419.99",
                "Model: WH-1000XM4, Sand Beige", "Touch sensor controls on ear cup", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wired RODE PodMic", "$115.00", "$165.00",
                "Model: WH-1000XM7-Pro, Black", "Cardioid polar pattern, broadcast grade", R.drawable.podemic));

        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$325.00", "$405.00",
                "Model: WH-1000XM5, Champagne", "Precise voice pickup for calls", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$315.99", "$395.99",
                "Model: WH-1000XM5, Silver", "Lightweight foldable design", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$329.99", "$409.99",
                "Model: WH-1000XM4, Pearl White", "Wearing detection auto pause", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wired RODE PodMic", "$99.99", "$139.99",
                "Model: WH-1000XM7-Lite, Beige", "Studio quality podcast recording", R.drawable.podemic));

        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$335.00", "$415.00",
                "Model: WH-1000XM5, Rose Gold", "Premium comfort with soft headband", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$309.99", "$389.99",
                "Model: WH-1000XM4, Navy", "Dual noise sensor technology", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$344.99", "$424.99",
                "Model: WH-1000XM4, Cream", "Ambient sound mode with 20 levels", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wired RODE PodMic", "$105.00", "$149.99",
                "Model: WH-1000XM7-Gen2, Black", "Internal pop filter for clean audio", R.drawable.podemic));

        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$318.00", "$398.00",
                "Model: WH-1000XM5, Ash Grey", "HD noise minimization processor QN1", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$289.99", "$369.99",
                "Model: WH-1000XM4, Smoke", "360 reality audio compatible", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$355.00", "$435.00",
                "Model: WH-1000XM4, Ivory", "Smart listening by Sony headphones app", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wired RODE PodMic", "$120.00", "$175.00",
                "Model: WH-1000XM7-Studio, Beige", "Optimised for voice over work", R.drawable.podemic));

        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$322.50", "$402.50",
                "Model: WH-1000XM5, Obsidian", "Optimised for Google Assistant", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$349.00", "$429.00",
                "Model: WH-1000XM4, Caramel", "Alexa built in voice control", R.drawable.sonybeige));
        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$295.00", "$375.00",
                "Model: WH-1000XM4, Slate", "Seamless Bluetooth multipoint", R.drawable.sony));
        masterProductList.add(new Product("SONY Premium Wired RODE PodMic", "$110.00", "$155.00",
                "Model: WH-1000XM7-USB, Black", "USB-C and XLR dual connectivity", R.drawable.podemic));

        masterProductList.add(new Product("SONY Premium Wireless Headphones", "$340.00", "$420.00",
                "Model: WH-1000XM5, Arctic White", "30 hour battery life wireless", R.drawable.sonybeige));
    }

    // add to cart or increase quantity if already exists
    public void addToCart(Product p) {
        for (CartItem item : cartList) {
            if (item.getProduct().getName().equals(p.getName()) &&
                    item.getProduct().getModel().equals(p.getModel())) {
                item.increaseQuantity();
                return;
            }
        }
        cartList.add(new CartItem(p));
    }

    // remove a specific cart item
    public void removeFromCart(CartItem item) {
        cartList.remove(item);
    }

    // get full cart list
    public ArrayList<CartItem> getCartItems() {
        return cartList;
    }

    // calculate total price of all cart items
    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            String priceStr = item.getProduct().getPrice().replace("$", "");
            total += Double.parseDouble(priceStr) * item.getQuantity();
        }
        return total;
    }
}