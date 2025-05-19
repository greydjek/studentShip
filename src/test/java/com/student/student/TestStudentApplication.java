package com.student.student;

import org.springframework.boot.SpringApplication;

public class TestStudentApplication {

	public static void main(String[] args) {
		SpringApplication.from(StudentApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
