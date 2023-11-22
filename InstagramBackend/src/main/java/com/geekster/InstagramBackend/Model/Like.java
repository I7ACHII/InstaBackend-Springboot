package com.geekster.InstagramBackend.Model;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PostLike")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @ManyToOne
    @JoinColumn(name = "fk_instaPost_id")
    Post instaPost;

    @ManyToOne
    @JoinColumn(name = "fk_likers_id")
    User liker;
}
