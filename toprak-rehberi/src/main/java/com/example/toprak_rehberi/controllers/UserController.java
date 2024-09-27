package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.dto.GoogleLoginDto;
import com.example.toprak_rehberi.dto.PasswordResetRequest;
import com.example.toprak_rehberi.dto.UserLoginDto;
import com.example.toprak_rehberi.dto.UserRegistrationDto;
import com.example.toprak_rehberi.repos.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.toprak_rehberi.entities.User;
import com.example.toprak_rehberi.services.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Collections;
import java.util.Map;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        try {
            userService.registerUser(
                    userRegistrationDto.getUsername(),
                    userRegistrationDto.getPassword(),
                    userRegistrationDto.getEmail()
            );
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userDto) {
        User foundUser = userService.findByUsername(userDto.getUsername());
        if (foundUser != null && userService.checkPassword(userDto.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.ok(new LoginResponse("Login successful", foundUser.getId(), foundUser.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.ok(Map.of("message", "User not found"));
        }
        // Şifreyi şifreleyip güncelleme
        user.setPassword(userService.passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            // Uygulamanın çalışma dizininde uploads/profile-pictures/ dizinini oluştur
            String uploadDirectory = "uploads/profile-pictures/";
            Path uploadPath = Paths.get(uploadDirectory);

            // Dizin mevcut değilse oluştur
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Dosya adı kullanıcı ID'si ile ilişkilendiriliyor
            String fileName = userId + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Dosya dizine kaydediliyor
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Dosya yolunu kullanıcı bilgileriyle ilişkili olarak veritabanında saklama
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setProfilePicturePath(filePath.toString());
            userRepository.save(user);

            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<Resource> getProfilePicture(@RequestParam Long userId) {
        try {
            // Fetch user profile picture path from database
            String profilePicturePath = userService.getProfilePicturePathByUserId(userId);
            if (profilePicturePath == null) {
                return ResponseEntity.notFound().build();
            }

            // Construct the file path
            Path filePath = Paths.get(profilePicturePath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                System.out.println("File path: " + filePath.toString()); // Log file path
                System.out.println("File exists: " + resource.exists()); // Log existence check
                System.out.println("File is readable: " + resource.isReadable()); // Log readability check*/

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                        .body(resource);
            } else {
                System.out.println("File not found or not readable");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/api/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginDto googleLoginDto) {
        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("62095680164-d25snl2v32pfn550lujnrmtovg5cpt48.apps.googleusercontent.com"))  // Google Client ID
                    .build();

            GoogleIdToken idToken = verifier.verify(googleLoginDto.getTokenId());
            if (idToken != null) {
                // Google token doğrulandı, kullanıcıyı bul veya oluştur
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                // Eğer kullanıcı veritabanında varsa giriş yaptır
                User user = userRepository.findByEmail(email);
                if (user == null) {

                    user = new User();
                    user.setEmail(email);
                    user.setUsername((String) payload.get("name"));  // Google'dan alınan isim
                    userRepository.save(user);
                }

                return ResponseEntity.ok(new LoginResponse("Google login successful", user.getId(), user.getUsername()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google login failed");
        }
    }






}