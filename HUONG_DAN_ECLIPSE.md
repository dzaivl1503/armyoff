# Hướng Dẫn Mở & Build Chạy Dự Án Army 2 Offline Trên Eclipse

Dự án **Army 2 Offline** đã được unpack, decompile toàn bộ mã nguồn Java, phục hồi tài nguyên đồ họa/âm thanh/bản đồ/dữ liệu RMS, và sửa toàn bộ lỗi cú pháp biên dịch để đạt **0 lỗi (Zero Errors)**. Dự án đã được cấu hình chuẩn Eclipse IDE với MicroEmulator tích hợp sẵn.

---

## 📁 Cấu Trúc Dự Án (`Downloads/armyoff/`)

```
armyoff/
├── .classpath                   # File cấu hình classpath của Eclipse
├── .project                     # File cấu hình dự án Eclipse Java Project
├── .settings/                   # Cấu hình compiler Java của Eclipse (Java 8+)
├── Army2Offline.launch          # Cấu hình Run Configuration chạy 1-click trong Eclipse
├── src/                         # Toàn bộ mã nguồn Java (.java) sạch lỗi 100%
│   ├── Launcher.java            # Class chạy chính (Main Entry Point)
│   ├── com/teamobi/mobiarmy2/   # GameMidlet & logic offline
│   ├── coreLG/                  # Canvas, CCanvas, Graphic engine
│   ├── screen/                  # Toàn bộ các màn hình giao diện
│   ├── player/                  # CPlayer, điều khiển nhân vật
│   ├── network/                 # Xử lý gói tin & MessageHandler
│   ├── item/                    # Bullet, vũ khí, đạn đạo
│   ├── effect/                  # Hiệu ứng nổ, khói, thời tiết
│   ├── map/                     # Bản đồ, Background, TileManager
│   └── CLib/                    # Thư viện hệ thống J2ME / SE
├── res/                         # Toàn bộ hình ảnh (.png), pack dữ liệu, âm thanh, font
├── rms/                         # Dữ liệu lưu offline (RMS)
├── bin/                         # Thư mục chứa file .class sau khi build
├── lib/                         # Thư viện MicroEmulator & MIDP 2.0 runtime
├── build.bat                    # Script biên dịch nhanh bằng dòng lệnh
├── run.bat                      # Script khởi chạy game 1-click
└── icon.png                     # Icon ứng dụng
```

---

## 🚀 Hướng Dẫn Mở Trên Eclipse

### Bước 1: Mở Eclipse và Import Dự Án
1. Khởi động **Eclipse IDE**.
2. Trên thanh menu, chọn: **File** -> **Open Projects from File System...** (hoặc **File** -> **Import...** -> **General** -> **Existing Projects into Workspace**).
3. Tại ô **Import source / Directory**, bấm **Directory...** và dẫn tới thư mục:
   ```
   C:\Users\Administrator\Downloads\armyoff
   ```
4. Đảm bảo ô `Army2Offline` đã được tích chọn.
5. Bấm **Finish**. Eclipse sẽ tự động nhận diện dự án, thư mục `src`, thư viện trong `lib/` và build tự động.

---

### Bước 2: Chạy Game Trên Eclipse

Có 2 cách cực kỳ tiện lợi:

#### Cách 1 (Nhanh nhất):
1. Trong cửa sổ **Package Explorer**, mở thư mục `src/`.
2. Chuột phải vào file `Launcher.java` -> chọn **Run As** -> **Java Application**.

#### Cách 2 (Dùng Run Configuration có sẵn):
1. Chuột phải vào project `Army2Offline` -> chọn **Run As** -> **Run Configurations...**
2. Chọn cấu hình **Army2Offline** đã được tạo sẵn -> Bấm **Run**.

Cửa sổ giả lập **MicroEmulator** sẽ hiện lên với game **Army 2 Offline** đầy đủ âm thanh, hình ảnh và giao diện!

---

## 🛠️ Biên Dịch & Chạy Bằng Command Line (Không Cần Mở Eclipse)

- **Để build lại toàn bộ**: Nhấp đúp vào `build.bat`
- **Để chạy game ngay**: Nhấp đúp vào `run.bat`
