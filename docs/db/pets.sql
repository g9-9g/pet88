CREATE TABLE `httc`.`pets` (
    pet_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(100),
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female')),
    birthdate DATE,
    color VARCHAR(50),
    avatar_url VARCHAR(255),
    health_notes TEXT,
    vaccination_history JSONB,
    nutrition_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES `httc`.`users`(user_id)
        ON DELETE CASCADE
);
