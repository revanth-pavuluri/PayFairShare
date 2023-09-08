CREATE TABLE finalsplits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pay_to BIGINT NOT NULL,
    pay_by BIGINT NOT NULL,
    amount FLOAT NOT NULL,
    status VARCHAR(255),
    group_id BIGINT,
    created_on TIMESTAMP NOT NULL DEFAULT current_timestamp(),
    updated_on TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    FOREIGN KEY (group_id) REFERENCES `groups`(id) ON UPDATE CASCADE ON DELETE CASCADE
);
