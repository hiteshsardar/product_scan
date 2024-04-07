
# Product Scan

An application used for keep tracking of products, we can scan a product or manually entered the details about a product. This application mainly developed for selling the product and billing.
## Clone the repository

git clone https://github.com/hiteshsardar/product_scan
## Pre-requisites
- Gradle installation and configuration
- Java installation and configuration
- Any code Ide for java (intellij idea/Eclipse)
## Steps to Build
- clone the project
- Open the root folder of the project
- open command prompt on the root folder
- run cmd -> gradlew clean build (cmd will be downloading all the dependencies for the project)
- run the project from any ide
## About
This project has a conf folder and that folder has a file called first_user.json, this file cntains the user that will configured first with the database is super admin and all the operarion permission will be permitted to that user.
- The default email is admin@user.com and password is admin.
- If you want to change the user then recomended to change before start of the application.
All the apis are listed below with privileges
- Administrator
    - http://localhost:8086/auth/signup
    - http://localhost:8086/product/save_product
    - http://localhost:8086/product/delete_product_request
    - http://localhost:8086/product/delete_product
- User
    - http://localhost:8086/auth/signin
    - http://localhost:8086/public/user/get-users
    - http://localhost:8086/public/product/get-products

## Feedback

If you have any feedback, please reach out to me at sardarhitesh1998@gmaail.com

