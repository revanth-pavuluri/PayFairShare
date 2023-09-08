CREATE TABLE user_group (
    user_id BIGINT,
    group_id BIGINT,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES `groups`(id) ON UPDATE CASCADE ON DELETE CASCADE
);