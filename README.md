# Game Center

Проект представляет собой платформу для игр. Проект состоит из бэкенда на Spring Boot, фронтенда на React и базы данных PostgreSQL. Вся инфраструктура запускается через Docker.

---

## **Технологии**

- **Backend**: Spring Boot (Java 17), PostgreSQL
- **Frontend**: React, Vite
- **Инфраструктура**: Docker, Docker Compose, Nginx

---

## **Запуск проекта**

### **1. Установка зависимостей**

Убедитесь, что у вас установлены следующие инструменты:

- **Java 17**
- **Node.js 20.x**
- **Docker** и **Docker Compose**
- **Git**

### **2. Клонирование репозитория**

```bash
git clone <>
cd game-center
```

### **3. Запуск через Docker Compose**

- Перейти в папку **docker**
```bash
cd docker
```
- Запустить проект
```bash
docker-compose up --build
```
- После запуска откройте браузер и перейдите по адресу
```bash
http://localhost:3000
```
