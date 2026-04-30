package com.example.fastmart.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fastmart.models.CartItem;
import com.example.fastmart.models.Product;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "fastmart.db";
    private static final int    DB_VERSION = 1;

    // favourites table
    private static final String TABLE_FAVOURITES  = "favourites";
    private static final String FAV_ID            = "id";
    private static final String FAV_NAME          = "name";
    private static final String FAV_PRICE         = "price";
    private static final String FAV_ORIGINAL_PRICE = "original_price";
    private static final String FAV_MODEL         = "model";
    private static final String FAV_DESCRIPTION   = "description";
    private static final String FAV_IMAGE_RES     = "image_res";

    // cart table
    private static final String TABLE_CART        = "cart";
    private static final String CART_ID           = "id";
    private static final String CART_NAME         = "name";
    private static final String CART_PRICE        = "price";
    private static final String CART_ORIGINAL_PRICE = "original_price";
    private static final String CART_MODEL        = "model";
    private static final String CART_DESCRIPTION  = "description";
    private static final String CART_IMAGE_RES    = "image_res";
    private static final String CART_QUANTITY     = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create favourites table to store products the user hearts
        String createFavourites = "CREATE TABLE " + TABLE_FAVOURITES + " ("
                + FAV_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAV_NAME          + " TEXT, "
                + FAV_PRICE         + " TEXT, "
                + FAV_ORIGINAL_PRICE + " TEXT, "
                + FAV_MODEL         + " TEXT UNIQUE, "
                + FAV_DESCRIPTION   + " TEXT, "
                + FAV_IMAGE_RES     + " INTEGER)";

        // create cart table to store products added to cart with quantity
        String createCart = "CREATE TABLE " + TABLE_CART + " ("
                + CART_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CART_NAME         + " TEXT, "
                + CART_PRICE        + " TEXT, "
                + CART_ORIGINAL_PRICE + " TEXT, "
                + CART_MODEL        + " TEXT UNIQUE, "
                + CART_DESCRIPTION  + " TEXT, "
                + CART_IMAGE_RES    + " INTEGER, "
                + CART_QUANTITY     + " INTEGER DEFAULT 1)";

        db.execSQL(createFavourites);
        db.execSQL(createCart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // fvrts

    // insert product into favourites table
    public boolean addFavourite(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAV_NAME,           product.getName());
        values.put(FAV_PRICE,          product.getPrice());
        values.put(FAV_ORIGINAL_PRICE, product.getOriginalPrice());
        values.put(FAV_MODEL,          product.getModel());
        values.put(FAV_DESCRIPTION,    product.getDescription());
        values.put(FAV_IMAGE_RES,      product.getImageRes());

        long result = db.insertWithOnConflict(TABLE_FAVOURITES,
                null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return result != -1;
    }

    // delete product from favourites using model as unique identifier
    public void removeFavourite(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, FAV_MODEL + "=?", new String[]{model});
        db.close();
    }

    // check if a product is already in favourites
    public boolean isFavourite(String model) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITES, new String[]{FAV_ID},
                FAV_MODEL + "=?", new String[]{model},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // get all favourite products as a list
    public ArrayList<Product> getAllFavourites() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_FAVOURITES, null);

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getString(cursor.getColumnIndexOrThrow(FAV_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FAV_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FAV_ORIGINAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FAV_MODEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FAV_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FAV_IMAGE_RES))
                );
                list.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // cart
    // insert product into cart or increase quantity if already exists
    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        // check if product already in cart using model as unique key
        Cursor cursor = db.query(TABLE_CART, new String[]{CART_ID, CART_QUANTITY},
                CART_MODEL + "=?", new String[]{product.getModel()},
                null, null, null);

        if (cursor.moveToFirst()) {
            // product exists so increase quantity using update query
            int currentQty = cursor.getInt(
                    cursor.getColumnIndexOrThrow(CART_QUANTITY));
            ContentValues values = new ContentValues();
            values.put(CART_QUANTITY, currentQty + 1);
            db.update(TABLE_CART, values,
                    CART_MODEL + "=?", new String[]{product.getModel()});
        } else {
            // product not in cart so insert as new row
            ContentValues values = new ContentValues();
            values.put(CART_NAME,           product.getName());
            values.put(CART_PRICE,          product.getPrice());
            values.put(CART_ORIGINAL_PRICE, product.getOriginalPrice());
            values.put(CART_MODEL,          product.getModel());
            values.put(CART_DESCRIPTION,    product.getDescription());
            values.put(CART_IMAGE_RES,      product.getImageRes());
            values.put(CART_QUANTITY,       1);
            db.insert(TABLE_CART, null, values);
        }

        cursor.close();
        db.close();
    }

    // increase quantity of a cart item by 1
    public void increaseCartQuantity(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CART
                + " SET " + CART_QUANTITY + " = " + CART_QUANTITY + " + 1"
                + " WHERE " + CART_MODEL + " = ?", new String[]{model});
        db.close();
    }

    // decrease quantity by 1 but never below 1
    public void decreaseCartQuantity(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{CART_QUANTITY},
                CART_MODEL + "=?", new String[]{model},
                null, null, null);

        if (cursor.moveToFirst()) {
            int qty = cursor.getInt(0);
            if (qty > 1) {
                ContentValues values = new ContentValues();
                values.put(CART_QUANTITY, qty - 1);
                db.update(TABLE_CART, values,
                        CART_MODEL + "=?", new String[]{model});
            }
        }

        cursor.close();
        db.close();
    }

    // delete a single item from cart using delete query
    public void removeFromCart(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, CART_MODEL + "=?", new String[]{model});
        db.close();
    }

    // get all cart items with their quantities
    public ArrayList<CartItem> getAllCartItems() {
        ArrayList<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getString(cursor.getColumnIndexOrThrow(CART_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CART_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CART_ORIGINAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CART_MODEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CART_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CART_IMAGE_RES))
                );
                CartItem item = new CartItem(p);
                item.setQuantity(cursor.getInt(
                        cursor.getColumnIndexOrThrow(CART_QUANTITY)));
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // calculate total price of all cart items from sqlite
    public double getCartTotal() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + CART_PRICE
                + ", " + CART_QUANTITY + " FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                String priceStr = cursor.getString(0).replace("$", "");
                int qty = cursor.getInt(1);
                total += Double.parseDouble(priceStr) * qty;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return total;
    }

    // delete all items from cart after checkout
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }
}