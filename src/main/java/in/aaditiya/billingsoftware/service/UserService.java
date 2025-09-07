package in.aaditiya.billingsoftware.service;

import in.aaditiya.billingsoftware.io.UserRequest;
import in.aaditiya.billingsoftware.io.UserResponse;

import java.util.List;

public interface UserService {
   UserResponse createUser(UserRequest request);
   String getUserRole(String email);
   List<UserResponse> readUsers();
   void deleteUser(String id);
}
