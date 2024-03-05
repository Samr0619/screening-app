package in.deloitte.screening.app.user.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.user.dto.PasswordUpdateRequest;
import in.deloitte.screening.app.user.entities.SignUpTable;
import in.deloitte.screening.app.user.entities.UserRoles;
import in.deloitte.screening.app.user.repositories.UserRolesRepository;
import in.deloitte.screening.app.user.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    UserRolesRepository userRoleRepository;
	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@GetMapping("/test")
	public ResponseEntity<?> getHello(){
		return ResponseEntity.ok("Hello, You are Authenticated");
	}
	
	@GetMapping("/getRoles")
	public ResponseEntity<?> getRoles(){
		List<UserRoles>list = userRoleRepository.findAll();
		
		return ResponseEntity.ok(list);
	}
	@PutMapping("/update-password")
	public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request) throws UserNotFoundException {
		logger.debug("API ::: /update-password");
		SignUpTable user = userService.updatePassword(request.getCurrentPassword(), request.getNewPassword(), request.getEmail());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
