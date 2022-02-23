package com.appsdeveloperblog.ws.clients.photoappwebclient.controller;

import com.appsdeveloperblog.ws.clients.photoappwebclient.model.AlbumDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class AlbumsController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public AlbumsController(OAuth2AuthorizedClientService authorizedClientService, RestTemplate restTemplate, WebClient webClient) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    @GetMapping("/albums")
    public String getAlbums(Model model, @RequestParam(required = false, defaultValue = "false") boolean useTemplate,
                            @AuthenticationPrincipal OidcUser principal) {

        OidcIdToken idToken = principal.getIdToken();
        //System.out.println("id token:" + idToken.getTokenValue());

        String url = "http://localhost:8089/albums";
        List<AlbumDto> albums;
        if (useTemplate) {
            albums = getAlbumsViaRestTemplate();
        } else {
            albums = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<AlbumDto>>() {})
                    .block();
        }
        model.addAttribute("albums", albums);

        return "albums";
    }

    private List<AlbumDto> getAlbumsViaRestTemplate() {
        System.out.println("use rest");
        //it's possible to use interceptor for restTemplate to add bearer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(), token.getName());
        String jtwAccessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();

        String url = "http://localhost:8089/albums";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jtwAccessToken);
        HttpEntity<List<AlbumDto>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<List<AlbumDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<AlbumDto>>() {
        });

        return responseEntity.getBody();
    }
}
