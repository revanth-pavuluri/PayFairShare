CREATE TABLE expensesplits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(255),
    user_responsible_amount FLOAT,
    expense_id BIGINT,
    user_id BIGINT,
    created_on TIMESTAMP NOT NULL DEFAULT current_timestamp(),
    updated_on TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    FOREIGN KEY (expense_id) REFERENCES expenses(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);
