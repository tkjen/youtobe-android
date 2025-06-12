# YouTube Clone Android App

Ứng dụng Android mô phỏng YouTube được xây dựng bằng Kotlin, sử dụng kiến trúc sạch và các thư viện hiện đại.

## Chức Năng Chính

### 1. Trang Chủ (Home)
- Xem video theo danh mục (All, Music, Gaming, Sports, News)
- Hiển thị video phổ biến
- Tìm kiếm video
- Xem thông tin kênh

### 2. Shorts
- Xem video ngắn
- Vuốt lên/xuốt để xem video tiếp theo
- Tải thêm video khi cuộn

### 3. Thư Viện (Library)
- Lưu trữ video đã xem
- Quản lý video yêu thích
- Vuốt để xóa video

### 4. Cài Đặt (Settings)
- Chuyển đổi giao diện (Sáng/Tối/Hệ thống)
- Quản lý tài khoản
- Cài đặt ứng dụng

### 5. Chi Tiết Video
- Xem video
- Xem thông tin video
- Xem thống kê kênh
- Tương tác với video

## Công Nghệ Sử Dụng

- **Ngôn ngữ**: Kotlin
- **Kiến trúc**: Clean Architecture + MVVM
- **Thư viện chính**:
  - Hilt: Tiêm phụ thuộc
  - Retrofit: Gọi API YouTube
  - Room: Lưu trữ local
  - Coroutines: Xử lý bất đồng bộ
  - Navigation: Điều hướng
  - ViewPager2: Chuyển đổi danh mục
  - Glide: Tải ảnh
  - Material Design: Giao diện

## Cấu Trúc Dự Án

```
app/
├── data/           # Tầng dữ liệu
│   ├── api/        # API YouTube
│   ├── local/      # Database local
│   ├── model/      # Model dữ liệu
│   └── repository/ # Repository
├── di/             # Dependency Injection
├── domain/         # Business Logic
├── ui/             # Giao diện
│   ├── home/       # Trang chủ
│   ├── shorts/     # Shorts
│   ├── libary/     # Thư viện
│   ├── settings/   # Cài đặt
│   └── video_details/ # Chi tiết video
└── utils/          # Tiện ích
```

## Cài Đặt

1. Clone repository
2. Thêm API key YouTube vào `local.properties`:
   ```
   YOUTUBE_API_KEY=your_api_key_here
   ```
3. Build và chạy ứng dụng

## Yêu Cầu

- Android Studio Arctic Fox trở lên
- JDK 11
- Android SDK 24+
- API key YouTube Data API v3

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- YouTube Data API v3
- Android Jetpack libraries
- Material Design components 