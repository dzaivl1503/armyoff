# ⚔️ Hướng Dẫn Cài Đặt & Chạy Server Mobi Army 2 Trên VPS

Thư mục `army2_server/` là bộ mã nguồn Server độc lập hoàn chỉnh, hỗ trợ quản lý tài khoản, cộng/trừ Xu & Lượng, chỉnh sửa nhiệm vụ 12 loại Gun trực tiếp qua Web Admin Dashboard và lưu trữ cơ sở dữ liệu SQL.

---

## 📁 Cấu Trúc Thư Mục

```
army2_server/
├── config/
│   └── server.properties       # Cấu hình Port, Mật khẩu Admin, Quà tân thủ, Kết nối DB
├── sql/
│   └── schema.sql              # CSDL SQL chứa 82 cấp độ nhiệm vụ và bảng tài khoản
├── src/
│   └── Army2Server.java        # Mã nguồn Server Java (Tích hợp Web Admin Dashboard)
├── data/                       # Chứa file cơ sở dữ liệu và bản lưu của người chơi
├── start.bat                   # File 1-click chạy trên Windows Server
├── start.sh                    # File chạy trên Linux VPS (Ubuntu/Debian/CentOS)
└── README.md                   # Tài liệu hướng dẫn này
```

---

## 🚀 1. Chạy Nhanh Trên Máy Windows Hoặc Windows VPS

1. Mở thư mục `army2_server/`.
2. Chạy file **`start.bat`**.
3. Server sẽ tự động biên dịch và chạy tại: `http://localhost:8080`.
4. Mở trình duyệt vào trang quản trị: **`http://localhost:8080/admin`**
   - Mật khẩu Admin mặc định: `admin123` (Có thể đổi trong `config/server.properties`).

---

## 🐧 2. Hướng Dẫn Triển Khai Trên Linux VPS (Ubuntu / Debian)

### Bước 1: Cài đặt Java trên VPS (nếu chưa có)
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk-headless
```

### Bước 2: Upload thư mục `army2_server` lên VPS
Bạn có thể dùng phần mềm **WinSCP**, **FileZilla** hoặc lệnh `scp` để tải thư mục `army2_server` lên VPS (ví dụ vào `/root/army2_server`).

### Bước 3: Phân quyền và khởi chạy
```bash
cd /root/army2_server
chmod +x start.sh
./start.sh
```

### Bước 4: Chạy ngầm 24/7 với `nohup` hoặc `screen` (Tùy chọn)
Để server luôn chạy khi tắt cửa sổ SSH:
```bash
nohup ./start.sh > server.log 2>&1 &
```
Hoặc dùng `screen`:
```bash
screen -S army2
./start.sh
# Bấm Ctrl + A rồi bấm D để thoát ra ngoài mà server vẫn chạy
```

---

## 🌐 3. Mở Port Tường Lửa Trên VPS
Đảm bảo VPS đã mở port `8080` (hoặc port bạn cấu hình):
```bash
sudo ufw allow 8080/tcp
sudo ufw reload
```

---

## 👑 4. Các Tính Năng Trong Trang Quản Trị Web (`/admin`)

Mở trình duyệt truy cập: `http://<IP-VPS-CỦA-BẠN>:8080/admin`

1. **Quản lý Tài Khoản & Xu / Lượng**:
   - Xem toàn bộ danh sách người chơi đã đăng ký.
   - Bấm nút **"Cộng/Sửa Tiền"** để nhập số **Xu** và **Lượng** mới cho bất kỳ người chơi nào.
   - Bấm nút **"Khóa" / "Mở Khóa"** tài khoản người chơi ngay lập tức.
2. **Quản lý 82 Cấp Độ Nhiệm Vụ (12 Loại Gun)**:
   - Hiển thị danh sách tất cả nhiệm vụ của 12 loại Gun: *Gunner, Miss 6, Electician, King Kong, Rocketer, Granos, Chicky, Tarzan, Apache, Magenta, Draby, Cow Girl*.
   - Bấm nút **"Sửa"** để chỉnh: Số lượng yêu cầu (5/50/500), Thưởng Xu, Thưởng Lượng, Thưởng EXP.
   - Tất cả thay đổi được lưu ngay vào CSDL và người chơi khi đăng nhập sẽ tự động áp dụng.
3. **Cài Đặt Sự Kiện Server**:
   - Bật sự kiện **x2, x3 EXP**.
   - Cài đặt Xu và Lượng khởi tạo cho người mới đăng ký.

---

## 🎮 5. Kết Nối Game Client Tới VPS Của Bạn

1. Mở game Army 2 Offline.
2. Tại Menu chính, chọn **"ĐĂNG NHẬP / CLOUD"** -> Chọn **"Menu"** -> **"Cài Đặt Máy Chủ (Server URL)"**.
3. Nhập địa chỉ VPS của bạn:
   `http://<IP-VPS-CỦA-BẠN>:8080/api`
4. Bấm **"Đăng Ký"** hoặc **"Đăng Nhập"** để bắt đầu chơi!
