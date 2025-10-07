# Just-Musikk (JustSpotify) - Music Streaming Platform

Aplikasi streaming musik modern yang dibangun dengan Spring Boot, menyediakan layanan streaming musik dengan sistem autentikasi, pembayaran, dan manajemen konten yang lengkap.

## 🚀 Fitur Utama

### 🎵 Manajemen Musik
- **Upload Musik**: Admin dapat mengunggah file musik dengan metadata lengkap
- **Streaming**: Streaming musik langsung melalui Dropbox
- **Download**: Pengguna dapat mengunduh musik favorit
- **Pencarian**: Cari musik berdasarkan judul, artis, album, atau genre
- **Favorit**: Sistem favorit untuk koleksi musik pribadi

### 🔐 Sistem Autentikasi & Otorisasi
- **JWT Authentication**: Sistem login dengan token JWT
- **Role-based Access Control**: Dua tingkatan user (USER dan ADMIN)
- **Secure Endpoints**: Proteksi endpoint berdasarkan role pengguna

### 💳 Sistem Pembayaran
- **OTP Payment Verification**: Sistem pembayaran dengan verifikasi OTP via email
- **Premium Features**: Akses penuh untuk pengguna premium
- **Payment History**: Riwayat pembayaran pengguna

### 📧 Notifikasi Email
- **Email Service**: Notifikasi OTP dan konfirmasi pembayaran
- **Gmail SMTP**: Integrasi dengan Gmail untuk pengiriman email

### ☁️ Penyimpanan Cloud
- **Dropbox Integration**: Penyimpanan file musik di Dropbox
- **Secure File Access**: URL sementara untuk streaming dan download

## 🛠️ Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.1.0** - Framework utama
- **Spring Security** - Keamanan aplikasi
- **Spring Data JPA** - ORM dan database operations
- **JWT (JSON Web Token)** - Authentication tokens
- **MySQL** - Database management system

### External Services
- **Dropbox API** - Cloud file storage
- **Gmail SMTP** - Email service
- **Maven** - Dependency management

### Development Tools
- **Lombok** - Reduce boilerplate code
- **Spring Boot DevTools** - Development support
- **Docker** - Containerization

## 📋 Prerequisites

Sebelum menjalankan aplikasi ini, pastikan Anda memiliki:

- **Java 17** atau lebih tinggi
- **MySQL 8.0** atau kompatibel
- **Maven 3.6** atau lebih tinggi
- **Akun Dropbox** dengan API access token
- **Akun Gmail** untuk konfigurasi email

## ⚙️ Konfigurasi

### 1. Database Setup

Buat database MySQL baru:
```sql
CREATE DATABASE justspotify;
```

### 2. Environment Variables

Buat file `application.properties` atau set environment variables:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/justspotify?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# Dropbox Configuration
dropbox.access-token=your_dropbox_access_token

# JWT Configuration
musicplatform.app.jwtSecret=your_jwt_secret_key
musicplatform.app.jwtExpirationMs=86400000

# Email Configuration
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

### 3. Dropbox Setup

