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

SET serveroutput ON;

-- Creating packages for all the procedures that will be defined in the package body

create or replace package student_reg_system
as
TYPE student_cursor is ref cursor;

PROCEDURE display_all_students(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_courses(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_course_credits(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_classes(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_g_enrollments(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_score_grade(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_prerequisites(student_cursor OUT SYS_REFCURSOR);
PROCEDURE display_all_logs(student_cursor OUT SYS_REFCURSOR);
PROCEDURE get_stud_info_by_class(classid_in_param IN classes.classid%TYPE, err_message OUT varchar2, student_cursor OUT SYS_REFCURSOR);
PROCEDURE get_prereq_info_by_dept_course(dept_code_in_param IN courses.dept_code%TYPE, course#_in_param IN courses.course#%TYPE, err_message OUT varchar2, student_cursor OUT SYS_REFCURSOR);
PROCEDURE enroll_graduate_students(student_b#_param IN students.b#%TYPE, class_id_param IN classes.classid%TYPE, output_msg OUT VARCHAR2);
PROCEDURE drop_graduate_student(student_b#_param IN students.b#%TYPE, class_id_param IN classes.classid%TYPE, error_msg OUT VARCHAR2);
PROCEDURE delete_curr_student(student_b#_param in students.b#%type, error_msg  out varchar);

END;
/

-- Creating package body where main procedure with all the code will be written
CREATE OR REPLACE PACKAGE BODY student_reg_system AS
----------------------------------------------------------------------------------------------------------------------------------------
--*****Question 2 start-point*****
--Procedure to display all the students available in the db by calling the procedure "display_all_students"
PROCEDURE display_all_students(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM students;
    END display_all_students;

--Procedure to display all the courses available in the db by calling the procedure "display_all_courses"
PROCEDURE display_all_courses(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM courses;
    END display_all_courses;

--Procedure to display all the course_credit available in the db by calling the procedure "display_all_course_credits"
PROCEDURE display_all_course_credits(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM course_credit;
    END display_all_course_credits;

--Procedure to display all the classes available in the db by calling the procedure "display_all_classes"
PROCEDURE display_all_classes(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM classes;
    END display_all_classes;

--Procedure to display all the g_enrollments available in the db by calling the procedure "display_all_g_enrollments"
PROCEDURE display_all_g_enrollments(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM g_enrollments;
    END display_all_g_enrollments;

--Procedure to display all the score_grade available in the db by calling the procedure "display_all_score_grade"
PROCEDURE display_all_score_grade(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM score_grade;
    END display_all_score_grade;

--Procedure to display all the prerequisites available in the db by calling the procedure "display_all_prerequisites"
PROCEDURE display_all_prerequisites(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM prerequisites;
    END display_all_prerequisites;

--Procedure to display all the logs available in the db by calling the procedure "display_all_logs"
PROCEDURE display_all_logs(student_cursor OUT SYS_REFCURSOR)
as
BEGIN
OPEN student_cursor FOR
		SELECT * FROM logs;
    END display_all_logs;

--*****Question 2 end-point*****
----------------------------------------------------------------------------------------------------------------------------------------
--*****Question 3 start-point*****
-- Procedure to get all the students (B#, first_name, last_name) when given a classid as param, this procedure will return invalid classid IF the classid is not present in the classes table.
PROCEDURE get_stud_info_by_class(classid_in_param IN classes.classid%TYPE, err_message OUT varchar2, student_cursor OUT SYS_REFCURSOR) IS
	class_id_exists number;
	students_enrolled_classes number;
BEGIN
	SELECT COUNT(classid) INTO class_id_exists FROM classes WHERE classid = classid_in_param;
	SELECT COUNT(g.g_b#) INTO students_enrolled_classes FROM classes cl, g_enrollments g WHERE cl.classid = classid_in_param AND cl.classid = g.classid;

	IF class_id_exists = 0 THEN
		err_message := 'The class is invalid';
	ELSIF students_enrolled_classes = 0 THEN
		err_message := 'There are no students enrolled in the given class id';
	ELSE
		OPEN student_cursor FOR
		SELECT s.b#, s.first_name, s.last_name
		FROM classes cl, g_enrollments g, students s
		WHERE cl.classid = classid_in_param and g.g_b# = s.b# and g.classid = cl.classid;
	END IF;
END;
--*****Question 3 end-point*****
----------------------------------------------------------------------------------------------------------------------------------------
--*****Question 4 start-point*****
--Procedure to return all the prerequisite courses when the dept_code and course# are given as parameters 
PROCEDURE get_prereq_info_by_dept_course(dept_code_in_param IN courses.dept_code%TYPE, course#_in_param IN courses.course#%TYPE, err_message OUT varchar2, student_cursor OUT SYS_REFCURSOR) IS
 	dept_code_exists number;
BEGIN

SELECT COUNT(*) INTO dept_code_exists FROM courses WHERE dept_code = dept_code_in_param and course# = course#_in_param;

IF dept_code_exists = 0 THEN
		err_message := 'The dept_code || course# does not exist.';
ELSE
        open student_cursor for
            select pre_req_c.cour, pre_req_c.pre_req_cour, crs.title from courses crs inner join (
            select concat(pre.dept_code, pre.course#) as cour, connect_by_root concat(pre.pre_dept_code, pre.pre_course#) as pre_req_cour
            from prerequisites pre where concat(pre.dept_code, pre.course#) = concat(dept_code_in_param, course#_in_param) connect by prior concat(pre.dept_code, pre.course#)
            = concat(pre.pre_dept_code, pre.pre_course#)) pre_req_c on concat(crs.dept_code, crs.course#) = pre_req_c.pre_req_cour;
	END IF;
    END;
--*****Question 4 end-point*****
----------------------------------------------------------------------------------------------------------------------------------------
--*****Question 5 start-point*****
--Procedure to enroll a graduate student when given a student's b# and classid as parameters.
PROCEDURE enroll_graduate_students(student_b#_param IN students.b#%TYPE, class_id_param IN classes.classid%TYPE, output_msg OUT VARCHAR2) is
    student_id_exists number;
    class_id_exists number;
    grad_student_exists number;
	class_offerred_curr_sem number;
    class_size_full_exists number;
    student_already_in_class number;
    student_overload number;
	pre_req_course_comp number;
    BEGIN
        select COUNT(*) into student_id_exists from students where b# = student_b#_param;
        select COUNT(*) into grad_student_exists from g_enrollments where g_b# = student_b#_param;
        select COUNT(*) into class_id_exists from classes where classid = class_id_param;
		select COUNT(*) into class_offerred_curr_sem from classes where classid = class_id_param and year != 2021 and semester != 'Spring';
        select COUNT(*) into class_size_full_exists from classes where limit = class_size and classid = class_id_param;
        select COUNT(*) into student_already_in_class from g_enrollments where g_b# = student_b#_param and classid = class_id_param;
        select COUNT(*) into student_overload from classes c, g_enrollments g where g.classid = c.classid and c.classid = class_id_param and g.g_b# = student_b#_param and c.year = 2021 and c.semester = 'Spring';
        select COUNT(*) into pre_req_course_comp from classes c, prerequisites pre where c.classid = class_id_param and c.course# = pre.course# and pre.pre_course# not in (
  select c.course# from g_enrollments g, classes c, score_grade sc where g.g_b# = student_b#_param and g.classid = c.classid and g.score = sc.score and sc.lgrade in ('A', 'A-', 'B+', 'B', 'B-', 'C+', 'C')
);
        IF student_id_exists = 0 then
            output_msg := 'The B# is invalid.';
        ELSIF grad_student_exists = 0 then
			output_msg := 'This is not a graduate student.';
        ELSIF  class_id_exists = 0 then
            output_msg := 'The classid is invalid';
        ELSIF class_size_full_exists > 0 then
            output_msg := 'The class is already full.';
		ELSIF class_offerred_curr_sem > 0 then
			 output_msg := 'Cannot enroll into a class from a previous semester.';
        ELSIF student_already_in_class > 0 then
            output_msg := 'The student is already in the class';
        ELSIF student_overload >= 5 then
            output_msg := 'Students cannot be enrolled in more than five classes in the same semester.!';
		ELSIF pre_req_course_comp > 0 then
            output_msg := 'Prerequisite not satisfied.';
        ELSE
            insert into g_enrollments (g_b#, classid, score) values (student_b#_param, class_id_param, null);
            output_msg := 'Successfully enrolled student';
        END IF;
    END;


--*****Question 5 end-point*****
----------------------------------------------------------------------------------------------------------------------------------------
--*****Question 6 start-point*****
--Procedure to drop a graduate student when given student id and class id as parameters.
PROCEDURE drop_graduate_student(student_b#_param IN students.b#%TYPE, class_id_param IN classes.classid%TYPE, error_msg OUT VARCHAR2) is
    student_id_exists number;
	grad_student_exists number;
    class_id_exists number;
    student_enrollment_exists number;
    enroll_curr_sem number;
	total_courses_enrolled number;
    BEGIN
        select COUNT(*) into student_id_exists from students where b# = student_b#_param;
		select COUNT(*) into grad_student_exists from g_enrollments where g_b# = student_b#_param;
        select COUNT(*) into class_id_exists from classes where classid = class_id_param;
        select COUNT(*) into student_enrollment_exists from g_enrollments where g_b# = student_b#_param and classid = class_id_param;
        select COUNT(*) into enroll_curr_sem from classes c, g_enrollments g where c.classid=class_id_param and g.g_b# = student_b#_param and c.year != 2021 and c.semester != 'Spring' and c.classid = g.classid;
		select COUNT(*) into total_courses_enrolled from g_enrollments g, classes c where g.classid = c.classid and c.year = 2021 and c.semester='Spring' and g.g_b# = student_b#_param;


        error_msg := null;

        IF student_id_exists = 0 then
            error_msg := '“The B# is invalid';
		ELSIF grad_student_exists = 0 then
			error_msg := 'This is not a graduate student.';
        ELSIF class_id_exists = 0 then
            error_msg := 'The classid is invalid.';
        ELSIF student_enrollment_exists = 0 then
            error_msg := 'The student is not enrolled in the class.';
        ELSIF enroll_curr_sem > 0 then
            error_msg := 'Only enrollment in the current semester can be dropped.';
		ELSIF total_courses_enrolled = 1 then
			error_msg := 'This is the only class for this student in Spring 2021 and cannot be dropped.';
        ELSE
            delete from g_enrollments where g_b# = student_b#_param and classid = class_id_param;
            error_msg := 'Successfully removed student enrollment';
        END IF;
    END; 
--*****Question 6 end-point*****
----------------------------------------------------------------------------------------------------------------------------------------
--*****Question 7 start-point*****
--Procedure to delete a student from students table when b# is providede as the parameter.
PROCEDURE delete_curr_student(student_b#_param in students.b#%type, error_msg  out varchar) IS
    student_id_exists number;
    BEGIN
        select COUNT(*) into student_id_exists from students where b# = student_b#_param;
    
        IF student_id_exists = 0 then
            error_msg := 'The B# is invalid.';
        ELSE
            delete from students where b# = student_b#_param;
            
            error_msg := 'Successfully removed student';
        END IF;
    END;
--*****Question 7 end-point*****
----------------------------------------------------------------------------------------------------------------------------------------
END;
/
show error;
