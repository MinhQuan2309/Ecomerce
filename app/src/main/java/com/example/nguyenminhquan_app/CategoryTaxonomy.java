package com.example.nguyenminhquan_app;

public final class CategoryTaxonomy {

    public static class SubCategoryItem {
        private final String name;
        private final String imageAsset;
        private final int iconRes;
        private final String filterKeyword;

        public SubCategoryItem(String name, String imageAsset, String filterKeyword) {
            this(name, imageAsset, 0, filterKeyword);
        }

        public SubCategoryItem(String name, int iconRes, String filterKeyword) {
            this(name, null, iconRes, filterKeyword);
        }

        public SubCategoryItem(String name, String imageAsset, int iconRes, String filterKeyword) {
            this.name = name;
            this.imageAsset = imageAsset;
            this.iconRes = iconRes;
            this.filterKeyword = filterKeyword;
        }

        public String getName() { return name; }
        public String getImageAsset() { return imageAsset; }
        public int getIconRes() { return iconRes; }
        public String getFilterKeyword() { return filterKeyword; }
    }

    public static class FilterItem {
        private final String label;
        private final String keyword;
        private final double minPrice;
        private final double maxPrice;

        public FilterItem(String label, String keyword) {
            this(label, keyword, 0, Double.MAX_VALUE);
        }

        public FilterItem(String label, double minPrice, double maxPrice) {
            this(label, null, minPrice, maxPrice);
        }

        public FilterItem(String label, String keyword, double minPrice, double maxPrice) {
            this.label = label;
            this.keyword = keyword;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }

        public String getLabel() { return label; }
        public String getKeyword() { return keyword; }
        public double getMinPrice() { return minPrice; }
        public double getMaxPrice() { return maxPrice; }
    }

    public static class MainCategory {
        private final String id;
        private final String name;
        private final String imageAsset;
        private final int iconRes;
        private final String title;
        private final String subtitle;
        private final SubCategoryItem[] subCategories;
        private final FilterItem[] purposes;
        private final FilterItem[] materials;
        private final FilterItem[] priceRanges;

        public MainCategory(String id, String name, String imageAsset, int iconRes,
                            String title, String subtitle,
                            SubCategoryItem[] subCategories,
                            FilterItem[] purposes,
                            FilterItem[] materials,
                            FilterItem[] priceRanges) {
            this.id = id;
            this.name = name;
            this.imageAsset = imageAsset;
            this.iconRes = iconRes;
            this.title = title;
            this.subtitle = subtitle;
            this.subCategories = subCategories;
            this.purposes = purposes;
            this.materials = materials;
            this.priceRanges = priceRanges;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getImageAsset() { return imageAsset; }
        public int getIconRes() { return iconRes; }
        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public SubCategoryItem[] getSubCategories() { return subCategories; }
        public FilterItem[] getPurposes() { return purposes; }
        public FilterItem[] getMaterials() { return materials; }
        public FilterItem[] getPriceRanges() { return priceRanges; }
    }

