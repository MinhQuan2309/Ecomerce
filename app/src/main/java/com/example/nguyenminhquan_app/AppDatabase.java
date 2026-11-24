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
    private static final int DB_VERSION = 6;

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
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        getInstance(context).seedProducts(db);
        getInstance(context).applyProductContent(db);
        getInstance(context).seedReviews(db);
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        seedProducts(db);
        applyProductContent(db);
        seedReviews(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            try {
                Cursor c = db.rawQuery("PRAGMA table_info(orders)", null);
                boolean hasOrderCode = false;
                while (c.moveToNext()) {
                    if ("order_code".equalsIgnoreCase(c.getString(1))) {
                        hasOrderCode = true;
                        break;
                    }
                }
                c.close();
                if (!hasOrderCode) {
                    db.execSQL("DROP TABLE IF EXISTS orders;");
                    db.execSQL("DROP TABLE IF EXISTS order_items;");
                }
            } catch (Exception ignored) {
            }
        }
        createTables(db);
        seedProducts(db);
        applyProductContent(db);
        seedReviews(db);
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

        try {
            Cursor c = db.rawQuery("PRAGMA table_info(orders)", null);
            boolean hasOrderCode = false;
            while (c.moveToNext()) {
                if ("order_code".equalsIgnoreCase(c.getString(1))) {
                    hasOrderCode = true;
                    break;
                }
            }
            c.close();
            if (!hasOrderCode) {
                db.execSQL("DROP TABLE IF EXISTS orders;");
            }
        } catch (Exception ignored) {}

        db.execSQL("CREATE TABLE IF NOT EXISTS orders ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "order_code TEXT, "
                + "username TEXT, "
                + "receiver_name TEXT, "
                + "receiver_phone TEXT, "
                + "receiver_email TEXT, "
                + "receiver_address TEXT, "
                + "shipping_method TEXT, "
                + "payment_method TEXT, "
                + "note TEXT, "
                + "subtotal REAL, "
                + "shipping_fee REAL, "
                + "discount REAL, "
                + "total REAL, "
                + "status TEXT, "
                + "created_at INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "order_id INTEGER, "
                + "product_id INTEGER, "
                + "product_name TEXT, "
                + "product_price REAL, "
                + "quantity INTEGER, "
                + "product_image TEXT);");

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
                            + "Kích thước gọn, bỏ vừa túi xách."},

            {"Áo thun Polo dệt kim", "220000", "ao_thun.jpg", "Áo",
                    "Áo thun Polo dệt kim cổ bẻ cao cấp, bề mặt vải dệt nổi thoáng khí, phom dáng thanh lịch."},

            {"Áo thun thể thao Pro Dry", "180000", "ao_thun.jpg", "Áo",
                    "Chất vải thun lạnh co giãn 4 chiều, công nghệ Dry-fit nhanh khô, chuyên dụng tập gym và chạy bộ."},

            {"Áo sơ mi trắng ngắn tay", "260000", "ao_so_mi.jpg", "Áo",
                    "Sơ mi cộc tay sợi tre kháng khuẩn tự nhiên, hạn chế nhăn và thấm hút mồ hôi tối ưu."},

            {"Áo khoác dù phối mảng", "490000", "ao_khoac_gio.jpg", "Áo",
                    "Thiết kế line art phối mảng cá tính, cản gió và chống nước mưa phùn, phong cách streetwear."},

            {"Quần jean đen Tek Black", "390000", "quan_jean.jpg", "Quần",
                    "Dòng Tek Black đen tuyền giữ màu lâu, chất denim co giãn tôn dáng, dễ phối với mọi loại áo."},

            {"Quần short thể thao Pro", "160000", "quan_short.jpg", "Quần",
                    "Vải dù gió thể thao siêu nhẹ, có lót lưới và túi khóa kéo an toàn cho điện thoại."},

            {"Áo thun ba lỗ thể thao", "135000", "ao_thun.jpg", "Áo",
                    "Áo thun ba lỗ tanktop cotton co giãn, thoáng mát cho tập gym và mặc ở nhà."},

            {"Áo thun tay dài basic", "175000", "ao_thun.jpg", "Áo",
                    "Áo thun tay dài cổ tròn vải cotton dày dặn, giữ ấm nhẹ khi trời se lạnh."},

            {"Áo khoác hoodie nỉ basic", "380000", "ao_khoac_gio.jpg", "Áo",
                    "Áo khoác hoodie nỉ chui đầu có mũ trùm ấm áp, form rộng unisex dạo phố."},

            {"Quần tây âu công sở", "320000", "quan_jean.jpg", "Quần",
                    "Quần tây âu dáng suông công sở lịch lãm, vải tuyết mưa đứng form ít nhăn."},

            {"Quần jogger thun bo gấu", "240000", "quan_jean.jpg", "Quần",
                    "Quần jogger thể thao ống bo gấu năng động, lưng chun co giãn tiện lợi."}
    };

    private void seedProducts(SQLiteDatabase db) {
        for (String[] row : PRODUCTS) {
            Cursor check = db.rawQuery("SELECT id FROM products WHERE name = ?", new String[]{row[0]});
            if (!check.moveToFirst()) {
                db.execSQL("INSERT INTO products (name, price, description, image, category) "
                                + "VALUES (?, ?, ?, ?, ?)",
                        new Object[]{row[0], Double.parseDouble(row[1]), row[4], row[2], row[3]});
            }
            check.close();
        }
    }

    private void applyProductContent(SQLiteDatabase db) {
        for (String[] row : PRODUCTS) {
            db.execSQL("UPDATE products SET image = ?, description = ?, category = ? WHERE name = ?",
                    new Object[]{row[2], row[4], row[3], row[0]});
        }
    }

    private void seedReviews(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM reviews", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();

        if (count > 0) return;

        String[][] sampleReviews = {
                {"1", "Hoàng Nam", "5", "Chất vải cotton mềm mịn, mặc thoáng mát, phom vừa vặn rất ưng ý."},
                {"1", "Thùy Linh", "5", "Giao hàng siêu nhanh, áo đóng gói kỹ càng, đường chỉ may rất đẹp."},
                {"1", "Đức Anh", "4", "Áo đẹp, đúng mô tả, giặt không bị co rút hay phai màu."},
                {"2", "Minh Trí", "5", "Sơ mi form đứng rất đẹp, vải ít nhăn, mặc đi làm ai cũng khen."},
                {"2", "Hải Đăng", "5", "Chất lượng tuyệt vời trong tầm giá, nhân viên tư vấn size chuẩn."},
                {"3", "Khánh Vy", "5", "Áo khoác gió nhẹ mà chắn gió tốt, đi phượt hay đi tour rất tiện."},
                {"4", "Tuấn Kiệt", "5", "Quần jean co giãn thoải mái, màu xanh wash chuẩn hình mẫu."},
                {"5", "Bảo Long", "4", "Quần short kaki form đẹp, túi sâu đựng điện thoại vừa vặn."},
                {"6", "Hữu Phước", "5", "Sneaker đi rất êm chân, đế bám tốt và dễ phối với mọi loại đồ."},
                {"7", "Văn Hùng", "5", "Giày chạy bộ cực nhẹ, đệm đàn hồi êm ái, chạy 5km không thấy mỏi."},
                {"8", "Ngọc Ánh", "5", "Mũ lưỡi trai vải dày dặn, form cứng cáp đội rất phong cách."},
                {"9", "Quốc Bảo", "5", "Balo ngăn chống sốc dày, đựng vừa laptop 15.6 inch và nhiều phụ kiện."},
                {"10", "Thanh Tùng", "5", "Đồng hồ thể thao chống nước tốt, pin trâu và kiểu dáng nam tính."},
                {"11", "Thu Trang", "5", "Kính râm phân cực chống chói tốt khi lái xe trời nắng, tặng kèm hộp xịn."},
                {"12", "Việt Hoàng", "5", "Tai nghe âm bass chắc, pin trâu dùng cả tuần mới cần sạc lại."},
                {"13", "Mỹ Duyên", "5", "Bình giữ nhiệt giữ đá từ sáng đến chiều vẫn chưa tan hết, 10 điểm."},
                {"14", "Gia Huy", "4", "Sổ tay bìa da mềm tay, giấy dày viết mực gel không bị lem sang trang sau."}
        };

        long now = System.currentTimeMillis();
        for (String[] r : sampleReviews) {
            int pid = Integer.parseInt(r[0]);
            int rating = Integer.parseInt(r[2]);
            long created = now - ((long) (Math.random() * 5 + 1) * 86400000L);
            db.execSQL("INSERT INTO reviews (product_id, username, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)",
                    new Object[]{pid, r[1], rating, r[3], created});
        }
    }

    public static List<Product> getProducts(Context context, String category) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = open(context);

        Cursor c;
        if (category == null) {
            c = db.rawQuery("SELECT id, name, price, description, image, category FROM products", null);
        } else {
            c = db.rawQuery("SELECT id, name, price, description, image, category FROM products "
                    + "WHERE category = ?", new String[]{category});
        }

        while (c.moveToNext()) {
            list.add(readProduct(context, c));
        }
        c.close();
        return list;
    }

    private static Product readProduct(Context context, Cursor c) {
        int id = c.getInt(0);
        String name = c.getString(1);
        double price = c.getDouble(2);
        String description = c.getString(3);
        String imageName = c.getString(4);
        String category = c.getColumnCount() > 5 ? c.getString(5) : null;

        if (imageName != null && imageName.contains(".")) {
            return new Product(id, name, price, description, 0, imageName, category);
        }

        int imageRes = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());

        return new Product(id, name, price, description, imageRes, null, category);
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
                "SELECT p.id, p.name, p.price, p.description, p.image, p.category "
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
                "SELECT p.id, p.name, p.price, p.description, p.image, p.category, c.quantity "
                        + "FROM cart c JOIN products p ON p.id = c.product_id", null);
        while (c.moveToNext()) {
            list.add(new CartItem(readProduct(context, c), c.getInt(6)));
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

    public static long createOrderFull(Context context, Order order, List<CartItem> cartItems) {
        SQLiteDatabase db = open(context);
        android.content.ContentValues cv = new android.content.ContentValues();
        cv.put("order_code", order.getOrderCode());
        cv.put("username", order.getUsername());
        cv.put("receiver_name", order.getReceiverName());
        cv.put("receiver_phone", order.getReceiverPhone());
        cv.put("receiver_email", order.getReceiverEmail());
        cv.put("receiver_address", order.getReceiverAddress());
        cv.put("shipping_method", order.getShippingMethod());
        cv.put("payment_method", order.getPaymentMethod());
        cv.put("note", order.getNote());
        cv.put("subtotal", order.getSubtotal());
        cv.put("shipping_fee", order.getShippingFee());
        cv.put("discount", order.getDiscount());
        cv.put("total", order.getTotal());
        cv.put("status", order.getStatus());
        cv.put("created_at", order.getCreatedAt());

        long orderId = db.insert("orders", null, cv);
        if (orderId > 0 && cartItems != null) {
            for (CartItem ci : cartItems) {
                android.content.ContentValues itemCv = new android.content.ContentValues();
                itemCv.put("order_id", orderId);
                itemCv.put("product_id", ci.getProduct().getId());
                itemCv.put("product_name", ci.getProduct().getName());
                itemCv.put("product_price", ci.getProduct().getPrice());
                itemCv.put("quantity", ci.getQuantity());
                itemCv.put("product_image", ci.getProduct().getAssetName() != null ? ci.getProduct().getAssetName() : "");
                db.insert("order_items", null, itemCv);
            }
        }
        return orderId;
    }

    public static Order getOrderById(Context context, long orderId) {
        SQLiteDatabase db = open(context);
        Cursor c = db.rawQuery("SELECT id, order_code, username, receiver_name, receiver_phone, receiver_email, receiver_address, "
                + "shipping_method, payment_method, note, subtotal, shipping_fee, discount, total, status, created_at "
                + "FROM orders WHERE id = ?", new String[]{String.valueOf(orderId)});
        Order order = null;
        if (c.moveToFirst()) {
            order = new Order(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getDouble(10),
                    c.getDouble(11),
                    c.getDouble(12),
                    c.getDouble(13),
                    c.getString(14),
                    c.getLong(15)
            );
        }
        c.close();

        if (order != null) {
            List<OrderItem> items = new ArrayList<>();
            Cursor ic = db.rawQuery("SELECT id, order_id, product_id, product_name, product_price, quantity, product_image "
                    + "FROM order_items WHERE order_id = ?", new String[]{String.valueOf(orderId)});
            while (ic.moveToNext()) {
                items.add(new OrderItem(
                        ic.getInt(0),
                        ic.getInt(1),
                        ic.getInt(2),
                        ic.getString(3),
                        ic.getDouble(4),
                        ic.getInt(5),
                        ic.getString(6)
                ));
            }
            ic.close();
            order.setItems(items);
        }
        return order;
    }

    public static List<Order> getOrdersByUser(Context context, String username) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = open(context);
        Cursor c = db.rawQuery("SELECT id, order_code, username, receiver_name, receiver_phone, receiver_email, receiver_address, "
                + "shipping_method, payment_method, note, subtotal, shipping_fee, discount, total, status, created_at "
                + "FROM orders WHERE username = ? ORDER BY created_at DESC", new String[]{username});
        while (c.moveToNext()) {
            Order order = new Order(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getDouble(10),
                    c.getDouble(11),
                    c.getDouble(12),
                    c.getDouble(13),
                    c.getString(14),
                    c.getLong(15)
            );
            list.add(order);
        }
        c.close();

        for (Order o : list) {
            List<OrderItem> items = new ArrayList<>();
            Cursor ic = db.rawQuery("SELECT id, order_id, product_id, product_name, product_price, quantity, product_image "
                    + "FROM order_items WHERE order_id = ?", new String[]{String.valueOf(o.getId())});
            while (ic.moveToNext()) {
                items.add(new OrderItem(
                        ic.getInt(0),
                        ic.getInt(1),
                        ic.getInt(2),
                        ic.getString(3),
                        ic.getDouble(4),
                        ic.getInt(5),
                        ic.getString(6)
                ));
            }
            ic.close();
            o.setItems(items);
        }
        return list;
    }

    public static void createOrder(Context context, String username, double total) {
        Order order = new Order();
        order.setOrderCode("DH" + System.currentTimeMillis() % 1000000);
        order.setUsername(username);
        order.setReceiverName(Session.getReceiver(context));
        order.setReceiverPhone(Session.getPhone(context));
        order.setReceiverEmail(Session.getEmail(context));
        order.setReceiverAddress(Session.getFullAddress(context));
        order.setShippingMethod("Giao hàng tiêu chuẩn");
        order.setPaymentMethod("Thanh toán khi nhận hàng (COD)");
        order.setSubtotal(total);
        order.setShippingFee(Constants.SHIPPING_FEE);
        order.setDiscount(0);
        order.setTotal(total);
        order.setStatus("Đã đặt hàng");
        order.setCreatedAt(System.currentTimeMillis());
        createOrderFull(context, order, getCartItems(context));
    }

    public static String getProductCategory(Context context, int productId) {
        SQLiteDatabase db = open(context);
        Cursor c = db.rawQuery("SELECT category FROM products WHERE id = ?",
                new String[]{String.valueOf(productId)});
        String category = null;
        if (c.moveToFirst()) {
            category = c.getString(0);
        }
        c.close();
        return category;
    }

    public static List<Product> getSimilarProducts(Context context, int productId, String category) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = open(context);
        if (category == null || category.isEmpty()) {
            category = getProductCategory(context, productId);
        }

        Cursor c = db.rawQuery(
                "SELECT id, name, price, description, image, category FROM products "
                        + "WHERE category = ? AND id != ? LIMIT 6",
                new String[]{category != null ? category : "", String.valueOf(productId)});
        while (c.moveToNext()) {
            list.add(readProduct(context, c));
        }
        c.close();

        if (list.size() < 4) {
            int needed = 4 - list.size();
            Cursor c2 = db.rawQuery(
                    "SELECT id, name, price, description, image, category FROM products "
                            + "WHERE id != ? AND (category != ? OR category IS NULL) LIMIT ?",
                    new String[]{String.valueOf(productId), category != null ? category : "", String.valueOf(needed)});
            while (c2.moveToNext()) {
                list.add(readProduct(context, c2));
            }
            c2.close();
        }
        return list;
    }

    public static List<Product> getRecommendedAccessories(Context context, int productId) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = open(context);
        String currentCategory = getProductCategory(context, productId);

        Cursor c;
        if (Constants.CATEGORY_ACCESSORY.equals(currentCategory) || Constants.CATEGORY_OTHER.equals(currentCategory)) {
            c = db.rawQuery(
                    "SELECT id, name, price, description, image, category FROM products "
                            + "WHERE category IN (?, ?) AND id != ? LIMIT 6",
                    new String[]{Constants.CATEGORY_SHIRT, Constants.CATEGORY_SHOES, String.valueOf(productId)});
        } else {
            c = db.rawQuery(
                    "SELECT id, name, price, description, image, category FROM products "
                            + "WHERE category IN (?, ?) AND id != ? LIMIT 6",
                    new String[]{Constants.CATEGORY_ACCESSORY, Constants.CATEGORY_OTHER, String.valueOf(productId)});
        }

        while (c.moveToNext()) {
            list.add(readProduct(context, c));
        }
        c.close();
        return list;
    }

    public static List<Product> getProductsByFilter(Context context, String categoryId, String keyword,
                                                    double minPrice, double maxPrice) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = open(context);
        getInstance(context).seedProducts(db);

        StringBuilder query = new StringBuilder("SELECT id, name, price, description, image, category FROM products WHERE 1=1 ");
        List<String> args = new ArrayList<>();

        if (categoryId != null && !categoryId.isEmpty() && !"ALL".equalsIgnoreCase(categoryId)) {
            if ("AO_THUN".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? AND (name LIKE '%thun%' OR name LIKE '%polo%' OR name LIKE '%ba lỗ%') ");
                args.add(Constants.CATEGORY_SHIRT);
            } else if ("AO_SO_MI".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? AND name LIKE '%sơ mi%' ");
                args.add(Constants.CATEGORY_SHIRT);
            } else if ("AO_KHOAC".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? AND (name LIKE '%khoác%' OR name LIKE '%hoodie%' OR name LIKE '%bomber%') ");
                args.add(Constants.CATEGORY_SHIRT);
            } else if ("QUAN_JEANS".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? AND name LIKE '%jean%' ");
                args.add(Constants.CATEGORY_PANTS);
            } else if ("QUAN_DAI".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? AND (name LIKE '%tây%' OR name LIKE '%jogger%' OR name LIKE '%chinos%' OR name LIKE '%kaki%') AND name NOT LIKE '%short%' ");
                args.add(Constants.CATEGORY_PANTS);
            } else if ("QUAN_SHORT".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? AND name LIKE '%short%' ");
                args.add(Constants.CATEGORY_PANTS);
            } else if ("PHU_KIEN".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? ");
                args.add(Constants.CATEGORY_ACCESSORY);
            } else if ("GIAY_DEP".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? ");
                args.add(Constants.CATEGORY_SHOES);
            } else if ("DO_DUNG".equalsIgnoreCase(categoryId)) {
                query.append("AND category = ? ");
                args.add(Constants.CATEGORY_OTHER);
            } else {
                query.append("AND category = ? ");
                args.add(categoryId);
            }
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.append("AND (name LIKE ? OR description LIKE ? OR category LIKE ?) ");
            String k = "%" + keyword.trim() + "%";
            args.add(k);
            args.add(k);
            args.add(k);
        }

        if (minPrice > 0) {
            query.append("AND price >= ? ");
            args.add(String.valueOf(minPrice));
        }

        if (maxPrice < Double.MAX_VALUE && maxPrice > 0) {
            query.append("AND price <= ? ");
            args.add(String.valueOf(maxPrice));
        }

        query.append("ORDER BY id ASC");

        Cursor c = db.rawQuery(query.toString(), args.toArray(new String[0]));
        while (c.moveToNext()) {
            list.add(readProduct(context, c));
        }
        c.close();
        return list;
    }
}
