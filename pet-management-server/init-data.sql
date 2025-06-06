-- Insert Users with different roles
-- Passwords are hashed version of 'password123'
INSERT INTO users (username, password, email, full_name, locked, role) VALUES
-- Admin users
('admin1', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'admin1@example.com', 'Admin One', 0, 'ROLE_ADMIN'),
('admin2', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'admin2@example.com', 'Admin Two', 0, 'ROLE_ADMIN'),

-- Vet users
('vet1', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'vet1@example.com', 'Dr. Vet One', 0, 'ROLE_VET'),
('vet2', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'vet2@example.com', 'Dr. Vet Two', 0, 'ROLE_VET'),

-- Staff users
('staff1', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'staff1@example.com', 'Staff One', 0, 'ROLE_STAFF'),
('staff2', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'staff2@example.com', 'Staff Two', 0, 'ROLE_STAFF'),

-- Pet Owner users
('owner1', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'owner1@example.com', 'Pet Owner One', 0, 'ROLE_PET_OWNER'),
('owner2', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'owner2@example.com', 'Pet Owner Two', 0, 'ROLE_PET_OWNER');

-- Insert Rooms
INSERT INTO rooms (description, type, nightly_fee, clean_fee, service_fee, is_available) VALUES
-- Standard Rooms (3 rooms)
('A cozy room for small pets under 10kg with a soft bed and basic toys.', 'STANDARD', 150000, 30000, 20000, 1),
('Comfortable room with temperature control and basic amenities for small pets.', 'STANDARD', 150000, 30000, 20000, 1),
('Standard room with natural lighting and ventilation for small pets.', 'STANDARD', 150000, 30000, 20000, 1),

-- Deluxe Rooms (3 rooms)
('Spacious room with premium bedding, toys, and 24/7 monitoring for medium-sized pets.', 'DELUXE', 250000, 50000, 30000, 1),
('Deluxe room with climate control, premium amenities, and daily play sessions.', 'DELUXE', 250000, 50000, 30000, 1),
('Luxurious room with premium bedding and personalized care for medium-sized pets.', 'DELUXE', 250000, 50000, 30000, 1),

-- Luxury Rooms (2 rooms)
('Premium suite with advanced climate control, premium toys, and personalized care for large pets.', 'LUXURY', 350000, 70000, 40000, 1),
('Luxury room with premium amenities, 24/7 monitoring, and dedicated play area for large pets.', 'LUXURY', 350000, 70000, 40000, 1),

-- Suite Rooms (2 rooms)
('Ultimate luxury suite with private play area, premium amenities, and dedicated staff for VIP pets.', 'SUITE', 500000, 100000, 50000, 1),
('Executive suite with private garden access, premium services, and personalized care for VIP pets.', 'SUITE', 500000, 100000, 50000, 1);

-- Insert Medicines
INSERT INTO medicines (name, description, unit, unit_price) VALUES
('Amoxicillin 250mg', 'Antibiotic for bacterial infections', 'tablet', 5000),
('Drontal Plus', 'Deworming medication for dogs and cats', 'tablet', 15000),
('Frontline Plus', 'Flea and tick prevention', 'vial', 250000),
('Heartgard Plus', 'Heartworm prevention', 'tablet', 35000),
('Meloxicam 1.5mg', 'Anti-inflammatory and pain relief', 'tablet', 8000),
('Vitamin B Complex', 'Vitamin supplement for pets', 'vial', 120000),
('Dexamethasone 0.5mg', 'Anti-inflammatory and immunosuppressant', 'tablet', 3000),
('Metronidazole 250mg', 'Antibiotic for gastrointestinal infections', 'tablet', 4000),
('Prednisolone 5mg', 'Anti-inflammatory and immunosuppressant', 'tablet', 6000),
('Fenbendazole 500mg', 'Broad-spectrum dewormer', 'tablet', 10000);

-- Insert Grooming Services
INSERT INTO grooming_services (name, description, duration_minutes, price, is_active) VALUES
('Basic Bath & Brush', 'Basic cleaning and brushing service', 60, 150000, 1),
('Full Grooming Package', 'Complete grooming including bath, haircut, and nail trimming', 120, 300000, 1),
('Deluxe Spa Treatment', 'Premium grooming with special treatments and massage', 180, 500000, 1),
('Nail Trimming', 'Professional nail care service', 30, 80000, 1),
('Ear Cleaning', 'Professional ear cleaning and inspection', 30, 100000, 1),
('Teeth Cleaning', 'Professional teeth cleaning service', 45, 200000, 1),
('Flea Treatment', 'Professional flea removal and prevention treatment', 60, 250000, 1),
('Skin Treatment', 'Specialized skin care treatment', 90, 350000, 1),
('Haircut & Styling', 'Professional haircut and styling service', 90, 250000, 1),
('Paw Care Package', 'Complete paw care including trimming and moisturizing', 45, 150000, 1);

-- Insert Pets for owner1
INSERT INTO pets (user_id, name, species, breed, gender, birthdate, color, health_notes, nutrition_notes, vaccination_history) VALUES
-- Dogs
(7, 'Max', 'DOG', 'Golden Retriever', 'MALE', '2020-01-15', 'Golden', 'Regular checkups needed', 'High-quality dog food', 'All vaccinations up to date'),
-- Cats
(7, 'Luna', 'CAT', 'Siamese', 'FEMALE', '2021-03-20', 'White', 'Indoor cat', 'Premium cat food', 'Annual vaccinations complete'),
-- Birds
(7, 'Rio', 'BIRD', 'Parrot', 'MALE', '2019-06-10', 'Green', 'Needs regular wing trimming', 'Seed mix diet', 'No specific vaccinations required'),
-- Fish
(7, 'Nemo', 'FISH', 'Goldfish', 'MALE', '2022-01-01', 'Orange', 'Regular water changes needed', 'Fish flakes', 'No vaccinations required');

-- Insert Pets for owner2
INSERT INTO pets (user_id, name, species, breed, gender, birthdate, color, health_notes, nutrition_notes, vaccination_history) VALUES
-- Dogs
(8, 'Bella', 'DOG', 'German Shepherd', 'FEMALE', '2020-05-10', 'Black and Tan', 'Active dog, needs regular exercise', 'Large breed dog food', 'All vaccinations up to date'),
-- Cats
(8, 'Oliver', 'CAT', 'Persian', 'MALE', '2021-08-15', 'Grey', 'Long-haired, needs regular grooming', 'Hairball control food', 'Annual vaccinations complete'),
-- Birds
(8, 'Sunny', 'BIRD', 'Canary', 'FEMALE', '2022-02-20', 'Yellow', 'Sensitive to temperature changes', 'Special bird seed mix', 'No specific vaccinations required'),
-- Fish
(8, 'Dory', 'FISH', 'Betta', 'FEMALE', '2022-03-01', 'Blue', 'Needs warm water', 'Betta pellets', 'No vaccinations required');
