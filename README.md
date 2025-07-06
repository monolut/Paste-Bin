# Pastebin Application

A simple Pastebin-like application built with **Spring Boot**.  
It allows users to create and retrieve text pastes via a REST API, storing files in S3-compatible storage and generating unique hashes.

---

## ✨ Features

- Create and retrieve pastes by unique URL hash
- Store files in **Cloudflare R2** or any S3-compatible storage
- Generate unique hashes with a multithreaded **HashCodeGenerator**
- Use **Redis** as a cache to help generate unique URL hashes efficiently

---

## 🛠 Technologies

- Java 17+
- Spring Boot
- Spring Data JPA
- Redis (only for URL hash generation)
- S3-compatible storage (Cloudflare R2)
- Maven

---

## 📂 Project Structure
```
src/main/java/org/ashirov/nicolai/pastebin
├── config
│ ├── S3Config.java # S3/Cloudflare R2 configuration
│ └── SpringConfig.java # Spring beans configuration
├── controller
│ └── PasteBinController.java # REST endpoints
├── dao
│ └── PasteDao.java # Persistence layer
├── model
│ ├── Data.java # Request/response DTO
│ └── Paste.java # Paste entity
├── service
│ ├── HashCodeGenerator.java # Unique hash generation logic
│ ├── RedisGenerateHash.java # Redis integration for hash generation
│ └── Scheduled.java # Optional scheduled tasks
└── PasteBinApplication.java # Main application class
```
---

## 🧩 Example Request/Response

### Create Paste (POST /paste)

**Request Body**
```json
{
  "content": "This is an example paste content.",
  "expires": "2025-07-07T15:30:00"
}
```
**Response**
```
a1b2c3d4 # Example of hash
```
---

### Build the project

git clone https://github.com/monolut/Paste-Bin.git
cd Paste-Bin

CREATE DATABASE pastebin;
CREATE USER pastebin_user WITH ENCRYPTED PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE pastebin TO pastebin_user;

Edit src/main/resources/application.properties



