package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;

    public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        List<UserDto> searchResult = null;
        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        ResponseEntity<ResponsePageDto<UserDto>> result = null;
        try {
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {
            };
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("#result - {}", result);
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /users {} ", e);
        }
        log.info("Ending request /users courseId {} ", courseId);
        return result.getBody();
//        return new PageImpl<>(searchResult);
    }

    public ResponseEntity<UserDto> getOneUserById(UUID userId){
        log.info("Buscando pelo usuário {} no serviço de userauth {}",userId , REQUEST_URL_AUTHUSER);
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        log.info("Enviando requisição do user {} para o serviços de users em {}.", userId, REQUEST_URL_AUTHUSER);
        String url = REQUEST_URL_AUTHUSER + "/users/"+ userId +"/courses/subscription";
        var courseUserDto = new CourseUserDto();
        courseUserDto.setUserId(userId);
        courseUserDto.setCourseId(courseId);
        restTemplate.postForObject(url, courseUserDto, String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        log.info("Enviando requisição para deleção de curso no AuthUser com CourseId {} em {}.", courseId, REQUEST_URL_AUTHUSER);
        String url = REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

    }
}
