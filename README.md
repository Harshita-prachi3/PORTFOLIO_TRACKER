
# PORTFOLIO_TRACKER

# 🧮 Simple Portfolio Tracker

## 🌟 Overview
The **Simple Portfolio Tracker** is a 🌐 web app that lets users 🧑‍💻 manage and 👀 monitor their 📊 stock portfolios. It offers features like ➕ adding, ✏️ editing, 🗑️ deleting stock holdings, viewing a 🖥️ dashboard with key 📈 metrics, and 📉 tracking the total portfolio value based on ⏱️ real-time stock prices.

---

## 🛠️ Features

### 🖼️ Frontend:
- **📊 Dashboard**:
  - Shows portfolio metrics like total 💵 value, top-performing stock, and portfolio 🧩 distribution.
- **📝 Form**:
  - Allows users to ➕ add or ✏️ edit stock details (e.g., 🏷️ stock name, 📃 ticker, 🔢 quantity, 💲 buy price).
- **📋 Holdings List**:
  - Displays current stock holdings in a 📄 table with options to ✏️ edit or 🗑️ delete.
- **📱 Responsive Design**:
  - Built using ⚛️ React (or any modern 🌐 framework) for an intuitive and 📱 mobile-friendly experience.

### 💾 Backend:
- **🔗 RESTful APIs**:
  - ➕ Add a new stock.
  - ✏️ Update stock details.
  - 🗑️ Delete a stock.
  - 📥 Fetch all stocks and calculate the portfolio value.
- **☕ Spring Boot Framework**:
  - Java-based backend with 🛠️ Spring Boot and 🗄️ JPA for efficient database interactions.
- **⚠️ Error Handling**:
  - Meaningful HTTP 🔢 codes and 🛡️ exception handling for a robust API.

### 🗄️ Database:
- **🐬 MySQL**:
  - Relational database to store stock details (e.g., 🏷️ name, 📃 ticker, 🔢 quantity, 💲 price).
  - Designed schema to include relevant 🔗 relations for users and portfolios.

### ⏱️ Real-Time Data:
- **📈 Live Stock Prices**:
  - Integrated with free stock price APIs.
  - Dynamically updates portfolio value.
- **🆕 Default Portfolio**:
  - Each user gets 5️⃣ random stocks (quantity: 1️⃣) for demo purposes.

---

## 🚀 Live Demo:


https://github.com/user-attachments/assets/53cde2e0-2127-44fe-961c-5a80f9452737




---

## ⚙️ Installation and Setup

### 📋 Prerequisites
- **⚛️ Frontend**: React.js, npm/yarn
- **☕ Backend**: Java (JDK 11+), Maven/Gradle, SpringBoot, RESTful API
- **🐬 Database**: MySQL Server

### 🖥️ Steps to Run Locally

#### 🛠️ Backend Setup
1. 📥 Clone the repo:
   ```bash
   git clone https://github.com/Harshita-prachi3/PORTFOLIO_TRACKER.git
   ```
2. Navigate to the 🗂️ backend folder:
   ```bash
   cd portfolio-tracker-backend
   ```
3. Configure the `application.properties` file with your 🐬 MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_tracker
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
4. Run the Spring Boot app:
   ```bash
   mvn spring-boot:run
   ```

#### ⚛️ Frontend Setup
1. Navigate to the 🗂️ frontend folder:
   ```bash
   cd portfolio-tracker-frontend
   ```
2. Install 📦 dependencies:
   ```bash
   npm install
   ```
3. Start the dev server:
   ```bash
   npm start
   ```

### 🌐 Accessing the App
- Open your 🌍 browser and visit: `http://localhost:3000`

---

## 🤔 Assumptions and Limitations
- Each user starts with a portfolio of 5️⃣ predefined stocks (quantity: 1️⃣).
- API ⛔ rate limits may apply for fetching real-time stock prices.
- 🔒 Authentication isn’t implemented in this version.

---

## 📖 API Documentation
### 🔗 Endpoints
1. **➕ Add Stock**:
   - `POST /api/stocks`
2. **✏️ Update Stock**:
   - `PUT /api/stocks/{id}`
3. **🗑️ Delete Stock**:
   - `DELETE /api/stocks/{id}`
4. **📋 Get All Stocks**:
   - `GET /api/stocks`
