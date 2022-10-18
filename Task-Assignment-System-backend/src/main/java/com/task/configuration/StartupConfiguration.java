package com.task.configuration;

import java.time.LocalDate;

import com.task.entities.User;
import com.task.services.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.*;

@Component
public class StartupConfiguration {
    @Autowired
	AdminService adminService;
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
		User admin = new User();
		admin.setFirstName("Admin");
		admin.setLastName("Account");
		admin.setEmail("admin@admin.com");
		admin.setPassword("Admin@123");
		admin.setRole("admin"); 
		admin.setSex("Male");
        admin.setBirthDate(LocalDate.now());
		adminService.addUser(admin);
    }
}

