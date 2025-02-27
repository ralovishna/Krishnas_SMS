package com.StudentMS;

import com.StudentMS.repository.StudentRepo;
import com.StudentMS.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.StudentMS"})
@EnableScheduling
public class MainApp {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    // CommandLineRunner bean for initializing data
//    @Bean
//    public CommandLineRunner demoData() {
//        return args -> {
//             Create students without manually setting IDs (let database handle it)
//            Student student1 = new Student(null, "Aman", "aman@.com", "aman123", "3245324532",1,null);
//            Student student2 = new Student(null, "Bikas", "bikas@.com", "bikas123", "24323432687",6,null);
//            Student student3 = new Student(null, "Chinmay", "chinmay@.com", "chinmay123", "30808876557",3,null);
//
//            // Save them to the repository
//            studentRepo.save(student1);
//            studentRepo.save(student2);
//            studentRepo.save(student1);
//             Create and save a teacher
//            Teacher teacher1 = new Teacher(null, "A", "teacher", "commerce", "12", "statistics", "a@gmail.com", "iamteacher", 45768645);
//            teacherRepo.save(teacher1);
//            System.out.println("Teacher saved successfully!");
//        };
//    }
}