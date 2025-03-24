package dev.poporo.labgithubactionstrivy.controller;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsafeController {

    // 안전하지 않은 역직렬화 예제
    @PostMapping("/unsafe")
    public Object unsafeDeserialization(@RequestBody byte[] data) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        return ois.readObject();  // 안전하지 않은 역직렬화
    }
}
