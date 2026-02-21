# 📚 Library Management System - Java Project

## 🌟 Project Overview

This project is a simple Library Management System developed using **Java Swing** and **MySQL/MariaDB** database. It allows users to manage books by adding, viewing, issuing, returning, and deleting records.

## 🛠️ Technologies Used

* Java (Swing GUI)
* JDBC Connectivity
* MySQL / MariaDB Database
* IntelliJ IDEA / Eclipse / NetBeans

## 📂 Project Features

✔ Add Book
✔ View Books
✔ Issue Book
✔ Return Book
✔ Delete Book
✔ Database Connectivity

## 💾 Database Setup

### 1. Install Database Server

Install and start:

* MySQL OR MariaDB server

### 2. Create Database

Run the following SQL command:

```sql
CREATE DATABASE library_db;
```

### 3. Create Table

```sql
USE library_db;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    status VARCHAR(50)
);
```

### 4. Import Database File (Optional)

If you have `library_db.sql`, import it using:

* phpMyAdmin → Import
  OR
* Terminal command:

```bash
mysql -u root -p library_db < library_db.sql
```

## ⚙️ JDBC Configuration

Update database connection in `Dashboard.java`:

```java
con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true",
    "root",
    "your_password"
);
```

⚠️ Note: Do not upload database password on public GitHub repositories.

## 🚀 How to Run Project

1. Install Java JDK
2. Install MySQL / MariaDB
3. Import project in IDE
4. Setup database
5. Run `Dashboard.java`

## 📌 Project Structure

```
LibraryProject/
│
├── src/
│   ├── Dashboard.java
│   └── Login.java
├── library_db.sql
├── README.md
```

## 🔐 Login Page Feature

The project also includes a **Login System** using Java Swing GUI and database authentication.

### Login Database Table

Create users table using:

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100)
);
```

👉 Login page verifies username and password from database before opening the dashboard.

## ❤️ Developer Information

* Language: Java
* Type: Mini Project
* Platform: Desktop Application

## ⭐ Future Improvements

* Login System
* Search Book Feature
* User Management
* Better UI Design

---

✨ Thank you for using Library Management System Project!
