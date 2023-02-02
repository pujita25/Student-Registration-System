-- Team Members:
-- Team member 1: Akhil Parimi – B00866489 (CS532-02)
-- Team member 2: Nagalakshmi Prasanna Pujita Bodapati – B00929285 (CS532-02)

-- We have done this assignment completely on our own except for the tools/software acknowledged in the project report.
-- We have not copied it, nor have we given our solution to anyone else. We understand that if we are involved in plagiarism
-- or cheating, we will have to sign an official form that we have cheated and that this form will be stored in our official
-- university records. We also understand that we will receive a grade of 0 for the involved assignment and our grades will be
-- reduced by one level (e.g., from A to A- or from B+ to B) for our first offense, and that we will receive a grade of “F” for
-- the course for any additional offense of any kind.

-- Akhil Parimi
-- Pujita Bodapati


DROP SEQUENCE log#;
DROP TRIGGER insert_student_logs;
DROP TRIGGER insert_grad_enrollment_logs;
DROP TRIGGER delete_student_logs;
DROP TRIGGER delete_grad_enrollments_logs;
DROP TRIGGER student_enroll_update_class;
DROP TRIGGER drop_student_update_class;
DROP TRIGGER drop_stud_enrollment;

CREATE SEQUENCE log#
START WITH 1000
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER insert_student_logs
AFTER INSERT ON students
FOR EACH ROW
BEGIN
INSERT INTO logs VALUES(log#.NEXTVAL, USER, SYSDATE, 'students', 'insert', :NEW.b#);
END;
/

CREATE OR REPLACE TRIGGER insert_grad_enrollment_logs
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
INSERT INTO logs VALUES(log#.NEXTVAL, USER, SYSDATE, 'g_enrollments', 'insert', :NEW.g_b# || '	' || :NEW.classid);
END;
/

CREATE OR REPLACE TRIGGER delete_student_logs
AFTER DELETE ON students
FOR EACH ROW
BEGIN 
INSERT INTO logs VALUES(log#.NEXTVAL, USER, SYSDATE, 'students', 'delete', :OLD.b#);
END;
/

CREATE OR REPLACE TRIGGER delete_grad_enrollments_logs
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
INSERT INTO logs VALUES(log#.NEXTVAL, USER, SYSDATE, 'g_enrollments', 'delete', :OLD.g_b# || ' ' || :OLD.classid);
END;
/

CREATE OR REPLACE TRIGGER student_enroll_update_class
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
UPDATE classes SET class_size = class_size + 1 WHERE classes.classid = :NEW.classid;
END;
/

CREATE OR REPLACE TRIGGER drop_student_update_class
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN 
UPDATE classes SET class_size = class_size - 1 WHERE classes.classid = :OLD.classid;
END;
/

CREATE OR REPLACE TRIGGER drop_stud_enrollment
BEFORE DELETE ON students
FOR EACH ROW
BEGIN
DELETE FROM g_enrollments WHERE g_b# = :OLD.b#;
END;
/ 

show error;