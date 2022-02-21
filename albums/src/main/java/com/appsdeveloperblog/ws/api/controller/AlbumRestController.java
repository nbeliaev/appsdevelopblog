package com.appsdeveloperblog.ws.api.controller;

import com.appsdeveloperblog.ws.api.model.AlbumDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumRestController {

    @GetMapping
    public List<AlbumDto> getAlbums() {
        AlbumDto album1 = new AlbumDto();
        album1.setId("albumIdHere");
        album1.setUserId("1");
        album1.setTitle("Album 1 title");
        album1.setDescription("Album 1 description");
        album1.setUrl("Album 1 URL");

        AlbumDto album2 = new AlbumDto();
        album2.setId("albumIdHere");
        album2.setUserId("2");
        album2.setTitle("Album 2 title");
        album2.setDescription("Album 2 description");
        album2.setUrl("Album 2 URL");

        return List.of(album1, album2);
    }
}
