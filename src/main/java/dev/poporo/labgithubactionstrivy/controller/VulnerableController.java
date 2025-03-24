package dev.poporo.labgithubactionstrivy.controller;

import dev.poporo.labgithubactionstrivy.entity.User;
import dev.poporo.labgithubactionstrivy.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class VulnerableController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    // SQL Injection 취약점
    @GetMapping("/users/search")
    public List<Map<String, Object>> searchUsers(@RequestParam String username) {
        // 취약: 사용자 입력을 직접 SQL 쿼리에 삽입
        String sql = "SELECT * FROM users WHERE username LIKE '%" + username + "%'";
        return jdbcTemplate.queryForList(sql);
    }

    // XSS(Cross-Site Scripting) 취약점
    @PostMapping("/comments")
    public String addComment(@RequestParam String comment) {
        // 취약: 사용자 입력을 이스케이프하지 않고 저장 및 반환
        return "<div class='comment'>" + comment + "</div>";
    }

    // CSRF(Cross-Site Request Forgery) 취약점
    // CSRF 보호가 비활성화된 상태에서 민감한 작업
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String userId, @RequestParam String newPassword) {
        // 취약: CSRF 토큰 검증 없음
        jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", newPassword, userId);
        return "Password updated successfully";
    }

    // 경로 순회(Path Traversal) 취약점
    @GetMapping("/files")
    public String readFile(@RequestParam String filename) {
        try {
            // 취약: 사용자 입력을 직접 파일 경로에 사용
            String content = new String(Files.readAllBytes(Paths.get("/app/files/" + filename)));
            return content;
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    // 안전하지 않은 파일 업로드
    @PostMapping("/upload")
    public String uploadFile(@RequestParam MultipartFile file) {
        try {
            // 취약: 파일 타입 검증 없이 저장
            String filename = file.getOriginalFilename();
            File dest = new File("/app/uploads/" + filename);
            file.transferTo(dest);
            return "File uploaded to " + dest.getAbsolutePath();
        } catch (IOException e) {
            return "Error uploading file: " + e.getMessage();
        }
    }

    // 민감한 정보 노출
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        // 취약: 사용자 비밀번호와 같은 민감한 정보가 포함된 전체 객체 반환
        return userRepository.findById(id).orElse(null);
    }
}
