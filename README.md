# FashionStore – Full Stack Fashion E-Commerce Web Application

**FashionStore** is a full-stack fashion e-commerce web application developed using Java, JSP, Servlets, JDBC, and MySQL. It provides customers with an online shopping experience where they can browse products, search and filter items, manage their shopping cart, place orders, and track their purchases.

The application follows a layered architecture with separate controller, DAO, model, and utility components. It uses JDBC for database connectivity and BCrypt for password hashing.

## Live Demo

**Live Application:** [https://fashionstore-sl4m.onrender.com](https://fashionstore-sl4m.onrender.com)

> Note: The application is hosted on Render's free tier. The server may take some time to respond when accessed after a period of inactivity.

## Features

### User Authentication
- User registration with input validation.
- Secure login and logout functionality.
- Password hashing using BCrypt.
- Change password functionality.
- Forgot password functionality.
- Session-based user authentication.

### Product Browsing
- Browse available fashion products.
- View individual product details.
- Browse products by category.
- Search products by keyword.
- Filter products by price range.
- Sort products by different criteria.
- Pagination for product listings.

### Shopping Cart
- Add products to the shopping cart.
- Select product variants such as size and color.
- Update product quantities.
- Remove individual items from the cart.
- View cart subtotal and total amount.
- Stock availability validation.

### Checkout and Orders
- Checkout with saved customer details.
- Cash on Delivery (COD) payment option.
- Delivery information validation.
- Automatic order creation.
- Order confirmation page.
- View order history.
- View individual order details.
- Cancel orders while they are eligible for cancellation.
- Automatic stock updates when orders are placed or cancelled.

### User Profile
- View customer profile information.
- Update personal details.
- Update saved delivery address.
- Change account password.

### Additional Features
- Responsive user interface.
- Reusable JSP components for header, navigation bar, and footer.
- Custom 404 error page.
- Custom 403 error page was intentionally excluded from the implemented scope.

## Technology Stack

| Category | Technologies |
|---|---|
| Frontend | HTML, CSS, JavaScript, JSP |
| Backend | Java, Jakarta Servlets |
| Database | MySQL 8.0 / TiDB Cloud |
| Database Connectivity | JDBC |
| Password Security | BCrypt |
| Build Tool | Apache Maven |
| Application Server | Apache Tomcat 10.1 |
| Containerization | Docker |
| Hosting | Render |
| Version Control | Git, GitHub |
| Database Driver | MySQL Connector/J |

## Application Architecture

The application follows a layered architecture to maintain separation of concerns and improve code organization.

```text
                    Customer
                       |
                       v
                JSP / Frontend
                       |
                       v
               Controller Layer
             (Jakarta Servlets)
                       |
                       v
                   DAO Layer
              (Database Operations)
                       |
                       v
                 Model Layer
               (Java Classes)
                       |
                       v
                JDBC Utility
                       |
                       v
                 MySQL / TiDB
```

### Project Structure

```text
FashionStore/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── fashionstore/
│   │   │           ├── controller/
│   │   │           ├── dao/
│   │   │           │   └── impl/
│   │   │           ├── model/
│   │   │           └── util/
│   │   │
│   │   ├── resources/
│   │   │
│   │   └── webapp/
│   │       ├── assets/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       │       ├── banners/
│   │       │       ├── categories/
│   │       │       └── products/
│   │       │
│   │       ├── index.jsp
│   │       │
│   │       └── WEB-INF/
│   │           ├── web.xml
│   │           └── views/
│   │               └── partials/
│   │
├── Dockerfile
├── .dockerignore
└── pom.xml
```

### Package Responsibilities

| Package | Responsibility |
|---|---|
| `controller` | Handles HTTP requests, user interactions, validation, and page navigation. |
| `dao` | Contains DAO interfaces defining database operations. |
| `dao.impl` | Implements database operations using JDBC and SQL queries. |
| `model` | Contains Java model classes representing application entities. |
| `util` | Contains shared utility classes, including database connectivity and password handling. |

## Database Design

The application uses a relational database with eight tables.

| Table | Description |
|---|---|
| `users` | Stores customer information, login credentials, and saved address details. |
| `categories` | Stores product categories. |
| `products` | Stores product information, descriptions, prices, and image paths. |
| `product_variants` | Stores product variations such as size, color, SKU, and stock quantity. |
| `carts` | Maintains one shopping cart per customer. |
| `cart_items` | Stores individual products and quantities added to shopping carts. |
| `orders` | Stores order details, delivery information, payment method, and order status. |
| `order_items` | Stores the products included in each order, including price and variant snapshots. |

### Database Relationships

- A user can have one shopping cart.
- A cart can contain multiple cart items.
- A product belongs to a category.
- A product can have multiple variants.
- An order belongs to a user.
- An order can contain multiple order items.
- Order items retain product and pricing details at the time of purchase.

## Installation and Setup

Follow these steps to run FashionStore locally.

### Prerequisites

Install the following software:

- Java Development Kit (JDK) 25
- Apache Maven
- Apache Tomcat 10.1
- MySQL Server 8.0
- Eclipse IDE for Enterprise Java and Web Developers (recommended)
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/VMT-084/FashionStore.git
```

Navigate to the application directory:

```bash
cd FashionStore/FashionStore
```

### 2. Create the Database

Open MySQL Workbench and execute:

```sql
CREATE DATABASE fashion_store;
USE fashion_store;
```

Create the eight database tables described in the Database Design section and insert the required sample data.

### 3. Configure Database Credentials

The application uses environment variables for database configuration.

Set the following environment variables:

| Variable | Example |
|---|---|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `3306` |
| `DB_NAME` | `fashion_store` |
| `DB_USER` | `root` |
| `DB_PASSWORD` | Your MySQL password |

For local development, configure these variables in your IDE's run configuration or operating system environment.

The production deployment uses the corresponding TiDB Cloud connection details.

**Important:** Never commit database passwords, API credentials, or other secrets to GitHub.

### 4. Build the Application

Run the following Maven command:

```bash
mvn clean package
```

This generates the WAR file inside the `target` directory.

### 5. Run Using Apache Tomcat

Deploy the generated WAR file to Apache Tomcat 10.1.

Start the Tomcat server and access the application through:

```text
http://localhost:8080/FashionStore/
```

The exact context path depends on the deployed WAR filename. If deployed as `ROOT.war`, use:

```text
http://localhost:8080/
```

## Deployment

FashionStore is deployed using **Docker and Render**, with **TiDB Cloud** as its managed database.

### Deployment Architecture

```text
        GitHub Repository
                |
                v
          Render Platform
                |
                v
          Docker Build
                |
                v
        Apache Tomcat 10.1
                |
                v
        FashionStore WAR
                |
                v
          TiDB Cloud
          (MySQL Compatible)
```

### Deployment Components

- **GitHub:** Stores the application source code.
- **Docker:** Packages the application and its runtime environment.
- **Apache Tomcat:** Runs the Java web application.
- **Render:** Hosts the Dockerized application.
- **TiDB Cloud:** Provides the remote MySQL-compatible database.

Database credentials are configured through Render environment variables rather than being stored in the source code.

## Security and Validation

The application incorporates the following security and validation measures:

- BCrypt password hashing.
- Session-based authentication.
- Session invalidation during logout.
- Server-side validation of user inputs.
- Validation of cart quantities and stock availability.
- Ownership checks for customer cart and order operations.
- Prepared statements through JDBC to help prevent SQL injection.
- Environment-based database credential management.

**Security note:** The simplified forgot-password flow does not include email OTP verification. It should be strengthened before using the application in a production environment.

## Future Improvements

Potential enhancements include:

- Integration of online payment gateways.
- Email verification and OTP-based password recovery.
- Product reviews and ratings.
- Wishlist functionality.
- Admin dashboard for product and inventory management.
- Order tracking and shipment updates.
- Enhanced product recommendations.
- Automated testing and CI/CD improvements.

## Author

**Vishnu M T**

Information Science and Engineering Graduate

- GitHub: [VMT-084](https://github.com/VMT-084)
- Project Repository: [FashionStore](https://github.com/VMT-084/FashionStore)
- Live Application: [FashionStore](https://fashionstore-sl4m.onrender.com)

---

If you find this project interesting, feel free to explore the repository!

**FashionStore – Bringing Fashion Shopping Online.**
