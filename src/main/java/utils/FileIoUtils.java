package utils;

import model.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public final class FileIoUtils {

    private static final String ERROR_PATH = "./templates/error404.html";

    private FileIoUtils() {
    }

    public static byte[] loadFile(String filePath) throws IOException {
        try {
            Path path = Paths.get(FileIoUtils.class.getClassLoader().getResource(filePath).toURI());
            return Files.readAllBytes(path);
        } catch (NullPointerException | URISyntaxException e) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
    }

    public static byte[] load404ErrorFile() throws IOException, URISyntaxException {
        Path path = Paths.get(FileIoUtils.class.getClassLoader().getResource(ERROR_PATH).toURI());
        return Files.readAllBytes(path);

    }

    public static byte[] userListToString(Collection<User> userList, String filepath) throws IOException {
        StringBuilder sb = new StringBuilder();
        String fileData = new String(loadFile(filepath));

        int idx = 3;

        for (User user : userList) {
            sb.append("<tr>");
            sb.append("<th scope=\"row\">").append(idx).append("</th>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            sb.append("<tr>");
            idx++;
        }

        return fileData
                .replace("<!--userlist-->", sb.toString())
                .getBytes();
    }

    public static byte[] replaceLoginBtnToUserName(User user, String filepath) throws IOException {
        String fileData = new String(loadFile(filepath));
        String target_index = "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>";
        String target_others = "<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>";
        return fileData
                .replace("<!--username-->", String.format("<li><a>%s</a></li>", user.getName()))
                .replace(target_index, "")
                .replace(target_others, "")
                .getBytes();
    }

}
