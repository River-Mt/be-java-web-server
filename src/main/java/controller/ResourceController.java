package controller;

import http.ContentType;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SessionService;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static utils.FileIoUtils.load404ErrorFile;
import static utils.FileIoUtils.loadFile;

public class ResourceController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    private final SessionService sessionService;

    public ResourceController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, URISyntaxException {

        try {
            String path = httpRequest.getPath();
            ContentType contentType = ContentType.from(path);
            String filePath = contentType.getDirectory() + path;

            if (path.endsWith(".html")) {
                sendDynamicFile(httpRequest, httpResponse, filePath, contentType);
                return;
            }

            logger.debug("filePath: {}", filePath);
            byte[] body = loadFile(filePath);
            httpResponse.forward(HttpStatusCode.OK, contentType, body);

        } catch (IllegalArgumentException ex) {
            byte[] errorBody = load404ErrorFile();
            httpResponse.do404(errorBody);
        }

    }

    private void sendDynamicFile(
            HttpRequest httpRequest,
            HttpResponse httpResponse,
            String filePath,
            ContentType contentType
    ) throws IOException, URISyntaxException {
        try {
            String sessionId = httpRequest.getSessionId();
            sessionService.validateHasSession(sessionId);
            logger.info("Session ID {}", sessionId);

            byte[] body = FileIoUtils.replaceLoginBtnToUserName(sessionService.getUserName(sessionId), filePath);
            httpResponse.forward(HttpStatusCode.OK, contentType, body);

        } catch (IllegalArgumentException ex) {

            byte[] body = FileIoUtils.load404ErrorFile();
            httpResponse.do404(body);

        } catch (NullPointerException e) {

            byte[] body = FileIoUtils.loadFile(filePath);
            httpResponse.forward(HttpStatusCode.OK, contentType, body);

        }

    }

}
