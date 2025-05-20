CREATE TABLE `httc`.`requests` (
    request_id SERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    pet_id BIGINT UNSIGNED NOT NULL,
    preferred_date DATE,
    preferred_time TIME,
    note TEXT,
    doctor_id BIGINT,
    medicalRequestStatus ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    reject_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner FOREIGN KEY (owner_id)
        REFERENCES `httc`.`users`(id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_pet FOREIGN KEY (pet_id)
        REFERENCES `httc`.`pets`(pet_id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_doctor FOREIGN KEY (doctor_id)
        REFERENCES `httc`.`users`(id)
        ON DELETE SET NULL
);

-- Add comments to document the table
ALTER TABLE `httc`.`requests` COMMENT 'Stores appointment requests from pet owners';
ALTER TABLE `httc`.`requests` MODIFY COLUMN request_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key for the request';
ALTER TABLE `httc`.`requests` MODIFY COLUMN owner_id BIGINT NOT NULL COMMENT 'ID of the pet owner making the request';
ALTER TABLE `httc`.`requests` MODIFY COLUMN pet_id BIGINT NOT NULL COMMENT 'ID of the pet for the appointment';
ALTER TABLE `httc`.`requests` MODIFY COLUMN preferred_date DATE COMMENT 'Preferred date for the appointment';
ALTER TABLE `httc`.`requests` MODIFY COLUMN preferred_time TIME COMMENT 'Preferred time for the appointment';
ALTER TABLE `httc`.`requests` MODIFY COLUMN note TEXT COMMENT 'Additional notes from the pet owner';
ALTER TABLE `httc`.`requests` MODIFY COLUMN doctor_id BIGINT COMMENT 'ID of the assigned veterinarian (if any)';
ALTER TABLE `httc`.`requests` MODIFY COLUMN medicalRequestStatus ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT 'Current medicalRequestStatus of the request';
ALTER TABLE `httc`.`requests` MODIFY COLUMN reject_reason TEXT COMMENT 'Reason for rejection (if rejected)';
ALTER TABLE `httc`.`requests` MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the request was created';
ALTER TABLE `httc`.`requests` MODIFY COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the request was last updated';

-- Add indexes for frequently queried fields
CREATE INDEX idx_requests_owner ON `httc`.`requests`(owner_id);
CREATE INDEX idx_requests_pet ON `httc`.`requests`(pet_id);
CREATE INDEX idx_requests_doctor ON `httc`.`requests`(doctor_id);
CREATE INDEX idx_requests_status ON `httc`.`requests`(medicalRequestStatus);
CREATE INDEX idx_requests_date ON `httc`.`requests`(preferred_date); 