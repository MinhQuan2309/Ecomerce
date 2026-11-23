package com.example.nguyenminhquan_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "MyDB";
    private static final int DB_VERSION = 4;

    private static AppDatabase instance;

    private AppDatabase(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    private static SQLiteDatabase open(Context context) {
        return getInstance(context).getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        seedProducts(db);
        applyProductContent(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTables(db);
        seedProducts(db);
        applyProductContent(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "price REAL, "
                + "description TEXT, "
                + "image TEXT, "
                + "category TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS cart ("
                + "product_id INTEGER PRIMARY KEY, "
                + "quantity INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS orders ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT, "
                + "total REAL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS favorites ("
                + "product_id INTEGER PRIMARY KEY);");

        db.execSQL("CREATE TABLE IF NOT EXISTS reviews ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "product_id INTEGER, "
                + "username TEXT, "
                + "rating INTEGER, "
                + "comment TEXT, "
                + "created_at INTEGER);");
    }

    private static final String[][] PRODUCTS = {
            {"Áo thun trắng", "150000", "ao_thun.jpg", Constants.CATEGORY_SHIRT,
                    "Áo thun cotton 100% dệt kim, bề mặt mềm mịn và thấm hút mồ hôi tốt. "
                            + "Form rộng thoải mái, cổ bo giữ phom sau nhiều lần giặt. "
                            + "Phù hợp mặc hằng ngày hoặc phối cùng áo khoác."},

            {"Áo sơ mi kẻ", "280000", "ao_so_mi.jpg", Constants.CATEGORY_SHIRT,
                    "Sơ mi vải lụa pha mềm rủ, họa tiết kẻ sọc nhỏ thanh lịch. "
                            + "Tay dài có măng sét, cổ đứng dễ phối với quần âu hoặc quần jean. "
                            + "Vải ít nhăn, giữ dáng tốt khi mặc cả ngày."},

            {"Áo khoác gió", "540000", "ao_khoac_gio.jpg", Constants.CATEGORY_SHIRT,
                    "Áo khoác hai lớp, lớp ngoài chắn gió và hạt mưa nhẹ, lớp lót lưới thoáng khí. "
                            + "Có mũ trùm, khóa kéo kín cổ và hai túi hai bên. "
                            + "Gấp gọn được nên tiện mang theo khi đi xa."},

            {"Quần jean xanh", "350000", "quan_jean.jpg", Constants.CATEGORY_PANTS,
                    "Quần jean denim co giãn nhẹ, ôm vừa phải và thoải mái khi ngồi lâu. "
                            + "Màu xanh wash cổ điển, càng mặc càng lên màu đẹp. "
                            + "Năm túi tiêu chuẩn, đường may chắc chắn."},

            {"Quần short kaki", "190000", "quan_short.jpg", Constants.CATEGORY_PANTS,
                    "Quần short vải kaki mềm, thoáng mát cho ngày nắng. "
                            + "Lưng thun phối dây rút dễ điều chỉnh, bốn túi rộng đựng vừa điện thoại. "
                            + "Ống đứng gọn gàng, dễ phối với áo thun."},

            {"Giày sneaker", "650000", "giay_sneaker.jpg", Constants.CATEGORY_SHOES,
                    "Giày sneaker màu trắng, đế cao su bám tốt trên cả nền khô và ẩm. "
                            + "Lót trong êm, nâng đỡ bàn chân khi đi bộ nhiều giờ. "
                            + "Thiết kế tối giản nên dễ phối với mọi trang phục."},

            {"Giày chạy bộ", "890000", "giay_chay_bo.jpg", Constants.CATEGORY_SHOES,
                    "Giày chạy bộ siêu nhẹ, thân lưới thoáng khí giúp chân luôn khô ráo. "
                            + "Đế giữa đàn hồi giảm chấn cho mỗi bước chạy. "
                            + "Gót ôm chắc, hạn chế trượt gót khi tăng tốc."},

            {"Mũ lưỡi trai", "95000", "mu_luoi_trai.jpg", Constants.CATEGORY_ACCESSORY,
                    "Mũ vải kaki dày dặn, lưỡi cong ôm đầu che nắng tốt. "
                            + "Khóa sau điều chỉnh được nhiều cỡ đầu. "
                            + "Có lỗ thoáng khí, giặt tay nhanh khô."},

            {"Balo laptop", "420000", "balo_laptop.jpg", Constants.CATEGORY_ACCESSORY,
                    "Balo dung tích 20 lít, ngăn chống sốc đựng vừa laptop 15 inch. "
                            + "Vải phủ chống nước, dây đeo đệm dày giảm mỏi vai. "
                            + "Nhiều ngăn phụ để sắp xếp sạc, sổ tay và chai nước."},

            {"Đồng hồ thể thao", "1250000", "dong_ho.jpg", Constants.CATEGORY_ACCESSORY,
                    "Đồng hồ thể thao chống nước 5ATM, thoải mái khi rửa tay hoặc đi mưa. "
                            + "Mặt kính cứng chống xước, dây silicon mềm ôm cổ tay. "
                            + "Hiển thị giờ, ngày và có chức năng bấm giờ."},

            {"Kính râm", "320000", "kinh_ram.jpg", Constants.CATEGORY_ACCESSORY,
                    "Kính râm tròng chống tia UV400, giảm chói khi đi đường nắng gắt. "
                            + "Gọng nhựa dẻo nhẹ, ôm thái dương mà không gây đau. "
                            + "Tặng kèm hộp cứng và khăn lau chuyên dụng."},

            {"Tai nghe không dây", "790000", "tai_nghe.jpg", Constants.CATEGORY_ACCESSORY,
                    "Tai nghe Bluetooth cho 30 giờ nghe liên tục kèm hộp sạc. "
                            + "Đệm tai mềm, cách âm tốt khi nghe nhạc hoặc gọi thoại. "
                            + "Kết nối nhanh, nút cảm ứng điều khiển tiện lợi."},

            {"Bình giữ nhiệt", "240000", "binh_giu_nhiet.jpg", Constants.CATEGORY_OTHER,
                    "Bình inox 304 hai lớp chân không, giữ nóng và giữ lạnh tới 12 giờ. "
                            + "Nắp vặn kín, không rò rỉ khi để nằm trong balo. "
                            + "Thân sơn nhám chống trượt, vừa tay cầm."},

            {"Sổ tay bìa da", "85000", "so_tay.jpg", Constants.CATEGORY_OTHER,
                    "Sổ tay 200 trang giấy kem ít lóa, viết mực gel không thấm ngược. "
                            + "Bìa da mềm bảo vệ ruột sổ, gáy khâu mở phẳng dễ viết. "
                            + "Kích thước gọn, bỏ vừa túi xách."}
    };

    private void seedProducts(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM products", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();

        if (count > 0) return;

        for (String[] row : PRODUCTS) {
            db.execSQL("INSERT INTO products (name, price, description, image, category) "
                            + "VALUES (?, ?, ?, ?, ?)",
                    new Object[]{row[0], Double.parseDouble(row[1]), row[4], row[2], row[3]});
        }
    }

    private void applyProductContent(SQLiteDatabase db) {
        for (String[] row : PRODUCTS) {
            db.execSQL("UPDATE products SET image = ?, description = ? WHERE name = ?",
                    new Object[]{row[2], row[4], row[0]});
        }
    }

    public static List<Product> getProducts(Context context, String category) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = open(context);

        Cursor c;
        if (category == null) {
            c = db.rawQuery("SELECT id, name, price, description, image FROM products", null);
        } else {
            c = db.rawQuery("SELECT id, name, price, description, image FROM products "
                    + "WHERE category = ?", new String[]{category});
        }

        while (c.moveToNext()) {
            list.add(readProduct(context, c));
        }
        c.close();
        return list;
    }

    private static Product readProduct(Context context, Cursor c) {
        String imageName = c.getString(4);

        if (imageName != null && imageName.contains(".")) {
            return new Product(c.getInt(0), c.getString(1), c.getDouble(2),
                    c.getString(3), 0, imageName);
        }

        int imageRes = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());

        return new Product(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3), imageRes);
    }

    public static Set<Integer> getFavoriteIds(Context context) {
        Set<Integer> ids = new HashSet<>();
        SQLiteDatabase db = open(context);

        Cursor c = db.rawQuery("SELECT product_id FROM favorites", null);
        while (c.moveToNext()) {
            ids.add(c.getInt(0));
        }
        c.close();
        return ids;
    }

    public static List<Product> getFavorites(Context context) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = open(context);

        Cursor c = db.rawQuery(
                "SELECT p.id, p.name, p.price, p.description, p.image "
                        + "FROM favorites f JOIN products p ON p.id = f.product_id", null);
        while (c.moveToNext()) {
            list.add(readProduct(context, c));
        }
        c.close();
        return list;
    }

    public static boolean isFavorite(Context context, int productId) {
        SQLiteDatabase db = open(context);

        Cursor c = db.rawQuery("SELECT product_id FROM favorites WHERE product_id = ?",
                new String[]{String.valueOf(productId)});
        boolean result = c.moveToFirst();
        c.close();
        return result;
    }

    public static boolean toggleFavorite(Context context, int productId) {
        boolean wasFavorite = isFavorite(context, productId);
        SQLiteDatabase db = open(context);

        if (wasFavorite) {
            db.execSQL("DELETE FROM favorites WHERE product_id = ?", new Object[]{productId});
        } else {
            db.execSQL("INSERT INTO favorites (product_id) VALUES (?)", new Object[]{productId});
        }

        return !wasFavorite;
    }

    public static void addToCart(Context context, int productId) {
        SQLiteDatabase db = open(context);

        Cursor c = db.rawQuery("SELECT quantity FROM cart WHERE product_id = ?",
                new String[]{String.valueOf(productId)});
        boolean daCo = c.moveToFirst();
        c.close();

        if (daCo) {
            db.execSQL("UPDATE cart SET quantity = quantity + 1 WHERE product_id = ?",
                    new Object[]{productId});
        } else {
            db.execSQL("INSERT INTO cart (product_id, quantity) VALUES (?, 1)",
                    new Object[]{productId});
        }
    }

    public static void changeQuantity(Context context, int productId, int delta) {
        SQLiteDatabase db = open(context);
        db.execSQL("UPDATE cart SET quantity = quantity + ? WHERE product_id = ?",
                new Object[]{delta, productId});
        db.execSQL("DELETE FROM cart WHERE quantity <= 0");
    }

    public static List<CartItem> getCartItems(Context context) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = open(context);

        Cursor c = db.rawQuery(
                "SELECT p.id, p.name, p.price, p.description, p.image, c.quantity "
                        + "FROM cart c JOIN products p ON p.id = c.product_id", null);
        while (c.moveToNext()) {
            list.add(new CartItem(readProduct(context, c), c.getInt(5)));
        }
        c.close();
        return list;
    }

    public static int getCartCount(Context context) {
        SQLiteDatabase db = open(context);
        Cursor c = db.rawQuery("SELECT IFNULL(SUM(quantity), 0) FROM cart", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public static double getCartTotal(Context context) {
        SQLiteDatabase db = open(context);
        Cursor c = db.rawQuery("SELECT IFNULL(SUM(p.price * c.quantity), 0) "
                + "FROM cart c JOIN products p ON p.id = c.product_id", null);
        c.moveToFirst();
        double total = c.getDouble(0);
        c.close();
        return total;
    }

    public static void clearCart(Context context) {
        SQLiteDatabase db = open(context);
        db.execSQL("DELETE FROM cart");
    }

    public static void addReview(Context context, int productId, String username,
                                 int rating, String comment) {
        SQLiteDatabase db = open(context);
        db.execSQL("INSERT INTO reviews (product_id, username, rating, comment, created_at) "
                        + "VALUES (?, ?, ?, ?, ?)",
                new Object[]{productId, username, rating, comment, System.currentTimeMillis()});
    }

    public static List<Review> getReviews(Context context, int productId) {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = open(context);

        Cursor c = db.rawQuery("SELECT id, username, rating, comment, created_at FROM reviews "
                        + "WHERE product_id = ? ORDER BY created_at DESC",
                new String[]{String.valueOf(productId)});
        while (c.moveToNext()) {
            list.add(new Review(c.getInt(0), c.getString(1), c.getInt(2),
                    c.getString(3), c.getLong(4)));
        }
        c.close();
        return list;
    }

    public static RatingSummary getRatingSummary(Context context, int productId) {
        SQLiteDatabase db = open(context);
        int[] stars = new int[5];
        int total = 0;
        int sum = 0;

        Cursor c = db.rawQuery("SELECT rating, COUNT(*) FROM reviews "
                        + "WHERE product_id = ? GROUP BY rating",
                new String[]{String.valueOf(productId)});
        while (c.moveToNext()) {
            int rating = c.getInt(0);
            int count = c.getInt(1);

            if (rating >= 1 && rating <= 5) {
                stars[rating - 1] = count;
                total += count;
                sum += rating * count;
            }
        }
        c.close();

        double average = total == 0 ? 0 : (double) sum / total;
        return new RatingSummary(total, average, stars);
    }

    public static void createOrder(Context context, String username, double total) {
        SQLiteDatabase db = open(context);
        db.execSQL("INSERT INTO orders (username, total) VALUES (?, ?)",
                new Object[]{username, total});
    }
}
