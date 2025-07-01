# 🎓 Student Management System (SMS)

A powerful, real-world **Student Management System** built with **Spring Boot**, **Thymeleaf**, and **Bootstrap**. Designed with **role-based access**, **ownership-based authorization**, **real-time notifications**, **assignment uploads**, **interactive charts**, and **priority announcements**.

🚧 **Chat feature** is currently under development to support role-based messaging.

---

## ✨ Key Features

- 🔐 **Role-Based Access & Authorization**
  - 👑 Admin: Full system control
  - 👨‍🏫 Teacher: Manage own students, upload assignments, post announcements
  - 🎓 Student: View own data, download files, and read updates

- 👤 **Strict Ownership Control**
  - Every user manages only their own data

- 📁 **Assignment Management**
  - Upload by Admin/Teacher
  - Download by Students

- 🔔 **Real-time Notifications**
  - New files, announcements, results, and more

- 📣 **Priority-Based Announcements**
  - High / Medium / Low priority, sorted and highlighted

- 📊 **Interactive Dashboard Charts**
  - Student count, performance analytics, department stats

- 💬 **Chat (coming soon!)**
  - One-on-one communication between teachers and students

---

## 🛠️ Tech Stack

| Layer         | Technologies Used                             |
|---------------|-----------------------------------------------|
| 🌐 Frontend   | HTML, CSS, JavaScript, Bootstrap, Thymeleaf   |
| 🔧 Backend    | Java, Spring Boot, Spring MVC, Spring Data JPA |
| 🔒 Security   | Spring Security with Role + Ownership Filters |
| 💾 Database   | MySQL                                          |
| 📈 Charts     | Chart.js or Google Charts (via Thymeleaf)     |

---

## 👥 User Roles & Permissions

| Role     | Access Capabilities                                                                      |
|----------|-------------------------------------------------------------------------------------------|
| 👑 Admin | Create/manage teachers, students, departments, courses, announcements, assignments        |
| 👨‍🏫 Teacher | Handle own students, upload class assignments, post announcements                      |
| 🎓 Student | View profile, download files, read announcements, get notified                          |

---

## 🗂️ Project Structure

src/
- ├── controller/
- ├── entity/
- │ ├── User (base)
- │ ├── Student
- │ ├── Teacher
- ├── repository/
- ├── service/
- ├── config/
- │ └── SecurityConfig.java
- └── templates/


---

## 📦 Main Modules

- ✅ Authentication (Login for Admin, Teacher, Student)
- ✅ Authorization (Access only own data)
- ✅ Assignment Upload & Download
- ✅ Notifications System
- ✅ Announcements with Priority Levels
- ✅ Charts Dashboard
- ⏳ Chat Module *(in development)*

---

## 🔁 Functional Flow

| Endpoint                        | Access     | Description                            |
|---------------------------------|------------|----------------------------------------|
| `/login`                        | Public     | Login page for all roles               |
| `/admin/create-teacher`        | Admin      | Register a new teacher                 |
| `/teacher/upload-assignment`   | Teacher    | Upload files for their own class       |
| `/student/download`            | Student    | View and download assigned files       |
| `/announcements`               | All Roles  | View announcements sorted by priority  |

---

## 🌱 Future Enhancements
- 💬 Role-based chat system (1-on-1 messaging)
- 📱 Fully responsive mobile UI
- 📊 Advanced analytics and reports
- 📄 Export PDFs for results & assignments

---

## 🧑‍💻 Developer
Krishna Malavia 
🎓 MCA (9 CGPA) | 5⭐ HackerRank in Java, Python, Problem Solving
🧠 Backend Developer 
- 🔗 GitHub
- 🔗 LinkedIn
