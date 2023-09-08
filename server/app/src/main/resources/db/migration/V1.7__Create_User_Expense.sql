CREATE TABLE user_expense (
    user_id BIGINT,
    expense_id BIGINT,
    PRIMARY KEY (user_id, expense_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (expense_id) REFERENCES expenses(id) ON UPDATE CASCADE ON DELETE CASCADE
);