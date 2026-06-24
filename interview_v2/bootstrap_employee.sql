-- 1. Create Department Table
CREATE TABLE IF NOT EXISTS Department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL
);

-- 2. Create Employee Table
CREATE TABLE IF NOT EXISTS Employee (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    salary DECIMAL(10, 2),
    department_id INT,
    manager_id INT,
    CONSTRAINT fk_department
        FOREIGN KEY(department_id) 
        REFERENCES Department(department_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_manager
        FOREIGN KEY(manager_id) 
        REFERENCES Employee(id)
        ON DELETE SET NULL
);

-- 3. Insert Departments
INSERT INTO Department (department_name) VALUES 
('Engineering'),
('Sales'),
('Marketing'),
('HR'),
('Executive');

-- 4. Insert Employees
-- Executives (No managers)
INSERT INTO Employee (name, email, salary, department_id, manager_id) VALUES 
('Alice CEO', 'alice@example.com', 250000, 5, NULL),
('Bob CTO', 'bob@example.com', 220000, 5, NULL);

-- Engineering
INSERT INTO Employee (name, email, salary, department_id, manager_id) VALUES 
('Charlie Lead', 'charlie@example.com', 180000, 1, 2),
('David Dev', 'david@example.com', 150000, 1, 3),
('Eve Dev', 'eve@example.com', 155000, 1, 3),
('Frank Dev', 'frank@example.com', 140000, 1, 3),
('Grace Intern', 'grace@example.com', 90000, 1, 3);

-- Sales
INSERT INTO Employee (name, email, salary, department_id, manager_id) VALUES 
('Hannah SalesMgr', 'hannah@example.com', 160000, 2, 1),
('Ian SalesRep', 'ian@example.com', 120000, 2, 8),
('Jack SalesRep', 'jack@example.com', 115000, 2, 8),
('Karen SalesRep', 'karen@example.com', 110000, 2, 8);

-- Duplicate Email Example for duplicate queries
INSERT INTO Employee (name, email, salary, department_id, manager_id) VALUES 
('David Duplicate', 'david@example.com', 80000, 1, 3);

-- Employees with duplicate salaries for ranking testing
INSERT INTO Employee (name, email, salary, department_id, manager_id) VALUES 
('Lucas Dev', 'lucas@example.com', 150000, 1, 3),    -- Same salary as David Dev
('Mia SalesRep', 'mia@example.com', 120000, 2, 8),   -- Same salary as Ian SalesRep
('Nina HR', 'nina@example.com', 110000, 4, 1),       -- Same salary as Karen SalesRep
('Oscar HR', 'oscar@example.com', 110000, 4, 1);     -- Three-way tie with Nina and Karen
