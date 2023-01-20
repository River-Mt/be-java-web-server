package service;

import db.UserRepository;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class UserService {
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(Map<String, String> parameters) {
        User user = new User(
                parameters.get(USER_ID),
                parameters.get(PASSWORD),
                parameters.get(NAME),
                parameters.get(EMAIL)
        );
        userRepository.addUser(user);
    }

    public void validateUser(String requestUserId, String requestPassword) {
        User user = userRepository.findUserById(requestUserId);
        if (user == null || !requestPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("로그인 실패");
        }
    }

    public byte[] makeUserListBody(String filepath) throws IOException {
        Collection<User> userList = userRepository.findAll();
        return FileIoUtils.userListToString(userList, filepath);
    }


    public byte[] makeUserNameIndexBody(String id, String filepath) throws IOException {
        User user = userRepository.findUserById(id);
        return FileIoUtils.replaceLoginBtnToUserName(user, filepath);
    }

}
