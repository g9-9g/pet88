# 💻 Hướng dẫn cài đặt phần mềm  
**Nhóm 27 – Hệ thống quản lý trung tâm chăm sóc thú cưng**

📌 **Chi tiết về chương trình tại:** *https://github.com/g9-9g/pet88*

---

## 🧩 Hướng dẫn sử dụng

### 🔹 Bước 1: Cài đặt yêu cầu hệ thống

- Cài đặt [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Cài đặt [Node.js](https://nodejs.org/en)

Kiểm tra phiên bản:
```bash
node -v
npm -v
docker -v
```

---

### 🔹 Bước 2: Khởi chạy hệ thống

#### 📦 **Backend (API + MongoDB qua Docker)**

1. Mở terminal, chuyển đến thư mục backend:
```bash
cd ./pet-management-server/
```

2. Khởi chạy Docker:
```bash
docker-compose up --build
```

> ✅ Hệ thống backend sẽ chạy tại: `http://localhost:8080`

---

#### 🎨 **Frontend (Next.js)**

1. Mở terminal khác, chuyển đến thư mục frontend:
```bash
cd ./pet-management-client/
```

2. Tạo file cấu hình môi trường:
```bash
touch .env.local
```

3. Thêm dòng sau vào `.env.local` để cấu hình API endpoint:
```
NEXT_PUBLIC_API_ENDPOINT=http://localhost:8080
```

4. Cài đặt dependencies:
```bash
npm install
```

5. Build và chạy ứng dụng:
```bash
npm run build
npm run start
```

> ✅ Giao diện frontend sẽ chạy tại: `http://localhost:3000`

---

## 🔐 Danh sách tài khoản đăng nhập hệ thống

| Vai trò            | Username    | Password   |
|--------------------|-------------|------------|
| Chủ nuôi           | owner69     | 12345678   |
| Bác sĩ thú y       | vet69       | 12345678   |
| Nhân viên trung tâm| staff69     | 12345678   |
| Quản trị viên      | admin69     | 12345678   |

---

## 🛑 Để dừng hệ thống

- Dừng Docker:
```bash
Ctrl + C trong terminal đang chạy docker
```
Hoặc:
```bash
docker-compose down
```

- Dừng frontend:
```bash
Ctrl + C trong terminal đang chạy `npm run start`
```
