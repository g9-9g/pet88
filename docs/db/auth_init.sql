CREATE TABLE `httc`.`users` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    full_name VARCHAR(255),
    locked BOOLEAN NOT NULL DEFAULT FALSE
);

-- Add some comments to document the table
ALTER TABLE `httc`.`users` COMMENT 'Stores user account information for authentication and user management';
ALTER TABLE `httc`.`users` MODIFY COLUMN username VARCHAR(255) NOT NULL UNIQUE COMMENT 'Unique username for login';
ALTER TABLE `httc`.`users` MODIFY COLUMN password VARCHAR(255) NOT NULL COMMENT 'User password (should be stored encrypted)';
ALTER TABLE `httc`.`users` MODIFY COLUMN email VARCHAR(255) COMMENT 'Email address for communications';
ALTER TABLE `httc`.`users` MODIFY COLUMN full_name VARCHAR(255) COMMENT 'Full name of the user';
ALTER TABLE `httc`.`users` MODIFY COLUMN locked BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Flag to indicate if the account is locked/disabled';

-- Optional: Add indexes for frequently queried fields
CREATE INDEX idx_users_email ON `httc`.`users`(email);

-- Create roles table
CREATE TABLE `httc`.`roles` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create user_roles join table
CREATE TABLE `httc`.`user_roles` (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert default roles
INSERT INTO `httc`.`roles` (name) VALUES 
('ROLE_VET'),
('ROLE_ADMIN'),
('ROLE_PET_OWNER'),
('ROLE_STAFF');

-- Update users table if needed
ALTER TABLE `httc`.`users` 
    MODIFY COLUMN username VARCHAR(255) NOT NULL UNIQUE COMMENT 'Unique username for login',
    MODIFY COLUMN password VARCHAR(255) NOT NULL COMMENT 'User password (should be stored encrypted)',
    MODIFY COLUMN email VARCHAR(255) COMMENT 'Email address for communications',
    MODIFY COLUMN full_name VARCHAR(255) COMMENT 'Full name of the user',
    MODIFY COLUMN locked BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Flag to indicate if the account is locked/disabled';

-- Add indexes
CREATE INDEX idx_users_email ON `httc`.`users`(email);
CREATE INDEX idx_roles_name ON `httc`.`roles`(name);