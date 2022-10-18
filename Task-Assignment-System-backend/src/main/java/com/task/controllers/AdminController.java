package com.task.controllers;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.task.dto.Userdto;
import com.task.entities.*;
import com.task.services.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = { "http://localhost:8080" }, allowCredentials = "true")
@RestController
public class AdminController {

	@Autowired
	private ModelMapper modelMapper;
	
	private User convertToUser(Userdto userdto)
	{ 
		return modelMapper.map(userdto, User.class); 
		}
	
	public static final Logger logger=Logger.getLogger(AdminController.class.getName()); 
    AdminService adminService;
    @Autowired 
    AdminController(AdminService adminService){
        this.adminService=adminService;
    }

    @GetMapping(path="/admin/courses")
    public List<Course> getCourses(){
        return adminService.getCourses();
    }
    @GetMapping(path="/admin/user")
    public User getUser(@RequestParam Long id){
        return adminService.getUser(id);
    }
    @GetMapping(path = "/admin/students")
    public List<User> getStudents(){
        return adminService.getStudents();
    }
    @GetMapping(path = "/admin/teachers")
    public List<User> getTeachers(){
        return adminService.getTeachers();
    }
    @GetMapping(path = "/admin/admins")
    public List<User> getAdmins(){
        return adminService.getAdmins();
    }

    @PostMapping("/admin/user")
    public Long addUser (@RequestBody Userdto userdto)
    {
    	User user=convertToUser(userdto);
        return adminService.addUser(user);    
    }
    @PostMapping("/admin/changePassword")
    public boolean updateUser (@RequestParam Long id, @RequestParam String password){
        return adminService.changePassword(id, password);    
    }
    @PostMapping("/admin/course")
    public boolean addCourse (@RequestParam String code, @RequestParam String name){
        return adminService.addCourse(code, name);
    }

    @GetMapping(path = "admin/course/{code}")
    public Course getCourse(@PathVariable String code) {
        return adminService.getCourse(code);
        
    }
    @PostMapping(path = "admin/addToCourse")
    public String addUserToCourse(@RequestParam Long userId, @RequestParam String courseCode, @RequestParam String role) {
        return adminService.addUserToCourse(userId, courseCode, role);
    }

    @DeleteMapping(path = "admin/removeFromCourse")
    public boolean removeUserFromCourse(@RequestParam Long userId, @RequestParam String courseCode) {
        return adminService.removeUserFromCourse(userId, courseCode);
    }

    @DeleteMapping(path = "admin/user")
    public boolean deleteUser(@CookieValue("id") String cookieId
                            ,@RequestParam Long id){
        if(Long.valueOf(cookieId).equals(id))
            return false;
        logger.log(Level.INFO,"{0}",cookieId);
        return adminService.deleteUser(id);
    }

    @DeleteMapping(path = "admin/course")
    public boolean deleteCourse(@RequestParam String code){
        return adminService.deleteCourse(code);
    }

    @GetMapping(path = "admin/courseMembers")
    public List<User> getCourseMembers(@RequestParam String courseCode, @RequestParam String role){
        return adminService.getCourseMembers(courseCode, role);
    }

    @PutMapping(path = "admin/updateCourseName")
    public boolean updateCourseName (@RequestParam String courseCode, @RequestParam String courseName){
        return adminService.updateCourseName(courseCode, courseName);
    }

}


