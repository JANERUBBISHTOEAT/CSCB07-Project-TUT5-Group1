# The Firebase functions use async methods

So if you want to do something with the returned data, you need to use following syntax:

```java
// A instance                                    Course Class
course_instance.getFromDatabase("course_id", new Course.MyCallback() {
    @Override
    //                            Another Instance
    public void onCallback(Course course_returned) {
        // Do something with the returned data
        // The data is returned in the course_returned variable
    }
});
```

A example of this is in the `TestActivity.java` file, as following:

```java
Course course = new Course();
course.getFromDatabase("CSCB07", new Course.MyCallback() {
    @Override
    public void onCallback(Course course_callback) {
        // set the course object
        course.setCourseName(course_callback.getCourseName());
        course.setCourseCode(course_callback.getCourseCode());
        course.setCourseDescription(course_callback.getCourseDescription());
        course.setSessionOffered(course_callback.getSessionOffered());
        course.setPreRequisiteCourses(course_callback.getPreRequisiteCourses());
        course.setVisible(course_callback.isVisible());
        // do something with the course object
        // Ex. toasts
        Toast.makeText(Test_Activity.this, course.getCourseCode(), Toast.LENGTH_SHORT).show();
        // do all the other stuff here
        // ...
    }
});
```