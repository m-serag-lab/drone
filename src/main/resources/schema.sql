create table drone
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    serial_number      VARCHAR(100),
    model              VARCHAR(20),
    max_weight         DECIMAL,
    battery_percentage DECIMAL,
    state              VARCHAR(20)
);

create table medication
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR,
    weight     DECIMAL,
    code       VARCHAR,
    image_path VARCHAR
);

create table drone_medication
(
    drone_id      BIGINT,
    medication_id BIGINT,
    FOREIGN KEY (drone_id) references drone (id) ON DELETE CASCADE,
    FOREIGN KEY (medication_id) references medication (id) ON DELETE CASCADE
);
