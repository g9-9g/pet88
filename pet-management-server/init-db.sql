-- Calculate Average Fee Per Day
DELIMITER //
CREATE FUNCTION IF NOT EXISTS calculate_avg_fee_per_day(
    nightly_fee DECIMAL(10,2),
    clean_fee DECIMAL(10,2),
    service_fee DECIMAL(10,2),
    room_type VARCHAR(50)
) 
RETURNS DECIMAL(10,2)
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
END //
DELIMITER ;

-- Calculate Total Fee
DELIMITER //
CREATE FUNCTION IF NOT EXISTS calculate_total_fee(
    nightly_fee DECIMAL(10,2),
    clean_fee DECIMAL(10,2),
    service_fee DECIMAL(10,2),
    check_in DATETIME,
    check_out DATETIME,
    room_type VARCHAR(50)
)
RETURNS DECIMAL(10,2)
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
END //
DELIMITER ;