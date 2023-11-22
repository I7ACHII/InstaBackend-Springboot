package com.geekster.InstagramBackend.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.geekster.InstagramBackend.Model.enums.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String postContent;
    private String postCaption;
    private String postLocation;

//    @Enumerated(value = EnumType.STRING)
    private PostType postType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // hide this in json but not in database table column
    private LocalDateTime postCreatedTimeStamp;

    @ManyToOne
    @JoinColumn(name = "fk_user-Id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User postOwner;

}