1. Buat akun Dropbox dan aplikasi di [Dropbox Developer Console](https://www.dropbox.com/developers)
2. Generate Access Token untuk aplikasi Anda
3. Set token tersebut di konfigurasi aplikasi

## 🚀 Cara Menjalankan

### Menggunakan Maven

```bash
# Clone repository
git clone <repository-url>
cd just-musikk

# Install dependencies
mvn clean install

# Jalankan aplikasi
mvn spring-boot:run

# Atau untuk development dengan auto-reload
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Menggunakan Docker

```bash
# Build dan jalankan dengan Docker Compose
docker-compose up --build

# Atau build image saja
docker build -t just-musikk:latest .
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

#### Login
```http
POST /api/auth/signin
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

### Song Management Endpoints

#### Get All Songs
```http
GET /api/songs
Authorization: Bearer <jwt_token>
```

#### Get Song by ID
```http
GET /api/songs/{id}
Authorization: Bearer <jwt_token>
```

#### Create Song (Admin Only)
```http
POST /api/songs
Authorization: Bearer <admin_jwt_token>
Content-Type: multipart/form-data

- title: string
- artist: string
- album: string (optional)
- genre: string (optional)
- duration: integer
- imageUrl: string (optional)
- audioFile: file
```

#### Update Song (Admin Only)
```http
PUT /api/songs/{id}
Authorization: Bearer <admin_jwt_token>
Content-Type: multipart/form-data

- title: string (optional)
- artist: string (optional)
- album: string (optional)
- genre: string (optional)
- duration: integer (optional)
- imageUrl: string (optional)
- audioFile: file (optional)
```

#### Delete Song (Admin Only)
```http
DELETE /api/songs/{id}
Authorization: Bearer <admin_jwt_token>
```

#### Search Songs
```http
GET /api/songs/search?q={query}
Authorization: Bearer <jwt_token>
```

#### Stream Song
```http
GET /api/songs/{id}/stream
Authorization: Bearer <jwt_token>
```

#### Download Song
```http
GET /api/songs/{id}/download
Authorization: Bearer <jwt_token>
```

### Payment Endpoints

#### Initiate Payment
```http
POST /api/payment/initiate
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "amount": 50000
}
```

#### Verify OTP Payment
```http
POST /api/payment/verify
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "otp": "123456"
}
```

## 🗄️ Struktur Database

### Tabel Users
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) UNIQUE NOT NULL,
  email VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(120) NOT NULL,
  is_paid BIT DEFAULT 0
);
```

### Tabel Roles
```sql
CREATE TABLE roles (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL
);
```

### Tabel Songs
```sql
CREATE TABLE songs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  artist VARCHAR(255) NOT NULL,
  album VARCHAR(255),
  genre VARCHAR(255),
  duration INT,
  file_path VARCHAR(255),
  image_url VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME
);
```

### Tabel Payments
```sql
CREATE TABLE payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  amount DOUBLE NOT NULL,
  status ENUM('PENDING', 'COMPLETED', 'FAILED', 'EXPIRED'),
  otp VARCHAR(255),
  otp_expiry DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Tabel User Favorites
```sql
CREATE TABLE user_favorites (
  user_id BIGINT,
  song_id BIGINT,
  PRIMARY KEY (user_id, song_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (song_id) REFERENCES songs(id)
);
```

## 🔒 Keamanan

- **Password Encryption**: Password dienkripsi menggunakan BCrypt
- **JWT Tokens**: Authentication menggunakan JWT dengan expiration time
- **Role-based Authorization**: Kontrol akses berdasarkan role pengguna
- **CORS Protection**: Konfigurasi CORS untuk mengamankan API endpoints
- **File Upload Security**: Validasi dan sanitasi file upload

## 📦 Deployment

### Production Deployment

1. **Environment Setup**:
   ```properties
   spring.profiles.active=prod
   spring.jpa.hibernate.ddl-auto=validate
   ```

2. **Database Migration**:
   - Gunakan Flyway atau Liquibase untuk database migration
   - Set `spring.jpa.hibernate.ddl-auto=validate` di production

3. **Security Headers**:
   ```properties
   server.error.include-message=never
   server.error.include-binding-errors=never
   ```

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/just-musikk-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# Integration tests
mvn verify
```

## 📈 Monitoring & Logging

- **Application Logs**: Konfigurasi logging level di `application.properties`
- **Database Queries**: SQL queries dapat dilihat dengan `spring.jpa.show-sql=true`
- **Performance Monitoring**: Gunakan Spring Boot Actuator untuk metrics

## 🤝 Contributing

1. Fork repository
2. Buat feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Contact

**Developer**: Asfari
**Email**: asfari160904@gmail.com

---

⭐ Jika Anda menyukai proyek ini, berikan star pada repository!