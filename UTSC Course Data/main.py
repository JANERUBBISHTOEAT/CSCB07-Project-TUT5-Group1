# Get all the courses from given URL
# URL: https://utsc.calendar.utoronto.ca/search-courses?course_keyword=&field_program_area_value=Computer+Science&breadth=All&field_enrolment_limits_value=All&field_course_experience_value=All
# Output: courses.json

import json

import requests
from bs4 import BeautifulSoup

# Get the HTML from the URL
url0 = 'https://utsc.calendar.utoronto.ca/search-courses?course_keyword=&field_program_area_value=Computer+Science&breadth=All&field_enrolment_limits_value=All&field_course_experience_value=All'
url1 = 'https://utsc.calendar.utoronto.ca/search-courses?course_keyword=&field_program_area_value=Computer%20Science&breadth=All&field_enrolment_limits_value=All&field_course_experience_value=All&page=1'
page0 = requests.get(url0)
page1 = requests.get(url1)

# Parse the HTML
soup0 = BeautifulSoup(page0.text, 'html.parser')
soup1 = BeautifulSoup(page1.text, 'html.parser')

# Get Course List from HTML by div class view-content
course_list = soup0.find(class_='view-content')
course_list.extend(soup1.find(class_='view-content'))

# Get all the courses from the course list by div class views-row
courses = course_list.find_all(class_='views-row')

# Extract the course name, course prerequisites

# Create a list to store all the courses
course_list = []

# Loop through all the courses
for course in courses:
    # If js-views-accordion-group-header exists, then it is a course
    if course.find(class_='js-views-accordion-group-header'):
        pass
    else:
        continue
    # Get the course name
    course_name = course.contents[1].text.strip()
    course_name, course_title = course_name.split(': ')
    # Get the course description
    course_description = course.contents[3].text.strip().strip('\n')
    # Get the course prerequisites
    try:
        course_prerequisites = course.find('strong', text='Prerequisite: ').find_next_sibling().text.strip()
    except:
        course_prerequisites = ''
    # Create a dictionary to store the course name and course prerequisites
    course_dict = {
        'course_name': course_name,
        'course_title': course_title,
        'course_prerequisites': course_prerequisites
    }
    # Append the course dictionary to the course list
    course_list.append(course_dict)



# Write to json file
with open('courses.json', 'w') as f:
    json.dump(course_list, f, indent=2)

# Print the course list
# print(course_list)
