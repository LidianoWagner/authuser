package com.lidiano.ead.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.lidiano.ead.dtos.CourseDTO;
import com.lidiano.ead.dtos.ResponsePageDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UserClient {

	@Autowired
    private RestTemplate restTemplate;

	@Value("${ead.api.url.course}")
    String BASE_URL_COURSE;

    public Page<CourseDTO> getAllCoursesByUser(UUID userid, Pageable pageable) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL_COURSE)
            .path("/courses")
            .queryParam("userid", userid)
            .queryParam("page", pageable.getPageNumber())
            .queryParam("size", pageable.getPageSize())
            .queryParam("sort", pageable.getSort().toString().replaceAll(": ", ","))
            .toUriString();

        log.debug("Requesting URL: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDto<CourseDTO>> responseType =
                new ParameterizedTypeReference<ResponsePageDto<CourseDTO>>() {};
            ResponseEntity<ResponsePageDto<CourseDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            List<CourseDTO> courses = response.getBody().getContent();
            return new PageImpl<>(courses, pageable, response.getBody().getTotalElements());
        } catch (HttpStatusCodeException e) {
            log.error("Error fetching courses for userid {}: {}", userid, e.getMessage());
            return Page.empty(pageable);
        }
    }
}
