package com.devrezaur.main.controller;
import com.devrezaur.main.model.RefreshToken;
import com.devrezaur.main.model.User;
import com.devrezaur.main.payload.JwtResponse;
import com.devrezaur.main.payload.RefreshTokenResponse;
import com.devrezaur.main.security.jwt.JwtUtils;
import com.devrezaur.main.security.jwt.RefreshTokenService;
import com.devrezaur.main.service.MyUserDetails;
import com.devrezaur.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public AuthController(UserService userService) {
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {
		Authentication auth = null;

		try {
			auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Incorrect credentials!");
		}

		MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
		final String jwt = jwtUtils.generateToken(myUserDetails);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(myUserDetails.getId());
		List<String> roles = myUserDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse("Bearer", jwt, refreshToken.getRefreshToken(), myUserDetails.getId(), myUserDetails.getFullname(),myUserDetails.getUsername() , roles));
	}

	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		User regUser = userService.findUserByUsername(user.getUsername());

		if(regUser != null)
			return ResponseEntity.badRequest().body("User already exists!");

		regUser = userService.saveUser(user);

		return ResponseEntity.ok().body(regUser);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@RequestBody Map<String, Long> userid) {
		refreshTokenService.deleteByUserId(userid.get("id"));
		return ResponseEntity.ok().body("User logged out");
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@RequestBody Map<String, String> refreshToken) {
		RefreshToken token = refreshTokenService.findByRefreshToken(refreshToken.get("token"));

		if(token != null && refreshTokenService.verifyExpiration(token) != null) {
			User user = token.getUser();
			Map<String, Object> claims = new HashMap<>();
			claims.put("ROLES", user.getRoles().stream().map(item -> item.getRole()).collect(Collectors.toList()));
			String jwt = jwtUtils.createToken(claims, user.getUsername());

			return ResponseEntity.ok(new RefreshTokenResponse("Bearer", jwt, refreshToken.get("token")));
		}

		return ResponseEntity.badRequest().body("Refresh token expired!");
	}

	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestParam String role) {
		// Generate a unique username based on existing usernames
		String username = generateUniqueUsername();

		// Generate a random password
		String password = generateRandomPassword();

		// Create a new user with the provided role, auto-generated username, and password
		User newUser = new User();
		newUser.setRole(role); // Assuming you have a method to set the role
		newUser.setUsername(username);
		newUser.setPassword(passwordEncoder.encode(password)); // Encrypt the password

		// Save the new user
		newUser = userService.saveUser(newUser);

		// Print message
		System.out.println("NEW USER DONE: " + newUser.getUsername());

		return ResponseEntity.ok().body(newUser);
	}

	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		// Check if the user exists
		User userToDelete = userService.findUserById(userId);
		if (userToDelete == null) {
			return ResponseEntity.badRequest().body("User not found!");
		}

		// Check if the authenticated user is an admin
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can delete users!");
		}

		// Delete the user
		userService.deleteUser(userId);
		return ResponseEntity.ok().body("User deleted successfully!");
	}

	// Method to check if the authenticated user is an admin
	private boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
	}

	// Method to generate a unique username based on existing usernames
	private String generateUniqueUsername() {
		// Logic to generate a unique username, for example: user1, user2, etc.
		int suffix = 1;
		String username;
		do {
			username = "user" + suffix++;
		} while (userService.findUserByUsername(username) != null); // Check if the username already exists

		return username;
	}

	// Method to generate a random password
	private String generateRandomPassword() {
		// Define the characters allowed in the password
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";

		// Initialize a random object
		Random random = new Random();

		// Initialize a StringBuilder to construct the password
		StringBuilder password = new StringBuilder();

		// Generate a random password with 8 characters
		for (int i = 0; i < 8; i++) {
			// Append a random character from the allowedChars string
			password.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
		}

		// Convert the StringBuilder to a String and return
		return password.toString();
	}
}
