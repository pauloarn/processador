CREATE TABLE processor.payment
(
    id            SERIAL      NOT NULL primary key,
    paymentCode   int,
    paymentStatus varchar(50) not null,
    cpfCnpj       varchar(14) not null,
    paymentMethod varchar(50) not null,
    cardNumber    varchar(150),
    paymentAmount decimal,
    createdAt     timestamp   not null,
    updatedAt     timestamp   not null
);
