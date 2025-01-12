# Stock Management System 🚀

Bu proje, **Spring Boot** tabanlı bir **Stok Yönetim Sistemi**'ni geliştirmek için oluşturulmuştur. Sistem, **ürünlerin ve kullanıcıların takibi**, **stok yönetimi**, ve **satış işlemleri** gibi temel işlevleri içerir. 📦📊

## 📋 Özellikler

- **Stock CRUD Operasyonları**: Ürün ve kullanıcı ekleme, güncelleme, silme ve listeleme işlemleri yapılabilir. 🛠️
- **Stok Satışı**: Ürünler satılabilir ve stok seviyeleri otomatik olarak güncellenir. 💸
- **Sepet Yönetimi**: Ürünler sepete eklenebilir ve sepetteki ürünler onaylanarak satışa dönüştürülebilir. 🛒
- **REST API**: Tüm işlemler REST API üzerinden yapılır, böylece sistem kolayca başka platformlarla entegre edilebilir. 🌐
- **Veritabanı Şeması**: Veritabanı, `schema.sql` ile otomatik olarak oluşturulur ve başlatılır. API üzerinden yüklenen test verileri ile sistem başlatılır. 🗄️
- **Kullanıcı Yönetimi**: Güvenli kimlik doğrulama (authentication) ve yetkilendirme (authorization) sağlar. Şifreler, **BCrypt** ile şifreli olarak veritabanında saklanır. 🔒
- **Dinamik Kullanıcı Rolleri**: Kullanıcılar farklı rollerle yetkilendirilebilir (Örn: ADMIN, USER). 👩‍💻👨‍💻

## 🔧 Kullanılan Teknolojiler

- **Backend**: Spring Boot 💻
  - Spring Data JPA (Veritabanı yönetimi) 🗂️
  - Spring Security (Güvenlik ve kullanıcı doğrulama) 🔑
  - Spring Web (REST API) 🌐
  - BCrypt (Şifreleme) 🔒
  - DTO Mapper (Veri transferi için DTO kullanımı) 📝
  
- **Frontend**: HTML, CSS, JavaScript (Bootstrap ile tasarım) 🎨
  - Modal popup'lar ve dinamik UI güncellemeleri 💡
  
- **Veritabanı**: MySQL (Schema.sql ile otomatik veritabanı yapılandırması) 🗃️
  
- **Diğer**:
  - Maven (Bağımlılık yönetimi) 📦
  - Bootstrap (UI için) 🎨

## 👤 Kullanıcı Bilgileri

Sistemde test amaçlı bir kullanıcı bulunmaktadır. Giriş yapmak için aşağıdaki bilgileri kullanabilirsiniz:

- **Kullanıcı Adı**: enes
- **Şifre**: 123

Bu kullanıcı ile sisteme giriş yaparak temel işlemleri test edebilirsiniz. ✔️

## 🛠️ Kurulum

### Ön Gereksinimler (Prerequisites)

- **JDK 11** veya daha yüksek bir sürüm ☕
- **Maven 3.x** 📦
- **Git** 🧳

![image](https://github.com/user-attachments/assets/db389eb9-d455-4d11-a956-148b3b810e06)
![image](https://github.com/user-attachments/assets/3123637b-897c-43b0-8c13-f8ffac69aca7)
![image](https://github.com/user-attachments/assets/c1cbd674-ae7c-49d2-afcb-a210c8f373a8)
![image](https://github.com/user-attachments/assets/94bd052b-5f7b-4ba8-be09-aaa7d32a018f)
![image](https://github.com/user-attachments/assets/544f0aaa-6ada-40ef-94cc-ab1d463f153e)
![image](https://github.com/user-attachments/assets/7662e395-eebe-4fab-ac1a-55108a4f5d3c)


### Adımlar

1. Reposunu klonlayın:
   ```bash
   git clone https://github.com/enescelebii/StockManagementAPP.git
   cd StockManagementAPP
   ```

2. Gerekli bağımlılıkları yükleyin:
   ```bash
   mvn clean install
   ```

3. Uygulamayı çalıştırın:
   ```bash
   mvn spring-boot:run
   ```

4. Uygulama, **`http://localhost:8080`** adresinde çalışacaktır. 🎉

## 💻 API Kullanımı

Aşağıdaki API uç noktaları ile uygulamanın işlevlerini kullanabilirsiniz:

## Stock API

- `POST /stocks/add` - Yeni stok ekler
- `PUT /stocks/{id}` - Var olan bir stoku günceller
- `PUT /stocks/{stockCode}/{quantity}` - Stok satışını gerçekleştirir
- `GET /stocks/{id}/image` - Stok ile ilişkili resmi getirir
- `POST /stocks/{id}/upload-image` - Stok resmini yükler
- `GET /stocks/page` - Sayfalı stok listesi alır
- `GET /stocks/list` - Tüm stokları listeler
- `GET /stocks/{id}` - Belirli bir stoku getirir
- `GET /stocks/search` - Stok araması yapar
- `DELETE /stocks/{id}` - Stoku siler

## User API

- `GET /users/list` - Kullanıcıları listeler
- `POST /users/add` - Yeni kullanıcı ekler
- `DELETE /users/{id}` - Kullanıcıyı siler
