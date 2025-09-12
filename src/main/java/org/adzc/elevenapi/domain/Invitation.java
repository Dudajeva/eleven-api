package org.adzc.elevenapi.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Invitation {
    private Long id;

    private Long userId;

    private String title;

    private String location;

    private String imageUrl;

    private Date createdAt;

    private Date updatedAt;

    private String content;

}