CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    upi_id VARCHAR(255),
    created_on TIMESTAMP NOT NULL DEFAULT current_timestamp(),
    updated_on TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
);

