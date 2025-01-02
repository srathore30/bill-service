package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.UserReq;
import sfa.bill_service.dto.res.UserRes;
import sfa.bill_service.services.UserServices;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PostMapping("/createUser")
    public ResponseEntity<UserRes> createUser(@RequestBody UserReq userReq){
        UserRes userRes = userServices.createUser(userReq);
        return new ResponseEntity<>(userRes, HttpStatus.CREATED);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserRes> getUserById(@PathVariable Long id){
        UserRes userRes = userServices.getUserById(id);
        return new ResponseEntity<>(userRes, HttpStatus.CREATED);
    }

    @PutMapping("/updateUserById/{id}")
    public ResponseEntity<UserRes> updateUserById(@PathVariable Long id, @RequestBody UserReq userReq){
        UserRes userRes = userServices.updateUserById(id,userReq);
        return new ResponseEntity<>(userRes, HttpStatus.CREATED);
    }

    @GetMapping("/loginUser")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password){
        String res = userServices.loginUser(username, password);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
