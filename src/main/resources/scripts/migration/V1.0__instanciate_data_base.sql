CREATE TABLE processor.payment(
    id  bigint NOT NULL primary key,
    cpfCnpj varchar(14) not null,
    paymentMethod varchar(50) not null,
    cardNumber varchar(150),
    paymentAmount decimal,
    createdAt timestamp not null,
    updatedAt timestamp not null
);
