create table if not exists roles (
    id bigserial primary key,
    name varchar(50) not null unique,
    description varchar(255),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create table if not exists app_users (
    id bigserial primary key,
    email varchar(150) not null unique,
    password varchar(255) not null,
    full_name varchar(150) not null,
    enabled boolean not null default true,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create table if not exists app_user_roles (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_user_roles_user foreign key (user_id) references app_users(id) on delete cascade,
    constraint fk_user_roles_role foreign key (role_id) references roles(id) on delete cascade
);

create table if not exists customers (
    id bigserial primary key,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(150) not null unique,
    phone varchar(25),
    status varchar(20) not null,
    user_id bigint unique,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_customers_user foreign key (user_id) references app_users(id)
);

create table if not exists customer_transactions (
    id bigserial primary key,
    customer_id bigint not null,
    transaction_reference varchar(64) not null unique,
    amount numeric(19,2) not null,
    reward_points bigint not null,
    transaction_date timestamp not null,
    description varchar(500),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_transactions_customer foreign key (customer_id) references customers(id) on delete cascade
);

create index if not exists idx_customers_email on customers(email);
create index if not exists idx_customers_user_id on customers(user_id);
create index if not exists idx_transactions_customer_id on customer_transactions(customer_id);
create index if not exists idx_transactions_transaction_date on customer_transactions(transaction_date);
create index if not exists idx_transactions_customer_date on customer_transactions(customer_id, transaction_date);

insert into roles(name, description) values
('ROLE_ADMIN', 'System administrator'),
('ROLE_CUSTOMER', 'Customer account')
on conflict (name) do nothing;

insert into app_users(email, password, full_name, enabled)
values ('admin@retail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOe/1O1c1nQZ4G6dkprdFM5lTyBC0bY4e', 'System Admin', true)
on conflict (email) do nothing;

insert into app_user_roles(user_id, role_id)
select u.id, r.id
from app_users u, roles r
where u.email = 'admin@retail.com' and r.name = 'ROLE_ADMIN'
on conflict do nothing;

insert into customers(first_name, last_name, email, phone, status, user_id)
values ('John', 'Doe', 'john.doe@retail.com', '9999999999', 'ACTIVE', null)
on conflict (email) do nothing;

insert into customers(first_name, last_name, email, phone, status, user_id)
values ('Jane', 'Smith', 'jane.smith@retail.com', '8888888888', 'ACTIVE', null)
on conflict (email) do nothing;


insert into customer_transactions(customer_id, transaction_reference, amount, reward_points, transaction_date, description)
select c.id, 'seed-john-1', 120.00, 90, now() - interval '20 days', 'Seed transaction for John'
from customers c where c.email = 'john.doe@retail.com'
on conflict (transaction_reference) do nothing;

insert into customer_transactions(customer_id, transaction_reference, amount, reward_points, transaction_date, description)
select c.id, 'seed-jane-1', 75.00, 25, now() - interval '10 days', 'Seed transaction for Jane'
from customers c where c.email = 'jane.smith@retail.com'
on conflict (transaction_reference) do nothing;

insert into customer_transactions(customer_id, transaction_reference, amount, reward_points, transaction_date, description)
select c.id, 'seed-john-2', 220.00, 290, now() - interval '2 days', 'High value seed transaction'
from customers c where c.email = 'john.doe@retail.com'
on conflict (transaction_reference) do nothing;
