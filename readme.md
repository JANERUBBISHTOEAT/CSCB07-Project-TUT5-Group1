# CSCB07 Project

*Last updated: 2022-12-01*

## Notice

Always read the [agreement](https://github.com/JANERUBBISHTOEAT/CSCB07-Project-TUT5-Group1/blob/main/docs/agreement.txt) before you start working on the project. You can find the agreement in the `docs` folder.

## Team Members

- [Qihui Li](https://github.com/qihui-li)
- [Xinzi Huang](https://github.com/Chloekyuu)
- [Zhiyu Pan](https://github.com/Chloepan33)
- [Yibai Wang](https://github.com/kouldbegood)
- [Zheyuan Wei](https://github.com/JANERUBBISHTOEAT) [![wakatime](https://wakatime.com/badge/user/e96f5331-d426-487c-9075-815806be0f98/project/1b553542-f331-4f35-8b59-81a011a70183.svg)](https://wakatime.com/badge/user/e96f5331-d426-487c-9075-815806be0f98/project/1b553542-f331-4f35-8b59-81a011a70183)

## Current Sprint

- [x] [CSCB07T5 Sprint 1](https://zheyuan-wei.atlassian.net/secure/GHGoToBoard.jspa?sprintId=1)
  - [x] [As a student, I want to sign up and login to my account so that I can securely access my data.](https://zheyuan-wei.atlassian.net/browse/CSCB07T5G1-4)
  - [ ] [As a student, I want to view the list of courses I have taken and add to this list so that I can keep track of all the courses I have taken.](https://zheyuan-wei.atlassian.net/browse/CSCB07T5G1-6)
  - [ ] [As a student, I want to generate a course timeline by providing courses I want to take so that I can plan my education accordingly.](https://zheyuan-wei.atlassian.net/browse/CSCB07T5G1-7)

- [x] [CSCB07T5 Sprint 2](https://zheyuan-wei.atlassian.net/jira/software/projects/CSCB07T5G1/boards/1)
  - [x] As a student, I want to view the list of courses I have taken and add to this list so that I can keep track of all the courses I have taken.
  - [ ] As a student, I want to generate a course timeline by providing courses I want to take so that I can plan my education accordingly.
  - [x] As an admin, I want to login to my account so that I can securely manage course information.
  - [x] As an admin, I want to add a course and define its name, course code, offering sessions, and prerequisites so that a student’s timeline could be generated correctly.
  - [x] As an admin, I want to view the list of all courses and edit or delete any course in the list so that I can keep the course information up to date.

## User Stories

1. [x] As a student, I want to sign up and login to my account so that I can securely access my data.
2. [x] As a student, I want to view the list of courses I have taken and add to this list so that I can keep track of all the courses I have taken.
3. [ ] As a student, I want to generate a course timeline by providing courses I want to take so that I can plan my education accordingly.
4. [x] As an admin, I want to login to my account so that I can securely manage course information.
5. [x] As an admin, I want to add a course and define its name, course code, offering sessions, and prerequisites so that a student’s timeline could be generated correctly.
6. [x] As an admin, I want to view the list of all courses and edit or delete any course in the list so that I can keep the course information up to date.

## Project Description

You are required to develop an Android application that would help students plan their courses. Its core functionality is the generation of a timeline of courses which displays each course in the earliest session the  student may take,  according  to  restrictions  on  prerequisites  and  session offerings.  Two types  of  users  are  to  be  considered:  admin  and  student.  Admins  specify  course  offerings  and prerequisites, whereas students generate timelines by providing the list of courses they want to take and those they have already taken. The courses are assumed to be offered within 4-month sessions: Winter, Summer, and Fall (e.g., Winter 2023, Summer 2023, Fall 2023, Winter 2024, etc.).

**Course timeline example:**
Assume a student has  taken CSCA08, CSCA48, and CSCA67, and wants to take CSCC24 and CSCC63. Table 2 shows the expected course timeline based on the prerequisites and course offering information provided in Table 1.

| Course | Session Offerings | Prerequisites |
| --- | --- | --- |
| CSCC24 | Winter, Summer | CSCB07, CSCB09 |
| CSCB07 | Fall, Summer | CSCA48 |
| CSCB09 | Winter, Summer | CSCA48 |  
| CSCA48 | Winter, Summer | CSCA08 |
| CSCA08 | Fall, Winter | None |
| CSCC63 | Fall, Winter | CSCB63, CSCB36 |
| CSCB63 | Winter, Summer | CSCB36 |
| CSCB36 | Fall, Summer | CSCA48, CSCA67 |
| CSCA67 | Fall, Winter | None |
*Table 1. Course Offerings and Prerequisites*

| Session | Course |
| --- | --- |
| Fall | 2022 | CSCB07, CSCB36 |
| Winter | 2023 | CSCB09, CSCB63 |
| Summer | 2023 | CSCC24 |  
| Fall | 2023 | CSCC63 |
*Table 2. Expected Course Timeline*

## Project Requirements

1. Develop the application based on the user stories listed in the following section
2. Use Scrum for the application development process
    a. Choose a Scrum Master
    b. Conduct “Standup” meetings and document them
    c. Use Jira to keep track of the user stories and make sure your TA is added to the project. You can refer to this tutorial for more information.
    d. You should have 2-3 sprints.
3. Keep track of the changes made to the code using some version control system and make sure the TA has access to your project as well.
4. Test the application according to the instructions that will be posted on Nov 21st.
