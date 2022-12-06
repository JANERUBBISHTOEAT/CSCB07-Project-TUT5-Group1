# <center>CSCB07 - Final Project</center>

<p align="right"> Scrum Members: </br>
Qihui Li</br>
Xinzi Huang</br>
Zhiyu Pan</br>
Yibai Wang (Kevin Wang on Quercus)</br>
Zheyuan Wei (Scrum Master)</br>
</p>

---
Table of Contents

- [CSCB07 - Final Project](#cscb07---final-project)
  - [The main tasks done by each member of the group](#the-main-tasks-done-by-each-member-of-the-group)
    - [Qihui Li](#qihui-li)
    - [Xinzi Huang](#xinzi-huang)
    - [Zhiyu Pan](#zhiyu-pan)
    - [Yibai Wang](#yibai-wang)
    - [Zheyuan Wei (Scrum Master)](#zheyuan-wei-scrum-master)
  - [A summary of each of the Scrum meetings](#a-summary-of-each-of-the-scrum-meetings)
    - [Sprint 1 (Nov 16-Nov 22)](#sprint-1-nov-16-nov-22)
    - [Sprint 2 (Nov 23-Dec 2)](#sprint-2-nov-23-dec-2)
    - [No Sprint 3 (Cancelled as we finished all the tasks in Sprint 2)](#no-sprint-3-cancelled-as-we-finished-all-the-tasks-in-sprint-2)

[Link to GitHub (Private Project)](https://github.com/JANERUBBISHTOEAT/CSCB07-Project-TUT5-Group1)

---

## The main tasks done by each member of the group

### Qihui Li

- Implemented the `ViewAllCourses` activity for Admins to view all the courses in the database
- Implemented the `EditCourse` activity for Admins to edit the information of a course
- Implemented the `EditCourseList` activity (course_want adn course_taken) for Students to edit the list of courses they want to take or have taken
- Helped implement the `AddCourse` frontend for Admins to add a new course to the database

### Xinzi Huang

- Implemented the `LoginActivity` activity for the user (Students and Admins) to login
- Implemented the `SignupActivity` activity for the user (Students and Admins) to register
- Refactored the `LoginActivity` to reflect the knowledge learned in the course


### Zhiyu Pan

- Implemented the `SignupActivity` activity for the user (Students and Admins) to register
- JUnit tests for `LoginActivity`
- All Front-end UI design in Sprint 2 (mainly for `CourseTaken`, `CourseWanted`,  `AdminEdit`, `AdminHome`)
- Helped with refactoring the `LoginActivity` for the ease of testing

### Yibai Wang

- Designed the Database for Courses
- Implemented the `Planner` function for Students to plan their courses, which is the core functionality of our app
- Designed the `Planner` UI for Students to plan their courses

### Zheyuan Wei (Scrum Master)

- Daily Scrum Meeting and Sprint Planning Meeting, and corresponding documentation
- Basic Firebase, Android Project, and GitHub setup
- Designed the Database for Users (Students and Admins)
- Proposed the idea of using `md5` to encrypt the password for security, and using `md5 with salt` to avoid coincidence of the same `md5` value for different passwords
- Designed and implemented the Java `Course` class which is widely used in the project for basic processing of the course information, and storing or retrieving data from the database
- According to the `Firebase` asynchronous nature, designed and implemented the `getFromDatabase()` method to retrieve data from the database with maximum ease of use, and provided a sample usage (callback function) for the user to process the data on GitHub
- Implemented the `AddCourse` activity for Admins to add a new course to the database
- Solved all the merge conflicts in the project
- Found and fixed all the bugs in the project whenever a merge is done wrongly

---

## A summary of each of the Scrum meetings

### Sprint 1 (Nov 16-Nov 22)

- Discussion of the project idea and the breakdown of the user stories into tasks (For Sprint 1, there are 5 tasks in total)
- Splitting of the tasks by reaching a consensus on the workloads (Poker Planning) of each task
- Decisions on the database structure
- Decisions on Splitting whole project into 3 sprints

### Sprint 2 (Nov 23-Dec 2)

- Discussion of the progress of the tasks in Sprint 1
- Enhancement of the database structure
- Breakdown of the user stories into tasks (For Sprint 2, there are 12 tasks in total)
- Since we found that we overestimated the workload of the tasks in Sprint 1. Therefore, we decided to have 2 sprints in total, and this sprint has 12 tasks in total; We also decided to split the tasks into 2 parts: Front-end and Back-end
- Assessed the workload of each task and split the tasks by reaching a consensus on the workloads (Poker Planning) of each task
- Assigned the tasks to each member of the group according to continuity from the previous sprint, and the workload of each task

### No Sprint 3 (Cancelled as we finished all the tasks in Sprint 2)

- Cancelled as we finished all the tasks in Sprint 2

---
Edit: Zheyuan Wei (Scrum Master)