    public static final MainCategory[] CATEGORIES = new MainCategory[]{
            new MainCategory(
                    "ALL",
                    "Tất cả",
                    null,
                    R.drawable.ic_tab_grid,
                    "Bộ Sưu Tập Thời Trang",
                    "Khám phá toàn bộ danh mục trang phục & phụ kiện",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Áo Thun", "ao_thun.jpg", "thun"),
                            new SubCategoryItem("Áo Sơ Mi", "ao_so_mi.jpg", "sơ mi"),
                            new SubCategoryItem("Áo Khoác", "ao_khoac_gio.jpg", "khoác"),
                            new SubCategoryItem("Quần Jeans", "quan_jean.jpg", "jean"),
                            new SubCategoryItem("Quần Dài & Tây", "quan_jean.jpg", "quần"),
                            new SubCategoryItem("Quần Short", "quan_short.jpg", "short"),
                            new SubCategoryItem("Giày Dép", "giay_sneaker.jpg", "giày"),
                            new SubCategoryItem("Phụ Kiện", "mu_luoi_trai.jpg", "balo"),
                            new SubCategoryItem("Đồ Dùng", "binh_giu_nhiet.jpg", "bình")
                    },
                    new FilterItem[]{
                            new FilterItem("Đi Làm / Công Sở", "công sở"),
                            new FilterItem("Thể Thao / Vận Động", "thể thao"),
                            new FilterItem("Dạo Phố / Hằng Ngày", "hằng ngày"),
                            new FilterItem("Du Lịch / Dã Ngoại", "du lịch")
                    },
                    new FilterItem[]{
                            new FilterItem("Cotton 100% Tự Nhiên", "cotton"),
                            new FilterItem("Sợi Tre Kháng Khuẩn", "tre"),
                            new FilterItem("Denim Bền Đẹp Co Giãn", "denim"),
                            new FilterItem("Vải Dù Gió Chống Thấm", "gió")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 200.000 đ", 0, 200000),
                            new FilterItem("200k - 500k", 200000, 500000),
                            new FilterItem("Trên 500.000 đ", 500000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "AO_THUN",
                    "Áo Thun",
                    "ao_thun.jpg",
                    0,
                    "Danh Mục Áo Thun",
                    "Đầy đủ phom dáng cổ tròn, polo, oversize, dệt kim và thể thao",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Thun Cổ Tròn", "ao_thun.jpg", "cổ tròn"),
                            new SubCategoryItem("Thun Cổ Polo", "ao_thun.jpg", "polo"),
                            new SubCategoryItem("Thun Tay Dài", "ao_thun.jpg", "tay dài"),
                            new SubCategoryItem("Thun Ba Lỗ (Tanktop)", "ao_thun.jpg", "ba lỗ"),
                            new SubCategoryItem("Thun Form Rộng (Oversize)", "ao_thun.jpg", "form rộng"),
                            new SubCategoryItem("Thun Dệt Kim Cao Cấp", "ao_thun.jpg", "dệt kim"),
                            new SubCategoryItem("Thun Thể Thao Pro Dry", "ao_thun.jpg", "dry")
                    },
                    new FilterItem[]{
                            new FilterItem("Mặc Hằng Ngày (Cơ Bản)", "hằng ngày"),
                            new FilterItem("Đi Làm / Văn Phòng", "văn phòng"),
                            new FilterItem("Thể Thao / Tập Gym", "thể thao"),
                            new FilterItem("Dạo Phố / Đi Chơi", "dạo phố"),
                            new FilterItem("Du Lịch / Dã Ngoại", "du lịch")
                    },
                    new FilterItem[]{
                            new FilterItem("Cotton 100% Compact Tự Nhiên", "cotton"),
                            new FilterItem("Sợi Tre Kháng Khuẩn Thoáng Mát", "tre"),
                            new FilterItem("Thun Lạnh Co Giãn 4 Chiều", "thun lạnh"),
                            new FilterItem("Vải Dệt Nổi Pique Mềm Rủ", "dệt")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 150.000 đ", 0, 150000),
                            new FilterItem("150k - 300k", 150000, 300000),
                            new FilterItem("Trên 300.000 đ", 300000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "AO_SO_MI",
                    "Áo Sơ Mi",
                    "ao_so_mi.jpg",
                    0,
                    "Danh Mục Áo Sơ Mi",
                    "Lịch lãm, trẻ trung cho phong cách công sở và thường nhật",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Sơ Mi Tay Ngắn", "ao_so_mi.jpg", "ngắn tay"),
                            new SubCategoryItem("Sơ Mi Tay Dài", "ao_so_mi.jpg", "tay dài"),
                            new SubCategoryItem("Sơ Mi Cổ Bẻ Oxford", "ao_so_mi.jpg", "oxford"),
                            new SubCategoryItem("Sơ Mi Cổ Trụ (Cổ Tàu)", "ao_so_mi.jpg", "cổ trụ"),
                            new SubCategoryItem("Sơ Mi Kẻ Sọc / Caro", "ao_so_mi.jpg", "kẻ"),
                            new SubCategoryItem("Sơ Mi Kháng Khuẩn Không Nhăn", "ao_so_mi.jpg", "kháng khuẩn")
                    },
                    new FilterItem[]{
                            new FilterItem("Công Sở / Văn Phòng", "công sở"),
                            new FilterItem("Hội Họp / Sự Kiện Trang Trọng", "sự kiện"),
                            new FilterItem("Dạo Phố / Hẹn Hò Cuối Tuần", "dạo phố"),
                            new FilterItem("Phong Cách Trẻ Trung Năng Động", "trẻ trung")
                    },
                    new FilterItem[]{
                            new FilterItem("Lụa Pha Mềm Mại Ít Nhăn", "lụa"),
                            new FilterItem("Sợi Tre Kháng Khuẩn Chống Mùi", "kháng khuẩn"),
                            new FilterItem("Cotton Oxford Dày Dặn", "cotton"),
                            new FilterItem("Vải Dệt Dobby Cao Cấp", "dobby")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 250.000 đ", 0, 250000),
                            new FilterItem("250k - 400k", 250000, 400000),
                            new FilterItem("Trên 400.000 đ", 400000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "AO_KHOAC",
                    "Áo Khoác",
                    "ao_khoac_gio.jpg",
                    0,
                    "Danh Mục Áo Khoác",
                    "Đa năng chắn gió, chống nắng, trượt nước và giữ ấm",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Áo Khoác Gió / Dù", "ao_khoac_gio.jpg", "gió"),
                            new SubCategoryItem("Áo Khoác Hoodie Nỉ", "ao_khoac_gio.jpg", "hoodie"),
                            new SubCategoryItem("Áo Khoác Bomber Cổ Bo", "ao_khoac_gio.jpg", "bomber"),
                            new SubCategoryItem("Áo Khoác Blazer / Vest", "ao_khoac_gio.jpg", "blazer"),
                            new SubCategoryItem("Áo Khoác Cardigan", "ao_khoac_gio.jpg", "cardigan"),
                            new SubCategoryItem("Áo Khoác Dù Phối Mảng", "ao_khoac_gio.jpg", "phối mảng")
                    },
                    new FilterItem[]{
                            new FilterItem("Chắn Gió & Chống Mưa Phùn", "chắn gió"),
                            new FilterItem("Chống Nắng & Tia Cực Tím", "chống nắng"),
                            new FilterItem("Giữ Ấm Mùa Lạnh", "giữ ấm"),
                            new FilterItem("Thời Trang Đường Phố / Phượt", "streetwear")
                    },
                    new FilterItem[]{
                            new FilterItem("Vải Dù Gió 2 Lớp Trượt Nước", "gió"),
                            new FilterItem("Lớp Lót Lưới Thoáng Khí", "lưới"),
                            new FilterItem("Nỉ Chân Cua Cotton Dày Dặn", "nỉ"),
                            new FilterItem("Vải Dệt Twill Cao Cấp", "twill")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 350.000 đ", 0, 350000),
                            new FilterItem("350k - 550k", 350000, 550000),
                            new FilterItem("Trên 550.000 đ", 550000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "QUAN_JEANS",
                    "Quần Jeans",
                    "quan_jean.jpg",
                    0,
                    "Danh Mục Quần Jeans",
                    "Chất denim chuẩn màu, co giãn thoải mái khi vận động",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Jeans Ống Suông", "quan_jean.jpg", "ống suông"),
                            new SubCategoryItem("Jeans Dáng Ôm (Slimfit)", "quan_jean.jpg", "slimfit"),
                            new SubCategoryItem("Jeans Ống Rộng (Baggy)", "quan_jean.jpg", "ống rộng"),
                            new SubCategoryItem("Jeans Rách Cá Tính", "quan_jean.jpg", "rách"),
                            new SubCategoryItem("Jeans Xanh Wash Cổ Điển", "quan_jean.jpg", "xanh"),
                            new SubCategoryItem("Jeans Đen Tuyền (Tek Black)", "quan_jean.jpg", "đen")
                    },
                    new FilterItem[]{
                            new FilterItem("Đi Học & Đi Làm Hằng Ngày", "đi làm"),
                            new FilterItem("Dạo Phố Cuối Tuần", "dạo phố"),
                            new FilterItem("Streetwear Bụi Bặm", "bụi bặm"),
                            new FilterItem("Phối Đồ Đa Phong Cách", "đa năng")
                    },
                    new FilterItem[]{
                            new FilterItem("Denim Cotton Co Giãn 4C", "denim"),
                            new FilterItem("Denim Dệt Chéo 12oz Bền Bỉ", "dệt chéo"),
                            new FilterItem("Vải Xử Lý Wash Bền Màu", "wash")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 300.000 đ", 0, 300000),
                            new FilterItem("300k - 450k", 300000, 450000),
                            new FilterItem("Trên 450.000 đ", 450000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "QUAN_DAI",
                    "Quần Dài & Tây",
                    "quan_jean.jpg",
                    0,
                    "Danh Mục Quần Dài & Quần Tây",
                    "Lịch lãm, đứng phom cho công sở, học tập và sự kiện",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Quần Tây Âu Công Sở", "quan_jean.jpg", "quần tây"),
                            new SubCategoryItem("Quần Kaki Chinos", "quan_jean.jpg", "kaki"),
                            new SubCategoryItem("Quần Jogger Ống Bo", "quan_jean.jpg", "jogger"),
                            new SubCategoryItem("Quần Túi Hộp (Cargo Pants)", "quan_jean.jpg", "cargo"),
                            new SubCategoryItem("Quần Ống Suông Rộng", "quan_jean.jpg", "suông")
                    },
                    new FilterItem[]{
                            new FilterItem("Môi Trường Công Sở / Trang Trọng", "công sở"),
                            new FilterItem("Học Sinh / Sinh Viên Đi Học", "đi học"),
                            new FilterItem("Vận Động Năng Động Hằng Ngày", "năng động")
                    },
                    new FilterItem[]{
                            new FilterItem("Vải Tuyết Mưa Đứng Dáng", "tuyết mưa"),
                            new FilterItem("Kaki Cotton Mềm Co Giãn", "kaki"),
                            new FilterItem("Poly Rayon Chống Nhăn", "rayon")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 250.000 đ", 0, 250000),
                            new FilterItem("250k - 400k", 250000, 400000),
                            new FilterItem("Trên 400.000 đ", 400000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "QUAN_SHORT",
                    "Quần Short",
                    "quan_short.jpg",
                    0,
                    "Danh Mục Quần Short",
                    "Mát mẻ, tiện lợi cho mùa hè và hoạt động thể thao",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Short Kaki Lưng Thun", "quan_short.jpg", "kaki"),
                            new SubCategoryItem("Short Thể Thao Pro", "quan_short.jpg", "thể thao"),
                            new SubCategoryItem("Short Jean Denim", "quan_short.jpg", "jean"),
                            new SubCategoryItem("Short Nỉ Cotton", "quan_short.jpg", "nỉ"),
                            new SubCategoryItem("Short Túi Hộp (Cargo)", "quan_short.jpg", "túi hộp")
                    },
                    new FilterItem[]{
                            new FilterItem("Mặc Nhà Thoải Mái", "ở nhà"),
                            new FilterItem("Chạy Bộ, Tập Gym, Thể Thao", "thể thao"),
                            new FilterItem("Dạo Phố & Đi Biển Mùa Hè", "dạo phố")
                    },
                    new FilterItem[]{
                            new FilterItem("Kaki Sợi Tự Nhiên Thoáng Khí", "kaki"),
                            new FilterItem("Vải Dù Gió Siêu Nhẹ Khô Nhanh", "dù"),
                            new FilterItem("Thun Cotton Co Giãn 4 Chiều", "cotton")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 180.000 đ", 0, 180000),
                            new FilterItem("180k - 250k", 180000, 250000),
                            new FilterItem("Trên 250.000 đ", 250000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "PHU_KIEN",
                    "Phụ Kiện",
                    "mu_luoi_trai.jpg",
                    0,
                    "Danh Mục Phụ Kiện Thời Trang",
                    "Balo, nón, kính mát, đồng hồ và phụ kiện công nghệ",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Nón Lưỡi Trai (Cap)", "mu_luoi_trai.jpg", "mũ"),
                            new SubCategoryItem("Nón Bucket (Tai Bèo)", "mu_luoi_trai.jpg", "nón"),
                            new SubCategoryItem("Balo Laptop Chống Sốc", "balo_laptop.jpg", "balo"),
                            new SubCategoryItem("Túi Đeo Chéo (Crossbody)", "balo_laptop.jpg", "túi"),
                            new SubCategoryItem("Kính Mát Chống UV400", "kinh_ram.jpg", "kính"),
                            new SubCategoryItem("Đồng Hồ Thể Thao Chống Nước", "dong_ho.jpg", "đồng hồ"),
                            new SubCategoryItem("Tai Nghe Bluetooth Không Dây", "tai_nghe.jpg", "tai nghe")
                    },
                    new FilterItem[]{
                            new FilterItem("Đi Học & Công Sở Hằng Ngày", "học"),
                            new FilterItem("Du Lịch, Dã Ngoại, Đi Phượt", "du lịch"),
                            new FilterItem("Thể Thao & Vận Động Ngoài Trời", "thể thao")
                    },
                    new FilterItem[]{
                            new FilterItem("Chống Nước 5ATM Tiêu Chuẩn", "chống nước"),
                            new FilterItem("Tròng Phân Cực Chống Tia UV400", "uv400"),
                            new FilterItem("Vải Oxford Phủ Trượt Nước", "oxford")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 200.000 đ", 0, 200000),
                            new FilterItem("200k - 600k", 200000, 600000),
                            new FilterItem("Trên 600.000 đ", 600000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "GIAY_DEP",
                    "Giày Dép",
                    "giay_sneaker.jpg",
                    0,
                    "Danh Mục Giày Dép",
                    "Đệm êm ái, bám tốt, thoải mái đi lại cả ngày",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Sneaker Cổ Thấp (Low-top)", "giay_sneaker.jpg", "sneaker"),
                            new SubCategoryItem("Giày Chạy Bộ Sport Running", "giay_chay_bo.jpg", "chạy bộ"),
                            new SubCategoryItem("Sneaker Cổ Cao (High-top)", "giay_sneaker.jpg", "cổ cao"),
                            new SubCategoryItem("Giày Lười (Slip-on / Loafer)", "giay_sneaker.jpg", "lười"),
                            new SubCategoryItem("Dép Quai Ngang & Sandal", "giay_sneaker.jpg", "dép")
                    },
                    new FilterItem[]{
                            new FilterItem("Đi Học, Đi Làm, Dạo Phố", "học"),
                            new FilterItem("Chạy Bộ, Tập Gym, Thể Thao", "chạy bộ"),
                            new FilterItem("Phối Đồ Streetwear Năng Động", "streetwear")
                    },
                    new FilterItem[]{
                            new FilterItem("Lưới Dệt Mesh Thoáng Khí", "lưới"),
                            new FilterItem("Đế Cao Su Non Đàn Hồi Bám Sàn", "cao su"),
                            new FilterItem("Đệm Bọt Khí Êm Ái Chống Mỏi", "đệm")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 500.000 đ", 0, 500000),
                            new FilterItem("500k - 800k", 500000, 800000),
                            new FilterItem("Trên 800.000 đ", 800000, Double.MAX_VALUE)
                    }
            ),
            new MainCategory(
                    "DO_DUNG",
                    "Đồ Dùng",
                    "binh_giu_nhiet.jpg",
                    0,
                    "Danh Mục Tiện Ích Đời Sống",
                    "Vật dụng văn phòng, cá nhân thiết yếu",
                    new SubCategoryItem[]{
                            new SubCategoryItem("Bình Giữ Nhiệt Inox 304", "binh_giu_nhiet.jpg", "bình"),
                            new SubCategoryItem("Sổ Tay Bìa Da Cao Cấp", "so_tay.jpg", "sổ"),
                            new SubCategoryItem("Bút Ký Kim Loại Sang Trọng", "so_tay.jpg", "bút")
                    },
                    new FilterItem[]{
                            new FilterItem("Văn Phòng & Bàn Làm Việc", "văn phòng"),
                            new FilterItem("Du Lịch, Dã Ngoại & Cắm Trại", "du lịch"),
                            new FilterItem("Quà Tặng Cho Bạn Bè & Đồng Nghiệp", "quà")
                    },
                    new FilterItem[]{
                            new FilterItem("Inox 304 Chuẩn Thực Phẩm", "inox"),
                            new FilterItem("Bìa Da PU Cao Cấp Mềm Mại", "da"),
                            new FilterItem("Giấy Chống Lóa Không Thấm Mực", "giấy")
                    },
                    new FilterItem[]{
                            new FilterItem("Dưới 100.000 đ", 0, 100000),
                            new FilterItem("100k - 250k", 100000, 250000),
                            new FilterItem("Trên 250.000 đ", 250000, Double.MAX_VALUE)
                    }
            )
    };

    public static MainCategory findCategory(int index) {
        if (index >= 0 && index < CATEGORIES.length) {
            return CATEGORIES[index];
        }
        return CATEGORIES[0];
    }

    private CategoryTaxonomy() {
    }
}
