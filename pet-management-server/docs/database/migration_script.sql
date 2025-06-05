-- ----------------------------------------------------------------------------
-- MySQL Workbench Migration
-- Migrated Schemata: httc2
-- Source Schemata: httc
-- Created: Thu Jun  5 20:34:33 2025
-- Workbench Version: 8.0.42
-- ----------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------------
-- Schema httc2
-- ----------------------------------------------------------------------------
DROP SCHEMA IF EXISTS `httc2` ;
CREATE SCHEMA IF NOT EXISTS `httc2` ;

-- ----------------------------------------------------------------------------
-- Table httc2.bookings
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`bookings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `check_in_time` DATETIME(6) NOT NULL,
  `check_out_time` DATETIME(6) NOT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `is_confirmed` BIT(1) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `pet_id` BIGINT NOT NULL,
  `special_care_notes` VARCHAR(1000) NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'COMPLETED', 'CANCELLED') NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `room_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKrgoycol97o21kpjodw1qox4nc` (`room_id` ASC) VISIBLE,
  CONSTRAINT `FKrgoycol97o21kpjodw1qox4nc`
    FOREIGN KEY (`room_id`)
    REFERENCES `httc2`.`rooms` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.grooming_requests
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`grooming_requests` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `completed_date_time` DATETIME(6) NULL DEFAULT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `requested_date_time` DATETIME(6) NOT NULL,
  `scheduled_date_time` DATETIME(6) NULL DEFAULT NULL,
  `staff_notes` TEXT NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'APPROVED', 'REJECTED', 'SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `owner_id` BIGINT NOT NULL,
  `pet_id` BIGINT NOT NULL,
  `service_id` BIGINT NOT NULL,
  `staff_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKqty96ia1qmwd0ha5o7q7t34rm` (`owner_id` ASC) VISIBLE,
  INDEX `FKrxishr6r1weu7oelj1f6eax57` (`pet_id` ASC) VISIBLE,
  INDEX `FKcf5vdmo99f7bbcax32ile38ii` (`service_id` ASC) VISIBLE,
  INDEX `FK2agwv2phtfl3lcf8okp7268r5` (`staff_id` ASC) VISIBLE,
  CONSTRAINT `FK2agwv2phtfl3lcf8okp7268r5`
    FOREIGN KEY (`staff_id`)
    REFERENCES `httc2`.`users` (`id`),
  CONSTRAINT `FKcf5vdmo99f7bbcax32ile38ii`
    FOREIGN KEY (`service_id`)
    REFERENCES `httc2`.`grooming_services` (`id`),
  CONSTRAINT `FKqty96ia1qmwd0ha5o7q7t34rm`
    FOREIGN KEY (`owner_id`)
    REFERENCES `httc2`.`users` (`id`),
  CONSTRAINT `FKrxishr6r1weu7oelj1f6eax57`
    FOREIGN KEY (`pet_id`)
    REFERENCES `httc2`.`pets` (`pet_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.grooming_services
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`grooming_services` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `duration_minutes` INT NOT NULL,
  `image_url` VARCHAR(255) NULL DEFAULT NULL,
  `is_active` BIT(1) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `price` DECIMAL(38,2) NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.medical_appointments
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`medical_appointments` (
  `appointment_date_time` DATETIME(6) NOT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `doctor_id` BIGINT NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT NOT NULL,
  `pet_id` BIGINT NOT NULL,
  `request_id` BIGINT NULL DEFAULT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `diagnosis` TEXT NULL DEFAULT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `symptoms` TEXT NULL DEFAULT NULL,
  `treatment` TEXT NULL DEFAULT NULL,
  `status` ENUM('COMPLETED', 'CANCELLED', 'SCHEDULED', 'FOLLOW_UP') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_pxs3ql9vk6q521jhxcxp9s68i` (`request_id` ASC) VISIBLE,
  INDEX `FKhnh72hdn8pwot4gxcteye6yay` (`doctor_id` ASC) VISIBLE,
  INDEX `FKgv3pauhdb8udkaf2py010gpg0` (`owner_id` ASC) VISIBLE,
  INDEX `FK97njfn1pjsw5ciskoffuqg9pr` (`pet_id` ASC) VISIBLE,
  CONSTRAINT `FK97njfn1pjsw5ciskoffuqg9pr`
    FOREIGN KEY (`pet_id`)
    REFERENCES `httc2`.`pets` (`pet_id`),
  CONSTRAINT `FKgv3pauhdb8udkaf2py010gpg0`
    FOREIGN KEY (`owner_id`)
    REFERENCES `httc2`.`users` (`id`),
  CONSTRAINT `FKhnh72hdn8pwot4gxcteye6yay`
    FOREIGN KEY (`doctor_id`)
    REFERENCES `httc2`.`users` (`id`),
  CONSTRAINT `FKpa84b48kilyqty7avjtxnuh4f`
    FOREIGN KEY (`request_id`)
    REFERENCES `httc2`.`medical_requests` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.medical_requests
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`medical_requests` (
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT NOT NULL,
  `pet_id` BIGINT NOT NULL,
  `preferred_date_time` DATETIME(6) NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `updated_by_id` BIGINT NULL DEFAULT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `rejection_reason` TEXT NULL DEFAULT NULL,
  `symptoms` TEXT NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL,
  `request_status` ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKe8q0gdhy035nqo57gu57iti6j` (`owner_id` ASC) VISIBLE,
  INDEX `FK5cyn1sl62ein4ebpg94idd0o4` (`pet_id` ASC) VISIBLE,
  INDEX `FK50vup9f1t5almxvnwvw3rpgxe` (`updated_by_id` ASC) VISIBLE,
  CONSTRAINT `FK50vup9f1t5almxvnwvw3rpgxe`
    FOREIGN KEY (`updated_by_id`)
    REFERENCES `httc2`.`users` (`id`),
  CONSTRAINT `FK5cyn1sl62ein4ebpg94idd0o4`
    FOREIGN KEY (`pet_id`)
    REFERENCES `httc2`.`pets` (`pet_id`),
  CONSTRAINT `FKe8q0gdhy035nqo57gu57iti6j`
    FOREIGN KEY (`owner_id`)
    REFERENCES `httc2`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.medicines
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`medicines` (
  `unit_price` DOUBLE NULL DEFAULT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `unit` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.pets
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`pets` (
  `birthdate` DATE NULL DEFAULT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `pet_id` BIGINT NOT NULL AUTO_INCREMENT,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  `gender` VARCHAR(10) NULL DEFAULT NULL,
  `color` VARCHAR(50) NULL DEFAULT NULL,
  `species` VARCHAR(50) NOT NULL,
  `breed` VARCHAR(100) NULL DEFAULT NULL,
  `name` VARCHAR(100) NOT NULL,
  `avatar_url` VARCHAR(255) NULL DEFAULT NULL,
  `health_notes` TEXT NULL DEFAULT NULL,
  `nutrition_notes` TEXT NULL DEFAULT NULL,
  `vaccination_history` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`pet_id`),
  INDEX `FKc47kjb41qf50bwgddm024m5xn` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKc47kjb41qf50bwgddm024m5xn`
    FOREIGN KEY (`user_id`)
    REFERENCES `httc2`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.prescriptions
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`prescriptions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `quantity` INT NOT NULL,
  `usage_instructions` VARCHAR(255) NULL DEFAULT NULL,
  `appointment_id` BIGINT NOT NULL,
  `medicine_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKs9wcy0a97t5kmqkgmmlupo95e` (`appointment_id` ASC) VISIBLE,
  INDEX `FK2ee3ttqhbkr86e3xkgmnmnoqf` (`medicine_id` ASC) VISIBLE,
  CONSTRAINT `FK2ee3ttqhbkr86e3xkgmnmnoqf`
    FOREIGN KEY (`medicine_id`)
    REFERENCES `httc2`.`medicines` (`id`),
  CONSTRAINT `FKs9wcy0a97t5kmqkgmmlupo95e`
    FOREIGN KEY (`appointment_id`)
    REFERENCES `httc2`.`medical_appointments` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.rooms
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`rooms` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `clean_fee` DECIMAL(38,2) NULL DEFAULT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `is_available` BIT(1) NOT NULL,
  `nightly_fee` DECIMAL(38,2) NULL DEFAULT NULL,
  `service_fee` DECIMAL(38,2) NULL DEFAULT NULL,
  `type` ENUM('STANDARD', 'DELUXE', 'LUXURY', 'SUITE') NULL DEFAULT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.tasks
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`tasks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `completed` BIT(1) NOT NULL,
  `created_at` DATETIME(6) NOT NULL,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `due_date` DATETIME(6) NULL DEFAULT NULL,
  `title` VARCHAR(255) NOT NULL,
  `assignee_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKekr1dgiqktpyoip3qmp6lxsit` (`assignee_id` ASC) VISIBLE,
  CONSTRAINT `FKekr1dgiqktpyoip3qmp6lxsit`
    FOREIGN KEY (`assignee_id`)
    REFERENCES `httc2`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table httc2.users
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `httc2`.`users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL COMMENT 'Unique username for login',
  `password` VARCHAR(255) NOT NULL COMMENT 'User password (should be stored encrypted)',
  `email` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Email address for communications',
  `full_name` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Full name of the user',
  `locked` TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'Flag to indicate if the account is locked/disabled',
  `role` ENUM('ROLE_VET', 'ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username` (`username` ASC) VISIBLE,
  UNIQUE INDEX `username_2` (`username` ASC) VISIBLE,
  UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6` (`username` ASC) VISIBLE,
  INDEX `idx_users_email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = 'Stores user account information for authentication and user management';

-- ----------------------------------------------------------------------------
-- Routine httc2.calculate_avg_fee_per_day
-- ----------------------------------------------------------------------------
DELIMITER $$

DELIMITER $$
USE `httc2`$$
CREATE DEFINER=`hackerphobia`@`localhost` FUNCTION `calculate_avg_fee_per_day`(
    nightly_fee DECIMAL(10,2),
    clean_fee DECIMAL(10,2),
    service_fee DECIMAL(10,2),
    room_type VARCHAR(50)
) RETURNS decimal(10,2)
    DETERMINISTIC
BEGIN
    DECLARE multiplier DECIMAL(10,2);
    DECLARE adjusted_nightly_fee DECIMAL(10,2);
    DECLARE total_fee DECIMAL(10,2);
    DECLARE estimated_nights INT DEFAULT 10;
    
    -- Determine multiplier based on room type
    IF room_type = 'DELUXE' THEN
        SET multiplier = 1.10;
    ELSEIF room_type = 'LUXURY' THEN
        SET multiplier = 1.20;
    ELSEIF room_type = 'SUITE' THEN
        SET multiplier = 1.30;
    ELSE
        SET multiplier = 1.0;
    END IF;
    
    -- Calculate adjusted nightly fee with multiplier
    SET adjusted_nightly_fee = nightly_fee * multiplier;
    
    -- Calculate total fee for estimated nights
    SET total_fee = (adjusted_nightly_fee * estimated_nights) + clean_fee + service_fee;
    
    -- Calculate and return average fee per day
    RETURN ROUND(total_fee / estimated_nights, 2);
END$$

DELIMITER ;

-- ----------------------------------------------------------------------------
-- Routine httc2.calculate_total_fee
-- ----------------------------------------------------------------------------
DELIMITER $$

DELIMITER $$
USE `httc2`$$
CREATE DEFINER=`hackerphobia`@`localhost` FUNCTION `calculate_total_fee`(
    nightly_fee DECIMAL(10,2),
    clean_fee DECIMAL(10,2),
    service_fee DECIMAL(10,2),
    check_in DATETIME,
    check_out DATETIME,
    room_type VARCHAR(50)
) RETURNS decimal(10,2)
    DETERMINISTIC
BEGIN
    DECLARE multiplier DECIMAL(10,2);
    DECLARE nights INT;
    DECLARE adjusted_nightly_fee DECIMAL(10,2);
    DECLARE total_fee DECIMAL(10,2);
    
    -- Validate dates
    IF check_in IS NULL OR check_out IS NULL OR check_out <= check_in THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid check-in/check-out time.';
    END IF;
    
    -- Determine multiplier based on room type
    IF room_type = 'DELUXE' THEN
        SET multiplier = 1.10;
    ELSEIF room_type = 'LUXURY' THEN
        SET multiplier = 1.20;
    ELSEIF room_type = 'SUITE' THEN
        SET multiplier = 1.30;
    ELSE
        SET multiplier = 1.0;
    END IF;
    
    -- Calculate number of nights
    SET nights = DATEDIFF(check_out, check_in);
    IF TIME(check_out) > TIME(check_in) THEN
        SET nights = nights + 1;
    END IF;
    
    -- Calculate adjusted nightly fee with multiplier
    SET adjusted_nightly_fee = nightly_fee * multiplier;
    
    -- Calculate total fee
    SET total_fee = (adjusted_nightly_fee * nights) + clean_fee + service_fee;
    
    RETURN ROUND(total_fee, 2);
END$$

DELIMITER ;
SET FOREIGN_KEY_CHECKS = 1;
