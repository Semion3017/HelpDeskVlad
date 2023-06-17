INSERT INTO Categories (name) VALUES
('Application & Services'),
('Benefits & Paper Work'),
('Hardware & Software'),
('People Management'),
('Security & Access'),
('Workplaces & Facilities');
INSERT INTO users (first_name, last_name, role, email, password) VALUES
('owner1', 'owner1', 'OWNER', 'user1_mogilev@yopmail.com', '$2a$10$hX100lVRPiAEyO5znOhv0uByX/oWn1jn0WDWMV2mh78QEUWyBdYi6'),
('owner2', 'owner2', 'OWNER', 'user2_mogilev@yopmail.com', '$2a$10$n4rVuFpenHQBgwW3RHQW1OplglhBaq2ikVOcCXF2TT406trUw9qOS'),
('manager', 'manager', 'MANAGER', 'manager1_mogilev@yopmail.com', '$2a$10$WMlyRC3UXR9N5mtXGt27H.U6SXWcCM7ZUEOeSyW21.X99eB58LbvO'),
('manager', 'manager', 'MANAGER', 'manager2_mogilev@yopmail.com', '$2a$10$aDgw3sv6FWRBXRjhBfZDButw4MqNEennLbSoGBbdRBqrH2lldXvfW'),
('engineer1', 'engineer1', 'ENGINEER', 'engineer1_mogilev@yopmail.com', '$2a$10$z5Lspfiw/p4JMuhkvrxO1ekQpLskH4NHOXAUP8H2e6rxwt0Tp.9Om'),
('engineer2', 'engineer2', 'ENGINEER', 'engineer2_mogilev@yopmail.com', '$2a$10$JpZ62ub4.pCh0Rubf..69Oy4wW0mLUEQzsqMFVd54LzhC/SBMg3CC');
INSERT INTO tickets (name, urgency, urgency_number, created_on, desired_resolution_date, owner_id, state, category_id, approver_id )VALUES
('name1', 'CRITICAL', 1, '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
--('name2', 'AVERAGE', 2, '2023-06-13', '2023-06-16', 2, 'IN_PROGRESS', 1, 3),
('name3', 'LOW', 4, '2023-06-13', '2023-06-19', 2, 'APPROVED', 1, 3),
('name2', 'LOW', 4, '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'HIGH', 2, '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'LOW', 4 , '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3 , '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3, '2023-06-30', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'HIGH', 2 , '2023-06-24', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3 , '2023-06-27', '2023-06-27', 1, 'DRAFT', 1, null),
('name2', 'LOW', 4 , '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3 , '2023-06-19', '2023-06-19', 1, 'DRAFT', 1, null),
('name2', 'LOW', 4 , '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3 , '2023-06-16', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'LOW', 4 , '2023-06-13', '2023-06-23', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3 , '2023-06-18', '2023-06-18', 1, 'DRAFT', 1, null),
('name2', 'AVERAGE', 3 , '2023-06-13', '2023-06-13', 1, 'DRAFT', 1, null),
('name4', 'HIGH', 2, '2023-06-13', '2023-06-13', 1, 'CANCELED', 1, null);
