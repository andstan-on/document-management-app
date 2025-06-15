CREATE TABLE Token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    created_at DATETIME,
    expires_at DATETIME,
    validated_at DATETIME,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);